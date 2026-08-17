package net.gobbob.mobends.client.gui.elements;

import java.util.LinkedList;

public interface IGuiElementsContainer extends IGuiPositioned {
   LinkedList<IGuiElement> getElements();
}
