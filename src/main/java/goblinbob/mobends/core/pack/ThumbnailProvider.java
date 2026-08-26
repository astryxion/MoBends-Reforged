package goblinbob.mobends.core.pack;

import goblinbob.mobends.standard.main.ModStatics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.DownloadingTexture;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class ThumbnailProvider
{
    public static final ResourceLocation DEFAULT_THUMBNAIL_LOCATION = ModStatics.getResource(
            "textures/gui/default_pack_thumbnail.png");

    private final PackCache packCache;

    public ThumbnailProvider(PackCache packCache)
    {
        this.packCache = packCache;
    }

    public ResourceLocation getThumbnailLocation(String packName, String thumbnailUrl)
    {
        final ResourceLocation resourceLocation = ModStatics.getResource(
                "bendspackthumbnails/" + packName);
        @Nullable Texture texture = Minecraft.getInstance().getTextureManager().getTexture(resourceLocation);

        if (texture == null)
        {
            DownloadingTexture httpTexture = new DownloadingTexture(
                    packCache.getThumbnailFile(packName),
                    thumbnailUrl,
                    DEFAULT_THUMBNAIL_LOCATION,
                    false,
                    null
            );

            Minecraft.getInstance().getTextureManager().register(resourceLocation, httpTexture);
            return resourceLocation;
        }

        return resourceLocation;
    }
}
