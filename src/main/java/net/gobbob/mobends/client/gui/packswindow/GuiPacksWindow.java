package net.gobbob.mobends.client.gui.packswindow;

import net.gobbob.mobends.client.gui.GuiBendsMenu;
import net.gobbob.mobends.client.gui.GuiHelper;
import net.gobbob.mobends.util.Draw;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Mouse;

public class GuiPacksWindow extends GuiScreen {
   public static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("mobends", "textures/gui/pack_window.png");
   public static final int EDITOR_WIDTH = 280;
   public static final int EDITOR_HEIGHT = 177;
   private static final int BUTTON_BACK = 0;
   private int x;
   private int y;
   private final GuiTabNavigation tabNavigation;
   private final GuiPackTab localPacksTab;
   private final GuiPackTab publicPacksTab;
   private GuiLocalPacks localPacks;

   public GuiPacksWindow() {
      this.localPacks = new GuiLocalPacks();
      this.tabNavigation = new GuiTabNavigation();
      this.localPacksTab = this.tabNavigation.addTab("mobends.gui.localpacks", 0);
      this.publicPacksTab = this.tabNavigation.addTab("mobends.gui.publicpacks", 1);
      this.tabNavigation.selectTab(0);
   }

   public void initGui() {
      super.initGui();
      this.x = (this.width - EDITOR_WIDTH) / 2;
      this.y = (this.height - EDITOR_HEIGHT) / 2;
      this.tabNavigation.initGui(this.x + 5, this.y);
      this.buttonList.clear();
      this.buttonList.add(new GuiButton(BUTTON_BACK, 10, this.height - 30, 60, 20, StatCollector.translateToLocal("mobends.gui.back")));
      this.localPacks.initGui(this.x, this.y);
   }

   public void updateScreen() {
      super.updateScreen();
      int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
      int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
      if (this.tabNavigation.getSelectedTab() == this.localPacksTab) {
         this.localPacks.update(mouseX, mouseY);
      }
   }

   protected void mouseClicked(int mouseX, int mouseY, int button) {
      super.mouseClicked(mouseX, mouseY, button);
      this.tabNavigation.mouseClicked(mouseX, mouseY, button);
      if (this.tabNavigation.getSelectedTab() == this.localPacksTab) {
         this.localPacks.mouseClicked(mouseX, mouseY, button);
      }
   }

   protected void mouseMovedOrUp(int mouseX, int mouseY, int state) {
      super.mouseMovedOrUp(mouseX, mouseY, state);
      if (this.tabNavigation.getSelectedTab() == this.localPacksTab) {
         this.localPacks.mouseReleased(mouseX, mouseY, state);
      }
   }

   public void handleMouseInput() {
      super.handleMouseInput();
      if (this.tabNavigation.getSelectedTab() == this.localPacksTab) {
         this.localPacks.handleMouseInput();
      }
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      this.mc.renderEngine.bindTexture(BACKGROUND_TEXTURE);
      Draw.borderBox(this.x + 4, this.y + 4, EDITOR_WIDTH, EDITOR_HEIGHT, 4, 36, 126);
      Draw.texturedModalRect(this.x, this.y - 13, 101, 0, 4, 16);
      Draw.texturedModalRect(this.x + 4, this.y - 13, EDITOR_WIDTH - 16, 16, 105, 0, 1, 16);
      Draw.texturedModalRect(this.x + EDITOR_WIDTH - 17, this.y - 13, 106, 0, 19, 16);
      this.tabNavigation.draw(mouseX, mouseY);
      if (this.tabNavigation.getSelectedTab() == this.localPacksTab) {
         this.localPacks.draw(partialTicks);
      } else if (this.tabNavigation.getSelectedTab() == this.publicPacksTab) {
         String comingSoon = "Coming soon...";
         this.fontRendererObj.drawStringWithShadow(comingSoon, this.x + EDITOR_WIDTH / 2 - this.fontRendererObj.getStringWidth(comingSoon) / 2, this.y + EDITOR_HEIGHT / 2 - 10, 16777215);
      }

      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   protected void keyTyped(char typedChar, int keyCode) {
      if (keyCode == 1) {
         GuiHelper.closeGui();
      }
   }

   protected void actionPerformed(GuiButton button) {
      if (button.id == BUTTON_BACK) {
         this.goBack();
      }
   }

   private void goBack() {
      this.mc.displayGuiScreen(new GuiBendsMenu());
   }

   public boolean doesGuiPauseGame() {
      return false;
   }
}
