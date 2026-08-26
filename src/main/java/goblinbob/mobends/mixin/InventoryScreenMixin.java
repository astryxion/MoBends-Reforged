package goblinbob.mobends.mixin;

import goblinbob.mobends.core.client.MoBendsRenderContext;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin
{
    @Inject(method = "renderEntityInInventory", at = @At("HEAD"), require = 0)
    private static void mobends$beginGuiEntityRender(int x, int y, int scale,
                                                     float mouseX, float mouseY,
                                                     LivingEntity entity, CallbackInfo ci)
    {
        MoBendsRenderContext.beginGuiEntityRender();
    }

    @Inject(method = "renderEntityInInventory", at = @At("RETURN"), require = 0)
    private static void mobends$endGuiEntityRender(int x, int y, int scale,
                                                   float mouseX, float mouseY,
                                                   LivingEntity entity, CallbackInfo ci)
    {
        MoBendsRenderContext.endGuiEntityRender();
    }
}
