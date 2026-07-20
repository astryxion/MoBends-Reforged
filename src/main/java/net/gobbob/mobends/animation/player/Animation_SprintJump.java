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
 * Backport of Mo'Bends 1.12.2 SprintJumpAnimationBit.
 */
public class Animation_SprintJump extends Animation {
   public String getName() {
      return "sprint_jump";
   }

   public void animate(EntityLivingBase argEntity, ModelBase argModel, EntityData argData) {
      ModelBendsPlayer model = (ModelBendsPlayer)argModel;
      Data_Player data = (Data_Player)argData;

      if (data.motion_prev.y < 0.0F && data.motion.y > 0.0F) {
         data.sprintJumpRelax = 0.0F;
      }

      if (data.sprintJumpRelax < 1.0F) {
         data.sprintJumpRelax += data.ticksPerFrame * 0.1F;
         if (data.sprintJumpRelax > 1.0F) {
            data.sprintJumpRelax = 1.0F;
         }
      }

      float relaxAngle = MathHelper.sqrt_float(MathHelper.sqrt_float(data.sprintJumpRelax));
      float bodyRotationY = data.sprintJumpLeg ? 20.0F : -20.0F;
      float bodyLean = MathHelper.clamp_float(data.motion.y, -0.2F, 0.2F);
      bodyLean = bodyLean * -100.0F + 20.0F;

      model.centerRotation.setSmoothX(0.0F, 0.3F);
      model.centerRotation.setY(0.0F);
      model.centerRotation.setZ(0.0F);
      model.renderOffset.setSmooth(new Vector3f(0.0F, -1.0F, 0.0F), 0.5F);
      model.renderRotation.setSmoothX(0.0F, 0.3F);
      model.renderRotation.setSmoothY(0.0F, 0.3F);
      model.renderRotation.setSmoothZ(0.0F, 0.3F);

      ((ModelRendererBends)model.bipedBody).rotation.setSmoothX(bodyLean, 0.3F);
      ((ModelRendererBends)model.bipedBody).rotation.setSmoothY(bodyRotationY, 0.3F);
      ((ModelRendererBends)model.bipedBody).rotation.setSmoothZ(0.0F, 0.3F);
      ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothZ(5.0F, 0.8F);
      ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothZ(-5.0F, 0.8F);
      ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothZ(10.0F, 0.3F);
      ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothZ(-10.0F, 0.3F);

      if (data.sprintJumpLeg) {
         ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothX(-45.0F, 0.8F);
         ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothX(45.0F, 0.8F);
         ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothX(50.0F, 0.3F);
         ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothX(-50.0F, 0.3F);
         ((ModelRendererBends)model.bipedRightForeLeg).rotation.setSmoothX(80.0F - relaxAngle * 80.0F, 0.5F);
         ((ModelRendererBends)model.bipedLeftForeLeg).rotation.setSmoothX(relaxAngle * 70.0F, 0.5F);
      } else {
         ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothX(45.0F, 0.8F);
         ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothX(-45.0F, 0.8F);
         ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothX(-50.0F, 0.3F);
         ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothX(50.0F, 0.3F);
         ((ModelRendererBends)model.bipedLeftForeLeg).rotation.setSmoothX(80.0F - relaxAngle * 80.0F, 0.5F);
         ((ModelRendererBends)model.bipedRightForeLeg).rotation.setSmoothX(relaxAngle * 70.0F, 0.5F);
      }

      ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothY(0.0F, 0.3F);
      ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothY(0.0F, 0.3F);
      ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothY(0.0F, 0.3F);
      ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothY(0.0F, 0.3F);
      ((ModelRendererBends)model.bipedRightForeArm).rotation.setSmoothX(0.0F, 0.3F);
      ((ModelRendererBends)model.bipedLeftForeArm).rotation.setSmoothX(0.0F, 0.3F);
      ((ModelRendererBends)model.bipedHead).rotation.setSmoothX(model.headRotationX - 20.0F, 1.0F);
      ((ModelRendererBends)model.bipedHead).rotation.setSmoothY(model.headRotationY - bodyRotationY, 1.0F);
   }
}
