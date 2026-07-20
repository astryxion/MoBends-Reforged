package net.gobbob.mobends.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

/**
 * Port of MoBends 1.12.2 ArrowTrail for 1.7.10 (Tessellator / GL11).
 */
public class ArrowTrail {
   public static final int MAX_LENGTH = 10;
   public static final float SPAWN_INTERVAL = 1.0F;

   private final Minecraft mc;
   private final EntityArrow trackedArrow;
   private final TrailNode[] nodes;
   private float spawnCooldown = 0.0F;

   public ArrowTrail(EntityArrow arrow) {
      this.mc = Minecraft.getMinecraft();
      this.trackedArrow = arrow;
      this.spawnCooldown = SPAWN_INTERVAL;
      this.nodes = new TrailNode[MAX_LENGTH];
      this.resetNodes();
   }

   public void onRenderTick(float ticksPerFrame) {
      this.spawnCooldown += ticksPerFrame;
   }

   public void render(double x, double y, double z, float partialTicks) {
      if (this.spawnCooldown > 40.0F) {
         this.spawnCooldown = 0.0F;
         this.resetNodes();
      }

      while(this.spawnCooldown >= SPAWN_INTERVAL) {
         for(int i = MAX_LENGTH - 1; i > 0; --i) {
            this.nodes[i].moveTo(this.nodes[i - 1]);
         }

         this.nodes[0].moveTo(this.trackedArrow);
         this.spawnCooldown -= SPAWN_INTERVAL;
      }

      this.renderNodes(partialTicks);
   }

   public void resetNodes() {
      for(int i = 0; i < MAX_LENGTH; ++i) {
         this.nodes[i] = new TrailNode(this.trackedArrow);
      }
   }

   public void renderNodes(float partialTicks) {
      Entity viewEntity = this.mc.renderViewEntity;
      if (viewEntity == null) {
         return;
      }

      double viewX = viewEntity.lastTickPosX + (viewEntity.posX - viewEntity.lastTickPosX) * (double)partialTicks;
      double viewY = viewEntity.lastTickPosY + (viewEntity.posY - viewEntity.lastTickPosY) * (double)partialTicks;
      double viewZ = viewEntity.lastTickPosZ + (viewEntity.posZ - viewEntity.lastTickPosZ) * (double)partialTicks;

      GL11.glPushMatrix();
      GL11.glDisable(GL11.GL_TEXTURE_2D);
      GL11.glDisable(GL11.GL_LIGHTING);
      GL11.glDisable(GL11.GL_CULL_FACE);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);

      Tessellator tessellator = Tessellator.instance;
      tessellator.startDrawingQuads();

      for(int i = 1; i < MAX_LENGTH; ++i) {
         TrailNode node0 = this.nodes[i - 1];
         TrailNode node1 = this.nodes[i];
         double pos0x = node0.x - viewX;
         double pos0y = node0.y - viewY;
         double pos0z = node0.z - viewZ;
         double pos1x = node1.x - viewX;
         double pos1y = node1.y - viewY;
         double pos1z = node1.z - viewZ;
         float scale0 = (float)(MAX_LENGTH - i) / (float)MAX_LENGTH * 0.1F;
         float scale1 = (float)(MAX_LENGTH - i - 1) / (float)MAX_LENGTH * 0.1F;
         if (i == 1) {
            scale1 = 0.0F;
         }

         Vector3f up0 = node0.up;
         Vector3f right0 = node0.right;
         Vector3f up1 = node1.up;
         Vector3f right1 = node1.right;

         tessellator.addVertex(pos0x + (double)(-right0.x) * (double)scale0, pos0y + (double)(-right0.y) * (double)scale0, pos0z + (double)(-right0.z) * (double)scale0);
         tessellator.addVertex(pos0x + (double)right0.x * (double)scale0, pos0y + (double)right0.y * (double)scale0, pos0z + (double)right0.z * (double)scale0);
         tessellator.addVertex(pos1x + (double)right1.x * (double)scale1, pos1y + (double)right1.y * (double)scale1, pos1z + (double)right1.z * (double)scale1);
         tessellator.addVertex(pos1x + (double)(-right1.x) * (double)scale1, pos1y + (double)(-right1.y) * (double)scale1, pos1z + (double)(-right1.z) * (double)scale1);

         tessellator.addVertex(pos0x + (double)(-up0.x) * (double)scale0, pos0y + (double)(-up0.y) * (double)scale0, pos0z + (double)(-up0.z) * (double)scale0);
         tessellator.addVertex(pos0x + (double)up0.x * (double)scale0, pos0y + (double)up0.y * (double)scale0, pos0z + (double)up0.z * (double)scale0);
         tessellator.addVertex(pos1x + (double)up1.x * (double)scale1, pos1y + (double)up1.y * (double)scale1, pos1z + (double)up1.z * (double)scale1);
         tessellator.addVertex(pos1x + (double)(-up1.x) * (double)scale1, pos1y + (double)(-up1.y) * (double)scale1, pos1z + (double)(-up1.z) * (double)scale1);
      }

      tessellator.draw();
      GL11.glEnable(GL11.GL_CULL_FACE);
      GL11.glEnable(GL11.GL_TEXTURE_2D);
      GL11.glEnable(GL11.GL_LIGHTING);
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glPopMatrix();
   }

   public boolean shouldBeRemoved() {
      return this.mc.theWorld == null || this.trackedArrow.isDead;
   }

   static class TrailNode {
      public double x;
      public double y;
      public double z;
      public final Vector3f up = new Vector3f();
      public final Vector3f right = new Vector3f();

      TrailNode(EntityArrow arrow) {
         this.moveTo(arrow);
      }

      public void moveTo(TrailNode trailNode) {
         this.x = trailNode.x;
         this.y = trailNode.y;
         this.z = trailNode.z;
         this.up.set(trailNode.up);
         this.right.set(trailNode.right);
      }

      public void moveTo(EntityArrow arrow) {
         this.x = arrow.posX;
         this.y = arrow.posY;
         this.z = arrow.posZ;

         Vector3f forward = vectorFromPitchYaw(arrow.rotationPitch, arrow.rotationYaw);
         Vector3f upVec = vectorFromPitchYaw(arrow.rotationPitch + 90.0F, arrow.rotationYaw);
         this.up.set(-upVec.x, -upVec.y, upVec.z);
         cross(-forward.x, -forward.y, forward.z, this.up.x, this.up.y, this.up.z, this.right);
      }

      private static Vector3f vectorFromPitchYaw(float pitch, float yaw) {
         float f = MathHelper.cos(-yaw * 0.017453292F - (float)Math.PI);
         float f1 = MathHelper.sin(-yaw * 0.017453292F - (float)Math.PI);
         float f2 = -MathHelper.cos(-pitch * 0.017453292F);
         float f3 = MathHelper.sin(-pitch * 0.017453292F);
         return new Vector3f(f1 * f2, f3, f * f2);
      }

      private static void cross(float ax, float ay, float az, float bx, float by, float bz, Vector3f out) {
         out.x = ay * bz - az * by;
         out.y = az * bx - ax * bz;
         out.z = ax * by - ay * bx;
      }
   }
}
