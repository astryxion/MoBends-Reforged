package net.gobbob.mobends;

import cpw.mods.fml.client.registry.RenderingRegistry;
import java.util.ArrayList;
import java.util.List;
import net.gobbob.mobends.animation.Animation;
import net.gobbob.mobends.animation.player.Animation_Attack;
import net.gobbob.mobends.animation.player.Animation_Axe;
import net.gobbob.mobends.animation.player.Animation_Bow;
import net.gobbob.mobends.animation.player.Animation_Falling;
import net.gobbob.mobends.animation.player.Animation_Flying;
import net.gobbob.mobends.animation.player.Animation_Jump;
import net.gobbob.mobends.animation.player.Animation_LadderClimb;
import net.gobbob.mobends.animation.player.Animation_Mining;
import net.gobbob.mobends.animation.player.Animation_Riding;
import net.gobbob.mobends.animation.player.Animation_Sneak;
import net.gobbob.mobends.animation.player.Animation_Sprint;
import net.gobbob.mobends.animation.player.Animation_SprintJump;
import net.gobbob.mobends.animation.player.Animation_Stand;
import net.gobbob.mobends.animation.player.Animation_Swimming;
import net.gobbob.mobends.animation.player.Animation_Walk;
import net.gobbob.mobends.client.renderer.entity.RenderBendsPlayer;
import net.gobbob.mobends.client.renderer.entity.RenderBendsSpider;
import net.gobbob.mobends.client.renderer.entity.RenderBendsZombie;
import net.gobbob.mobends.util.BendsLogger;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class AnimatedEntity {
   public static AnimatedEntity[] animatedEntities;
   public String id;
   public String displayName;
   public Entity entity;
   public Class<? extends Entity> entityClass;
   public Render renderer;
   public List<Animation> animations = new ArrayList();
   public boolean animate = true;

   public AnimatedEntity(String argID, String argDisplayName, Entity argEntity, Class<? extends Entity> argClass, Render argRenderer) {
      this.id = argID;
      this.displayName = argDisplayName;
      this.entityClass = argClass;
      this.renderer = argRenderer;
      this.entity = argEntity;
      this.animate = true;
   }

   public AnimatedEntity add(Animation argGroup) {
      this.animations.add(argGroup);
      return this;
   }

   public static void registerRendering() {
      for(int i = 0; i < animatedEntities.length; ++i) {
         if (animatedEntities[i].animate) {
            RenderingRegistry.registerEntityRenderingHandler(animatedEntities[i].entityClass, animatedEntities[i].renderer);
         }
      }

      BendsLogger.log("Registering Animated Entities...", BendsLogger.INFO);
   }

   public Animation get(String argName) {
      for(int i = 0; i < this.animations.size(); ++i) {
         if (((Animation)this.animations.get(i)).getName().equalsIgnoreCase(argName)) {
            return (Animation)this.animations.get(i);
         }
      }

      return null;
   }

   public static AnimatedEntity getByEntity(Entity argEntity) {
      for(int i = 0; i < animatedEntities.length; ++i) {
         if (animatedEntities[i].entityClass.isInstance(argEntity)) {
            return animatedEntities[i];
         }
      }

      return null;
   }

   static {
      // Avoid Minecraft.getMinecraft() in <clinit>: class loads during FML preInit.
      // Player GUI preview already uses the live client player.
      animatedEntities = new AnimatedEntity[]{
            (new AnimatedEntity("player", "Player", (Entity)null, EntityPlayer.class, new RenderBendsPlayer()))
                  .add(new Animation_Stand()).add(new Animation_Walk()).add(new Animation_Sneak()).add(new Animation_Sprint())
                  .add(new Animation_Jump()).add(new Animation_SprintJump()).add(new Animation_Falling()).add(new Animation_Flying())
                  .add(new Animation_LadderClimb()).add(new Animation_Attack()).add(new Animation_Swimming()).add(new Animation_Bow())
                  .add(new Animation_Riding()).add(new Animation_Mining()).add(new Animation_Axe()),
            (new AnimatedEntity("zombie", "Zombie", new EntityZombie((World)null), EntityZombie.class, new RenderBendsZombie()))
                  .add(new net.gobbob.mobends.animation.zombie.Animation_Stand())
                  .add(new net.gobbob.mobends.animation.zombie.Animation_Walk()),
            new AnimatedEntity("spider", "Spider", new EntitySpider((World)null), EntitySpider.class, new RenderBendsSpider())
      };
   }
}
