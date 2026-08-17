package net.gobbob.mobends.client.model.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.gobbob.mobends.AnimatedEntity;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.ModelRendererBends_SeperatedChild;
import net.gobbob.mobends.data.Data_Skeleton;
import net.gobbob.mobends.pack.BendsPack;
import net.gobbob.mobends.pack.BendsVar;
import net.gobbob.mobends.util.SmoothVector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

@SideOnly(Side.CLIENT)
public class ModelBendsSkeleton extends ModelBiped {
   public ModelRendererBends bipedRightForeArm;
   public ModelRendererBends bipedLeftForeArm;
   public ModelRendererBends bipedRightForeLeg;
   public ModelRendererBends bipedLeftForeLeg;
   public SmoothVector3f renderOffset;
   public SmoothVector3f renderRotation;
   public float headRotationX;
   public float headRotationY;
   public float armSwing;
   public float armSwingAmount;
   public float bowDraw;

   public ModelBendsSkeleton() {
      this(0.0F, false);
   }

   /**
    * @param inflate box inflation (armor uses 1.0 / 0.5)
    * @param armor   true = thick biped armor boxes (vanilla ModelSkeleton's {@code p_i1168_2_})
    */
   public ModelBendsSkeleton(float inflate, boolean armor) {
      super(inflate, 0.0F, 64, 32);
      this.renderOffset = new SmoothVector3f();
      this.renderRotation = new SmoothVector3f();
      this.bipedCloak = new ModelRendererBends(this, 0, 0);
      this.bipedCloak.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, inflate);
      this.bipedEars = new ModelRendererBends(this, 24, 0);
      this.bipedEars.addBox(-3.0F, -6.0F, -1.0F, 6, 6, 1, inflate);
      this.bipedHead = new ModelRendererBends(this, 0, 0);
      this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, inflate);
      this.bipedHead.setRotationPoint(0.0F, -12.0F, 0.0F);
      this.bipedHeadwear = new ModelRendererBends(this, 32, 0);
      this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, inflate + 0.5F);
      this.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.bipedBody = (new ModelRendererBends(this, 16, 16)).setShowChildIfHidden(true);
      this.bipedBody.addBox(-4.0F, -12.0F, -2.0F, 8, 12, 4, inflate);
      this.bipedBody.setRotationPoint(0.0F, 12.0F, 0.0F);

      if (armor) {
         this.buildThickLimbs(inflate);
      } else {
         this.buildBoneLimbs(inflate);
      }

      this.bipedBody.addChild(this.bipedHead);
      this.bipedBody.addChild(this.bipedRightArm);
      this.bipedBody.addChild(this.bipedLeftArm);
      this.bipedHead.addChild(this.bipedHeadwear);
      this.bipedRightArm.addChild(this.bipedRightForeArm);
      this.bipedLeftArm.addChild(this.bipedLeftForeArm);
      this.bipedRightLeg.addChild(this.bipedRightForeLeg);
      this.bipedLeftLeg.addChild(this.bipedLeftForeLeg);
      ((ModelRendererBends_SeperatedChild)this.bipedRightArm).setSeperatedPart(this.bipedRightForeArm);
      ((ModelRendererBends_SeperatedChild)this.bipedLeftArm).setSeperatedPart(this.bipedLeftForeArm);
   }

   private void buildBoneLimbs(float inflate) {
      this.bipedRightArm = (new ModelRendererBends_SeperatedChild(this, 40, 16)).setMother((ModelRendererBends)this.bipedBody);
      this.bipedRightArm.addBox(-1.0F, -2.0F, -1.0F, 2, 6, 2, inflate);
      this.bipedRightArm.setRotationPoint(-5.0F, -10.0F, 0.0F);
      this.bipedLeftArm = (new ModelRendererBends_SeperatedChild(this, 40, 16)).setMother((ModelRendererBends)this.bipedBody);
      this.bipedLeftArm.mirror = true;
      this.bipedLeftArm.addBox(-1.0F, -2.0F, -1.0F, 2, 6, 2, inflate);
      this.bipedLeftArm.setRotationPoint(5.0F, -10.0F, 0.0F);
      this.bipedRightLeg = new ModelRendererBends(this, 0, 16);
      this.bipedRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, inflate);
      this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
      this.bipedLeftLeg = new ModelRendererBends(this, 0, 16);
      this.bipedLeftLeg.mirror = true;
      this.bipedLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, inflate);
      this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
      this.bipedRightForeArm = new ModelRendererBends(this, 40, 22);
      this.bipedRightForeArm.addBox(-1.0F, 0.0F, -2.0F, 2, 6, 2, inflate);
      this.bipedRightForeArm.setRotationPoint(0.0F, 4.0F, 1.0F);
      this.bipedRightForeArm.getBox().offsetTextureQuad(this.bipedRightForeArm, 3, 0.0F, -6.0F);
      this.bipedLeftForeArm = new ModelRendererBends(this, 40, 22);
      this.bipedLeftForeArm.mirror = true;
      this.bipedLeftForeArm.addBox(-1.0F, 0.0F, -2.0F, 2, 6, 2, inflate);
      this.bipedLeftForeArm.setRotationPoint(0.0F, 4.0F, 1.0F);
      this.bipedLeftForeArm.getBox().offsetTextureQuad(this.bipedLeftForeArm, 3, 0.0F, -6.0F);
      this.bipedRightForeLeg = new ModelRendererBends(this, 0, 22);
      this.bipedRightForeLeg.addBox(-1.0F, 0.0F, 0.0F, 2, 6, 2, inflate);
      // 1.12.2 SkeletonData.initModelPose: 2-wide thigh front is z=-1, not the 4-wide z=-2.
      this.bipedRightForeLeg.setRotationPoint(0.0F, 6.0F, -1.0F);
      this.bipedRightForeLeg.getBox().offsetTextureQuad(this.bipedRightForeLeg, 3, 0.0F, -6.0F);
      this.bipedLeftForeLeg = new ModelRendererBends(this, 0, 22);
      this.bipedLeftForeLeg.mirror = true;
      this.bipedLeftForeLeg.addBox(-1.0F, 0.0F, 0.0F, 2, 6, 2, inflate);
      this.bipedLeftForeLeg.setRotationPoint(0.0F, 6.0F, -1.0F);
      this.bipedLeftForeLeg.getBox().offsetTextureQuad(this.bipedLeftForeLeg, 3, 0.0F, -6.0F);
      ((ModelRendererBends)this.bipedRightArm).offsetBox_Add(-0.01F, 0.0F, -0.01F).resizeBox(2.02F, 6.0F, 2.02F).updateVertices();
      ((ModelRendererBends)this.bipedLeftArm).offsetBox_Add(-0.01F, 0.0F, -0.01F).resizeBox(2.02F, 6.0F, 2.02F).updateVertices();
      ((ModelRendererBends)this.bipedRightLeg).offsetBox_Add(-0.01F, 0.0F, -0.01F).resizeBox(2.02F, 6.0F, 2.02F).updateVertices();
      ((ModelRendererBends)this.bipedLeftLeg).offsetBox_Add(-0.01F, 0.0F, -0.01F).resizeBox(2.02F, 6.0F, 2.02F).updateVertices();
   }

   private void buildThickLimbs(float inflate) {
      this.bipedRightArm = (new ModelRendererBends_SeperatedChild(this, 40, 16)).setMother((ModelRendererBends)this.bipedBody);
      this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 6, 4, inflate);
      this.bipedRightArm.setRotationPoint(-5.0F, -10.0F, 0.0F);
      this.bipedLeftArm = (new ModelRendererBends_SeperatedChild(this, 40, 16)).setMother((ModelRendererBends)this.bipedBody);
      this.bipedLeftArm.mirror = true;
      this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 6, 4, inflate);
      this.bipedLeftArm.setRotationPoint(5.0F, -10.0F, 0.0F);
      this.bipedRightLeg = new ModelRendererBends(this, 0, 16);
      this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, inflate);
      this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
      this.bipedLeftLeg = new ModelRendererBends(this, 0, 16);
      this.bipedLeftLeg.mirror = true;
      this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, inflate);
      this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
      this.bipedRightForeArm = new ModelRendererBends(this, 40, 22);
      this.bipedRightForeArm.addBox(0.0F, 0.0F, -4.0F, 4, 6, 4, inflate);
      this.bipedRightForeArm.setRotationPoint(-3.0F, 4.0F, 2.0F);
      this.bipedRightForeArm.getBox().offsetTextureQuad(this.bipedRightForeArm, 3, 0.0F, -6.0F);
      this.bipedLeftForeArm = new ModelRendererBends(this, 40, 22);
      this.bipedLeftForeArm.mirror = true;
      this.bipedLeftForeArm.addBox(0.0F, 0.0F, -4.0F, 4, 6, 4, inflate);
      this.bipedLeftForeArm.setRotationPoint(-1.0F, 4.0F, 2.0F);
      this.bipedLeftForeArm.getBox().offsetTextureQuad(this.bipedLeftForeArm, 3, 0.0F, -6.0F);
      this.bipedRightForeLeg = new ModelRendererBends(this, 0, 22);
      this.bipedRightForeLeg.addBox(-2.0F, 0.0F, 0.0F, 4, 6, 4, inflate);
      this.bipedRightForeLeg.setRotationPoint(0.0F, 6.0F, -2.0F);
      this.bipedRightForeLeg.getBox().offsetTextureQuad(this.bipedRightForeLeg, 3, 0.0F, -6.0F);
      this.bipedLeftForeLeg = new ModelRendererBends(this, 0, 22);
      this.bipedLeftForeLeg.mirror = true;
      this.bipedLeftForeLeg.addBox(-2.0F, 0.0F, 0.0F, 4, 6, 4, inflate);
      this.bipedLeftForeLeg.setRotationPoint(0.0F, 6.0F, -2.0F);
      this.bipedLeftForeLeg.getBox().offsetTextureQuad(this.bipedLeftForeLeg, 3, 0.0F, -6.0F);
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
         GL11.glScalef(1.0F / f6, 1.0F / f6, 1.0F / f6);
         GL11.glTranslatef(0.0F, 24.0F * p_78088_7_, 0.0F);
         this.bipedBody.render(p_78088_7_);
         this.bipedRightLeg.render(p_78088_7_);
         this.bipedLeftLeg.render(p_78088_7_);
         GL11.glPopMatrix();
      } else {
         this.bipedBody.render(p_78088_7_);
         this.bipedRightLeg.render(p_78088_7_);
         this.bipedLeftLeg.render(p_78088_7_);
      }
   }

   public void setRotationAngles(float argSwingTime, float argSwingAmount, float argArmSway, float argHeadY, float argHeadX, float argNr6, Entity argEntity) {
      if (Minecraft.getMinecraft().theWorld == null) {
         return;
      }

      Data_Skeleton data = Data_Skeleton.get(argEntity.getEntityId());
      this.armSwing = argSwingTime;
      this.armSwingAmount = argSwingAmount;
      this.headRotationX = argHeadX;
      this.headRotationY = argHeadY;
      this.bowDraw = data.bowDraw;
      ((ModelRendererBends)this.bipedHead).sync(data.head);
      ((ModelRendererBends)this.bipedHeadwear).sync(data.headwear);
      ((ModelRendererBends)this.bipedBody).sync(data.body);
      ((ModelRendererBends)this.bipedRightArm).sync(data.rightArm);
      ((ModelRendererBends)this.bipedLeftArm).sync(data.leftArm);
      ((ModelRendererBends)this.bipedRightLeg).sync(data.rightLeg);
      ((ModelRendererBends)this.bipedLeftLeg).sync(data.leftLeg);
      this.bipedRightForeArm.sync(data.rightForeArm);
      this.bipedLeftForeArm.sync(data.leftForeArm);
      this.bipedRightForeLeg.sync(data.rightForeLeg);
      this.bipedLeftForeLeg.sync(data.leftForeLeg);
      this.renderOffset.set(data.renderOffset);
      this.renderRotation.set(data.renderRotation);

      if (data.canBeUpdated()) {
         this.renderOffset.setSmooth(new Vector3f(0.0F, 0.0F, 0.0F), 0.5F);
         this.renderRotation.setSmooth(new Vector3f(0.0F, 0.0F, 0.0F), 0.5F);
         ((ModelRendererBends)this.bipedHead).pre_rotation.setSmoothZero(0.5F);
         ((ModelRendererBends)this.bipedBody).pre_rotation.setSmoothZero(0.5F);
         ((ModelRendererBends)this.bipedRightArm).pre_rotation.setSmoothZero(0.5F);
         ((ModelRendererBends)this.bipedLeftArm).pre_rotation.setSmoothZero(0.5F);
         ((ModelRendererBends)this.bipedRightLeg).pre_rotation.setSmoothZero(0.5F);
         ((ModelRendererBends)this.bipedLeftLeg).pre_rotation.setSmoothZero(0.5F);
         this.bipedRightForeArm.pre_rotation.setSmoothZero(0.5F);
         this.bipedLeftForeArm.pre_rotation.setSmoothZero(0.5F);
         this.bipedRightForeLeg.pre_rotation.setSmoothZero(0.5F);
         this.bipedLeftForeLeg.pre_rotation.setSmoothZero(0.5F);
         ((ModelRendererBends)this.bipedHead).resetScale();
         ((ModelRendererBends)this.bipedHeadwear).resetScale();
         ((ModelRendererBends)this.bipedBody).resetScale();
         ((ModelRendererBends)this.bipedRightArm).resetScale();
         ((ModelRendererBends)this.bipedLeftArm).resetScale();
         ((ModelRendererBends)this.bipedRightLeg).resetScale();
         ((ModelRendererBends)this.bipedLeftLeg).resetScale();
         this.bipedRightForeArm.resetScale();
         this.bipedLeftForeArm.resetScale();
         this.bipedRightForeLeg.resetScale();
         this.bipedLeftForeLeg.resetScale();

         BendsVar.tempData = data;
         AnimatedEntity animated = AnimatedEntity.getByEntity(argEntity);
         String packAnim;
         if (!data.isOnGround() || data.ticksAfterTouchdown < 1.0F) {
            animated.get("jump").animate((EntityLivingBase)argEntity, this, data);
            packAnim = "jump";
         } else if (data.motion.x == 0.0F && data.motion.z == 0.0F) {
            animated.get("stand").animate((EntityLivingBase)argEntity, this, data);
            packAnim = "stand";
         } else {
            animated.get("walk").animate((EntityLivingBase)argEntity, this, data);
            packAnim = "walk";
         }

         if (argEntity instanceof EntitySkeleton) {
            EntitySkeleton skeleton = (EntitySkeleton)argEntity;
            ItemStack held = skeleton.getHeldItem();
            if (held != null && held.getItem() instanceof ItemBow && Data_Skeleton.isAimingBow(skeleton)) {
               data.bowDraw += data.ticksPerFrame;
               if (data.bowDraw > 20.0F) {
                  data.bowDraw = 20.0F;
               }
            } else {
               data.bowDraw -= data.ticksPerFrame * 2.0F;
               if (data.bowDraw < 0.0F) {
                  data.bowDraw = 0.0F;
               }
            }

            this.bowDraw = data.bowDraw;
            this.aimedBow = data.bowDraw > 0.5F;
            if (this.aimedBow) {
               animated.get("bow").animate(skeleton, this, data);
            } else if (skeleton.isSwingInProgress || skeleton.swingProgress > 0.0F || this.onGround > 0.0F) {
               animated.get("tool").animate(skeleton, this, data);
            }
         }

         BendsPack.animate(this, animated != null ? animated.id : "skeleton", packAnim);
         ((ModelRendererBends)this.bipedHead).update(data.ticksPerFrame);
         ((ModelRendererBends)this.bipedHeadwear).update(data.ticksPerFrame);
         ((ModelRendererBends)this.bipedBody).update(data.ticksPerFrame);
         ((ModelRendererBends)this.bipedLeftArm).update(data.ticksPerFrame);
         ((ModelRendererBends)this.bipedRightArm).update(data.ticksPerFrame);
         ((ModelRendererBends)this.bipedLeftLeg).update(data.ticksPerFrame);
         ((ModelRendererBends)this.bipedRightLeg).update(data.ticksPerFrame);
         this.bipedLeftForeArm.update(data.ticksPerFrame);
         this.bipedRightForeArm.update(data.ticksPerFrame);
         this.bipedLeftForeLeg.update(data.ticksPerFrame);
         this.bipedRightForeLeg.update(data.ticksPerFrame);
         this.renderOffset.update(data.ticksPerFrame);
         this.renderRotation.update(data.ticksPerFrame);
         data.updatedThisFrame = true;
      }

      data.syncModelInfo(this);
   }

   public void postRender(float argScale) {
      GL11.glTranslatef(this.renderOffset.vSmooth.x * argScale, this.renderOffset.vSmooth.y * argScale, this.renderOffset.vSmooth.z * argScale);
      GL11.glRotatef(-this.renderRotation.getX(), 1.0F, 0.0F, 0.0F);
      GL11.glRotatef(-this.renderRotation.getY(), 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(this.renderRotation.getZ(), 0.0F, 0.0F, 1.0F);
   }

   public void updateWithEntityData(EntitySkeleton skeleton) {
      Data_Skeleton data = Data_Skeleton.get(skeleton.getEntityId());
      if (data != null) {
         this.renderOffset.set(data.renderOffset);
         this.renderRotation.set(data.renderRotation);
      }
   }
}
