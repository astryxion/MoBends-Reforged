package net.gobbob.mobends;

import cpw.mods.fml.client.registry.RenderingRegistry;
import java.util.ArrayList;
import java.util.List;
import net.gobbob.mobends.animation.Animation;
import net.gobbob.mobends.animation.player.Animation_Attack;
import net.gobbob.mobends.animation.player.Animation_Axe;
import net.gobbob.mobends.animation.player.Animation_Bow;
import net.gobbob.mobends.animation.player.Animation_Eating;
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
import net.gobbob.mobends.animation.player.Animation_Tool;
import net.gobbob.mobends.animation.player.Animation_TorchHolding;
import net.gobbob.mobends.animation.player.Animation_Walk;
import net.gobbob.mobends.animation.spider.Animation_Crawl;
import net.gobbob.mobends.animation.spider.Animation_Death;
import net.gobbob.mobends.animation.spider.Animation_Idle;
import net.gobbob.mobends.animation.spider.Animation_Move;
import net.gobbob.mobends.client.renderer.entity.RenderBendsCaveSpider;
import net.gobbob.mobends.client.renderer.entity.RenderBendsPlayer;
import net.gobbob.mobends.client.renderer.entity.RenderBendsSkeleton;
import net.gobbob.mobends.client.renderer.entity.RenderBendsSpider;
import net.gobbob.mobends.client.renderer.entity.RenderBendsSquid;
import net.gobbob.mobends.client.renderer.entity.RenderBendsZombie;
import net.gobbob.mobends.util.BendsLogger;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
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
   /** False for GUI-only aliases that share another entry's entity class (wither skeleton). */
   public boolean registerRenderer = true;

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

   public AnimatedEntity skipRenderer() {
      this.registerRenderer = false;
      return this;
   }

   public boolean isMob() {
      return this.entityClass != EntityPlayer.class;
   }

   public static AnimatedEntity getById(String id) {
      for(int i = 0; i < animatedEntities.length; ++i) {
         if (id.equals(animatedEntities[i].id)) {
            return animatedEntities[i];
         }
      }

      return null;
   }

   private static AnimatedEntity spiderAnims(AnimatedEntity entity) {
      return entity.add(new Animation_Idle()).add(new Animation_Move()).add(new net.gobbob.mobends.animation.spider.Animation_Jump()).add(new Animation_Crawl()).add(new Animation_Death());
   }

   private static AnimatedEntity skeletonAnims(AnimatedEntity entity) {
      return entity.add(new net.gobbob.mobends.animation.skeleton.Animation_Stand())
            .add(new net.gobbob.mobends.animation.skeleton.Animation_Walk())
            .add(new net.gobbob.mobends.animation.skeleton.Animation_Jump())
            .add(new net.gobbob.mobends.animation.skeleton.Animation_Bow())
            .add(new net.gobbob.mobends.animation.skeleton.Animation_Tool());
   }

   private static EntitySkeleton witherPreview() {
      EntitySkeleton wither = new EntitySkeleton((World)null);
      wither.setSkeletonType(1);
      return wither;
   }

   private static EntityZombie villagerPreview() {
      EntityZombie villager = new EntityZombie((World)null);
      villager.setVillager(true);
      return villager;
   }

   public void preparePreview(World world) {
      if (this.entity == null) {
         return;
      }

      this.entity.worldObj = world;
      if (world != null && this.entity instanceof EntitySkeleton) {
         EntitySkeleton skeleton = (EntitySkeleton)this.entity;
         if (skeleton.getSkeletonType() == 1 && skeleton.getHeldItem() == null) {
            skeleton.setCurrentItemOrArmor(0, new ItemStack(Items.stone_sword));
         }
      }

      if (world != null && this.entity instanceof EntityZombie && "zombievillager".equals(this.id)) {
         ((EntityZombie)this.entity).setVillager(true);
      }
   }

   public static void registerRendering() {
      for(int i = 0; i < animatedEntities.length; ++i) {
         if (animatedEntities[i].registerRenderer) {
            RenderingRegistry.registerEntityRenderingHandler(animatedEntities[i].entityClass, animatedEntities[i].renderer);
         }
      }

      BendsLogger.log("Registering Animated Entities...", BendsLogger.INFO);
   }

   public static boolean shouldAnimate(Entity argEntity) {
      AnimatedEntity animated = getByEntity(argEntity);
      return animated != null && animated.animate;
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
      if (argEntity instanceof EntitySkeleton && ((EntitySkeleton)argEntity).getSkeletonType() == 1) {
         AnimatedEntity wither = getById("witherskeleton");
         if (wither != null) {
            return wither;
         }
      }

      if (argEntity instanceof EntityZombie && !(argEntity instanceof EntityPigZombie) && ((EntityZombie)argEntity).isVillager()) {
         AnimatedEntity villager = getById("zombievillager");
         if (villager != null) {
            return villager;
         }
      }

      for(int i = 0; i < animatedEntities.length; ++i) {
         if (animatedEntities[i].entityClass == argEntity.getClass()) {
            return animatedEntities[i];
         }
      }

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
                  .add(new Animation_Riding()).add(new Animation_Tool()).add(new Animation_Mining()).add(new Animation_Axe())
                  .add(new Animation_TorchHolding()).add(new Animation_Eating()),
            (new AnimatedEntity("zombie", "Zombie", new EntityZombie((World)null), EntityZombie.class, new RenderBendsZombie()))
                  .add(new net.gobbob.mobends.animation.zombie.Animation_Stand())
                  .add(new net.gobbob.mobends.animation.zombie.Animation_Walk()),
            (new AnimatedEntity("zombievillager", "Zombie Villager", villagerPreview(), EntityZombie.class, new RenderBendsZombie()).skipRenderer())
                  .add(new net.gobbob.mobends.animation.zombie.Animation_Stand())
                  .add(new net.gobbob.mobends.animation.zombie.Animation_Walk()),
            skeletonAnims(new AnimatedEntity("skeleton", "Skeleton", new EntitySkeleton((World)null), EntitySkeleton.class, new RenderBendsSkeleton())),
            skeletonAnims(new AnimatedEntity("witherskeleton", "Wither Skeleton", witherPreview(), EntitySkeleton.class, new RenderBendsSkeleton()).skipRenderer()),
            (new AnimatedEntity("pigzombie", "Zombie Pigman", new EntityPigZombie((World)null), EntityPigZombie.class, new RenderBendsZombie()))
                  .add(new net.gobbob.mobends.animation.pigzombie.Animation_Stand())
                  .add(new net.gobbob.mobends.animation.pigzombie.Animation_Walk()),
            spiderAnims(new AnimatedEntity("spider", "Spider", new EntitySpider((World)null), EntitySpider.class, new RenderBendsSpider())),
            spiderAnims(new AnimatedEntity("cavespider", "Cave Spider", new EntityCaveSpider((World)null), EntityCaveSpider.class, new RenderBendsCaveSpider())),
            (new AnimatedEntity("squid", "Squid", new EntitySquid((World)null), EntitySquid.class, new RenderBendsSquid()))
                  .add(new net.gobbob.mobends.animation.squid.Animation_Squid())
      };
   }
}
