package goblinbob.mobends.forge.platform;

import goblinbob.mobends.api.rendering.IBufferSource;
import goblinbob.mobends.api.rendering.IRenderLayer;
import goblinbob.mobends.api.rendering.IVertexConsumer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;

public class ForgeBufferSource implements IBufferSource
{
    private final IRenderTypeBuffer bufferSource;

    public ForgeBufferSource(IRenderTypeBuffer bufferSource)
    {
        this.bufferSource = bufferSource;
    }

    @Override
    public IVertexConsumer getBuffer(IRenderLayer renderLayer)
    {
        RenderType renderType = (RenderType) renderLayer.getNative();
        return new ForgeVertexConsumer(bufferSource.getBuffer(renderType));
    }

    @Override
    public Object getNative()
    {
        return bufferSource;
    }

    public IRenderTypeBuffer getBufferSource()
    {
        return bufferSource;
    }
}
