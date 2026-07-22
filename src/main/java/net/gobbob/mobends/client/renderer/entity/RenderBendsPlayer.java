package net.gobbob.mobends.client.renderer.entity;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.gobbob.mobends.MoBends;
import net.gobbob.mobends.client.model.ModelCustomArmor;
import net.gobbob.mobends.client.model.entity.ModelBendsPlayer;
import net.gobbob.mobends.compat.skinlayers3d.SkinLayersRender;
import net.gobbob.mobends.compat.superhero.SuperheroArmorCompat;
import net.gobbob.mobends.customarmor.CustomArmor;
import net.gobbob.mobends.data.Data_Player;
import net.gobbob.mobends.settings.SettingsBoolean;
import net.gobbob.mobends.settings.SettingsNode;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;

public class RenderBendsPlayer extends RenderPlayer {
   public int refreshModel = 0;
   private final BendsCapeRenderer capeRenderer = new BendsCapeRenderer();
   private boolean usingVanillaModels;

   public RenderBendsPlayer() {
      this.applyBendsModels();
   }

   private void applyBendsModels() {
      this.mainModel = new ModelBendsPlayer(0.0F);
      this.modelBipedMain = (ModelBendsPlayer)this.mainModel;
      this.modelArmorChestplate = new ModelBendsPlayer(1.0F);
      this.modelArmor = new ModelBendsPlayer(0.5F);
      this.usingVanillaModels = false;
   }

   private void applyVanillaModels() {
      this.mainModel = new ModelBiped(0.0F);
      this.modelBipedMain = (ModelBiped)this.mainModel;
      this.modelArmorChestplate = new ModelBiped(1.0F);
      this.modelArmor = new ModelBiped(0.5F);
      this.usingVanillaModels = true;
   }

   private void ensureModelsFor(AbstractClientPlayer player) {
      boolean wantVanilla = SuperheroArmorCompat.isWearingSuperheroSuit(player);
      if (wantVanilla != this.usingVanillaModels || this.refreshModel != MoBends.refreshModel) {
         if (wantVanilla) {
            this.applyVanillaModels();
         } else {
            this.applyBendsModels();
         }

         this.refreshModel = MoBends.refreshModel;
      }
   }

   protected int shouldRenderPass(AbstractClientPlayer p_77032_1_, int p_77032_2_, float p_77032_3_) {
      ItemStack itemstack = p_77032_1_.inventory.armorItemInSlot(3 - p_77032_2_);
      RenderPlayerEvent.SetArmorModel event = new RenderPlayerEvent.SetArmorModel(p_77032_1_, this, 3 - p_77032_2_, p_77032_3_, itemstack);
      MinecraftForge.EVENT_BUS.post(event);
      if (event.result != -1) {
         return event.result;
      } else {
         if (itemstack != null) {
            Item item = itemstack.getItem();
            if (item instanceof ItemArmor) {
               ItemArmor itemarmor = (ItemArmor)item;
               ResourceLocation texture = RenderBiped.getArmorResource(p_77032_1_, itemstack, p_77032_2_, (String)null);
               this.bindTexture(texture);
               ModelBiped modelbiped = p_77032_2_ == 2 ? this.modelArmor : this.modelArmorChestplate;
               modelbiped = ForgeHooksClient.getArmorModel(p_77032_1_, itemstack, p_77032_2_, modelbiped);
               // Fisk / Legends suits keep their own models. Never wrap with CustomArmor.
               if (!this.usingVanillaModels && !SuperheroArmorCompat.isSuperheroArmorItem(item)) {
                  modelbiped = CustomArmor.get(modelbiped, texture.getResourcePath(), p_77032_2_ == 2 ? 0.5F : 1.0F).armorModel;
                  if (modelbiped instanceof ModelCustomArmor && this.modelBipedMain instanceof ModelBendsPlayer) {
                     ((ModelCustomArmor)modelbiped).setSourceModel((ModelBendsPlayer)this.modelBipedMain);
                  }
               }

               modelbiped.bipedHead.showModel = p_77032_2_ == 0;
               modelbiped.bipedHeadwear.showModel = p_77032_2_ == 0;
               modelbiped.bipedBody.showModel = p_77032_2_ == 1 || p_77032_2_ == 2;
               modelbiped.bipedRightArm.showModel = p_77032_2_ == 1;
               modelbiped.bipedLeftArm.showModel = p_77032_2_ == 1;
               modelbiped.bipedRightLeg.showModel = p_77032_2_ == 2 || p_77032_2_ == 3;
               modelbiped.bipedLeftLeg.showModel = p_77032_2_ == 2 || p_77032_2_ == 3;

               this.setRenderPassModel(modelbiped);
               modelbiped.onGround = this.mainModel.onGround;
               modelbiped.isRiding = this.mainModel.isRiding;
               modelbiped.isChild = this.mainModel.isChild;
               int j = itemarmor.getColor(itemstack);
               if (j != -1) {
                  float f1 = (float)(j >> 16 & 255) / 255.0F;
                  float f2 = (float)(j >> 8 & 255) / 255.0F;
                  float f3 = (float)(j & 255) / 255.0F;
                  GL11.glColor3f(f1, f2, f3);
                  if (itemstack.isItemEnchanted()) {
                     return 31;
                  }

                  return 16;
               }

               GL11.glColor3f(1.0F, 1.0F, 1.0F);
               if (itemstack.isItemEnchanted()) {
                  return 15;
               }

               return 1;
            }
         }

         return -1;
      }
   }

