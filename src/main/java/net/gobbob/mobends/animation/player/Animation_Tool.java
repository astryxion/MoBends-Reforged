package net.gobbob.mobends.animation.player;

import net.gobbob.mobends.animation.Animation;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.entity.ModelBendsPlayer;
import net.gobbob.mobends.data.Data_Player;
import net.gobbob.mobends.data.EntityData;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

/**
 * Port of 1.12.2 ToolAction. One swing for pickaxe, axe, torch, blocks, food, etc.
 * Right-hand only (1.7.10 has no offhand). Does not pose legs.
 */
public class Animation_Tool extends Animation {
   public String getName() {
      return "tool";
   }

   public void animate(EntityLivingBase argEntity, ModelBase argModel, EntityData argData) {
      animateTool((EntityPlayer)argEntity, (ModelBendsPlayer)argModel, (Data_Player)argData);
   }

   static void animateTool(EntityPlayer player, ModelBendsPlayer model, Data_Player data) {
      if (!Animation_Attack.isPunching(player, model, data)) {
         return;
      }

      if (!(player.isSwingInProgress || player.swingProgress > 0.0F || model.onGround > 0.0F)) {
         return;
      }

      float progress = model.onGround > 0.0F ? model.onGround : player.swingProgress;
      float swing = MathHelper.sqrt_float(progress) * ((float)Math.PI * 2.0F);
      float bodyYaw = MathHelper.sin(swing) * 30.0F;
      float bodyPitch = player.isSneaking() ? 20.0F : 0.0F;
      // 1.12.2 body: orientY(bodyYaw) then rotateX(20) if sneaking.
      ((ModelRendererBends)model.bipedBody).pre_rotation.setSmoothZero(1.0F);
      ((ModelRendererBends)model.bipedBody).rotation.setSmoothX(bodyPitch, 0.8F);
      ((ModelRendererBends)model.bipedBody).rotation.setSmoothY(bodyYaw, 0.8F);
      ((ModelRendererBends)model.bipedBody).rotation.setSmoothZ(0.0F, 0.8F);
      ((ModelRendererBends)model.bipedHead).rotation.setSmoothX(model.headRotationX - bodyPitch, 0.8F);
      ((ModelRendererBends)model.bipedHead).rotation.setSmoothY(model.headRotationY - bodyYaw, 0.8F);
      // 1.12.2: orientInstantX(ax).localRotateZ(az) → q = qx * qz.
      // 1.7.10 native match: pre_rotation.X = ax, rotation.Z = az (not pre_Z + rot_X).
      float ax = MathHelper.sin(swing) * 50.0F - 30.0F;
      float az = MathHelper.cos(swing) * -20.0F + 10.0F;
      ModelRendererBends arm = (ModelRendererBends)model.bipedRightArm;
      arm.pre_rotation.setX(ax);
      arm.pre_rotation.setY(0.0F);
      arm.pre_rotation.setZ(0.0F);
      arm.rotation.setX(0.0F);
      arm.rotation.setY(0.0F);
      arm.rotation.setZ(az);
   }
}
