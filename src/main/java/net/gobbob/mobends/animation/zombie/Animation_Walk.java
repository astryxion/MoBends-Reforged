package net.gobbob.mobends.animation.zombie;

import net.gobbob.mobends.animation.Animation;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.entity.ModelBendsZombie;
import net.gobbob.mobends.data.Data_Zombie;
import net.gobbob.mobends.data.EntityData;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.MathHelper;

public class Animation_Walk extends Animation {
   public String getName() {
      return "walk";
   }

   public void animate(EntityLivingBase argEntity, ModelBase argModel, EntityData argData) {
      EntityZombie zombie = (EntityZombie)argEntity;
      ModelBendsZombie model = (ModelBendsZombie)argModel;
      Data_Zombie data = (Data_Zombie)argData;
      model.renderOffset.setSmoothY(-3.0F);
      float var2 = 30.0F + MathHelper.cos(model.armSwing * 0.6662F * 2.0F) * 10.0F;
      ((ModelRendererBends)model.bipedBody).rotation.setSmoothX(var2, 0.3F);
      ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothX(0.9F * (float)((double)(MathHelper.cos(model.armSwing * 0.6662F + (float)Math.PI) * 2.0F * model.armSwingAmount * 0.5F) / Math.PI * (double)180.0F));
      ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothX(0.9F * (float)((double)(MathHelper.cos(model.armSwing * 0.6662F) * 2.0F * model.armSwingAmount * 0.5F) / Math.PI * (double)180.0F));
      ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothZ(5.0F, 0.3F);
      ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothZ(-5.0F, 0.3F);
      ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothX(-5.0F + 0.9F * (float)((double)(MathHelper.cos(model.armSwing * 0.6662F) * 1.4F * model.armSwingAmount) / Math.PI * (double)180.0F), 1.0F);
      ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothX(-5.0F + 0.9F * (float)((double)(MathHelper.cos(model.armSwing * 0.6662F + (float)Math.PI) * 1.4F * model.armSwingAmount) / Math.PI * (double)180.0F), 1.0F);
      ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothY(0.0F);
      ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothY(0.0F);
      ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothZ(10.0F, 0.2F);
      ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothZ(-10.0F, 0.2F);
      float var = (float)((double)(model.armSwing * 0.6662F) / Math.PI) % 2.0F;
      ((ModelRendererBends)model.bipedLeftForeLeg).rotation.setSmoothX((float)(var > 1.0F ? 45 : 0), 0.3F);
      ((ModelRendererBends)model.bipedRightForeLeg).rotation.setSmoothX((float)(var > 1.0F ? 0 : 45), 0.3F);
      ((ModelRendererBends)model.bipedLeftForeArm).rotation.setSmoothX((float)(Math.cos((double)(model.armSwing * 0.6662F) + (Math.PI / 2D)) + (double)1.0F) / 2.0F * -20.0F, 1.0F);
      ((ModelRendererBends)model.bipedRightForeArm).rotation.setSmoothX((float)(Math.cos((double)(model.armSwing * 0.6662F)) + (double)1.0F) / 2.0F * -20.0F, 0.3F);
      float var1 = MathHelper.cos(model.armSwing * 0.6662F + (float)Math.PI) / (float)Math.PI * 180.0F * 0.5F;
      ((ModelRendererBends)model.bipedBody).rotation.setSmoothY(MathHelper.cos(model.armSwing * 0.6662F + (float)Math.PI) / (float)Math.PI * 180.0F * 0.5F, 0.3F);
      if (data.currentWalkingState == 1) {
         ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothX(-120.0F);
         ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothX(-120.0F);
      }

      ((ModelRendererBends)model.bipedHead).rotation.setX(model.headRotationX - 30.0F);
      ((ModelRendererBends)model.bipedHead).rotation.setY(model.headRotationY - var1);
   }
}
