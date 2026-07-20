package net.gobbob.mobends.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.gobbob.mobends.client.renderer.entity.ArrowTrailManager;
import net.gobbob.mobends.client.renderer.entity.RenderBendsArrow;
import net.gobbob.mobends.client.renderer.entity.RenderBendsPlayer;
import net.gobbob.mobends.data.Data_Player;
import net.gobbob.mobends.data.Data_Spider;
import net.gobbob.mobends.data.Data_Zombie;
import net.gobbob.mobends.settings.SettingsBoolean;
import net.gobbob.mobends.settings.SettingsNode;
import net.gobbob.mobends.util.BendsLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import org.lwjgl.util.vector.Vector3f;

public class EventHandler_DataUpdate {
   private static float ticks = 0.0F;
   public static float ticksPerFrame = 0.0F;

   @SubscribeEvent
   public void updateAnimations(TickEvent.RenderTickEvent event) {
      if (Minecraft.getMinecraft().theWorld != null) {
         if (Minecraft.getMinecraft().thePlayer != null && !Minecraft.getMinecraft().isGamePaused()) {
            float newTicks = (float)Minecraft.getMinecraft().thePlayer.ticksExisted + event.renderTickTime;
            if (ticks > newTicks) {
               ticks = newTicks;
            }

            ticksPerFrame = Math.min(Math.max(0.0F, newTicks - ticks), 1.0F);
            ticks = newTicks;
            if (((SettingsBoolean)SettingsNode.getSetting("arrowTrail")).data) {
               ArrowTrailManager.onRenderTick(ticksPerFrame);
            }
         } else {
            ticksPerFrame = 0.0F;
         }

         for(int i = 0; i < Data_Player.dataList.size(); ++i) {
            ((Data_Player)Data_Player.dataList.get(i)).update(event.renderTickTime);
         }

         for(int i = 0; i < Data_Zombie.dataList.size(); ++i) {
            ((Data_Zombie)Data_Zombie.dataList.get(i)).update(event.renderTickTime);
         }

         for(int i = 0; i < Data_Spider.dataList.size(); ++i) {
            ((Data_Spider)Data_Spider.dataList.get(i)).update(event.renderTickTime);
         }

      }
   }

   @SubscribeEvent
   public void onClientTick(TickEvent.ClientTickEvent event) {
      if (Minecraft.getMinecraft().theWorld != null) {
         if (!(RenderManager.instance.entityRenderMap.get(EntityPlayer.class) instanceof RenderBendsPlayer)) {
            Render render = new RenderBendsPlayer();
            RenderManager.instance.entityRenderMap.put(EntityPlayer.class, render);
            render.setRenderManager(RenderManager.instance);
         }

         if (!(RenderManager.instance.entityRenderMap.get(EntityArrow.class) instanceof RenderBendsArrow)) {
            Render arrowRender = new RenderBendsArrow();
            RenderManager.instance.entityRenderMap.put(EntityArrow.class, arrowRender);
            arrowRender.setRenderManager(RenderManager.instance);
         }

         for(int i = 0; i < Data_Player.dataList.size(); ++i) {
            Data_Player data = (Data_Player)Data_Player.dataList.get(i);
            Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(data.entityID);
            if (entity != null) {
               if (!data.entityType.equalsIgnoreCase(entity.getCommandSenderName())) {
                  Data_Player.dataList.remove(data);
                  Data_Player.add(new Data_Player(entity.getEntityId()));
                  BendsLogger.log("Reset entity", BendsLogger.DEBUG);
               } else {
                  data.motion_prev.set(data.motion);
                  data.motion.x = (float)entity.posX - data.position.x;
                  data.motion.y = (float)entity.posY - data.position.y;
                  data.motion.z = (float)entity.posZ - data.position.z;
                  data.position = new Vector3f((float)entity.posX, (float)entity.posY, (float)entity.posZ);
               }
            } else {
               Data_Player.dataList.remove(data);
               BendsLogger.log("No entity", BendsLogger.DEBUG);
            }
         }

         for(int i = 0; i < Data_Zombie.dataList.size(); ++i) {
            Data_Zombie data = (Data_Zombie)Data_Zombie.dataList.get(i);
            Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(data.entityID);
            if (entity != null) {
               if (!data.entityType.equalsIgnoreCase(entity.getCommandSenderName())) {
                  Data_Zombie.dataList.remove(data);
                  Data_Zombie.add(new Data_Zombie(entity.getEntityId()));
                  BendsLogger.log("Reset entity", BendsLogger.DEBUG);
               } else {
                  data.motion_prev.set(data.motion);
                  data.motion.x = (float)entity.posX - data.position.x;
                  data.motion.y = (float)entity.posY - data.position.y;
                  data.motion.z = (float)entity.posZ - data.position.z;
                  data.position = new Vector3f((float)entity.posX, (float)entity.posY, (float)entity.posZ);
               }
            } else {
               Data_Zombie.dataList.remove(data);
               BendsLogger.log("No entity", BendsLogger.DEBUG);
            }
         }

         for(int i = 0; i < Data_Spider.dataList.size(); ++i) {
            Data_Spider data = (Data_Spider)Data_Spider.dataList.get(i);
            Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(data.entityID);
            if (entity != null) {
               if (!data.entityType.equalsIgnoreCase(entity.getCommandSenderName())) {
                  Data_Spider.dataList.remove(data);
                  Data_Spider.add(new Data_Spider(entity.getEntityId()));
                  BendsLogger.log("Reset entity", BendsLogger.DEBUG);
               } else {
                  data.motion_prev.set(data.motion);
                  data.motion.x = (float)entity.posX - data.position.x;
                  data.motion.y = (float)entity.posY - data.position.y;
                  data.motion.z = (float)entity.posZ - data.position.z;
                  data.position = new Vector3f((float)entity.posX, (float)entity.posY, (float)entity.posZ);
               }
            } else {
               Data_Spider.dataList.remove(data);
               BendsLogger.log("No entity", BendsLogger.DEBUG);
            }
         }

      }
   }
}
