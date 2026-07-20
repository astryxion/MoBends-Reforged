package net.gobbob.mobends.animation.player;

import net.gobbob.mobends.animation.Animation;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.entity.ModelBendsPlayer;
import net.gobbob.mobends.data.Data_Player;
import net.gobbob.mobends.data.EntityData;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

/**
 * Backport of Mo'Bends 1.12.2 LadderClimbAnimationBit for 1.7.10.
 */
public class Animation_LadderClimb extends Animation {
   public String getName() {
      return "ladder_climb";
   }

   public void animate(EntityLivingBase argEntity, ModelBase argModel, EntityData argData) {
      ModelBendsPlayer model = (ModelBendsPlayer)argModel;
      Data_Player data = (Data_Player)argData;

      model.centerRotation.setSmoothX(0.0F, 0.3F);
      model.centerRotation.setY(0.0F);
      model.centerRotation.setZ(0.0F);

      float progress = data.climbingCycle;
      float armSwingRight = (float)Math.sin((double)progress) * 0.5F + 0.5F;
      float armSwingLeft = (float)Math.sin((double)progress + Math.PI) * 0.5F + 0.5F;
      float armSwingRight2 = (float)Math.sin((double)progress - 0.3D) * 0.5F + 0.5F;
      float armSwingLeft2 = (float)Math.sin((double)progress + Math.PI - 0.3D) * 0.5F + 0.5F;
      float armSwingDouble = (float)Math.sin((double)progress * 2.0D) * 0.5F + 0.5F;
      float armSwingDouble2 = (float)Math.sin((double)progress * 2.0D - 1.8D) * 0.5F + 0.5F;

      float legAnimationOffset = (float)Math.PI;
      float legSwingRight = (float)Math.sin((double)(progress + legAnimationOffset)) * 0.5F + 0.5F;
      float legSwingLeft = (float)Math.sin((double)(progress + legAnimationOffset) + Math.PI) * 0.5F + 0.5F;
      float legSwingRight2 = (float)Math.sin((double)(progress + legAnimationOffset + 0.3F)) * 0.5F + 0.5F;
      float legSwingLeft2 = (float)Math.sin((double)(progress + legAnimationOffset) + Math.PI + 0.3D) * 0.5F + 0.5F;

      float armOrientX = -45.0F;
      float climbingRotation = data.getClimbingRotation();
      float renderRotationY = MathHelper.wrapAngleTo180_float(argEntity.rotationYaw - model.headRotationY - climbingRotation);
      model.renderRotation.setSmoothY(renderRotationY, 0.6F);
      model.renderRotation.setSmoothX(0.0F, 0.6F);
      model.renderRotation.setSmoothZ(0.0F, 0.6F);
      model.renderOffset.setSmoothZ(armSwingDouble2, 0.6F);
      model.renderOffset.setSmoothX(0.0F, 0.6F);
      model.renderOffset.setSmoothY(-1.0F, 0.6F);

      ((ModelRendererBends)model.bipedBody).rotation.setSmoothX(armSwingDouble * 10.0F, 0.5F);
      ((ModelRendererBends)model.bipedBody).rotation.setSmoothY(0.0F, 0.5F);
      ((ModelRendererBends)model.bipedBody).rotation.setSmoothZ(0.0F, 0.5F);
      ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothX(-90.0F + armOrientX + armSwingRight * 70.0F, 0.5F);
      ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothX(-90.0F + armOrientX + armSwingLeft * 70.0F, 0.5F);
      ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothY(0.0F, 0.5F);
      ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothY(0.0F, 0.5F);
      ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothZ(0.0F, 0.5F);
      ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothZ(0.0F, 0.5F);
      ((ModelRendererBends)model.bipedRightForeArm).rotation.setSmoothX(armSwingRight2 * -80.0F, 0.5F);
      ((ModelRendererBends)model.bipedLeftForeArm).rotation.setSmoothX(armSwingLeft2 * -80.0F, 0.5F);

      ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothX(-45.0F - legSwingRight * 50.0F, 0.5F);
      ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothX(-45.0F - legSwingLeft * 50.0F, 0.5F);
      ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothY(0.0F, 0.5F);
      ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothY(0.0F, 0.5F);
      ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothZ(0.0F, 0.5F);
      ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothZ(0.0F, 0.5F);
      ((ModelRendererBends)model.bipedRightForeLeg).rotation.setSmoothX(20.0F + legSwingRight2 * 90.0F, 0.5F);
      ((ModelRendererBends)model.bipedLeftForeLeg).rotation.setSmoothX(20.0F + legSwingLeft2 * 90.0F, 0.5F);

      float headYaw = MathHelper.clamp_float(MathHelper.wrapAngleTo180_float(model.headRotationY + renderRotationY), -90.0F, 90.0F);
      ((ModelRendererBends)model.bipedHead).rotation.setSmoothX(model.headRotationX, 1.0F);
      ((ModelRendererBends)model.bipedHead).rotation.setSmoothY(headYaw, 1.0F);

      // 1.12.2 used 0.6, but that fires while a full ladder block is still above
      // (ledgeHeight is only the fractional Y when y+1 is climbable). Require being
      // on the last climbable block before the mantle/top-out pose.
      float ledgeClimbStart = 1.4F;
      if (data.getLedgeHeight() >= ledgeClimbStart) {
         float armRotX = data.getLedgeHeight() - ledgeClimbStart;
         ((ModelRendererBends)model.bipedBody).rotation.setSmoothX(armRotX * 50.0F, 0.5F);
         ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothX(-100.0F + armRotX * 40.0F, 0.5F);
         ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothX(-100.0F + armRotX * 40.0F, 0.5F);
         ((ModelRendererBends)model.bipedRightForeArm).rotation.setSmoothX(-10.0F, 0.5F);
         ((ModelRendererBends)model.bipedLeftForeArm).rotation.setSmoothX(-10.0F, 0.5F);
      }
   }
}
