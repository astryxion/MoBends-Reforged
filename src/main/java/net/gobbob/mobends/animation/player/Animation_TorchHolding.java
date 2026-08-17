package net.gobbob.mobends.animation.player;

import net.gobbob.mobends.animation.Animation;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.entity.ModelBendsPlayer;
import net.gobbob.mobends.data.Data_Player;
import net.gobbob.mobends.data.EntityData;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;

/**
 * Overlay from 1.12.2 TorchHoldingAnimationBit. Only poses the holding arm so
 * armor/gloves still follow {@code postRender} of the existing bones.
 */
public class Animation_TorchHolding extends Animation {
   public String getName() {
      return "torch_holding";
   }

   public void animate(EntityLivingBase argEntity, ModelBase argModel, EntityData argData) {
      ModelBendsPlayer model = (ModelBendsPlayer)argModel;
      Data_Player data = (Data_Player)argData;
      ModelRendererBends arm = (ModelRendererBends)model.bipedRightArm;
      ModelRendererBends foreArm = (ModelRendererBends)model.bipedRightForeArm;
      if (Animation_Attack.isPunching(argEntity, model, data)) {
         return;
      }

      model.torchItemUpright = true;
      arm.pre_rotation.setSmoothZero(0.5F);
      arm.rotation.setSmoothX(-90.0F + model.headRotationX * 0.5F, 0.8F);
      arm.rotation.setSmoothY(model.headRotationY * 0.7F, 0.8F);
      arm.rotation.setSmoothZ(0.0F, 0.8F);
      foreArm.pre_rotation.setSmoothZero(0.5F);
      foreArm.rotation.setSmoothX(-5.0F, 0.8F);
      foreArm.rotation.setSmoothY(0.0F, 0.8F);
      foreArm.rotation.setSmoothZ(0.0F, 0.8F);
   }
}
