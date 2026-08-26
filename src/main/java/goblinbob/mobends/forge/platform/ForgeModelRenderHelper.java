package goblinbob.mobends.forge.platform;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import goblinbob.mobends.api.rendering.IModelRenderHelper;
import goblinbob.mobends.standard.main.ModConfig;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemRenderer;

public class ForgeModelRenderHelper implements IModelRenderHelper
{
    @Override
    public void renderModelToBuffer(Object model, Object poseStack, Object vertexConsumer, int packedLight, int packedOverlay, int color)
    {
        Model m = (Model) model;
        MatrixStack ps = (MatrixStack) poseStack;
        IVertexBuilder vc = (IVertexBuilder) vertexConsumer;

        float alpha = ((color >> 24) & 0xFF) / 255.0f;
        float red = ((color >> 16) & 0xFF) / 255.0f;
        float green = ((color >> 8) & 0xFF) / 255.0f;
        float blue = (color & 0xFF) / 255.0f;

        m.renderToBuffer(ps, vc, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public Object getArmorFoilBuffer(Object bufferSource, Object renderType, boolean hasFoil)
    {
        IRenderTypeBuffer source = (IRenderTypeBuffer) bufferSource;
        RenderType type = (RenderType) renderType;
        return ItemRenderer.getArmorFoilBuffer(source, type, ModConfig.newEnchantGlint, hasFoil);
    }
}
