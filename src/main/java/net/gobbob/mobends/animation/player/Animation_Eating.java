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
 * Overlay from 1.12.2 EatingAnimationBit. Right-hand only (1.7.10 has no offhand).
 * Poses existing arm/head bones only — armor glue is unchanged.
 */
public class Animation_Eating extends Animation {
   public String getName() {
      return "eating";
   }

   public void animate(EntityLivingBase argEntity, ModelBase argModel, EntityData argData) {
      ModelBendsPlayer model = (ModelBendsPlayer)argModel;
      Data_Player data = (Data_Player)argData;
      data.eatBringUp += data.ticksPerFrame * 0.15F;
      if (data.eatBringUp > 1.0F) {
         data.eatBringUp = 1.0F;
      }

      float t = data.eatBringUp;
      if (t >= 1.0F) {
         float wiggle = MathHelper.cos(data.ticks);
         ((ModelRendererBends)model.bipedHead).rotation.setSmoothX(wiggle * 5.0F, 0.6F);
         ((ModelRendererBends)model.bipedHead).rotation.setSmoothY(15.0F, 0.6F);
         ((ModelRendererBends)model.bipedHead).pre_rotation.setSmoothZero(0.6F);
      }

      ModelRendererBends arm = (ModelRendererBends)model.bipedRightArm;
      ModelRendererBends foreArm = (ModelRendererBends)model.bipedRightForeArm;
      arm.pre_rotation.setSmoothZero(0.5F);
      arm.rotation.setSmoothX(t * -80.0F, 1.0F);
      arm.rotation.setSmoothY(0.0F, 1.0F);
      arm.rotation.setSmoothZ(t * 45.0F, 1.0F);
      foreArm.pre_rotation.setSmoothZero(0.5F);
      foreArm.rotation.setSmoothX(t * -45.0F, 1.0F);
      foreArm.rotation.setSmoothY(0.0F, 1.0F);
      foreArm.rotation.setSmoothZ(0.0F, 1.0F);
   }
}
