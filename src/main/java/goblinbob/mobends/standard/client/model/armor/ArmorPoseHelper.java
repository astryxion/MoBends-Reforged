package goblinbob.mobends.standard.client.model.armor;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import goblinbob.mobends.api.rendering.IEntityVertexHelper;
import goblinbob.mobends.core.client.model.ModelPartTransform;
import goblinbob.mobends.core.util.GlHelper;
import goblinbob.mobends.standard.data.BipedEntityData;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector4f;

import java.util.ArrayList;
import java.util.List;

public final class ArmorPoseHelper
{
    public static final float SCALE = 1.0f / 16.0f;

    private ArmorPoseHelper()
    {
    }

    public static void applyPartTransform(MatrixStack poseStack, ModelPartTransform transform, boolean isChildPart)
    {
        if (transform == null)
        {
            return;
        }

        float offsetScale = transform.offsetScale;

        if (transform.globalOffset.x != 0 || transform.globalOffset.y != 0 || transform.globalOffset.z != 0)
        {
            poseStack.translate(
                transform.globalOffset.x * SCALE,
                transform.globalOffset.y * SCALE,
                transform.globalOffset.z * SCALE
            );
        }

        if (isChildPart && (transform.position.x != 0 || transform.position.y != 0 || transform.position.z != 0))
        {
            poseStack.translate(
                transform.position.x * SCALE * offsetScale,
                transform.position.y * SCALE * offsetScale,
                transform.position.z * SCALE * offsetScale
            );
        }

        if (transform.offset.x != 0 || transform.offset.y != 0 || transform.offset.z != 0)
        {
            poseStack.translate(
                transform.offset.x * SCALE * offsetScale,
                transform.offset.y * SCALE * offsetScale,
                transform.offset.z * SCALE * offsetScale
            );
        }

        GlHelper.rotate(poseStack, transform.rotation.getSmooth());
    }

    public static void applyLegTransform(MatrixStack poseStack, ModelPartTransform transform, float vanillaLegX)
    {
        if (transform == null)
        {
            return;
        }

        float offsetScale = transform.offsetScale;

        if (transform.globalOffset.x != 0 || transform.globalOffset.y != 0 || transform.globalOffset.z != 0)
        {
            poseStack.translate(
                transform.globalOffset.x * SCALE,
                transform.globalOffset.y * SCALE,
                transform.globalOffset.z * SCALE
            );
        }

        poseStack.translate(
            vanillaLegX * SCALE * offsetScale,
            transform.position.y * SCALE * offsetScale,
            transform.position.z * SCALE * offsetScale
        );

        if (transform.offset.x != 0 || transform.offset.y != 0 || transform.offset.z != 0)
        {
            poseStack.translate(
                transform.offset.x * SCALE * offsetScale,
                transform.offset.y * SCALE * offsetScale,
                transform.offset.z * SCALE * offsetScale
            );
        }

        GlHelper.rotate(poseStack, transform.rotation.getSmooth());
    }

    public static void applyBodyTransformWithPivot(MatrixStack poseStack, BipedEntityData<?> entityData)
    {
        ModelPartTransform body = entityData.body;
        if (body == null)
        {
            return;
        }

        if (body.globalOffset.x != 0 || body.globalOffset.y != 0 || body.globalOffset.z != 0)
        {
            poseStack.translate(
                body.globalOffset.x * SCALE,
                body.globalOffset.y * SCALE,
                body.globalOffset.z * SCALE
            );
        }

        float offsetScale = body.offsetScale;

        poseStack.translate(
            body.position.x * SCALE * offsetScale,
            body.position.y * SCALE * offsetScale,
            body.position.z * SCALE * offsetScale
        );

        if (body.offset.x != 0 || body.offset.y != 0 || body.offset.z != 0)
        {
            poseStack.translate(
                body.offset.x * SCALE * offsetScale,
                body.offset.y * SCALE * offsetScale,
                body.offset.z * SCALE * offsetScale
            );
        }

        GlHelper.rotate(poseStack, body.rotation.getSmooth());

        poseStack.translate(
            -body.position.x * SCALE * offsetScale,
            -body.position.y * SCALE * offsetScale,
            -body.position.z * SCALE * offsetScale
        );
    }

