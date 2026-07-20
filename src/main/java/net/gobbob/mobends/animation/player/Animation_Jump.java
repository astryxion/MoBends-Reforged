package net.gobbob.mobends.animation.player;

import net.gobbob.mobends.animation.Animation;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.entity.ModelBendsPlayer;
import net.gobbob.mobends.data.Data_Player;
import net.gobbob.mobends.data.EntityData;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import org.lwjgl.util.vector.Vector3f;

/**
 * Backport of Mo'Bends 1.12.2 JumpAnimationBit (Kotlin).
 */
public class Animation_Jump extends Animation {
   public String getName() {
      return "jump";
   }

   public void animate(EntityLivingBase argEntity, ModelBase argModel, EntityData argData) {
      ModelBendsPlayer model = (ModelBendsPlayer)argModel;
      Data_Player data = (Data_Player)argData;

      // Restart takeoff pose when rising again after falling (double-jump / bounce).
      if (data.motion_prev.y < 0.0F && data.motion.y > 0.0F) {
         data.jumpPoseInitialized = false;
      }

      if (!data.jumpPoseInitialized) {
         this.applyTakeoffPose(model);
         data.jumpPoseInitialized = true;
      }

      model.renderRotation.setSmoothX(0.0F, 0.3F);
      model.renderRotation.setSmoothY(0.0F, 0.3F);
      model.renderRotation.setSmoothZ(0.0F, 0.3F);
      model.centerRotation.setSmoothX(0.0F, 0.7F);
      model.centerRotation.setY(0.0F);
      model.centerRotation.setZ(0.0F);
      model.renderItemRotation.setSmoothX(0.0F, 0.3F);
      model.renderItemRotation.setSmoothY(0.0F, 0.3F);
      model.renderItemRotation.setSmoothZ(0.0F, 0.3F);
      model.renderOffset.setSmooth(new Vector3f(0.0F, -1.0F, 0.0F), 0.3F);

      float bodyRotationX = Math.max(1.0F - data.ticksAfterLiftoff * 0.1F, 0.0F);
      ((ModelRendererBends)model.bipedBody).rotation.setSmoothX(bodyRotationX, 0.2F);
      ((ModelRendererBends)model.bipedBody).rotation.setSmoothY(0.0F, 0.2F);
      ((ModelRendererBends)model.bipedBody).rotation.setSmoothZ(0.0F, 0.2F);
      ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothZ(45.0F, 0.05F);
      ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothZ(-45.0F, 0.05F);
      ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothX(0.0F, 0.3F);
      ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothX(0.0F, 0.3F);
      ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothY(0.0F, 0.3F);
      ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothY(0.0F, 0.3F);
      ((ModelRendererBends)model.bipedRightForeArm).rotation.setSmoothX(0.0F, 0.3F);
      ((ModelRendererBends)model.bipedLeftForeArm).rotation.setSmoothX(0.0F, 0.3F);
      ((ModelRendererBends)model.bipedHead).rotation.setSmoothX(model.headRotationX - bodyRotationX, 1.0F);
      ((ModelRendererBends)model.bipedHead).rotation.setSmoothY(model.headRotationY, 1.0F);

      if (argData.motion.x != 0.0F || argData.motion.z != 0.0F) {
         float limbSwing = model.armSwing * 0.6662F;
         float limbSwingAmount = 0.7F * model.armSwingAmount / (float)Math.PI * 180.0F;
         ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothX(-5.0F + MathHelper.cos(limbSwing) * limbSwingAmount, 1.0F);
         ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothX(-5.0F + MathHelper.cos(limbSwing + (float)Math.PI) * limbSwingAmount, 1.0F);
         ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothZ(0.0F, 0.3F);
         ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothZ(0.0F, 0.3F);
         ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothY(0.0F, 0.3F);
         ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothY(0.0F, 0.3F);
         float limbSwingVar = limbSwing / (float)Math.PI % 2.0F;
         ((ModelRendererBends)model.bipedLeftForeLeg).rotation.setSmoothX(limbSwingVar > 1.0F ? 45.0F : 0.0F, 0.3F);
         ((ModelRendererBends)model.bipedRightForeLeg).rotation.setSmoothX(limbSwingVar > 1.0F ? 0.0F : 45.0F, 0.3F);
         ((ModelRendererBends)model.bipedLeftForeArm).rotation.setSmoothX((MathHelper.cos(limbSwing + (float)Math.PI / 2.0F) / 2.0F + 0.5F) * -20.0F, 0.3F);
         ((ModelRendererBends)model.bipedRightForeArm).rotation.setSmoothX((MathHelper.cos(limbSwing) / 2.0F + 0.5F) * -20.0F, 0.3F);
      } else {
         ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothZ(10.0F, 0.1F);
         ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothX(-45.0F, 0.3F);
         ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothZ(-10.0F, 0.1F);
         ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothX(-17.0F, 0.3F);
         ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothY(0.0F, 0.3F);
         ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothY(0.0F, 0.3F);
         ((ModelRendererBends)model.bipedRightForeLeg).rotation.setSmoothX(70.0F, 0.3F);
         ((ModelRendererBends)model.bipedLeftForeLeg).rotation.setSmoothX(17.0F, 0.3F);
      }
   }

   private void applyTakeoffPose(ModelBendsPlayer model) {
      ((ModelRendererBends)model.bipedBody).rotation.setX(20.0F);
      ((ModelRendererBends)model.bipedRightLeg).rotation.setX(0.0F);
      ((ModelRendererBends)model.bipedLeftLeg).rotation.setX(0.0F);
      ((ModelRendererBends)model.bipedRightForeLeg).rotation.setX(0.0F);
      ((ModelRendererBends)model.bipedLeftForeLeg).rotation.setX(0.0F);
      ((ModelRendererBends)model.bipedRightArm).rotation.setZ(2.0F);
      ((ModelRendererBends)model.bipedLeftArm).rotation.setZ(-2.0F);
      ((ModelRendererBends)model.bipedRightForeArm).rotation.setX(-20.0F);
      ((ModelRendererBends)model.bipedLeftForeArm).rotation.setX(-20.0F);
   }
}
