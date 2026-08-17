package net.gobbob.mobends.client.gui.packswindow;

import net.gobbob.mobends.util.Draw;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class GuiPackTab {
   public static final int WIDTH = 18;
   public static final int HEIGHT = 15;
   public final String titleKey;
   private final int textureIndex;
   private int x;
   private int y;
   private boolean hovered;
   private boolean selected;
   private float selectedTransitionTween;

   public GuiPackTab(String titleKey, int textureIndex) {
      this.titleKey = titleKey;
      this.textureIndex = textureIndex;
      this.hovered = false;
      this.selected = false;
      this.selectedTransitionTween = 0.0F;
   }

   public void initGui(int x, int y) {
      this.x = x;
      this.y = y;
   }

   public void update(int mouseX, int mouseY) {
      this.hovered = mouseX >= this.x && mouseX <= this.x + WIDTH && mouseY >= this.y - HEIGHT && mouseY <= this.y;
      if (this.selected) {
         this.selectedTransitionTween = Math.min(this.selectedTransitionTween + 0.2F, 1.0F);
      } else {
         this.selectedTransitionTween = Math.max(this.selectedTransitionTween - 0.2F, 0.0F);
      }
   }

   public void draw(int mouseX, int mouseY) {
      this.update(mouseX, mouseY);
      Minecraft.getMinecraft().renderEngine.bindTexture(GuiPacksWindow.BACKGROUND_TEXTURE);
      int yOffset = this.y - HEIGHT + (this.selected ? -1 : 0);
      int selectedTextureY = 147;
      int hoveredTextureY = 132;
      int neutralTextureY = 117;
      int textureY = this.hovered ? hoveredTextureY : neutralTextureY;
      Draw.texturedModalRect(this.x, yOffset, this.textureIndex * WIDTH, textureY, WIDTH, HEIGHT);
      if (this.selectedTransitionTween > 0.0F) {
         GL11.glEnable(GL11.GL_BLEND);
         GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, this.selectedTransitionTween);
         Draw.texturedModalRect(this.x, yOffset, this.textureIndex * WIDTH, selectedTextureY, WIDTH, HEIGHT);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glDisable(GL11.GL_BLEND);
      }
   }

   public boolean mouseClicked(int mouseX, int mouseY, int button) {
      this.update(mouseX, mouseY);
      return this.hovered;
   }

   public void setSelected(boolean selected) {
      this.selected = selected;
   }
}
