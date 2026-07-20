package net.gobbob.mobends.animation.player;

import net.gobbob.mobends.animation.Animation;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.entity.ModelBendsPlayer;
import net.gobbob.mobends.data.Data_Player;
import net.gobbob.mobends.data.EntityData;
import net.gobbob.mobends.util.Quaternion;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.lwjgl.util.vector.Vector3f;

/**
 * Clean 1:1 port of MoBends 1.12.2 FlyingAnimationBit.
 * centerRotation uses quaternions (orientX / rotateZ / localRotateY) like 1.12.2.
 */
public class Animation_Flying extends Animation {
   private static final float PI = (float)Math.PI;
   private static final double STILL_MOTION_THRESHOLD = 0.1D;

   public String getName() {
      return "flying";
   }

   public void animate(EntityLivingBase argEntity, ModelBase argModel, EntityData argData) {
      ModelBendsPlayer model = (ModelBendsPlayer)argModel;
      Data_Player data = (Data_Player)argData;
      EntityPlayer player = (EntityPlayer)argEntity;

      double magnitude = getInterpolatedMotionMagnitude(data);
      float ticks = data.ticks;
      float forwardMomentum = MathHelper.clamp_float((float)getForwardMomentum(player, data), -1.0F, 1.0F);
      float sideMomentum = MathHelper.clamp_float((float)getSidewaysMomentum(player, data), -1.0F, 1.0F);
      double xzMomentum = getInterpolatedXZMotionMagnitude(data);
      float headPitch = model.headRotationX;
      float headYaw = model.headRotationY;
      float headYawAbs = MathHelper.abs(headYaw);
      float yMomentumAngle = (float)(Math.atan2(xzMomentum, (double)data.motion.y) * 180.0D / (double)PI);

      Quaternion center = model.centerQuatTarget;
      model.centerQuatActive = true;

      // Soften look yaw that drives whole-body lean so F5 doesn't fight the mouse
      float lookDelta = MathHelper.wrapAngleTo180_float(headYaw - data.flightBodyLookYaw);
      float lookFollow = MathHelper.clamp_float(data.ticksPerFrame * 0.4F, 0.0F, 1.0F);
      data.flightBodyLookYaw = MathHelper.wrapAngleTo180_float(data.flightBodyLookYaw + lookDelta * lookFollow);
      float bodyLookYaw = data.flightBodyLookYaw;

      model.renderRotation.setSmoothX(0.0F, 0.7F);
      model.renderRotation.setSmoothY(0.0F, 0.7F);
      model.renderRotation.setSmoothZ(0.0F, 0.7F);
      model.renderOffset.setSmooth(new Vector3f(0.0F, 0.0F, 0.0F), 0.7F);

      if (player.isSprinting() && !model.aimedBow && data.ticksAfterPunch >= 10.0F) {
         float speedFactor = MathHelper.clamp_float((float)magnitude, 0.0F, 0.2F) / 0.2F;
         // Pitch negated for 1.7.10 model space (Euler used -X); yaw/bank keep 1.12.2 signs
         center.orientX(-(yMomentumAngle * speedFactor));
         center.rotateZ(bodyLookYaw);

         float bodyRotationX = MathHelper.clamp_float(headPitch * 0.8F, -60.0F, 0.0F);
         ((ModelRendererBends)model.bipedHead).rotation.setSmoothY(headYaw - bodyLookYaw, 1.0F);
         ((ModelRendererBends)model.bipedHead).rotation.setSmoothX(headPitch - bodyRotationX - yMomentumAngle * speedFactor, 1.0F);
         ((ModelRendererBends)model.bipedBody).rotation.setSmoothX(bodyRotationX, 0.7F);
         ((ModelRendererBends)model.bipedBody).rotation.setSmoothY(0.0F, 0.7F);
         ((ModelRendererBends)model.bipedBody).rotation.setSmoothZ(0.0F, 0.7F);
         ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothX(-bodyRotationX, 0.7F);
         ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothZ(-60.0F + 55.0F * speedFactor - headYawAbs * 0.5F, 0.7F);
         ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothX(-bodyRotationX, 0.7F);
         ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothZ(60.0F - 55.0F * speedFactor + headYawAbs * 0.5F, 0.7F);
         ((ModelRendererBends)model.bipedLeftForeArm).rotation.setSmoothX(0.0F, 0.7F);
         ((ModelRendererBends)model.bipedRightForeArm).rotation.setSmoothX(0.0F, 0.7F);
         ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothZ(-5.0F, 0.7F);
         ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothZ(5.0F, 0.7F);
         ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothX(0.0F, 0.7F);
         ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothX(0.0F, 0.7F);
         ((ModelRendererBends)model.bipedLeftForeLeg).rotation.setSmoothX(0.0F, 0.7F);
         ((ModelRendererBends)model.bipedRightForeLeg).rotation.setSmoothX(0.0F, 0.7F);
      } else if (magnitude < STILL_MOTION_THRESHOLD) {
         float armSway = (MathHelper.cos(ticks * 0.0825F) + 1.0F) / 2.0F;
         float armSway2 = (-MathHelper.sin(ticks * 0.0825F) + 1.0F) / 2.0F;
         float legFlap = MathHelper.cos(ticks * 0.125F);
         float legFlap2 = MathHelper.sin(ticks * 0.125F);

         ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothX(armSway2 * 30.0F - 15.0F, 0.3F);
         ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothZ(-armSway * 30.0F, 0.3F);
         ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothX(armSway2 * 30.0F - 15.0F, 0.3F);
         ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothZ(armSway * 30.0F, 0.3F);
         ((ModelRendererBends)model.bipedLeftForeArm).rotation.setSmoothX(armSway2 * -40.0F, 0.3F);
         ((ModelRendererBends)model.bipedRightForeArm).rotation.setSmoothX(armSway2 * -40.0F, 0.3F);
         ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothZ(-5.0F + legFlap * 3.0F, 0.3F);
         ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothX(-25.0F + legFlap2 * 5.0F, 0.3F);
         ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothZ(5.0F - legFlap * 3.0F, 0.3F);
         ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothX(-6.0F + legFlap2 * 5.0F, 0.3F);
         ((ModelRendererBends)model.bipedLeftForeLeg).rotation.setSmoothX(20.0F - legFlap2 * 15.0F, 0.4F);
         ((ModelRendererBends)model.bipedRightForeLeg).rotation.setSmoothX(5.0F, 0.4F);
         ((ModelRendererBends)model.bipedBody).rotation.setSmoothX(armSway * 10.0F, 0.3F);
         ((ModelRendererBends)model.bipedBody).rotation.setSmoothY(0.0F, 0.3F);
         ((ModelRendererBends)model.bipedBody).rotation.setSmoothZ(0.0F, 0.3F);

         center.orientZero();

         ((ModelRendererBends)model.bipedHead).rotation.setSmoothX(headPitch, 1.0F);
         ((ModelRendererBends)model.bipedHead).rotation.setSmoothY(headYaw, 1.0F);
         // Ease body look to 0 while hovering so re-entering move doesn't snap
         data.flightBodyLookYaw *= Math.max(0.0F, 1.0F - data.ticksPerFrame * 0.5F);
      } else {
         // Moving: rotateX(forward*50), localRotateY(-headYaw)
         center.orientZero();
         center.rotateX(-(forwardMomentum * 50.0F));
         ((ModelRendererBends)model.bipedBody).rotation.setSmoothX(0.0F, 0.5F);
         ((ModelRendererBends)model.bipedBody).rotation.setSmoothY(0.0F, 0.5F);
         ((ModelRendererBends)model.bipedBody).rotation.setSmoothZ(0.0F, 0.5F);
         ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothX(forwardMomentum * 90.0F, 0.5F);
         ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothZ(sideMomentum * -80.0F - 20.0F, 0.5F);
         ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothX(forwardMomentum * 90.0F, 0.5F);
         ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothZ(sideMomentum * -80.0F + 20.0F, 0.5F);
         ((ModelRendererBends)model.bipedLeftForeArm).rotation.setSmoothX(0.0F, 0.5F);
         ((ModelRendererBends)model.bipedRightForeArm).rotation.setSmoothX(0.0F, 0.5F);
         ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothX(-45.0F, 0.5F);
         ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothZ(sideMomentum * -40.0F - 5.0F, 0.5F);
         ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothX(-6.0F, 0.5F);
         ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothZ(sideMomentum * -40.0F + 5.0F, 0.5F);
         ((ModelRendererBends)model.bipedLeftForeLeg).rotation.setSmoothX(30.0F, 0.5F);
         ((ModelRendererBends)model.bipedRightForeLeg).rotation.setSmoothX(10.0F, 0.5F);

         ((ModelRendererBends)model.bipedHead).rotation.setSmoothX(headPitch - forwardMomentum * 50.0F, 1.0F);
         ((ModelRendererBends)model.bipedHead).rotation.setSmoothY(headYaw - bodyLookYaw, 1.0F);

         if (!model.aimedBow) {
            center.localRotateY(-bodyLookYaw);
         }
      }
   }

