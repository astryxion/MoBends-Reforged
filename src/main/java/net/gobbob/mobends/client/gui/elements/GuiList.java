package net.gobbob.mobends.client.gui.elements;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class GuiList<T extends IGuiListElement> extends GuiScrollPanel {
   protected int spacing;
   protected int paddingLeft;
   protected int paddingTop;
   protected int paddingBottom;

   public GuiList(int x, int y, int width, int height, int spacing, int paddingLeft, int paddingTop, int paddingBottom) {
      super((GuiElement)null, x, y, width, height);
      this.spacing = spacing;
      this.paddingLeft = paddingLeft;
      this.paddingTop = paddingTop;
      this.paddingBottom = paddingBottom;
   }

   public GuiList(int x, int y, int width, int height) {
      this(x, y, width, height, 10, 0, 0, 0);
   }

   public abstract LinkedList<T> getListElements();

   public void initGui(int x, int y) {
      this.x = x;
      this.y = y;
      int yOffset = this.paddingTop;

      for(T entry : this.getListElements()) {
         entry.initGui(this.paddingLeft, yOffset);
         yOffset += entry.getHeight() + this.spacing;
      }

      yOffset -= this.spacing;
      this.contentSize = yOffset + this.paddingBottom;
      if (this.contentSize <= this.height) {
         this.scrollAmountTarget = 0;
      } else if (this.scrollAmountTarget + this.height > this.contentSize) {
         this.scrollAmountTarget = this.contentSize - this.height;
         this.scrollAmount = this.contentSize - this.height;
      }
   }

   public void addElement(T element) {
      List<T> elements = this.getListElements();
      elements.add(element);
      element.setOrder(elements.size() - 1);
      this.initGui(this.x, this.y);
   }

   public void insertOrMoveElement(T element, int newIndex) {
      List<T> elements = this.getListElements();
      if (elements.contains(element)) {
         elements.remove(element);
      }

      try {
         elements.add(newIndex, element);
         element.setOrder(newIndex);
      } catch (IndexOutOfBoundsException var5) {
         elements.add(element);
      }

      this.initGui(this.x, this.y);
   }

   public void removeElement(T element) {
      this.getListElements().remove(element);
      this.initGui(this.x, this.y);
   }

   public void clearElements() {
      this.getListElements().clear();
      this.initGui(this.x, this.y);
   }

   protected boolean handleMouseClickedElements(int mouseX, int mouseY, int button) {
      Iterator<T> it = this.getListElements().descendingIterator();

      while(it.hasNext()) {
         if (((IGuiListElement)it.next()).handleMouseClicked(mouseX, mouseY, button)) {
            return true;
         }
      }

      return false;
   }

   public void update(int mouseX, int mouseY) {
      super.update(mouseX, mouseY);

      for(T element : this.getListElements()) {
         element.update(mouseX - this.x, mouseY - this.y + this.scrollAmount);
      }
   }

   protected void drawContent(float partialTicks) {
      for(T element : this.getListElements()) {
         element.draw(partialTicks);
      }
   }

   public boolean handleMouseClicked(int mouseX, int mouseY, int button) {
      return this.hovered && this.handleMouseClickedElements(mouseX - this.x, mouseY - this.y + this.scrollAmount, button) ? true : super.handleMouseClicked(mouseX, mouseY, button);
   }

   public int getSpacing() {
      return this.spacing;
   }

   public int getPaddingTop() {
      return this.paddingTop;
   }

   public int getPaddingLeft() {
      return this.paddingLeft;
   }

   public int getPaddingBottom() {
      return this.paddingBottom;
   }
}
