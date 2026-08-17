package net.gobbob.mobends.client.gui.settingswindow;

import java.util.LinkedList;
import net.gobbob.mobends.client.gui.elements.GuiList;
import net.gobbob.mobends.client.gui.packswindow.GuiPacksWindow;
import net.gobbob.mobends.util.Draw;
import net.minecraft.client.Minecraft;

public class GuiBenderList extends GuiList<GuiBenderSettings> {
   private final LinkedList<GuiBenderSettings> elements;

   public GuiBenderList(int x, int y, int width, int height) {
      super(x, y, width, height, 5, 3, 3, 15);
      this.elements = new LinkedList<GuiBenderSettings>();
   }

   protected void drawBackground(float partialTicks) {
      Minecraft.getMinecraft().renderEngine.bindTexture(GuiPacksWindow.BACKGROUND_TEXTURE);
      Draw.borderBox(0, 0, this.width, this.height, 4, 36, 117);
   }

   public LinkedList<GuiBenderSettings> getListElements() {
      return this.elements;
   }

   protected int getScrollSpeed() {
      return 20;
   }
}
