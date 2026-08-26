package goblinbob.mobends.mixin.wearablebackpacks;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import goblinbob.mobends.compat.WearableBackpacksCompat;
import goblinbob.mobends.core.client.MoBendsRenderContext;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "com.nyfaria.wearablebackpacks.client.renderer.SimpleArmorRenderer", remap = false)
public class BackPackRenderLayerMixin
{
    @Inject(method = "render", at = @At("HEAD"), require = 0, remap = true)
    private void mobends$beginFollowAnimatedBody(float partialTicks, MatrixStack poseStack,
                                                 IVertexBuilder bufferIn, int packedLightIn,
                                                 CallbackInfo ci)
    {
        WearableBackpacksCompat.beginFollow(poseStack, MoBendsRenderContext.getCurrentEntity());
    }

    @Inject(method = "render", at = @At("RETURN"), require = 0, remap = true)
    private void mobends$endFollowAnimatedBody(float partialTicks, MatrixStack poseStack,
                                               IVertexBuilder bufferIn, int packedLightIn,
                                               CallbackInfo ci)
    {
        WearableBackpacksCompat.endFollow(poseStack);
    }

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;isShiftKeyDown()Z"),
            require = 0,
            remap = true)
    private boolean mobends$skipVanillaSneakOffset(LivingEntity entity)
    {
        if (WearableBackpacksCompat.isFollowing())
        {
            return false;
        }
        return entity.isShiftKeyDown();
    }
}
