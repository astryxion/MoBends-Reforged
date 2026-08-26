package goblinbob.mobends.core.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.math.vector.Vector3f;
import goblinbob.mobends.core.data.EntityData;
import goblinbob.mobends.core.util.GlHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.LivingEntity;

public abstract class MutatedRenderer<T extends LivingEntity>
{

    protected final float scale = 0.0625F;
    protected final TextureManager textureManager;

    public MutatedRenderer()
    {
        textureManager = Minecraft.getInstance().getTextureManager();
    }

    public void beforeRender(EntityData<T> data, T entity, float partialTicks, MatrixStack poseStack)
    {

        poseStack.mulPose(Vector3f.YP.rotationDegrees(-interpolateRotation(entity.yBodyRotO, entity.yBodyRot, partialTicks)));

        this.renderLocalAccessories(entity, data, partialTicks, poseStack);

        float globalScale = entity.isBaby() ? getChildScale() : 1;

        if (globalScale != 1.0f)
        {
            poseStack.scale(globalScale, globalScale, globalScale);
        }

        poseStack.translate(data.globalOffset.getX() * scale,
                data.globalOffset.getY() * scale,
                data.globalOffset.getZ() * scale);
        float centerY = entity.getBbHeight() / (2.0f * globalScale);
        poseStack.translate(0, centerY, 0);
        GlHelper.rotate(poseStack, data.centerRotation.getSmooth());
        poseStack.translate(0, -centerY, 0);
        GlHelper.rotate(poseStack, data.renderRotation.getSmooth());

        poseStack.translate(data.localOffset.getX() * scale,
                data.localOffset.getY() * scale,
                data.localOffset.getZ() * scale);

        this.transformLocally(entity, data, partialTicks, poseStack);

        poseStack.mulPose(Vector3f.YP.rotationDegrees(interpolateRotation(entity.yBodyRotO, entity.yBodyRot, partialTicks)));
    }

    public void afterRender(T entity, float partialTicks, MatrixStack poseStack)
    {
    }

    protected void renderLocalAccessories(T entity, EntityData<?> data, float partialTicks, MatrixStack poseStack)
    {
    }

    protected void transformLocally(T entity, EntityData<?> data, float partialTicks, MatrixStack poseStack)
    {
    }

    protected static float interpolateRotation(float prevYawOffset, float yawOffset, float partialTicks)
    {
        float f;
        for (f = yawOffset - prevYawOffset; f < -180.0F; f += 360.0F) ;

        while (f >= 180.0F)
            f -= 360.0F;

        return prevYawOffset + partialTicks * f;
    }

    protected float getChildScale()
    {
        return 0.5F;
    }

}