    public static List<CapturedVertex[]> groupIntoQuads(List<CapturedVertex> vertices)
    {
        List<CapturedVertex[]> quads = new ArrayList<>();
        for (int i = 0; i + 3 < vertices.size(); i += 4)
        {
            quads.add(new CapturedVertex[] {
                vertices.get(i),
                vertices.get(i + 1),
                vertices.get(i + 2),
                vertices.get(i + 3)
            });
        }
        return quads;
    }

    public static void renderSlicedVertices(
            MatrixStack poseStack,
            IVertexBuilder consumer,
            List<SliceResult> sliceResults,
            boolean renderUpper,
            float offsetX,
            float offsetY,
            float offsetZ,
            int packedLight,
            int packedOverlay,
            int armorColor)
    {
        renderSlicedVertices(poseStack, consumer, sliceResults, renderUpper, offsetX, offsetY, offsetZ,
                packedLight, packedOverlay, armorColor, LimbInflation.NONE);
    }

    public static void renderSlicedVertices(
            MatrixStack poseStack,
            IVertexBuilder consumer,
            List<SliceResult> sliceResults,
            boolean renderUpper,
            float offsetX,
            float offsetY,
            float offsetZ,
            int packedLight,
            int packedOverlay,
            int armorColor,
            LimbInflation inflation)
    {
        Matrix4f matrix = poseStack.last().pose();
        Matrix3f normal = poseStack.last().normal();

        for (SliceResult result : sliceResults)
        {
            List<SliceResult.SlicedVertex> vertices = renderUpper
                    ? result.getUpperVertices()
                    : result.getLowerVertices();

            if (vertices.isEmpty())
            {
                continue;
            }

            int vertexCount = vertices.size();

            if (vertexCount == 4)
            {
                outputVertex(matrix, normal, consumer, vertices.get(0), offsetX, offsetY, offsetZ, packedLight, packedOverlay, armorColor, inflation);
                outputVertex(matrix, normal, consumer, vertices.get(1), offsetX, offsetY, offsetZ, packedLight, packedOverlay, armorColor, inflation);
                outputVertex(matrix, normal, consumer, vertices.get(2), offsetX, offsetY, offsetZ, packedLight, packedOverlay, armorColor, inflation);
                outputVertex(matrix, normal, consumer, vertices.get(3), offsetX, offsetY, offsetZ, packedLight, packedOverlay, armorColor, inflation);
            }
            else if (vertexCount == 3)
            {
                outputVertex(matrix, normal, consumer, vertices.get(0), offsetX, offsetY, offsetZ, packedLight, packedOverlay, armorColor, inflation);
                outputVertex(matrix, normal, consumer, vertices.get(1), offsetX, offsetY, offsetZ, packedLight, packedOverlay, armorColor, inflation);
                outputVertex(matrix, normal, consumer, vertices.get(2), offsetX, offsetY, offsetZ, packedLight, packedOverlay, armorColor, inflation);
                outputVertex(matrix, normal, consumer, vertices.get(2), offsetX, offsetY, offsetZ, packedLight, packedOverlay, armorColor, inflation);
            }
            else if (vertexCount == 5)
            {
                outputVertex(matrix, normal, consumer, vertices.get(0), offsetX, offsetY, offsetZ, packedLight, packedOverlay, armorColor, inflation);
                outputVertex(matrix, normal, consumer, vertices.get(1), offsetX, offsetY, offsetZ, packedLight, packedOverlay, armorColor, inflation);
                outputVertex(matrix, normal, consumer, vertices.get(2), offsetX, offsetY, offsetZ, packedLight, packedOverlay, armorColor, inflation);
                outputVertex(matrix, normal, consumer, vertices.get(3), offsetX, offsetY, offsetZ, packedLight, packedOverlay, armorColor, inflation);

                outputVertex(matrix, normal, consumer, vertices.get(0), offsetX, offsetY, offsetZ, packedLight, packedOverlay, armorColor, inflation);
                outputVertex(matrix, normal, consumer, vertices.get(3), offsetX, offsetY, offsetZ, packedLight, packedOverlay, armorColor, inflation);
                outputVertex(matrix, normal, consumer, vertices.get(4), offsetX, offsetY, offsetZ, packedLight, packedOverlay, armorColor, inflation);
                outputVertex(matrix, normal, consumer, vertices.get(4), offsetX, offsetY, offsetZ, packedLight, packedOverlay, armorColor, inflation);
            }
            else if (vertexCount >= 6)
            {
                for (int i = 1; i < vertexCount - 1; i++)
                {
                    outputVertex(matrix, normal, consumer, vertices.get(0), offsetX, offsetY, offsetZ, packedLight, packedOverlay, armorColor, inflation);
                    outputVertex(matrix, normal, consumer, vertices.get(i), offsetX, offsetY, offsetZ, packedLight, packedOverlay, armorColor, inflation);
                    outputVertex(matrix, normal, consumer, vertices.get(i + 1), offsetX, offsetY, offsetZ, packedLight, packedOverlay, armorColor, inflation);
                    outputVertex(matrix, normal, consumer, vertices.get(i + 1), offsetX, offsetY, offsetZ, packedLight, packedOverlay, armorColor, inflation);
                }
            }
        }
    }

