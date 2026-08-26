package goblinbob.mobends.standard.client.model.items;

import net.minecraft.client.renderer.entity.model.ElytraModel;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;

public class ModelBendsElytra<T extends LivingEntity> extends ElytraModel<T>
{
    public ModelBendsElytra()
    {
        super();
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        float f = 0.2617994F;
        float f1 = -0.2617994F;
        float f2 = 0.0F;
        float f3 = 0.0F;

        if (entity.isFallFlying())
        {
            float f4 = 1.0F;
            Vector3d deltaMovement = entity.getDeltaMovement();

            if (deltaMovement.y < 0.0D)
            {
                Vector3d vec3d = deltaMovement.normalize();
                f4 = 1.0F - (float)Math.pow(-vec3d.y, 1.5D);
            }

            f = f4 * 0.34906584F + (1.0F - f4) * f;
            f1 = f4 * -((float)Math.PI / 2F) + (1.0F - f4) * f1;
        }

        this.leftWing.x = 5.0F;
        this.leftWing.y = f2;

        if (entity instanceof AbstractClientPlayerEntity)
        {
            this.leftWing.xRot = f;
            this.leftWing.yRot = f3;
            this.leftWing.zRot = f1;
        }
        else
        {
            this.leftWing.xRot = f;
            this.leftWing.zRot = f1;
            this.leftWing.yRot = f3;
        }

        this.rightWing.x = -this.leftWing.x;
        this.rightWing.yRot = -this.leftWing.yRot;
        this.rightWing.y = this.leftWing.y;
        this.rightWing.xRot = this.leftWing.xRot;
        this.rightWing.zRot = -this.leftWing.zRot;
    }
}
