package net.gobbob.mobends.client.gui.elements;

import net.gobbob.mobends.util.Draw;
import net.gobbob.mobends.util.GUtil;
import net.gobbob.mobends.util.UIScissorHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public abstract class GuiScrollPanel extends GuiElement {
   protected int width;
   protected int height;
   protected int scrollBarWidth;
   protected int contentSize;
   protected int scrollAmountTarget;
   protected int prevScrollAmount;
   protected int scrollAmount;
   protected boolean hovered;
   protected boolean scrollBarHovered;
   protected boolean scrollHandleHovered;
   protected boolean scrollBarGrabbed;
   protected int scrollBarGrabY;

   public GuiScrollPanel(GuiElement parent, int x, int y, int width, int height) {
      super(parent, x, y);
      this.width = width;
      this.height = height;
      this.scrollBarWidth = 6;
      this.contentSize = 0;
      this.scrollAmountTarget = 0;
      this.scrollAmount = 0;
      this.hovered = false;
      this.scrollBarHovered = false;
      this.scrollHandleHovered = false;
      this.scrollBarGrabbed = false;
      this.scrollBarGrabY = 0;
   }

   public void update(int mouseX, int mouseY) {
      this.hovered = mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height;
      this.scrollBarHovered = false;
      this.scrollHandleHovered = false;
      if (this.scrollBarGrabbed) {
         this.scrollTo((mouseY - this.y - this.scrollBarGrabY) * this.contentSize / this.height);
      }

      if (this.hovered && mouseX >= this.x + this.width - this.scrollBarWidth) {
         this.scrollBarHovered = true;
         int scrollHandleY = this.getScrollHandleY();
         int scrollBarHeight = this.getScrollHandleHeight();
         if (mouseY >= this.y + scrollHandleY && mouseY <= this.y + scrollHandleY + scrollBarHeight) {
            this.scrollHandleHovered = true;
         }
      }

      this.prevScrollAmount = this.scrollAmount;
      this.scrollAmount = (int)((float)this.scrollAmount + (float)(this.scrollAmountTarget - this.scrollAmount) * this.getScrollTweenSpeed());
   }

   public boolean handleMouseClicked(int mouseX, int mouseY, int button) {
      super.handleMouseClicked(mouseX, mouseY, button);
      this.scrollBarGrabbed = false;
      if (this.scrollBarHovered) {
         if (this.scrollHandleHovered) {
            this.scrollBarGrabY = mouseY - this.y - this.getScrollHandleY();
         } else {
            this.scrollBarGrabY = this.getScrollHandleHeight() / 2;
         }

         this.scrollBarGrabbed = true;
         return true;
      } else {
         return false;
      }
   }

   public boolean handleMouseReleased(int mouseX, int mouseY, int button) {
      this.scrollBarGrabbed = false;
      return false;
   }

   public boolean handleMouseInput() {
      int mouseWheelRoll = -Mouse.getEventDWheel();
      if (this.hovered) {
         if (mouseWheelRoll != 0) {
            mouseWheelRoll = mouseWheelRoll > 0 ? 1 : -1;
            this.scroll(mouseWheelRoll * this.getScrollSpeed());
         }

         return true;
      } else {
         return false;
      }
   }

   protected void scrollTo(int value) {
      if (this.contentSize <= this.height) {
         this.scrollAmountTarget = 0;
      } else {
         this.scrollAmountTarget = value;
         if (this.scrollAmountTarget < 0) {
            this.scrollAmountTarget = 0;
         } else if (this.scrollAmountTarget > this.contentSize - this.height) {
            this.scrollAmountTarget = this.contentSize - this.height;
         }
      }
   }

   protected void scroll(int amount) {
      this.scrollTo(this.scrollAmountTarget + amount);
   }

   public void draw(float partialTicks) {
      float scroll = GUtil.lerp((float)this.prevScrollAmount, (float)this.scrollAmount, partialTicks);
      GL11.glPushMatrix();
      GL11.glTranslatef(this.getViewX(), this.getViewY(), 0.0F);
      this.drawBackground(partialTicks);
      GL11.glPushMatrix();
      GL11.glTranslatef(0.0F, -scroll, 0.0F);
      UIScissorHelper.INSTANCE.setUIBounds((int)this.getAbsoluteX(), (int)this.getAbsoluteY(), this.width - this.scrollBarWidth, this.height);
      UIScissorHelper.INSTANCE.enable();
      this.drawChildren(partialTicks);
      this.drawContent(partialTicks);
      UIScissorHelper.INSTANCE.disable();
      GL11.glPopMatrix();
      this.drawForeground(partialTicks);
      GL11.glPopMatrix();
   }

   protected abstract void drawContent(float partialTicks);

   protected void drawForeground(float partialTicks) {
      this.drawScrollBar(partialTicks);
   }

   protected void drawScrollBar(float partialTicks) {
      if (this.contentSize > this.height) {
         int scrollBarHeight = this.getScrollHandleHeight();
         Draw.rectangle((float)(this.width - this.scrollBarWidth), 0.0F, (float)this.scrollBarWidth, (float)this.height, this.getBackgroundColor());
         int barColor = this.scrollBarGrabbed ? this.getScrollBarGrabbedColor() : (this.scrollHandleHovered ? this.getScrollBarHoveredColor() : this.getScrollBarColor());
         Draw.rectangle((float)(this.width - this.scrollBarWidth), (float)this.getScrollHandleY(partialTicks), (float)this.scrollBarWidth, (float)scrollBarHeight, barColor);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      }
   }

   public int getScrollAmount() {
      return this.scrollAmount;
   }

   protected int getScrollHandleY() {
      return this.getScrollHandleY(1.0F);
   }

   protected int getScrollHandleY(float partialTicks) {
      if (this.contentSize <= this.height) {
         return 0;
      } else {
         int scroll = (int)GUtil.lerp((float)this.prevScrollAmount, (float)this.scrollAmount, partialTicks);
         return scroll * (this.height + 2) / this.contentSize;
      }
   }

   protected int getScrollHandleHeight() {
      return this.contentSize <= this.height ? 0 : this.height * this.height / this.contentSize;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public void setX(int x) {
      this.x = x;
   }

   public void setY(int y) {
      this.y = y;
   }

   public void setWidth(int width) {
      this.width = width;
   }

   public void setHeight(int height) {
      this.height = height;
   }

   protected int getScrollSpeed() {
      return 10;
   }

   protected float getScrollTweenSpeed() {
      return 0.5F;
   }

   protected int getBackgroundColor() {
      return -15658735;
   }

   protected int getScrollBarColor() {
      return -7829368;
   }

   protected int getScrollBarHoveredColor() {
      return -6710887;
   }

   protected int getScrollBarGrabbedColor() {
      return -4473925;
   }
}
