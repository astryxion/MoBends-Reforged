package net.gobbob.mobends.animation.player;

import net.gobbob.mobends.animation.Animation;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.entity.ModelBendsPlayer;
import net.gobbob.mobends.data.EntityData;
import net.gobbob.mobends.util.GUtil;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

/**
 * Port of MoBends 1.12.2 biped/player SprintAnimationBit.
 */
public class Animation_Sprint extends Animation {
   public String getName() {
      return "sprint";
   }

   public void animate(EntityLivingBase argEntity, ModelBase argModel, EntityData argData) {
      ModelBendsPlayer model = (ModelBendsPlayer)argModel;

      float PI = (float)Math.PI;
      // 1.12.2: limbSwing * 0.6662F * 0.8F (slightly slower cadence than walk)
      float limbSwing = model.armSwing * 0.6662F * 0.8F;
      float armSwingAmount = model.armSwingAmount / PI * 180.0F * 1.1F;

      ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothX(MathHelper.cos(limbSwing + PI) * armSwingAmount, 0.8F);
      ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothX(MathHelper.cos(limbSwing) * armSwingAmount, 0.8F);
      ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothZ(5.0F, 0.8F);
      ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothZ(-5.0F, 0.8F);

      float legSwingAmount = 1.26F * model.armSwingAmount / PI * 180.0F;
      ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothX(-5.0F + MathHelper.cos(limbSwing) * legSwingAmount, 1.0F);
      ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothX(-5.0F + MathHelper.cos(limbSwing + PI) * legSwingAmount, 1.0F);
      ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothY(0.0F);
      ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothY(0.0F);
      ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothZ(2.0F, 1.0F);
      ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothZ(-2.0F, 1.0F);

      // Continuous foreleg bend (not the old step 45/0 flip)
      float foreLegSwingAmount = 0.7F * model.armSwingAmount / PI * 180.0F;
      float var = (limbSwing / PI) % 2.0F;
      ((ModelRendererBends)model.bipedLeftForeLeg).rotation.setSmoothX(40.0F + MathHelper.cos(limbSwing + 1.8F) * foreLegSwingAmount, 0.7F);
      ((ModelRendererBends)model.bipedRightForeLeg).rotation.setSmoothX(40.0F + MathHelper.cos(limbSwing + PI + 1.8F) * foreLegSwingAmount, 0.7F);
      ((ModelRendererBends)model.bipedLeftForeArm).rotation.setSmoothX(var > 1.0F ? -10.0F : -45.0F, 0.3F);
      ((ModelRendererBends)model.bipedRightForeArm).rotation.setSmoothX(var > 1.0F ? -45.0F : -10.0F, 0.3F);

      float bodyRotationY = MathHelper.cos(limbSwing) * -40.0F;
      float bodyRotationX = MathHelper.cos(limbSwing * 2.0F) * 10.0F + 10.0F;
      float var10 = model.headRotationY * 0.3F;
      var10 = GUtil.max(var10, 10.0F);
      var10 = GUtil.min(var10, -10.0F);

      ((ModelRendererBends)model.bipedBody).rotation.setSmoothY(bodyRotationY, 0.8F);
      ((ModelRendererBends)model.bipedBody).rotation.setSmoothX(bodyRotationX, 0.8F);
      ((ModelRendererBends)model.bipedBody).rotation.setSmoothZ(-var10, 0.8F);

      // Match player SprintAnimationBit: shortly after punch, don't counter-rotate the head
      if (argData.ticksAfterPunch < 10.0F) {
         ((ModelRendererBends)model.bipedHead).rotation.setSmoothX(model.headRotationX, 0.5F);
         ((ModelRendererBends)model.bipedHead).rotation.setSmoothY(model.headRotationY, 0.5F);
      } else {
         ((ModelRendererBends)model.bipedHead).rotation.setSmoothX(model.headRotationX - bodyRotationX, 0.5F);
         ((ModelRendererBends)model.bipedHead).rotation.setSmoothY(model.headRotationY - bodyRotationY, 0.5F);
      }

      // 1.12.2 globalOffset bob
      model.renderOffset.setSmoothY(MathHelper.cos(limbSwing * 2.0F + 0.6F) * 1.5F, 0.9F);
   }
}
