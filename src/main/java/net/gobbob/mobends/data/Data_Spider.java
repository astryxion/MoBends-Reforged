package net.gobbob.mobends.data;

import java.util.ArrayList;
import java.util.List;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.entity.ModelBendsSpider;
import net.gobbob.mobends.util.GUtil;
import net.gobbob.mobends.util.SmoothVector3f;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;

public class Data_Spider extends EntityData {
   public static List<Data_Spider> dataList = new ArrayList();
   public ModelRendererBends spiderHead;
   public ModelRendererBends spiderNeck;
   public ModelRendererBends spiderBody;
   public ModelRendererBends spiderLeg1;
   public ModelRendererBends spiderLeg2;
   public ModelRendererBends spiderLeg3;
   public ModelRendererBends spiderLeg4;
   public ModelRendererBends spiderLeg5;
   public ModelRendererBends spiderLeg6;
   public ModelRendererBends spiderLeg7;
   public ModelRendererBends spiderLeg8;
   public ModelRendererBends spiderForeLeg1;
   public ModelRendererBends spiderForeLeg2;
   public ModelRendererBends spiderForeLeg3;
   public ModelRendererBends spiderForeLeg4;
   public ModelRendererBends spiderForeLeg5;
   public ModelRendererBends spiderForeLeg6;
   public ModelRendererBends spiderForeLeg7;
   public ModelRendererBends spiderForeLeg8;
   public SmoothVector3f renderOffset = new SmoothVector3f();
   public SmoothVector3f renderRotation = new SmoothVector3f();
   public int currentWalkingState = 0;
   public Limb[] limbs;
   public float prevCrawlProgress = 0.0F;
   public float crawlProgress = 0.0F;
   public float startTransition = 1.0F;
   public boolean resetAfterJumped = false;
   public float deathWiggleSpeed = 1.0F;
   public float deathWigglePhase = 0.0F;
   public String currentAnim = "";
   /** 1.12.2 wall yaw (SOUTH=0, WEST=90, NORTH=180, EAST=270), or NaN if none. */
   public float wallYaw = Float.NaN;

   public Data_Spider(int argEntityID) {
      super(argEntityID);
      this.ensureLimbs();
   }

   public void ensureLimbs() {
      if (this.limbs == null) {
         this.limbs = new Limb[8];

         for(int i = 0; i < 8; ++i) {
            this.limbs[i] = new Limb(this, i);
         }
      }
   }

