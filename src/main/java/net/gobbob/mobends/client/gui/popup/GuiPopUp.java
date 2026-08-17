package net.gobbob.mobends.client.gui.popup;

import java.util.ArrayList;
import java.util.List;
import net.gobbob.mobends.client.gui.GuiBendsMenu;
import net.gobbob.mobends.client.gui.elements.GuiCustomButton;
import net.gobbob.mobends.util.Draw;
import net.gobbob.mobends.util.GUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.opengl.GL11;

public class GuiPopUp {
   protected int x;
   protected int y;
   protected int width;
   protected int height;
   private String[] title;
   protected FontRenderer fontRenderer;
   private List<Button> buttons;

   public GuiPopUp(String title, ButtonProps[] buttonProps) {
      this(title, 120, 60, buttonProps);
   }

   public GuiPopUp(String title, int width, int height, ButtonProps[] buttonProps) {
      this.fontRenderer = Minecraft.getMinecraft().fontRenderer;
      this.width = width;
      this.height = height;
      this.title = GUtil.wrapText(this.fontRenderer, title, width - 20);
      this.buttons = new ArrayList<Button>();

      for(int i = 0; i < buttonProps.length; ++i) {
         this.buttons.add(new Button(this.fontRenderer, buttonProps[i]));
      }
   }

   public void initGui(int x, int y) {
      this.x = x - this.width / 2;
      this.y = y - this.height / 2;
      int offset = this.x + this.width - 5;

      for(int i = 0; i < this.buttons.size(); ++i) {
         Button button = (Button)this.buttons.get(i);
         button.buttonUI.setPosition(offset - button.buttonUI.width, this.y + this.height - 25);
         offset -= button.buttonUI.width + 5;
      }
   }

   public void update(int mouseX, int mouseY) {
   }

   public void display(int mouseX, int mouseY, float partialTicks) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      Minecraft.getMinecraft().renderEngine.bindTexture(GuiBendsMenu.ICONS_TEXTURE);
      Draw.texturedModalRect(this.x - 4, this.y - 4, 60, 64, 4, 4);
      Draw.texturedModalRect(this.x, this.y - 4, this.width, 4, 64, 64, 1, 4);
      Draw.texturedModalRect(this.x + this.width, this.y - 4, 65, 64, 4, 4);
      Draw.texturedModalRect(this.x + this.width, this.y, 4, this.height, 65, 68, 4, 1);
      Draw.texturedModalRect(this.x + this.width, this.y + this.height, 65, 69, 4, 4);
      Draw.texturedModalRect(this.x, this.y + this.height, this.width, 4, 64, 69, 1, 4);
      Draw.texturedModalRect(this.x - 4, this.y + this.height, 60, 69, 4, 4);
      Draw.texturedModalRect(this.x - 4, this.y, 4, this.height, 60, 68, 4, 1);
      Draw.texturedModalRect(this.x, this.y, this.width, this.height, 64, 68, 1, 1);
      int yOffset = 6;

      for(int i = 0; i < this.title.length; ++i) {
         String line = this.title[i];
         this.fontRenderer.drawStringWithShadow(line, this.x + (this.width - this.fontRenderer.getStringWidth(line)) / 2, this.y + yOffset, 16777215);
         yOffset += 9;
      }

      for(int i = 0; i < this.buttons.size(); ++i) {
         ((Button)this.buttons.get(i)).buttonUI.drawButton(mouseX, mouseY, partialTicks);
      }
   }

   public void mouseClicked(int mouseX, int mouseY, int state) {
      for(int i = 0; i < this.buttons.size(); ++i) {
         Button button = (Button)this.buttons.get(i);
         if (button.buttonUI.mousePressed(mouseX, mouseY)) {
            button.props.action.performAction();
            return;
         }
      }
   }

   public void keyTyped(char typedChar, int keyCode) {
   }

   public interface ButtonAction {
      void performAction();
   }

   public static class ButtonProps {
      private String label;
      private ButtonAction action;

      public ButtonProps(String label, ButtonAction action) {
         this.label = label;
         this.action = action;
      }
   }

   public static class Button {
      public ButtonProps props;
      public GuiCustomButton buttonUI;

      public Button(FontRenderer fontRenderer, ButtonProps props) {
         this.props = props;
         int labelWidth = fontRenderer.getStringWidth(props.label);
         this.buttonUI = new GuiCustomButton(Math.max(labelWidth + 10, 50), 20, props.label);
      }
   }
}