   public void renderFirstPersonArm(EntityPlayer p_82441_1_) {
      if (this.usingVanillaModels) {
         super.renderFirstPersonArm(p_82441_1_);
         return;
      }

      // Skin Layers injects into RenderPlayer.renderFirstPersonArm and posts modelBipedMain's
      // right arm. Mo' Bends' arm is a SeparatedChild (posts the body) — swap a vanilla biped
      // for this call so stock FP 3D sleeves work, then restore the bent model.
      ModelBiped previousBiped = this.modelBipedMain;
      net.minecraft.client.model.ModelBase previousMain = this.mainModel;
      ModelBiped firstPersonArm = new ModelBiped(0.0F);
      this.modelBipedMain = firstPersonArm;
      this.mainModel = firstPersonArm;
      try {
         super.renderFirstPersonArm(p_82441_1_);
      } finally {
         this.modelBipedMain = previousBiped;
         this.mainModel = previousMain;
      }
   }

   private float interpolateRotation(float p_77034_1_, float p_77034_2_, float p_77034_3_) {
      float f3;
      for(f3 = p_77034_2_ - p_77034_1_; f3 < -180.0F; f3 += 360.0F) {
      }

      while(f3 >= 180.0F) {
         f3 -= 360.0F;
      }

      return p_77034_1_ + p_77034_3_ * f3;
   }

   protected void rotateSuperCorpse(EntityLivingBase p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_) {
      GL11.glRotatef(180.0F - p_77043_3_, 0.0F, 1.0F, 0.0F);
      if (p_77043_1_.deathTime > 0) {
         float f3 = ((float)p_77043_1_.deathTime + p_77043_4_ - 1.0F) / 20.0F * 1.6F;
         f3 = MathHelper.sqrt_float(f3);
         if (f3 > 1.0F) {
            f3 = 1.0F;
         }

         GL11.glRotatef(f3 * this.getDeathMaxRotation(p_77043_1_), 0.0F, 0.0F, 1.0F);
      } else {
         String s = EnumChatFormatting.getTextWithoutFormattingCodes(p_77043_1_.getCommandSenderName());
         if ((s.equals("Dinnerbone") || s.equals("Grumm")) && (!(p_77043_1_ instanceof EntityPlayer) || !((EntityPlayer)p_77043_1_).getHideCape())) {
            GL11.glTranslatef(0.0F, p_77043_1_.height + 0.1F, 0.0F);
            GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
         }
      }

   }

