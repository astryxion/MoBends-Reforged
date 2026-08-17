package net.gobbob.mobends.animation.player;

import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.entity.ModelBendsPlayer;
import net.gobbob.mobends.data.Data_Player;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

/** 1.12.2 AttackStanceAnimationBit + AttackStanceSprintAnimationBit. */
public class Animation_Attack_Stance {
   private static final float PI = (float)Math.PI;
   private static final float KNEEL_DURATION = 0.15F;

   public static void animate(EntityPlayer player, ModelBendsPlayer model, Data_Player data) {
      if (!data.isOnGround()) {
         return;
      }

      if (player.isSprinting()) {
         animateSprint(player, model, data);
      } else if (Animation_Attack_Sword.isStillHorizontally(data)) {
         animateKneel(player, model, data);
      }
   }

   private static void animateSprint(EntityPlayer player, ModelBendsPlayer model, Data_Player data) {
      if (Animation_Attack_Sword.holdingSword(player)) {
         model.swordTrail.add(model);
      }

      Animation_Attack_Sword.applyBodyAndHead(model, 0.0F, 20.0F, 0.3F);
      Animation_Attack_Sword.setMainArm(model, 60.0F, 60.0F, 0.3F);
      Animation_Attack_Sword.setOffArm(model, -30.0F, 0.3F);
      model.renderItemRotation.setSmoothX(45.0F, 0.3F);
   }

   private static void animateKneel(EntityPlayer player, ModelBendsPlayer model, Data_Player data) {
      float breath0 = (float)Math.sin((double)(data.ticks / 5.0F));
      float breath1 = (float)Math.cos((double)(data.ticks / 5.7F));
      float bodyX = 20.0F + breath0 * 2.0F;

      model.renderRotation.setSmoothY(30.0F, 0.3F);
      Animation_Attack_Sword.applyBodyAndHead(model, bodyX, 0.0F, 30.0F, 0.3F);
      Animation_Attack_Sword.applyStillKneelLegs(model, 0.3F);
      Animation_Attack_Sword.setMainArm(model, 60.0F + breath0 * 5.0F, breath1 * 5.0F, 0.3F);
      Animation_Attack_Sword.setOffArm(model, -60.0F + breath1 * 5.0F, 0.3F);
      Animation_Attack_Sword.setForeArms(model, -20.0F, -60.0F, 0.3F);
      model.renderItemRotation.setSmoothX(65.0F, 0.3F);
      model.renderOffset.setSmoothY(-2.0F);

      float touchdown = Math.min(data.ticksAfterTouchdown * KNEEL_DURATION, 1.0F);
      if (touchdown < 1.0F) {
         ((ModelRendererBends)model.bipedBody).rotation.setSmoothX(5.0F * (1.0F - touchdown) + 15.0F, 1.0F);
         model.renderOffset.setY(-MathHelper.sin(touchdown * PI) * 2.0F - 2.0F);
      }
   }
}
