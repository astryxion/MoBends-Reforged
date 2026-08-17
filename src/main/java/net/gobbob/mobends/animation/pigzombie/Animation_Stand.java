package net.gobbob.mobends.animation.pigzombie;

import net.gobbob.mobends.animation.Animation;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.entity.ModelBendsZombie;
import net.gobbob.mobends.data.EntityData;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;

/**
 * Port of 1.12.2 pigzombie StandAnimationBit (biped stand + slouch overlay).
 */
public class Animation_Stand extends Animation {
   public String getName() {
      return "stand";
   }

   public void animate(EntityLivingBase argEntity, ModelBase argModel, EntityData argData) {
      ModelBendsZombie model = (ModelBendsZombie)argModel;
      model.renderOffset.setSmoothY(-3.0F);
      ((ModelRendererBends)model.bipedBody).rotation.setSmoothX(20.0F, 0.3F);
      ((ModelRendererBends)model.bipedBody).rotation.setSmoothZ(-10.0F, 0.3F);
      ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothX(-20.0F, 0.3F);
      ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothZ(10.0F, 0.3F);
      ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothX(-20.0F, 0.3F);
      ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothZ(10.0F, 0.3F);
      ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothZ(10.0F, 0.3F);
      ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothZ(-10.0F, 0.3F);
      ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothX(-30.0F, 0.3F);
      ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothX(-10.0F, 0.3F);
      ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothY(-10.0F, 0.3F);
      ((ModelRendererBends)model.bipedRightForeLeg).rotation.setSmoothX(25.0F, 0.3F);
      ((ModelRendererBends)model.bipedLeftForeLeg).rotation.setSmoothX(25.0F, 0.3F);
      ((ModelRendererBends)model.bipedHead).rotation.setX(model.headRotationX - 20.0F);
      ((ModelRendererBends)model.bipedHead).rotation.setY(model.headRotationY);
   }
}
