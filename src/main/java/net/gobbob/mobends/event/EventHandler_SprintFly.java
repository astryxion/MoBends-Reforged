package net.gobbob.mobends.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.gobbob.mobends.settings.SettingsBoolean;
import net.gobbob.mobends.settings.SettingsNode;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;

import java.lang.reflect.Field;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * 1.8+ / 1.12.2 creative flight doubles speed while sprinting ({@code Ctrl+W}).
 * 1.7.10 does not — apply a fixed 2× vanilla fly-speed while flying and sprinting.
 * <p>
 * Uses absolute speeds (never multiplies the current value) so client/server tick
 * interplay cannot compound into runaway flight.
 */
public class EventHandler_SprintFly {

   private static final float VANILLA_FLY_SPEED = 0.05F;
   private static final float SPRINT_FLY_SPEED = VANILLA_FLY_SPEED * 2.0F;

   private static Field flySpeedField;
   private static boolean fieldResolved;

   /** Players we currently have at sprint-fly speed (identity keys — client vs server are separate). */
   private static final Map boosted = new IdentityHashMap();

   @SubscribeEvent
   public void onPlayerTick(TickEvent.PlayerTickEvent event) {
      if (event.phase != TickEvent.Phase.START || event.player == null) {
         return;
      }

      EntityPlayer player = event.player;
      PlayerCapabilities caps = player.capabilities;
      if (caps == null) {
         return;
      }

      // Heal any previously compounded / corrupted fly speed immediately.
      float current = caps.getFlySpeed();
      if (current > SPRINT_FLY_SPEED * 1.5F || current < 0.0F || Float.isNaN(current) || Float.isInfinite(current)) {
         setFlySpeed(caps, VANILLA_FLY_SPEED);
         boosted.remove(player);
         current = VANILLA_FLY_SPEED;
      }

      SettingsBoolean setting = (SettingsBoolean) SettingsNode.getSetting("sprintFlyBoost");
      boolean enabled = setting == null || setting.data;
      boolean shouldBoost = enabled && caps.isFlying && player.isSprinting();

      if (shouldBoost) {
         setFlySpeed(caps, SPRINT_FLY_SPEED);
         boosted.put(player, Boolean.TRUE);
      } else if (boosted.remove(player) != null) {
         setFlySpeed(caps, VANILLA_FLY_SPEED);
      } else if (Math.abs(current - SPRINT_FLY_SPEED) < 0.0001F && !(caps.isFlying && player.isSprinting())) {
         // Left sprint-fly without a map entry (e.g. after reload) — snap back to vanilla.
         setFlySpeed(caps, VANILLA_FLY_SPEED);
      }
   }

   private static void setFlySpeed(PlayerCapabilities capabilities, float speed) {
      if (capabilities == null) {
         return;
      }
      // setFlySpeed is @SideOnly(CLIENT) in 1.7.10 — set the field on both sides via reflection.
      try {
         if (!fieldResolved) {
            fieldResolved = true;
            try {
               flySpeedField = PlayerCapabilities.class.getDeclaredField("flySpeed");
            } catch (NoSuchFieldException e) {
               flySpeedField = PlayerCapabilities.class.getDeclaredField("field_75096_f");
            }
            flySpeedField.setAccessible(true);
         }
         if (flySpeedField != null) {
            flySpeedField.setFloat(capabilities, speed);
         }
      } catch (Throwable t) {
         try {
            capabilities.setFlySpeed(speed);
         } catch (Throwable ignored) {
         }
      }
   }
}
