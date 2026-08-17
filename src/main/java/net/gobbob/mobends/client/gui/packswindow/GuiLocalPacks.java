package net.gobbob.mobends.client.gui.packswindow;

import java.io.IOException;
import java.util.LinkedList;
import net.gobbob.mobends.MoBends;
import net.gobbob.mobends.client.gui.GuiDragger;
import net.gobbob.mobends.pack.BendsPack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.StatCollector;

public class GuiLocalPacks extends Gui {
   private final GuiPackList availablePacksList;
   private final GuiPackList appliedPacksList;
   private final LinkedList<GuiPackEntry> availablePacks;
   private final LinkedList<GuiPackEntry> appliedPacks;
   private final GuiDragger<GuiPackEntry> dragger;
   private final FontRenderer fontRenderer;
   private int x;
   private int y;

   public GuiLocalPacks() {
      this.fontRenderer = Minecraft.getMinecraft().fontRenderer;
      this.availablePacks = new LinkedList<GuiPackEntry>();
      this.appliedPacks = new LinkedList<GuiPackEntry>();
      this.availablePacksList = new GuiPackList(this.availablePacks);
      this.appliedPacksList = new GuiPackList(this.appliedPacks);
      this.dragger = new GuiDragger<GuiPackEntry>();
      this.resetPackLists();
      this.availablePacksList.setElementClickedListener(new GuiPackList.IElementClickedListener() {
         public void onElementClicked(GuiPackEntry element) {
            for(GuiPackEntry entry : GuiLocalPacks.this.appliedPacksList.getListElements()) {
               entry.setSelected(false);
            }

            GuiLocalPacks.this.dragger.setDraggedElement(element);
         }
      });
      this.appliedPacksList.setElementClickedListener(new GuiPackList.IElementClickedListener() {
         public void onElementClicked(GuiPackEntry element) {
            for(GuiPackEntry entry : GuiLocalPacks.this.availablePacksList.getListElements()) {
               entry.setSelected(false);
            }

            GuiLocalPacks.this.dragger.setDraggedElement(element);
         }
      });
   }

   public void initGui(int x, int y) {
      this.x = x;
      this.y = y;
      this.availablePacksList.initGui(x + 9, y + 23);
      this.appliedPacksList.initGui(x + GuiPacksWindow.EDITOR_WIDTH - GuiPackList.WIDTH - 1, y + 23);
   }

   public boolean mouseClicked(int mouseX, int mouseY, int button) {
      boolean eventHandled = false;
      eventHandled |= this.availablePacksList.handleMouseClicked(mouseX, mouseY, button);
      eventHandled |= this.appliedPacksList.handleMouseClicked(mouseX, mouseY, button);
      return eventHandled;
   }

   public void mouseReleased(int mouseX, int mouseY, int button) {
      this.availablePacksList.handleMouseReleased(mouseX, mouseY, button);
      this.appliedPacksList.handleMouseReleased(mouseX, mouseY, button);
      this.dragger.stopDragging();
      this.resolveAppliedPacks();
   }

   public boolean handleMouseInput() {
      boolean handled = this.availablePacksList.handleMouseInput();
      handled |= this.appliedPacksList.handleMouseInput();
      return handled;
   }

   public void update(int mouseX, int mouseY) {
      this.availablePacksList.update(mouseX, mouseY);
      this.appliedPacksList.update(mouseX, mouseY);
      this.dragger.update(mouseX, mouseY);
      GuiPackEntry element = this.dragger.getDraggedElement();
      if (element != null) {
         GuiPackList list = mouseX < this.x + GuiPacksWindow.EDITOR_WIDTH / 2 ? this.availablePacksList : this.appliedPacksList;
         int y = element.getDragY() - element.getDragPivotY() - list.getY() + list.getScrollAmount() + element.getHeight() / 2;
         int order = y / (element.getHeight() + list.getSpacing());
         if (list != element.getParentList()) {
            if (list == this.appliedPacksList) {
               this.moveAppliedPacksToUnusedExcept(element);
            }

            element.getParentList().removeElement(element);
            list.insertOrMoveElement(element, order);
            element.setParentList(list);
         } else if (order != element.getOrder()) {
            list.insertOrMoveElement(element, order);
         }
      }
   }

   public void draw(float partialTicks) {
      this.availablePacksList.draw(partialTicks);
      this.appliedPacksList.draw(partialTicks);
      this.drawCenteredString(this.fontRenderer, StatCollector.translateToLocal("mobends.gui.unusedpacks"), this.x + GuiPacksWindow.EDITOR_WIDTH / 4, this.y + 8, 16777215);
      this.drawCenteredString(this.fontRenderer, StatCollector.translateToLocal("mobends.gui.appliedpacks"), this.x + GuiPacksWindow.EDITOR_WIDTH * 3 / 4 + 6, this.y + 8, 16777215);
      GuiPackEntry element = this.dragger.getDraggedElement();
      if (element != null) {
         element.draw(partialTicks);
      }
   }

   private void moveAppliedPacksToUnusedExcept(GuiPackEntry keep) {
      LinkedList<GuiPackEntry> existing = new LinkedList<GuiPackEntry>(this.appliedPacksList.getListElements());

      for(GuiPackEntry entry : existing) {
         if (entry != keep) {
            this.appliedPacksList.removeElement(entry);
            this.availablePacksList.addElement(entry);
            entry.setParentList(this.availablePacksList);
         }
      }
   }

   public void resetPackLists() {
      this.availablePacksList.clearElements();
      this.appliedPacksList.clearElements();

      for(int i = 0; i < BendsPack.bendsPacks.size(); ++i) {
         BendsPack pack = (BendsPack)BendsPack.bendsPacks.get(i);
         GuiPackEntry entry = new GuiPackEntry(pack);
         if (i == BendsPack.currentPack) {
            entry.setParentList(this.appliedPacksList);
            this.appliedPacksList.addElement(entry);
         } else {
            entry.setParentList(this.availablePacksList);
            this.availablePacksList.addElement(entry);
         }
      }
   }

   private void resolveAppliedPacks() {
      int newIndex = 0;
      if (!this.appliedPacks.isEmpty()) {
         GuiPackEntry applied = (GuiPackEntry)this.appliedPacks.getFirst();
         int found = BendsPack.bendsPacks.indexOf(applied.getPack());
         if (found >= 0) {
            newIndex = found;
         }
      }

      if (newIndex != BendsPack.currentPack) {
         if (BendsPack.currentPack != 0) {
            try {
               BendsPack.getCurrentPack().save();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }

         BendsPack.currentPack = newIndex;

         try {
            BendsPack.getCurrentPack().apply();
         } catch (IOException e) {
            e.printStackTrace();
         }

         MoBends.saveConfig();
      }
   }
}