   /**
    * Stock 3D Skin Layers posts each limb alone. With Mo' Bends that floats hair (head is a body
    * child) and drops the jacket to the hips. Disable the mixin layers and glue our own.
    */
   protected void renderModel(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float ageInTicks,
         float netHeadYaw, float headPitch, float scale) {
      if (this.usingVanillaModels || !SkinLayersRender.isActive() || !(entity instanceof AbstractClientPlayer)) {
         super.renderModel(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
         return;
      }

      Object[] stockLayers = SkinLayersRender.disableStockFeatureRenderers(this);
      try {
         super.renderModel(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
         SkinLayersRender.renderPlayerLayers((AbstractClientPlayer) entity, this.modelBipedMain, scale);
      } finally {
         SkinLayersRender.restoreStockFeatureRenderers(this, stockLayers);
      }
   }

   public void doRender(AbstractClientPlayer p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
      this.ensureModelsFor(p_76986_1_);

      if (!this.usingVanillaModels) {
         Data_Player data = Data_Player.get(p_76986_1_.getEntityId());
         float f2 = this.interpolateRotation(p_76986_1_.prevRenderYawOffset, p_76986_1_.renderYawOffset, p_76986_9_);
         this.interpolateRotation(p_76986_1_.prevRotationYawHead, p_76986_1_.rotationYawHead, p_76986_9_);
         if (((SettingsBoolean)SettingsNode.getSetting("swordTrail")).data) {
            GL11.glPushMatrix();
            float f5 = 0.0625F;
            float f4 = this.handleRotationFloat(p_76986_1_, p_76986_9_);
            this.rotateSuperCorpse(p_76986_1_, f4, f2, p_76986_9_);
            GL11.glTranslatef(0.0F, -24.0F * f5 - 0.0078125F - 2.0F * f5, 0.0F);
            GL11.glScalef(f5, f5, f5);
            data.swordTrail.render((ModelBendsPlayer)this.mainModel);
            GL11.glPopMatrix();
         }
      }

      super.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
   }

   protected void rotateCorpse(AbstractClientPlayer argPlayer, float p_77043_2_, float p_77043_3_, float p_77043_4_) {
      if (argPlayer.isEntityAlive() && argPlayer.isPlayerSleeping()) {
         GL11.glRotatef(argPlayer.getBedOrientationInDegrees(), 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(this.getDeathMaxRotation(argPlayer), 0.0F, 0.0F, 1.0F);
         GL11.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
      } else if (this.usingVanillaModels) {
         // Fisk / Legends own animations — do not apply Mo' Bends body transforms.
         super.rotateCorpse(argPlayer, p_77043_2_, p_77043_3_, p_77043_4_);
      } else {
         super.rotateCorpse(argPlayer, p_77043_2_, p_77043_3_, p_77043_4_);
         ((ModelBendsPlayer)this.modelBipedMain).updateWithEntityData(argPlayer);
         // Already in body space after rotateCorpse — do NOT sandwich renderYawOffset
         // (that caused upside-down / on-back flight in F5).
         ((ModelBendsPlayer)this.modelBipedMain).postRender(0.0625F, argPlayer.height);
      }

   }

   protected void renderEquippedItems(AbstractClientPlayer argPlayer, float argPartialTicks) {
      if (this.usingVanillaModels) {
         super.renderEquippedItems(argPlayer, argPartialTicks);
         return;
      }

      RenderPlayerEvent.Specials.Pre event = new RenderPlayerEvent.Specials.Pre(argPlayer, this, argPartialTicks);
      if (!MinecraftForge.EVENT_BUS.post(event)) {
         GL11.glColor3f(1.0F, 1.0F, 1.0F);
         ItemStack itemstack = argPlayer.inventory.armorItemInSlot(3);
         if (itemstack != null && event.renderHelmet) {
            GL11.glPushMatrix();
            this.modelBipedMain.bipedBody.postRender(0.0625F);
            this.modelBipedMain.bipedHead.postRender(0.0625F);
            if (!(itemstack.getItem() instanceof ItemBlock)) {
               if (itemstack.getItem() == Items.skull) {
                  float f1 = 1.0625F;
                  GL11.glScalef(f1, -f1, -f1);
                  GameProfile gameprofile = null;
                  if (itemstack.hasTagCompound()) {
                     NBTTagCompound nbttagcompound = itemstack.getTagCompound();
                     if (nbttagcompound.hasKey("SkullOwner", 10)) {
                        gameprofile = NBTUtil.func_152459_a(nbttagcompound.getCompoundTag("SkullOwner"));
                     } else if (nbttagcompound.hasKey("SkullOwner", 8) && !StringUtils.isNullOrEmpty(nbttagcompound.getString("SkullOwner"))) {
                        gameprofile = new GameProfile((UUID)null, nbttagcompound.getString("SkullOwner"));
                     }
                  }

                  TileEntitySkullRenderer.field_147536_b.func_152674_a(-0.5F, 0.0F, -0.5F, 1, 180.0F, itemstack.getItemDamage(), gameprofile);
               }
            } else {
               IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(itemstack, ItemRenderType.EQUIPPED);
               boolean is3D = customRenderer != null && customRenderer.shouldUseRenderHelper(ItemRenderType.EQUIPPED, itemstack, ItemRendererHelper.BLOCK_3D);
               if (is3D || RenderBlocks.renderItemIn3d(Block.getBlockFromItem(itemstack.getItem()).getRenderType())) {
                  float f1 = 0.625F;
                  GL11.glTranslatef(0.0F, -0.25F, 0.0F);
                  GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                  GL11.glScalef(f1, -f1, -f1);
               }

               this.renderManager.itemRenderer.renderItem(argPlayer, itemstack, 0);
            }

            GL11.glPopMatrix();
         }

         if (argPlayer.getCommandSenderName().equals("deadmau5") && argPlayer.func_152123_o()) {
            this.bindTexture(argPlayer.getLocationSkin());

            for(int j = 0; j < 2; ++j) {
               float f9 = argPlayer.prevRotationYaw + (argPlayer.rotationYaw - argPlayer.prevRotationYaw) * argPartialTicks - (argPlayer.prevRenderYawOffset + (argPlayer.renderYawOffset - argPlayer.prevRenderYawOffset) * argPartialTicks);
               float f10 = argPlayer.prevRotationPitch + (argPlayer.rotationPitch - argPlayer.prevRotationPitch) * argPartialTicks;
               GL11.glPushMatrix();
               GL11.glRotatef(f9, 0.0F, 1.0F, 0.0F);
               GL11.glRotatef(f10, 1.0F, 0.0F, 0.0F);
               GL11.glTranslatef(0.375F * (float)(j * 2 - 1), 0.0F, 0.0F);
               GL11.glTranslatef(0.0F, -0.375F, 0.0F);
               GL11.glRotatef(-f10, 1.0F, 0.0F, 0.0F);
               GL11.glRotatef(-f9, 0.0F, 1.0F, 0.0F);
               float f2 = 1.3333334F;
               GL11.glScalef(f2, f2, f2);
               this.modelBipedMain.renderEars(0.0625F);
               GL11.glPopMatrix();
            }
         }

         boolean flag = argPlayer.func_152122_n();
         flag = event.renderCape && flag;
         if (flag && !argPlayer.isInvisible() && !argPlayer.getHideCape()) {
            this.bindTexture(argPlayer.getLocationCape());
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPushMatrix();
            float scale = 0.0625F;
            // Body postRender already includes Mo' Bends crouch lean — do not add vanilla
            // sneak +4px / +25° (those assume an upright torso and pull the cape off the back).
            this.modelBipedMain.bipedBody.postRender(scale);

            // Match 1.12.2 LayerCustomCape attach: back of torso, slightly out from the body.
            GL11.glTranslatef(0.0F, -12.0F * scale, 2.2F * scale);

            double d3 = argPlayer.field_71091_bM + (argPlayer.field_71094_bP - argPlayer.field_71091_bM) * (double)argPartialTicks - (argPlayer.prevPosX + (argPlayer.posX - argPlayer.prevPosX) * (double)argPartialTicks);
            double d4 = argPlayer.field_71096_bN + (argPlayer.field_71095_bQ - argPlayer.field_71096_bN) * (double)argPartialTicks - (argPlayer.prevPosY + (argPlayer.posY - argPlayer.prevPosY) * (double)argPartialTicks);
            double d0 = argPlayer.field_71097_bO + (argPlayer.field_71085_bR - argPlayer.field_71097_bO) * (double)argPartialTicks - (argPlayer.prevPosZ + (argPlayer.posZ - argPlayer.prevPosZ) * (double)argPartialTicks);
            float f4 = argPlayer.prevRenderYawOffset + (argPlayer.renderYawOffset - argPlayer.prevRenderYawOffset) * argPartialTicks;
            double d1 = (double)MathHelper.sin(f4 * (float)Math.PI / 180.0F);
            double d2 = (double)(-MathHelper.cos(f4 * (float)Math.PI / 180.0F));
            float f5 = (float)d4 * 10.0F;
            if (f5 < -6.0F) {
               f5 = -6.0F;
            }

            if (f5 > 32.0F) {
               f5 = 32.0F;
            }

            float f6 = (float)(d3 * d1 + d0 * d2) * 100.0F;
            float f7 = (float)(d3 * d2 - d0 * d1) * 100.0F;
            if (f6 < 0.0F) {
               f6 = 0.0F;
            }

            float f8 = argPlayer.prevCameraYaw + (argPlayer.cameraYaw - argPlayer.prevCameraYaw) * argPartialTicks;
            f5 += MathHelper.sin((argPlayer.prevDistanceWalkedModified + (argPlayer.distanceWalkedModified - argPlayer.prevDistanceWalkedModified) * argPartialTicks) * 6.0F) * 32.0F * f8;

            Data_Player capeData = Data_Player.get(argPlayer.getEntityId());
            boolean flyingSprint = argPlayer.capabilities.isFlying && argPlayer.isSprinting();
            if (flyingSprint) {
               capeData.setCapeWaveSpeed(4.0F);
            } else {
               capeData.setCapeWaveSpeed(1.0F);
               GL11.glRotatef(6.0F + f6 / 2.0F + f5, 1.0F, 0.0F, 0.0F);
               GL11.glRotatef(f7 / 2.0F, 0.0F, 0.0F, 1.0F);
               GL11.glRotatef(-f7 / 2.0F, 0.0F, 1.0F, 0.0F);
            }

            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            this.capeRenderer.applyAnimation(capeData);
            this.capeRenderer.render(scale);
            GL11.glPopMatrix();
         }

         ItemStack itemstack1 = argPlayer.inventory.getCurrentItem();
         if (itemstack1 != null && event.renderItem) {
            GL11.glPushMatrix();
            this.modelBipedMain.bipedRightArm.postRender(0.0625F);
            GL11.glTranslatef(0.0F, 0.5625F, 0.0F);
            if (this.modelBipedMain instanceof ModelBendsPlayer) {
               GL11.glRotatef(((ModelBendsPlayer)this.modelBipedMain).renderItemRotation.vSmooth.x, 1.0F, 0.0F, 0.0F);
               GL11.glRotatef(((ModelBendsPlayer)this.modelBipedMain).renderItemRotation.vSmooth.y, 0.0F, -1.0F, 0.0F);
               GL11.glRotatef(((ModelBendsPlayer)this.modelBipedMain).renderItemRotation.vSmooth.z, 0.0F, 0.0F, 1.0F);
            }

            GL11.glTranslatef(-0.0625F, -0.125F, 0.0625F);
            if (argPlayer.fishEntity != null) {
               itemstack1 = new ItemStack(Items.stick);
            }

            EnumAction enumaction = null;
            if (argPlayer.getItemInUseCount() > 0) {
               enumaction = itemstack1.getItemUseAction();
            }

            IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(itemstack1, ItemRenderType.EQUIPPED);
            boolean is3D = customRenderer != null && customRenderer.shouldUseRenderHelper(ItemRenderType.EQUIPPED, itemstack1, ItemRendererHelper.BLOCK_3D);
            if (!is3D && (!(itemstack1.getItem() instanceof ItemBlock) || !RenderBlocks.renderItemIn3d(Block.getBlockFromItem(itemstack1.getItem()).getRenderType()))) {
               if (itemstack1.getItem() == Items.bow) {
                  float f2 = 0.625F;
                  GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
                  GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
                  GL11.glScalef(f2, -f2, f2);
                  GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
                  GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
               } else if (itemstack1.getItem().isFull3D()) {
                  float f2 = 0.625F;
                  if (itemstack1.getItem().shouldRotateAroundWhenRendering()) {
                     GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
                     GL11.glTranslatef(0.0F, -0.125F, 0.0F);
                  }

                  if (argPlayer.getItemInUseCount() > 0 && enumaction == EnumAction.block) {
                     GL11.glTranslatef(0.05F, 0.0F, -0.1F);
                     GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
                     GL11.glRotatef(-10.0F, 1.0F, 0.0F, 0.0F);
                     GL11.glRotatef(-60.0F, 0.0F, 0.0F, 1.0F);
                  }

                  GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
                  GL11.glScalef(f2, -f2, f2);
                  GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
                  GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
               } else {
                  float f2 = 0.375F;
                  GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
                  GL11.glScalef(f2, f2, f2);
                  GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
                  GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
                  GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
               }
            } else {
               float f2 = 0.5F;
               GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
               f2 *= 0.75F;
               GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
               GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
               GL11.glScalef(-f2, -f2, f2);
            }

            if (itemstack1.getItem().requiresMultipleRenderPasses()) {
               for(int k = 0; k < itemstack1.getItem().getRenderPasses(itemstack1.getItemDamage()); ++k) {
                  int i = itemstack1.getItem().getColorFromItemStack(itemstack1, k);
                  float f12 = (float)(i >> 16 & 255) / 255.0F;
                  float f3 = (float)(i >> 8 & 255) / 255.0F;
                  float f4 = (float)(i & 255) / 255.0F;
                  GL11.glColor4f(f12, f3, f4, 1.0F);
                  this.renderManager.itemRenderer.renderItem(argPlayer, itemstack1, k);
               }
            } else {
               int k = itemstack1.getItem().getColorFromItemStack(itemstack1, 0);
               float f11 = (float)(k >> 16 & 255) / 255.0F;
               float f12 = (float)(k >> 8 & 255) / 255.0F;
               float f3 = (float)(k & 255) / 255.0F;
               GL11.glColor4f(f11, f12, f3, 1.0F);
               this.renderManager.itemRenderer.renderItem(argPlayer, itemstack1, 0);
            }

            GL11.glPopMatrix();
         }

         net.gobbob.mobends.compat.hats.HatsRender.renderPlayerHat(argPlayer, this.modelBipedMain, argPartialTicks);

         MinecraftForge.EVENT_BUS.post(new RenderPlayerEvent.Specials.Post(argPlayer, this, argPartialTicks));
      }
   }
}
