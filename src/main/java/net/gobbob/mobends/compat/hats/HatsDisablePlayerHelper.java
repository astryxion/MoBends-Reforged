package net.gobbob.mobends.compat.hats;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hats.api.RenderOnEntityHelper;
import net.gobbob.mobends.client.renderer.entity.RenderBendsPlayer;
import net.gobbob.mobends.client.renderer.entity.RenderBendsSpider;
import net.gobbob.mobends.client.renderer.entity.RenderBendsZombie;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;

@SideOnly(Side.CLIENT)
final class HatsDisablePlayerHelper extends RenderOnEntityHelper {
   public Class helperForClass() {
      return EntityPlayer.class;
   }

   public float getHatScale(EntityLivingBase ent) {
      return isMoBendsRenderer(ent) ? 0.0F : 1.0F;
   }

   static boolean isMoBendsRenderer(EntityLivingBase ent) {
      Render render = RenderManager.instance.getEntityRenderObject(ent);
      return render instanceof RenderBendsPlayer
            || render instanceof RenderBendsZombie
            || render instanceof RenderBendsSpider;
   }
}

@SideOnly(Side.CLIENT)
final class HatsDisableZombieHelper extends RenderOnEntityHelper {
   public Class helperForClass() {
      return EntityZombie.class;
   }

   public float getHatScale(EntityLivingBase ent) {
      return HatsDisablePlayerHelper.isMoBendsRenderer(ent) ? 0.0F : 1.0F;
   }
}

@SideOnly(Side.CLIENT)
final class HatsDisableSpiderHelper extends RenderOnEntityHelper {
   public Class helperForClass() {
      return EntitySpider.class;
   }

   public float getHatScale(EntityLivingBase ent) {
      return HatsDisablePlayerHelper.isMoBendsRenderer(ent) ? 0.0F : 1.0F;
   }
}
