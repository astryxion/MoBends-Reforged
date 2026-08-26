package goblinbob.mobends.platform;

import com.mojang.blaze3d.matrix.MatrixStack;
import goblinbob.mobends.api.rendering.IPoseStack;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

import java.nio.FloatBuffer;

public class McPoseStack implements IPoseStack
{
    private final MatrixStack poseStack;

    public McPoseStack(MatrixStack poseStack)
    {
        this.poseStack = poseStack;
    }

    public McPoseStack()
    {
        this.poseStack = new MatrixStack();
    }

    @Override
    public void pushPose()
    {
        poseStack.pushPose();
    }

    @Override
    public void popPose()
    {
        poseStack.popPose();
    }

    @Override
    public void translate(double x, double y, double z)
    {
        poseStack.translate(x, y, z);
    }

    @Override
    public void translate(float x, float y, float z)
    {
        poseStack.translate(x, y, z);
    }

    @Override
    public void scale(float x, float y, float z)
    {
        poseStack.scale(x, y, z);
    }

    @Override
    public void rotateX(float angle)
    {
        poseStack.mulPose(Vector3f.XP.rotation(angle));
    }

    @Override
    public void rotateY(float angle)
    {
        poseStack.mulPose(Vector3f.YP.rotation(angle));
    }

    @Override
    public void rotateZ(float angle)
    {
        poseStack.mulPose(Vector3f.ZP.rotation(angle));
    }

    @Override
    public void mulPoseQuaternion(float x, float y, float z, float w)
    {
        poseStack.mulPose(new Quaternion(x, y, z, w));
    }

    @Override
    public void mulPoseMatrix(float[] matrix)
    {
        Matrix4f m = new Matrix4f(matrix);
        poseStack.last().pose().multiply(m);
        poseStack.last().normal().mul(new Matrix3f(m));
    }

    @Override
    public void setIdentity()
    {
        poseStack.last().pose().setIdentity();
        poseStack.last().normal().setIdentity();
    }

    @Override
    public void getPose(float[] dest)
    {
        Matrix4f pose = poseStack.last().pose();
        pose.store(FloatBuffer.wrap(dest));
    }

    @Override
    public void getNormal(float[] dest)
    {
        Matrix3f normal = poseStack.last().normal();
        if (dest.length >= 9)
        {
            dest[0] = normal.m00;
            dest[1] = normal.m10;
            dest[2] = normal.m20;
            dest[3] = normal.m01;
            dest[4] = normal.m11;
            dest[5] = normal.m21;
            dest[6] = normal.m02;
            dest[7] = normal.m12;
            dest[8] = normal.m22;
        }
    }

    @Override
    public Object getNative()
    {
        return poseStack;
    }

    public MatrixStack getPoseStack()
    {
        return poseStack;
    }

    @Override
    public boolean isEmpty()
    {
        return false;
    }

    @Override
    public void clear()
    {
        while (!poseStack.clear())
        {
        }
        poseStack.last().pose().setIdentity();
        poseStack.last().normal().setIdentity();
    }
}
