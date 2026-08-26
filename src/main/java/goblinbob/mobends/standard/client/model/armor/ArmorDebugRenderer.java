package goblinbob.mobends.standard.client.model.armor;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import goblinbob.mobends.api.platform.PlatformServices;
import goblinbob.mobends.api.rendering.DrawMode;
import goblinbob.mobends.api.rendering.IBufferBuilder;
import goblinbob.mobends.api.rendering.IModelRenderHelper;
import goblinbob.mobends.api.rendering.ITesselator;
import goblinbob.mobends.api.rendering.VertexFormatType;
import goblinbob.mobends.core.client.model.IModelPart;
import goblinbob.mobends.standard.data.BipedEntityData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArmorDebugRenderer
{
    private static final float SCALE = 1.0f / 16.0f;

    private static List<CapturedVertex> lastCapturedVertices = null;
    private static BipedEntityData<?> lastEntityData = null;

    private static BipedModel<PlayerEntity> innerArmorModel = null;
    private static BipedModel<PlayerEntity> outerArmorModel = null;

    public static void storeCapturedVertices(List<CapturedVertex> vertices, BipedEntityData<?> data)
    {
        lastCapturedVertices = vertices;
        lastEntityData = data;
    }

    public static int getCapturedVertexCount()
    {
        return lastCapturedVertices != null ? lastCapturedVertices.size() : 0;
    }

    public static String getArmorSlotInfo()
    {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        if (player == null) return "No player";

        StringBuilder sb = new StringBuilder();
        sb.append("H:").append(hasArmor(player, EquipmentSlotType.HEAD) ? "Y" : "N");
        sb.append(" C:").append(hasArmor(player, EquipmentSlotType.CHEST) ? "Y" : "N");
        sb.append(" L:").append(hasArmor(player, EquipmentSlotType.LEGS) ? "Y" : "N");
        sb.append(" F:").append(hasArmor(player, EquipmentSlotType.FEET) ? "Y" : "N");
        return sb.toString();
    }

    public static String getCaptureStatus()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Models: ").append(innerArmorModel != null && outerArmorModel != null ? "OK" : "NOT INIT");
        if (lastCapturedVertices != null)
        {
            sb.append(" | Last: ").append(lastCapturedVertices.size()).append("v");
        }
        else
        {
            sb.append(" | Last: null");
        }
        return sb.toString();
    }

    private static boolean hasArmor(PlayerEntity player, EquipmentSlotType slot)
    {
        ItemStack stack = player.getItemBySlot(slot);
        return !stack.isEmpty() && stack.getItem() instanceof ArmorItem;
    }

    @SuppressWarnings("unchecked")
    private static void initArmorModels()
    {
        if (innerArmorModel == null || outerArmorModel == null)
        {
            innerArmorModel = new BipedModel<>(0.5F);
            outerArmorModel = new BipedModel<>(1.0F);
        }
    }

    public static void renderDebug(
            MatrixStack poseStack,
            BipedEntityData<?> data,
            boolean showOriginal,
            boolean showTransformed,
            boolean showBoneRegions,
            boolean showBoneAxes,
            boolean showTestGeometry,
            Map<BoneRegion, Boolean> boneEnabled)
    {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        if (player != null)
        {
            capturePlayerArmor(player, data);
        }

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.depthMask(false);

        renderOriginMarker(poseStack);

        if (showBoneRegions)
        {
            renderBoneRegions(poseStack, boneEnabled);
        }

        if (showBoneAxes)
        {
            renderBoneAxes(poseStack, data, boneEnabled);
        }

        if (showTestGeometry)
        {
            renderTestGeometry(poseStack);
        }

        if (lastCapturedVertices != null && !lastCapturedVertices.isEmpty())
        {
            if (showOriginal)
            {
                renderOriginalVertices(poseStack, lastCapturedVertices, boneEnabled);
            }

            if (showTransformed)
            {
                renderTransformedVertices(poseStack, lastCapturedVertices, data, boneEnabled);
            }
        }

        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    private static void renderTestGeometry(MatrixStack poseStack)
    {
        PlatformServices.get().setPositionColorShader();
        RenderSystem.lineWidth(2.0f);

        Matrix4f matrix = poseStack.last().pose();

        ITesselator tesselator = ITesselator.getInstance();
        IBufferBuilder buffer = tesselator.begin(DrawMode.DEBUG_LINES, VertexFormatType.POSITION_COLOR);

        drawWireBox(buffer, matrix, -4, 0, -2, 4, 12, 2, 1.0f, 1.0f, 0.0f, 1.0f);

        drawWireBox(buffer, matrix, -4, -8, -4, 4, 0, 4, 0.0f, 1.0f, 1.0f, 1.0f);

        drawWireBox(buffer, matrix, 4, 0, -2, 8, 12, 2, 1.0f, 0.5f, 0.0f, 1.0f);

        drawWireBox(buffer, matrix, -8, 0, -2, -4, 12, 2, 1.0f, 0.5f, 0.0f, 1.0f);

        drawWireBox(buffer, matrix, -0.1f, 12, -2, 3.9f, 24, 2, 0.5f, 0.0f, 1.0f, 1.0f);

        drawWireBox(buffer, matrix, -3.9f, 12, -2, 0.1f, 24, 2, 0.5f, 0.0f, 1.0f, 1.0f);

        tesselator.endAndDraw(buffer);
    }

    private static void drawWireBox(IBufferBuilder buffer, Matrix4f matrix,
                                     float minX, float minY, float minZ,
                                     float maxX, float maxY, float maxZ,
                                     float r, float g, float b, float a)
    {
        minX *= SCALE;
        minY *= SCALE;
        minZ *= SCALE;
        maxX *= SCALE;
        maxY *= SCALE;
        maxZ *= SCALE;

        addLine(buffer, matrix, minX, minY, minZ, maxX, minY, minZ, r, g, b, a);
        addLine(buffer, matrix, maxX, minY, minZ, maxX, minY, maxZ, r, g, b, a);
        addLine(buffer, matrix, maxX, minY, maxZ, minX, minY, maxZ, r, g, b, a);
        addLine(buffer, matrix, minX, minY, maxZ, minX, minY, minZ, r, g, b, a);

        addLine(buffer, matrix, minX, maxY, minZ, maxX, maxY, minZ, r, g, b, a);
        addLine(buffer, matrix, maxX, maxY, minZ, maxX, maxY, maxZ, r, g, b, a);
        addLine(buffer, matrix, maxX, maxY, maxZ, minX, maxY, maxZ, r, g, b, a);
        addLine(buffer, matrix, minX, maxY, maxZ, minX, maxY, minZ, r, g, b, a);

        addLine(buffer, matrix, minX, minY, minZ, minX, maxY, minZ, r, g, b, a);
        addLine(buffer, matrix, maxX, minY, minZ, maxX, maxY, minZ, r, g, b, a);
        addLine(buffer, matrix, maxX, minY, maxZ, maxX, maxY, maxZ, r, g, b, a);
        addLine(buffer, matrix, minX, minY, maxZ, minX, maxY, maxZ, r, g, b, a);
    }

    private static void renderOriginMarker(MatrixStack poseStack)
    {
        PlatformServices.get().setPositionColorShader();
        RenderSystem.lineWidth(3.0f);

        Matrix4f matrix = poseStack.last().pose();

        ITesselator tesselator = ITesselator.getInstance();
        IBufferBuilder buffer = tesselator.begin(DrawMode.DEBUG_LINES, VertexFormatType.POSITION_COLOR);

        float len = 0.5f;

        addLine(buffer, matrix, 0, 0, 0, len, 0, 0, 1, 0, 0, 1);
        addLine(buffer, matrix, 0, 0, 0, 0, len, 0, 0, 1, 0, 1);
        addLine(buffer, matrix, 0, 0, 0, 0, 0, len, 0, 0, 1, 1);

        tesselator.endAndDraw(buffer);
    }

    private static void capturePlayerArmor(PlayerEntity player, BipedEntityData<?> data)
    {
        initArmorModels();

        CapturingVertexConsumer captureConsumer = new CapturingVertexConsumer();
        captureConsumer.clear();

        boolean hasAnyArmor = hasArmor(player, EquipmentSlotType.HEAD) ||
                              hasArmor(player, EquipmentSlotType.CHEST) ||
                              hasArmor(player, EquipmentSlotType.LEGS) ||
                              hasArmor(player, EquipmentSlotType.FEET);

        if (hasAnyArmor)
        {
            captureArmorSlot(player, EquipmentSlotType.HEAD, captureConsumer);
            captureArmorSlot(player, EquipmentSlotType.CHEST, captureConsumer);
            captureArmorSlot(player, EquipmentSlotType.LEGS, captureConsumer);
            captureArmorSlot(player, EquipmentSlotType.FEET, captureConsumer);
        }
        else
        {
            captureBaseArmorModel(captureConsumer);
        }

        List<CapturedVertex> vertices = captureConsumer.getVertices();
        if (!vertices.isEmpty())
        {
            lastCapturedVertices = new ArrayList<>(vertices);
            lastEntityData = data;
        }
    }

    private static void captureBaseArmorModel(CapturingVertexConsumer captureConsumer)
    {
        if (outerArmorModel == null) return;

        outerArmorModel.setAllVisible(true);

        resetToRestPose(outerArmorModel);

        MatrixStack capturePoseStack = new MatrixStack();
        IModelRenderHelper.Holder.getHelper().renderModelToBuffer(outerArmorModel, capturePoseStack, captureConsumer, 15728880,
                                       OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
    }

    private static void captureArmorSlot(PlayerEntity player, EquipmentSlotType slot, CapturingVertexConsumer captureConsumer)
    {
        ItemStack itemStack = player.getItemBySlot(slot);
        if (itemStack.isEmpty()) return;
        if (!(itemStack.getItem() instanceof ArmorItem)) return;
        ArmorItem armorItem = (ArmorItem) itemStack.getItem();
        if (armorItem.getSlot() != slot) return;

        boolean usesInnerModel = (slot == EquipmentSlotType.LEGS);
        BipedModel<PlayerEntity> armorModel = usesInnerModel ? innerArmorModel : outerArmorModel;
        if (armorModel == null) return;

        armorModel.setAllVisible(false);
        switch (slot)
        {
            case HEAD:
                armorModel.head.visible = true;
                armorModel.hat.visible = true;
                break;
            case CHEST:
                armorModel.body.visible = true;
                armorModel.rightArm.visible = true;
                armorModel.leftArm.visible = true;
                break;
            case LEGS:
                armorModel.body.visible = true;
                armorModel.rightLeg.visible = true;
                armorModel.leftLeg.visible = true;
                break;
            case FEET:
                armorModel.rightLeg.visible = true;
                armorModel.leftLeg.visible = true;
                break;
            default:
                break;
        }

        resetToRestPose(armorModel);

        MatrixStack capturePoseStack = new MatrixStack();
        IModelRenderHelper.Holder.getHelper().renderModelToBuffer(armorModel, capturePoseStack, captureConsumer,
                                  15728880, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
    }

    private static void resetToRestPose(BipedModel<?> model)
    {
        resetPart(model.head);
        resetPart(model.hat);
        resetPart(model.body);
        resetPart(model.leftArm);
        resetPart(model.rightArm);
        resetPart(model.leftLeg);
        resetPart(model.rightLeg);
    }

    private static void resetPart(ModelRenderer part)
    {
        part.xRot = 0;
        part.yRot = 0;
        part.zRot = 0;
    }

    private static void renderOriginalVertices(MatrixStack poseStack, List<CapturedVertex> vertices,
                                                Map<BoneRegion, Boolean> boneEnabled)
    {
        ArmorBoneAssignment assignment = new ArmorBoneAssignment();

        PlatformServices.get().setPositionColorShader();
        RenderSystem.lineWidth(2.0f);

        Matrix4f matrix = poseStack.last().pose();

        ITesselator tesselator = ITesselator.getInstance();
        IBufferBuilder buffer = tesselator.begin(DrawMode.DEBUG_LINES, VertexFormatType.POSITION_COLOR);

        for (int i = 0; i < vertices.size(); i += 4)
        {
            if (i + 3 >= vertices.size()) break;

            CapturedVertex v0 = vertices.get(i);
            CapturedVertex v1 = vertices.get(i + 1);
            CapturedVertex v2 = vertices.get(i + 2);
            CapturedVertex v3 = vertices.get(i + 3);

            BoneRegion region = assignment.assignVertex(v0.x, v0.y, v0.z);
            if (!boneEnabled.getOrDefault(region, true)) continue;

            float r = 0.0f, g = 1.0f, b = 0.0f, a = 1.0f;

            addLine(buffer, matrix, v0.x, v0.y, v0.z, v1.x, v1.y, v1.z, r, g, b, a);
            addLine(buffer, matrix, v1.x, v1.y, v1.z, v2.x, v2.y, v2.z, r, g, b, a);
            addLine(buffer, matrix, v2.x, v2.y, v2.z, v3.x, v3.y, v3.z, r, g, b, a);
            addLine(buffer, matrix, v3.x, v3.y, v3.z, v0.x, v0.y, v0.z, r, g, b, a);
        }

        tesselator.endAndDraw(buffer);
    }

    private static void renderTransformedVertices(MatrixStack poseStack, List<CapturedVertex> vertices,
                                                   BipedEntityData<?> data,
                                                   Map<BoneRegion, Boolean> boneEnabled)
    {
        ArmorBoneAssignment assignment = new ArmorBoneAssignment();

        java.util.Map<BoneRegion, float[]> transforms = computeTransforms(poseStack, data);
        java.util.Map<BoneRegion, float[]> restPositions = computeRestPositions(data);

        PlatformServices.get().setPositionColorShader();
        RenderSystem.lineWidth(2.0f);

        Matrix4f matrix = poseStack.last().pose();

        ITesselator tesselator = ITesselator.getInstance();
        IBufferBuilder buffer = tesselator.begin(DrawMode.DEBUG_LINES, VertexFormatType.POSITION_COLOR);

        for (int i = 0; i < vertices.size(); i += 4)
        {
            if (i + 3 >= vertices.size()) break;

            CapturedVertex v0 = vertices.get(i);
            CapturedVertex v1 = vertices.get(i + 1);
            CapturedVertex v2 = vertices.get(i + 2);
            CapturedVertex v3 = vertices.get(i + 3);

            BoneRegion region = assignment.assignVertex(v0.x, v0.y, v0.z);
            if (!boneEnabled.getOrDefault(region, true)) continue;

            float[] transform = transforms.get(region);
            float[] restPos = restPositions.get(region);
            if (transform == null || restPos == null) continue;

            float[] t0 = transformVertex(v0, transform, restPos);
            float[] t1 = transformVertex(v1, transform, restPos);
            float[] t2 = transformVertex(v2, transform, restPos);
            float[] t3 = transformVertex(v3, transform, restPos);

            float r = 1.0f, g = 0.0f, b = 0.0f, a = 1.0f;

            addLine(buffer, matrix, t0[0], t0[1], t0[2], t1[0], t1[1], t1[2], r, g, b, a);
            addLine(buffer, matrix, t1[0], t1[1], t1[2], t2[0], t2[1], t2[2], r, g, b, a);
            addLine(buffer, matrix, t2[0], t2[1], t2[2], t3[0], t3[1], t3[2], r, g, b, a);
            addLine(buffer, matrix, t3[0], t3[1], t3[2], t0[0], t0[1], t0[2], r, g, b, a);
        }

        tesselator.endAndDraw(buffer);
    }

    private static float[] transformVertex(CapturedVertex v, float[] transform, float[] restPos)
    {
        float localX = v.x - restPos[0];
        float localY = v.y - restPos[1];
        float localZ = v.z - restPos[2];

        float tx = transform[0] * localX + transform[1] * localY + transform[2] * localZ + transform[9];
        float ty = transform[3] * localX + transform[4] * localY + transform[5] * localZ + transform[10];
        float tz = transform[6] * localX + transform[7] * localY + transform[8] * localZ + transform[11];

        return new float[]{tx, ty, tz};
    }

    private static void renderBoneRegions(MatrixStack poseStack, Map<BoneRegion, Boolean> boneEnabled)
    {
        PlatformServices.get().setPositionColorShader();
        RenderSystem.lineWidth(1.0f);

        Matrix4f matrix = poseStack.last().pose();

        ITesselator tesselator = ITesselator.getInstance();
        IBufferBuilder buffer = tesselator.begin(DrawMode.DEBUG_LINES, VertexFormatType.POSITION_COLOR);

        float a = 0.8f;

        if (boneEnabled.getOrDefault(BoneRegion.HEAD, true))
            drawBox(buffer, matrix, -4, -8, -4, 4, 0, 4, 0.5f, 0.5f, 1.0f, a);

        if (boneEnabled.getOrDefault(BoneRegion.BODY, true))
            drawBox(buffer, matrix, -4, 0, -2, 4, 12, 2, 1.0f, 0.5f, 0.0f, a);

        if (boneEnabled.getOrDefault(BoneRegion.LEFT_ARM_UPPER, true))
            drawBox(buffer, matrix, 4, 0, -2, 8, 6, 2, 0.0f, 1.0f, 0.5f, a);
        if (boneEnabled.getOrDefault(BoneRegion.LEFT_ARM_LOWER, true))
            drawBox(buffer, matrix, 4, 6, -2, 8, 12, 2, 0.0f, 0.7f, 0.3f, a);

        if (boneEnabled.getOrDefault(BoneRegion.RIGHT_ARM_UPPER, true))
            drawBox(buffer, matrix, -8, 0, -2, -4, 6, 2, 1.0f, 1.0f, 0.0f, a);
        if (boneEnabled.getOrDefault(BoneRegion.RIGHT_ARM_LOWER, true))
            drawBox(buffer, matrix, -8, 6, -2, -4, 12, 2, 0.7f, 0.7f, 0.0f, a);

        if (boneEnabled.getOrDefault(BoneRegion.LEFT_LEG_UPPER, true))
            drawBox(buffer, matrix, -0.1f, 12, -2, 3.9f, 18, 2, 1.0f, 0.0f, 1.0f, a);
        if (boneEnabled.getOrDefault(BoneRegion.LEFT_LEG_LOWER, true))
            drawBox(buffer, matrix, -0.1f, 18, -2, 3.9f, 24, 2, 0.7f, 0.0f, 0.7f, a);

        if (boneEnabled.getOrDefault(BoneRegion.RIGHT_LEG_UPPER, true))
            drawBox(buffer, matrix, -3.9f, 12, -2, 0.1f, 18, 2, 0.0f, 1.0f, 1.0f, a);
        if (boneEnabled.getOrDefault(BoneRegion.RIGHT_LEG_LOWER, true))
            drawBox(buffer, matrix, -3.9f, 18, -2, 0.1f, 24, 2, 0.0f, 0.7f, 0.7f, a);

        tesselator.endAndDraw(buffer);
    }

    private static void drawBox(IBufferBuilder buffer, Matrix4f matrix,
                                 float minX, float minY, float minZ,
                                 float maxX, float maxY, float maxZ,
                                 float r, float g, float b, float a)
    {
        minX *= SCALE;
        minY *= SCALE;
        minZ *= SCALE;
        maxX *= SCALE;
        maxY *= SCALE;
        maxZ *= SCALE;

        addLine(buffer, matrix, minX, minY, minZ, maxX, minY, minZ, r, g, b, a);
        addLine(buffer, matrix, maxX, minY, minZ, maxX, minY, maxZ, r, g, b, a);
        addLine(buffer, matrix, maxX, minY, maxZ, minX, minY, maxZ, r, g, b, a);
        addLine(buffer, matrix, minX, minY, maxZ, minX, minY, minZ, r, g, b, a);

        addLine(buffer, matrix, minX, maxY, minZ, maxX, maxY, minZ, r, g, b, a);
        addLine(buffer, matrix, maxX, maxY, minZ, maxX, maxY, maxZ, r, g, b, a);
        addLine(buffer, matrix, maxX, maxY, maxZ, minX, maxY, maxZ, r, g, b, a);
        addLine(buffer, matrix, minX, maxY, maxZ, minX, maxY, minZ, r, g, b, a);

        addLine(buffer, matrix, minX, minY, minZ, minX, maxY, minZ, r, g, b, a);
        addLine(buffer, matrix, maxX, minY, minZ, maxX, maxY, minZ, r, g, b, a);
        addLine(buffer, matrix, maxX, minY, maxZ, maxX, maxY, maxZ, r, g, b, a);
        addLine(buffer, matrix, minX, minY, maxZ, minX, maxY, maxZ, r, g, b, a);
    }

    private static void renderBoneAxes(MatrixStack poseStack, BipedEntityData<?> data,
                                        Map<BoneRegion, Boolean> boneEnabled)
    {
        PlatformServices.get().setPositionColorShader();
        RenderSystem.lineWidth(3.0f);

        ITesselator tesselator = ITesselator.getInstance();
        IBufferBuilder buffer = tesselator.begin(DrawMode.DEBUG_LINES, VertexFormatType.POSITION_COLOR);

        float axisLength = 0.15f;

        for (BoneRegion region : BoneRegion.values())
        {
            if (!boneEnabled.getOrDefault(region, true)) continue;

            IModelPart part = getBoneModelPart(region, data);
            if (part == null && region != BoneRegion.ROOT) continue;

            poseStack.pushPose();

            if (part != null)
            {
                part.applyCharacterTransform(poseStack, SCALE);
            }

            Matrix4f matrix = poseStack.last().pose();

            addLine(buffer, matrix, 0, 0, 0, axisLength, 0, 0, 1, 0, 0, 1);
            addLine(buffer, matrix, 0, 0, 0, 0, axisLength, 0, 0, 1, 0, 1);
            addLine(buffer, matrix, 0, 0, 0, 0, 0, axisLength, 0, 0, 1, 1);

            poseStack.popPose();
        }

        tesselator.endAndDraw(buffer);
    }

    private static void addLine(IBufferBuilder buffer, Matrix4f matrix,
                                 float x1, float y1, float z1,
                                 float x2, float y2, float z2,
                                 float r, float g, float b, float a)
    {
        Vector4f p1 = new Vector4f(x1, y1, z1, 1.0F);
        p1.transform(matrix);
        Vector4f p2 = new Vector4f(x2, y2, z2, 1.0F);
        p2.transform(matrix);

        buffer.addVertex(p1.x(), p1.y(), p1.z()).setColor(r, g, b, a);
        buffer.addVertex(p2.x(), p2.y(), p2.z()).setColor(r, g, b, a);
    }

    private static IModelPart getBoneModelPart(BoneRegion region, BipedEntityData<?> data)
    {
        switch (region)
        {
            case HEAD: return data.head;
            case BODY: return data.body;
            case LEFT_ARM_UPPER: return data.leftArm;
            case LEFT_ARM_LOWER: return data.leftForeArm;
            case RIGHT_ARM_UPPER: return data.rightArm;
            case RIGHT_ARM_LOWER: return data.rightForeArm;
            case LEFT_LEG_UPPER: return data.leftLeg;
            case LEFT_LEG_LOWER: return data.leftForeLeg;
            case RIGHT_LEG_UPPER: return data.rightLeg;
            case RIGHT_LEG_LOWER: return data.rightForeLeg;
            case ROOT:
            default: return null;
        }
    }

    private static java.util.Map<BoneRegion, float[]> computeTransforms(MatrixStack poseStack, BipedEntityData<?> data)
    {
        java.util.Map<BoneRegion, float[]> transforms = new java.util.EnumMap<>(BoneRegion.class);

        for (BoneRegion region : BoneRegion.values())
        {
            IModelPart part = getBoneModelPart(region, data);
            if (part == null && region != BoneRegion.ROOT) continue;

            poseStack.pushPose();

            if (part != null)
            {
                part.applyCharacterTransform(poseStack, SCALE);
            }

            Matrix4f matrix = poseStack.last().pose();

            float[] t = new float[12];
            t[0] = matrix.m00; t[1] = matrix.m01; t[2] = matrix.m02;
            t[3] = matrix.m10; t[4] = matrix.m11; t[5] = matrix.m12;
            t[6] = matrix.m20; t[7] = matrix.m21; t[8] = matrix.m22;
            t[9] = matrix.m03; t[10] = matrix.m13; t[11] = matrix.m23;

            transforms.put(region, t);

            poseStack.popPose();
        }

        return transforms;
    }

    private static java.util.Map<BoneRegion, float[]> computeRestPositions(BipedEntityData<?> data)
    {
        java.util.Map<BoneRegion, float[]> positions = new java.util.EnumMap<>(BoneRegion.class);

        positions.put(BoneRegion.HEAD, new float[]{0, 0, 0});
        positions.put(BoneRegion.BODY, new float[]{0, 0, 0});

        positions.put(BoneRegion.LEFT_ARM_UPPER, new float[]{5 * SCALE, 2 * SCALE, 0});
        positions.put(BoneRegion.LEFT_ARM_LOWER, new float[]{5 * SCALE, 8 * SCALE, 0});

        positions.put(BoneRegion.RIGHT_ARM_UPPER, new float[]{-5 * SCALE, 2 * SCALE, 0});
        positions.put(BoneRegion.RIGHT_ARM_LOWER, new float[]{-5 * SCALE, 8 * SCALE, 0});

        positions.put(BoneRegion.LEFT_LEG_UPPER, new float[]{1.9f * SCALE, 12 * SCALE, 0});
        positions.put(BoneRegion.LEFT_LEG_LOWER, new float[]{1.9f * SCALE, 18 * SCALE, 0});

        positions.put(BoneRegion.RIGHT_LEG_UPPER, new float[]{-1.9f * SCALE, 12 * SCALE, 0});
        positions.put(BoneRegion.RIGHT_LEG_LOWER, new float[]{-1.9f * SCALE, 18 * SCALE, 0});

        positions.put(BoneRegion.ROOT, new float[]{0, 0, 0});

        return positions;
    }
}
