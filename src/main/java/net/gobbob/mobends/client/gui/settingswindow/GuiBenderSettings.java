package net.gobbob.mobends.client.gui.settingswindow;

import net.gobbob.mobends.AnimatedEntity;
import net.gobbob.mobends.MoBends;
import net.gobbob.mobends.client.gui.elements.GuiSmallToggleButton;
import net.gobbob.mobends.client.gui.elements.IGuiListElement;
import net.gobbob.mobends.settings.SettingsBoolean;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class GuiBenderSettings implements IGuiListElement {
   private final String label;
   private final AnimatedEntity entity;
   private final SettingsBoolean setting;
   private final Minecraft mc;
   private final GuiSmallToggleButton toggleButton;
   private int x;
   private int y;
   private int listOrder;

   public GuiBenderSettings(AnimatedEntity entity) {
      this.entity = entity;
      this.setting = null;
      this.label = entity.displayName;
      this.mc = Minecraft.getMinecraft();
      this.toggleButton = new GuiSmallToggleButton();
      this.toggleButton.setToggleState(entity.animate);
   }

   public GuiBenderSettings(SettingsBoolean setting) {
      this.entity = null;
      this.setting = setting;
      this.label = setting.displayName;
      this.mc = Minecraft.getMinecraft();
      this.toggleButton = new GuiSmallToggleButton();
      this.toggleButton.setToggleState(setting.data);
   }

   public void initGui(int x, int y) {
      this.x = x;
      this.y = y;
      this.toggleButton.initGui(x + 4, y + 4);
   }

   public boolean handleMouseClicked(int mouseX, int mouseY, int mouseButton) {
      if (this.toggleButton.mouseClicked(mouseX, mouseY, mouseButton)) {
         if (this.entity != null) {
            this.entity.animate = this.toggleButton.getToggleState();
            ++MoBends.refreshModel;
         } else if (this.setting != null) {
            this.setting.data = this.toggleButton.getToggleState();
         }

         MoBends.saveConfig();
         return true;
      } else {
         return false;
      }
   }

   public void update(int mouseX, int mouseY) {
      this.toggleButton.update(mouseX, mouseY);
   }

   public void draw(float partialTicks) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.fontRenderer.drawStringWithShadow(this.label, this.x + 38, this.y + 10, 16777215);
      this.toggleButton.draw();
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public int getHeight() {
      return 20;
   }

   public int getOrder() {
      return this.listOrder;
   }

   public void setOrder(int order) {
      this.listOrder = order;
   }
}
