package goblinbob.mobends.standard.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import goblinbob.mobends.standard.main.ModConfig;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.TridentRenderer;
import net.minecraft.entity.projectile.TridentEntity;

public class RenderBendsTrident extends TridentRenderer
{
    public RenderBendsTrident(EntityRendererManager manager)
    {
        super(manager);
    }

    @Override
    public void render(TridentEntity entity, float entityYaw, float partialTicks, MatrixStack poseStack,
                       IRenderTypeBuffer buffer, int packedLight)
    {
        if (ModConfig.tridentTrail)
        {
            ArrowTrailManager.renderTrail(entity, poseStack, partialTicks);
        }

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
