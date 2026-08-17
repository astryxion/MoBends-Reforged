package net.gobbob.mobends.animation.skeleton;

import net.gobbob.mobends.animation.Animation;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.entity.ModelBendsSkeleton;
import net.gobbob.mobends.data.Data_Skeleton;
import net.gobbob.mobends.data.EntityData;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import org.lwjgl.util.vector.Vector3f;

/**
 * Port of 1.12.2 skeleton StandAnimationBit (biped stand; skeleton overlay is commented out upstream).
 */
public class Animation_Stand extends Animation {
   private static final float KNEEL_DURATION = 0.15F;

   public String getName() {
      return "stand";
   }

   public void animate(EntityLivingBase argEntity, ModelBase argModel, EntityData argData) {
      ModelBendsSkeleton model = (ModelBendsSkeleton)argModel;
      Data_Skeleton data = (Data_Skeleton)argData;

      model.renderOffset.setSmooth(new Vector3f(0.0F, 0.0F, 0.0F), 0.3F);
      model.renderRotation.setSmooth(new Vector3f(0.0F, 0.0F, 0.0F), 0.3F);

      ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothX(0.0F, 0.3F);
      ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothY(5.0F, 0.3F);
      ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothZ(2.0F, 0.3F);
      ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothX(0.0F, 0.3F);
      ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothY(-5.0F, 0.3F);
      ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothZ(-2.0F, 0.3F);
      ((ModelRendererBends)model.bipedRightForeLeg).rotation.setSmoothX(4.0F, 0.3F);
      ((ModelRendererBends)model.bipedLeftForeLeg).rotation.setSmoothX(4.0F, 0.3F);
      ((ModelRendererBends)model.bipedRightForeArm).rotation.setSmoothX(-4.0F, 0.3F);
      ((ModelRendererBends)model.bipedLeftForeArm).rotation.setSmoothX(-4.0F, 0.3F);

      ((ModelRendererBends)model.bipedHead).rotation.setSmoothX(model.headRotationX, 0.3F);
      ((ModelRendererBends)model.bipedHead).rotation.setSmoothY(model.headRotationY, 0.3F);

      float phase = data.ticks / 10.0F;
      float PI = (float)Math.PI;
      ((ModelRendererBends)model.bipedBody).rotation.setSmoothX(((MathHelper.cos(phase) - 1.0F) / 2.0F) * -3.0F, 1.0F);
      ((ModelRendererBends)model.bipedBody).rotation.setSmoothY(0.0F, 0.3F);
      ((ModelRendererBends)model.bipedBody).rotation.setSmoothZ(0.0F, 0.3F);
      ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothX(0.0F, 0.4F);
      ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothY(0.0F, 0.4F);
      ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothZ(MathHelper.cos(phase + PI / 2.0F) * -2.5F + 2.5F, 0.4F);
      ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothX(0.0F, 0.4F);
      ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothY(0.0F, 0.4F);
      ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothZ(MathHelper.cos(phase + PI / 2.0F) * 2.5F - 2.5F, 0.4F);

      float touchdown = Math.min(data.ticksAfterTouchdown * KNEEL_DURATION, 1.0F);
      if (touchdown < 1.0F) {
         ((ModelRendererBends)model.bipedBody).rotation.setSmoothX(20.0F * (1.0F - touchdown), 1.0F);
         model.renderOffset.setSmoothY((float)(-Math.sin(touchdown * Math.PI) * 2.0F), 1.0F);
      }
   }
}
