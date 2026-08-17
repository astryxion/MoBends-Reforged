package net.gobbob.mobends.compat.aether;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.gobbob.mobends.client.model.ModelArmorGlue;
import net.gobbob.mobends.client.model.ModelCustomArmor;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.entity.ModelBendsPlayer;
import net.gobbob.mobends.client.renderer.entity.RenderBendsPlayer;
import net.gobbob.mobends.util.BendsLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;

/**
 * Hats-style glue for Aether Legacy and Aether II accessories: skip their world-space
 * draw, {@code postRender} the Mo' Bends bone, then paint the original mesh in that space.
 */
@SideOnly(Side.CLIENT)
public final class AetherAccessoriesRender {

   private static final float SCALE = 0.0625F;
   private static final float NECK_FROM_HIPS = -12.0F;

   private AetherAccessoriesRender() {
   }

   public static void registerIfPresent() {
      Legacy.registerIfPresent();
      AetherII.registerIfPresent();
   }

   public static void renderPlayerAccessories(EntityPlayer player, ModelBiped model, float partialTicks) {
      if (player == null || !(model instanceof ModelBendsPlayer)) {
         return;
      }
      ModelBendsPlayer bends = (ModelBendsPlayer) model;
      Legacy.render(player, bends, partialTicks);
      AetherII.render(player, bends, partialTicks);
      GL11.glColor3f(1.0F, 1.0F, 1.0F);
   }

   static void glueGloves(ModelBendsPlayer bends, ModelCustomArmor gloves) {
      glueUpperArm(bends, bends.bipedRightArm, gloves.bipedRightArm);
      glueForeArm(bends, bends.bipedRightArm, bends.bipedRightForeArm, gloves.bipedRightForeArm);
      glueUpperArm(bends, bends.bipedLeftArm, gloves.bipedLeftArm);
      glueForeArm(bends, bends.bipedLeftArm, bends.bipedLeftForeArm, gloves.bipedLeftForeArm);
   }

   static void glueUpperArm(ModelBendsPlayer bends, ModelRenderer armBone, ModelRenderer gloveArm) {
      GL11.glPushMatrix();
      bends.bipedBody.postRender(SCALE);
      postRenderSelf(armBone);
      ModelArmorGlue.renderVanillaPartWithoutChildren(gloveArm, SCALE);
      GL11.glPopMatrix();
   }

   static void glueForeArm(ModelBendsPlayer bends, ModelRenderer upperArm, ModelRenderer foreArm, ModelRenderer gloveForeArm) {
      GL11.glPushMatrix();
      bends.bipedBody.postRender(SCALE);
      postRenderSelf(upperArm);
      postRenderSelf(foreArm);
      ModelArmorGlue.renderVanillaPartWithoutChildren(gloveForeArm, SCALE);
      GL11.glPopMatrix();
   }

   static void postRenderSelf(ModelRenderer bone) {
      if (bone instanceof ModelRendererBends) {
         ((ModelRendererBends) bone).postRenderSelf(SCALE);
      } else if (bone != null) {
         bone.postRender(SCALE);
      }
   }

   static void glueHat(ModelBendsPlayer bends) {
      bends.bipedBody.postRender(SCALE);
      bends.bipedHead.postRender(SCALE);
   }

   static void glueBody(ModelBendsPlayer bends, ModelRenderer bodyPart) {
      GL11.glPushMatrix();
      bends.bipedBody.postRender(SCALE);
      GL11.glTranslatef(0.0F, NECK_FROM_HIPS * SCALE, 0.0F);
      ModelArmorGlue.renderVanillaPart(bodyPart, SCALE);
      GL11.glPopMatrix();
   }

