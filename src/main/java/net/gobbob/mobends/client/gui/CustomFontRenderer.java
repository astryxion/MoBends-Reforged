package net.gobbob.mobends.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;

public class CustomFontRenderer {
   protected CustomFont font;
   protected int characterSpacing = 1;

   public void setFont(CustomFont font) {
      this.font = font;
   }

   public int getTextWidth(String textToDraw) {
      if (this.font == null || textToDraw == null) {
         return 0;
      }

      int width = 0;
      for(int i = 0; i < textToDraw.length(); ++i) {
         CustomFont.Symbol symbol = this.font.getSymbol(textToDraw.charAt(i));
         if (symbol != null) {
            width += symbol.width;
            if (i != textToDraw.length() - 1) {
               width += this.characterSpacing;
            }
         }
      }

      return width;
   }

   public void drawText(String textToDraw, int x, int y) {
      if (this.font == null || textToDraw == null) {
         return;
      }

      Minecraft.getMinecraft().renderEngine.bindTexture(this.font.resourceLocation);
      Tessellator tessellator = Tessellator.instance;
      tessellator.startDrawingQuads();
      int nextCharX = x;
      for(int i = 0; i < textToDraw.length(); ++i) {
         CustomFont.Symbol symbol = this.font.getSymbol(textToDraw.charAt(i));
         if (symbol != null) {
            this.drawSymbol(symbol, tessellator, nextCharX, y);
            nextCharX += symbol.width + this.characterSpacing;
         }
      }

      tessellator.draw();
   }

   public void drawCenteredText(String textToDraw, int x, int y) {
      this.drawText(textToDraw, x - this.getTextWidth(textToDraw) / 2, y);
   }

   private void drawSymbol(CustomFont.Symbol symbol, Tessellator tessellator, int x, int y) {
      x += symbol.offsetX;
      y += symbol.offsetY;
      float textureX = (float)symbol.u / (float)this.font.atlasWidth;
      float textureY = (float)symbol.v / (float)this.font.atlasHeight;
      float textureWidth = (float)symbol.width / (float)this.font.atlasWidth;
      float textureHeight = (float)symbol.height / (float)this.font.atlasHeight;
      tessellator.addVertexWithUV((double)x, (double)y, 0.0D, (double)textureX, (double)(textureY + textureHeight));
      tessellator.addVertexWithUV((double)(x + symbol.width), (double)y, 0.0D, (double)(textureX + textureWidth), (double)(textureY + textureHeight));
      tessellator.addVertexWithUV((double)(x + symbol.width), (double)(y - symbol.height), 0.0D, (double)(textureX + textureWidth), (double)textureY);
      tessellator.addVertexWithUV((double)x, (double)(y - symbol.height), 0.0D, (double)textureX, (double)textureY);
   }
}
