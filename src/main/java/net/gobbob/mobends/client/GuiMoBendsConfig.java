package net.gobbob.mobends.client;

import cpw.mods.fml.client.config.DummyConfigElement.DummyCategoryElement;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.IConfigElement;
import java.util.ArrayList;
import java.util.List;
import net.gobbob.mobends.MoBends;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;

public class GuiMoBendsConfig extends GuiConfig {
   public GuiMoBendsConfig(GuiScreen parent) {
      super(parent, getConfigElements(), MoBends.MODID, MoBends.MODID, false, false, "Mo' Bends Configuration");
   }

   private static List<IConfigElement> getConfigElements() {
      List<IConfigElement> list = new ArrayList();
      list.add(new DummyCategoryElement("animate", "mobends.config.category.animate", new ConfigElement(MoBends.config.getCategory("animate")).getChildElements(), CategoryEntryWithId.class));
      List<IConfigElement> general = new ArrayList();
      List children = new ConfigElement(MoBends.config.getCategory("general")).getChildElements();

      for(int i = 0; i < children.size(); ++i) {
         IConfigElement element = (IConfigElement)children.get(i);
         if (!"Current Pack".equals(element.getName())) {
            general.add(element);
         }
      }

      list.add(new DummyCategoryElement("general", "mobends.config.category.general", general, CategoryEntryWithId.class));
      return list;
   }

   /**
    * Nested category screens need a config ID so Done posts {@code OnConfigChangedEvent}
    * immediately (Forge's default child GuiConfig passes null and skips the event).
    */
   public static class CategoryEntryWithId extends GuiConfigEntries.CategoryEntry {
      public CategoryEntryWithId(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
         super(owningScreen, owningEntryList, configElement);
      }

      protected GuiScreen buildChildScreen() {
         String line2 = (this.owningScreen.titleLine2 == null ? "" : this.owningScreen.titleLine2) + " > " + this.name;
         return new GuiConfig(this.owningScreen, this.configElement.getChildElements(), this.owningScreen.modID, MoBends.MODID, this.owningScreen.allRequireWorldRestart || this.configElement.requiresWorldRestart(), this.owningScreen.allRequireMcRestart || this.configElement.requiresMcRestart(), this.owningScreen.title, line2);
      }
   }
}
