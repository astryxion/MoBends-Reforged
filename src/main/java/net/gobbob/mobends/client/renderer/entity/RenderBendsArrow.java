package net.gobbob.mobends.client.renderer.entity;

import net.gobbob.mobends.settings.SettingsBoolean;
import net.gobbob.mobends.settings.SettingsNode;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.entity.projectile.EntityArrow;

/**
 * Vanilla arrow renderer with MoBends 1.12.2-style arrow trails.
 */
public class RenderBendsArrow extends RenderArrow {
   public void doRender(EntityArrow entity, double x, double y, double z, float entityYaw, float partialTicks) {
      if (((SettingsBoolean)SettingsNode.getSetting("arrowTrail")).data) {
         ArrowTrailManager.renderTrail(entity, x, y, z, partialTicks);
      }

      super.doRender(entity, x, y, z, entityYaw, partialTicks);
   }
}
