package net.gobbob.mobends;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import java.io.File;
import net.gobbob.mobends.event.EventHandler_SprintFly;
import net.gobbob.mobends.event.EventHandler_SprintSwim;
import net.gobbob.mobends.pack.BendsPack;
import net.gobbob.mobends.settings.SettingsBoolean;
import net.gobbob.mobends.settings.SettingsNode;
import net.minecraftforge.common.config.Configuration;

@Mod(
   modid = "mobends",
   version = "1.0.0",
   dependencies = "after:Hats;after:fiskheroes;after:legends;after:skinlayers3d"
)
public class MoBends {
   public static final String MODID = "mobends";
   public static final String MODNAME = "Mo' Bends";
   public static final String VERSION = "0.20.1";
   @SidedProxy(
      serverSide = "net.gobbob.mobends.CommonProxy",
      clientSide = "net.gobbob.mobends.client.ClientProxy"
   )
   public static CommonProxy proxy;
   @Instance("mobends")
   public static MoBends instance;
   public static File configFile;
   public static int refreshModel = 0;

   @EventHandler
   public void preInit(FMLPreInitializationEvent event) {
      configFile = event.getSuggestedConfigurationFile();
      Configuration config = new Configuration(event.getSuggestedConfigurationFile());
      config.load();
      proxy.preInit(config);
      config.save();
   }

   public static void saveConfig() {
      Configuration config = new Configuration(configFile);
      config.load();

      for(int i = 0; i < AnimatedEntity.animatedEntities.length; ++i) {
         config.get("Animate", AnimatedEntity.animatedEntities[i].id, false).setValue(AnimatedEntity.animatedEntities[i].animate);
      }

      config.get("General", "Sword Trail", true).setValue(((SettingsBoolean)SettingsNode.getSetting("swordTrail")).data);
      config.get("General", "Arrow Trail", true).setValue(((SettingsBoolean)SettingsNode.getSetting("arrowTrail")).data);
      config.get("General", "Sprint Fly Boost", true).setValue(((SettingsBoolean)SettingsNode.getSetting("sprintFlyBoost")).data);
      config.get("General", "Sprint Swim Boost", true).setValue(((SettingsBoolean)SettingsNode.getSetting("sprintSwimBoost")).data);
      config.get("General", "Current Pack", true).setValue(BendsPack.currentPack);
      config.save();
   }

   @EventHandler
   public void init(FMLInitializationEvent event) {
      FMLCommonHandler.instance().bus().register(new EventHandler_SprintFly());
      FMLCommonHandler.instance().bus().register(new EventHandler_SprintSwim());
   }

   @EventHandler
   public void postInit(FMLPostInitializationEvent event) {
      proxy.postInit();
   }
}
