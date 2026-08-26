package goblinbob.mobends.mixin.carryon;

import com.mojang.blaze3d.matrix.MatrixStack;
import goblinbob.mobends.compat.CarryOnCompat;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "tschipp.carryon.client.event.RenderEvents", remap = false)
public class CarryRenderHelperMixin
{
    @Inject(method = "applyGeneralTransformations", at = @At("RETURN"), require = 0)
    private void mobends$followAnimatedHands(PlayerEntity player, float partialTicks, MatrixStack matrix, CallbackInfo ci)
    {
        CarryOnCompat.applyAnchor(player, matrix);
    }
}
