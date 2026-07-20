package hats.api;

import net.minecraft.entity.EntityLivingBase;

/**
 * Compile-time stub matching Hats' public API. Excluded from the Mo' Bends jar;
 * at runtime the Hats mod provides these classes.
 */
public abstract class RenderOnEntityHelper {
   public float renderTick;
   public int currentPass;

   public abstract Class helperForClass();

   public boolean canWearHat(EntityLivingBase living) {
      return true;
   }

   public boolean canUnlockHat(EntityLivingBase living) {
      return true;
   }

   public float getPrevRenderYaw(EntityLivingBase living) {
      return living.prevRenderYawOffset;
   }

   public float getRenderYaw(EntityLivingBase living) {
      return living.renderYawOffset;
   }

   public float getPrevRotationYaw(EntityLivingBase living) {
      return living.prevRotationYawHead;
   }

   public float getRotationYaw(EntityLivingBase living) {
      return living.rotationYawHead;
   }

   public float getPrevRotationPitch(EntityLivingBase living) {
      return living.prevRotationPitch;
   }

   public float getRotationPitch(EntityLivingBase living) {
      return living.rotationPitch;
   }

   public float getPrevRotationRoll(EntityLivingBase living) {
      return 0.0F;
   }

   public float getRotationRoll(EntityLivingBase living) {
      return 0.0F;
   }

   public float getRotatePointVert(EntityLivingBase ent) {
      return 0.0F;
   }

   public float getRotatePointHori(EntityLivingBase ent) {
      return 0.0F;
   }

   public float getRotatePointSide(EntityLivingBase ent) {
      return 0.0F;
   }

   public float getOffsetPointVert(EntityLivingBase ent) {
      return 0.0F;
   }

   public float getOffsetPointHori(EntityLivingBase ent) {
      return 0.0F;
   }

   public float getOffsetPointSide(EntityLivingBase ent) {
      return 0.0F;
   }

   public float getHatScale(EntityLivingBase ent) {
      return 1.0F;
   }

   public int passesNeeded() {
      return 1;
   }
}
