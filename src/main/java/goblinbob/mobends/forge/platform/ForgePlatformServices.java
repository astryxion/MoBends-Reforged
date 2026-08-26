package goblinbob.mobends.forge.platform;

import com.mojang.blaze3d.matrix.MatrixStack;
import goblinbob.mobends.api.entity.IEntity;
import goblinbob.mobends.api.entity.IItemStack;
import goblinbob.mobends.api.entity.ILivingEntity;
import goblinbob.mobends.api.entity.IPlayer;
import com.mojang.blaze3d.systems.RenderSystem;
import goblinbob.mobends.api.platform.IPlatformServices;
import goblinbob.mobends.platform.armor.ArmorModelProviderHolder;
import goblinbob.mobends.platform.armor.IArmorTextureProvider;
import goblinbob.mobends.api.rendering.IArmorColorProvider;
import goblinbob.mobends.api.rendering.IArmorHelper;
import goblinbob.mobends.api.rendering.IArmorLayerProvider;
import goblinbob.mobends.api.rendering.IBufferSource;
import goblinbob.mobends.api.rendering.IEntityVertexHelper;
import goblinbob.mobends.api.rendering.IModelRenderHelper;
import goblinbob.mobends.api.rendering.IPoseStack;
import goblinbob.mobends.api.rendering.IRenderLayerProvider;
import goblinbob.mobends.api.rendering.ITesselator;
import goblinbob.mobends.api.resource.ILocalization;
import goblinbob.mobends.api.resource.IResourceManager;
import goblinbob.mobends.api.resource.IResourcePath;
import goblinbob.mobends.platform.McEntity;
import goblinbob.mobends.platform.McItemStack;
import goblinbob.mobends.platform.McLivingEntity;
import goblinbob.mobends.platform.McLocalization;
import goblinbob.mobends.platform.McPlayer;
import goblinbob.mobends.platform.McPoseStack;
import goblinbob.mobends.platform.McResourceManager;
import goblinbob.mobends.platform.McRenderLayerProvider;
import goblinbob.mobends.platform.McResourcePath;
import goblinbob.mobends.core.util.ResourceLocationFactory;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;

import javax.annotation.Nullable;

public class ForgePlatformServices implements IPlatformServices
{
    private final McResourceManager resourceManager = new McResourceManager();
    private final McLocalization localization = new McLocalization();
    private final McRenderLayerProvider renderLayerProvider = new McRenderLayerProvider();

    public ForgePlatformServices()
    {
        IEntityVertexHelper.Holder.setHelper(new ForgeEntityVertexHelper());
        IArmorHelper.Holder.setHelper(new ForgeArmorHelper());
        IModelRenderHelper.Holder.setHelper(new ForgeModelRenderHelper());

        ArmorModelProviderHolder.setProvider(new ForgeArmorModelProvider());
        IArmorTextureProvider.Holder.setProvider(new ForgeArmorTextureProvider());
        IArmorLayerProvider.Holder.setProvider(new ForgeArmorLayerProvider());
        IArmorColorProvider.Holder.setProvider(new ForgeArmorColorProvider());
    }

    @Override
    public String getPlatformName()
    {
        return "Forge";
    }

    @Override
    public String getMinecraftVersion()
    {
        return "1.20.1";
    }

    @Override
    public boolean isClient()
    {
        return FMLEnvironment.dist.isClient();
    }

    @Override
    public boolean isDevelopmentEnvironment()
    {
        return !FMLLoader.isProduction();
    }

    @Override
    public IResourceManager getResourceManager()
    {
        return resourceManager;
    }

    @Override
    public ILocalization getLocalization()
    {
        return localization;
    }

    @Override
    public IRenderLayerProvider getRenderLayerProvider()
    {
        return renderLayerProvider;
    }

    @Override
    @Nullable
    public IEntity wrapEntity(Object nativeEntity)
    {
        if (nativeEntity instanceof Entity) {
            Entity entity = (Entity) nativeEntity;
            return new McEntity(entity);
        }
        return null;
    }

    @Override
    @Nullable
    public ILivingEntity wrapLivingEntity(Object nativeEntity)
    {
        if (nativeEntity instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) nativeEntity;
            return new McLivingEntity(entity);
        }
        return null;
    }

    @Override
    @Nullable
    public IPlayer wrapPlayer(Object nativeEntity)
    {
        if (nativeEntity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) nativeEntity;
            return new McPlayer(player);
        }
        return null;
    }

    @Override
    public IPoseStack wrapPoseStack(Object nativePoseStack)
    {
        if (nativePoseStack instanceof MatrixStack) {
            MatrixStack poseStack = (MatrixStack) nativePoseStack;
            return new McPoseStack(poseStack);
        }
        throw new IllegalArgumentException("Expected MatrixStack, got: " + nativePoseStack.getClass().getName());
    }

    @Override
    public IBufferSource wrapBufferSource(Object nativeBufferSource)
    {
        if (nativeBufferSource instanceof IRenderTypeBuffer) {
            IRenderTypeBuffer bufferSource = (IRenderTypeBuffer) nativeBufferSource;
            return new ForgeBufferSource(bufferSource);
        }
        throw new IllegalArgumentException("Expected IRenderTypeBuffer, got: " + nativeBufferSource.getClass().getName());
    }

    @Override
    public IItemStack wrapItemStack(Object nativeItemStack)
    {
        if (nativeItemStack instanceof ItemStack) {
            ItemStack itemStack = (ItemStack) nativeItemStack;
            return new McItemStack(itemStack);
        }
        throw new IllegalArgumentException("Expected ItemStack, got: " + nativeItemStack.getClass().getName());
    }

    @Override
    public IResourcePath wrapResourceLocation(Object nativeResourceLocation)
    {
        if (nativeResourceLocation instanceof ResourceLocation) {
            ResourceLocation location = (ResourceLocation) nativeResourceLocation;
            return new McResourcePath(location);
        }
        throw new IllegalArgumentException("Expected ResourceLocation, got: " + nativeResourceLocation.getClass().getName());
    }

    @Override
    public IPoseStack createPoseStack()
    {
        return new McPoseStack();
    }

    @Override
    public IResourcePath createResourcePath(String namespace, String path)
    {
        return new McResourcePath(namespace, path);
    }

    @Override
    @Nullable
    public IResourcePath parseResourcePath(String location)
    {
        try
        {
            ResourceLocation loc = ResourceLocationFactory.parse(location);
            return new McResourcePath(loc);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public ITesselator getTesselator()
    {
        return new ForgeTesselator();
    }

    @Override
    public void setPositionShader()
    {
        RenderSystem.disableTexture();
    }

    @Override
    public void setPositionColorShader()
    {
        RenderSystem.disableTexture();
    }

    @Override
    public void setPositionTexShader()
    {
        RenderSystem.enableTexture();
    }

    @Override
    public void setPositionTexColorShader()
    {
        RenderSystem.enableTexture();
    }

    @Override
    public void setConfigBoolean(String key, boolean value)
    {
        goblinbob.mobends.forge.ForgeConfig.set(key, value);
    }
}
