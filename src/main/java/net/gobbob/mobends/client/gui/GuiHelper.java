package net.gobbob.mobends.client.gui;

import java.net.URI;
import net.gobbob.mobends.util.BendsLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

public class GuiHelper {
   public static void closeGui() {
      Minecraft.getMinecraft().displayGuiScreen((net.minecraft.client.gui.GuiScreen)null);
   }

   public static void playButtonSound() {
      Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
   }

   public static boolean openUrlInBrowser(String url) {
      try {
         Class oclass = Class.forName("java.awt.Desktop");
         Object object = oclass.getMethod("getDesktop").invoke((Object)null);
         oclass.getMethod("browse", URI.class).invoke(object, new URI(url));
         return true;
      } catch (Throwable t) {
         BendsLogger.log("Couldn't open link " + url, BendsLogger.INFO);
         return false;
      }
   }
}
