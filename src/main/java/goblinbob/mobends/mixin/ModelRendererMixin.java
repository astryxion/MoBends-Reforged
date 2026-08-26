package goblinbob.mobends.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import goblinbob.mobends.core.client.model.IModelRendererExt;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.client.renderer.model.ModelRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ModelRenderer.class)
public abstract class ModelRendererMixin implements IModelRendererExt
{
    @Shadow
    @Final
    private ObjectList<ModelRenderer> children;

    @Unique
    private float xScale = 1.0F;
    @Unique
    private float yScale = 1.0F;
    @Unique
    private float zScale = 1.0F;
    @Unique
    private boolean skipDraw;

    @Override
    public float mobends$getXScale()
    {
        return this.xScale;
    }

    @Override
    public void mobends$setXScale(float value)
    {
        this.xScale = value;
    }

    @Override
    public float mobends$getYScale()
    {
        return this.yScale;
    }

    @Override
    public void mobends$setYScale(float value)
    {
        this.yScale = value;
    }

    @Override
    public float mobends$getZScale()
    {
        return this.zScale;
    }

    @Override
    public void mobends$setZScale(float value)
    {
        this.zScale = value;
    }

    @Override
    public boolean mobends$getSkipDraw()
    {
        return this.skipDraw;
    }

    @Override
    public void mobends$setSkipDraw(boolean value)
    {
        this.skipDraw = value;
    }

    @Override
    public List<ModelRenderer> mobends$getChildren()
    {
        return this.children;
    }

    @Inject(method = "translateAndRotate", at = @At("TAIL"))
    private void mobends$applyPartScale(MatrixStack poseStack, CallbackInfo ci)
    {
        if (this.xScale != 1.0F || this.yScale != 1.0F || this.zScale != 1.0F)
        {
            poseStack.scale(this.xScale, this.yScale, this.zScale);
        }
    }

    @Inject(method = "compile", at = @At("HEAD"), cancellable = true)
    private void mobends$skipOwnCubes(MatrixStack.Entry pose, IVertexBuilder consumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, CallbackInfo ci)
    {
        if (this.skipDraw)
        {
            ci.cancel();
        }
    }
}
