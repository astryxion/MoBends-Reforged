package net.gobbob.mobends.client.gui.elements;

public interface IGuiElement extends IGuiPositioned {
   void update(int mouseX, int mouseY);

   boolean handleMouseClicked(int mouseX, int mouseY, int button);

   boolean handleMouseReleased(int mouseX, int mouseY, int button);

   void initGui();

   void draw(float partialTicks);

   IGuiElement getParent();
}