   public void syncModelInfo(ModelBendsSpider argModel) {
      if (this.spiderHead == null) {
         this.spiderHead = new ModelRendererBends(argModel);
      }

      this.spiderHead.sync((ModelRendererBends)argModel.spiderHead);
      if (this.spiderNeck == null) {
         this.spiderNeck = new ModelRendererBends(argModel);
      }

      this.spiderNeck.sync((ModelRendererBends)argModel.spiderNeck);
      if (this.spiderBody == null) {
         this.spiderBody = new ModelRendererBends(argModel);
      }

      this.spiderBody.sync((ModelRendererBends)argModel.spiderBody);
      if (this.spiderLeg1 == null) {
         this.spiderLeg1 = new ModelRendererBends(argModel);
      }

      this.spiderLeg1.sync((ModelRendererBends)argModel.spiderLeg1);
      if (this.spiderLeg2 == null) {
         this.spiderLeg2 = new ModelRendererBends(argModel);
      }

      this.spiderLeg2.sync((ModelRendererBends)argModel.spiderLeg2);
      if (this.spiderLeg3 == null) {
         this.spiderLeg3 = new ModelRendererBends(argModel);
      }

      this.spiderLeg3.sync((ModelRendererBends)argModel.spiderLeg3);
      if (this.spiderLeg4 == null) {
         this.spiderLeg4 = new ModelRendererBends(argModel);
      }

      this.spiderLeg4.sync((ModelRendererBends)argModel.spiderLeg4);
      if (this.spiderLeg5 == null) {
         this.spiderLeg5 = new ModelRendererBends(argModel);
      }

      this.spiderLeg5.sync((ModelRendererBends)argModel.spiderLeg5);
      if (this.spiderLeg6 == null) {
         this.spiderLeg6 = new ModelRendererBends(argModel);
      }

      this.spiderLeg6.sync((ModelRendererBends)argModel.spiderLeg6);
      if (this.spiderLeg7 == null) {
         this.spiderLeg7 = new ModelRendererBends(argModel);
      }

      this.spiderLeg7.sync((ModelRendererBends)argModel.spiderLeg7);
      if (this.spiderLeg8 == null) {
         this.spiderLeg8 = new ModelRendererBends(argModel);
      }

      this.spiderLeg8.sync((ModelRendererBends)argModel.spiderLeg8);
      if (this.spiderForeLeg1 == null) {
         this.spiderForeLeg1 = new ModelRendererBends(argModel);
      }

      this.spiderForeLeg1.sync(argModel.spiderForeLeg1);
      if (this.spiderForeLeg2 == null) {
         this.spiderForeLeg2 = new ModelRendererBends(argModel);
      }

      this.spiderForeLeg2.sync(argModel.spiderForeLeg2);
      if (this.spiderForeLeg3 == null) {
         this.spiderForeLeg3 = new ModelRendererBends(argModel);
      }

      this.spiderForeLeg3.sync(argModel.spiderForeLeg3);
      if (this.spiderForeLeg4 == null) {
         this.spiderForeLeg4 = new ModelRendererBends(argModel);
      }

      this.spiderForeLeg4.sync(argModel.spiderForeLeg4);
      if (this.spiderForeLeg5 == null) {
         this.spiderForeLeg5 = new ModelRendererBends(argModel);
      }

      this.spiderForeLeg5.sync(argModel.spiderForeLeg5);
      if (this.spiderForeLeg6 == null) {
         this.spiderForeLeg6 = new ModelRendererBends(argModel);
      }

      this.spiderForeLeg6.sync(argModel.spiderForeLeg6);
      if (this.spiderForeLeg7 == null) {
         this.spiderForeLeg7 = new ModelRendererBends(argModel);
      }

      this.spiderForeLeg7.sync(argModel.spiderForeLeg7);
      if (this.spiderForeLeg8 == null) {
         this.spiderForeLeg8 = new ModelRendererBends(argModel);
      }

      this.spiderForeLeg8.sync(argModel.spiderForeLeg8);
      this.renderOffset.set(argModel.renderOffset);
      this.renderRotation.set(argModel.renderRotation);
   }

   public static void add(Data_Spider argData) {
      dataList.add(argData);
   }

   public static Data_Spider get(int argEntityID) {
      for(int i = 0; i < dataList.size(); ++i) {
         if (((Data_Spider)dataList.get(i)).entityID == argEntityID) {
            return (Data_Spider)dataList.get(i);
         }
      }

      Data_Spider newData = new Data_Spider(argEntityID);
      if (Minecraft.getMinecraft().theWorld.getEntityByID(argEntityID) != null) {
         dataList.add(newData);
      }

      return newData;
   }

   public void update(float argPartialTicks) {
      super.update(argPartialTicks);
   }

   public void updateLimbs() {
      this.ensureLimbs();
      this.prevCrawlProgress = this.crawlProgress;
      this.crawlProgress += MathHelper.sqrt_float(this.motion.x * this.motion.x + this.motion.y * this.motion.y + this.motion.z * this.motion.z);
      this.wallYaw = this.calcWallYaw();

      for(int i = 0; i < this.limbs.length; ++i) {
         this.limbs[i].updateClient();
      }
   }

   public float getInterpolatedCrawlProgress(float partialTicks) {
      return this.prevCrawlProgress + (this.crawlProgress - this.prevCrawlProgress) * partialTicks;
   }

