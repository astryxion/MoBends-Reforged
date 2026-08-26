package goblinbob.mobends.core.supporters;

import goblinbob.mobends.core.util.Color;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class SupporterContent
{
    public static Set<Map.Entry<String, AccessoryDetails>> getAccessories()
    {
        return Collections.emptySet();
    }

    public static Map<String, AccessorySettings> getAccessorySettingsMapFor(PlayerEntity player)
    {
        return Collections.emptyMap();
    }

    public static Color getTrailColorFor(PlayerEntity player)
    {
        return new Color(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
