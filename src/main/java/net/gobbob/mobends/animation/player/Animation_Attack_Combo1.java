package net.gobbob.mobends.animation.player;

import net.gobbob.mobends.client.model.entity.ModelBendsPlayer;
import net.gobbob.mobends.data.Data_Player;
import net.minecraft.entity.player.EntityPlayer;

/** 1.12.2 AttackSlashDownAnimationBit. Combo hit 2. */
public class Animation_Attack_Combo1 {
   public static void animate(EntityPlayer player, ModelBendsPlayer model, Data_Player data) {
      if (data.ticksAfterPunch < 0.5F) {
         model.swordTrail.reset();
      }

      if (data.ticksAfterPunch < 4.0F && Animation_Attack_Sword.holdingSword(player)) {
         model.swordTrail.add(model);
      }

      float attackState = data.ticksAfterPunch / 10.0F;
      float armSwing = Animation_Attack_Sword.clamp01(attackState * 3.0F);

      if (Animation_Attack_Sword.isStillHorizontally(data) && !player.isRiding()) {
         Animation_Attack_Sword.applyStillKneelLegs(model, 0.3F);
         model.renderOffset.setSmoothY(-2.0F);
      }

      Animation_Attack_Sword.applyBodyAndHead(model, 20.0F - attackState * 20.0F, 30.0F + 10.0F * attackState, 0.9F);
      Animation_Attack_Sword.setMainArmInstantX(model, 60.0F, -20.0F + armSwing * 70.0F, 0.3F);
      Animation_Attack_Sword.setOffArm(model, -80.0F, 0.3F);
      Animation_Attack_Sword.setForeArms(model, -20.0F, -60.0F, 0.3F);
      model.renderItemRotation.setX(90.0F);
   }
}
