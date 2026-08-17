package net.gobbob.mobends.client.model.entity;

import net.gobbob.mobends.AnimatedEntity;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.data.Data_Squid;
import net.gobbob.mobends.pack.BendsPack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelSquid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class ModelBendsSquid extends ModelSquid {
   public ModelRendererBends squidBody;
   public ModelRendererBends[][] tentacles = new ModelRendererBends[8][Data_Squid.TENTACLE_SECTIONS];

   public ModelBendsSquid() {
      this.squidBody = new ModelRendererBends(this, 0, 0);
      this.squidBody.addBox(-6.0F, -8.0F, -6.0F, 12, 16, 12, 0.0F);
      this.squidBody.setRotationPoint(0.0F, 8.0F, 0.0F);

      for(int i = 0; i < this.tentacles.length; ++i) {
         // Vanilla 1.7.10/1.12 squid: radius 5, Y=15 (1px into the body so the mouth is covered).
         // 1.12.2 MoBends used radius 4 / Y=16 with z-shifted boxes; those offsets only
         // connect under quaternion bones and leave a gap on 1.7.10 Euler parts.
         double d0 = (double)i * Math.PI * 2.0D / (double)this.tentacles.length;
         float f = (float)Math.cos(d0) * 5.0F;
         float f1 = (float)Math.sin(d0) * 5.0F;
         this.tentacles[i][0] = new ModelRendererBends(this, 48, 0);
         this.tentacles[i][0].addBox(-1.0F, 0.0F, -1.0F, 2, Data_Squid.SECTION_HEIGHT, 2, 0.0F);
         this.tentacles[i][0].setRotationPoint(f, 15.0F, f1);
         this.tentacles[i][0].rotation.setY((float)i * -360.0F / (float)this.tentacles.length + 90.0F);

         for(int j = 1; j < Data_Squid.TENTACLE_SECTIONS; ++j) {
            this.tentacles[i][j] = new ModelRendererBends(this, 48, 0);
            this.tentacles[i][j].addBox(-1.0F, 0.0F, -1.0F, 2, Data_Squid.SECTION_HEIGHT, 2, 0.0F);
            this.tentacles[i][j].setRotationPoint(0.0F, (float)Data_Squid.SECTION_HEIGHT, 0.0F);
            this.tentacles[i][j - 1].addChild(this.tentacles[i][j]);
         }
      }

   }

   public void render(Entity argEntity, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_) {
      this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, argEntity);
      this.squidBody.render(p_78088_7_);

      for(int i = 0; i < this.tentacles.length; ++i) {
         this.tentacles[i][0].render(p_78088_7_);
      }

   }

   public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity argEntity) {
      if (Minecraft.getMinecraft().theWorld != null) {
         if (!Minecraft.getMinecraft().theWorld.isRemote || !Minecraft.getMinecraft().isGamePaused()) {
            Data_Squid data = Data_Squid.get(argEntity.getEntityId());
            this.squidBody.sync(data.squidBody);

            for(int i = 0; i < 8; ++i) {
               for(int j = 0; j < Data_Squid.TENTACLE_SECTIONS; ++j) {
                  if (data.squidTentacles != null && data.squidTentacles[i][j] != null) {
                     this.tentacles[i][j].sync(data.squidTentacles[i][j]);
                  }
               }
            }

            if (data.canBeUpdated()) {
               this.squidBody.resetScale();

               for(int i = 0; i < 8; ++i) {
                  for(int j = 0; j < Data_Squid.TENTACLE_SECTIONS; ++j) {
                     this.tentacles[i][j].resetScale();
                  }
               }

               // 1.12.2 updates parts on the render tick *before* SquidController.perform().
               // Interpolating after setting the new target made the wave feel snappier.
               float dt = data.ticksPerFrame;
               if (dt < 0.0F) {
                  dt = 0.0F;
               }

               if (dt > 1.0F) {
                  dt = 1.0F;
               }

               this.squidBody.update(dt);

               for(int i = 0; i < 8; ++i) {
                  for(int j = 0; j < Data_Squid.TENTACLE_SECTIONS; ++j) {
                     this.tentacles[i][j].update(dt);
                  }
               }

               AnimatedEntity animated = AnimatedEntity.getByEntity(argEntity);
               if (animated != null && animated.get("swim") != null) {
                  animated.get("swim").animate((EntityLivingBase)argEntity, this, data);
               }

               BendsPack.animate(this, "squid", "swim");
               data.updatedThisFrame = true;
            }

            data.syncModelInfo(this);
         }
      }
   }
}
