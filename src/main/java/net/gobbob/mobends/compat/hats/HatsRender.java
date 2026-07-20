package net.gobbob.mobends.compat.hats;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.gobbob.mobends.client.model.entity.ModelBendsSpider;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Method;

/**
 * Safe entry points for Mo' Bends renderers. Does not reference Hats types at class-load time
 * so Mo' Bends still runs when Hats is absent.
 */
@SideOnly(Side.CLIENT)
public final class HatsRender {

   private static final float SPIDER_HAT_Y = 4.0F * 0.0625F;
   private static final float SPIDER_HAT_Z = -4.0F * 0.0625F;

   private static boolean resolved;
   private static boolean active;
   private static Method renderPlayerHat;
   private static Method renderMobHat;

   private HatsRender() {
   }

   public static void registerIfPresent() {
      if (!Loader.isModLoaded("Hats")) {
         resolved = true;
         active = false;
         return;
      }
      try {
         Class<?> compat = Class.forName("net.gobbob.mobends.compat.hats.HatsCompat");
         compat.getMethod("register").invoke(null);
         active = Boolean.TRUE.equals(compat.getMethod("isActive").invoke(null));
         if (active) {
            Class<?> glued = Class.forName("net.gobbob.mobends.compat.hats.HatsGluedRenderer");
            renderPlayerHat = glued.getDeclaredMethod("renderPlayerHat", EntityPlayer.class, Float.TYPE);
            renderPlayerHat.setAccessible(true);
            renderMobHat = glued.getDeclaredMethod("renderMobHat", net.minecraft.entity.EntityLivingBase.class, Float.TYPE);
            renderMobHat.setAccessible(true);
         }
      } catch (Throwable t) {
         active = false;
      }
      resolved = true;
   }

   public static boolean isActive() {
      if (!resolved) {
         registerIfPresent();
      }
      return active;
   }

   public static void renderPlayerHat(EntityPlayer player, ModelBiped model, float partialTicks) {
      if (!isActive() || model == null || renderPlayerHat == null) {
         return;
      }
      GL11.glPushMatrix();
      if (model.bipedBody != null) {
         model.bipedBody.postRender(0.0625F);
      }
      if (model.bipedHead != null) {
         model.bipedHead.postRender(0.0625F);
      }
      try {
         renderPlayerHat.invoke(null, player, Float.valueOf(partialTicks));
      } catch (Throwable ignored) {
      }
      GL11.glPopMatrix();
   }

   public static void renderZombieHat(EntityZombie zombie, ModelBiped model, float partialTicks) {
      if (!isActive() || model == null || renderMobHat == null) {
         return;
      }
      GL11.glPushMatrix();
      if (model.bipedBody != null) {
         model.bipedBody.postRender(0.0625F);
      }
      if (model.bipedHead != null) {
         model.bipedHead.postRender(0.0625F);
      }
      try {
         renderMobHat.invoke(null, zombie, Float.valueOf(partialTicks));
      } catch (Throwable ignored) {
      }
      GL11.glPopMatrix();
   }

   public static void renderSpiderHat(EntitySpider spider, ModelBendsSpider model, float partialTicks) {
      if (!isActive() || model == null || model.spiderHead == null || renderMobHat == null) {
         return;
      }
      GL11.glPushMatrix();
      model.spiderHead.postRender(0.0625F);
      GL11.glTranslatef(0.0F, SPIDER_HAT_Y, SPIDER_HAT_Z);
      try {
         renderMobHat.invoke(null, spider, Float.valueOf(partialTicks));
      } catch (Throwable ignored) {
      }
      GL11.glPopMatrix();
   }
}
