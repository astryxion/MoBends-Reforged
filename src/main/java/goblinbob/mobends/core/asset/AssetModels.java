package goblinbob.mobends.core.asset;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.LogManager;
import net.minecraft.client.renderer.model.IBakedModel;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class AssetModels
{
    public static final AssetModels INSTANCE = new AssetModels();
    private static final Logger LOGGER = LogManager.getLogger(AssetModels.class);

    private final Map<AssetLocation, IBakedModel> bakedModelMap = new HashMap<>();

    public IBakedModel register(AssetLocation location) throws IOException
    {
        try (InputStream stream = new FileInputStream(AssetsModule.INSTANCE.getAssetFile(location));
             BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)))
        {
            JsonElement jsonElement = new JsonParser().parse(reader);
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            IBakedModel bakedModel = parseAndBakeModel(jsonObject, location);
            bakedModelMap.put(location, bakedModel);

            return bakedModel;
        }
    }

    public void clearCache()
    {
        bakedModelMap.clear();
    }

    public IBakedModel getModel(AssetLocation location)
    {
        if (!bakedModelMap.containsKey(location))
        {
            try
            {
                IBakedModel bakedModel = register(location);
                bakedModelMap.put(location, bakedModel);
                return bakedModel;
            }
            catch(IOException e)
            {
                LOGGER.warn("Failed to bake asset model: {}", location.toString(), e);
                bakedModelMap.put(location, null);
                return null;
            }
        }

        return bakedModelMap.get(location);
    }

    private AssetLocation resolveTextureName(String name)
    {
        if (name.equals("missingno") || name.startsWith("#"))
        {
            return null;
        }

        return new AssetLocation("textures/" + name);
    }

    private IBakedModel parseAndBakeModel(JsonObject modelJson, AssetLocation modelLocation) throws IOException
    {
        LOGGER.warn("Custom model baking not implemented for this platform: {}", modelLocation);
        return null;
    }
}
