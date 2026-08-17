package net.gobbob.mobends.client.model.entity;

import net.gobbob.mobends.AnimatedEntity;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.data.Data_Spider;
import net.gobbob.mobends.pack.BendsPack;
import net.gobbob.mobends.pack.BendsVar;
import net.gobbob.mobends.util.SmoothVector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSpider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySpider;
import org.lwjgl.opengl.GL11;

public class ModelBendsSpider extends ModelSpider {
   public ModelRenderer spiderHead;
   public ModelRenderer spiderNeck;
   public ModelRenderer spiderBody;
   public ModelRenderer spiderLeg1;
   public ModelRenderer spiderLeg2;
   public ModelRenderer spiderLeg3;
   public ModelRenderer spiderLeg4;
   public ModelRenderer spiderLeg5;
   public ModelRenderer spiderLeg6;
   public ModelRenderer spiderLeg7;
   public ModelRenderer spiderLeg8;
   public ModelRendererBends spiderForeLeg1;
   public ModelRendererBends spiderForeLeg2;
   public ModelRendererBends spiderForeLeg3;
   public ModelRendererBends spiderForeLeg4;
   public ModelRendererBends spiderForeLeg5;
   public ModelRendererBends spiderForeLeg6;
   public ModelRendererBends spiderForeLeg7;
   public ModelRendererBends spiderForeLeg8;
   public SmoothVector3f renderOffset = new SmoothVector3f();
   public SmoothVector3f renderRotation = new SmoothVector3f();
   public float headRotationX;
   public float headRotationY;
   public float armSwing;
   public float armSwingAmount;

