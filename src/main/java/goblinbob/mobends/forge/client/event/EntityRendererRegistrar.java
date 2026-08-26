package goblinbob.mobends.forge.client.event;

import goblinbob.mobends.standard.client.renderer.entity.RenderBendsSpectralArrow;
import goblinbob.mobends.standard.client.renderer.entity.RenderBendsTippedArrow;
import goblinbob.mobends.standard.client.renderer.entity.RenderBendsTrident;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
public class EntityRendererRegistrar
{
    public static void registerRenderers(FMLClientSetupEvent event)
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityType.ARROW, RenderBendsTippedArrow::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityType.SPECTRAL_ARROW, RenderBendsSpectralArrow::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityType.TRIDENT, RenderBendsTrident::new);
    }
}
