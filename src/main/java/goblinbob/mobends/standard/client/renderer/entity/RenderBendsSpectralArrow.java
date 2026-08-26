package goblinbob.mobends.standard.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.projectile.SpectralArrowEntity;

public class RenderBendsSpectralArrow extends RenderBendsArrow<SpectralArrowEntity>
{
    public static final ResourceLocation RES_SPECTRAL_ARROW = goblinbob.mobends.core.util.ResourceLocationFactory.parse(
            "textures/entity/projectiles/spectral_arrow.png");

    public RenderBendsSpectralArrow(EntityRendererManager manager)
    {
        super(manager);
    }

    @Override
    public ResourceLocation getTextureLocation(SpectralArrowEntity entity)
    {
        return RES_SPECTRAL_ARROW;
    }
}
