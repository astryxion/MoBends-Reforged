package net.gobbob.mobends.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.gobbob.mobends.settings.SettingsBoolean;
import net.gobbob.mobends.settings.SettingsNode;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;

/**
 * 1.13+ sprint-swimming is much faster than 1.7.10's water crawl.
 * While in water and sprinting ({@code Ctrl+W}), add look-aligned acceleration
 * so swimming feels closer to modern sprint-swim.
 */
public class EventHandler_SprintSwim {

   /** Extra acceleration along look each tick (after vanilla water drag). */
   private static final double SPRINT_SWIM_ACCEL = 0.024D;

   @SubscribeEvent
   public void onPlayerTick(TickEvent.PlayerTickEvent event) {
      if (event.phase != TickEvent.Phase.END || event.player == null) {
         return;
      }

      EntityPlayer player = event.player;
      SettingsBoolean setting = (SettingsBoolean) SettingsNode.getSetting("sprintSwimBoost");
      boolean enabled = setting == null || setting.data;
      if (!enabled) {
         return;
      }

      if (!player.isInWater() || player.capabilities.isFlying || !player.isSprinting()) {
         return;
      }
      // Only when actually pressing forward (sprint-swim), not spinning in place.
      if (player.moveForward <= 0.0F) {
         return;
      }

      Vec3 look = player.getLookVec();
      double accel = SPRINT_SWIM_ACCEL * (double) player.moveForward;
      player.motionX += look.xCoord * accel;
      player.motionY += look.yCoord * accel;
      player.motionZ += look.zCoord * accel;
   }
}
