package goblinbob.mobends.standard.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import goblinbob.mobends.standard.main.ModConfig;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.projectile.AbstractArrowEntity;

public abstract class RenderBendsArrow<T extends AbstractArrowEntity> extends ArrowRenderer<T>
{
    public RenderBendsArrow(EntityRendererManager manager)
    {
        super(manager);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, MatrixStack poseStack,
                       IRenderTypeBuffer buffer, int packedLight)
    {
        if (ModConfig.showArrowTrails)
        {
            ArrowTrailManager.renderTrail(entity, poseStack, partialTicks);
        }

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
