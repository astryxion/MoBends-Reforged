package net.gobbob.mobends.animation.squid;

import net.gobbob.mobends.animation.Animation;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.entity.ModelBendsSquid;
import net.gobbob.mobends.data.Data_Squid;
import net.gobbob.mobends.data.EntityData;
import net.gobbob.mobends.event.EventHandler_DataUpdate;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.util.MathHelper;

/**
 * Port of 1.12.2 SquidController. Waves each tentacle as 9 segments.
 */
public class Animation_Squid extends Animation {
   public String getName() {
      return "swim";
   }

   public void animate(EntityLivingBase argEntity, ModelBase argModel, EntityData argData) {
      EntitySquid squid = (EntitySquid)argEntity;
      ModelBendsSquid model = (ModelBendsSquid)argModel;
      float pi = (float)Math.PI;
      float squidRotation = squid.prevSquidRotation + (squid.squidRotation - squid.prevSquidRotation) * EventHandler_DataUpdate.partialTicks + 1.1F;
      float f = squidRotation / pi;
      if (f < 0.0F) {
         f = 0.0F;
      }

      float baseTentacleAngle = 0.0F;
      if (squid.prevSquidRotation < pi) {
         baseTentacleAngle = MathHelper.sin(f * f * pi) * 60.0F;
      }

      for(int i = 0; i < model.tentacles.length; ++i) {
         float d0 = (float)i * -360.0F / (float)model.tentacles.length + 90.0F;
         // 1.12.2: orientX(base).rotateY(d0) → q = qy * qx → rotation.X then rotation.Y.
         // Spread Y is constant per tentacle — set it instantly so they don't sit bunched
         // at Y=0 while smoothness 0.1 eases in (that left a hole over the mouth).
         ModelRendererBends root = model.tentacles[i][0];
         root.pre_rotation.setSmoothZero(1.0F);
         root.rotation.chaseX(baseTentacleAngle, 0.1F);
         root.rotation.setY(d0);
         root.rotation.setZ(0.0F);
         float f2 = squidRotation / (pi * 2.0F);
         if (f2 < 0.0F) {
            f2 = 0.0F;
         }

         for(int j = 1; j < Data_Squid.TENTACLE_SECTIONS; ++j) {
            float tentacleAngle = 0.0F;
            if (squid.squidRotation < pi) {
               tentacleAngle = MathHelper.sin(f2 * pi * 2.0F + (float)j * 0.1F) * 10.0F;
            }

            ModelRendererBends section = model.tentacles[i][j];
            section.pre_rotation.setSmoothZero(1.0F);
            section.rotation.chaseX(-tentacleAngle, 0.1F);
            section.rotation.setY(0.0F);
            section.rotation.setZ(0.0F);
         }
      }

   }
}
