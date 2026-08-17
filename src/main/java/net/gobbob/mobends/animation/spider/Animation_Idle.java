package net.gobbob.mobends.animation.spider;

import net.gobbob.mobends.animation.Animation;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.entity.ModelBendsSpider;
import net.gobbob.mobends.data.Data_Spider;
import net.gobbob.mobends.data.EntityData;
import net.gobbob.mobends.event.EventHandler_DataUpdate;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySpider;
import org.lwjgl.util.vector.Vector3f;

public class Animation_Idle extends Animation {
   private static final float KNEEL_DURATION = 10.0F;

   public String getName() {
      return "idle";
   }

   public void animate(EntityLivingBase argEntity, ModelBase argModel, EntityData argData) {
      ModelBendsSpider model = (ModelBendsSpider)argModel;
      Data_Spider data = (Data_Spider)argData;
      data.ensureLimbs();
      float ticks = data.ticks;
      float pt = EventHandler_DataUpdate.partialTicks;
      EntitySpider spider = (EntitySpider)argEntity;
      double groundLevel = Math.sin((double)ticks * 0.1D) * 0.5D;
      float touchdown = Math.min(data.ticksAfterTouchdown / KNEEL_DURATION, 1.0F);
      if (touchdown < 1.0F) {
         float touchdownInv = 1.0F - touchdown;
         groundLevel += Math.sin((double)(touchdown * 1.0F) * Math.PI * 2.0D) * 4.0D * (double)touchdownInv;
      }

      ((ModelRendererBends)model.spiderHead).rotation.setX(model.headRotationX);
      ((ModelRendererBends)model.spiderHead).rotation.setY(model.headRotationY);
      double bodyX = Math.sin((double)ticks * 0.2D) * 0.4D;
      double bodyZ = Math.cos((double)ticks * 0.2D) * 0.4D;

      for(int i = 0; i < data.limbs.length; ++i) {
         Data_Spider.Limb limb = data.limbs[i];
         Data_Spider.IKResult ikResult = limb.solveIK(bodyX, bodyZ, pt);
         if (ikResult.deviation > 0.9D || ikResult.xzDistance * 0.0625D > 1.2D) {
            limb.adjustToNeutralPosition();
         }

         ModelRendererBends upper = SpiderLimbs.upper(model, i);
         ModelRendererBends lower = SpiderLimbs.lower(model, i);
         SpiderLimbs.setUpperYaw(upper, limb.getYawDegrees(ikResult), true, 1.0F);
         double lift = groundLevel - 7.0D + Math.sin((double)limb.getAdjustingProgress() * Math.PI) * 4.0D;
         SpiderLimbs.putLimbOnGround(upper, lower, limb.odd, ikResult.xzDistance, lift);
      }

      if (spider.ticksExisted % 100 < 10) {
         data.limbs[6].adjustToLocalPosition(0.0D, 1.5D, 0.2F);
         data.limbs[7].adjustToLocalPosition(0.0D, 1.5D, 0.2F);
      }

      model.renderOffset.setSmooth(new Vector3f((float)bodyX, (float)(-groundLevel), (float)(-bodyZ)), 0.5F);
      model.renderRotation.setSmoothZero(0.5F);
   }
}
