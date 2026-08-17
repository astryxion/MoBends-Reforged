package net.gobbob.mobends.animation.player;

import net.gobbob.mobends.animation.Animation;
import net.gobbob.mobends.client.model.entity.ModelBendsPlayer;
import net.gobbob.mobends.data.Data_Player;
import net.gobbob.mobends.data.EntityData;
import net.gobbob.mobends.pack.BendsPack;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;

public class Animation_Attack extends Animation {
   public String getName() {
      return "attack";
   }

   public void animate(EntityLivingBase argEntity, ModelBase argModel, EntityData argData) {
      ModelBendsPlayer model = (ModelBendsPlayer)argModel;
      Data_Player data = (Data_Player)argData;
      EntityPlayer player = (EntityPlayer)argEntity;
      if (player.getCurrentEquippedItem() == null) {
         if (data.ticksAfterPunch < 10.0F) {
            Animation_Attack_Punch.animate(player, model, data);
            BendsPack.animate(model, "player", "punch");
         } else if (data.ticksAfterPunch < 60.0F) {
            Animation_Attack_PunchStance.animate(player, model, data);
            BendsPack.animate(model, "player", "punch_stance");
         }
      } else if (player.getCurrentEquippedItem().getItem() instanceof ItemSword) {
         if (data.ticksAfterPunch < 10.0F) {
            if (data.currentAttack == 1) {
               Animation_Attack_Combo0.animate(player, model, data);
               BendsPack.animate(model, "player", "attack_0");
            } else if (data.currentAttack == 2) {
               Animation_Attack_Combo1.animate(player, model, data);
               BendsPack.animate(model, "player", "attack_1");
            } else if (data.currentAttack == 3) {
               Animation_Attack_Combo2.animate(player, model, data);
               BendsPack.animate(model, "player", "attack_2");
            } else if (data.currentAttack == 4) {
               Animation_Attack_Combo3.animate(player, model, data);
               BendsPack.animate(model, "player", "attack_3");
            } else if (data.currentAttack == 5) {
               Animation_Attack_Combo4.animate(player, model, data);
               BendsPack.animate(model, "player", "attack_4");
            }
         } else if (data.ticksAfterPunch < 60.0F && data.isOnGround() && (player.isSprinting() || Animation_Attack_Sword.isStillHorizontally(data))) {
            Animation_Attack_Stance.animate(player, model, data);
            BendsPack.animate(model, "player", player.isSprinting() ? "attack_stance_sprint" : "attack_stance");
         }
      } else {
         Animation_Tool.animateTool(player, model, data);
         BendsPack.animate(model, "player", "tool");
      }
   }

   /** True for the whole punch, not just vanilla isSwingInProgress (that flag drops mid-swing). */
   static boolean isPunching(EntityLivingBase entity, ModelBendsPlayer model, Data_Player data) {
      if (entity.isSwingInProgress || entity.swingProgress > 0.0F || model.onGround > 0.0F) {
         return true;
      }
      return data.currentAttack != 0 && data.ticksAfterPunch < 10.0F;
   }
}
