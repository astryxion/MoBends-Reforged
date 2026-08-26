package goblinbob.mobends.standard.client.renderer.entity.layers;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.math.vector.Vector3f;
import goblinbob.mobends.core.client.model.BendsCube;
import goblinbob.mobends.core.client.model.BendsModelPart;
import goblinbob.mobends.core.data.EntityData;
import goblinbob.mobends.core.data.EntityDatabase;
import goblinbob.mobends.lib.math.SmoothOrientation;
import goblinbob.mobends.core.util.GlHelper;
import goblinbob.mobends.standard.data.BipedEntityData;
import goblinbob.mobends.standard.mutators.BipedMutator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.HandSide;
import net.minecraft.entity.LivingEntity;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ResourceLocation;

public class LayerCustomHeldItem<E extends LivingEntity, M extends net.minecraft.client.renderer.entity.model.EntityModel<E>> extends LayerRenderer<E, M>
{

    private final BipedMutator<?, E, M> mutator;

    public LayerCustomHeldItem(LivingRenderer<E, M> renderer, BipedMutator<?, E, M> mutator)
    {
        super(renderer);
        this.mutator = mutator;
    }

    @Override
    public void render(MatrixStack poseStack, IRenderTypeBuffer bufferSource, int packedLight,
                       E entity, float limbSwing, float limbSwingAmount,
                       float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
    {
        if (goblinbob.mobends.compat.FirstPersonModelCompat.isRenderingFirstPersonBody(entity)
                && this.getParentModel() instanceof BipedModel<?>)
        {
            BipedModel<?> humanoidParent = (BipedModel<?>) this.getParentModel();
            if (goblinbob.mobends.compat.FirstPersonModelCompat.showsVanillaHands(humanoidParent))
            {
                return;
            }
        }

        boolean rightHanded = entity.getMainArm() == HandSide.RIGHT;
        ItemStack mainHandItem = rightHanded ? entity.getMainHandItem() : entity.getOffhandItem();
        ItemStack offHandItem = rightHanded ? entity.getOffhandItem() : entity.getMainHandItem();

        if (!mainHandItem.isEmpty() || !offHandItem.isEmpty())
        {
            poseStack.pushPose();

            if (this.getParentModel().young && !this.isDrivenByMoBends(entity))
            {
                poseStack.translate(0.0F, 0.75F, 0.0F);
                poseStack.scale(0.5F, 0.5F, 0.5F);
            }

            this.renderHeldItem(entity, mainHandItem, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND,
                    HandSide.RIGHT, poseStack, bufferSource, packedLight);
            this.renderHeldItem(entity, offHandItem, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND,
                    HandSide.LEFT, poseStack, bufferSource, packedLight);

            poseStack.popPose();
        }
    }

    private void renderHeldItem(E entity, ItemStack itemStack, ItemCameraTransforms.TransformType displayContext,
                                HandSide arm, MatrixStack poseStack,
                                IRenderTypeBuffer bufferSource, int packedLight)
    {
        if (!itemStack.isEmpty())
        {
            if (goblinbob.mobends.compat.NotEnoughAnimationsCompat.renderHeldItem(
                    entity, this.getParentModel(), itemStack, arm, poseStack, bufferSource, packedLight))
            {
                return;
            }

            if (this.renderSpyglassOnHead(entity, itemStack, arm, poseStack, bufferSource, packedLight))
            {
                return;
            }

            poseStack.pushPose();

            this.translateToHand(arm, entity, poseStack);

            poseStack.mulPose(Vector3f.XP.rotationDegrees(-90.0F));
            poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));

            boolean leftHanded = arm == HandSide.LEFT;
            this.translateToGrip(arm, entity, itemStack, displayContext, poseStack);

            Minecraft.getInstance().getItemRenderer().renderStatic(
                    entity, itemStack, displayContext, leftHanded,
                    poseStack, bufferSource, entity.level, packedLight,
                    LivingRenderer.getOverlayCoords(entity, 0.0F));

            poseStack.popPose();
        }
    }

    private boolean renderSpyglassOnHead(E entity, ItemStack itemStack, HandSide arm,
                                         MatrixStack poseStack, IRenderTypeBuffer bufferSource, int packedLight)
    {
        if (!isSpyglassItem(itemStack)
                || entity.getUseItem() != itemStack
                || entity.swingTime != 0)
        {
            return false;
        }

        if (mutator == null || !mutator.shouldRenderCustom())
        {
            return false;
        }

        final BendsModelPart body = mutator.getBody();
        final BendsModelPart head = mutator.getHead();
        if (body == null || head == null)
        {
            return false;
        }

        final float scale = 1.0F / 16.0F;

        poseStack.pushPose();

        poseStack.translate(body.position.x * scale, body.position.y * scale, body.position.z * scale);
        GlHelper.rotate(poseStack, body.rotation.getSmooth());
        poseStack.translate(head.position.x * scale, head.position.y * scale, head.position.z * scale);
        GlHelper.rotate(poseStack, head.rotation.getSmooth());

        poseStack.translate(0.0F, -0.25F, 0.0F);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        poseStack.scale(0.625F, -0.625F, -0.625F);

        poseStack.translate((arm == HandSide.LEFT ? -2.5F : 2.5F) / 16.0F, -0.0625F, 0.0F);

        Minecraft.getInstance().getItemRenderer().renderStatic(
                entity, itemStack, ItemCameraTransforms.TransformType.HEAD, false,
                poseStack, bufferSource, entity.level, packedLight,
                LivingRenderer.getOverlayCoords(entity, 0.0F));

        poseStack.popPose();
        return true;
    }

    protected void translateToGrip(HandSide arm, E entity, ItemStack itemStack,
                                   ItemCameraTransforms.TransformType displayContext, MatrixStack poseStack)
    {
        float vanillaGripX = arm == HandSide.LEFT ? 1.0F : -1.0F;
        BendsModelPart foreArm = this.getCustomForeArm(arm);

        if (foreArm != null && !foreArm.getCubes().isEmpty())
        {
            float minX = Float.POSITIVE_INFINITY;
            float maxX = Float.NEGATIVE_INFINITY;

            for (BendsCube cube : foreArm.getCubes())
            {
                minX = Math.min(minX, cube.minX);
                maxX = Math.max(maxX, cube.maxX);
            }

            float centredGripX = (minX + maxX) * 0.5F;
            float ownOffset = Math.abs(this.getDisplayOffsetX(entity, itemStack, displayContext));
            float blend = 1.0F - Math.min(1.0F, ownOffset);
            float gripX = vanillaGripX + (centredGripX - vanillaGripX) * blend;

            poseStack.translate(-gripX / 16.0F, 0.125F, -0.625F);
            return;
        }

        poseStack.translate(-vanillaGripX / 16.0F, 0.125F, -0.625F);
    }

    private float getDisplayOffsetX(E entity, ItemStack itemStack, ItemCameraTransforms.TransformType displayContext)
    {
        try
        {
            IBakedModel model = Minecraft.getInstance().getItemRenderer()
                    .getModel(itemStack, entity.level, entity);
            return model.getTransforms().getTransform(displayContext).translation.x() * 16.0F;
        }
        catch (Exception e)
        {
            return 0.0F;
        }
    }

    private boolean isDrivenByMoBends(E entity)
    {
        return goblinbob.mobends.core.util.BenderHelper.isEntityAnimated(entity)
                && !goblinbob.mobends.compat.ModCompatManager.shouldDeferAnimation(entity);
    }

    private BendsModelPart getCustomForeArm(HandSide arm)
    {
        if (mutator == null || !mutator.shouldRenderCustom() || mutator.getBody() == null)
        {
            return null;
        }

        BendsModelPart armPart = arm == HandSide.RIGHT ? mutator.getRightArm() : mutator.getLeftArm();
        BendsModelPart foreArm = arm == HandSide.RIGHT ? mutator.getRightForeArm() : mutator.getLeftForeArm();

        return armPart != null && foreArm != null ? foreArm : null;
    }

    protected void translateToHand(HandSide arm, E entity, MatrixStack poseStack)
    {
        BendsModelPart foreArm = this.isDrivenByMoBends(entity) ? this.getCustomForeArm(arm) : null;

        if (foreArm != null)
        {
            BendsModelPart body = mutator.getBody();
            BendsModelPart armPart = arm == HandSide.RIGHT ? mutator.getRightArm() : mutator.getLeftArm();

            float scale = 1.0F / 16.0F;

            poseStack.translate(body.position.x * scale, body.position.y * scale, body.position.z * scale);
            if (body.offset.x != 0 || body.offset.y != 0 || body.offset.z != 0)
            {
                poseStack.translate(body.offset.x * scale, body.offset.y * scale, body.offset.z * scale);
            }
            GlHelper.rotate(poseStack, body.rotation.getSmooth());

            poseStack.translate(armPart.position.x * scale, armPart.position.y * scale, armPart.position.z * scale);
            if (armPart.offset.x != 0 || armPart.offset.y != 0 || armPart.offset.z != 0)
            {
                poseStack.translate(armPart.offset.x * scale, armPart.offset.y * scale, armPart.offset.z * scale);
            }
            GlHelper.rotate(poseStack, armPart.rotation.getSmooth());

            poseStack.translate(foreArm.position.x * scale, foreArm.position.y * scale, foreArm.position.z * scale);
            if (foreArm.offset.x != 0 || foreArm.offset.y != 0 || foreArm.offset.z != 0)
            {
                poseStack.translate(foreArm.offset.x * scale, foreArm.offset.y * scale, foreArm.offset.z * scale);
            }
            GlHelper.rotate(poseStack, foreArm.rotation.getSmooth());

            poseStack.translate(0, -4.0F * scale, -2.0F * scale);

            EntityData<?> entityData = EntityDatabase.instance.get(entity);
            if (entityData instanceof BipedEntityData<?>) {
            BipedEntityData<?> bipedData = (BipedEntityData<?>) entityData;
                SmoothOrientation itemRotation = arm == HandSide.RIGHT
                        ? bipedData.renderRightItemRotation
                        : bipedData.renderLeftItemRotation;

                poseStack.translate(0, 8.0F * scale, 0);
                GlHelper.rotate(poseStack, itemRotation.getSmooth());
                poseStack.translate(0, -8.0F * scale, 0);
            }

            return;
        }

        M model = this.getParentModel();
        ((IHasArm) model).translateToHand(arm, poseStack);

        EntityData<?> entityData = EntityDatabase.instance.get(entity);
        if (entityData instanceof BipedEntityData<?> && this.isDrivenByMoBends(entity))
        {
            BipedEntityData<?> bipedData = (BipedEntityData<?>) entityData;
            SmoothOrientation itemRotation = arm == HandSide.RIGHT
                    ? bipedData.renderRightItemRotation
                    : bipedData.renderLeftItemRotation;

            poseStack.translate(0, 8F * 0.0625F, 0);
            GlHelper.rotate(poseStack, itemRotation.getSmooth());
            poseStack.translate(0, -8F * 0.0625F, 0);
        }
    }

    private static boolean isSpyglassItem(ItemStack itemStack)
    {
        ResourceLocation name = itemStack.getItem().getRegistryName();
        return name != null && "minecraft:spyglass".equals(name.toString());
    }
}
