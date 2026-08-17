package net.gobbob.mobends.animation.skeleton;

import net.gobbob.mobends.animation.Animation;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.entity.ModelBendsSkeleton;
import net.gobbob.mobends.data.EntityData;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

/**
 * Port of 1.12.2 ToolAction for skeleton/wither melee swings.
 */
public class Animation_Tool extends Animation {
   public String getName() {
      return "tool";
   }

   public void animate(EntityLivingBase argEntity, ModelBase argModel, EntityData argData) {
      if (!(argEntity.isSwingInProgress || argEntity.swingProgress > 0.0F || argModel.onGround > 0.0F)) {
         return;
      }

      ModelBendsSkeleton model = (ModelBendsSkeleton)argModel;
      float progress = model.onGround > 0.0F ? model.onGround : argEntity.swingProgress;
      float swing = MathHelper.sqrt_float(progress) * ((float)Math.PI * 2.0F);
      float bodyYaw = MathHelper.sin(swing) * 30.0F;
      ((ModelRendererBends)model.bipedBody).rotation.setSmoothY(bodyYaw, 0.8F);
      ((ModelRendererBends)model.bipedHead).rotation.setSmoothX(model.headRotationX, 0.8F);
      ((ModelRendererBends)model.bipedHead).rotation.setSmoothY(model.headRotationY - bodyYaw, 0.8F);

      float ax = MathHelper.sin(swing) * 50.0F - 30.0F;
      float az = MathHelper.cos(swing) * -20.0F + 10.0F;
      ModelRendererBends arm = (ModelRendererBends)model.bipedRightArm;
      arm.pre_rotation.setX(ax);
      arm.pre_rotation.setY(0.0F);
      arm.pre_rotation.setZ(0.0F);
      arm.rotation.setX(0.0F);
      arm.rotation.setY(0.0F);
      arm.rotation.setZ(az);
   }
}
