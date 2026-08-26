package goblinbob.mobends.standard.client.gui;


import net.minecraft.util.text.StringTextComponent;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import goblinbob.mobends.core.data.EntityData;
import goblinbob.mobends.core.data.EntityDatabase;
import goblinbob.mobends.core.util.ScreenHelper;
import goblinbob.mobends.standard.client.model.armor.ArmorDebugRenderer;
import goblinbob.mobends.standard.client.model.armor.BoneRegion;
import goblinbob.mobends.standard.data.BipedEntityData;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3f;

import java.util.EnumMap;
import java.util.Map;

public class ArmorDebugScreen extends Screen
{
    private static boolean showOriginalVertices = true;
    private static boolean showTransformedVertices = true;
    private static boolean showBoneRegions = true;
    private static boolean showBoneAxes = true;
    private static boolean showTestGeometry = false;
    private static boolean freezeAnimation = false;
    private static float debugScale = 50.0f;
    private static float rotationY = 0.0f;
    private static float rotationX = 20.0f;

    private static final Map<BoneRegion, Boolean> boneEnabled = new EnumMap<>(BoneRegion.class);
    static {
        for (BoneRegion region : BoneRegion.values()) {
            boneEnabled.put(region, true);
        }
    }

    private int selectedBone = 0;
    private boolean dragging = false;
    private double lastMouseX, lastMouseY;

    public ArmorDebugScreen()
    {
        super(new StringTextComponent("Armor Debug"));
    }

    @Override
    protected void init()
    {
        super.init();

        int buttonWidth = 120;
        int buttonHeight = 20;
        int x = 10;
        int y = 30;
        int spacing = 22;

        addButton(new Button(x, y, buttonWidth, buttonHeight,
                new StringTextComponent("Original: " + (showOriginalVertices ? "ON" : "OFF")),
                btn -> {
                    showOriginalVertices = !showOriginalVertices;
                    btn.setMessage(new StringTextComponent("Original: " + (showOriginalVertices ? "ON" : "OFF")));
                }));
        y += spacing;

        addButton(new Button(x, y, buttonWidth, buttonHeight,
                new StringTextComponent("Transformed: " + (showTransformedVertices ? "ON" : "OFF")),
                btn -> {
                    showTransformedVertices = !showTransformedVertices;
                    btn.setMessage(new StringTextComponent("Transformed: " + (showTransformedVertices ? "ON" : "OFF")));
                }));
        y += spacing;

        addButton(new Button(x, y, buttonWidth, buttonHeight,
                new StringTextComponent("Bone Regions: " + (showBoneRegions ? "ON" : "OFF")),
                btn -> {
                    showBoneRegions = !showBoneRegions;
                    btn.setMessage(new StringTextComponent("Bone Regions: " + (showBoneRegions ? "ON" : "OFF")));
                }));
        y += spacing;

        addButton(new Button(x, y, buttonWidth, buttonHeight,
                new StringTextComponent("Bone Axes: " + (showBoneAxes ? "ON" : "OFF")),
                btn -> {
                    showBoneAxes = !showBoneAxes;
                    btn.setMessage(new StringTextComponent("Bone Axes: " + (showBoneAxes ? "ON" : "OFF")));
                }));
        y += spacing;

        addButton(new Button(x, y, buttonWidth, buttonHeight,
                new StringTextComponent("Freeze: " + (freezeAnimation ? "ON" : "OFF")),
                btn -> {
                    freezeAnimation = !freezeAnimation;
                    btn.setMessage(new StringTextComponent("Freeze: " + (freezeAnimation ? "ON" : "OFF")));
                }));
        y += spacing;

        addButton(new Button(x, y, buttonWidth, buttonHeight,
                new StringTextComponent("Test Geo: " + (showTestGeometry ? "ON" : "OFF")),
                btn -> {
                    showTestGeometry = !showTestGeometry;
                    btn.setMessage(new StringTextComponent("Test Geo: " + (showTestGeometry ? "ON" : "OFF")));
                }));
        y += spacing;

        addButton(new Button(x, y, 55, buttonHeight,
                new StringTextComponent("Scale -"),
                btn -> debugScale = Math.max(10, debugScale - 10)));

        addButton(new Button(x + 60, y, 55, buttonHeight,
                new StringTextComponent("Scale +"),
                btn -> debugScale = Math.min(200, debugScale + 10)));
        y += spacing;

        addButton(new Button(x, y, buttonWidth, buttonHeight,
                new StringTextComponent("Reset View"),
                btn -> {
                    rotationX = 20.0f;
                    rotationY = 0.0f;
                    debugScale = 50.0f;
                }));
        y += spacing + 10;

        BoneRegion[] regions = BoneRegion.values();
        for (int i = 0; i < regions.length; i++) {
            BoneRegion region = regions[i];
            int finalY = y;
            addButton(new Button(x, finalY, buttonWidth, buttonHeight,
                    new StringTextComponent(getShortBoneName(region) + ": " + (boneEnabled.get(region) ? "ON" : "OFF")),
                    btn -> {
                        boneEnabled.put(region, !boneEnabled.get(region));
                        btn.setMessage(new StringTextComponent(getShortBoneName(region) + ": " + (boneEnabled.get(region) ? "ON" : "OFF")));
                    }));
            y += spacing;
        }
    }

