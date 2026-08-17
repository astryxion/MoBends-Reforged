package net.gobbob.mobends.client.gui;

public interface IGuiDraggable {
   void dragTo(int x, int y);

   void setDragged(boolean dragged);

   boolean isDragged();
}
