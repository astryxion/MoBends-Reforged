package goblinbob.mobends.compat;

import net.minecraftforge.fml.ModList;
import net.minecraft.util.Hand;
import net.minecraft.entity.LivingEntity;

import java.lang.reflect.Method;

public class ArtifactsCompat
{
    private static final String MOD_ID = "artifacts";

    private static boolean initialized = false;
    private static boolean isLoaded = false;

    private static Class<?> umbrellaClass;
    private static Method isHoldingUmbrellaUprightMethod;

    public static void init()
    {
        if (initialized)
        {
            return;
        }
        initialized = true;

        isLoaded = ModList.get().isLoaded(MOD_ID);

        if (isLoaded)
        {
            try
            {
                umbrellaClass = Class.forName("artifacts.item.UmbrellaItem");
            }
            catch (Exception e)
            {
                isLoaded = false;
                return;
            }

            try
            {
                isHoldingUmbrellaUprightMethod = umbrellaClass.getMethod(
                        "isHoldingUmbrellaUpright", LivingEntity.class, Hand.class);
            }
            catch (Exception e)
            {
                isHoldingUmbrellaUprightMethod = null;
            }
        }
    }

    public static boolean isModLoaded()
    {
        if (!initialized)
        {
            init();
        }
        return isLoaded;
    }

    public static boolean isHoldingUmbrellaUpright(LivingEntity entity, Hand hand)
    {
        if (entity == null || !isModLoaded())
        {
            return false;
        }

        if (isHoldingUmbrellaUprightMethod != null)
        {
            try
            {
                Boolean upright = (Boolean) isHoldingUmbrellaUprightMethod.invoke(null, entity, hand);
                return upright != null && upright;
            }
            catch (Exception e)
            {
                isHoldingUmbrellaUprightMethod = null;
            }
        }

        return umbrellaClass.isInstance(entity.getItemInHand(hand).getItem())
                && (!entity.isUsingItem() || entity.getUsedItemHand() != hand);
    }

    public static boolean isHoldingUmbrellaUpright(LivingEntity entity)
    {
        return isHoldingUmbrellaUpright(entity, Hand.MAIN_HAND)
                || isHoldingUmbrellaUpright(entity, Hand.OFF_HAND);
    }
}
