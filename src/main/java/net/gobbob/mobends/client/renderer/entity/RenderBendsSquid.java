package net.gobbob.mobends.client.renderer.entity;

import net.gobbob.mobends.AnimatedEntity;
import net.gobbob.mobends.MoBends;
import net.gobbob.mobends.client.model.entity.ModelBendsSquid;
import net.minecraft.client.model.ModelSquid;
import net.minecraft.client.renderer.entity.RenderSquid;
import net.minecraft.entity.passive.EntitySquid;

public class RenderBendsSquid extends RenderSquid {
   public int refreshModel = 0;
   private final RenderSquid vanilla = new RenderSquid(new ModelSquid(), 0.7F);

   public RenderBendsSquid() {
      super(new ModelBendsSquid(), 0.7F);
   }

   public void doRender(EntitySquid entity, double x, double y, double z, float yaw, float partialTicks) {
      if (!AnimatedEntity.shouldAnimate(entity)) {
         VanillaRenderBridge.bind(this.vanilla);
         this.vanilla.doRender(entity, x, y, z, yaw, partialTicks);
         return;
      }

      if (this.refreshModel != MoBends.refreshModel) {
         this.mainModel = new ModelBendsSquid();
         this.refreshModel = MoBends.refreshModel;
      }

      super.doRender(entity, x, y, z, yaw, partialTicks);
   }
}
