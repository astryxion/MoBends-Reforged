package net.gobbob.mobends.client.gui.elements;

import net.gobbob.mobends.client.gui.GuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiCustomButton extends GuiButton {
   private final Minecraft mc;

   public GuiCustomButton(int buttonId, int width, int height) {
      super(buttonId, 0, 0, width, height, "");
      this.mc = Minecraft.getMinecraft();
   }

   public GuiCustomButton(int width, int height, String text) {
      super(0, 0, 0, width, height, text);
      this.mc = Minecraft.getMinecraft();
   }

   public GuiCustomButton setPosition(int x, int y) {
      this.xPosition = x;
      this.yPosition = y;
      return this;
   }

   public void drawButton(int mouseX, int mouseY, float partialTicks) {
      super.drawButton(this.mc, mouseX, mouseY);
   }

   public boolean mousePressed(int mouseX, int mouseY) {
      boolean clicked = this.mousePressed(this.mc, mouseX, mouseY);
      if (clicked) {
         GuiHelper.playButtonSound();
      }

      return clicked;
   }

   public GuiCustomButton setText(String text) {
      this.displayString = text;
      return this;
   }
}
