package net.gobbob.mobends.util;

public class Color {
   public static final Color white = new Color(1.0F, 1.0F, 1.0F, 1.0F);
   public static final Color red = new Color(1.0F, 0.0F, 0.0F, 1.0F);
   public static final Color green = new Color(0.0F, 1.0F, 0.0F, 1.0F);
   public static final Color blue = new Color(0.0F, 0.0F, 1.0F, 1.0F);
   public static final Color black = new Color(0.0F, 0.0F, 0.0F, 1.0F);
   public float r = 0.0F;
   public float g = 0.0F;
   public float b = 0.0F;
   public float a = 0.0F;

   public Color(float r, float g, float b) {
      this.r = r;
      this.g = g;
      this.b = b;
      this.a = 1.0F;
   }

   public Color(float r, float g, float b, float a) {
      this.r = r;
      this.g = g;
      this.b = b;
      this.a = a;
   }
}