   public boolean isStillHorizontally() {
      return this.motion.x == 0.0F && this.motion.z == 0.0F;
   }

   public float calcWallYaw() {
      EntityLivingBase entity = this.getEntity();
      if (!(entity instanceof EntitySpider) || !((EntitySpider)entity).isBesideClimbableBlock()) {
         return Float.NaN;
      }

      int x = MathHelper.floor_double(entity.posX);
      int y = MathHelper.floor_double(entity.posY);
      int z = MathHelper.floor_double(entity.posZ);
      if (isSolid(entity, x, y, z - 1)) {
         return 180.0F;
      }

      if (isSolid(entity, x, y, z + 1)) {
         return 0.0F;
      }

      if (isSolid(entity, x - 1, y, z)) {
         return 90.0F;
      }

      if (isSolid(entity, x + 1, y, z)) {
         return 270.0F;
      }

      return Float.NaN;
   }

   private static boolean isSolid(EntityLivingBase entity, int x, int y, int z) {
      Block block = entity.worldObj.getBlock(x, y, z);
      return block != null && block != Blocks.air;
   }

   public void onLiftoff() {
      super.onLiftoff();
   }

   public static class Limb {
      private final Data_Spider data;
      public final int index;
      public final boolean odd;
      public final double neutralYaw;
      public final float upperX;
      public final float upperZ;
      private double worldX;
      private double worldZ;
      private double prevWorldX;
      private double prevWorldZ;
      private double adjustTargetX;
      private double adjustTargetZ;
      private float adjustingProgress = 1.0F;
      private float adjustingSpeed = 0.2F;

      public Limb(Data_Spider data, int index) {
         this.data = data;
         this.index = index;
         this.odd = index % 2 == 1;
         double yaw = (double)this.index / 7.0D * 2.0D - 1.0D;
         this.neutralYaw = this.odd ? yaw * 1.3D : Math.PI - yaw * 1.3D;
         this.upperX = this.odd ? 4.0F : -4.0F;
         this.upperZ = (float)(2 - index / 2);
         this.resetPosition();
      }

      public void resetPosition() {
         EntityLivingBase entity = this.data.getEntity();
         if (entity == null) {
            return;
         }

         float distance = 1.0F;
         float bodyYaw = entity.renderYawOffset / 180.0F * (float)Math.PI;
         this.worldX = Math.cos(this.neutralYaw + (double)bodyYaw) * (double)distance + entity.posX;
         this.worldZ = Math.sin(this.neutralYaw + (double)bodyYaw) * (double)distance + entity.posZ;
         this.prevWorldX = this.worldX;
         this.prevWorldZ = this.worldZ;
      }

      public void updateClient() {
         this.prevWorldX = this.worldX;
         this.prevWorldZ = this.worldZ;
         if (this.adjustingProgress < 1.0F) {
            this.adjustingProgress += this.adjustingSpeed;
            if (this.adjustingProgress >= 1.0F) {
               this.worldX = this.adjustTargetX;
               this.worldZ = this.adjustTargetZ;
               this.adjustingProgress = 1.0F;
            } else {
               this.worldX += (this.adjustTargetX - this.worldX) * 0.2D;
               this.worldZ += (this.adjustTargetZ - this.worldZ) * 0.2D;
            }
         }
      }

      public void adjustToNeutralPosition() {
         if (this.adjustingProgress != 1.0F) {
            return;
         }

         EntityLivingBase entity = this.data.getEntity();
         if (entity == null) {
            return;
         }

         this.adjustingSpeed = 0.2F;
         this.adjustingProgress = 0.0F;
         float distance = 1.2F;
         float bodyYaw = entity.renderYawOffset / 180.0F * (float)Math.PI;
         this.adjustTargetX = Math.cos(this.neutralYaw + (double)bodyYaw) * (double)distance + entity.posX;
         this.adjustTargetZ = Math.sin(this.neutralYaw + (double)bodyYaw) * (double)distance + entity.posZ;
      }

