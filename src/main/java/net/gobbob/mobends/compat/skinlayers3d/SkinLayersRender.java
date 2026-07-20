package net.gobbob.mobends.compat.skinlayers3d;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.gobbob.mobends.client.model.entity.ModelBendsPlayer;
import net.gobbob.mobends.util.BendsLogger;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Soft entry points for 3D Skin Layers — safe when the mod is absent.
 */
@SideOnly(Side.CLIENT)
public final class SkinLayersRender {

   private static final String MOD_ID = "skinlayers3d";
   private static final String MOD_CLASS = "dev.tr7zw.skinlayers.SkinLayersModBase";

   private static boolean resolved;
   private static boolean modPresent;
   private static boolean rendererReady;
   private static Method renderLayersMethod;
   private static Method renderFirstPersonSleeveMethod;
   private static List stockLayerFields;

   private SkinLayersRender() {
   }

   public static void registerIfPresent() {
      if (resolved) {
         return;
      }
      resolved = true;
      modPresent = Loader.isModLoaded(MOD_ID) || classExists(MOD_CLASS);
      if (!modPresent) {
         return;
      }
      try {
         Class<?> glued = Class.forName("net.gobbob.mobends.compat.skinlayers3d.SkinLayersGluedRenderer");
         glued.getMethod("init").invoke(null);
         rendererReady = Boolean.TRUE.equals(glued.getMethod("isReady").invoke(null));
         renderLayersMethod = glued.getMethod("renderLayers", AbstractClientPlayer.class, ModelBendsPlayer.class,
               Float.TYPE);
         renderFirstPersonSleeveMethod = glued.getMethod("renderFirstPersonRightSleeve", AbstractClientPlayer.class,
               net.minecraft.client.model.ModelRenderer.class);
         BendsLogger.log("3D Skin Layers compatibility enabled (head/body pivot fixes).", BendsLogger.INFO);
      } catch (Throwable t) {
         rendererReady = false;
         BendsLogger.log("3D Skin Layers glued renderer failed: " + t.getMessage(), BendsLogger.ERROR);
         t.printStackTrace();
      }
   }

   private static boolean classExists(String name) {
      try {
         Class.forName(name);
         return true;
      } catch (Throwable t) {
         return false;
      }
   }

   public static boolean isModPresent() {
      if (!resolved) {
         registerIfPresent();
      }
      return modPresent;
   }

   public static boolean isActive() {
      return isModPresent() && rendererReady;
   }

   /**
    * Temporarily clears stock feature-renderer fields so the living-entity mixin cannot draw
    * at vanilla pivots. Returns the previous field values for {@link #restoreStockFeatureRenderers}.
    */
   public static Object[] disableStockFeatureRenderers(RenderPlayer renderer) {
      if (renderer == null || !isModPresent()) {
         return null;
      }
      ensureStockLayerFields(renderer);
      if (stockLayerFields == null || stockLayerFields.isEmpty()) {
         return null;
      }
      Object[] previous = new Object[stockLayerFields.size()];
      for(int i = 0; i < stockLayerFields.size(); ++i) {
         Field field = (Field) stockLayerFields.get(i);
         try {
            previous[i] = field.get(renderer);
            if (previous[i] != null) {
               field.set(renderer, null);
            }
         } catch (Throwable ignored) {
         }
      }
      return previous;
   }

   public static void restoreStockFeatureRenderers(RenderPlayer renderer, Object[] previous) {
      if (renderer == null || previous == null || stockLayerFields == null) {
         return;
      }
      for(int i = 0; i < stockLayerFields.size() && i < previous.length; ++i) {
         Field field = (Field) stockLayerFields.get(i);
         try {
            field.set(renderer, previous[i]);
         } catch (Throwable ignored) {
         }
      }
   }

   private static void ensureStockLayerFields(RenderPlayer renderer) {
      if (stockLayerFields != null) {
         return;
      }
      List found = new ArrayList();
      Class<?> c = renderer.getClass();
      while (c != null && c != Object.class) {
         Field[] fields = c.getDeclaredFields();
         for(int i = 0; i < fields.length; ++i) {
            Field field = fields[i];
            String typeName = field.getType().getName();
            if (typeName.endsWith("HeadLayerFeatureRenderer") || typeName.endsWith("BodyLayerFeatureRenderer")) {
               try {
                  field.setAccessible(true);
                  found.add(field);
               } catch (Throwable ignored) {
               }
            }
         }
         c = c.getSuperclass();
      }
      stockLayerFields = found;
      if (!found.isEmpty()) {
         BendsLogger.log("Found " + found.size() + " stock 3D Skin Layers field(s) on player renderer.",
               BendsLogger.INFO);
      }
   }

   public static void renderPlayerLayers(AbstractClientPlayer player, ModelBiped model, float scale) {
      if (!isActive() || player == null || !(model instanceof ModelBendsPlayer) || renderLayersMethod == null) {
         return;
      }
      try {
         renderLayersMethod.invoke(null, player, (ModelBendsPlayer) model, Float.valueOf(scale));
      } catch (Throwable t) {
         Throwable cause = t.getCause() != null ? t.getCause() : t;
         BendsLogger.log("3D Skin Layers render failed: " + cause.getMessage(), BendsLogger.ERROR);
      }
   }

   /** First-person right sleeve onto the arm bone that was just drawn. Leaves third-person alone. */
   public static void renderFirstPersonRightSleeve(AbstractClientPlayer player,
         net.minecraft.client.model.ModelRenderer rightArm) {
      if (!isActive() || player == null || rightArm == null || renderFirstPersonSleeveMethod == null) {
         return;
      }
      try {
         renderFirstPersonSleeveMethod.invoke(null, player, rightArm);
      } catch (Throwable t) {
         Throwable cause = t.getCause() != null ? t.getCause() : t;
         BendsLogger.log("3D Skin Layers first-person sleeve failed: " + cause.getMessage(), BendsLogger.ERROR);
      }
   }
}
