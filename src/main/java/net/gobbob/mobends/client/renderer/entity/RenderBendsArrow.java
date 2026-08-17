package net.gobbob.mobends.client.renderer.entity;

import net.gobbob.mobends.settings.SettingsBoolean;
import net.gobbob.mobends.settings.SettingsNode;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.projectile.EntityArrow;

/**
 * Vanilla arrow renderer with MoBends 1.12.2-style arrow trails.
 */
public class RenderBendsArrow extends RenderArrow {
   public void doRender(EntityArrow entity, double x, double y, double z, float entityYaw, float partialTicks) {
      if (((SettingsBoolean)SettingsNode.getSetting("arrowTrail")).data) {
         ArrowTrailManager.renderTrail(entity, x, y, z, partialTicks);
      }

      // Skeleton-shot arrows render nock-first with vanilla yaw; player-shot ones do not.
      boolean flip = entity.shootingEntity instanceof EntitySkeleton;
      if (flip) {
         entity.prevRotationYaw += 180.0F;
         entity.rotationYaw += 180.0F;
         entityYaw += 180.0F;
      }

      try {
         super.doRender(entity, x, y, z, entityYaw, partialTicks);
      } finally {
         if (flip) {
            entity.prevRotationYaw -= 180.0F;
            entity.rotationYaw -= 180.0F;
         }
      }
   }
}
