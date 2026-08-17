package net.gobbob.mobends.animation.player;

import net.gobbob.mobends.client.model.entity.ModelBendsPlayer;
import net.gobbob.mobends.data.Data_Player;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

/**
 * 1.12.2 AttackWhirlSlashAnimationBit. Combo hit 5.
 * Spin yaw is set instantly and wrapped so F5 does not interpolate across ±180.
 */
public class Animation_Attack_Combo4 {
   public static void animate(EntityPlayer player, ModelBendsPlayer model, Data_Player data) {
      if (data.ticksAfterPunch < 0.5F) {
         model.swordTrail.reset();
      }

      if (player.getCurrentEquippedItem() != null) {
         model.swordTrail.add(model);
      }

      float attackState = data.ticksAfterPunch / 10.0F;
      float armSwing = Animation_Attack_Sword.clamp01(attackState * 2.0F);
      float var5 = Animation_Attack_Sword.clamp01(attackState * 1.6F);
      float renderYaw = MathHelper.wrapAngleTo180_float(30.0F + 360.0F * var5);

      Animation_Attack_Sword.applyBodyAndHead(model, 20.0F - attackState * 20.0F, 20.0F * attackState, 0.9F);
      Animation_Attack_Sword.setOffArm(model, -80.0F, 0.3F);
      Animation_Attack_Sword.setMainArmInstantX(model, 10.0F + var5 * 120.0F, -20.0F + armSwing * 70.0F, 0.3F);
      Animation_Attack_Sword.setForeArms(model, -20.0F, -60.0F, 0.3F);

      if (Animation_Attack_Sword.isStillHorizontally(data)) {
         Animation_Attack_Sword.applyStillKneelLegs(model, 0.3F);
      }

      model.renderOffset.setSmoothY(-2.0F);
      model.renderItemRotation.setSmoothX(90.0F * attackState, 0.9F);
      model.renderRotation.setX(0.0F);
      model.renderRotation.setZ(0.0F);
      model.renderRotation.setY(renderYaw);
   }
}
