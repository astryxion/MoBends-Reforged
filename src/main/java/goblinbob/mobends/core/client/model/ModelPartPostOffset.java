package goblinbob.mobends.core.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.math.vector.Vector3f;

public class ModelPartPostOffset extends ModelPart
{

    protected Vector3f postOffset = new Vector3f(0.0F, 0.0F, 0.0F);

    public ModelPartPostOffset(int texOffsetX, int texOffsetY)
    {
        super(texOffsetX, texOffsetY);
    }

    public ModelPartPostOffset()
    {
        super();
    }

    public ModelPartPostOffset setPostOffset(float x, float y, float z)
    {
        this.postOffset.set(x, y, z);
        return this;
    }

    @Override
    public void propagateTransform(MatrixStack poseStack, float scale)
    {
        super.propagateTransform(poseStack, scale);
    }

    @Override
    public void applyPostTransform(MatrixStack poseStack, float scale)
    {
        poseStack.translate(this.postOffset.x * scale, this.postOffset.y * scale, this.postOffset.z * scale);
    }

}
