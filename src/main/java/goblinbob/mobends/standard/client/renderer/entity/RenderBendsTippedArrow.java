package goblinbob.mobends.standard.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.projectile.ArrowEntity;

public class RenderBendsTippedArrow extends RenderBendsArrow<ArrowEntity>
{
    public static final ResourceLocation RES_ARROW = goblinbob.mobends.core.util.ResourceLocationFactory.parse("textures/entity/projectiles/arrow.png");
    public static final ResourceLocation RES_TIPPED_ARROW = goblinbob.mobends.core.util.ResourceLocationFactory.parse(
            "textures/entity/projectiles/tipped_arrow.png");

    public RenderBendsTippedArrow(EntityRendererManager manager)
    {
        super(manager);
    }

    @Override
    public ResourceLocation getTextureLocation(ArrowEntity entity)
    {
        return entity.getColor() > 0 ? RES_TIPPED_ARROW : RES_ARROW;
    }
}
