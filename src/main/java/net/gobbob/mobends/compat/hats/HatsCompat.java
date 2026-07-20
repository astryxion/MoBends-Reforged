package net.gobbob.mobends.compat.hats;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hats.api.Api;
import net.gobbob.mobends.util.BendsLogger;

/**
 * Soft Hats integration internals. Loaded only via {@link HatsRender#registerIfPresent()} when Hats is present.
 */
@SideOnly(Side.CLIENT)
public final class HatsCompat {

   private static boolean active;

   private HatsCompat() {
   }

   public static void register() {
      try {
         Api.registerHelper(new HatsDisablePlayerHelper());
         Api.registerHelper(new HatsDisableZombieHelper());
         Api.registerHelper(new HatsDisableSpiderHelper());
         active = true;
         BendsLogger.log("Hats compatibility enabled.", BendsLogger.INFO);
      } catch (Throwable t) {
         active = false;
         BendsLogger.log("Hats compatibility failed to register: " + t.getMessage(), BendsLogger.INFO);
      }
   }

   public static boolean isActive() {
      return active;
   }
}
