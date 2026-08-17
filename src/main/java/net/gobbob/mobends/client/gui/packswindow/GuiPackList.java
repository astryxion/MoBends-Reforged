package net.gobbob.mobends.client.gui.packswindow;

import java.util.Iterator;
import java.util.LinkedList;
import net.gobbob.mobends.client.gui.elements.GuiList;
import net.gobbob.mobends.util.Draw;
import net.minecraft.client.Minecraft;

public class GuiPackList extends GuiList<GuiPackEntry> {
   public static final int WIDTH = 126;
   public static final int HEIGHT = 131;
   private LinkedList<GuiPackEntry> listEntries;
   private IElementClickedListener elementClickedListener;

   public GuiPackList(LinkedList<GuiPackEntry> listEntries) {
      super(0, 0, WIDTH, HEIGHT, 3, 5, 5, 5);
      this.listEntries = listEntries;
   }

   public void setElementClickedListener(IElementClickedListener listener) {
      this.elementClickedListener = listener;
   }

   public LinkedList<GuiPackEntry> getListElements() {
      return this.listEntries;
   }

   public void initGui(int x, int y) {
      super.initGui(x, y);
   }

   protected void drawBackground(float partialTicks) {
      Minecraft.getMinecraft().renderEngine.bindTexture(GuiPacksWindow.BACKGROUND_TEXTURE);
      Draw.borderBox(0, 0, this.width, this.height, 4, 36, 117);
   }

   protected void drawContent(float partialTicks) {
      for(GuiPackEntry element : this.getListElements()) {
         if (!element.isDragged()) {
            element.draw(partialTicks);
         }
      }
   }

   protected boolean handleMouseClickedElements(int mouseX, int mouseY, int button) {
      Iterator<GuiPackEntry> it = this.getListElements().descendingIterator();

      while(it.hasNext()) {
         GuiPackEntry clickedEntry = (GuiPackEntry)it.next();
         if (clickedEntry.handleMouseClicked(mouseX, mouseY, button)) {
            for(GuiPackEntry entry : this.getListElements()) {
               entry.setSelected(entry == clickedEntry);
            }

            if (this.elementClickedListener != null) {
               this.elementClickedListener.onElementClicked(clickedEntry);
            }

            return true;
         }
      }

      return false;
   }

   public interface IElementClickedListener {
      void onElementClicked(GuiPackEntry entry);
   }
}
