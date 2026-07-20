package net.gobbob.mobends.client.model.entity;

import net.gobbob.mobends.AnimatedEntity;
import net.gobbob.mobends.animation.player.Animation_Falling;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.ModelRendererBends_SeperatedChild;
import net.gobbob.mobends.client.renderer.SwordTrail;
import net.gobbob.mobends.data.Data_Player;
import net.gobbob.mobends.pack.BendsPack;
import net.gobbob.mobends.pack.BendsVar;
import net.gobbob.mobends.util.SmoothVector3f;
import net.gobbob.mobends.util.Quaternion;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class ModelBendsPlayer extends ModelBiped {
   public ModelRenderer bipedRightForeArm;
   public ModelRenderer bipedLeftForeArm;
   public ModelRenderer bipedRightForeLeg;
   public ModelRenderer bipedLeftForeLeg;
   public SmoothVector3f renderOffset;
   public SmoothVector3f renderRotation;
   /** Body-centered lean (1.12.2 centerRotation). Applied around entity mid-height, not the feet. */
   public SmoothVector3f centerRotation;
   /** Quaternion centerRotation for flying (1.12.2 SmoothOrientation). */
   public Quaternion centerQuat = new Quaternion();
   public Quaternion centerQuatTarget = new Quaternion();
   public boolean centerQuatActive = false;
   public SmoothVector3f renderItemRotation;
   public SwordTrail swordTrail;
   public float headRotationX;
   public float headRotationY;
   public float armSwing;
   public float armSwingAmount;

   public ModelBendsPlayer() {
      this(0.0F);
   }

   public ModelBendsPlayer(float p_i1148_1_) {
      this(p_i1148_1_, 0.0F, 64, 32);
   }

   public ModelBendsPlayer(float p_i1149_1_, float p_i1149_2_, int p_i1149_3_, int p_i1149_4_) {
      // Use ModelBiped's ctor so texture size is set on the obfuscated ModelBase fields
      // (avoids NoSuchFieldError if a deobf jar is installed by mistake).
      super(p_i1149_1_, p_i1149_2_, p_i1149_3_, p_i1149_4_);
      this.renderOffset = new SmoothVector3f();
      this.renderRotation = new SmoothVector3f();
      this.centerRotation = new SmoothVector3f();
      this.renderItemRotation = new SmoothVector3f();
      this.swordTrail = new SwordTrail();
      this.bipedCloak = new ModelRendererBends(this, 0, 0);
      this.bipedCloak.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, p_i1149_1_);
      this.bipedEars = new ModelRendererBends(this, 24, 0);
      this.bipedEars.addBox(-3.0F, -6.0F, -1.0F, 6, 6, 1, p_i1149_1_);
      this.bipedHead = new ModelRendererBends(this, 0, 0);
      this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, p_i1149_1_);
      this.bipedHead.setRotationPoint(0.0F, 0.0F + p_i1149_2_ - 12.0F, 0.0F);
      this.bipedHeadwear = new ModelRendererBends(this, 32, 0);
      this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, p_i1149_1_ + 0.5F);
      this.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.bipedBody = (new ModelRendererBends(this, 16, 16)).setShowChildIfHidden(true);
      this.bipedBody.addBox(-4.0F, -12.0F, -2.0F, 8, 12, 4, p_i1149_1_);
      this.bipedBody.setRotationPoint(0.0F, 0.0F + p_i1149_2_ + 12.0F, 0.0F);
      this.bipedRightArm = (new ModelRendererBends_SeperatedChild(this, 40, 16)).setMother((ModelRendererBends)this.bipedBody);
      this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 6, 4, p_i1149_1_);
      this.bipedRightArm.setRotationPoint(-5.0F, 2.0F + p_i1149_2_ - 12.0F, 0.0F);
      this.bipedLeftArm = (new ModelRendererBends_SeperatedChild(this, 40, 16)).setMother((ModelRendererBends)this.bipedBody);
      this.bipedLeftArm.mirror = true;
      this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 6, 4, p_i1149_1_);
      this.bipedLeftArm.setRotationPoint(5.0F, 2.0F + p_i1149_2_ - 12.0F, 0.0F);
      this.bipedRightLeg = new ModelRendererBends(this, 0, 16);
      this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, p_i1149_1_);
      this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F + p_i1149_2_, 0.0F);
      this.bipedLeftLeg = new ModelRendererBends(this, 0, 16);
      this.bipedLeftLeg.mirror = true;
      this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, p_i1149_1_);
      this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F + p_i1149_2_, 0.0F);
      this.bipedRightForeArm = new ModelRendererBends(this, 40, 22);
      this.bipedRightForeArm.addBox(0.0F, 0.0F, -4.0F, 4, 6, 4, p_i1149_1_);
      this.bipedRightForeArm.setRotationPoint(-3.0F, 4.0F, 2.0F);
      ((ModelRendererBends)this.bipedRightForeArm).getBox().offsetTextureQuad(this.bipedRightForeArm, 3, 0.0F, -6.0F);
      this.bipedLeftForeArm = new ModelRendererBends(this, 40, 22);
      this.bipedLeftForeArm.mirror = true;
      this.bipedLeftForeArm.addBox(0.0F, 0.0F, -4.0F, 4, 6, 4, p_i1149_1_);
      this.bipedLeftForeArm.setRotationPoint(-1.0F, 4.0F, 2.0F);
      ((ModelRendererBends)this.bipedLeftForeArm).getBox().offsetTextureQuad(this.bipedRightForeArm, 3, 0.0F, -6.0F);
      this.bipedRightForeLeg = new ModelRendererBends(this, 0, 22);
      this.bipedRightForeLeg.addBox(-2.0F, 0.0F, 0.0F, 4, 6, 4, p_i1149_1_);
      this.bipedRightForeLeg.setRotationPoint(0.0F, 6.0F, -2.0F);
      ((ModelRendererBends)this.bipedRightForeLeg).getBox().offsetTextureQuad(this.bipedRightForeLeg, 3, 0.0F, -6.0F);
      this.bipedLeftForeLeg = new ModelRendererBends(this, 0, 22);
      this.bipedLeftForeLeg.mirror = true;
      this.bipedLeftForeLeg.addBox(-2.0F, 0.0F, 0.0F, 4, 6, 4, p_i1149_1_);
      this.bipedLeftForeLeg.setRotationPoint(0.0F, 6.0F, -2.0F);
      ((ModelRendererBends)this.bipedLeftForeLeg).getBox().offsetTextureQuad(this.bipedLeftForeLeg, 3, 0.0F, -6.0F);
      this.bipedBody.addChild(this.bipedHead);
      this.bipedBody.addChild(this.bipedRightArm);
      this.bipedBody.addChild(this.bipedLeftArm);
      this.bipedHead.addChild(this.bipedHeadwear);
      this.bipedRightArm.addChild(this.bipedRightForeArm);
      this.bipedLeftArm.addChild(this.bipedLeftForeArm);
      this.bipedRightLeg.addChild(this.bipedRightForeLeg);
      this.bipedLeftLeg.addChild(this.bipedLeftForeLeg);
      ((ModelRendererBends_SeperatedChild)this.bipedRightArm).setSeperatedPart((ModelRendererBends)this.bipedRightForeArm);
      ((ModelRendererBends_SeperatedChild)this.bipedLeftArm).setSeperatedPart((ModelRendererBends)this.bipedLeftForeArm);
      ((ModelRendererBends)this.bipedRightArm).offsetBox_Add(-0.01F, 0.0F, -0.01F).resizeBox(4.02F, 6.0F, 4.02F).updateVertices();
      ((ModelRendererBends)this.bipedLeftArm).offsetBox_Add(-0.01F, 0.0F, -0.01F).resizeBox(4.02F, 6.0F, 4.02F).updateVertices();
      ((ModelRendererBends)this.bipedRightLeg).offsetBox_Add(-0.01F, 0.0F, -0.01F).resizeBox(4.02F, 6.0F, 4.02F).updateVertices();
      ((ModelRendererBends)this.bipedLeftLeg).offsetBox_Add(-0.01F, 0.0F, -0.01F).resizeBox(4.02F, 6.0F, 4.02F).updateVertices();
   }

   public void render(Entity argEntity, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_) {
      this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, argEntity);
      if (this.isChild) {
         float f6 = 2.0F;
         GL11.glPushMatrix();
         GL11.glScalef(1.5F / f6, 1.5F / f6, 1.5F / f6);
         GL11.glTranslatef(0.0F, 16.0F * p_78088_7_, 0.0F);
         this.bipedHead.render(p_78088_7_);
         GL11.glPopMatrix();
         GL11.glPushMatrix();
         GL11.glScalef(1.0F / f6, 1.0F / f6, 1.0F / f6);
         GL11.glTranslatef(0.0F, 24.0F * p_78088_7_, 0.0F);
         this.bipedBody.render(p_78088_7_);
         this.bipedRightArm.render(p_78088_7_);
         this.bipedLeftArm.render(p_78088_7_);
         this.bipedRightLeg.render(p_78088_7_);
         this.bipedLeftLeg.render(p_78088_7_);
         this.bipedHeadwear.render(p_78088_7_);
         GL11.glPopMatrix();
      } else {
         this.bipedBody.render(p_78088_7_);
         this.bipedRightLeg.render(p_78088_7_);
         this.bipedLeftLeg.render(p_78088_7_);
      }

   }

   public void setRotationAngles(float argSwingTime, float argSwingAmount, float argArmSway, float argHeadY, float argHeadX, float argNr6, Entity argEntity) {
      if (Minecraft.getMinecraft().theWorld != null) {
         if (!Minecraft.getMinecraft().theWorld.isRemote || !Minecraft.getMinecraft().isGamePaused()) {
            Data_Player data = Data_Player.get(argEntity.getEntityId());
            this.armSwing = argSwingTime;
            this.armSwingAmount = argSwingAmount;
            this.headRotationX = argHeadX;
            this.headRotationY = argHeadY;
            if (Minecraft.getMinecraft().currentScreen != null) {
               this.headRotationY = 0.0F;
            }

            ((ModelRendererBends)this.bipedHead).sync(data.head);
            ((ModelRendererBends)this.bipedHeadwear).sync(data.headwear);
            ((ModelRendererBends)this.bipedBody).sync(data.body);
            ((ModelRendererBends)this.bipedRightArm).sync(data.rightArm);
            ((ModelRendererBends)this.bipedLeftArm).sync(data.leftArm);
            ((ModelRendererBends)this.bipedRightLeg).sync(data.rightLeg);
            ((ModelRendererBends)this.bipedLeftLeg).sync(data.leftLeg);
            ((ModelRendererBends)this.bipedRightForeArm).sync(data.rightForeArm);
            ((ModelRendererBends)this.bipedLeftForeArm).sync(data.leftForeArm);
            ((ModelRendererBends)this.bipedRightForeLeg).sync(data.rightForeLeg);
            ((ModelRendererBends)this.bipedLeftForeLeg).sync(data.leftForeLeg);
            this.renderOffset.set(data.renderOffset);
            this.renderRotation.set(data.renderRotation);
            this.centerRotation.set(data.centerRotation);
            this.centerQuat.set(data.centerQuat);
            this.centerQuatTarget.set(data.centerQuatTarget);
            this.centerQuatActive = data.centerQuatActive;
            this.renderItemRotation.set(data.renderItemRotation);
            this.swordTrail = data.swordTrail;
            if (Data_Player.get(argEntity.getEntityId()).canBeUpdated()) {
               this.renderOffset.setSmooth(new Vector3f(0.0F, -1.0F, 0.0F), 0.5F);
               this.renderRotation.setSmooth(new Vector3f(0.0F, 0.0F, 0.0F), 0.5F);
               boolean flying = argEntity instanceof EntityPlayer && ((EntityPlayer)argEntity).capabilities.isFlying;
               this.centerQuatActive = false;
               if (!flying) {
                  this.centerRotation.setSmooth(new Vector3f(0.0F, 0.0F, 0.0F), 0.5F);
                  this.centerQuat.orientZero();
                  this.centerQuatTarget.orientZero();
                  data.flightBodyLookYaw = 0.0F;
               }
               this.renderItemRotation.setSmooth(new Vector3f(0.0F, 0.0F, 0.0F), 0.5F);
               // Decay pre_rotation so swim/attack/axe leftovers don't leave arms stuck out
               ((ModelRendererBends)this.bipedHead).pre_rotation.setSmoothZero(0.5F);
               ((ModelRendererBends)this.bipedBody).pre_rotation.setSmoothZero(0.5F);
               ((ModelRendererBends)this.bipedRightArm).pre_rotation.setSmoothZero(0.5F);
               ((ModelRendererBends)this.bipedLeftArm).pre_rotation.setSmoothZero(0.5F);
               ((ModelRendererBends)this.bipedRightLeg).pre_rotation.setSmoothZero(0.5F);
               ((ModelRendererBends)this.bipedLeftLeg).pre_rotation.setSmoothZero(0.5F);
               ((ModelRendererBends)this.bipedRightForeArm).pre_rotation.setSmoothZero(0.5F);
               ((ModelRendererBends)this.bipedLeftForeArm).pre_rotation.setSmoothZero(0.5F);
               ((ModelRendererBends)this.bipedRightForeLeg).pre_rotation.setSmoothZero(0.5F);
               ((ModelRendererBends)this.bipedLeftForeLeg).pre_rotation.setSmoothZero(0.5F);
               ((ModelRendererBends)this.bipedHead).resetScale();
               ((ModelRendererBends)this.bipedHeadwear).resetScale();
               ((ModelRendererBends)this.bipedBody).resetScale();
               ((ModelRendererBends)this.bipedRightArm).resetScale();
               ((ModelRendererBends)this.bipedLeftArm).resetScale();
               ((ModelRendererBends)this.bipedRightLeg).resetScale();
               ((ModelRendererBends)this.bipedLeftLeg).resetScale();
               ((ModelRendererBends)this.bipedRightForeArm).resetScale();
               ((ModelRendererBends)this.bipedLeftForeArm).resetScale();
               ((ModelRendererBends)this.bipedRightForeLeg).resetScale();
               ((ModelRendererBends)this.bipedLeftForeLeg).resetScale();
               BendsVar.tempData = Data_Player.get(argEntity.getEntityId());
               if (argEntity.isRiding()) {
                  AnimatedEntity.getByEntity(argEntity).get("riding").animate((EntityLivingBase)argEntity, this, Data_Player.get(argEntity.getEntityId()));
                  BendsPack.animate(this, "player", "riding");
               } else if (Data_Player.get(argEntity.getEntityId()).isClimbing()) {
                  AnimatedEntity.getByEntity(argEntity).get("ladder_climb").animate((EntityLivingBase)argEntity, this, Data_Player.get(argEntity.getEntityId()));
                  BendsPack.animate(this, "player", "ladder_climb");
               } else if (argEntity.isInWater()) {
                  AnimatedEntity.getByEntity(argEntity).get("swimming").animate((EntityLivingBase)argEntity, this, Data_Player.get(argEntity.getEntityId()));
                  BendsPack.animate(this, "player", "swimming");
               } else if (!Data_Player.get(argEntity.getEntityId()).isOnGround() || Data_Player.get(argEntity.getEntityId()).ticksAfterTouchdown < 2.0F) {
                  Data_Player airData = Data_Player.get(argEntity.getEntityId());
                  if (argEntity instanceof EntityPlayer && ((EntityPlayer)argEntity).capabilities.isFlying) {
                     AnimatedEntity.getByEntity(argEntity).get("flying").animate((EntityLivingBase)argEntity, this, airData);
                     BendsPack.animate(this, "player", "flying");
                  } else if (airData.ticksFalling > Animation_Falling.TICKS_BEFORE_FALLING) {
                     AnimatedEntity.getByEntity(argEntity).get("falling").animate((EntityLivingBase)argEntity, this, airData);
                     BendsPack.animate(this, "player", "falling");
                  } else if (argEntity.isSprinting()) {
                     AnimatedEntity.getByEntity(argEntity).get("sprint_jump").animate((EntityLivingBase)argEntity, this, airData);
                     BendsPack.animate(this, "player", "sprint_jump");
                  } else {
                     AnimatedEntity.getByEntity(argEntity).get("jump").animate((EntityLivingBase)argEntity, this, airData);
                     BendsPack.animate(this, "player", "jump");
                  }
               } else {
                  if (Data_Player.get(argEntity.getEntityId()).motion.x == 0.0F && Data_Player.get(argEntity.getEntityId()).motion.z == 0.0F) {
                     AnimatedEntity.getByEntity(argEntity).get("stand").animate((EntityLivingBase)argEntity, this, Data_Player.get(argEntity.getEntityId()));
                     BendsPack.animate(this, "player", "stand");
                  } else if (argEntity.isSprinting()) {
                     AnimatedEntity.getByEntity(argEntity).get("sprint").animate((EntityLivingBase)argEntity, this, Data_Player.get(argEntity.getEntityId()));
                     BendsPack.animate(this, "player", "sprint");
                  } else {
                     AnimatedEntity.getByEntity(argEntity).get("walk").animate((EntityLivingBase)argEntity, this, Data_Player.get(argEntity.getEntityId()));
                     BendsPack.animate(this, "player", "walk");
                  }

                  if (argEntity.isSneaking()) {
                     AnimatedEntity.getByEntity(argEntity).get("sneak").animate((EntityLivingBase)argEntity, this, Data_Player.get(argEntity.getEntityId()));
                     BendsPack.animate(this, "player", "sneak");
                  }
               }

               if (this.aimedBow) {
                  AnimatedEntity.getByEntity(argEntity).get("bow").animate((EntityLivingBase)argEntity, this, Data_Player.get(argEntity.getEntityId()));
                  BendsPack.animate(this, "player", "bow");
               } else if ((((EntityPlayer)argEntity).getCurrentEquippedItem() == null || !(((EntityPlayer)argEntity).getCurrentEquippedItem().getItem() instanceof ItemPickaxe)) && (((EntityPlayer)argEntity).getCurrentEquippedItem() == null || Block.getBlockFromItem(((EntityPlayer)argEntity).getCurrentEquippedItem().getItem()) == Blocks.air)) {
                  if (((EntityPlayer)argEntity).getCurrentEquippedItem() != null && ((EntityPlayer)argEntity).getCurrentEquippedItem().getItem() instanceof ItemAxe) {
                     AnimatedEntity.getByEntity(argEntity).get("axe").animate((EntityLivingBase)argEntity, this, Data_Player.get(argEntity.getEntityId()));
                     BendsPack.animate(this, "player", "axe");
                  } else {
                     AnimatedEntity.getByEntity(argEntity).get("attack").animate((EntityLivingBase)argEntity, this, Data_Player.get(argEntity.getEntityId()));
                     BendsPack.animate(this, "player", "attack");
                  }
               } else {
                  AnimatedEntity.getByEntity(argEntity).get("mining").animate((EntityLivingBase)argEntity, this, Data_Player.get(argEntity.getEntityId()));
                  BendsPack.animate(this, "player", "mining");
               }

               ((ModelRendererBends)this.bipedHead).update(data.ticksPerFrame);
               ((ModelRendererBends)this.bipedHeadwear).update(data.ticksPerFrame);
               ((ModelRendererBends)this.bipedBody).update(data.ticksPerFrame);
               ((ModelRendererBends)this.bipedLeftArm).update(data.ticksPerFrame);
               ((ModelRendererBends)this.bipedRightArm).update(data.ticksPerFrame);
               ((ModelRendererBends)this.bipedLeftLeg).update(data.ticksPerFrame);
               ((ModelRendererBends)this.bipedRightLeg).update(data.ticksPerFrame);
               ((ModelRendererBends)this.bipedLeftForeArm).update(data.ticksPerFrame);
               ((ModelRendererBends)this.bipedRightForeArm).update(data.ticksPerFrame);
               ((ModelRendererBends)this.bipedLeftForeLeg).update(data.ticksPerFrame);
               ((ModelRendererBends)this.bipedRightForeLeg).update(data.ticksPerFrame);
               this.renderOffset.update(data.ticksPerFrame);
               this.renderRotation.update(data.ticksPerFrame);
               this.centerRotation.update(data.ticksPerFrame);
               if (this.centerQuatActive) {
                  // ~1.12.2 SmoothOrientation smoothness — keeps look-driven lean from yanking F5
                  float t = MathHelper.clamp_float(data.ticksPerFrame * 0.75F, 0.0F, 1.0F);
                  this.centerQuat.slerp(this.centerQuatTarget, t);
               }

               this.renderItemRotation.update(data.ticksPerFrame);
               this.swordTrail.update(data.ticksPerFrame);
               data.updatedThisFrame = true;
            }

            Data_Player.get(argEntity.getEntityId()).syncModelInfo(this);
         }
      }
   }

   public void postRender(float argScale) {
      this.postRender(argScale, 1.8F);
   }

   /**
    * Applies offsets/rotations like 1.12.2 MutatedRenderer: centerRotation pivots at mid-body,
    * then renderRotation applies at the feet/origin. Flying uses quaternion centerQuat.
    */
   public void postRender(float argScale, float entityHeight) {
      GL11.glTranslatef(this.renderOffset.vSmooth.x * argScale, this.renderOffset.vSmooth.y * argScale, this.renderOffset.vSmooth.z * argScale);
      float halfHeight = entityHeight * 0.5F;
      GL11.glTranslatef(0.0F, halfHeight, 0.0F);
      if (this.centerQuatActive) {
         this.centerQuat.applyGl();
      } else {
         GL11.glRotatef(-this.centerRotation.getX(), 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(-this.centerRotation.getY(), 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(this.centerRotation.getZ(), 0.0F, 0.0F, 1.0F);
      }

      GL11.glTranslatef(0.0F, -halfHeight, 0.0F);
      GL11.glRotatef(-this.renderRotation.getX(), 1.0F, 0.0F, 0.0F);
      GL11.glRotatef(-this.renderRotation.getY(), 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(this.renderRotation.getZ(), 0.0F, 0.0F, 1.0F);
   }

   public void postRenderArm(float argScale) {
      this.bipedRightArm.postRender(argScale);
      this.bipedRightForeArm.postRender(argScale);
      GL11.glTranslatef(2.0F * argScale, 4.0F * argScale, 2.0F * argScale);
      GL11.glRotatef(this.renderItemRotation.vSmooth.x, 1.0F, 0.0F, 0.0F);
      GL11.glRotatef(this.renderItemRotation.vSmooth.y, 0.0F, -1.0F, 0.0F);
      GL11.glRotatef(this.renderItemRotation.vSmooth.z, 0.0F, 0.0F, 1.0F);
   }

   public void updateWithEntityData(AbstractClientPlayer argPlayer) {
      Data_Player data = Data_Player.get(argPlayer.getEntityId());
      if (data != null) {
         this.renderOffset.set(data.renderOffset);
         this.renderRotation.set(data.renderRotation);
         this.centerRotation.set(data.centerRotation);
         this.centerQuat.set(data.centerQuat);
         this.centerQuatTarget.set(data.centerQuatTarget);
         this.centerQuatActive = data.centerQuatActive;
         this.renderItemRotation.set(data.renderItemRotation);
      }

   }
}
