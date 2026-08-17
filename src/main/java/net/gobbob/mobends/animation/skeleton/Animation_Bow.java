package net.gobbob.mobends.animation.skeleton;

import net.gobbob.mobends.animation.Animation;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.entity.ModelBendsSkeleton;
import net.gobbob.mobends.data.Data_Skeleton;
import net.gobbob.mobends.data.EntityData;
import net.gobbob.mobends.util.GUtil;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;

/**
 * 1.7.10 Euler port of the player bow pose, driven by {@link Data_Skeleton#bowDraw}
 * the way 1.12.2 BowAction uses item-in-use time.
 */
public class Animation_Bow extends Animation {
   public String getName() {
      return "bow";
   }

   public void animate(EntityLivingBase argEntity, ModelBase argModel, EntityData argData) {
      ModelBendsSkeleton model = (ModelBendsSkeleton)argModel;
      Data_Skeleton data = (Data_Skeleton)argData;
      float aimedBowDuration = data.bowDraw;
      if (aimedBowDuration > 15.0F) {
         aimedBowDuration = 15.0F;
      }

      // Always the aimed pose (1.12.2 BowAction). Do not use the player's <10 "bring up to chest"
      // stage — that parks the bow in the torso while draw time ramps.
      float var1 = 20.0F - Math.max(0.0F, aimedBowDuration - 10.0F) / 5.0F * 20.0F;
      ((ModelRendererBends)model.bipedBody).rotation.setSmoothX(var1, 0.3F);
      float var = Math.max(0.0F, aimedBowDuration - 10.0F) / 5.0F * -25.0F;
      ((ModelRendererBends)model.bipedBody).rotation.setSmoothY(var + model.headRotationY, 0.3F);
      ((ModelRendererBends)model.bipedBody).rotation.setSmoothZ(0.0F, 0.3F);
      ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothX(-90.0F - var1, 0.3F);
      ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothX(-30.0F);
      ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothY(80.0F);
      ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothZ(0.0F, 0.3F);
      float var2 = Math.max(aimedBowDuration, 10.0F) / 10.0F;
      model.bipedLeftForeArm.rotation.setSmoothX(var2 * -30.0F);
      model.bipedRightForeArm.rotation.setSmoothX(0.0F, 0.3F);
      ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothY(-var);
      ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothZ(0.0F, 0.3F);
      float var5 = -90.0F + model.headRotationX;
      var5 = GUtil.min(var5, -120.0F);
      ((ModelRendererBends)model.bipedLeftArm).pre_rotation.setSmoothX(var5, 0.3F);
      ((ModelRendererBends)model.bipedRightArm).pre_rotation.setSmoothX(model.headRotationX);
      ((ModelRendererBends)model.bipedHead).rotation.setSmoothY(-var);
      ((ModelRendererBends)model.bipedHead).pre_rotation.setSmoothX(-var1, 0.3F);
      ((ModelRendererBends)model.bipedHead).rotation.setSmoothX(model.headRotationX);
   }
}
