package net.gobbob.mobends.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

public class UIScissorHelper {
   public static final UIScissorHelper INSTANCE = new UIScissorHelper();
   private int x;
   private int y;
   private int width;
   private int height;

   public void setUIBounds(int uiX, int uiY, int uiWidth, int uiHeight) {
      Minecraft mc = Minecraft.getMinecraft();
      ScaledResolution scaledResolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
      int scaledWidth = scaledResolution.getScaledWidth();
      int scaledHeight = scaledResolution.getScaledHeight();
      this.x = uiX * mc.displayWidth / scaledWidth;
      this.y = (scaledHeight - uiY - uiHeight) * mc.displayHeight / scaledHeight;
      this.width = uiWidth * mc.displayWidth / scaledWidth;
      this.height = uiHeight * mc.displayHeight / scaledHeight;
   }

   public void enable() {
      GL11.glEnable(GL11.GL_SCISSOR_TEST);
      GL11.glScissor(this.x, this.y, Math.max(0, this.width), Math.max(0, this.height));
   }

   public void disable() {
      GL11.glDisable(GL11.GL_SCISSOR_TEST);
   }
}
