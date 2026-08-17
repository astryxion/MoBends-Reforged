package net.gobbob.mobends.animation.player;

import net.gobbob.mobends.animation.Animation;
import net.gobbob.mobends.client.model.entity.ModelBendsPlayer;
import net.gobbob.mobends.data.Data_Player;
import net.gobbob.mobends.data.EntityData;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

/** Pack alias — 1.12.2 folded axe into ToolAction. */
public class Animation_Axe extends Animation {
   public String getName() {
      return "axe";
   }

   public void animate(EntityLivingBase argEntity, ModelBase argModel, EntityData argData) {
      Animation_Tool.animateTool((EntityPlayer)argEntity, (ModelBendsPlayer)argModel, (Data_Player)argData);
   }
}
