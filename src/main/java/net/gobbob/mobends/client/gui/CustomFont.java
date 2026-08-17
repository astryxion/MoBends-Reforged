package net.gobbob.mobends.client.gui;

import java.util.HashMap;
import net.minecraft.util.ResourceLocation;

public class CustomFont {
   public static final CustomFont BOLD = new CustomFont("bold", 256, 32, createBoldMap());
   public final int atlasWidth;
   public final int atlasHeight;
   public final ResourceLocation resourceLocation;
   protected final HashMap<Character, Symbol> symbolMap;

   public CustomFont(String textureName, int atlasWidth, int atlasHeight, HashMap<Character, Symbol> symbolMap) {
      this.atlasWidth = atlasWidth;
      this.atlasHeight = atlasHeight;
      this.resourceLocation = new ResourceLocation("mobends", "textures/gui/fonts/" + textureName + ".png");
      this.symbolMap = symbolMap;
   }

   public Symbol getSymbol(char charAt) {
      return this.symbolMap.containsKey(Character.valueOf(charAt)) ? (Symbol)this.symbolMap.get(Character.valueOf(charAt)) : null;
   }

   private static HashMap<Character, Symbol> createBoldMap() {
      HashMap<Character, Symbol> map = new HashMap();
      map.put(Character.valueOf('a'), new Symbol(0, 0, 9, 11));
      map.put(Character.valueOf('b'), new Symbol(10, 0, 9, 11));
      map.put(Character.valueOf('c'), new Symbol(20, 0, 9, 11));
      map.put(Character.valueOf('d'), new Symbol(30, 0, 8, 11));
      map.put(Character.valueOf('e'), new Symbol(39, 0, 9, 11));
      map.put(Character.valueOf('f'), new Symbol(49, 0, 9, 11));
      map.put(Character.valueOf('g'), new Symbol(59, 0, 9, 11));
      map.put(Character.valueOf('h'), new Symbol(69, 0, 9, 11));
      map.put(Character.valueOf('i'), new Symbol(79, 0, 3, 11));
      map.put(Character.valueOf('j'), new Symbol(83, 0, 8, 11));
      map.put(Character.valueOf('k'), new Symbol(92, 0, 10, 11));
      map.put(Character.valueOf('l'), new Symbol(103, 0, 9, 11));
      map.put(Character.valueOf('m'), new Symbol(113, 0, 10, 11));
      map.put(Character.valueOf('n'), new Symbol(124, 0, 9, 11));
      map.put(Character.valueOf('o'), new Symbol(134, 0, 9, 11));
      map.put(Character.valueOf('p'), new Symbol(144, 0, 9, 11));
      map.put(Character.valueOf('q'), new Symbol(154, 0, 10, 11));
      map.put(Character.valueOf('r'), new Symbol(165, 0, 9, 11));
      map.put(Character.valueOf('s'), new Symbol(175, 0, 9, 11));
      map.put(Character.valueOf('t'), new Symbol(185, 0, 9, 11));
      map.put(Character.valueOf('u'), new Symbol(195, 0, 9, 11));
      map.put(Character.valueOf('v'), new Symbol(205, 0, 10, 11));
      map.put(Character.valueOf('w'), new Symbol(216, 0, 10, 11));
      map.put(Character.valueOf('x'), new Symbol(227, 0, 11, 11));
      map.put(Character.valueOf('y'), new Symbol(239, 0, 11, 11));
      map.put(Character.valueOf('z'), new Symbol(0, 12, 9, 11));
      map.put(Character.valueOf('-'), new Symbol(10, 12, 9, 11));
      return map;
   }

   public static class Symbol {
      public int u;
      public int v;
      public int width;
      public int height;
      public int offsetX;
      public int offsetY;

      public Symbol(int u, int v, int width, int height) {
         this.u = u;
         this.v = v;
         this.width = width;
         this.height = height;
      }
   }
}
