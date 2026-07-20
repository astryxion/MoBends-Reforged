package hats.api;

/**
 * Compile-time stub matching Hats' public API. Excluded from the Mo' Bends jar;
 * at runtime the Hats mod provides these classes.
 */
public final class Api {
   public static void registerHelper(RenderOnEntityHelper helper) {
      try {
         Class.forName("hats.common.core.ApiHandler").getDeclaredMethod("registerHelper", RenderOnEntityHelper.class).invoke((Object)null, helper);
      } catch (Exception var2) {
      }

   }

   public static Object createHatInfo(String hatName, int r, int g, int b, int alpha) {
      try {
         return Class.forName("hats.common.core.ApiHandler").getDeclaredMethod("createHatInfo", String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE).invoke((Object)null, hatName, r, g, b, alpha);
      } catch (Exception var6) {
         return null;
      }
   }

   public static Object createHatInfo(String hatName, int r, int g, int b) {
      try {
         return Class.forName("hats.common.core.ApiHandler").getDeclaredMethod("createHatInfo", String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE).invoke((Object)null, hatName, r, g, b);
      } catch (Exception var5) {
         return null;
      }
   }

   public static Object getRandomHatInfoWithServerWeightage(int r, int g, int b, int alpha) {
      try {
         return Class.forName("hats.common.core.ApiHandler").getDeclaredMethod("getRandomHatInfoWithServerWeightage", Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE).invoke((Object)null, r, g, b, alpha);
      } catch (Exception var5) {
         return null;
      }
   }

   public static Object getRandomHatInfoWithServerWeightage(int r, int g, int b) {
      try {
         return Class.forName("hats.common.core.ApiHandler").getDeclaredMethod("getRandomHatInfoWithServerWeightage", Integer.TYPE, Integer.TYPE, Integer.TYPE).invoke((Object)null, r, g, b);
      } catch (Exception var4) {
         return null;
      }
   }

   public static Object getRandomHatInfo(int r, int g, int b, int alpha) {
      try {
         return Class.forName("hats.common.core.ApiHandler").getDeclaredMethod("getRandomHatInfo", Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE).invoke((Object)null, r, g, b, alpha);
      } catch (Exception var5) {
         return null;
      }
   }

   public static Object getRandomHatInfo(int r, int g, int b) {
      try {
         return Class.forName("hats.common.core.ApiHandler").getDeclaredMethod("getRandomHatInfo", Integer.TYPE, Integer.TYPE, Integer.TYPE).invoke((Object)null, r, g, b);
      } catch (Exception var4) {
         return null;
      }
   }

   public static void renderHat(Object info, float alpha, float hatScale, float mobRenderScaleX, float mobRenderScaleY, float mobRenderScaleZ, float renderYawOffset, float rotationYaw, float rotationPitch, float rotationRoll, float rotatePointVert, float rotatePointHori, float rotatePointSide, float offsetVert, float offsetHori, float offsetSide, boolean forceRender, boolean bindTexture, float renderTick) {
      try {
         Class.forName("hats.common.core.ApiHandler").getDeclaredMethod("renderHat", Object.class, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Boolean.TYPE, Boolean.TYPE, Float.TYPE).invoke((Object)null, info, alpha, hatScale, mobRenderScaleX, mobRenderScaleY, mobRenderScaleZ, renderYawOffset, rotationYaw, rotationPitch, rotationRoll, rotatePointVert, rotatePointHori, rotatePointSide, offsetVert, offsetHori, offsetSide, forceRender, bindTexture, renderTick);
      } catch (Exception var20) {
      }

   }
}
