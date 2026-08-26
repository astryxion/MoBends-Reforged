package goblinbob.mobends.core.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import goblinbob.mobends.lib.math.Quaternion;
import goblinbob.mobends.lib.math.matrix.IMat4x4d;
import goblinbob.mobends.lib.math.vector.IVec3dRead;
import goblinbob.mobends.lib.math.vector.IVec3fRead;
import net.minecraft.util.math.vector.Matrix4f;

public class GlHelper
{
    public static void rotate(MatrixStack poseStack, Quaternion quaternionIn)
    {
        if (quaternionIn == null) return;

        if (quaternionIn.lengthSquared() < 1.0E-6F) return;

        net.minecraft.util.math.vector.Quaternion mcQuat = new net.minecraft.util.math.vector.Quaternion(
                quaternionIn.x,
                quaternionIn.y,
                quaternionIn.z,
                quaternionIn.w
        );

        poseStack.mulPose(mcQuat);
    }

    public static void rotate(MatrixStack poseStack, net.minecraft.util.math.vector.Quaternion quaternionIn)
    {
        if (quaternionIn == null) return;
        float lengthSq = quaternionIn.i() * quaternionIn.i()
                + quaternionIn.j() * quaternionIn.j()
                + quaternionIn.k() * quaternionIn.k()
                + quaternionIn.r() * quaternionIn.r();
        if (lengthSq < 1.0E-6F) return;
        poseStack.mulPose(quaternionIn);
    }

    public static void transform(MatrixStack poseStack, IMat4x4d matrixIn)
    {
        if (matrixIn == null) return;

        Matrix4f mcMatrix = new Matrix4f(new float[] {
                (float) matrixIn.get(0, 0), (float) matrixIn.get(0, 1), (float) matrixIn.get(0, 2), (float) matrixIn.get(0, 3),
                (float) matrixIn.get(1, 0), (float) matrixIn.get(1, 1), (float) matrixIn.get(1, 2), (float) matrixIn.get(1, 3),
                (float) matrixIn.get(2, 0), (float) matrixIn.get(2, 1), (float) matrixIn.get(2, 2), (float) matrixIn.get(2, 3),
                (float) matrixIn.get(3, 0), (float) matrixIn.get(3, 1), (float) matrixIn.get(3, 2), (float) matrixIn.get(3, 3)
        });

        poseStack.last().pose().multiply(mcMatrix);
    }

    public static void translate(MatrixStack poseStack, IVec3fRead vector)
    {
        if (vector == null) return;
        poseStack.translate(vector.getX(), vector.getY(), vector.getZ());
    }

    public static void translate(MatrixStack poseStack, IVec3dRead vector)
    {
        if (vector == null) return;
        poseStack.translate(vector.getX(), vector.getY(), vector.getZ());
    }

    public static void translate(MatrixStack poseStack, float x, float y, float z)
    {
        poseStack.translate(x, y, z);
    }

    public static void scale(MatrixStack poseStack, float scale)
    {
        poseStack.scale(scale, scale, scale);
    }

    public static void scale(MatrixStack poseStack, float x, float y, float z)
    {
        poseStack.scale(x, y, z);
    }

    public static Quaternion quaternionFromAxisAngle(float x, float y, float z, float angleDegrees)
    {
        float angleRadians = (float) Math.toRadians(angleDegrees);
        float halfAngle = angleRadians * 0.5f;
        float sinHalfAngle = (float) Math.sin(halfAngle);
        float cosHalfAngle = (float) Math.cos(halfAngle);

        float length = (float) Math.sqrt(x * x + y * y + z * z);
        if (length > 0)
        {
            x /= length;
            y /= length;
            z /= length;
        }

        return new Quaternion(
                x * sinHalfAngle,
                y * sinHalfAngle,
                z * sinHalfAngle,
                cosHalfAngle
        );
    }

    public static net.minecraft.util.math.vector.Quaternion toJomlQuaternion(Quaternion quat)
    {
        if (quat == null) return new net.minecraft.util.math.vector.Quaternion(0.0F, 0.0F, 0.0F, 1.0F);
        return new net.minecraft.util.math.vector.Quaternion(quat.x, quat.y, quat.z, quat.w);
    }

    public static Quaternion fromJomlQuaternion(net.minecraft.util.math.vector.Quaternion quat)
    {
        if (quat == null) return new Quaternion();
        return new Quaternion(quat.i(), quat.j(), quat.k(), quat.r());
    }

    @Deprecated
    public static void rotate(Quaternion quaternionIn)
    {
    }

    @Deprecated
    public static void transform(IMat4x4d matrixIn)
    {
    }

    @Deprecated
    public static void vertex(IVec3fRead vector)
    {
    }

    @Deprecated
    public static void vertex(IVec3dRead vector)
    {
    }
}
