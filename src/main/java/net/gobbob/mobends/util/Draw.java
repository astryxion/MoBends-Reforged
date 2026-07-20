package net.gobbob.mobends.util;

import org.lwjgl.opengl.GL11;

public class Draw {
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
}
