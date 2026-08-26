package goblinbob.mobends.core.client.model;

import net.minecraft.client.renderer.model.ModelRenderer;

import java.util.List;

/**
 * 1.16.5 ModelRenderer has no skipDraw / per-axis scale / named children.
 * Mixin-applied fields that 1.20 ModelPart exposed directly.
 *
 * Lives outside goblinbob.mobends.mixin because Mixin 0.8 forbids non-mixin
 * classes in a defined mixin package.
 */
public interface IModelRendererExt
{
    float mobends$getXScale();

    void mobends$setXScale(float value);

    float mobends$getYScale();

    void mobends$setYScale(float value);

    float mobends$getZScale();

    void mobends$setZScale(float value);

    boolean mobends$getSkipDraw();

    void mobends$setSkipDraw(boolean value);

    List<ModelRenderer> mobends$getChildren();

    static IModelRendererExt of(ModelRenderer part)
    {
        return (IModelRendererExt) (Object) part;
    }
}
