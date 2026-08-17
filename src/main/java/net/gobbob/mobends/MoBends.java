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
   version = MoBends.VERSION,
   guiFactory = "net.gobbob.mobends.client.MoBendsGuiFactory",
   dependencies = "after:Hats;after:fiskheroes;after:legends;after:skinlayers3d;after:aether;after:aether_legacy;after:lotr;after:thebetweenlands;after:TwilightForest"
)
public class MoBends {
   public static final String MODID = "mobends";
   public static final String MODNAME = "Mo' Bends";
   public static final String VERSION = "1.2.0";
   @SidedProxy(
      serverSide = "net.gobbob.mobends.CommonProxy",
      clientSide = "net.gobbob.mobends.client.ClientProxy"
   )
   public static CommonProxy proxy;
   @Instance("mobends")
   public static MoBends instance;
   public static File configFile;
   public static Configuration config;
   public static int refreshModel = 0;
   private static long lastConfigModified = -1L;
   private static boolean savingConfig;

   @EventHandler
   public void preInit(FMLPreInitializationEvent event) {
      configFile = event.getSuggestedConfigurationFile();
      config = new Configuration(configFile);
      config.load();
      proxy.preInit(config);
      config.save();
      rememberConfigModified();
   }

   public static void applyConfig() {
      if (config == null || AnimatedEntity.animatedEntities == null) {
         return;
      }

      for(int i = 0; i < AnimatedEntity.animatedEntities.length; ++i) {
         net.minecraftforge.common.config.Property prop = config.get("animate", AnimatedEntity.animatedEntities[i].id, true, "Enable Mo' Bends animation for " + AnimatedEntity.animatedEntities[i].displayName);
         prop.setLanguageKey("mobends.config.animate." + AnimatedEntity.animatedEntities[i].id);
         AnimatedEntity.animatedEntities[i].animate = prop.getBoolean();
      }

      applyGeneralBoolean("swordTrail", "Sword Trail");
      applyGeneralBoolean("arrowTrail", "Arrow Trail");
      applyGeneralBoolean("sprintFlyBoost", "Sprint Fly Boost");
      applyGeneralBoolean("sprintSwimBoost", "Sprint Swim Boost");
      applyGeneralBoolean("spinAttack", "Spin Attack");
      config.setCategoryLanguageKey("animate", "mobends.config.category.animate");
      config.setCategoryLanguageKey("general", "mobends.config.category.general");
      if (config.hasCategory("Animate") && config.getCategory("Animate") != config.getCategory("animate")) {
         config.removeCategory(config.getCategory("Animate"));
      }

      if (config.hasCategory("General") && config.getCategory("General") != config.getCategory("general")) {
         config.removeCategory(config.getCategory("General"));
      }
   }

   private static void applyGeneralBoolean(String settingId, String configName) {
      net.minecraftforge.common.config.Property prop = config.get("general", configName, true);
      prop.setLanguageKey("mobends.config.general." + settingId);
      ((SettingsBoolean)SettingsNode.getSetting(settingId)).data = prop.getBoolean();
   }

   public static void saveConfig() {
      if (config == null) {
         config = new Configuration(configFile);
         config.load();
      }

      for(int i = 0; i < AnimatedEntity.animatedEntities.length; ++i) {
         config.get("animate", AnimatedEntity.animatedEntities[i].id, true).set(AnimatedEntity.animatedEntities[i].animate);
      }

      config.get("general", "Sword Trail", true).set(((SettingsBoolean)SettingsNode.getSetting("swordTrail")).data);
      config.get("general", "Arrow Trail", true).set(((SettingsBoolean)SettingsNode.getSetting("arrowTrail")).data);
      config.get("general", "Sprint Fly Boost", true).set(((SettingsBoolean)SettingsNode.getSetting("sprintFlyBoost")).data);
      config.get("general", "Sprint Swim Boost", true).set(((SettingsBoolean)SettingsNode.getSetting("sprintSwimBoost")).data);
      config.get("general", "Spin Attack", true).set(((SettingsBoolean)SettingsNode.getSetting("spinAttack")).data);
      config.get("general", "Current Pack", 0).set(BendsPack.currentPack);
      savingConfig = true;
      try {
         config.save();
         rememberConfigModified();
      } finally {
         savingConfig = false;
      }
   }

   public static void reloadConfigIfFileChanged() {
      if (savingConfig || configFile == null || !configFile.exists()) {
         return;
      }

      long modified = configFile.lastModified();
      if (lastConfigModified < 0L) {
         lastConfigModified = modified;
         return;
      }

      if (modified == lastConfigModified) {
         return;
      }

      lastConfigModified = modified;
      if (config == null) {
         config = new Configuration(configFile);
      }

      config.load();
      applyConfig();
      ++refreshModel;
   }

   public static void rememberConfigModified() {
      if (configFile != null && configFile.exists()) {
         lastConfigModified = configFile.lastModified();
      }
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
