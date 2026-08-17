package net.gobbob.mobends.util;

import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

public class Draw {
   private static final float UV = 0.00390625F;

   public static void rectangle(float x, float y, float w, float h) {
      GL11.glBegin(7);
      GL11.glTexCoord2f(0.0F, 0.0F);
      GL11.glVertex3f(x + 0.0F, y + 0.0F, 0.0F);
      GL11.glTexCoord2f(0.0F, 1.0F);
      GL11.glVertex3f(x + 0.0F, y + h, 0.0F);
      GL11.glTexCoord2f(1.0F, 1.0F);
      GL11.glVertex3f(x + w, y + h, 0.0F);
      GL11.glTexCoord2f(1.0F, 0.0F);
      GL11.glVertex3f(x + w, y + 0.0F, 0.0F);
      GL11.glEnd();
   }

   public static void rectangle(float left, float top, float width, float height, int color) {
      float a = (float)(color >> 24 & 255) / 255.0F;
      float r = (float)(color >> 16 & 255) / 255.0F;
      float g = (float)(color >> 8 & 255) / 255.0F;
      float b = (float)(color & 255) / 255.0F;
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glDisable(GL11.GL_TEXTURE_2D);
      GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
      GL11.glColor4f(r, g, b, a);
      rectangle(left, top, width, height);
      GL11.glEnable(GL11.GL_TEXTURE_2D);
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public static void rectangle_xgradient(float x, float y, float w, float h, Color color0, Color color1) {
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glHint(3152, 4354);
      GL11.glShadeModel(7425);
      GL11.glBegin(7);
      GL11.glColor4f(color0.r, color0.g, color0.b, color0.a);
      GL11.glTexCoord2f(0.0F, 0.0F);
      GL11.glVertex3f(x + 0.0F, y + 0.0F, 0.0F);
      GL11.glTexCoord2f(0.0F, 1.0F);
      GL11.glVertex3f(x + 0.0F, y + h, 0.0F);
      GL11.glColor4f(color1.r, color1.g, color1.b, color1.a);
      GL11.glTexCoord2f(1.0F, 1.0F);
      GL11.glVertex3f(x + w, y + h, 0.0F);
      GL11.glTexCoord2f(1.0F, 0.0F);
      GL11.glVertex3f(x + w, y + 0.0F, 0.0F);
      GL11.glEnd();
   }

   public static void rectangleHorizontalGradient(float x, float y, float width, float height, int color0, int color1) {
      float a0 = (float)(color0 >> 24 & 255) / 255.0F;
      float r0 = (float)(color0 >> 16 & 255) / 255.0F;
      float g0 = (float)(color0 >> 8 & 255) / 255.0F;
      float b0 = (float)(color0 & 255) / 255.0F;
      float a1 = (float)(color1 >> 24 & 255) / 255.0F;
      float r1 = (float)(color1 >> 16 & 255) / 255.0F;
      float g1 = (float)(color1 >> 8 & 255) / 255.0F;
      float b1 = (float)(color1 & 255) / 255.0F;
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glDisable(GL11.GL_TEXTURE_2D);
      GL11.glShadeModel(GL11.GL_SMOOTH);
      GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
      Tessellator tessellator = Tessellator.instance;
      tessellator.startDrawingQuads();
      tessellator.setColorRGBA_F(r0, g0, b0, a0);
      tessellator.addVertex((double)x, (double)(y + height), 0.0D);
      tessellator.setColorRGBA_F(r1, g1, b1, a1);
      tessellator.addVertex((double)(x + width), (double)(y + height), 0.0D);
      tessellator.addVertex((double)(x + width), (double)y, 0.0D);
      tessellator.setColorRGBA_F(r0, g0, b0, a0);
      tessellator.addVertex((double)x, (double)y, 0.0D);
      tessellator.draw();
      GL11.glEnable(GL11.GL_TEXTURE_2D);
      GL11.glShadeModel(GL11.GL_FLAT);
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public static void texturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
      Tessellator tessellator = Tessellator.instance;
      tessellator.startDrawingQuads();
      tessellator.addVertexWithUV((double)x, (double)(y + height), 0.0D, (double)((float)textureX * UV), (double)((float)(textureY + height) * UV));
      tessellator.addVertexWithUV((double)(x + width), (double)(y + height), 0.0D, (double)((float)(textureX + width) * UV), (double)((float)(textureY + height) * UV));
      tessellator.addVertexWithUV((double)(x + width), (double)y, 0.0D, (double)((float)(textureX + width) * UV), (double)((float)textureY * UV));
      tessellator.addVertexWithUV((double)x, (double)y, 0.0D, (double)((float)textureX * UV), (double)((float)textureY * UV));
      tessellator.draw();
   }

   public static void texturedModalRect(int x, int y, int width, int height, int textureX, int textureY, int textureWidth, int textureHeight) {
      Tessellator tessellator = Tessellator.instance;
      tessellator.startDrawingQuads();
      tessellator.addVertexWithUV((double)x, (double)(y + height), 0.0D, (double)((float)textureX * UV), (double)((float)(textureY + textureHeight) * UV));
      tessellator.addVertexWithUV((double)(x + width), (double)(y + height), 0.0D, (double)((float)(textureX + textureWidth) * UV), (double)((float)(textureY + textureHeight) * UV));
      tessellator.addVertexWithUV((double)(x + width), (double)y, 0.0D, (double)((float)(textureX + textureWidth) * UV), (double)((float)textureY * UV));
      tessellator.addVertexWithUV((double)x, (double)y, 0.0D, (double)((float)textureX * UV), (double)((float)textureY * UV));
      tessellator.draw();
   }

   public static void texturedRectangle(int x, int y, int width, int height, float textureX, float textureY, float textureWidth, float textureHeight) {
      Tessellator tessellator = Tessellator.instance;
      tessellator.startDrawingQuads();
      tessellator.addVertexWithUV((double)x, (double)(y + height), 0.0D, (double)textureX, (double)(textureY + textureHeight));
      tessellator.addVertexWithUV((double)(x + width), (double)(y + height), 0.0D, (double)(textureX + textureWidth), (double)(textureY + textureHeight));
      tessellator.addVertexWithUV((double)(x + width), (double)y, 0.0D, (double)(textureX + textureWidth), (double)textureY);
      tessellator.addVertexWithUV((double)x, (double)y, 0.0D, (double)textureX, (double)textureY);
      tessellator.draw();
   }

   public static void borderBox(int x, int y, int width, int height, int border, int textureX, int textureY) {
      texturedModalRect(x - border, y - border, textureX, textureY, border, border);
      texturedModalRect(x, y - border, width, border, textureX + border, textureY, 1, border);
      texturedModalRect(x + width, y - border, textureX + border + 1, textureY, border, border);
      texturedModalRect(x + width, y, border, height, textureX + border + 1, textureY + border, border, 1);
      texturedModalRect(x + width, y + height, textureX + border + 1, textureY + border + 1, border, border);
      texturedModalRect(x, y + height, width, border, textureX + border, textureY + border + 1, 1, border);
      texturedModalRect(x - border, y + height, textureX, textureY + border + 1, border, border);
      texturedModalRect(x - border, y, border, height, textureX, textureY + border, border, 1);
      texturedModalRect(x, y, width, height, textureX + border, textureY + border, 1, 1);
   }
}
