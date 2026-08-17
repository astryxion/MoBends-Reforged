package net.gobbob.mobends.client.renderer.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.gobbob.mobends.AnimatedEntity;
import net.gobbob.mobends.MoBends;
import net.gobbob.mobends.client.model.entity.ModelBendsSkeleton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderSkeleton;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class RenderBendsSkeleton extends RenderBiped {
   private static final ResourceLocation skeletonTextures = new ResourceLocation("textures/entity/skeleton/skeleton.png");
   private static final ResourceLocation witherSkeletonTextures = new ResourceLocation("textures/entity/skeleton/wither_skeleton.png");
   private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
   public int refreshModel = 0;
   private final RenderSkeleton vanilla = new RenderSkeleton();

   public RenderBendsSkeleton() {
      super(new ModelBendsSkeleton(), 0.5F);
   }

   protected void func_82421_b() {
      this.field_82423_g = new ModelBendsSkeleton(1.0F, true);
      this.field_82425_h = new ModelBendsSkeleton(0.5F, true);
   }

   protected void func_82422_c() {
      GL11.glTranslatef(0.09375F, 0.1875F, 0.0F);
   }

   public void doRender(EntitySkeleton skeleton, double x, double y, double z, float yaw, float partialTicks) {
      if (!AnimatedEntity.shouldAnimate(skeleton)) {
         VanillaRenderBridge.bind(this.vanilla);
         this.vanilla.doRender((EntityLiving)skeleton, x, y, z, yaw, partialTicks);
         return;
      }

      if (this.refreshModel != MoBends.refreshModel) {
         this.mainModel = new ModelBendsSkeleton();
         this.modelBipedMain = (ModelBiped)this.mainModel;
         this.func_82421_b();
         this.refreshModel = MoBends.refreshModel;
      }

      super.doRender((EntityLiving)skeleton, x, y, z, yaw, partialTicks);
   }

   protected ResourceLocation getEntityTexture(EntitySkeleton skeleton) {
      return skeleton.getSkeletonType() == 1 ? witherSkeletonTextures : skeletonTextures;
   }

   protected void preRenderCallback(EntitySkeleton skeleton, float partialTicks) {
      if (skeleton.getSkeletonType() == 1) {
         GL11.glScalef(1.2F, 1.2F, 1.2F);
      }
   }

   protected void rotateCorpse(EntitySkeleton skeleton, float p_77043_2_, float p_77043_3_, float p_77043_4_) {
      super.rotateCorpse(skeleton, p_77043_2_, p_77043_3_, p_77043_4_);
      if (this.modelBipedMain instanceof ModelBendsSkeleton) {
         ModelBendsSkeleton model = (ModelBendsSkeleton)this.modelBipedMain;
         model.updateWithEntityData(skeleton);
         model.postRender(0.0625F);
      }
   }

   protected void renderEquippedItems(EntitySkeleton skeleton, float partialTicks) {
      ItemStack held = skeleton.getHeldItem();
      float draw = this.modelBipedMain instanceof ModelBendsSkeleton ? ((ModelBendsSkeleton)this.modelBipedMain).bowDraw : 0.0F;
      if (held != null && held.getItem() instanceof ItemBow && draw > 0.5F) {
         skeleton.setCurrentItemOrArmor(0, (ItemStack)null);
         super.renderEquippedItems((EntityLiving)skeleton, partialTicks);
         skeleton.setCurrentItemOrArmor(0, held);
         this.renderDrawnBow(skeleton, held, draw);
      } else {
         super.renderEquippedItems((EntityLiving)skeleton, partialTicks);
      }

      net.gobbob.mobends.compat.hats.HatsRender.renderSkeletonHat(skeleton, this.modelBipedMain, partialTicks);
   }

   /**
    * 1.12.2 shows the pulling bow model (nocked arrow). 1.7.10 {@code EntityLivingBase.getItemIcon}
    * only swaps to pulling icons for players, so skeletons would otherwise render the empty standby bow.
    */
   private void renderDrawnBow(EntitySkeleton skeleton, ItemStack bow, float draw) {
      GL11.glPushMatrix();
      GL11.glColor3f(1.0F, 1.0F, 1.0F);
      if (this.mainModel.isChild) {
         float f1 = 0.5F;
         GL11.glTranslatef(0.0F, 0.625F, 0.0F);
         GL11.glRotatef(-20.0F, -1.0F, 0.0F, 0.0F);
         GL11.glScalef(f1, f1, f1);
      }

      this.modelBipedMain.bipedRightArm.postRender(0.0625F);
      GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);
      GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
      GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
      GL11.glScalef(0.625F, -0.625F, 0.625F);
      GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
      GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);

      int pulling = 0;
      if (draw >= 18.0F) {
         pulling = 2;
      } else if (draw > 13.0F) {
         pulling = 1;
      }

      IIcon icon = bow.getItem() == Items.bow ? Items.bow.getItemIconForUseDuration(pulling) : bow.getIconIndex();
      if (icon == null) {
         icon = bow.getIconIndex();
      }

      if (icon != null) {
         this.bindTexture(this.renderManager.renderEngine.getResourceLocation(bow.getItemSpriteNumber()));
         GL11.glEnable(GL12.GL_RESCALE_NORMAL);
         // ItemRenderer.renderItem 2D equipped path — without this the sprite is tiny and sideways.
         GL11.glTranslatef(0.0F, -0.3F, 0.0F);
         GL11.glScalef(1.5F, 1.5F, 1.5F);
         GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
         GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
         int color = bow.getItem().getColorFromItemStack(bow, 0);
         GL11.glColor4f((float)(color >> 16 & 255) / 255.0F, (float)(color >> 8 & 255) / 255.0F, (float)(color & 255) / 255.0F, 1.0F);
         ItemRenderer.renderItemIn2D(Tessellator.instance, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 0.0625F);
         if (bow.hasEffect(0)) {
            this.renderItemGlint();
         }

         GL11.glDisable(GL12.GL_RESCALE_NORMAL);
      }

      GL11.glPopMatrix();
   }

   private void renderItemGlint() {
      GL11.glDepthFunc(GL11.GL_EQUAL);
      GL11.glDisable(GL11.GL_LIGHTING);
      this.bindTexture(RES_ITEM_GLINT);
      GL11.glEnable(GL11.GL_BLEND);
      OpenGlHelper.glBlendFunc(768, 1, 1, 0);
      float tint = 0.76F;
      GL11.glColor4f(0.5F * tint, 0.25F * tint, 0.8F * tint, 1.0F);
      GL11.glMatrixMode(GL11.GL_TEXTURE);
      GL11.glPushMatrix();
      GL11.glScalef(0.125F, 0.125F, 0.125F);
      float phase = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
      GL11.glTranslatef(phase, 0.0F, 0.0F);
      GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
      ItemRenderer.renderItemIn2D(Tessellator.instance, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      GL11.glScalef(0.125F, 0.125F, 0.125F);
      phase = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
      GL11.glTranslatef(-phase, 0.0F, 0.0F);
      GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
      ItemRenderer.renderItemIn2D(Tessellator.instance, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
      GL11.glPopMatrix();
      GL11.glMatrixMode(GL11.GL_MODELVIEW);
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glEnable(GL11.GL_LIGHTING);
      GL11.glDepthFunc(GL11.GL_LEQUAL);
   }

   protected void renderEquippedItems(EntityLiving living, float partialTicks) {
      this.renderEquippedItems((EntitySkeleton)living, partialTicks);
   }

   protected ResourceLocation getEntityTexture(EntityLiving living) {
      return this.getEntityTexture((EntitySkeleton)living);
   }

   public void doRender(EntityLiving living, double x, double y, double z, float yaw, float partialTicks) {
      this.doRender((EntitySkeleton)living, x, y, z, yaw, partialTicks);
   }

   protected int shouldRenderPass(EntityLiving living, int pass, float partialTicks) {
      return super.shouldRenderPass(living, pass, partialTicks);
   }

   protected void preRenderCallback(EntityLivingBase living, float partialTicks) {
      this.preRenderCallback((EntitySkeleton)living, partialTicks);
   }

   protected void renderEquippedItems(EntityLivingBase living, float partialTicks) {
      this.renderEquippedItems((EntitySkeleton)living, partialTicks);
   }

   protected void rotateCorpse(EntityLivingBase living, float p_77043_2_, float p_77043_3_, float p_77043_4_) {
      this.rotateCorpse((EntitySkeleton)living, p_77043_2_, p_77043_3_, p_77043_4_);
   }

   public void doRender(EntityLivingBase living, double x, double y, double z, float yaw, float partialTicks) {
      this.doRender((EntitySkeleton)living, x, y, z, yaw, partialTicks);
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return this.getEntityTexture((EntitySkeleton)entity);
   }

   public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
      this.doRender((EntitySkeleton)entity, x, y, z, yaw, partialTicks);
   }
}
