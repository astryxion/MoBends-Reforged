package net.gobbob.mobends.animation.player;

import net.gobbob.mobends.animation.Animation;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.entity.ModelBendsPlayer;
import net.gobbob.mobends.data.Data_Player;
import net.gobbob.mobends.data.EntityData;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.util.vector.Vector3f;

public class Animation_Stand extends Animation {
   public String getName() {
      return "stand";
   }

   public void animate(EntityLivingBase argEntity, ModelBase argModel, EntityData argData) {
      ModelBendsPlayer model = (ModelBendsPlayer)argModel;
      Data_Player data = (Data_Player)argData;
      ((ModelRendererBends)model.bipedBody).rotation.setSmooth(new Vector3f(0.0F, 0.0F, 0.0F), 0.5F);
      ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothZ(2.0F, 0.2F);
      ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothZ(-2.0F, 0.2F);
      ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothX(0.0F, 0.1F);
      ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothX(0.0F, 0.1F);
      ((ModelRendererBends)model.bipedRightLeg).rotation.setSmoothY(5.0F);
      ((ModelRendererBends)model.bipedLeftLeg).rotation.setSmoothY(-5.0F);
      ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothX(0.0F, 0.1F);
      ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothX(0.0F, 0.1F);
      ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothY(0.0F, 0.3F);
      ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothY(0.0F, 0.3F);
      ((ModelRendererBends)model.bipedRightForeLeg).rotation.setSmoothX(4.0F, 0.1F);
      ((ModelRendererBends)model.bipedLeftForeLeg).rotation.setSmoothX(4.0F, 0.1F);
      ((ModelRendererBends)model.bipedRightForeArm).rotation.setSmoothX(-4.0F, 0.1F);
      ((ModelRendererBends)model.bipedLeftForeArm).rotation.setSmoothX(-4.0F, 0.1F);
      ((ModelRendererBends)model.bipedHead).rotation.setSmoothX(model.headRotationX, 0.3F);
      ((ModelRendererBends)model.bipedHead).rotation.setSmoothY(model.headRotationY, 0.3F);
      ((ModelRendererBends)model.bipedBody).rotation.setSmoothX((float)((Math.cos((double)(data.ticks / 10.0F)) - (double)1.0F) / (double)2.0F) * -3.0F);
      ((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothZ(-((float)((Math.cos((double)(data.ticks / 10.0F) + (Math.PI / 2D)) - (double)1.0F) / (double)2.0F)) * -5.0F, 0.3F);
      ((ModelRendererBends)model.bipedRightArm).rotation.setSmoothZ(-((float)((Math.cos((double)(data.ticks / 10.0F) + (Math.PI / 2D)) - (double)1.0F) / (double)2.0F)) * 5.0F, 0.3F);
   }
}
