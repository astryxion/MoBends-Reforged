package goblinbob.mobends.compat;

import net.minecraftforge.fml.ModList;

public class CuriosCompat
{
    private static final String MOD_ID = "curios";

    private static boolean initialized = false;
    private static boolean isLoaded = false;

    public static void init()
    {
        if (initialized) return;
        initialized = true;

        isLoaded = ModList.get().isLoaded(MOD_ID);
    }

    public static boolean isModLoaded()
    {
        if (!initialized) init();
        return isLoaded;
    }
}
