package net.gobbob.mobends.animation.player;

import net.gobbob.mobends.animation.Animation;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.entity.ModelBendsPlayer;
import net.gobbob.mobends.data.EntityData;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class Animation_Sneak extends Animation {
   public String getName() {
      return "sneak";
   }

   public void animate(EntityLivingBase argEntity, ModelBase argModel, EntityData argData) {
      ModelBendsPlayer model = (ModelBendsPlayer)argModel;
      float var = (float)((double)(model.armSwing * 0.6662F) / Math.PI) % 2.0F;
      ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothX(-5.0F + 1.1F * (float)((double)(MathHelper.cos(model.armSwing * 0.6662F) * 1.4F * model.armSwingAmount) / Math.PI * (double)180.0F), 1.0F);
      ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothX(-5.0F + 1.1F * (float)((double)(MathHelper.cos(model.armSwing * 0.6662F + (float)Math.PI) * 1.4F * model.armSwingAmount) / Math.PI * (double)180.0F), 1.0F);
      ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothZ(10.0F);
      ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothZ(-10.0F);
      ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothX(-20.0F + 20.0F * MathHelper.cos(model.armSwing * 0.6662F + (float)Math.PI));
      ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothX(-20.0F + 20.0F * MathHelper.cos(model.armSwing * 0.6662F));
      ((ModelRendererBends)model.bipedLeftForeLeg).rotation.setSmoothX((float)(var > 1.0F ? 45 : 10), 0.3F);
      ((ModelRendererBends)model.bipedRightForeLeg).rotation.setSmoothX((float)(var > 1.0F ? 10 : 45), 0.3F);
      ((ModelRendererBends)model.bipedLeftForeArm).rotation.setSmoothX((float)(var > 1.0F ? -10 : -45), 0.01F);
      ((ModelRendererBends)model.bipedRightForeArm).rotation.setSmoothX((float)(var > 1.0F ? -45 : -10), 0.01F);
      float var2 = 25.0F + (float)Math.cos((double)(model.armSwing * 0.6662F * 2.0F)) * 5.0F;
      ((ModelRendererBends)model.bipedBody).rotation.setSmoothX(var2);
      ((ModelRendererBends)model.bipedHead).rotation.setSmoothX(model.headRotationX - var2, 0.3F);
   }
}
