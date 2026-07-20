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
 * Backport of Mo'Bends 1.12.2 FallingAnimationBit.
 */
public class Animation_Falling extends Animation {
   public static final float TICKS_BEFORE_FALLING = 10.0F;
   public static final float FALLING_TRANSITION_TICKS = 80.0F;

   public String getName() {
      return "falling";
   }

   public void animate(EntityLivingBase argEntity, ModelBase argModel, EntityData argData) {
      ModelBendsPlayer model = (ModelBendsPlayer)argModel;
      Data_Player data = (Data_Player)argData;

      model.centerRotation.setSmoothX(0.0F, 0.3F);
      model.centerRotation.setY(0.0F);
      model.centerRotation.setZ(0.0F);

      ((ModelRendererBends)model.bipedHead).rotation.setSmoothX(model.headRotationX, 1.0F);
      ((ModelRendererBends)model.bipedHead).rotation.setSmoothY(model.headRotationY, 1.0F);
      ((ModelRendererBends)model.bipedBody).rotation.setSmoothY(0.0F, 0.5F);
      ((ModelRendererBends)model.bipedBody).rotation.setSmoothX(0.0F, 0.5F);
      ((ModelRendererBends)model.bipedBody).rotation.setSmoothZ(0.0F, 0.5F);

      float ticks = data.ticks * 0.5F;
      float rightArmDelay = 1.0F;
      float armSpan = 20.0F;
      float legSpan = 10.0F;
      float transition = (data.ticksFalling - TICKS_BEFORE_FALLING) / FALLING_TRANSITION_TICKS;
      transition = MathHelper.clamp_float(transition, 0.0F, 1.0F);
      float s = transition * 0.9F;

      ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothZ(-90.0F + MathHelper.sin(ticks) * armSpan, s);
      ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothY(MathHelper.cos(ticks) * armSpan, s);
      ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothX(0.0F, s);
      ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothZ(90.0F + MathHelper.sin(ticks + rightArmDelay) * armSpan, s);
      ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothY(MathHelper.cos(ticks + rightArmDelay) * armSpan, s);
      ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothX(0.0F, s);
      ((ModelRendererBends)model.bipedLeftForeArm).rotation.setSmoothX(-15.0F, s);
      ((ModelRendererBends)model.bipedRightForeArm).rotation.setSmoothX(-15.0F, s);
      ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothX(MathHelper.sin(ticks) * legSpan, s);
      ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothZ(-20.0F + MathHelper.cos(ticks) * legSpan, s);
      ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothY(0.0F, s);
      ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothX(MathHelper.sin(ticks + rightArmDelay) * legSpan, s);
      ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothZ(20.0F + MathHelper.cos(ticks + rightArmDelay) * legSpan, s);
      ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothY(0.0F, s);
      ((ModelRendererBends)model.bipedLeftForeLeg).rotation.setSmoothX(20.0F, s);
      ((ModelRendererBends)model.bipedRightForeLeg).rotation.setSmoothX(20.0F, s);
      model.renderRotation.setSmoothX(20.0F, s);
      model.renderRotation.setSmoothY(0.0F, s);
      model.renderRotation.setSmoothZ(0.0F, s);
      ((ModelRendererBends)model.bipedHead).rotation.setSmoothX(model.headRotationX - 20.0F * transition, s);
   }
}