   private static double getMotionMagnitude(Data_Player data) {
      return Math.sqrt((double)(data.motion.x * data.motion.x + data.motion.y * data.motion.y + data.motion.z * data.motion.z));
   }

   private static double getPrevMotionMagnitude(Data_Player data) {
      return Math.sqrt((double)(data.motion_prev.x * data.motion_prev.x + data.motion_prev.y * data.motion_prev.y + data.motion_prev.z * data.motion_prev.z));
   }

   private static double getInterpolatedMotionMagnitude(Data_Player data) {
      return (getMotionMagnitude(data) + getPrevMotionMagnitude(data)) * 0.5D;
   }

   private static double getXZMotionMagnitude(Data_Player data) {
      return Math.sqrt((double)(data.motion.x * data.motion.x + data.motion.z * data.motion.z));
   }

   private static double getPrevXZMotionMagnitude(Data_Player data) {
      return Math.sqrt((double)(data.motion_prev.x * data.motion_prev.x + data.motion_prev.z * data.motion_prev.z));
   }

   private static double getInterpolatedXZMotionMagnitude(Data_Player data) {
      return (getXZMotionMagnitude(data) + getPrevXZMotionMagnitude(data)) * 0.5D;
   }

   private static boolean isStillHorizontally(Data_Player data) {
      return getXZMotionMagnitude(data) < 0.01D;
   }

   private static double getForwardMomentum(EntityPlayer player, Data_Player data) {
      if (isStillHorizontally(data)) {
         return 0.0D;
      } else {
         Vec3 look = player.getLook(1.0F);
         double len = Math.sqrt(look.xCoord * look.xCoord + look.zCoord * look.zCoord);
         if (len < 1.0E-4D) {
            return 0.0D;
         } else {
            double lx = look.xCoord / len;
            double lz = look.zCoord / len;
            return lx * (double)data.motion.x + lz * (double)data.motion.z;
         }
      }
   }

   private static double getSidewaysMomentum(EntityPlayer player, Data_Player data) {
      if (isStillHorizontally(data)) {
         return 0.0D;
      } else {
         Vec3 look = player.getLook(1.0F);
         double rx = -look.zCoord;
         double rz = look.xCoord;
         double len = Math.sqrt(rx * rx + rz * rz);
         if (len < 1.0E-4D) {
            return 0.0D;
         } else {
            rx /= len;
            rz /= len;
            return rx * (double)data.motion.x + rz * (double)data.motion.z;
         }
      }
   }
}
