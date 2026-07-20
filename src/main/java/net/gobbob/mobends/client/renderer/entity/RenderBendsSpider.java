package net.gobbob.mobends.client.renderer.entity;

import net.gobbob.mobends.MoBends;
import net.gobbob.mobends.client.model.entity.ModelBendsSpider;
import net.gobbob.mobends.compat.hats.HatsRender;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySpider;

public class RenderBendsSpider extends RenderSpider {
   public int refreshModel = 0;

   public RenderBendsSpider() {
      this.mainModel = new ModelBendsSpider();
      this.setRenderPassModel(new ModelBendsSpider());
   }

   public void doRender(EntityLiving p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
      if (this.refreshModel != MoBends.refreshModel) {
         this.mainModel = new ModelBendsSpider();
         this.setRenderPassModel(new ModelBendsSpider());
         this.refreshModel = MoBends.refreshModel;
      }

      super.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
   }

   protected void renderEquippedItems(EntityLivingBase entity, float partialTicks) {
      super.renderEquippedItems(entity, partialTicks);
      if (entity instanceof EntitySpider && this.mainModel instanceof ModelBendsSpider) {
         HatsRender.renderSpiderHat((EntitySpider)entity, (ModelBendsSpider)this.mainModel, partialTicks);
      }
   }
}
