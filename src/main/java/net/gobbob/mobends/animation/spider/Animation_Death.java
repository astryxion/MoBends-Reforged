package net.gobbob.mobends.animation.spider;

import net.gobbob.mobends.animation.Animation;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.entity.ModelBendsSpider;
import net.gobbob.mobends.data.Data_Spider;
import net.gobbob.mobends.data.EntityData;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import org.lwjgl.util.vector.Vector3f;

public class Animation_Death extends Animation {
   public String getName() {
      return "death";
   }

   public void animate(EntityLivingBase argEntity, ModelBase argModel, EntityData argData) {
      ModelBendsSpider model = (ModelBendsSpider)argModel;
      Data_Spider data = (Data_Spider)argData;
      data.ensureLimbs();
      if (!"death".equals(data.currentAnim)) {
         data.deathWiggleSpeed = 1.0F;
         data.deathWigglePhase = 0.0F;
      }

      model.renderOffset.setSmooth(new Vector3f(0.0F, 10.0F, 0.0F), 0.3F);
      ((ModelRendererBends)model.spiderHead).rotation.setX(model.headRotationX);
      ((ModelRendererBends)model.spiderHead).rotation.setY(model.headRotationY);
      float[] baseZ = new float[]{-45.0F, 45.0F, -33.3F, 33.3F, -33.3F, 33.3F, -45.0F, 45.0F};
      float[] baseY = new float[]{45.0F, -45.0F, 22.5F, -22.5F, -22.5F, 22.5F, -45.0F, 45.0F};
      float limbSwing = model.armSwing * 0.6662F;
      float limbSwingAmount = model.armSwingAmount / (float)Math.PI * 180.0F;
      float f3 = -(MathHelper.cos(limbSwing * 2.0F + 0.0F) * 0.4F) * limbSwingAmount;
      float f4 = -(MathHelper.cos(limbSwing * 2.0F + (float)Math.PI) * 0.4F) * limbSwingAmount;
      float f5 = -(MathHelper.cos(limbSwing * 2.0F + ((float)Math.PI / 2F)) * 0.4F) * limbSwingAmount;
      float f6 = -(MathHelper.cos(limbSwing * 2.0F + ((float)Math.PI * 3F / 2F)) * 0.4F) * limbSwingAmount;
      float f7 = Math.abs(MathHelper.sin(limbSwing + 0.0F) * 0.4F) * limbSwingAmount;
      float f8 = Math.abs(MathHelper.sin(limbSwing + (float)Math.PI) * 0.4F) * limbSwingAmount;
      float f9 = Math.abs(MathHelper.sin(limbSwing + ((float)Math.PI / 2F)) * 0.4F) * limbSwingAmount;
      float f10 = Math.abs(MathHelper.sin(limbSwing + ((float)Math.PI * 3F / 2F)) * 0.4F) * limbSwingAmount;
      float[] yawAdd = new float[]{f3, -f3, f4, -f4, f5, -f5, f6, -f6};
      float[] zWave = new float[]{f7, -f7, f8, -f8, f9, -f9, f10, -f10};
      if (data.deathWiggleSpeed > 0.0F) {
         data.deathWiggleSpeed -= data.ticksPerFrame * 0.1F;
         if (data.deathWiggleSpeed < 0.0F) {
            data.deathWiggleSpeed = 0.0F;
         }
      }

      data.deathWigglePhase += (0.3F + data.deathWiggleSpeed * 2.0F) * data.ticksPerFrame;
      float wiggleAmount = 10.0F + data.deathWiggleSpeed * 10.0F;
      float wiggle1 = MathHelper.cos(data.deathWigglePhase) * wiggleAmount;
      float wiggle2 = MathHelper.cos(data.deathWigglePhase + (float)Math.PI / 4.0F) * wiggleAmount;
      float wiggle3 = MathHelper.cos(data.deathWigglePhase + (float)Math.PI / 2.0F) * wiggleAmount;
      float wiggle4 = MathHelper.cos(data.deathWigglePhase + (float)Math.PI / 4.0F * 3.0F) * wiggleAmount;
      float[] wiggle = new float[]{wiggle1, wiggle2, wiggle3, wiggle4, wiggle1, wiggle2, wiggle3, wiggle4};
      float foreBend = 89.0F;

      for(int i = 0; i < 8; ++i) {
         boolean odd = i % 2 == 1;
         ModelRendererBends upper = SpiderLimbs.upper(model, i);
         ModelRendererBends lower = SpiderLimbs.lower(model, i);
         SpiderLimbs.setUpperYaw(upper, baseY[i] + yawAdd[i], true, 1.0F);
         upper.rotation.setX(0.0F);
         upper.rotation.setY(0.0F);
         upper.rotation.setZ(baseZ[i] + zWave[i] + wiggle[i]);
         lower.pre_rotation.setSmoothZero(1.0F);
         lower.rotation.setX(0.0F);
         lower.rotation.setY(0.0F);
         lower.rotation.setZ(odd ? foreBend : -foreBend);
      }

      model.renderRotation.setSmoothZero(0.3F);
   }
}
