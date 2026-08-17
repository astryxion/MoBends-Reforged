package net.gobbob.mobends.animation.pigzombie;

import net.gobbob.mobends.animation.Animation;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.entity.ModelBendsZombie;
import net.gobbob.mobends.data.EntityData;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

/**
 * Port of 1.12.2 pigzombie WalkAnimationBit: zombie walk plus the pigman slouch overlay.
 */
public class Animation_Walk extends Animation {
   public String getName() {
      return "walk";
   }

   public void animate(EntityLivingBase argEntity, ModelBase argModel, EntityData argData) {
      (new net.gobbob.mobends.animation.zombie.Animation_Walk()).animate(argEntity, argModel, argData);
      ModelBendsZombie model = (ModelBendsZombie)argModel;
      float bob = Math.abs(MathHelper.sin(model.armSwing * 0.6662F)) * -1.4F - 3.0F;
      model.renderOffset.setSmoothY(bob);
      ((ModelRendererBends)model.bipedBody).rotation.setSmoothZ(-10.0F, 0.3F);
      ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothZ(10.0F, 0.3F);
      ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothZ(10.0F, 0.3F);
   }
}
