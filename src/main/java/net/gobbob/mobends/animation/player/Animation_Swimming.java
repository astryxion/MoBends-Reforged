package net.gobbob.mobends.animation.player;

import net.gobbob.mobends.animation.Animation;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.entity.ModelBendsPlayer;
import net.gobbob.mobends.data.Data_Player;
import net.gobbob.mobends.data.EntityData;
import net.gobbob.mobends.util.GUtil;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import org.lwjgl.util.vector.Vector3f;

public class Animation_Swimming extends Animation {
   public String getName() {
      return "swimming";
   }

   public void animate(EntityLivingBase argEntity, ModelBase argModel, EntityData argData) {
      ModelBendsPlayer model = (ModelBendsPlayer)argModel;
      Data_Player data = (Data_Player)argData;
      float armSway = (MathHelper.cos(data.ticks * 0.1625F) + 1.0F) / 2.0F;
      float armSway2 = (-MathHelper.sin(data.ticks * 0.1625F) + 1.0F) / 2.0F;
      float legFlap = MathHelper.cos(data.ticks * 0.4625F);
      float foreArmSway = (float)((double)(data.ticks * 0.1625F) % (Math.PI * 2D)) / ((float)Math.PI * 2F);
      float foreArmStretch = armSway * 2.0F;
      --foreArmStretch;
      foreArmStretch = GUtil.min(foreArmStretch, 0.0F);
      if (data.motion.x == 0.0F && data.motion.z == 0.0F) {
         armSway = (MathHelper.cos(data.ticks * 0.0825F) + 1.0F) / 2.0F;
         armSway2 = (-MathHelper.sin(data.ticks * 0.0825F) + 1.0F) / 2.0F;
         legFlap = MathHelper.cos(data.ticks * 0.2625F);
         // Kill leftover body roll/yaw from flight/walk so the tread pose stays upright.
         model.renderRotation.setSmoothY(0.0F, 0.5F);
         model.renderRotation.setSmoothZ(0.0F, 0.5F);
         model.centerRotation.setSmooth(new Vector3f(0.0F, 0.0F, 0.0F), 0.5F);
         ((ModelRendererBends)model.bipedHead).pre_rotation.setSmoothX(0.0F, 0.3F);
         ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothX(armSway2 * 30.0F - 15.0F, 0.3F);
         ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothX(armSway2 * 30.0F - 15.0F, 0.3F);
         ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothZ(-armSway * 30.0F);
         ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothZ(armSway * 30.0F);
         ((ModelRendererBends)model.bipedLeftForeArm).rotation.setSmoothX(armSway2 * -40.0F, 0.3F);
         ((ModelRendererBends)model.bipedRightForeArm).rotation.setSmoothX(armSway2 * -40.0F, 0.3F);
         ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothX(legFlap * 40.0F, 0.3F);
         ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothX(-legFlap * 40.0F, 0.3F);
         ((ModelRendererBends)model.bipedLeftForeLeg).rotation.setSmoothX(5.0F, 0.4F);
         ((ModelRendererBends)model.bipedRightForeLeg).rotation.setSmoothX(5.0F, 0.4F);
         ((ModelRendererBends)model.bipedBody).rotation.setSmoothX(armSway * 10.0F);
         ((ModelRendererBends)model.bipedHead).rotation.setSmoothX(model.headRotationX);
         ((ModelRendererBends)model.bipedHead).rotation.setSmoothY(model.headRotationY);
      } else {
         ((ModelRendererBends)model.bipedHead).pre_rotation.setSmoothX(-70.0F - armSway * -20.0F, 0.3F);
         model.renderRotation.setSmoothX(70.0F, 0.3F);
         // Pitch only — any leftover Y/Z reads as that stupid sideways slant underwater.
         model.renderRotation.setSmoothY(0.0F, 0.3F);
         model.renderRotation.setSmoothZ(0.0F, 0.3F);
         model.centerRotation.setSmooth(new Vector3f(0.0F, 0.0F, 0.0F), 0.5F);
         model.renderOffset.setSmoothZ(10.0F, 0.3F);
         ((ModelRendererBends)model.bipedLeftArm).pre_rotation.setSmoothY(90.0F, 0.3F);
         ((ModelRendererBends)model.bipedRightArm).pre_rotation.setSmoothY(-90.0F, 0.3F);
         ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothX(armSway * -120.0F - 45.0F, 0.3F);
         ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothX(armSway * -120.0F - 45.0F, 0.3F);
         ((ModelRendererBends)model.bipedLeftArm).pre_rotation.setSmoothZ(armSway * -20.0F);
         ((ModelRendererBends)model.bipedRightArm).pre_rotation.setSmoothZ(-(armSway * -20.0F));
         ((ModelRendererBends)model.bipedLeftForeArm).rotation.setSmoothX(foreArmSway < 0.55F || (double)foreArmSway > 0.9 ? foreArmStretch * -60.0F : -60.0F, 0.3F);
         ((ModelRendererBends)model.bipedRightForeArm).rotation.setSmoothX(foreArmSway < 0.55F || (double)foreArmSway > 0.9 ? foreArmStretch * -60.0F : -60.0F, 0.3F);
         ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothX(legFlap * 40.0F, 0.3F);
         ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothX(-legFlap * 40.0F, 0.3F);
         ((ModelRendererBends)model.bipedLeftForeLeg).rotation.setSmoothX(5.0F, 0.4F);
         ((ModelRendererBends)model.bipedRightForeLeg).rotation.setSmoothX(5.0F, 0.4F);
         ((ModelRendererBends)model.bipedBody).rotation.setSmoothX(armSway * -20.0F);
         ((ModelRendererBends)model.bipedBody).rotation.setSmoothY(0.0F, 0.3F);
         ((ModelRendererBends)model.bipedBody).rotation.setSmoothZ(0.0F, 0.3F);
         ((ModelRendererBends)model.bipedHead).rotation.setSmoothX(model.headRotationX);
         ((ModelRendererBends)model.bipedHead).rotation.setSmoothY(model.headRotationY);
         model.renderItemRotation.setSmoothX(armSway * 120.0F, 0.3F);
      }

   }
}
