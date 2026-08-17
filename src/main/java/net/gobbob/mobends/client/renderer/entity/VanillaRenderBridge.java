package net.gobbob.mobends.client.renderer.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;

final class VanillaRenderBridge {
   private VanillaRenderBridge() {
   }

   static void bind(Render vanilla) {
      vanilla.setRenderManager(RenderManager.instance);
   }
}
