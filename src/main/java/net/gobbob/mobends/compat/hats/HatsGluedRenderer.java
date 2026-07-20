package net.gobbob.mobends.compat.hats;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Renders a Hats model already in Mo' Bends head-space (after body/head postRender).
 * Mirrors the 1.12.2 Hats×Mo'Bends glued path: flip upright and sit on top of the head,
 * without Hats' world-space yaw / rotatePoint pipeline.
 */
@SideOnly(Side.CLIENT)
public final class HatsGluedRenderer {

   private static final String GUI_HAT_SELECTION = "hats.client.gui.GuiHatSelection";
   private static final float HEAD_TOP_NUDGE = 8.0F * 0.0625F;

   private static boolean resolved;
   private static Field tickHandlerClientField;
   private static Field playerHatsField;
   private static Field mobHatsField;
   private static Field hatInfoField;
   private static Field doNotRenderField;
   private static Field hatNameField;
   private static Field renderFlagField;
   private static Field colourRField;
   private static Field colourGField;
   private static Field colourBField;
   private static Field alphaField;
   private static Field prevColourRField;
   private static Field prevColourGField;
   private static Field prevColourBField;
   private static Field prevAlphaField;
   private static Field recolourField;
   private static Field modelsField;
   private static Field currentHatRendersField;
   private static Field requestedHatsField;
   private static Field reloadingHatsField;
   private static Method modelRenderMethod;
   private static Method requestHatMethod;

   private HatsGluedRenderer() {
   }

   public static void renderPlayerHat(EntityPlayer player, float partialTicks) {
      if (!HatsCompat.isActive()) {
         return;
      }
      Object hatEnt = getPlayerHatEntity(player);
      renderHatEntity(hatEnt, partialTicks, true);
   }

   public static void renderMobHat(EntityLivingBase entity, float partialTicks) {
      if (!HatsCompat.isActive()) {
         return;
      }
      Object hatEnt = getMobHatEntity(entity);
      renderHatEntity(hatEnt, partialTicks, false);
   }

   private static void renderHatEntity(Object hatEnt, float partialTicks, boolean isPlayer) {
      if (hatEnt == null) {
         return;
      }
      ensureReflect();
      if (hatInfoField == null || modelsField == null || modelRenderMethod == null) {
         return;
      }

      try {
         if (isHatSelectionGuiOpen() && renderFlagField != null) {
            renderFlagField.setBoolean(hatEnt, true);
         }
         hatEnt.getClass().getMethod("validateHatInfo").invoke(hatEnt);

         Object info = hatInfoField.get(hatEnt);
         if (info == null) {
            return;
         }
         if (doNotRenderField != null && doNotRenderField.getBoolean(info)) {
            return;
         }

         String name = hatNameField != null ? (String) hatNameField.get(info) : null;
         if (name == null || name.isEmpty()) {
            return;
         }

         @SuppressWarnings("unchecked")
         Map<String, Object> models = (Map<String, Object>) modelsField.get(null);
         Object model = models == null ? null : models.get(name);
         if (model == null) {
            requestMissingHat(name);
            return;
         }

         if (!isPlayer && currentHatRendersField != null) {
            Object tickHandler = tickHandlerClientField.get(null);
            if (tickHandler != null) {
               int current = currentHatRendersField.getInt(tickHandler);
               // Soft limit — players always render; mobs respect Hats' max when available.
               if (current >= 200) {
                  return;
               }
            }
         }

         drawGlued(info, model, 1.0F, partialTicks);

         if (currentHatRendersField != null) {
            Object tickHandler = tickHandlerClientField.get(null);
            if (tickHandler != null) {
               currentHatRendersField.setInt(tickHandler, currentHatRendersField.getInt(tickHandler) + 1);
            }
         }
      } catch (Throwable ignored) {
      }
   }

   /**
    * Same glued transform as 1.12.2 HatsHeadGluedRenderer, adapted for 1.7.10 / LWJGL2.
    */
   private static void drawGlued(Object info, Object model, float alpha, float renderTick) throws Exception {
      GL11.glAlphaFunc(516, 0.001F);
      GL11.glPushMatrix();
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
      GL11.glScalef(1.001F, 1.001F, 1.001F);

      applyHatColour(info, alpha, renderTick);

      // Face the same way as Hats' constant Tabula/Techne portion…
      GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
      // …then flip upright for head-space (Hats' world pipeline leaves this inverted here).
      GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
      GL11.glTranslatef(0.0F, 1.0F, 0.0F);
      GL11.glTranslatef(0.0F, -1.0F, 0.0F);
      GL11.glTranslatef(0.0F, 1.0F, 0.0F);
      // Head bone origin is at the neck; nudge to the crown (8 model pixels).
      GL11.glTranslatef(0.0F, HEAD_TOP_NUDGE, 0.0F);
      GL11.glScalef(-1.0F, -1.0F, 1.0F);

      modelRenderMethod.invoke(model, Boolean.TRUE, Float.valueOf(0.0625F));

      GL11.glDisable(GL11.GL_BLEND);
      GL11.glPopMatrix();
      GL11.glAlphaFunc(516, 0.1F);
   }

