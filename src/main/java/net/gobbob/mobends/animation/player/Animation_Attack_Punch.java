package net.gobbob.mobends.animation.player;

import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.entity.ModelBendsPlayer;
import net.gobbob.mobends.data.Data_Player;
import net.minecraft.entity.player.EntityPlayer;

public class Animation_Attack_Punch {
   public static void animate(EntityPlayer player, ModelBendsPlayer model, Data_Player data) {
      if (data.motion.x == 0.0F && data.motion.z == 0.0F) {
         model.renderRotation.setSmoothY(20.0F);
         model.renderOffset.setSmoothY(-2.0F);
         ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothX(-90.0F, 0.3F);
         ((ModelRendererBends)model.bipedRightForeArm).rotation.setSmoothX(-80.0F, 0.3F);
         ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothX(-90.0F, 0.3F);
         ((ModelRendererBends)model.bipedLeftForeArm).rotation.setSmoothX(-80.0F, 0.3F);
         ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothZ(20.0F, 0.3F);
         ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothZ(-20.0F, 0.3F);
         ((ModelRendererBends)model.bipedBody).rotation.setSmoothX(10.0F, 0.3F);
         ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothX(-30.0F, 0.3F);
         ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothX(-30.0F, 0.3F);
         ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothY(-25.0F, 0.3F);
         ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothZ(10.0F);
         ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothZ(-10.0F);
         ((ModelRendererBends)model.bipedRightForeLeg).rotation.setSmoothX(30.0F, 0.3F);
         ((ModelRendererBends)model.bipedLeftForeLeg).rotation.setSmoothX(30.0F, 0.3F);
         if (data.fistPunchArm) {
            ((ModelRendererBends)model.bipedRightArm).pre_rotation.setSmoothZ(100.0F, 0.9F);
            ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothX(-90.0F, 0.9F);
            ((ModelRendererBends)model.bipedRightArm).pre_rotation.setSmoothX(model.headRotationX, 0.9F);
            ((ModelRendererBends)model.bipedRightForeArm).rotation.setSmoothX(0.0F, 0.9F);
            ((ModelRendererBends)model.bipedBody).rotation.setSmoothY(-20.0F, 0.6F);
            ((ModelRendererBends)model.bipedHead).rotation.setSmoothY(model.headRotationY, 0.3F);
            ((ModelRendererBends)model.bipedHead).rotation.setSmoothX(model.headRotationX - 10.0F, 0.3F);
         } else {
            ((ModelRendererBends)model.bipedLeftArm).pre_rotation.setSmoothZ(-100.0F, 0.9F);
            ((ModelRendererBends)model.bipedLeftArm).pre_rotation.setSmoothY(30.0F, 0.9F);
            ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothX(-90.0F, 0.9F);
            ((ModelRendererBends)model.bipedLeftArm).pre_rotation.setSmoothX(model.headRotationX, 0.9F);
            ((ModelRendererBends)model.bipedLeftForeArm).rotation.setSmoothX(0.0F, 0.9F);
            ((ModelRendererBends)model.bipedBody).rotation.setSmoothY(20.0F, 0.6F);
            ((ModelRendererBends)model.bipedHead).rotation.setSmoothY(model.headRotationY - 20.0F - 20.0F, 0.3F);
            ((ModelRendererBends)model.bipedHead).rotation.setSmoothX(model.headRotationX - 10.0F, 0.3F);
         }
      } else {
         ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothX(-90.0F, 0.3F);
         ((ModelRendererBends)model.bipedRightForeArm).rotation.setSmoothX(-80.0F, 0.3F);
         ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothX(-90.0F, 0.3F);
         ((ModelRendererBends)model.bipedLeftForeArm).rotation.setSmoothX(-80.0F, 0.3F);
         ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothZ(20.0F, 0.3F);
         ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothZ(-20.0F, 0.3F);
         ((ModelRendererBends)model.bipedBody).rotation.setSmoothX(10.0F, 0.3F);
         ((ModelRendererBends)model.bipedBody).rotation.setSmoothY(0.0F, 0.3F);
         if (data.fistPunchArm) {
            ((ModelRendererBends)model.bipedRightArm).pre_rotation.setSmoothZ(100.0F, 0.9F);
            ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothX(-90.0F, 0.9F);
            ((ModelRendererBends)model.bipedRightArm).pre_rotation.setSmoothX(-20.0F + model.headRotationX, 0.9F);
            ((ModelRendererBends)model.bipedRightForeArm).rotation.setSmoothX(0.0F, 0.9F);
            ((ModelRendererBends)model.bipedBody).rotation.setSmoothY(-20.0F, 0.6F);
            ((ModelRendererBends)model.bipedHead).rotation.setSmoothY(model.headRotationY + 20.0F, 0.3F);
            ((ModelRendererBends)model.bipedHead).rotation.setSmoothX(model.headRotationX - 10.0F, 0.3F);
         } else {
            ((ModelRendererBends)model.bipedLeftArm).pre_rotation.setSmoothZ(-100.0F, 0.9F);
            ((ModelRendererBends)model.bipedLeftArm).pre_rotation.setSmoothY(-15.0F, 0.9F);
            ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothX(-90.0F, 0.9F);
            ((ModelRendererBends)model.bipedLeftArm).pre_rotation.setSmoothX(-20.0F + model.headRotationX, 0.9F);
            ((ModelRendererBends)model.bipedLeftForeArm).rotation.setSmoothX(0.0F, 0.9F);
            ((ModelRendererBends)model.bipedBody).rotation.setSmoothY(20.0F, 0.6F);
            ((ModelRendererBends)model.bipedHead).rotation.setSmoothY(model.headRotationY - 20.0F, 0.3F);
            ((ModelRendererBends)model.bipedHead).rotation.setSmoothX(model.headRotationX - 10.0F, 0.3F);
         }
      }

   }
}
