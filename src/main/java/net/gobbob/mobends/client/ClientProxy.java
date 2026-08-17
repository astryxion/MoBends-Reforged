package net.gobbob.mobends.client;

import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.gobbob.mobends.AnimatedEntity;
import net.gobbob.mobends.CommonProxy;
import net.gobbob.mobends.MoBends;
import net.gobbob.mobends.client.renderer.entity.RenderBendsArrow;
import net.gobbob.mobends.compat.aether.AetherAccessoriesRender;
import net.gobbob.mobends.compat.hats.HatsRender;
import net.gobbob.mobends.compat.skinlayers3d.SkinLayersRender;
import net.gobbob.mobends.compat.waveycapes.WaveyCapesRender;
import net.gobbob.mobends.event.EventHandler_DataUpdate;
import net.gobbob.mobends.event.EventHandler_Keyboard;
import net.gobbob.mobends.pack.BendsPack;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

public class ClientProxy extends CommonProxy {
   public static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
   public static final ResourceLocation texture_NULL = new ResourceLocation("mobends", "textures/white.png");
   public static final ResourceLocation GOBLIN_CAPE = new ResourceLocation("mobends", "textures/goblinCape.png");

   public void preInit(Configuration config) {
      AnimatedEntity.registerRendering();
      RenderingRegistry.registerEntityRenderingHandler(EntityArrow.class, new RenderBendsArrow());
      ClientRegistry.registerKeyBinding(EventHandler_Keyboard.key_Menu);
      MinecraftForge.EVENT_BUS.register(new EventHandler_DataUpdate());
      FMLCommonHandler.instance().bus().register(new EventHandler_DataUpdate());
      FMLCommonHandler.instance().bus().register(new EventHandler_Keyboard());
      FMLCommonHandler.instance().bus().register(this);
      MoBends.applyConfig();
      BendsPack.preInit(config);
   }

   public void postInit() {
      HatsRender.registerIfPresent();
      SkinLayersRender.registerIfPresent();
      WaveyCapesRender.registerIfPresent();
      AetherAccessoriesRender.registerIfPresent();
   }

   @SubscribeEvent
   public void onConfigChanged(OnConfigChangedEvent event) {
      if (MoBends.MODID.equals(event.modID)) {
         if (MoBends.config != null) {
            MoBends.config.save();
         }

         MoBends.applyConfig();
         ++MoBends.refreshModel;
         MoBends.rememberConfigModified();
      }
   }
}
