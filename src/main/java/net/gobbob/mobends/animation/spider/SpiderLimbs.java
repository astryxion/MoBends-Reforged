package net.gobbob.mobends.animation.spider;

import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.entity.ModelBendsSpider;

/**
 * 1.12.2 SpiderController.putLimbOnGround as 1.7.10 Euler (pre_Y then rotation.Z).
 */
public final class SpiderLimbs {
   private static final float SEGMENT = 12.0F;
   private static final float MAX_STRETCH = SEGMENT * 2.0F;

   private SpiderLimbs() {
   }

   public static ModelRendererBends upper(ModelBendsSpider model, int index) {
      switch(index) {
         case 0:
            return (ModelRendererBends)model.spiderLeg1;
         case 1:
            return (ModelRendererBends)model.spiderLeg2;
         case 2:
            return (ModelRendererBends)model.spiderLeg3;
         case 3:
            return (ModelRendererBends)model.spiderLeg4;
         case 4:
            return (ModelRendererBends)model.spiderLeg5;
         case 5:
            return (ModelRendererBends)model.spiderLeg6;
         case 6:
            return (ModelRendererBends)model.spiderLeg7;
         default:
            return (ModelRendererBends)model.spiderLeg8;
      }
   }

   public static ModelRendererBends lower(ModelBendsSpider model, int index) {
      switch(index) {
         case 0:
            return model.spiderForeLeg1;
         case 1:
            return model.spiderForeLeg2;
         case 2:
            return model.spiderForeLeg3;
         case 3:
            return model.spiderForeLeg4;
         case 4:
            return model.spiderForeLeg5;
         case 5:
            return model.spiderForeLeg6;
         case 6:
            return model.spiderForeLeg7;
         default:
            return model.spiderForeLeg8;
      }
   }

   public static void putLimbOnGround(ModelRendererBends upper, ModelRendererBends lower, boolean odd, double stretchDistance, double groundLevel) {
      putLimbOnGround(upper, lower, odd, stretchDistance, groundLevel, 1.0F, true);
   }

   public static void putLimbOnGround(ModelRendererBends upper, ModelRendererBends lower, boolean odd, double stretchDistance, double groundLevel, float smoothness) {
      putLimbOnGround(upper, lower, odd, stretchDistance, groundLevel, smoothness, false);
   }

   public static void putLimbOnGround(ModelRendererBends upper, ModelRendererBends lower, boolean odd, double stretchDistance, double groundLevel, float smoothness, boolean instant) {
      double c = groundLevel == 0.0D ? stretchDistance : Math.sqrt(stretchDistance * stretchDistance + groundLevel * groundLevel);
      if (c > (double)MAX_STRETCH) {
         c = (double)MAX_STRETCH;
      }

      double alpha = c > (double)MAX_STRETCH ? 0.0D : Math.acos(c / 2.0D / (double)SEGMENT);
      double beta = Math.atan2(stretchDistance, -groundLevel);
      double lowerAngle = Math.max(-2.3D, -2.0D * alpha);
      double upperAngle = Math.min(1.0D, alpha + beta - Math.PI / 2.0D);
      float upperZ = (float)(upperAngle / Math.PI * 180.0D) * (odd ? -1.0F : 1.0F);
      float lowerZ = (float)(lowerAngle / Math.PI * 180.0D) * (odd ? -1.0F : 1.0F);
      upper.rotation.setSmoothX(0.0F, smoothness);
      upper.rotation.setSmoothY(0.0F, smoothness);
      lower.pre_rotation.setSmoothZero(smoothness);
      lower.rotation.setSmoothX(0.0F, smoothness);
      lower.rotation.setSmoothY(0.0F, smoothness);
      if (instant) {
         upper.rotation.setZ(upperZ);
         lower.rotation.setZ(lowerZ);
      } else {
         upper.rotation.setSmoothZ(upperZ, smoothness);
         lower.rotation.setSmoothZ(lowerZ, smoothness);
      }
   }

   public static void setUpperYaw(ModelRendererBends upper, float yawDegrees, boolean instant, float smoothness) {
      upper.pre_rotation.setSmoothX(0.0F, smoothness);
      upper.pre_rotation.setSmoothZ(0.0F, smoothness);
      if (instant) {
         upper.pre_rotation.setY(yawDegrees);
      } else {
         upper.pre_rotation.setSmoothY(yawDegrees, smoothness);
      }
   }
}
