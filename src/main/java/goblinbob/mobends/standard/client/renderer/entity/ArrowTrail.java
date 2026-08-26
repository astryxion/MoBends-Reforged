package goblinbob.mobends.standard.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import goblinbob.mobends.core.client.TrailRenderQueue;
import goblinbob.mobends.core.client.event.DataUpdateHandler;
import goblinbob.mobends.lib.math.vector.Vec3f;
import goblinbob.mobends.lib.math.vector.VectorUtils;
import goblinbob.mobends.standard.main.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.world.World;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector4f;

public class ArrowTrail
{
    public static final int MAX_LENGTH = 10;
    public static final float SPAWN_INTERVAL = 1;

    private final Minecraft mc;
    private AbstractArrowEntity trackedArrow;
    private TrailNode[] nodes;
    private float spawnCooldown = 0;

    public ArrowTrail(AbstractArrowEntity arrow)
    {
        this.mc = Minecraft.getInstance();
        this.trackedArrow = arrow;
        this.spawnCooldown = SPAWN_INTERVAL;
        this.nodes = new TrailNode[MAX_LENGTH];

        resetNodes();
    }

    public void onRenderTick()
    {
        spawnCooldown += DataUpdateHandler.ticksPerFrame;
    }

    public void render(MatrixStack poseStack, float partialTicks)
    {
        if (this.spawnCooldown > 40)
        {
            this.spawnCooldown = 0;
            resetNodes();
        }

        while (this.spawnCooldown >= SPAWN_INTERVAL)
        {
            for (int i = MAX_LENGTH - 1; i > 0; i--)
            {
                nodes[i].moveTo(nodes[i - 1]);
            }
            nodes[0].moveTo(trackedArrow);
            this.spawnCooldown -= SPAWN_INTERVAL;
        }

        renderNodes(poseStack, partialTicks);
    }

    public void resetNodes()
    {
        for (int i = 0; i < MAX_LENGTH; i++)
            this.nodes[i] = new TrailNode(trackedArrow);
    }

    public void renderNodes(MatrixStack poseStack, float partialTicks)
    {
        final double originX = MathHelper.lerp(partialTicks, trackedArrow.xOld, trackedArrow.getX());
        final double originY = MathHelper.lerp(partialTicks, trackedArrow.yOld, trackedArrow.getY());
        final double originZ = MathHelper.lerp(partialTicks, trackedArrow.zOld, trackedArrow.getZ());

        final Matrix4f matrix = poseStack.last().pose();
        final World level = trackedArrow.level;

        for (int i = 1; i < MAX_LENGTH; i++)
        {
            TrailNode node0 = nodes[i - 1];
            TrailNode node1 = nodes[i];

            Vector3d pos0 = new Vector3d(node0.x - originX, node0.y - originY, node0.z - originZ);
            Vector3d pos1 = new Vector3d(node1.x - originX, node1.y - originY, node1.z - originZ);
            float scale0 = ((float) (MAX_LENGTH - i)) / MAX_LENGTH * .1F;
            float scale1 = ((float) MAX_LENGTH - i - 1.0f) / MAX_LENGTH * .1F;
            if (i == 1)
            {
                scale1 = 0;
            }
            final Vec3f up0 = node0.up;
            final Vec3f right0 = node0.right;
            final Vec3f up1 = node1.up;
            final Vec3f right1 = node1.right;

            final int color0 = trailColor(level, node0);
            final int color1 = trailColor(level, node1);

            addVertex(matrix, pos0.x + (-right0.x) * scale0, pos0.y + (-right0.y) * scale0, pos0.z + (-right0.z) * scale0, color0);
            addVertex(matrix, pos0.x + (right0.x) * scale0, pos0.y + (right0.y) * scale0, pos0.z + (right0.z) * scale0, color0);
            addVertex(matrix, pos1.x + (right1.x) * scale1, pos1.y + (right1.y) * scale1, pos1.z + (right1.z) * scale1, color1);
            addVertex(matrix, pos1.x + (-right1.x) * scale1, pos1.y + (-right1.y) * scale1, pos1.z + (-right1.z) * scale1, color1);

            addVertex(matrix, pos0.x + (-up0.x) * scale0, pos0.y + (-up0.y) * scale0, pos0.z + (-up0.z) * scale0, color0);
            addVertex(matrix, pos0.x + (up0.x) * scale0, pos0.y + (up0.y) * scale0, pos0.z + (up0.z) * scale0, color0);
            addVertex(matrix, pos1.x + (up1.x) * scale1, pos1.y + (up1.y) * scale1, pos1.z + (up1.z) * scale1, color1);
            addVertex(matrix, pos1.x + (-up1.x) * scale1, pos1.y + (-up1.y) * scale1, pos1.z + (-up1.z) * scale1, color1);
        }
    }

    private static int trailColor(World level, TrailNode node)
    {
        if (ModConfig.arrowTrailFullBright)
        {
            return 0x80FFFFFF;
        }

        final int packedLight = WorldRenderer.getLightColor(level, new BlockPos(node.x, node.y, node.z));
        final int blockLight = LightTexture.block(packedLight);
        final int skyLight = Math.max(0, LightTexture.sky(packedLight) - level.getSkyDarken());
        final float brightness = 0.15F + 0.85F * (Math.max(blockLight, skyLight) / 15.0F);
        final int channel = (int) (brightness * 255.0F);

        return 0x80000000 | (channel << 16) | (channel << 8) | channel;
    }

    private static void addVertex(Matrix4f matrix, double x, double y, double z, int color)
    {
        Vector4f vec = new Vector4f((float) x, (float) y, (float) z, 1.0F);
        vec.transform(matrix);
        TrailRenderQueue.vertex(vec.x(), vec.y(), vec.z(), color);
    }

    public boolean shouldBeRemoved()
    {
        return mc.level == null || trackedArrow.removed;
    }

    static class TrailNode
    {
        public double x;
        public double y;
        public double z;

        public final Vec3f up;
        public final Vec3f right;

        TrailNode(AbstractArrowEntity arrow)
        {
            this.up = new Vec3f();
            this.right = new Vec3f();

            this.moveTo(arrow);
        }

        public void moveTo(TrailNode trailNode)
        {
            this.x = trailNode.x;
            this.y = trailNode.y;
            this.z = trailNode.z;
            this.up.set(trailNode.up);
            this.right.set(trailNode.right);
        }

        public void moveTo(AbstractArrowEntity arrow)
        {
            this.x = arrow.getX();
            this.y = arrow.getY();
            this.z = arrow.getZ();

            final Vector3d forward = arrow.getForward();

            float pitch = arrow.xRot;
            float yaw = arrow.yRot;
            float upPitch = pitch + 90F;

            float f = MathHelper.cos(-yaw * ((float)Math.PI / 180F) - (float)Math.PI);
            float f1 = MathHelper.sin(-yaw * ((float)Math.PI / 180F) - (float)Math.PI);
            float f2 = -MathHelper.cos(-upPitch * ((float)Math.PI / 180F));
            float f3 = MathHelper.sin(-upPitch * ((float)Math.PI / 180F));
            Vector3d up = new Vector3d(f1 * f2, f3, f * f2);

            this.up.set((float) -up.x, (float) -up.y, (float) up.z);

            VectorUtils.cross(
                    (float) -forward.x, (float) -forward.y, (float) forward.z,
                    this.up.x, this.up.y, this.up.z, this.right);
        }
    }
}
