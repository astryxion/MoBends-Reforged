package goblinbob.mobends.compat;

import goblinbob.mobends.api.animation.MoBendsAnimationControl;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Hand;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public final class ThirdPartyPoseCompat
{
    private static final String[] DEFAULT_SELF_POSING_MODS = {
            "cgm",
            "stabxmodernguns"
    };

    private static boolean initialized = false;

    private ThirdPartyPoseCompat()
    {
    }

    public static void init()
    {
        if (initialized)
        {
            return;
        }
        initialized = true;

        for (final String modId : DEFAULT_SELF_POSING_MODS)
        {
            MoBendsAnimationControl.registerSelfPosingMod(modId);
        }

        MoBendsAnimationControl.registerAnimationDeferral("mobends", ThirdPartyPoseCompat::shouldYieldToHeldItem);
    }

    public static boolean shouldYieldToHeldItem(LivingEntity entity)
    {
        if (entity == null)
        {
            return false;
        }

        return isSelfPosed(entity.getItemInHand(Hand.MAIN_HAND))
                || isSelfPosed(entity.getItemInHand(Hand.OFF_HAND));
    }

    private static boolean isSelfPosed(ItemStack stack)
    {
        if (stack == null || stack.isEmpty())
        {
            return false;
        }

        final ResourceLocation id = Registry.ITEM.getKey(stack.getItem());
        return id != null && MoBendsAnimationControl.isSelfPosingMod(id.getNamespace());
    }
}
