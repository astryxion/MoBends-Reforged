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

public class Animation_Move extends Animation {
   private static final float KNEEL_DURATION = 10.0F;

   public String getName() {
      return "move";
   }

   public void animate(EntityLivingBase argEntity, ModelBase argModel, EntityData argData) {
      ModelBendsSpider model = (ModelBendsSpider)argModel;
      Data_Spider data = (Data_Spider)argData;
      data.ensureLimbs();
      float ticks = data.ticks;
      float limbSwing = model.armSwing * 0.6662F;
      float groundLevel = MathHelper.sin(ticks * 0.6F) * 1.2F;
      float touchdown = Math.min(data.ticksAfterTouchdown / KNEEL_DURATION, 1.0F);
      if (data.startTransition < 1.0F) {
         data.startTransition += data.ticksPerFrame * 0.1F;
      }

      if (touchdown < 1.0F) {
         float touchdownInv = 1.0F - touchdown;
         groundLevel += (float)(Math.sin((double)(touchdown * 1.2F - 0.2F) * Math.PI * 2.0D) * 3.0D * (double)touchdownInv);
      }

      ((ModelRendererBends)model.spiderHead).rotation.setX(model.headRotationX);
      ((ModelRendererBends)model.spiderHead).rotation.setY(model.headRotationY);
      float bodyX = MathHelper.sin(ticks * 0.2F) * 0.4F;
      float bodyZ = MathHelper.cos(ticks * 0.2F) * 0.4F;
      animateMovingLimb(model, data, groundLevel, limbSwing + 0.0F, 0, 20.0F, 10.0F, -80.0F, -50.0F);
      animateMovingLimb(model, data, groundLevel, limbSwing + 0.3F, 1, 20.0F, 10.0F, -80.0F, -50.0F);
      animateMovingLimb(model, data, groundLevel, limbSwing + 0.3F, 2, 15.0F, 15.0F, -30.0F, 10.0F);
      animateMovingLimb(model, data, groundLevel, limbSwing + 0.0F, 3, 15.0F, 15.0F, -30.0F, 10.0F);
      animateMovingLimb(model, data, groundLevel, limbSwing + 0.4F, 4, 7.0F, 15.0F, 20.0F, 50.0F);
      animateMovingLimb(model, data, groundLevel, limbSwing + 0.7F, 5, 7.0F, 15.0F, 20.0F, 50.0F);
      animateMovingLimb(model, data, groundLevel, limbSwing + 0.7F, 6, 10.0F, 20.0F, 60.0F, 80.0F);
      animateMovingLimb(model, data, groundLevel, limbSwing + 0.4F, 7, 10.0F, 20.0F, 60.0F, 80.0F);
      model.renderOffset.setSmooth(new Vector3f(bodyX, -groundLevel, -bodyZ), 0.5F);
      model.renderRotation.setSmoothZero(0.5F);
   }

   static void animateMovingLimb(ModelBendsSpider model, Data_Spider data, float groundLevel, float limbSwing, int index, float minDist, float maxDist, float minRot, float maxRot) {
      boolean odd = index % 2 == 1;
      float offset = (index + 1) / 2 % 2 == 0 ? (float)Math.PI : 0.0F;
      boolean instant = data.startTransition >= 1.0F;
      float smoothness = instant ? 1.0F : Math.max(0.05F, data.startTransition);
      float sideRotation = minRot + (MathHelper.sin(limbSwing + offset) * 0.5F + 0.5F) * (maxRot - minRot);
      float dist = minDist + (MathHelper.sin(limbSwing + offset) * 0.5F + 0.5F) * (maxDist - minDist);
      groundLevel += -7.0F + Math.max(0.0F, MathHelper.cos(limbSwing + offset)) * 4.0F;
      Data_Spider.Limb limb = data.limbs[index];
      ModelRendererBends upper = SpiderLimbs.upper(model, index);
      ModelRendererBends lower = SpiderLimbs.lower(model, index);
      SpiderLimbs.setUpperYaw(upper, odd ? sideRotation : -sideRotation, instant, smoothness);
      SpiderLimbs.putLimbOnGround(upper, lower, odd, (double)dist, (double)groundLevel, smoothness, instant);
      limb.setAngleAndDistance(odd ? sideRotation / 180.0F * (float)Math.PI : (float)Math.PI - sideRotation / 180.0F * (float)Math.PI, dist * 0.0625F);
   }
}
