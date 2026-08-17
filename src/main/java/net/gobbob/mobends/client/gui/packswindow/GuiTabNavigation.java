package net.gobbob.mobends.client.gui.packswindow;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StatCollector;

public class GuiTabNavigation {
   private int x;
   private int y;
   private List<GuiPackTab> tabs;
   private GuiPackTab selectedTab;

   public GuiTabNavigation() {
      this.tabs = new ArrayList<GuiPackTab>();
      this.selectedTab = null;
   }

   public void initGui(int x, int y) {
      this.x = x;
      this.y = y;

      for(int i = 0; i < this.tabs.size(); ++i) {
         ((GuiPackTab)this.tabs.get(i)).initGui(x + i * (GuiPackTab.WIDTH - 1), y);
      }
   }

   public GuiPackTab addTab(String tabName, int textureIndex) {
      GuiPackTab tab = new GuiPackTab(tabName, textureIndex);
      this.tabs.add(tab);
      return tab;
   }

   public void draw(int mouseX, int mouseY) {
      Minecraft mc = Minecraft.getMinecraft();

      for(int i = 0; i < this.tabs.size(); ++i) {
         GuiPackTab tab = (GuiPackTab)this.tabs.get(i);
         if (this.selectedTab != tab) {
            tab.draw(mouseX, mouseY);
         }
      }

      GuiPackTab selected = this.getSelectedTab();
      if (selected != null) {
         selected.draw(mouseX, mouseY);
         mc.fontRenderer.drawStringWithShadow(StatCollector.translateToLocal(selected.titleKey), this.x + (GuiPackTab.WIDTH - 2) * this.tabs.size() + 10, this.y - 10, 16777215);
      }
   }

   public boolean mouseClicked(int mouseX, int mouseY, int button) {
      for(int i = 0; i < this.tabs.size(); ++i) {
         GuiPackTab tab = (GuiPackTab)this.tabs.get(i);
         if (tab.mouseClicked(mouseX, mouseY, button)) {
            this.selectTab(tab);
            return true;
         }
      }

      return false;
   }

   public void selectTab(int index) {
      this.selectTab((GuiPackTab)this.tabs.get(index));
   }

   public void selectTab(GuiPackTab tab) {
      this.selectedTab = tab;

      for(int i = 0; i < this.tabs.size(); ++i) {
         ((GuiPackTab)this.tabs.get(i)).setSelected(false);
      }

      this.selectedTab.setSelected(true);
   }

   public GuiPackTab getSelectedTab() {
      return this.selectedTab;
   }
}
