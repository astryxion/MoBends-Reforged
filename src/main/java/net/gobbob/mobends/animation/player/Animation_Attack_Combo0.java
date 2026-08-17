package net.gobbob.mobends.animation.player;

import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.entity.ModelBendsPlayer;
import net.gobbob.mobends.data.Data_Player;
import net.minecraft.entity.player.EntityPlayer;

/** 1.12.2 AttackSlashUpAnimationBit. Combo hit 1. */
public class Animation_Attack_Combo0 {
   public static void animate(EntityPlayer player, ModelBendsPlayer model, Data_Player data) {
      if (data.ticksAfterPunch < 0.5F) {
         model.swordTrail.reset();
      }

      if (data.ticksAfterPunch < 4.0F && Animation_Attack_Sword.holdingSword(player)) {
         model.swordTrail.add(model);
      }

      float attackState = data.ticksAfterPunch / 10.0F;
      float armSwing = Animation_Attack_Sword.clamp01(attackState * 3.0F);

      Animation_Attack_Sword.applyBodyAndHead(model, 20.0F - armSwing * 20.0F, -70.0F * armSwing, 0.9F);
      Animation_Attack_Sword.setMainArm(model, 110.0F * armSwing, 60.0F - armSwing * 180.0F, 0.9F);
      Animation_Attack_Sword.setOffArm(model, -20.0F, 0.3F);
      Animation_Attack_Sword.setForeArms(model, -20.0F, -60.0F, 0.3F);

      if (Animation_Attack_Sword.isStillHorizontally(data) && !player.isRiding()) {
         ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothX(-20.0F, 0.9F);
         ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothY(15.0F, 0.9F);
         ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothZ(5.0F, 0.9F);
         ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothX(-20.0F, 0.9F);
         ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothY(-15.0F, 0.9F);
         ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothZ(-5.0F, 0.9F);
         ((ModelRendererBends)model.bipedRightForeLeg).rotation.setSmoothX(25.0F, 0.9F);
         model.renderRotation.setSmoothY(0.0F, 0.3F);
         model.renderOffset.setSmoothY(-1.0F);
      }

      model.renderItemRotation.setX(180.0F);
   }
}