   public ModelBendsSpider() {
      float f = 0.0F;
      byte b0 = 15;
      float legLength = 12.0F;
      float foreLegLength = 12.0F;
      this.spiderHead = new ModelRendererBends(this, 32, 4);
      this.spiderHead.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 8, f);
      this.spiderHead.setRotationPoint(0.0F, (float)b0, -3.0F);
      this.spiderNeck = new ModelRendererBends(this, 0, 0);
      this.spiderNeck.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6, f);
      this.spiderNeck.setRotationPoint(0.0F, (float)b0, 0.0F);
      this.spiderBody = new ModelRendererBends(this, 0, 12);
      this.spiderBody.addBox(-5.0F, -4.0F, -6.0F, 10, 8, 12, f);
      this.spiderBody.setRotationPoint(0.0F, (float)b0, 9.0F);

      ModelRendererBends[] uppers = new ModelRendererBends[8];
      ModelRendererBends[] lowers = new ModelRendererBends[8];

      for(int i = 0; i < 8; ++i) {
         boolean odd = i % 2 == 1;
         int z = 2 - i / 2;
         uppers[i] = new ModelRendererBends(this, odd ? 18 : 26, 0);
         if (odd) {
            uppers[i].addBox(-1.0F, -1.0F, -1.0F, 8, 2, 2, f);
         } else {
            uppers[i].addBox(-7.0F, -1.0F, -1.0F, 8, 2, 2, f);
            uppers[i].offsetBox_Add(8.0F - legLength, 0.0F, 0.0F);
         }

         uppers[i].setRotationPoint(odd ? 4.0F : -4.0F, (float)b0, (float)z);
         uppers[i].resizeBox(legLength, 2.0F, 2.0F).updateVertices();
         lowers[i] = new ModelRendererBends(this, odd ? 26 : 18, 0);
         if (odd) {
            lowers[i].addBox(0.0F, 0.0F, -1.0F, 8, 2, 2, f);
         } else {
            lowers[i].addBox(-foreLegLength, 0.0F, -1.0F, 8, 2, 2, f);
         }

         lowers[i].setRotationPoint(odd ? foreLegLength : -foreLegLength, 0.0F, 0.0F);
         lowers[i].offsetBox_Add(0.0F, 0.0F, 0.005F).resizeBox(foreLegLength, 1.99F, 1.99F).updateVertices();
         uppers[i].addChild(lowers[i]);
      }

      this.spiderLeg1 = uppers[0];
      this.spiderLeg2 = uppers[1];
      this.spiderLeg3 = uppers[2];
      this.spiderLeg4 = uppers[3];
      this.spiderLeg5 = uppers[4];
      this.spiderLeg6 = uppers[5];
      this.spiderLeg7 = uppers[6];
      this.spiderLeg8 = uppers[7];
      this.spiderForeLeg1 = lowers[0];
      this.spiderForeLeg2 = lowers[1];
      this.spiderForeLeg3 = lowers[2];
      this.spiderForeLeg4 = lowers[3];
      this.spiderForeLeg5 = lowers[4];
      this.spiderForeLeg6 = lowers[5];
      this.spiderForeLeg7 = lowers[6];
      this.spiderForeLeg8 = lowers[7];
   }

   public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_) {
      this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
      GL11.glPushMatrix();
      GL11.glTranslatef(this.renderOffset.vSmooth.x * p_78088_7_, this.renderOffset.vSmooth.y * p_78088_7_, this.renderOffset.vSmooth.z * p_78088_7_);
      GL11.glRotatef(-this.renderRotation.getX(), 1.0F, 0.0F, 0.0F);
      GL11.glRotatef(-this.renderRotation.getY(), 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(this.renderRotation.getZ(), 0.0F, 0.0F, 1.0F);
      this.spiderHead.render(p_78088_7_);
      this.spiderNeck.render(p_78088_7_);
      this.spiderBody.render(p_78088_7_);
      this.spiderLeg1.render(p_78088_7_);
      this.spiderLeg2.render(p_78088_7_);
      this.spiderLeg3.render(p_78088_7_);
      this.spiderLeg4.render(p_78088_7_);
      this.spiderLeg5.render(p_78088_7_);
      this.spiderLeg6.render(p_78088_7_);
      this.spiderLeg7.render(p_78088_7_);
      this.spiderLeg8.render(p_78088_7_);
      GL11.glPopMatrix();
   }

   public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity argEntity) {
      if (Minecraft.getMinecraft().theWorld == null) {
         return;
      }

      if (Minecraft.getMinecraft().theWorld.isRemote && Minecraft.getMinecraft().isGamePaused()) {
         return;
      }

      Data_Spider data = Data_Spider.get(argEntity.getEntityId());
      this.headRotationX = p_78087_5_;
      this.headRotationY = p_78087_4_;
      this.armSwing = p_78087_1_;
      this.armSwingAmount = p_78087_2_;
      ((ModelRendererBends)this.spiderHead).sync(data.spiderHead);
      ((ModelRendererBends)this.spiderNeck).sync(data.spiderNeck);
      ((ModelRendererBends)this.spiderBody).sync(data.spiderBody);
      ((ModelRendererBends)this.spiderLeg1).sync(data.spiderLeg1);
      ((ModelRendererBends)this.spiderLeg2).sync(data.spiderLeg2);
      ((ModelRendererBends)this.spiderLeg3).sync(data.spiderLeg3);
      ((ModelRendererBends)this.spiderLeg4).sync(data.spiderLeg4);
      ((ModelRendererBends)this.spiderLeg5).sync(data.spiderLeg5);
      ((ModelRendererBends)this.spiderLeg6).sync(data.spiderLeg6);
      ((ModelRendererBends)this.spiderLeg7).sync(data.spiderLeg7);
      ((ModelRendererBends)this.spiderLeg8).sync(data.spiderLeg8);
      this.spiderForeLeg1.sync(data.spiderForeLeg1);
      this.spiderForeLeg2.sync(data.spiderForeLeg2);
      this.spiderForeLeg3.sync(data.spiderForeLeg3);
      this.spiderForeLeg4.sync(data.spiderForeLeg4);
      this.spiderForeLeg5.sync(data.spiderForeLeg5);
      this.spiderForeLeg6.sync(data.spiderForeLeg6);
      this.spiderForeLeg7.sync(data.spiderForeLeg7);
      this.spiderForeLeg8.sync(data.spiderForeLeg8);
      this.renderOffset.set(data.renderOffset);
      this.renderRotation.set(data.renderRotation);
      if (data.canBeUpdated()) {
         ((ModelRendererBends)this.spiderHead).resetScale();
         ((ModelRendererBends)this.spiderNeck).resetScale();
         ((ModelRendererBends)this.spiderBody).resetScale();
         ((ModelRendererBends)this.spiderLeg1).resetScale();
         ((ModelRendererBends)this.spiderLeg2).resetScale();
         ((ModelRendererBends)this.spiderLeg3).resetScale();
         ((ModelRendererBends)this.spiderLeg4).resetScale();
         ((ModelRendererBends)this.spiderLeg5).resetScale();
         ((ModelRendererBends)this.spiderLeg6).resetScale();
         ((ModelRendererBends)this.spiderLeg7).resetScale();
         ((ModelRendererBends)this.spiderLeg8).resetScale();
         this.spiderForeLeg1.resetScale();
         this.spiderForeLeg2.resetScale();
         this.spiderForeLeg3.resetScale();
         this.spiderForeLeg4.resetScale();
         this.spiderForeLeg5.resetScale();
         this.spiderForeLeg6.resetScale();
         this.spiderForeLeg7.resetScale();
         this.spiderForeLeg8.resetScale();
         ((ModelRendererBends)this.spiderBody).rotation.setSmoothZero(0.5F);
         ((ModelRendererBends)this.spiderNeck).rotation.setSmoothZero(0.5F);
         BendsVar.tempData = data;
         AnimatedEntity animated = AnimatedEntity.getByEntity(argEntity);
         String packAnim = "idle";
         if (argEntity instanceof EntitySpider && animated != null) {
            EntitySpider spider = (EntitySpider)argEntity;
            if (spider.getHealth() <= 0.0F) {
               packAnim = "death";
            } else if (spider.isBesideClimbableBlock()) {
               packAnim = "crawl";
            } else if (!data.isOnGround() || data.ticksAfterTouchdown < 1.0F) {
               packAnim = "jump";
            } else if (data.isStillHorizontally()) {
               packAnim = "idle";
            } else {
               packAnim = "move";
            }

            if ("jump".equals(packAnim)) {
               data.resetAfterJumped = false;
            } else if (!data.resetAfterJumped && ("idle".equals(packAnim) || "move".equals(packAnim))) {
               data.ensureLimbs();
               for(int i = 0; i < data.limbs.length; ++i) {
                  data.limbs[i].resetPosition();
               }

               data.resetAfterJumped = true;
            }

            if (("move".equals(packAnim) || "crawl".equals(packAnim)) && !packAnim.equals(data.currentAnim)) {
               data.startTransition = 0.0F;
            }

            if (animated.get(packAnim) != null) {
               animated.get(packAnim).animate((EntityLivingBase)argEntity, this, data);
            }

            data.currentAnim = packAnim;
         }

         BendsPack.animate(this, "spider", packAnim);
         ((ModelRendererBends)this.spiderHead).update(data.ticksPerFrame);
         ((ModelRendererBends)this.spiderNeck).update(data.ticksPerFrame);
         ((ModelRendererBends)this.spiderBody).update(data.ticksPerFrame);
         ((ModelRendererBends)this.spiderLeg1).update(data.ticksPerFrame);
         ((ModelRendererBends)this.spiderLeg2).update(data.ticksPerFrame);
         ((ModelRendererBends)this.spiderLeg3).update(data.ticksPerFrame);
         ((ModelRendererBends)this.spiderLeg4).update(data.ticksPerFrame);
         ((ModelRendererBends)this.spiderLeg5).update(data.ticksPerFrame);
         ((ModelRendererBends)this.spiderLeg6).update(data.ticksPerFrame);
         ((ModelRendererBends)this.spiderLeg7).update(data.ticksPerFrame);
         ((ModelRendererBends)this.spiderLeg8).update(data.ticksPerFrame);
         this.spiderForeLeg1.update(data.ticksPerFrame);
         this.spiderForeLeg2.update(data.ticksPerFrame);
         this.spiderForeLeg3.update(data.ticksPerFrame);
         this.spiderForeLeg4.update(data.ticksPerFrame);
         this.spiderForeLeg5.update(data.ticksPerFrame);
         this.spiderForeLeg6.update(data.ticksPerFrame);
         this.spiderForeLeg7.update(data.ticksPerFrame);
         this.spiderForeLeg8.update(data.ticksPerFrame);
         this.renderOffset.update(data.ticksPerFrame);
         this.renderRotation.update(data.ticksPerFrame);
         data.updatedThisFrame = true;
      }

      data.syncModelInfo(this);
   }
}
