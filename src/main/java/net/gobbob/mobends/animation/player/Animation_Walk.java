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
 * Port of MoBends 1.12.2 biped/player WalkAnimationBit.
 */
public class Animation_Walk extends Animation {
   private static final float KNEEL_DURATION = 0.15F;

   public String getName() {
      return "walk";
   }

   public void animate(EntityLivingBase argEntity, ModelBase argModel, EntityData argData) {
      ModelBendsPlayer model = (ModelBendsPlayer)argModel;

      float PI = (float)Math.PI;
      float limbSwing = model.armSwing * 0.6662F;
      float armSwingAmount = model.armSwingAmount * 0.5F / PI * 180.0F;

      ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothX(MathHelper.cos(limbSwing + PI) * armSwingAmount, 0.8F);
      ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothX(MathHelper.cos(limbSwing) * armSwingAmount, 0.8F);
      ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothZ(5.0F, 0.8F);
      ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothZ(-5.0F, 0.8F);

      float legSwingAmount = 0.7F * model.armSwingAmount / PI * 180.0F;
      ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothX(-5.0F + MathHelper.cos(limbSwing) * legSwingAmount, 1.0F);
      ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothX(-5.0F + MathHelper.cos(limbSwing + PI) * legSwingAmount, 1.0F);
      ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothY(0.0F);
      ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothY(0.0F);
      ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothZ(2.0F, 1.0F);
      ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothZ(-2.0F, 1.0F);

      float var = (limbSwing / PI) % 2.0F;
      ((ModelRendererBends)model.bipedLeftForeLeg).rotation.setSmoothX(var > 1.0F ? 45.0F : 0.0F, 0.5F);
      ((ModelRendererBends)model.bipedRightForeLeg).rotation.setSmoothX(var > 1.0F ? 0.0F : 45.0F, 0.5F);
      ((ModelRendererBends)model.bipedLeftForeArm).rotation.setSmoothX(MathHelper.cos(limbSwing + PI / 2.0F) * -10.0F - 10.0F, 0.8F);
      ((ModelRendererBends)model.bipedRightForeArm).rotation.setSmoothX(MathHelper.cos(limbSwing) * -10.0F - 10.0F, 0.8F);

      float bodyRotationY = MathHelper.cos(limbSwing) * -20.0F;
      float bodyRotationX = MathHelper.cos(limbSwing * 2.0F) * 5.0F + 3.0F;
      float var10 = model.headRotationY * 0.1F;
      var10 = GUtil.max(var10, 10.0F);
      var10 = GUtil.min(var10, -10.0F);

      ((ModelRendererBends)model.bipedBody).rotation.setSmoothY(bodyRotationY, 0.5F);
      ((ModelRendererBends)model.bipedBody).rotation.setSmoothX(bodyRotationX, 0.5F);
      ((ModelRendererBends)model.bipedBody).rotation.setSmoothZ(-var10, 0.5F);

      if (argData.ticksAfterPunch < 10.0F) {
         ((ModelRendererBends)model.bipedHead).rotation.setSmoothX(model.headRotationX, 0.5F);
         ((ModelRendererBends)model.bipedHead).rotation.setSmoothY(model.headRotationY, 0.5F);
      } else {
         ((ModelRendererBends)model.bipedHead).rotation.setSmoothX(model.headRotationX - bodyRotationX, 0.5F);
         ((ModelRendererBends)model.bipedHead).rotation.setSmoothY(model.headRotationY - bodyRotationY, 0.5F);
      }

      model.renderOffset.setSmoothY(MathHelper.cos(limbSwing * 2.0F) * 0.6F, 0.3F);

      // Landing kneel (1.12.2 touchdown)
      float touchdown = Math.min(argData.ticksAfterTouchdown * KNEEL_DURATION, 1.0F);
      if (touchdown < 1.0F) {
         ((ModelRendererBends)model.bipedBody).rotation.setSmoothX(20.0F * (1.0F - touchdown), 1.0F);
         model.renderOffset.setSmoothY((float)(-Math.sin(touchdown * Math.PI) * 2.0F), 1.0F);
      }
   }
}
