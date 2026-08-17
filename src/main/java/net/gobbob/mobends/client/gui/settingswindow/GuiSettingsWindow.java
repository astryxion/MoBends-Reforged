package net.gobbob.mobends.client.gui.settingswindow;

import net.gobbob.mobends.AnimatedEntity;
import net.gobbob.mobends.MoBends;
import net.gobbob.mobends.client.gui.GuiBendsMenu;
import net.gobbob.mobends.client.gui.GuiHelper;
import net.gobbob.mobends.client.gui.elements.GuiCompactTextField;
import net.gobbob.mobends.event.EventHandler_DataUpdate;
import net.gobbob.mobends.util.Draw;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GuiSettingsWindow extends GuiScreen {
   public static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("mobends", "textures/gui/pack_window.png");
   public static final int EDITOR_WIDTH = 280;
   public static final int EDITOR_HEIGHT = 177;
   private static final int COMPONENT_BUTTON_BACK = 0;
   private int x;
   private int y;
   private GuiCompactTextField filterQueryInput;
   private final GuiBenderList bendsSettingsListUI = new GuiBenderList(0, 0, EDITOR_WIDTH - 10, EDITOR_HEIGHT - 10 - 20);
   private String filterQuery = "";

   public GuiSettingsWindow() {
      this.fetchBenders();
   }

   public void initGui() {
      super.initGui();
      Keyboard.enableRepeatEvents(true);
      this.x = (this.width - EDITOR_WIDTH) / 2;
      this.y = (this.height - EDITOR_HEIGHT) / 2;
      this.buttonList.clear();
      this.buttonList.add(new GuiButton(COMPONENT_BUTTON_BACK, 10, this.height - 30, 60, 20, StatCollector.translateToLocal("mobends.gui.back")));
      this.filterQueryInput = new GuiCompactTextField(this.fontRendererObj, this.x + 6, this.y + 6, 150, 16);
      this.filterQueryInput.setFocused(true);
      this.filterQueryInput.setPlaceholderText(StatCollector.translateToLocal("mobends.gui.search"));
      this.filterQueryInput.setText(this.filterQuery);
      this.bendsSettingsListUI.initGui(this.x + 9, this.y + 9 + 20);
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      this.mc.renderEngine.bindTexture(BACKGROUND_TEXTURE);
      Draw.borderBox(this.x + 4, this.y + 4, EDITOR_WIDTH, EDITOR_HEIGHT, 4, 36, 126);
      Draw.texturedModalRect(this.x, this.y - 13, 101, 0, 4, 16);
      Draw.texturedModalRect(this.x + 4, this.y - 13, EDITOR_WIDTH - 16, 16, 105, 0, 1, 16);
      Draw.texturedModalRect(this.x + EDITOR_WIDTH - 17, this.y - 13, 106, 0, 19, 16);
      this.bendsSettingsListUI.draw(EventHandler_DataUpdate.partialTicks);
      this.fontRendererObj.drawStringWithShadow(StatCollector.translateToLocal("mobends.gui.settings"), this.x + 6, this.y - 9, 16777215);
      this.filterQueryInput.drawTextBox();
      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   public void updateScreen() {
      super.updateScreen();
      int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
      int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
      this.bendsSettingsListUI.update(mouseX, mouseY);
      this.filterQueryInput.updateCursorCounter();
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
      super.mouseClicked(mouseX, mouseY, mouseButton);
      this.bendsSettingsListUI.handleMouseClicked(mouseX, mouseY, mouseButton);
      this.filterQueryInput.mouseClicked(mouseX, mouseY, mouseButton);
   }

   protected void mouseMovedOrUp(int mouseX, int mouseY, int mouseButton) {
      super.mouseMovedOrUp(mouseX, mouseY, mouseButton);
      this.bendsSettingsListUI.handleMouseReleased(mouseX, mouseY, mouseButton);
   }

   public void handleMouseInput() {
      super.handleMouseInput();
      this.bendsSettingsListUI.handleMouseInput();
   }

   protected void keyTyped(char typedChar, int keyCode) {
      this.filterQueryInput.textboxKeyTyped(typedChar, keyCode);
      if (!this.filterQueryInput.getText().equals(this.filterQuery)) {
         this.filterQuery = this.filterQueryInput.getText();
         this.fetchBenders();
      }

      if (keyCode == 1) {
         MoBends.saveConfig();
         GuiHelper.closeGui();
      }
   }

   protected void actionPerformed(GuiButton button) {
      if (button.id == COMPONENT_BUTTON_BACK) {
         this.goBack();
      }
   }

   private void goBack() {
      MoBends.saveConfig();
      this.mc.displayGuiScreen(new GuiBendsMenu());
   }

   public boolean doesGuiPauseGame() {
      return false;
   }

   public void fetchBenders() {
      this.bendsSettingsListUI.clearElements();
      String query = this.filterQuery == null ? "" : this.filterQuery.toLowerCase();

      for(int i = 0; i < AnimatedEntity.animatedEntities.length; ++i) {
         AnimatedEntity entity = AnimatedEntity.animatedEntities[i];
         if (query.length() == 0 || entity.displayName.toLowerCase().contains(query)) {
            this.bendsSettingsListUI.addElement(new GuiBenderSettings(entity));
         }
      }
   }
}
