package net.gobbob.mobends.client.gui.elements;

import net.gobbob.mobends.client.gui.CustomFont;
import net.gobbob.mobends.client.gui.CustomFontRenderer;
import net.gobbob.mobends.client.gui.GuiHelper;
import net.gobbob.mobends.event.EventHandler_DataUpdate;
import net.gobbob.mobends.util.Color;
import net.gobbob.mobends.util.Draw;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiSectionButton {
   public static final ResourceLocation BUTTONS_TEXTURE = new ResourceLocation("mobends", "textures/gui/buttons.png");
   public static final float HOVER_ICON_ANIMATION_DURATION = 2.0F;
   public static final float HOVER_BG_ANIMATION_DURATION = 2.0F;
   protected final Minecraft mc;
   private String label;
   private int x;
   private int y;
   private int width;
   private int height;
   private int bgTextureU;
   private int bgTextureV;
   private Color neutralColor;
   private Color bgColor;
   private SectionIcon leftIcon = null;
   private SectionIcon rightIcon = null;
   private CustomFontRenderer fontRenderer;
   private boolean hover;
   private boolean pressed;
   private float ticksAfterHovered = 0.0F;

   public GuiSectionButton(int x, int y, String label, Color bgColor) {
      this.mc = Minecraft.getMinecraft();
      this.label = label;
      this.x = x;
      this.y = y;
      this.width = 318;
      this.height = 43;
      this.bgTextureU = 0;
      this.bgTextureV = 0;
      this.neutralColor = Color.fromHex(0xFF777777);
      this.bgColor = new Color(bgColor);
      this.hover = false;
      this.pressed = false;
      this.fontRenderer = new CustomFontRenderer();
      this.fontRenderer.setFont(CustomFont.BOLD);
   }

   public GuiSectionButton(String label, Color bgColor) {
      this(0, 0, label, bgColor);
   }

   public GuiSectionButton(String label, int bgColor) {
      this(0, 0, label, Color.fromHex(bgColor));
   }

   public GuiSectionButton setLeftIcon(int u, int v, int width, int height) {
      this.leftIcon = new SectionIcon(u, v, width, height);
      return this;
   }

   public GuiSectionButton setRightIcon(int u, int v, int width, int height) {
      this.rightIcon = new SectionIcon(u, v, width, height);
      return this;
   }

   public void initGui(int x, int y) {
      this.x = x;
      this.y = y;
   }

   public void update(int mouseX, int mouseY) {
      boolean nowHover = mouseX >= this.x && mouseX < this.x + this.width && mouseY >= this.y && mouseY < this.y + this.height;
      if (nowHover && !this.hover) {
         this.onHover();
      }

      this.hover = nowHover;
   }

   public boolean mouseClicked(int mouseX, int mouseY, int event) {
      if (this.hover) {
         GuiHelper.playButtonSound();
         this.pressed = true;
      }

      return this.pressed;
   }

   public void mouseReleased(int mouseX, int mouseY, int event) {
      this.pressed = false;
   }

   public void onHover() {
      this.ticksAfterHovered = 0.0F;
   }

   public void display() {
      this.ticksAfterHovered += EventHandler_DataUpdate.ticksPerFrame;
      if (this.hover) {
         GL11.glColor4f(this.bgColor.r, this.bgColor.g, this.bgColor.b, this.bgColor.a);
      } else {
         GL11.glColor4f(this.neutralColor.r, this.neutralColor.g, this.neutralColor.b, this.neutralColor.a);
      }

      this.mc.renderEngine.bindTexture(BUTTONS_TEXTURE);
      int tX = this.bgTextureU;
      int tY = this.bgTextureV;
      float uScale = 0.001953125F;
      float vScale = 0.0078125F;
      if (this.hover) {
         GL11.glColor4f(this.bgColor.r, this.bgColor.g, this.bgColor.b, this.bgColor.a);
      } else {
         GL11.glColor4f(this.neutralColor.r, this.neutralColor.g, this.neutralColor.b, this.neutralColor.a);
      }

      GL11.glDisable(GL11.GL_TEXTURE_2D);
      Draw.rectangle((float)this.x, (float)this.y, (float)this.width, (float)this.height);
      GL11.glEnable(GL11.GL_TEXTURE_2D);
      float bgt = 1.0F;
      if (this.hover) {
         if (this.ticksAfterHovered < HOVER_BG_ANIMATION_DURATION) {
            bgt = 1.0F - this.ticksAfterHovered / HOVER_BG_ANIMATION_DURATION;
            bgt = bgt * bgt * bgt;
         } else {
            bgt = 0.0F;
         }
      }

      int mountainOffsetY = (int)(bgt * 10.0F);
      Draw.texturedRectangle(this.x, this.y + mountainOffsetY, this.width, this.height - 2 - mountainOffsetY, (float)tX * uScale, (float)tY * vScale, (float)(tX + this.width) * uScale, (float)(tY + this.height - 2 - mountainOffsetY) * vScale);
      Draw.texturedRectangle(this.x, this.y + this.height - 2, this.width, 2, (float)tX * uScale, (float)(tY + this.height - 2) * vScale, (float)(tX + this.width) * uScale, (float)(tY + 2) * vScale);
      if (this.hover) {
         float scale = 1.0F;
         if (this.ticksAfterHovered < HOVER_ICON_ANIMATION_DURATION) {
            float pi = (float)Math.PI;
            float t = this.ticksAfterHovered / HOVER_ICON_ANIMATION_DURATION;
            scale = 1.0F - MathHelper.cos(t * pi * 1.5F);
            scale = MathHelper.sqrt_float(scale);
         }

         int iconSpacing = 30;
         if (this.leftIcon != null) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPushMatrix();
            GL11.glTranslatef((float)(this.x + iconSpacing), (float)this.y + (float)this.height / 2.0F, 0.0F);
            GL11.glScalef(scale, scale, 1.0F);
            this.leftIcon.draw(uScale, vScale);
            GL11.glPopMatrix();
         }

         if (this.rightIcon != null) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float)(this.x + this.width - iconSpacing), (float)this.y + (float)this.height / 2.0F, 0.0F);
            GL11.glScalef(scale, scale, 1.0F);
            this.rightIcon.draw(uScale, vScale);
            GL11.glPopMatrix();
         }
      }

      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.fontRenderer.drawCenteredText(this.label, this.x + this.width / 2, this.y + this.height / 2 + 6);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public void setPosition(int i, int j) {
      this.x = i;
      this.y = j;
   }

   private static class SectionIcon {
      private int texU;
      private int texV;
      private int texWidth;
      private int texHeight;

      public SectionIcon(int u, int v, int width, int height) {
         this.texU = u;
         this.texV = v;
         this.texWidth = width;
         this.texHeight = height;
      }

      public void draw(float uScale, float vScale) {
         Draw.texturedRectangle(-this.texWidth / 2, -this.texHeight / 2, this.texWidth, this.texHeight, (float)this.texU * uScale, (float)this.texV * vScale, (float)this.texWidth * uScale, (float)this.texHeight * vScale);
      }
   }
}
