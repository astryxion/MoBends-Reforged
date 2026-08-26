package goblinbob.mobends.mixin;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import goblinbob.mobends.standard.client.model.armor.ArmorCaptureContext;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IRenderTypeBuffer.Impl.class)
public abstract class BufferSourceCaptureMixin {

    @Inject(method = "getBuffer", at = @At("HEAD"), cancellable = true)
    private void mobends$redirectBufferToCapture(RenderType renderType, CallbackInfoReturnable<IVertexBuilder> cir) {
        IVertexBuilder capture = ArmorCaptureContext.active();

        if (capture == null) {
            return;
        }

        if (ArmorCaptureContext.isEmissiveType(renderType)) {
            ArmorCaptureContext.recordEmissive(renderType);
            cir.setReturnValue(ArmorCaptureContext.discard());
            return;
        }

        cir.setReturnValue(capture);
    }

}
