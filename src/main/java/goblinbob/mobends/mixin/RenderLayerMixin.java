package goblinbob.mobends.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import goblinbob.mobends.core.client.MoBendsRenderContext;
import goblinbob.mobends.standard.mutators.BipedMutator;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerRenderer.class)
public abstract class RenderLayerMixin {

    @Inject(method = "coloredCutoutModelCopyLayerRender", at = @At("HEAD"), cancellable = true, require = 0)
    private static <T extends LivingEntity> void mobends$redirectOverlayToBendsParts(
            EntityModel<T> parentModel, EntityModel<T> copyModel, ResourceLocation textureLocation,
            MatrixStack poseStack, IRenderTypeBuffer bufferSource, int packedLight,
            T entity, float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch, float partialTick,
            float red, float green, float blue,
            CallbackInfo ci) {
        if (entity.isInvisible()) {
            return;
        }
        BipedMutator<?, ?, ?> mutator = MoBendsRenderContext.getCurrentBipedMutator();
        if (mutator == null || !mutator.shouldRenderCustom()
                || !(copyModel instanceof BipedModel<?>)
                || !mutator.hasOuterParts()) {
            return;
        }
        int color = ((int) (255.0F) << 24)
                | ((int) (red * 255.0F) << 16)
                | ((int) (green * 255.0F) << 8)
                | (int) (blue * 255.0F);
        int packedOverlay = LivingRenderer.getOverlayCoords(entity, 0.0F);
        IVertexBuilder vc = bufferSource.getBuffer(RenderType.entityCutoutNoCull(textureLocation));
        mutator.renderOuter(poseStack, vc, packedLight, packedOverlay, color);
        ci.cancel();
    }
}
