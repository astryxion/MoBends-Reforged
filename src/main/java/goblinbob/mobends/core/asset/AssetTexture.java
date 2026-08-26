package goblinbob.mobends.core.asset;

import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import org.apache.logging.log4j.LogManager;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.resources.IResourceManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class AssetTexture extends Texture
{
    private static final Logger LOGGER = LogManager.getLogger(AssetTexture.class);

    private final AssetLocation assetLocation;

    @Nullable
    private NativeImage image;

    public AssetTexture(AssetLocation assetLocation)
    {
        this.assetLocation = assetLocation;
    }

    @Override
    public void load(IResourceManager resourceManager) throws IOException
    {
        this.close();

        try (InputStream inputStream = new FileInputStream(AssetsModule.INSTANCE.getAssetFile(assetLocation)))
        {
            this.image = NativeImage.read(inputStream);
        }
        catch (IOException ioexception)
        {
            LOGGER.error("Couldn't load asset texture {}", assetLocation.toString(), ioexception);
            throw ioexception;
        }

        if (!RenderSystem.isOnRenderThreadOrInit())
        {
            RenderSystem.recordRenderCall(this::uploadTexture);
        }
        else
        {
            this.uploadTexture();
        }
    }

    private void uploadTexture()
    {
        if (this.image != null)
        {
            TextureUtil.prepareImage(this.getId(), this.image.getWidth(), this.image.getHeight());
            this.image.upload(0, 0, 0, false);
        }
    }

    @Override
    public void close()
    {
        super.close();
        if (this.image != null)
        {
            this.image.close();
            this.image = null;
        }
    }
}
