package goblinbob.mobends.standard.client.model.armor;

import net.minecraft.client.renderer.entity.model.BipedModel;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.HashMap;
import java.util.Map;

public class ArmorModelFactory
{
    private static final Logger LOG = LogManager.getLogger(ArmorModelFactory.class);

    protected static Map<BipedModel<?>, ArmorWrapper> outerLayerCache = new HashMap<>();

    protected static Map<BipedModel<?>, ArmorWrapper> innerLayerCache = new HashMap<>();

    public static ArmorWrapper getArmorWrapper(BipedModel<?> model, boolean shouldBeMutated, float inflation)
    {
        Map<BipedModel<?>, ArmorWrapper> cache = (inflation < 0.75F) ? innerLayerCache : outerLayerCache;

        ArmorWrapper wrapper = cache.get(model);

        if (shouldBeMutated)
        {
            if (wrapper == null)
            {
                wrapper = ArmorWrapper.createFor(model, inflation);
                cache.put(model, wrapper);

            }

            return wrapper;
        }

        if (wrapper != null)
        {
            wrapper.deapply();
        }

        return wrapper;
    }

    public static ArmorWrapper getArmorWrapper(BipedModel<?> model, boolean shouldBeMutated)
    {
        return getArmorWrapper(model, shouldBeMutated, 1.0F);
    }

    public static void refresh()
    {
        for (ArmorWrapper wrapper : outerLayerCache.values())
        {
            wrapper.demutate();
        }
        outerLayerCache.clear();

        for (ArmorWrapper wrapper : innerLayerCache.values())
        {
            wrapper.demutate();
        }
        innerLayerCache.clear();
    }
}
