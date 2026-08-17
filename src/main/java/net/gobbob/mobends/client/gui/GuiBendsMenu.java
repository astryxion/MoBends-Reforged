package net.gobbob.mobends.client.gui;

import net.gobbob.mobends.WebAPI;
import net.gobbob.mobends.client.gui.elements.GuiSectionButton;
import net.gobbob.mobends.client.gui.packswindow.GuiPacksWindow;
import net.gobbob.mobends.client.gui.popup.GuiEditorNotFound;
import net.gobbob.mobends.client.gui.popup.GuiPopUp;
import net.gobbob.mobends.client.gui.settingswindow.GuiSettingsWindow;
import net.gobbob.mobends.util.Draw;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiBendsMenu extends GuiScreen {
   private static final ResourceLocation MENU_TITLE_TEXTURE = new ResourceLocation("mobends", "textures/gui/title.png");
   public static final ResourceLocation ICONS_TEXTURE = new ResourceLocation("mobends", "textures/gui/icons.png");
   private GuiSectionButton settingsButton;
   private GuiSectionButton packsButton;
   private GuiSectionButton customizeButton;
   private GuiPopUp popUp;

   public GuiBendsMenu() {
      Keyboard.enableRepeatEvents(true);
      this.settingsButton = (new GuiSectionButton(StatCollector.translateToLocal("mobends.gui.section.settings"), 0xFFDA3A00)).setLeftIcon(0, 43, 19, 19).setRightIcon(19, 43, 19, 19);
      this.packsButton = (new GuiSectionButton(StatCollector.translateToLocal("mobends.gui.section.packs"), 0xFF4577DE)).setLeftIcon(38, 43, 23, 20).setRightIcon(38, 43, 23, 20);
      this.customizeButton = (new GuiSectionButton(StatCollector.translateToLocal("mobends.gui.section.customize"), 0xFF26DAA3)).setLeftIcon(80, 43, 19, 14).setRightIcon(80, 43, 19, 14);
      this.popUp = null;
   }

   public void initGui() {
      super.initGui();
      this.buttonList.clear();
      if (this.popUp != null) {
         this.popUp.initGui(this.width / 2, this.height / 2);
      }

      int startY = this.height / 2 - 32;
      int distance = 49;
      this.settingsButton.initGui((this.width - 318) / 2, startY);
      this.packsButton.initGui((this.width - 318) / 2, startY + distance);
      this.customizeButton.initGui((this.width - 318) / 2, startY + distance * 2);
   }

   protected void keyTyped(char typedChar, int keyCode) {
      if (this.popUp == null) {
         if (keyCode == 1) {
            GuiHelper.closeGui();
         }
      }
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   public void updateScreen() {
      int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
      int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
      if (this.popUp != null) {
         this.popUp.update(mouseX, mouseY);
      } else {
         this.settingsButton.update(mouseX, mouseY);
         this.packsButton.update(mouseX, mouseY);
         this.customizeButton.update(mouseX, mouseY);
      }
   }

   protected void mouseClicked(int x, int y, int state) {
      if (this.popUp != null) {
         this.popUp.mouseClicked(x, y, state);
      } else if (this.settingsButton.mouseClicked(x, y, state)) {
         this.mc.displayGuiScreen(new GuiSettingsWindow());
      } else if (this.packsButton.mouseClicked(x, y, state)) {
         this.mc.displayGuiScreen(new GuiPacksWindow());
      } else if (this.customizeButton.mouseClicked(x, y, state)) {
         AnimationEditorRegistry.IAnimationEditor editor = AnimationEditorRegistry.INSTANCE.getPrimaryEditor();
         if (editor == null) {
            this.openPopUp(new GuiEditorNotFound(new GuiPopUp.ButtonAction() {
               public void performAction() {
                  GuiBendsMenu.this.closePopUp();
               }
            }, new GuiPopUp.ButtonAction() {
               public void performAction() {
                  GuiEditorNotFound editorNotFoundPopup = (GuiEditorNotFound)GuiBendsMenu.this.popUp;
                  String downloadUrl = WebAPI.INSTANCE.getOfficialAnimationEditorUrl();
                  if (downloadUrl == null || !GuiHelper.openUrlInBrowser(downloadUrl)) {
                     editorNotFoundPopup.setErrorOccurred(true);
                  } else {
                     GuiBendsMenu.this.closePopUp();
                  }
               }
            }));
         } else {
            editor.openEditorGui();
         }
      }

      super.mouseClicked(x, y, state);
   }

   protected void mouseMovedOrUp(int mouseX, int mouseY, int state) {
      super.mouseMovedOrUp(mouseX, mouseY, state);
      this.settingsButton.mouseReleased(mouseX, mouseY, state);
      this.packsButton.mouseReleased(mouseX, mouseY, state);
      this.customizeButton.mouseReleased(mouseX, mouseY, state);
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      GL11.glDisable(GL11.GL_LIGHTING);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.renderEngine.bindTexture(MENU_TITLE_TEXTURE);
      int titleWidth = 334;
      int titleHeight = 74;
      Draw.texturedRectangle((this.width - titleWidth) / 2, (this.height - titleHeight) / 2 - 70, titleWidth, titleHeight, 0.0F, 0.0F, 1.0F, 1.0F);
      this.settingsButton.display();
      this.packsButton.display();
      this.customizeButton.display();
      super.drawScreen(mouseX, mouseY, partialTicks);
      if (this.popUp != null) {
         GL11.glDisable(GL11.GL_DEPTH_TEST);
         this.drawDefaultBackground();
         this.popUp.display(mouseX, mouseY, partialTicks);
         GL11.glEnable(GL11.GL_DEPTH_TEST);
      }
   }

   public boolean doesGuiPauseGame() {
      return false;
   }

   private void closePopUp() {
      this.popUp = null;
      this.initGui();
   }

   private void openPopUp(GuiPopUp popUp) {
      this.popUp = popUp;
      this.initGui();
   }
}
