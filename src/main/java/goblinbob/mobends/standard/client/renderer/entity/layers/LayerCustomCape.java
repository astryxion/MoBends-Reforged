package goblinbob.mobends.standard.client.renderer.entity.layers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.math.vector.Vector3f;
import goblinbob.mobends.api.player.IPlayerSkinProvider;
import goblinbob.mobends.core.data.EntityData;
import goblinbob.mobends.core.data.EntityDatabase;
import goblinbob.mobends.standard.client.renderer.entity.BendsCapeRenderer;
import goblinbob.mobends.standard.data.PlayerData;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class LayerCustomCape extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>
{

    private final BendsCapeRenderer capeRenderer;

    public LayerCustomCape(IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> renderer)
    {
        super(renderer);
        this.capeRenderer = new BendsCapeRenderer();
    }

    @Override
    public void render(MatrixStack poseStack, IRenderTypeBuffer bufferSource, int packedLight,
                       AbstractClientPlayerEntity player, float limbSwing, float limbSwingAmount,
                       float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
    {
        final EntityData<?> entityData = EntityDatabase.instance.get(player);
        if (!(entityData instanceof PlayerData))
            return;

        final PlayerData data = (PlayerData) entityData;
        final float scale = 0.0625F;

        IPlayerSkinProvider skinProvider = IPlayerSkinProvider.Holder.getProvider();
        ResourceLocation capeTexture = skinProvider != null ? (ResourceLocation) skinProvider.getCapeTexture(player) : null;
        if (capeTexture != null && !player.isInvisible() && player.isModelPartShown(PlayerModelPart.CAPE))
        {
            final ItemStack itemstack = player.getItemBySlot(EquipmentSlotType.CHEST);

            if (itemstack.getItem() != Items.ELYTRA)
            {
                RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                poseStack.pushPose();

                data.body.applyLocalTransform(poseStack, 0.0625F);
                poseStack.translate(0.0F, -12.0F * scale, 2.2F * scale);
                data.cape.applyLocalTransform(poseStack, 0.0625F);
                poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));

                capeRenderer.applyAnimation(data);
                capeRenderer.render(poseStack, bufferSource, packedLight, player, 0.0625F);

                poseStack.popPose();
            }
        }
    }
}