   private static void applyHatColour(Object info, float alpha, float renderTick) throws Exception {
      int colourR = colourRField.getInt(info);
      int colourG = colourGField.getInt(info);
      int colourB = colourBField.getInt(info);
      int colourA = alphaField.getInt(info);
      int recolour = recolourField != null ? recolourField.getInt(info) : 0;

      if (recolour > 0 && prevColourRField != null) {
         float diffR = (float) (colourR - prevColourRField.getInt(info));
         float diffG = (float) (colourG - prevColourGField.getInt(info));
         float diffB = (float) (colourB - prevColourBField.getInt(info));
         float diffA = (float) (colourA - prevAlphaField.getInt(info));
         float f = (float) recolour - renderTick;
         diffR *= f / 20.0F;
         diffG *= f / 20.0F;
         diffB *= f / 20.0F;
         diffA *= f / 20.0F;
         GL11.glColor4f(
               ((float) colourR - diffR) / 255.0F,
               ((float) colourG - diffG) / 255.0F,
               ((float) colourB - diffB) / 255.0F,
               MathHelper.clamp_float(alpha * (((float) colourA - diffA) / 255.0F), 0.0F, 1.0F));
      } else {
         GL11.glColor4f(
               (float) colourR / 255.0F,
               (float) colourG / 255.0F,
               (float) colourB / 255.0F,
               MathHelper.clamp_float(alpha * ((float) colourA / 255.0F), 0.0F, 1.0F));
      }
   }

   private static void requestMissingHat(String name) {
      try {
         if (reloadingHatsField != null && reloadingHatsField.getBoolean(null)) {
            return;
         }
         Object tickHandler = tickHandlerClientField.get(null);
         if (tickHandler == null || requestedHatsField == null || requestHatMethod == null) {
            return;
         }
         @SuppressWarnings("unchecked")
         java.util.Collection<String> requested = (java.util.Collection<String>) requestedHatsField.get(tickHandler);
         if (requested != null && !requested.contains(name)) {
            requestHatMethod.invoke(null, name, null);
            requested.add(name);
         }
      } catch (Throwable ignored) {
      }
   }

   private static Object getPlayerHatEntity(EntityPlayer player) {
      ensureReflect();
      if (playerHatsField == null) {
         return null;
      }
      try {
         Object tickHandler = tickHandlerClientField.get(null);
         if (tickHandler == null) {
            return null;
         }
         @SuppressWarnings("unchecked")
         Map<String, Object> hats = (Map<String, Object>) playerHatsField.get(tickHandler);
         return hats == null ? null : hats.get(player.getCommandSenderName());
      } catch (Throwable t) {
         return null;
      }
   }

   private static Object getMobHatEntity(EntityLivingBase entity) {
      ensureReflect();
      if (mobHatsField == null) {
         return null;
      }
      try {
         Object tickHandler = tickHandlerClientField.get(null);
         if (tickHandler == null) {
            return null;
         }
         @SuppressWarnings("unchecked")
         Map<Integer, Object> hats = (Map<Integer, Object>) mobHatsField.get(tickHandler);
         return hats == null ? null : hats.get(Integer.valueOf(entity.getEntityId()));
      } catch (Throwable t) {
         return null;
      }
   }

   private static boolean isHatSelectionGuiOpen() {
      GuiScreen screen = Minecraft.getMinecraft().currentScreen;
      return screen != null && GUI_HAT_SELECTION.equals(screen.getClass().getName());
   }

   private static void ensureReflect() {
      if (resolved) {
         return;
      }
      resolved = true;
      try {
         Class<?> commonProxy = Class.forName("hats.common.core.CommonProxy");
         tickHandlerClientField = commonProxy.getField("tickHandlerClient");
         Class<?> tickHandler = Class.forName("hats.client.core.TickHandlerClient");
         playerHatsField = tickHandler.getField("hats");
         mobHatsField = tickHandler.getField("mobHats");
         currentHatRendersField = tickHandler.getField("currentHatRenders");
         requestedHatsField = tickHandler.getField("requestedHats");

         Class<?> entityHat = Class.forName("hats.common.entity.EntityHat");
         hatInfoField = entityHat.getField("info");
         renderFlagField = entityHat.getField("render");

         Class<?> hatInfo = Class.forName("hats.common.core.HatInfo");
         hatNameField = hatInfo.getField("hatName");
         colourRField = hatInfo.getField("colourR");
         colourGField = hatInfo.getField("colourG");
         colourBField = hatInfo.getField("colourB");
         alphaField = hatInfo.getField("alpha");

         Class<?> hatInfoClient = Class.forName("hats.client.core.HatInfoClient");
         doNotRenderField = hatInfoClient.getField("doNotRender");
         recolourField = hatInfoClient.getField("recolour");
         prevColourRField = hatInfoClient.getField("prevColourR");
         prevColourGField = hatInfoClient.getField("prevColourG");
         prevColourBField = hatInfoClient.getField("prevColourB");
         prevAlphaField = hatInfoClient.getField("prevAlpha");

         Class<?> clientProxy = Class.forName("hats.client.core.ClientProxy");
         modelsField = clientProxy.getField("models");

         Class<?> modelTechne = Class.forName("ichun.common.core.techne.model.ModelTechne2");
         modelRenderMethod = modelTechne.getMethod("render", Boolean.TYPE, Float.TYPE);

         Class<?> hatHandler = Class.forName("hats.common.core.HatHandler");
         reloadingHatsField = hatHandler.getField("reloadingHats");
         requestHatMethod = hatHandler.getMethod("requestHat", String.class, EntityPlayer.class);
      } catch (Throwable t) {
         tickHandlerClientField = null;
         playerHatsField = null;
         mobHatsField = null;
         hatInfoField = null;
         modelsField = null;
         modelRenderMethod = null;
      }
   }
}
