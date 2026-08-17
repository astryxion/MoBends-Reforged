package net.gobbob.mobends.client.gui;

import java.util.ArrayList;
import java.util.List;

public class AnimationEditorRegistry {
   public static AnimationEditorRegistry INSTANCE = new AnimationEditorRegistry();
   private List<IAnimationEditor> registeredEditors = new ArrayList<IAnimationEditor>();

   public void registerEditor(IAnimationEditor editor) {
      this.registeredEditors.add(editor);
   }

   public IAnimationEditor getPrimaryEditor() {
      return this.registeredEditors.size() > 0 ? (IAnimationEditor)this.registeredEditors.get(0) : null;
   }

   public interface IAnimationEditor {
      void openEditorGui();
   }
}
