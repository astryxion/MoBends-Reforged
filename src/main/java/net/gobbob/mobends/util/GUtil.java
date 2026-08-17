package net.gobbob.mobends.util;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.util.vector.Vector3f;

public class GUtil {
   public static float max(float argNum, float argMax) {
      return argNum > argMax ? argMax : argNum;
   }

   public static Vector3f max(Vector3f argNum, float argMax) {
      if (argNum.x > argMax) {
         argNum.x = argMax;
      }

      if (argNum.y > argMax) {
         argNum.y = argMax;
      }

      if (argNum.z > argMax) {
         argNum.z = argMax;
      }

      return argNum;
   }

   public static float min(float argNum, float argMax) {
      return argNum < argMax ? argMax : argNum;
   }

   public static double wrapRadians(double a) {
      a = a % Math.PI;
      if (a >= Math.PI) {
         a -= Math.PI * 2.0D;
      }

      if (a < -Math.PI) {
         a += Math.PI * 2.0D;
      }

      return a;
   }

   public static double getRadianDifference(double a, double b) {
      a = wrapRadians(a);
      b = wrapRadians(b);
      double d = Math.abs(a - b);
      return d > Math.PI ? Math.PI * 2.0D - d : d;
   }

   public static Vector3f translate(Vector3f num, Vector3f move) {
      num.x += move.x;
      num.y += move.y;
      num.z += move.z;
      return num;
   }

   public static Vector3f scale(Vector3f num, Vector3f move) {
      num.x *= move.x;
      num.y *= move.y;
      num.z *= move.z;
      return num;
   }

   public static Vector3f rotateX(Vector3f num, float rotation) {
      Vector3f y = new Vector3f();
      Vector3f z = new Vector3f();
      y.y = (float)Math.cos((double)((180.0F + rotation) / 180.0F) * Math.PI);
      y.z = (float)Math.sin((double)((180.0F + rotation) / 180.0F) * Math.PI);
      y.normalise();
      y.y *= -num.y;
      y.z *= num.y;
      z.y = (float)Math.sin((double)((180.0F + rotation) / 180.0F) * Math.PI);
      z.z = (float)Math.cos((double)((180.0F + rotation) / 180.0F) * Math.PI);
      z.normalise();
      z.y *= -num.z;
      z.z *= -num.z;
      num = new Vector3f(num.x, y.y + z.y, y.z + z.z);
      return num;
   }

   public static Vector3f rotateY(Vector3f num, float rotation) {
      Vector3f x = new Vector3f();
      Vector3f z = new Vector3f();
      x.x = (float)Math.cos((double)(-rotation / 180.0F) * Math.PI);
      x.z = (float)Math.sin((double)(-rotation / 180.0F) * Math.PI);
      x.normalise();
      x.x *= -num.x;
      x.z *= num.x;
      z.x = (float)Math.sin((double)(-rotation / 180.0F) * Math.PI);
      z.z = (float)Math.cos((double)(-rotation / 180.0F) * Math.PI);
      z.normalise();
      z.x *= num.z;
      z.z *= num.z;
      num = new Vector3f(x.x + z.x, num.y, x.z + z.z);
      return num;
   }

   public static Vector3f rotateZ(Vector3f num, float rotation) {
      Vector3f x = new Vector3f();
      Vector3f y = new Vector3f();
      x.x = (float)Math.sin((double)((rotation - 90.0F) / 180.0F) * Math.PI);
      x.y = (float)Math.cos((double)((rotation - 90.0F) / 180.0F) * Math.PI);
      x.normalise();
      x.x *= -num.x;
      x.y *= num.x;
      y.x = (float)Math.cos((double)((rotation - 90.0F) / 180.0F) * Math.PI);
      y.y = (float)Math.sin((double)((rotation - 90.0F) / 180.0F) * Math.PI);
      y.normalise();
      y.x *= -num.y;
      y.y *= -num.y;
      num = new Vector3f(y.x + x.x, y.y + x.y, num.z);
      return num;
   }

   public static Vector3f[] translate(Vector3f[] nums, Vector3f move) {
      for(int i = 0; i < nums.length; ++i) {
         nums[i] = translate(nums[i], move);
      }

      return nums;
   }

   public static Vector3f[] scale(Vector3f[] nums, Vector3f move) {
      for(int i = 0; i < nums.length; ++i) {
         nums[i] = scale(nums[i], move);
      }

      return nums;
   }

   public static Vector3f[] rotateX(Vector3f[] nums, float move) {
      for(int i = 0; i < nums.length; ++i) {
         nums[i] = rotateX(nums[i], move);
      }

      return nums;
   }

   public static Vector3f[] rotateY(Vector3f[] nums, float move) {
      for(int i = 0; i < nums.length; ++i) {
         nums[i] = rotateY(nums[i], move);
      }

      return nums;
   }

   public static Vector3f[] rotateZ(Vector3f[] nums, float move) {
      for(int i = 0; i < nums.length; ++i) {
         nums[i] = rotateZ(nums[i], move);
      }

      return nums;
   }

   public static float lerp(float a, float b, float slide) {
      return a + (b - a) * slide;
   }

   public static String[] wrapText(FontRenderer fontRenderer, String text, int maxWidth) {
      if (maxWidth <= 0) {
         return new String[0];
      }

      if (text == null || !text.contains(" ")) {
         return new String[]{text};
      }

      List<String> lines = new ArrayList();
      String leftover = text;
      String line = "";
      boolean endOfString = false;

      do {
         String leftoverToNextSpace;
         if (leftover.contains(" ")) {
            leftoverToNextSpace = leftover.substring(0, leftover.indexOf(" "));
         } else {
            leftoverToNextSpace = leftover;
            endOfString = true;
         }

         int currentWidth = fontRenderer.getStringWidth(line + leftoverToNextSpace);
         if (currentWidth > maxWidth) {
            lines.add(line.trim());
            line = leftoverToNextSpace + " ";
         } else {
            line = line + leftoverToNextSpace + " ";
         }

         if (!endOfString) {
            leftover = leftover.substring(leftover.indexOf(" ") + 1);
         } else {
            lines.add(line.trim());
         }
      } while(!endOfString);

      return (String[])lines.toArray(new String[lines.size()]);
   }
}