    public static void outputVertex(
            Matrix4f matrix,
            Matrix3f normal,
            IVertexBuilder consumer,
            SliceResult.SlicedVertex v,
            float offsetX,
            float offsetY,
            float offsetZ,
            int packedLight,
            int packedOverlay,
            int armorColor,
            LimbInflation inflation)
    {
        float vx = inflation.displaceX(v.x) + offsetX;
        float vy = v.y + offsetY;
        float vz = inflation.displaceZ(v.z) + offsetZ;

        Vector4f pos = new Vector4f(vx, vy, vz, 1.0F);
        pos.transform(matrix);

        Vector3f n = new Vector3f(v.normalX, v.normalY, v.normalZ);
        n.transform(normal);

        float tintR = ((armorColor >> 16) & 0xFF) / 255.0F;
        float tintG = ((armorColor >> 8) & 0xFF) / 255.0F;
        float tintB = (armorColor & 0xFF) / 255.0F;
        float tintA = ((armorColor >> 24) & 0xFF) / 255.0F;

        int color = ((int)(v.alpha * tintA * 255.0F) << 24) |
                    ((int)(v.red * tintR * 255.0F) << 16) |
                    ((int)(v.green * tintG * 255.0F) << 8) |
                    (int)(v.blue * tintB * 255.0F);
        IEntityVertexHelper.Holder.getHelper().emitVertex(consumer,
                pos.x(), pos.y(), pos.z(), color, v.u, v.v,
                packedOverlay, packedLight, n.x(), n.y(), n.z());
    }

    public static void resetPartToOrigin(ModelRenderer part, float[] storage)
    {
        storage[0] = part.x;
        storage[1] = part.y;
        storage[2] = part.z;
        storage[3] = part.xRot;
        storage[4] = part.yRot;
        storage[5] = part.zRot;

        part.x = 0;
        part.y = 0;
        part.z = 0;
        part.xRot = 0;
        part.yRot = 0;
        part.zRot = 0;
    }

    public static void restorePartFromStorage(ModelRenderer part, float[] storage)
    {
        part.x = storage[0];
        part.y = storage[1];
        part.z = storage[2];
        part.xRot = storage[3];
        part.yRot = storage[4];
        part.zRot = storage[5];
    }

    public static void renderPartAtOrigin(
            ModelRenderer part,
            MatrixStack poseStack,
            IVertexBuilder vertexConsumer,
            int packedLight,
            int packedOverlay)
    {
        if (part == null || !part.visible)
        {
            return;
        }

        float origX = part.x, origY = part.y, origZ = part.z;
        float origXRot = part.xRot, origYRot = part.yRot, origZRot = part.zRot;

        part.x = 0;
        part.y = 0;
        part.z = 0;
        part.xRot = 0;
        part.yRot = 0;
        part.zRot = 0;

        part.render(poseStack, vertexConsumer, packedLight, packedOverlay);

        part.x = origX;
        part.y = origY;
        part.z = origZ;
        part.xRot = origXRot;
        part.yRot = origYRot;
        part.zRot = origZRot;
    }

    public static void renderPartWithVanillaPosition(
            ModelRenderer part,
            MatrixStack poseStack,
            IVertexBuilder vertexConsumer,
            int packedLight,
            int packedOverlay)
    {
        if (part == null || !part.visible)
        {
            return;
        }

        float origXRot = part.xRot, origYRot = part.yRot, origZRot = part.zRot;

        part.xRot = 0;
        part.yRot = 0;
        part.zRot = 0;

        part.render(poseStack, vertexConsumer, packedLight, packedOverlay);

        part.xRot = origXRot;
        part.yRot = origYRot;
        part.zRot = origZRot;
    }
}
