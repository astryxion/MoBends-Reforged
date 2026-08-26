package goblinbob.mobends.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import goblinbob.mobends.forge.mixin.MixinBridge;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.entity.model.IllagerModel;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SegmentedModel.class)
public abstract class HierarchicalModelMixin<E extends Entity> {

    @Inject(method = "renderToBuffer", at = @At("HEAD"), cancellable = true)
    private void mobends$interceptRender(MatrixStack poseStack, IVertexBuilder vertexConsumer,
                                         int packedLight, int packedOverlay,
                                         float red, float green, float blue, float alpha,
                                         CallbackInfo ci) {
        if ((Object) this instanceof IllagerModel<?> && MixinBridge.shouldRenderBipedCustom()) {
            MixinBridge.renderBipedMutated(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
            ci.cancel();
        }
    }

}
