package goblinbob.mobends.mixin;

import goblinbob.mobends.core.bender.EntityBender;
import goblinbob.mobends.core.bender.EntityBenderRegistry;
import goblinbob.mobends.compat.ModCompatManager;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererSwimMixin
{
    @Redirect(
            method = "setupRotations",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/player/AbstractClientPlayerEntity;getSwimAmount(F)F"),
            require = 0
    )
    private float mobends$suppressSwimRotation(AbstractClientPlayerEntity entity, float partialTick)
    {
        EntityBender bender = EntityBenderRegistry.instance.getForEntity(entity);
        if (bender != null && bender.isAnimated() && !ModCompatManager.shouldDeferAnimation(entity)
                && !mobends$isCrawling(entity))
        {
            return 0.0F;
        }
        return entity.getSwimAmount(partialTick);
    }

    @Unique
    private static boolean mobends$isCrawling(AbstractClientPlayerEntity entity)
    {
        return entity.isVisuallySwimming() && !entity.isInWater();
    }
}