    private String getShortBoneName(BoneRegion region) {
        switch (region) {
            case HEAD: return "Head";
            case BODY: return "Body";
            case LEFT_ARM_UPPER: return "L.Arm Up";
            case LEFT_ARM_LOWER: return "L.Arm Low";
            case RIGHT_ARM_UPPER: return "R.Arm Up";
            case RIGHT_ARM_LOWER: return "R.Arm Low";
            case LEFT_LEG_UPPER: return "L.Leg Up";
            case LEFT_LEG_LOWER: return "L.Leg Low";
            case RIGHT_LEG_UPPER: return "R.Leg Up";
            case RIGHT_LEG_LOWER: return "R.Leg Low";
            case ROOT: return "Root";
            default: return region.name();
        }
    }

    @Override
    public void render(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks)
    {
        ScreenHelper.renderBackground(this, poseStack, mouseX, mouseY, partialTicks);

        AbstractGui.drawCenteredString(poseStack, font, "Armor Debug - Drag to rotate", width / 2, 10, 0xFFFFFF);

        int previewX = width / 2 + 50;
        int previewY = height / 2 + 50;

        renderArmorPreview(poseStack, previewX, previewY, debugScale, partialTicks);

        int infoX = width - 200;
        int infoY = 30;
        font.drawShadow(poseStack, "Scale: " + (int)debugScale, infoX, infoY, 0xAAAAAA);
        infoY += 12;
        font.drawShadow(poseStack, "Rotation Y: " + (int)rotationY, infoX, infoY, 0xAAAAAA);
        infoY += 12;
        font.drawShadow(poseStack, "Rotation X: " + (int)rotationX, infoX, infoY, 0xAAAAAA);
        infoY += 12;

        int vertexCount = ArmorDebugRenderer.getCapturedVertexCount();
        int vertexColor = vertexCount > 0 ? 0x00FF00 : 0xFF0000;
        font.drawShadow(poseStack, "Vertices: " + vertexCount, infoX, infoY, vertexColor);
        infoY += 12;

        String armorInfo = ArmorDebugRenderer.getArmorSlotInfo();
        font.drawShadow(poseStack, "Armor: " + armorInfo, infoX, infoY, 0xAAAAAA);
        infoY += 12;

        String captureStatus = ArmorDebugRenderer.getCaptureStatus();
        font.drawShadow(poseStack, captureStatus, infoX, infoY, 0xAAAAAA);
        infoY += 20;

        font.drawShadow(poseStack, "Legend:", infoX, infoY, 0xFFFFFF);
        infoY += 12;
        font.drawShadow(poseStack, "Green = Original", infoX, infoY, 0x00FF00);
        infoY += 12;
        font.drawShadow(poseStack, "Red = Transformed", infoX, infoY, 0xFF0000);
        infoY += 12;
        font.drawShadow(poseStack, "Blue boxes = Bone regions", infoX, infoY, 0x4444FF);

        super.render(poseStack, mouseX, mouseY, partialTicks);
    }

    private void renderArmorPreview(MatrixStack poseStack, int x, int y, float scale, float partialTicks)
    {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        if (player == null) return;

        EntityData<?> entityData = EntityDatabase.instance.get(player);
        if (!(entityData instanceof BipedEntityData<?>)) return;

        BipedEntityData<?> data = (BipedEntityData<?>) entityData;

        poseStack.pushPose();

        poseStack.translate(x, y, 100);
        poseStack.scale(scale, scale, scale);

        poseStack.mulPose(Vector3f.XP.rotation((float) Math.toRadians(rotationX)));
        poseStack.mulPose(Vector3f.YP.rotation((float) Math.toRadians(rotationY)));

        poseStack.translate(0, -0.5f, 0);

        RenderSystem.enableDepthTest();

        ArmorDebugRenderer.renderDebug(
                poseStack,
                data,
                showOriginalVertices,
                showTransformedVertices,
                showBoneRegions,
                showBoneAxes,
                showTestGeometry,
                boneEnabled
        );

        RenderSystem.disableDepthTest();

        poseStack.popPose();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        if (button == 0 && mouseX > 140) {
            dragging = true;
            lastMouseX = mouseX;
            lastMouseY = mouseY;
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button)
    {
        if (button == 0) {
            dragging = false;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY)
    {
        if (dragging) {
            rotationY += (float) (mouseX - lastMouseX) * 0.5f;
            rotationX += (float) (mouseY - lastMouseY) * 0.5f;
            rotationX = Math.max(-90, Math.min(90, rotationX));
            lastMouseX = mouseX;
            lastMouseY = mouseY;
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY)
    {
        return handleMouseScrolled(mouseX, mouseY, scrollY);
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double scrollY)
    {
        return handleMouseScrolled(mouseX, mouseY, scrollY);
    }

    private boolean handleMouseScrolled(double mouseX, double mouseY, double scrollY)
    {
        debugScale += (float) scrollY * 5;
        debugScale = Math.max(10, Math.min(200, debugScale));
        return true;
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }

    protected void renderBlurredBackground(float partialTick)
    {
    }

    public static boolean isFreezeAnimation() {
        return freezeAnimation;
    }

    public static boolean isBoneEnabled(BoneRegion region) {
        return boneEnabled.getOrDefault(region, true);
    }

    public static boolean isShowTestGeometry() {
        return showTestGeometry;
    }
}
