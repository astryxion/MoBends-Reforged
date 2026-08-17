package net.gobbob.mobends.animation.spider;

import net.gobbob.mobends.animation.Animation;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.entity.ModelBendsSpider;
import net.gobbob.mobends.data.Data_Spider;
import net.gobbob.mobends.data.EntityData;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.util.vector.Vector3f;

public class Animation_Jump extends Animation {
   public String getName() {
      return "jump";
   }

   public void animate(EntityLivingBase argEntity, ModelBase argModel, EntityData argData) {
      ModelBendsSpider model = (ModelBendsSpider)argModel;
      Data_Spider data = (Data_Spider)argData;
      data.ensureLimbs();
      ((ModelRendererBends)model.spiderHead).rotation.setX(model.headRotationX);
      ((ModelRendererBends)model.spiderHead).rotation.setY(model.headRotationY);

      for(int i = 0; i < data.limbs.length; ++i) {
         boolean odd = i % 2 == 1;
         float naturalYaw = -((float)i / 7.0F * 2.0F - 1.0F);
         naturalYaw = odd ? -naturalYaw * 1.3F : naturalYaw * 1.3F;
         SpiderLimbs.setUpperYaw(SpiderLimbs.upper(model, i), naturalYaw / (float)Math.PI * 180.0F, true, 1.0F);
      }

      float motionY = -data.motion.y * 5.0F;
      if (motionY < -1.0F) {
         motionY = -1.0F;
      }

      if (motionY > 1.0F) {
         motionY = 1.0F;
      }

      float legAngle = -20.0F + motionY * 25.0F;
      float foreLegAngle = -70.0F - motionY * 40.0F;
      float smoothness = 1.0F;

      for(int i = 0; i < 8; ++i) {
         boolean odd = i % 2 == 1;
         ModelRendererBends upper = SpiderLimbs.upper(model, i);
         ModelRendererBends lower = SpiderLimbs.lower(model, i);
         upper.rotation.setSmoothX(0.0F, smoothness);
         upper.rotation.setSmoothY(0.0F, smoothness);
         upper.rotation.setSmoothZ(odd ? -legAngle : legAngle, smoothness);
         lower.pre_rotation.setSmoothZero(smoothness);
         lower.rotation.setSmoothX(0.0F, smoothness);
         lower.rotation.setSmoothY(0.0F, smoothness);
         lower.rotation.setSmoothZ(odd ? -foreLegAngle : foreLegAngle, smoothness);
      }

      model.renderOffset.setSmooth(new Vector3f(0.0F, 0.0F, 0.0F), 0.5F);
      model.renderRotation.setSmoothZero(0.5F);
   }
}
