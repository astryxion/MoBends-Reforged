package net.gobbob.mobends.animation.spider;

import net.gobbob.mobends.animation.Animation;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.entity.ModelBendsSpider;
import net.gobbob.mobends.data.Data_Spider;
import net.gobbob.mobends.data.EntityData;
import net.gobbob.mobends.event.EventHandler_DataUpdate;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import org.lwjgl.util.vector.Vector3f;

public class Animation_Crawl extends Animation {
   public String getName() {
      return "crawl";
   }

   public void animate(EntityLivingBase argEntity, ModelBase argModel, EntityData argData) {
      ModelBendsSpider model = (ModelBendsSpider)argModel;
      Data_Spider data = (Data_Spider)argData;
      data.ensureLimbs();
      if (data.startTransition < 1.0F) {
         data.startTransition += data.ticksPerFrame * 0.1F;
      }

      float limbSwing = data.getInterpolatedCrawlProgress(EventHandler_DataUpdate.partialTicks) * 5.0F;
      float groundLevel = MathHelper.sin(limbSwing * 0.6F) * 1.2F;
      ((ModelRendererBends)model.spiderHead).rotation.setX(model.headRotationX);
      ((ModelRendererBends)model.spiderHead).rotation.setY(model.headRotationY);
      Animation_Move.animateMovingLimb(model, data, groundLevel, limbSwing + 0.0F, 0, 20.0F, 10.0F, -80.0F, -50.0F);
      Animation_Move.animateMovingLimb(model, data, groundLevel, limbSwing + 0.3F, 1, 20.0F, 10.0F, -80.0F, -50.0F);
      Animation_Move.animateMovingLimb(model, data, groundLevel, limbSwing + 0.3F, 2, 15.0F, 15.0F, -30.0F, 10.0F);
      Animation_Move.animateMovingLimb(model, data, groundLevel, limbSwing + 0.0F, 3, 15.0F, 15.0F, -30.0F, 10.0F);
      Animation_Move.animateMovingLimb(model, data, groundLevel, limbSwing + 0.4F, 4, 7.0F, 15.0F, 20.0F, 50.0F);
      Animation_Move.animateMovingLimb(model, data, groundLevel, limbSwing + 0.7F, 5, 7.0F, 15.0F, 20.0F, 50.0F);
      Animation_Move.animateMovingLimb(model, data, groundLevel, limbSwing + 0.7F, 6, 10.0F, 20.0F, 60.0F, 80.0F);
      Animation_Move.animateMovingLimb(model, data, groundLevel, limbSwing + 0.4F, 7, 10.0F, 20.0F, 60.0F, 80.0F);
      float climbingRotation = Float.isNaN(data.wallYaw) ? 0.0F : data.wallYaw;
      float yaw = argEntity.prevRotationYaw + (argEntity.rotationYaw - argEntity.prevRotationYaw) * data.ticksPerFrame;
      float renderRotationY = MathHelper.wrapAngleTo180_float(yaw - climbingRotation);
      model.renderRotation.setSmooth(new Vector3f(-90.0F, renderRotationY, 0.0F), 0.6F);
      model.renderOffset.setSmooth(new Vector3f(0.0F, -10.0F, 0.0F), 0.5F);
   }
}