      public void adjustToLocalPosition(double x, double z, float adjustingSpeed) {
         if (this.adjustingProgress != 1.0F) {
            return;
         }

         EntityLivingBase entity = this.data.getEntity();
         if (entity == null) {
            return;
         }

         this.adjustingSpeed = adjustingSpeed;
         this.adjustingProgress = 0.0F;
         float bodyYaw = entity.renderYawOffset / 180.0F * (float)Math.PI;
         this.adjustTargetX = x * Math.cos((double)bodyYaw) - z * Math.sin((double)bodyYaw) + entity.posX;
         this.adjustTargetZ = x * Math.sin((double)bodyYaw) + z * Math.cos((double)bodyYaw) + entity.posZ;
      }

      public void setAngleAndDistance(float angle, float distance) {
         this.setLocalPosition(MathHelper.cos(angle) * distance + this.upperX * 0.0625F, MathHelper.sin(angle) * distance - this.upperZ * 0.0625F);
      }

      public void setLocalPosition(double x, double z) {
         EntityLivingBase entity = this.data.getEntity();
         if (entity == null) {
            return;
         }

         this.adjustingProgress = 1.0F;
         float bodyYaw = entity.renderYawOffset / 180.0F * (float)Math.PI;
         this.worldX = this.adjustTargetX = x * Math.cos((double)bodyYaw) - z * Math.sin((double)bodyYaw) + entity.posX;
         this.worldZ = this.adjustTargetZ = x * Math.sin((double)bodyYaw) + z * Math.cos((double)bodyYaw) + entity.posZ;
      }

      public IKResult solveIK(double bodyX, double bodyZ, float pt) {
         EntityLivingBase entity = this.data.getEntity();
         if (entity == null) {
            return new IKResult(0.0D, 0.0D, 0.0D);
         }

         double renderYawOffset = (double)((entity.prevRenderYawOffset + (entity.renderYawOffset - entity.prevRenderYawOffset) * pt) / 180.0F) * Math.PI;
         double spiderX = entity.prevPosX + (entity.posX - entity.prevPosX) * (double)pt;
         double spiderZ = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double)pt;
         double worldLimbX = this.prevWorldX + (this.worldX - this.prevWorldX) * (double)pt;
         double worldLimbZ = this.prevWorldZ + (this.worldZ - this.prevWorldZ) * (double)pt;
         double x = (worldLimbX - spiderX) / 0.0625D;
         double z = -(worldLimbZ - spiderZ) / 0.0625D;
         double localX = x * Math.cos(renderYawOffset) - z * Math.sin(renderYawOffset) - bodyX;
         double localZ = x * Math.sin(renderYawOffset) + z * Math.cos(renderYawOffset) - bodyZ;
         double deltaX = (double)this.upperX - localX;
         double deltaZ = (double)this.upperZ - localZ;
         double xzDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
         double xzAngle = Math.atan2(deltaX, deltaZ);
         return new IKResult(xzDistance, xzAngle, GUtil.getRadianDifference(this.neutralYaw, xzAngle + Math.PI / 2.0D));
      }

      public float getYawDegrees(IKResult result) {
         double xzAngle = this.odd ? (Math.PI / 2.0D + result.xzAngle) : (-Math.PI / 2.0D + result.xzAngle);
         return (float)(xzAngle / Math.PI * 180.0D);
      }

      public float getAdjustingProgress() {
         return this.adjustingProgress;
      }
   }

   public static class IKResult {
      public final double xzDistance;
      public final double xzAngle;
      public final double deviation;

      public IKResult(double xzDistance, double xzAngle, double deviation) {
         this.xzDistance = xzDistance;
         this.xzAngle = xzAngle;
         this.deviation = deviation;
      }
   }
}
