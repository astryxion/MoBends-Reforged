package net.gobbob.mobends.client.gui.elements;

import java.util.Iterator;
import java.util.LinkedList;
import org.lwjgl.opengl.GL11;

public abstract class GuiElement implements IGuiElement, IGuiElementsContainer {
   protected int x;
   protected int y;
   protected IGuiElement parent;
   protected LinkedList<IGuiElement> children;

   public GuiElement(IGuiElement parent, int x, int y) {
      this.parent = parent;
      this.x = x;
      this.y = y;
      this.children = new LinkedList<IGuiElement>();
   }

   public void initGui() {
      this.children.clear();
   }

   public IGuiElement getParent() {
      return this.parent;
   }

   public LinkedList<IGuiElement> getElements() {
      return this.children;
   }

   public void addElement(IGuiElement element) {
      this.getElements().add(element);
      element.initGui();
   }

   public void update(int mouseX, int mouseY) {
      this.updateChildren(mouseX, mouseY);
   }

   public boolean handleMouseClicked(int mouseX, int mouseY, int button) {
      return this.handleMouseClickedChildren(mouseX, mouseY, button);
   }

   public boolean handleMouseReleased(int mouseX, int mouseY, int button) {
      return this.handleMouseReleasedChildren(mouseX, mouseY, button);
   }

   public void draw(float partialTicks) {
      GL11.glPushMatrix();
      GL11.glTranslatef(this.getViewX(), this.getViewY(), 0.0F);
      this.drawBackground(partialTicks);
      this.drawChildren(partialTicks);
      this.drawForeground(partialTicks);
      GL11.glPopMatrix();
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public float getViewX() {
      return (float)this.getX();
   }

   public float getViewY() {
      return (float)this.getY();
   }

   public float getAbsoluteX() {
      IGuiElement parentElement = this.getParent();
      if (parentElement instanceof GuiElement) {
         return ((GuiElement)parentElement).getAbsoluteX() + this.getViewX();
      } else {
         return (float)this.getX();
      }
   }

   public float getAbsoluteY() {
      IGuiElement parentElement = this.getParent();
      if (parentElement instanceof GuiElement) {
         return ((GuiElement)parentElement).getAbsoluteY() + this.getViewY();
      } else {
         return (float)this.getY();
      }
   }

   protected void updateChildren(int mouseX, int mouseY) {
      for(IGuiElement element : this.getElements()) {
         element.update(mouseX - this.getX(), mouseY - this.getY());
      }
   }

   protected void drawChildren(float partialTicks) {
      for(IGuiElement element : this.getElements()) {
         element.draw(partialTicks);
      }
   }

   protected boolean handleMouseClickedChildren(int mouseX, int mouseY, int button) {
      Iterator<IGuiElement> it = this.getElements().descendingIterator();
      while(it.hasNext()) {
         if (it.next().handleMouseClicked(mouseX, mouseY, button)) {
            return true;
         }
      }

      return false;
   }

   protected boolean handleMouseReleasedChildren(int mouseX, int mouseY, int button) {
      Iterator<IGuiElement> it = this.getElements().descendingIterator();
      while(it.hasNext()) {
         if (it.next().handleMouseReleased(mouseX, mouseY, button)) {
            return true;
         }
      }

      return false;
   }

   protected abstract void drawBackground(float partialTicks);

   protected abstract void drawForeground(float partialTicks);
}
