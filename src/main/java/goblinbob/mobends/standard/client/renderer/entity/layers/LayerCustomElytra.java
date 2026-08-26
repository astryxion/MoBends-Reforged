package goblinbob.mobends.standard.client.renderer.entity.layers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import goblinbob.mobends.api.player.IPlayerSkinProvider;
import goblinbob.mobends.api.rendering.IModelRenderHelper;
import goblinbob.mobends.core.data.EntityData;
import goblinbob.mobends.core.data.EntityDatabase;
import goblinbob.mobends.standard.data.PlayerData;
import net.minecraft.client.renderer.entity.model.ElytraModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class LayerCustomElytra extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>
{
    private static final ResourceLocation TEXTURE_ELYTRA = goblinbob.mobends.core.util.ResourceLocationFactory.parse("textures/entity/elytra.png");
    private final ElytraModel<AbstractClientPlayerEntity> elytraModel;

    public LayerCustomElytra(IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> renderer)
    {
        super(renderer);
        this.elytraModel = new ElytraModel<>();
    }

    @Override
    public void render(MatrixStack poseStack, IRenderTypeBuffer bufferSource, int packedLight,
                       AbstractClientPlayerEntity player, float limbSwing, float limbSwingAmount,
                       float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
    {
        if (goblinbob.mobends.compat.FirstPersonModelCompat.isRenderingFirstPersonBody(player) && player.isSwimming())
            return;

        final EntityData<?> entityData = EntityDatabase.instance.get(player);
        if (!(entityData instanceof PlayerData))
            return;

        final PlayerData data = (PlayerData) entityData;
        final float scale = 0.0625F;

        ItemStack itemstack = player.getItemBySlot(EquipmentSlotType.CHEST);

        if (itemstack.getItem() == Items.ELYTRA)
        {
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            IPlayerSkinProvider skinProvider = IPlayerSkinProvider.Holder.getProvider();
            ResourceLocation texture;
            ResourceLocation elytraTexture = skinProvider != null ? (ResourceLocation) skinProvider.getElytraTexture(player) : null;
            ResourceLocation capeTexture = skinProvider != null ? (ResourceLocation) skinProvider.getCapeTexture(player) : null;
            if (elytraTexture != null)
            {
                texture = elytraTexture;
            }
            else if (capeTexture != null && player.isModelPartShown(PlayerModelPart.CAPE))
            {
                texture = capeTexture;
            }
            else
            {
                texture = TEXTURE_ELYTRA;
            }

            poseStack.pushPose();
            data.body.applyCharacterTransform(poseStack, 0.0625F);
            poseStack.translate(0.0F, -12.0F * scale, 0.0F);

            this.elytraModel.young = player.isBaby();
            this.elytraModel.riding = false;
            this.elytraModel.attackTime = 0.0F;
            this.elytraModel.setupAnim(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            IModelRenderHelper renderHelper = IModelRenderHelper.Holder.getHelper();
            IVertexBuilder vertexConsumer = (IVertexBuilder) renderHelper.getArmorFoilBuffer(
                    bufferSource, RenderType.armorCutoutNoCull(texture), itemstack.hasFoil());
            renderHelper.renderModelToBuffer(this.elytraModel, poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY,
                    0xFFFFFFFF);

            RenderSystem.disableBlend();
            poseStack.popPose();
        }
    }
}
