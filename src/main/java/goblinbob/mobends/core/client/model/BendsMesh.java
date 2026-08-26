package goblinbob.mobends.core.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import goblinbob.mobends.api.rendering.IEntityVertexHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector4f;

import java.util.Arrays;

public class BendsMesh
{
    private final float[] positions;
    private final float[] uvs;
    private final float[] normals;
    private final int vertexCount;

    public BendsMesh(float[] positions, float[] uvs, float[] normals)
    {
        this.positions = positions;
        this.uvs = uvs;
        this.normals = normals;
        this.vertexCount = positions.length / 3;
    }

    public boolean isEmpty()
    {
        return vertexCount == 0;
    }

    public void compile(MatrixStack.Entry pose, IVertexBuilder vertexConsumer,
                        int packedLight, int packedOverlay, int color)
    {
        final Matrix4f matrix = pose.pose();
        final Matrix3f normalMatrix = pose.normal();

        for (int i = 0; i < vertexCount; ++i)
        {
            final int p = i * 3;
            final int t = i * 2;

            final float x = positions[p];
            final float y = positions[p + 1];
            final float z = positions[p + 2];

            final float nx = normals[p];
            final float ny = normals[p + 1];
            final float nz = normals[p + 2];

            Vector4f pos = new Vector4f(x, y, z, 1.0F);
            pos.transform(matrix);

            Vector3f n = new Vector3f(nx, ny, nz);
            n.transform(normalMatrix);

            IEntityVertexHelper.Holder.getHelper().emitVertex(vertexConsumer,
                    pos.x(), pos.y(), pos.z(),
                    color,
                    uvs[t], uvs[t + 1],
                    packedOverlay, packedLight,
                    n.x(), n.y(), n.z());
        }
    }

    public static class Builder
    {
        private float[] positions = new float[288];
        private float[] uvs = new float[192];
        private float[] normals = new float[288];
        private int count = 0;

        public void addVertex(float x, float y, float z, float u, float v, float nx, float ny, float nz)
        {
            if ((count + 1) * 3 > positions.length)
            {
                positions = Arrays.copyOf(positions, positions.length * 2);
                normals = Arrays.copyOf(normals, normals.length * 2);
                uvs = Arrays.copyOf(uvs, uvs.length * 2);
            }

            final int p = count * 3;
            final int t = count * 2;

            positions[p] = x;
            positions[p + 1] = y;
            positions[p + 2] = z;
            uvs[t] = u;
            uvs[t + 1] = v;
            normals[p] = nx;
            normals[p + 1] = ny;
            normals[p + 2] = nz;

            ++count;
        }

        public boolean isEmpty()
        {
            return count == 0;
        }

        public BendsMesh build()
        {
            return new BendsMesh(Arrays.copyOf(positions, count * 3),
                                 Arrays.copyOf(uvs, count * 2),
                                 Arrays.copyOf(normals, count * 3));
        }
    }
}
