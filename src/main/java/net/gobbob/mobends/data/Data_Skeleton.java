package net.gobbob.mobends.data;

import java.util.ArrayList;
import java.util.List;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.entity.ModelBendsSkeleton;
import net.gobbob.mobends.util.SmoothVector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class Data_Skeleton extends EntityData {
   public static List<Data_Skeleton> dataList = new ArrayList();
   public ModelRendererBends head;
   public ModelRendererBends headwear;
   public ModelRendererBends body;
   public ModelRendererBends rightArm;
   public ModelRendererBends leftArm;
   public ModelRendererBends rightLeg;
   public ModelRendererBends leftLeg;
   public ModelRendererBends rightForeArm;
   public ModelRendererBends leftForeArm;
   public ModelRendererBends rightForeLeg;
   public ModelRendererBends leftForeLeg;
   public SmoothVector3f renderOffset = new SmoothVector3f();
   public SmoothVector3f renderRotation = new SmoothVector3f();
   public boolean jumpPoseInitialized = false;
   /** 1.12.2 item-in-use ticks (0 idle, 20 fully drawn). 1.7.10 has no client draw flag. */
   public float bowDraw = 0.0F;

   public Data_Skeleton(int argEntityID) {
      super(argEntityID);
   }

   public void syncModelInfo(ModelBendsSkeleton argModel) {
      if (this.head == null) {
         this.head = new ModelRendererBends(argModel);
      }

      this.head.sync((ModelRendererBends)argModel.bipedHead);
      if (this.headwear == null) {
         this.headwear = new ModelRendererBends(argModel);
      }

      this.headwear.sync((ModelRendererBends)argModel.bipedHeadwear);
      if (this.body == null) {
         this.body = new ModelRendererBends(argModel);
      }

      this.body.sync((ModelRendererBends)argModel.bipedBody);
      if (this.rightArm == null) {
         this.rightArm = new ModelRendererBends(argModel);
      }

      this.rightArm.sync((ModelRendererBends)argModel.bipedRightArm);
      if (this.leftArm == null) {
         this.leftArm = new ModelRendererBends(argModel);
      }

      this.leftArm.sync((ModelRendererBends)argModel.bipedLeftArm);
      if (this.rightLeg == null) {
         this.rightLeg = new ModelRendererBends(argModel);
      }

      this.rightLeg.sync((ModelRendererBends)argModel.bipedRightLeg);
      if (this.leftLeg == null) {
         this.leftLeg = new ModelRendererBends(argModel);
      }

      this.leftLeg.sync((ModelRendererBends)argModel.bipedLeftLeg);
      if (this.rightForeArm == null) {
         this.rightForeArm = new ModelRendererBends(argModel);
      }

      this.rightForeArm.sync((ModelRendererBends)argModel.bipedRightForeArm);
      if (this.leftForeArm == null) {
         this.leftForeArm = new ModelRendererBends(argModel);
      }

      this.leftForeArm.sync((ModelRendererBends)argModel.bipedLeftForeArm);
      if (this.rightForeLeg == null) {
         this.rightForeLeg = new ModelRendererBends(argModel);
      }

      this.rightForeLeg.sync((ModelRendererBends)argModel.bipedRightForeLeg);
      if (this.leftForeLeg == null) {
         this.leftForeLeg = new ModelRendererBends(argModel);
      }

      this.leftForeLeg.sync((ModelRendererBends)argModel.bipedLeftForeLeg);
      this.renderOffset.set(argModel.renderOffset);
      this.renderRotation.set(argModel.renderRotation);
   }

   public static void add(Data_Skeleton argData) {
      dataList.add(argData);
   }

   public static Data_Skeleton get(int argEntityID) {
      for(int i = 0; i < dataList.size(); ++i) {
         if (((Data_Skeleton)dataList.get(i)).entityID == argEntityID) {
            return (Data_Skeleton)dataList.get(i);
         }
      }

      Data_Skeleton newData = new Data_Skeleton(argEntityID);
      if (Minecraft.getMinecraft().theWorld.getEntityByID(argEntityID) != null) {
         dataList.add(newData);
      }

      return newData;
   }

   public void onLiftoff() {
      super.onLiftoff();
      this.jumpPoseInitialized = false;
   }

   /**
    * 1.12.2 uses {@code isSwingingArms()} / item-in-use. 1.7.10 never syncs that, so treat
    * "looking at a player or iron golem in bow range" as drawing.
    */
   public static boolean isAimingBow(EntitySkeleton skeleton) {
      if (skeleton == null || skeleton.worldObj == null || skeleton.getSkeletonType() == 1) {
         return false;
      }

      ItemStack held = skeleton.getHeldItem();
      if (held == null || !(held.getItem() instanceof ItemBow)) {
         return false;
      }

      List players = skeleton.worldObj.playerEntities;
      for(int i = 0; i < players.size(); ++i) {
         EntityPlayer player = (EntityPlayer)players.get(i);
         if (isValidBowTarget(skeleton, player)) {
            return true;
         }
      }

      List loaded = skeleton.worldObj.loadedEntityList;
      for(int i = 0; i < loaded.size(); ++i) {
         Object obj = loaded.get(i);
         if (obj instanceof EntityIronGolem && isValidBowTarget(skeleton, (EntityLivingBase)obj)) {
            return true;
         }
      }

      return false;
   }

   private static boolean isValidBowTarget(EntitySkeleton skeleton, EntityLivingBase target) {
      if (target == null || target.isDead) {
         return false;
      }

      if (target instanceof EntityPlayer && ((EntityPlayer)target).capabilities.disableDamage) {
         return false;
      }

      if (skeleton.getDistanceSqToEntity(target) > 225.0D) {
         return false;
      }

      if (!skeleton.canEntityBeSeen(target)) {
         return false;
      }

      Vec3 look = skeleton.getLook(1.0F);
      double dx = target.posX - skeleton.posX;
      double dy = target.posY + (double)target.getEyeHeight() - (skeleton.posY + (double)skeleton.getEyeHeight());
      double dz = target.posZ - skeleton.posZ;
      double len = Math.sqrt(dx * dx + dy * dy + dz * dz);
      if (len < 0.001D) {
         return true;
      }

      double dot = (look.xCoord * dx + look.yCoord * dy + look.zCoord * dz) / len;
      return dot > 0.55D;
   }

   /**
    * Port of 1.12.2 {@code EntityData.isStrafing()} (movement angle vs look, 30° dead zone).
    */
   public boolean isStrafing() {
      EntityLivingBase entity = this.getEntity();
      if (entity == null) {
         return false;
      }

      float deadZone = 0.0025F;
      if (this.motion.x * this.motion.x + this.motion.z * this.motion.z < deadZone) {
         return false;
      }

      Vec3 look = entity.getLook(1.0F);
      float lookAngle = (float)(Math.atan2(look.xCoord, look.zCoord) / Math.PI * 180.0D);
      float moveAngle = (float)(Math.atan2((double)this.motion.x, (double)this.motion.z) / Math.PI * 180.0D);
      float angle = MathHelper.wrapAngleTo180_float(moveAngle - lookAngle);
      float threshold = 30.0F;
      return angle >= threshold && angle <= 180.0F - threshold || angle >= -180.0F + threshold && angle <= -threshold;
   }
}
