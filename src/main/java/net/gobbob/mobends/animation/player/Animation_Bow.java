package net.gobbob.mobends.animation.player;

import net.gobbob.mobends.animation.Animation;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.entity.ModelBendsPlayer;
import net.gobbob.mobends.data.Data_Player;
import net.gobbob.mobends.data.EntityData;
import net.gobbob.mobends.util.GUtil;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class Animation_Bow extends Animation {
   public String getName() {
      return "bow";
   }

   public void animate(EntityLivingBase argEntity, ModelBase argModel, EntityData argData) {
      ModelBendsPlayer model = (ModelBendsPlayer)argModel;
      Data_Player data = (Data_Player)argData;
      EntityPlayer player = (EntityPlayer)argEntity;
      float aimedBowDuration = 0.0F;
      if (player != null) {
         aimedBowDuration = (float)player.getItemInUseDuration();
      }

      if (aimedBowDuration > 15.0F) {
         aimedBowDuration = 15.0F;
      }

      if (aimedBowDuration < 10.0F) {
         ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothX(0.0F, 0.3F);
         ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothX(0.0F, 0.3F);
         ((ModelRendererBends)model.bipedBody).rotation.setSmoothX(30.0F, 0.3F);
         ((ModelRendererBends)model.bipedBody).rotation.setSmoothY(0.0F, 0.3F);
         ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothZ(0.0F);
         ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothX(-30.0F);
         ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothX(-30.0F);
         ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothY(80.0F);
         float var = aimedBowDuration / 10.0F;
         ((ModelRendererBends)model.bipedLeftForeArm).rotation.setSmoothX(var * -50.0F);
         ((ModelRendererBends)model.bipedHead).rotation.setSmoothX(model.headRotationX - 30.0F, 0.3F);
      } else {
         float var1 = 20.0F - (aimedBowDuration - 10.0F) / 5.0F * 20.0F;
         ((ModelRendererBends)model.bipedBody).rotation.setSmoothX(var1, 0.3F);
         float var = (aimedBowDuration - 10.0F) / 5.0F * -25.0F;
         ((ModelRendererBends)model.bipedBody).rotation.setSmoothY(var + model.headRotationY, 0.3F);
         ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothX(-90.0F - var1, 0.3F);
         ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothX(-30.0F);
         ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothY(80.0F);
         float var2 = aimedBowDuration / 10.0F;
         ((ModelRendererBends)model.bipedLeftForeArm).rotation.setSmoothX(var2 * -30.0F);
         ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothY(-var);
         float var5 = -90.0F + model.headRotationX;
         var5 = GUtil.min(var5, -120.0F);
         ((ModelRendererBends)model.bipedLeftArm).pre_rotation.setSmoothX(var5, 0.3F);
         ((ModelRendererBends)model.bipedRightArm).pre_rotation.setSmoothX(model.headRotationX);
         ((ModelRendererBends)model.bipedHead).rotation.setSmoothY(-var);
         ((ModelRendererBends)model.bipedHead).pre_rotation.setSmoothX(-var1, 0.3F);
         ((ModelRendererBends)model.bipedHead).rotation.setSmoothX(model.headRotationX);
      }

   }
}
