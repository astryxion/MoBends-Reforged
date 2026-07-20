package net.gobbob.mobends.client.renderer.entity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.entity.projectile.EntityArrow;

/**
 * Port of MoBends 1.12.2 ArrowTrailManager.
 */
public class ArrowTrailManager {
   private static final HashMap<EntityArrow, ArrowTrail> trailMap = new HashMap();

   public static ArrowTrail getOrMake(EntityArrow arrow) {
      ArrowTrail trail = (ArrowTrail)trailMap.get(arrow);
      if (trail == null) {
         trail = new ArrowTrail(arrow);
         trailMap.put(arrow, trail);
      }

      return trail;
   }

   public static void renderTrail(EntityArrow entity, double x, double y, double z, float partialTicks) {
      getOrMake(entity).render(x, y, z, partialTicks);
   }

   public static void cleanup() {
      Iterator<Map.Entry<EntityArrow, ArrowTrail>> it = trailMap.entrySet().iterator();

      while(it.hasNext()) {
         if (((ArrowTrail)((Map.Entry)it.next()).getValue()).shouldBeRemoved()) {
            it.remove();
         }
      }

   }

   public static void onRenderTick(float ticksPerFrame) {
      for(ArrowTrail trail : trailMap.values()) {
         trail.onRenderTick(ticksPerFrame);
      }

      cleanup();
   }
}
