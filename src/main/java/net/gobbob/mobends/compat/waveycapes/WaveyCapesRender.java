package net.gobbob.mobends.compat.waveycapes;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.gobbob.mobends.util.BendsLogger;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Soft Wavey Capes integration. Wavey Capes only redirects {@code renderCloak} inside vanilla
 * {@link RenderPlayer#renderEquippedItems}, which Mo' Bends overrides — so without this glue the
 * cape vanishes when both mods are loaded.
 */
@SideOnly(Side.CLIENT)
public final class WaveyCapesRender {

   private static final String MOD_ID = "waveycapes";
   private static final String LAYER_CLASS = "dev.tr7zw.waveycapes.renderlayers.CustomCapeRenderLayer";

   private static boolean resolved;
   private static boolean active;
   private static Object layer;
   private static Method doRenderLayer;
   private static RenderPlayer boundRenderer;

   private WaveyCapesRender() {
   }

   public static void registerIfPresent() {
      if (resolved) {
         return;
      }
      resolved = true;
      if (!Loader.isModLoaded(MOD_ID) && !classExists(LAYER_CLASS)) {
         active = false;
         return;
      }
      try {
         Class.forName(LAYER_CLASS);
         active = true;
         BendsLogger.log("Wavey Capes compatibility enabled (Mo' Bends cape deferred).", BendsLogger.INFO);
      } catch (Throwable t) {
         active = false;
         BendsLogger.log("Wavey Capes compatibility failed: " + t.getMessage(), BendsLogger.INFO);
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

   public static boolean isActive() {
      if (!resolved) {
         registerIfPresent();
      }
      return active;
   }

   private static boolean ensureLayer(RenderPlayer renderer, ModelBiped model) {
      if (layer != null && boundRenderer == renderer) {
         return doRenderLayer != null;
      }
      try {
         Class<?> layerClass = Class.forName(LAYER_CLASS);
         Constructor<?> ctor = layerClass.getConstructor(RenderPlayer.class, ModelBase.class);
         layer = ctor.newInstance(renderer, model);
         doRenderLayer = layerClass.getMethod("doRenderLayer", AbstractClientPlayer.class, Float.TYPE, Float.TYPE,
               Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Boolean.TYPE);
         boundRenderer = renderer;
         return true;
      } catch (Throwable t) {
         active = false;
         layer = null;
         doRenderLayer = null;
         BendsLogger.log("Wavey Capes layer init failed: " + t.getMessage(), BendsLogger.ERROR);
         return false;
      }
   }

   /**
    * Renders Wavey Capes attached to the Mo' Bends body (follows spine bends).
    * Sets up a vanilla-like cloak matrix so Wavey's {@code atVanillaCloakMatrix} path can undo lean
    * and apply its own simulation.
    */
   public static void renderCape(RenderPlayer renderer, AbstractClientPlayer player, ModelBiped model, float partialTicks) {
      if (!isActive() || renderer == null || player == null || model == null) {
         return;
      }
      if (!ensureLayer(renderer, model)) {
         return;
      }

      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPushMatrix();
      float scale = 0.0625F;
      // Mo' Bends crouch is bone lean only — skip vanilla sneak +4px / +25° offsets.
      if (model.bipedBody != null) {
         model.bipedBody.postRender(scale);
      }

      // Same attach as Mo' Bends / 1.12.2 cape — Wavey then undoes the lean below.
      GL11.glTranslatef(0.0F, -12.0F * scale, 2.2F * scale);

      double d3 = player.field_71091_bM + (player.field_71094_bP - player.field_71091_bM) * (double)partialTicks
            - (player.prevPosX + (player.posX - player.prevPosX) * (double)partialTicks);
      double d4 = player.field_71096_bN + (player.field_71095_bQ - player.field_71096_bN) * (double)partialTicks
            - (player.prevPosY + (player.posY - player.prevPosY) * (double)partialTicks);
      double d0 = player.field_71097_bO + (player.field_71085_bR - player.field_71097_bO) * (double)partialTicks
            - (player.prevPosZ + (player.posZ - player.prevPosZ) * (double)partialTicks);
      float f4 = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks;
      double d1 = (double)MathHelper.sin(f4 * (float)Math.PI / 180.0F);
      double d2 = (double)(-MathHelper.cos(f4 * (float)Math.PI / 180.0F));
      float f5 = (float)d4 * 10.0F;
      if (f5 < -6.0F) {
         f5 = -6.0F;
      }
      if (f5 > 32.0F) {
         f5 = 32.0F;
      }

      float f6 = (float)(d3 * d1 + d0 * d2) * 100.0F;
      float f7 = (float)(d3 * d2 - d0 * d1) * 100.0F;
      if (f6 < 0.0F) {
         f6 = 0.0F;
      }

      float f8 = player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * partialTicks;
      f5 += MathHelper.sin((player.prevDistanceWalkedModified
            + (player.distanceWalkedModified - player.prevDistanceWalkedModified) * partialTicks) * 6.0F) * 32.0F * f8;

      GL11.glRotatef(6.0F + f6 / 2.0F + f5, 1.0F, 0.0F, 0.0F);
      GL11.glRotatef(f7 / 2.0F, 0.0F, 0.0F, 1.0F);
      GL11.glRotatef(-f7 / 2.0F, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);

      try {
         doRenderLayer.invoke(layer, player, Float.valueOf(0.0F), Float.valueOf(0.0F), Float.valueOf(partialTicks),
               Float.valueOf(0.0F), Float.valueOf(0.0F), Float.valueOf(0.0F), Float.valueOf(0.0F), Boolean.TRUE);
      } catch (Throwable t) {
         Throwable cause = t.getCause() != null ? t.getCause() : t;
         BendsLogger.log("Wavey Capes render failed: " + cause.getMessage(), BendsLogger.ERROR);
      }

      GL11.glPopMatrix();
   }
}