   static void glueCape(EntityPlayer player, ModelBendsPlayer bends, ModelRenderer cloak, float partialTicks) {
      GL11.glPushMatrix();
      bends.bipedBody.postRender(SCALE);
      // Body back face is +2px; sit on it. Do not add Aether's extra -1px Z after the 180° flip.
      GL11.glTranslatef(0.0F, NECK_FROM_HIPS * SCALE, 2.0F * SCALE);

      double d3 = player.field_71091_bM + (player.field_71094_bP - player.field_71091_bM) * (double) partialTicks
            - (player.prevPosX + (player.posX - player.prevPosX) * (double) partialTicks);
      double d4 = player.field_71096_bN + (player.field_71095_bQ - player.field_71096_bN) * (double) partialTicks
            - (player.prevPosY + (player.posY - player.prevPosY) * (double) partialTicks);
      double d0 = player.field_71097_bO + (player.field_71085_bR - player.field_71097_bO) * (double) partialTicks
            - (player.prevPosZ + (player.posZ - player.prevPosZ) * (double) partialTicks);
      float yaw = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks;
      double d1 = (double) MathHelper.sin(yaw * (float) Math.PI / 180.0F);
      double d2 = (double) (-MathHelper.cos(yaw * (float) Math.PI / 180.0F));
      float f5 = (float) d4 * 10.0F;
      if (f5 < -6.0F) {
         f5 = -6.0F;
      }
      if (f5 > 32.0F) {
         f5 = 32.0F;
      }
      float f6 = (float) (d3 * d1 + d0 * d2) * 100.0F;
      float f7 = (float) (d3 * d2 - d0 * d1) * 100.0F;
      if (f6 < 0.0F) {
         f6 = 0.0F;
      }
      float f8 = player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * partialTicks;
      f5 += MathHelper.sin((player.prevDistanceWalkedModified
            + (player.distanceWalkedModified - player.prevDistanceWalkedModified) * partialTicks) * 6.0F) * 32.0F * f8;

      GL11.glRotatef(6.0F + f6 / 2.0F + f5, 1.0F, 0.0F, 0.0F);
      GL11.glRotatef(f7 / 2.0F, 0.0F, 0.0F, 1.0F);
      GL11.glRotatef(-f7 / 2.0F, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
      GL11.glTranslatef(0.0F, 0.015625F, 0.0F);
      GL11.glScalef(0.8F, 0.9375F, 0.234375F);
      ModelArmorGlue.renderVanillaPart(cloak, SCALE);
      GL11.glPopMatrix();
   }

   static void applyColor(EntityPlayer player, int colour) {
      float red = (float) (colour >> 16 & 255) / 255.0F;
      float green = (float) (colour >> 8 & 255) / 255.0F;
      float blue = (float) (colour & 255) / 255.0F;
      if (player.hurtTime > 0) {
         GL11.glColor3f(1.0F, 0.5F, 0.5F);
      } else {
         GL11.glColor3f(red, green, blue);
      }
   }

   static void bind(ResourceLocation texture) {
      if (texture != null) {
         Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
      }
   }

   static ResourceLocation textureOf(Field textureField, Item item) {
      try {
         Object texture = textureField.get(item);
         return texture instanceof ResourceLocation ? (ResourceLocation) texture : null;
      } catch (Throwable t) {
         return null;
      }
   }

   static Object enumConstant(Class<?> type, String name) {
      Object[] constants = type.getEnumConstants();
      for (int i = 0; constants != null && i < constants.length; ++i) {
         if (name.equals(((Enum<?>) constants[i]).name())) {
            return constants[i];
         }
      }
      return null;
   }

   static void hidePart(ModelRenderer part, boolean visible) {
      if (part != null) {
         part.showModel = visible;
         part.isHidden = !visible;
      }
   }

   @SideOnly(Side.CLIENT)
   static final class Legacy {
      private static boolean resolved;
      private static boolean active;
      private static Method playerAetherGet;
      private static Method getAccessoryInventory;
      private static Method getStackInSlot;
      private static Method wearingAccessory;
      private static Object glovesType;
      private static Object pendantType;
      private static Object capeType;
      private static Method instanceMethod;
      private static Field modelMiscField;
      private static Field textureField;
      private static Field shouldRenderCape;
      private static Object phoenixGloves;
      private static Object invisibilityCape;
      private static ModelBiped overlay;
      private static ModelCustomArmor glovesModel;

      static void registerIfPresent() {
         if (resolved) {
            return;
         }
         resolved = true;
         if (!Loader.isModLoaded("aether_legacy")) {
            return;
         }
         try {
            Class<?> playerAether = Class.forName("com.gildedgames.the_aether.player.PlayerAether");
            playerAetherGet = playerAether.getMethod("get", EntityPlayer.class);
            getAccessoryInventory = playerAether.getMethod("getAccessoryInventory");
            shouldRenderCape = playerAether.getField("shouldRenderCape");
            Class<?> accessoryType = Class.forName("com.gildedgames.the_aether.api.accessories.AccessoryType");
            glovesType = enumConstant(accessoryType, "GLOVES");
            pendantType = enumConstant(accessoryType, "PENDANT");
            capeType = enumConstant(accessoryType, "CAPE");
            Class<?> inventory = Class.forName("com.gildedgames.the_aether.api.player.util.IAccessoryInventory");
            getStackInSlot = inventory.getMethod("getStackInSlot", accessoryType);
            try {
               wearingAccessory = inventory.getMethod("wearingAccessory", ItemStack.class);
            } catch (NoSuchMethodException ignored) {
               wearingAccessory = null;
            }
            Class<?> renderer = Class.forName("com.gildedgames.the_aether.client.renders.entity.PlayerAetherRenderer");
            instanceMethod = renderer.getMethod("instance");
            modelMiscField = renderer.getField("modelMisc");
            textureField = Class.forName("com.gildedgames.the_aether.items.accessories.ItemAccessory").getField("texture");
            Class<?> items = Class.forName("com.gildedgames.the_aether.items.ItemsAether");
            try {
               phoenixGloves = items.getField("phoenix_gloves").get(null);
            } catch (Throwable ignored) {
            }
            try {
               invisibilityCape = items.getField("invisibility_cape").get(null);
            } catch (Throwable ignored) {
            }
            overlay = new ModelBiped(1.0F);
            glovesModel = new ModelCustomArmor(1.0F);
            MinecraftForge.EVENT_BUS.register(new Hide());
            active = true;
            BendsLogger.log("Aether Legacy accessory compatibility enabled.", BendsLogger.INFO);
         } catch (Throwable t) {
            active = false;
            BendsLogger.log("Aether Legacy accessory compatibility failed: " + t, BendsLogger.ERROR);
         }
      }

      static void render(EntityPlayer player, ModelBendsPlayer bends, float partialTicks) {
         if (!active) {
            return;
         }
         ItemStack gloves = stack(player, glovesType);
         if (gloves != null && gloves.getItem() != null) {
            bind(textureOf(textureField, gloves.getItem()));
            if (phoenixGloves != null && gloves.getItem() == phoenixGloves) {
               if (player.hurtTime > 0) {
                  GL11.glColor3f(1.0F, 0.5F, 0.5F);
               } else {
                  GL11.glColor3f(1.0F, 1.0F, 1.0F);
               }
            } else {
               applyColor(player, gloves.getItem().getColorFromItemStack(gloves, 0));
            }
            glueGloves(bends, glovesModel);
            GL11.glColor3f(1.0F, 1.0F, 1.0F);
         }

         ItemStack pendant = stack(player, pendantType);
         if (pendant != null && pendant.getItem() != null) {
            bind(textureOf(textureField, pendant.getItem()));
            applyColor(player, pendant.getItem().getColorFromItemStack(new ItemStack(pendant.getItem(), 1, 0), 1));
            glueBody(bends, overlay.bipedBody);
            GL11.glColor3f(1.0F, 1.0F, 1.0F);
         }

         ItemStack cape = stack(player, capeType);
         if (cape != null && cape.getItem() != null && !player.isInvisible() && shouldRenderCape(player) && !wearingInvisibilityCape(player)) {
            bind(textureOf(textureField, cape.getItem()));
            applyColor(player, cape.getItem().getColorFromItemStack(cape, 0));
            glueCape(player, bends, overlay.bipedCloak, partialTicks);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         }
      }

      private static ItemStack stack(EntityPlayer player, Object type) {
         if (type == null) {
            return null;
         }
         try {
            Object playerAether = playerAetherGet.invoke(null, player);
            if (playerAether == null) {
               return null;
            }
            Object inventory = getAccessoryInventory.invoke(playerAether);
            if (inventory == null) {
               return null;
            }
            return (ItemStack) getStackInSlot.invoke(inventory, type);
         } catch (Throwable t) {
            return null;
         }
      }

      private static boolean shouldRenderCape(EntityPlayer player) {
         try {
            Object playerAether = playerAetherGet.invoke(null, player);
            return playerAether != null && shouldRenderCape.getBoolean(playerAether);
         } catch (Throwable t) {
            return true;
         }
      }

      private static boolean wearingInvisibilityCape(EntityPlayer player) {
         if (wearingAccessory == null || invisibilityCape == null || !(invisibilityCape instanceof Item)) {
            return false;
         }
         try {
            Object playerAether = playerAetherGet.invoke(null, player);
            Object inventory = playerAether == null ? null : getAccessoryInventory.invoke(playerAether);
            if (inventory == null) {
               return false;
            }
            return Boolean.TRUE.equals(wearingAccessory.invoke(inventory, new ItemStack((Item) invisibilityCape)));
         } catch (Throwable t) {
            return false;
         }
      }

      private static ModelBiped modelMisc() {
         try {
            Object renderer = instanceMethod.invoke(null);
            return renderer == null ? null : (ModelBiped) modelMiscField.get(renderer);
         } catch (Throwable t) {
            return null;
         }
      }

      @SideOnly(Side.CLIENT)
      public static final class Hide {
         @SubscribeEvent(priority = EventPriority.HIGHEST)
         public void hide(RenderLivingEvent.Post event) {
            if (event.renderer instanceof RenderBendsPlayer) {
               setVisible(false);
            }
         }

         @SubscribeEvent(priority = EventPriority.LOWEST)
         public void restore(RenderLivingEvent.Post event) {
            if (event.renderer instanceof RenderBendsPlayer) {
               setVisible(true);
            }
         }

         private static void setVisible(boolean visible) {
            ModelBiped misc = modelMisc();
            if (misc == null) {
               return;
            }
            hidePart(misc.bipedBody, visible);
            hidePart(misc.bipedLeftArm, visible);
            hidePart(misc.bipedRightArm, visible);
            hidePart(misc.bipedCloak, visible);
         }
      }
   }

   @SideOnly(Side.CLIENT)
   static final class AetherII {
      private static final int PENDANT_SLOT = 0;
      private static final int GLOVE_SLOT = 6;
      private static final ResourceLocation LEATHER_OVERLAY =
            new ResourceLocation("aether", "textures/armor/Leather Overlay.png");

      private static final ResourceLocation EAR_CAP =
            new ResourceLocation("aether", "textures/player/mouseEarCap.png");

      private static boolean resolved;
      private static boolean active;
      private static Method playerAetherGet;
      private static Field accessoriesField;
      private static Field stacksField;
      private static Field textureField;
      private static Class<?> leatherGlovesClass;
      private static Class<?> mouseEarCapClass;
      private static ModelBiped overlay;
      private static ModelBiped earModel;
      private static ModelCustomArmor glovesModel;
      private static ItemStack hiddenPendant;
      private static ItemStack hiddenGloves;
      private static ItemStack[] hiddenEarCaps;

      static void registerIfPresent() {
         if (resolved) {
            return;
         }
         resolved = true;
         if (!Loader.isModLoaded("aether")) {
            return;
         }
         try {
            Class<?> playerAether = Class.forName("net.aetherteam.aether.player.PlayerAether");
            playerAetherGet = playerAether.getMethod("get", EntityPlayer.class);
            accessoriesField = playerAether.getField("accessories");
            stacksField = Class.forName("net.aetherteam.aether.containers.inventory.InventoryAccessories").getField("stacks");
            textureField = Class.forName("net.aetherteam.aether.items.ItemAccessory").getField("texture");
            try {
               leatherGlovesClass = Class.forName("net.aetherteam.aether.items.ItemLeatherGloves");
            } catch (ClassNotFoundException ignored) {
               leatherGlovesClass = null;
            }
            mouseEarCapClass = Class.forName("net.aetherteam.aether.items.ItemMouseEarCap");
            overlay = new ModelBiped(0.6F);
            earModel = new ModelBiped(0.0F);
            glovesModel = new ModelCustomArmor(0.6F);
            MinecraftForge.EVENT_BUS.register(new Hide());
            active = true;
            BendsLogger.log("Aether II accessory compatibility enabled.", BendsLogger.INFO);
         } catch (Throwable t) {
            active = false;
            BendsLogger.log("Aether II accessory compatibility failed: " + t, BendsLogger.ERROR);
         }
      }

      static void render(EntityPlayer player, ModelBendsPlayer bends, float partialTicks) {
         if (!active) {
            return;
         }
         ItemStack[] stacks = stacks(player);
         if (stacks == null) {
            return;
         }
         ItemStack gloves = slot(stacks, GLOVE_SLOT);
         if (gloves != null && gloves.getItem() != null) {
            bind(textureOf(textureField, gloves.getItem()));
            applyColor(player, gloves.getItem().getColorFromItemStack(gloves, 0));
            glueGloves(bends, glovesModel);
            if (leatherGlovesClass != null && leatherGlovesClass.isInstance(gloves.getItem())) {
               bind(LEATHER_OVERLAY);
               applyColor(player, gloves.getItem().getColorFromItemStack(gloves, 1));
               glueGloves(bends, glovesModel);
            }
            GL11.glColor3f(1.0F, 1.0F, 1.0F);
         }

         ItemStack pendant = slot(stacks, PENDANT_SLOT);
         if (pendant != null && pendant.getItem() != null) {
            bind(textureOf(textureField, pendant.getItem()));
            applyColor(player, pendant.getItem().getColorFromItemStack(new ItemStack(pendant.getItem(), 1, 0), 1));
            glueBody(bends, overlay.bipedBody);
            GL11.glColor3f(1.0F, 1.0F, 1.0F);
         }

         renderMouseEarCap(player, bends, stacks);
      }

      private static void renderMouseEarCap(EntityPlayer player, ModelBendsPlayer bends, ItemStack[] stacks) {
         ItemStack cap = findMouseEarCap(stacks);
         if (cap == null || cap.getItem() == null) {
            return;
         }
         bind(EAR_CAP);
         applyColor(player, cap.getItem().getColorFromItemStack(cap, 0));
         GL11.glPushMatrix();
         glueHat(bends);
         for (int i = 0; i < 2; ++i) {
            GL11.glPushMatrix();
            GL11.glScalef(1.333333F, 1.333333F, 1.333333F);
            GL11.glTranslatef(0.28F * (float) (i * 2 - 1), -0.28F, 0.0F);
            ModelArmorGlue.renderVanillaPart(earModel.bipedEars, SCALE);
            GL11.glPopMatrix();
         }
         GL11.glTranslatef(0.0F, -0.01F, 0.0F);
         GL11.glDisable(GL11.GL_CULL_FACE);
         ModelArmorGlue.renderVanillaPart(overlay.bipedHead, 0.06F);
         GL11.glEnable(GL11.GL_CULL_FACE);
         GL11.glPopMatrix();
         GL11.glColor3f(1.0F, 1.0F, 1.0F);
      }

      private static ItemStack findMouseEarCap(ItemStack[] stacks) {
         for (int i = 0; i < stacks.length; ++i) {
            if (isMouseEarCap(stacks[i])) {
               return stacks[i];
            }
         }
         return null;
      }

      private static boolean isMouseEarCap(ItemStack stack) {
         return stack != null && stack.getItem() != null && mouseEarCapClass != null
               && mouseEarCapClass.isInstance(stack.getItem());
      }

      private static ItemStack slot(ItemStack[] stacks, int index) {
         return stacks.length > index ? stacks[index] : null;
      }

      private static ItemStack[] stacks(EntityPlayer player) {
         try {
            Object playerAether = playerAetherGet.invoke(null, player);
            if (playerAether == null) {
               return null;
            }
            Object accessories = accessoriesField.get(playerAether);
            if (accessories == null) {
               return null;
            }
            return (ItemStack[]) stacksField.get(accessories);
         } catch (Throwable t) {
            return null;
         }
      }

      @SideOnly(Side.CLIENT)
      public static final class Hide {
         @SubscribeEvent(priority = EventPriority.HIGHEST)
         public void hide(RenderPlayerEvent.Specials.Pre event) {
            if (!(event.renderer instanceof RenderBendsPlayer)) {
               return;
            }
            ItemStack[] stacks = stacks(event.entityPlayer);
            if (stacks == null) {
               return;
            }
            if (stacks.length > PENDANT_SLOT) {
               hiddenPendant = stacks[PENDANT_SLOT];
               stacks[PENDANT_SLOT] = null;
            }
            if (stacks.length > GLOVE_SLOT) {
               hiddenGloves = stacks[GLOVE_SLOT];
               stacks[GLOVE_SLOT] = null;
            }
            hiddenEarCaps = new ItemStack[stacks.length];
            for (int i = 0; i < stacks.length; ++i) {
               if (isMouseEarCap(stacks[i])) {
                  hiddenEarCaps[i] = stacks[i];
                  stacks[i] = null;
               }
            }
         }

         @SubscribeEvent(priority = EventPriority.LOWEST)
         public void restore(RenderPlayerEvent.Specials.Pre event) {
            if (!(event.renderer instanceof RenderBendsPlayer)) {
               return;
            }
            ItemStack[] stacks = stacks(event.entityPlayer);
            if (stacks == null) {
               hiddenPendant = null;
               hiddenGloves = null;
               hiddenEarCaps = null;
               return;
            }
            if (stacks.length > PENDANT_SLOT) {
               stacks[PENDANT_SLOT] = hiddenPendant;
            }
            if (stacks.length > GLOVE_SLOT) {
               stacks[GLOVE_SLOT] = hiddenGloves;
            }
            if (hiddenEarCaps != null) {
               int limit = Math.min(stacks.length, hiddenEarCaps.length);
               for (int i = 0; i < limit; ++i) {
                  if (hiddenEarCaps[i] != null) {
                     stacks[i] = hiddenEarCaps[i];
                  }
               }
            }
            hiddenPendant = null;
            hiddenGloves = null;
            hiddenEarCaps = null;
         }
      }
   }
}
