package net.gobbob.mobends.client.gui;

public class GuiDragger<T extends IGuiDraggable> {
   private T draggedElement;

   public GuiDragger() {
      this.draggedElement = null;
   }

   public void setDraggedElement(T draggedElement) {
      if (this.draggedElement != null) {
         this.draggedElement.setDragged(false);
      }

      this.draggedElement = draggedElement;
      if (this.draggedElement != null) {
         this.draggedElement.setDragged(true);
      }
   }

   public void stopDragging() {
      if (this.draggedElement != null) {
         this.draggedElement.setDragged(false);
      }

      this.draggedElement = null;
   }

   public T getDraggedElement() {
      return this.draggedElement;
   }

   public void update(int mouseX, int mouseY) {
      if (this.draggedElement != null) {
         this.draggedElement.dragTo(mouseX, mouseY);
      }
   }
}
