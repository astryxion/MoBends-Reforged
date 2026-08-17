package net.gobbob.mobends.animation.player;

import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.entity.ModelBendsPlayer;
import net.gobbob.mobends.data.Data_Player;
import net.gobbob.mobends.util.GUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;

/**
 * 1.12.2 sword poses applied in 1.7.10's native space.
 * 1.12.2 {@code orientZ(z).rotateY(y)} is the old combo's pre_rotation.Z + rotation.X slash,
 * not Euler Y (that twisted the arm instead of swinging the sword).
 */
final class Animation_Attack_Sword {
   private Animation_Attack_Sword() {
   }

   static boolean isStillHorizontally(Data_Player data) {
      return data.motion.x == 0.0F && data.motion.z == 0.0F;
   }

   static float clamp01(float value) {
      if (value < 0.0F) {
         return 0.0F;
      }
      return GUtil.max(value, 1.0F);
   }

   static boolean holdingSword(EntityPlayer player) {
      return player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ItemSword;
   }

   /**
    * Torso uses 1.12.2 body X/Y. Head looks at the camera in that twisted space
    * ({@code look - body}), so the chest turns but you never see your own face.
    */
   static void applyBodyAndHead(ModelBendsPlayer model, float bodyX, float bodyY, float smoothness) {
      applyBodyAndHead(model, bodyX, bodyY, 0.0F, smoothness);
   }

   static void applyBodyAndHead(ModelBendsPlayer model, float bodyX, float bodyY, float headYawExtra, float smoothness) {
      ((ModelRendererBends)model.bipedBody).rotation.setSmoothX(bodyX, smoothness);
      ((ModelRendererBends)model.bipedBody).rotation.setSmoothY(bodyY, smoothness);
      ((ModelRendererBends)model.bipedBody).rotation.setSmoothZ(0.0F, smoothness);
      ((ModelRendererBends)model.bipedHead).rotation.setSmoothX(model.headRotationX - bodyX, smoothness);
      ((ModelRendererBends)model.bipedHead).rotation.setSmoothY(model.headRotationY - bodyY + headYawExtra, smoothness);
      ((ModelRendererBends)model.bipedHead).rotation.setSmoothZ(0.0F, smoothness);
      ((ModelRendererBends)model.bipedHead).pre_rotation.setSmoothZero(smoothness);
   }

   /** 1.12.2 {@code orientZ(preZ).rotateY(rotX)} → raise with Z, slash with X. */
   static void setMainArm(ModelBendsPlayer model, float preZ, float rotX, float smoothness) {
      ModelRendererBends arm = (ModelRendererBends)model.bipedRightArm;
      arm.pre_rotation.setSmoothX(0.0F, smoothness);
      arm.pre_rotation.setSmoothY(0.0F, smoothness);
      arm.pre_rotation.setSmoothZ(preZ, smoothness);
      // Original combos used 3.0 so the 180° chop snaps; 0.9 left the sword lagging.
      arm.rotation.setSmoothX(rotX, 3.0F);
      arm.rotation.setSmoothY(0.0F, smoothness);
      arm.rotation.setSmoothZ(0.0F, smoothness);
   }

   static void setMainArmInstantX(ModelBendsPlayer model, float preZ, float rotX, float smoothness) {
      ModelRendererBends arm = (ModelRendererBends)model.bipedRightArm;
      arm.pre_rotation.setSmoothX(0.0F, smoothness);
      arm.pre_rotation.setSmoothY(0.0F, smoothness);
      arm.pre_rotation.setSmoothZ(preZ, smoothness);
      arm.rotation.setX(rotX);
      arm.rotation.setSmoothY(0.0F, smoothness);
      arm.rotation.setSmoothZ(0.0F, smoothness);
   }

   /** 1.12.2 {@code orientZ(preZ)} on the off hand. */
   static void setOffArm(ModelBendsPlayer model, float preZ, float smoothness) {
      ModelRendererBends arm = (ModelRendererBends)model.bipedLeftArm;
      arm.pre_rotation.setSmoothX(0.0F, smoothness);
      arm.pre_rotation.setSmoothY(0.0F, smoothness);
      arm.pre_rotation.setSmoothZ(preZ, smoothness);
      arm.rotation.setSmoothX(0.0F, smoothness);
      arm.rotation.setSmoothY(0.0F, smoothness);
      arm.rotation.setSmoothZ(0.0F, smoothness);
   }

   static void setForeArms(ModelBendsPlayer model, float rightX, float leftX, float smoothness) {
      ((ModelRendererBends)model.bipedRightForeArm).rotation.setSmoothX(rightX, smoothness);
      ((ModelRendererBends)model.bipedRightForeArm).rotation.setSmoothY(0.0F, smoothness);
      ((ModelRendererBends)model.bipedRightForeArm).rotation.setSmoothZ(0.0F, smoothness);
      ((ModelRendererBends)model.bipedLeftForeArm).rotation.setSmoothX(leftX, smoothness);
      ((ModelRendererBends)model.bipedLeftForeArm).rotation.setSmoothY(0.0F, smoothness);
      ((ModelRendererBends)model.bipedLeftForeArm).rotation.setSmoothZ(0.0F, smoothness);
   }

   static void applyStillKneelLegs(ModelBendsPlayer model, float smoothness) {
      ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothX(-30.0F, smoothness);
      ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothY(0.0F, smoothness);
      ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothZ(10.0F, smoothness);
      ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothX(-30.0F, smoothness);
      ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothY(-25.0F, smoothness);
      ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothZ(-10.0F, smoothness);
      ((ModelRendererBends)model.bipedRightForeLeg).rotation.setSmoothX(30.0F, smoothness);
      ((ModelRendererBends)model.bipedLeftForeLeg).rotation.setSmoothX(30.0F, smoothness);
   }
}
