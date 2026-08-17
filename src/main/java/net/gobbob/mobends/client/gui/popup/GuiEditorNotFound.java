package net.gobbob.mobends.client.gui.popup;

import net.minecraft.util.StatCollector;

public class GuiEditorNotFound extends GuiPopUp {
   boolean errorOccurred;

   public GuiEditorNotFound(ButtonAction onBack, ButtonAction onGetEditor) {
      super(StatCollector.translateToLocal("mobends.gui.editornotfound"), 200, 100, new ButtonProps[]{
            new ButtonProps(StatCollector.translateToLocal("mobends.gui.back"), onBack),
            new ButtonProps(StatCollector.translateToLocal("mobends.gui.geteditor"), onGetEditor)
      });
   }

   public void setErrorOccurred(boolean errorOccurred) {
      this.errorOccurred = errorOccurred;
   }

   public void display(int mouseX, int mouseY, float partialTicks) {
      super.display(mouseX, mouseY, partialTicks);
      if (this.errorOccurred) {
         String message = "There seems to be something wrong. Please check your internet connection, or contact the developers.";
         this.fontRenderer.drawStringWithShadow(message, 5, 5, -65536);
      }
   }
}
