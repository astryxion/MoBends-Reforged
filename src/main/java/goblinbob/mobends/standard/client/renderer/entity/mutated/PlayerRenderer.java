package goblinbob.mobends.standard.client.renderer.entity.mutated;

import com.mojang.blaze3d.matrix.MatrixStack;
import goblinbob.mobends.core.data.EntityData;
import goblinbob.mobends.standard.data.PlayerData;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;

public class PlayerRenderer extends BipedRenderer<AbstractClientPlayerEntity>
{

    @Override
    protected void transformLocally(AbstractClientPlayerEntity entity, EntityData<?> data, float partialTicks, MatrixStack poseStack)
    {
        if (entity.isCrouching())
        {
            final boolean flying = data instanceof PlayerData
                    ? ((PlayerData) data).isFlying()
                    : entity.abilities.flying;

            if (flying)
            {
                poseStack.translate(0F, 4F * scale, 0F);
            }
            else
            {
                poseStack.translate(0F, 5F * scale, 0F);
            }
        }
    }

}
