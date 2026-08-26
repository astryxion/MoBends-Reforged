package goblinbob.mobends.compat;

import net.minecraftforge.fml.ModList;
import goblinbob.mobends.standard.mutators.BipedMutator;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.lang.reflect.Method;

public class ParCoolCompat
{
    private static final String MOD_ID = "parcool";

    private enum Api
    {
        NONE,
        MODERN,
        LEGACY
    }

    private static boolean initialized = false;
    private static Api api = Api.NONE;

    private static Method getAnimatorMethod;
    private static Method isIdleMethod;

    private static Method getAnimationMethod;
    private static Method hasAnimatorMethod;
    private static Method shouldCancelAnimationMethod;

    public static void init()
    {
        if (initialized)
        {
            return;
        }
        initialized = true;

        if (!ModList.get().isLoaded(MOD_ID))
        {
            return;
        }

        try
        {
            initModernReflection();
            api = Api.MODERN;
            return;
        }
        catch (Exception ignored)
        {
        }

        try
        {
            initLegacyReflection();
            api = Api.LEGACY;
        }
        catch (Exception ignored)
        {
            api = Api.NONE;
        }
    }

    private static void initModernReflection() throws Exception
    {
        Class<?> animatorClass = Class.forName("com.alrex.parcool.client.animation.system.PlayerAnimator");

        getAnimatorMethod = animatorClass.getMethod("get", AbstractClientPlayerEntity.class);
        isIdleMethod = animatorClass.getMethod("isIdle");
    }

    private static void initLegacyReflection() throws Exception
    {
        Class<?> animationClass = Class.forName("com.alrex.parcool.common.capability.Animation");

        getAnimationMethod = animationClass.getMethod("get", PlayerEntity.class);
        hasAnimatorMethod = animationClass.getMethod("hasAnimator");
        shouldCancelAnimationMethod = animationClass.getMethod("shouldCancelAnimation", PlayerEntity.class);
    }

    public static boolean isModLoaded()
    {
        if (!initialized)
        {
            init();
        }
        return api != Api.NONE;
    }

    public static boolean isAnimating(LivingEntity entity)
    {
        if (!isModLoaded())
        {
            return false;
        }

        switch (api) {
case MODERN:
return isAnimatingModern(entity);
case LEGACY:
return isAnimatingLegacy(entity);
default:
return false;
}
    }

    private static boolean isAnimatingModern(LivingEntity entity)
    {
        if (!(entity instanceof AbstractClientPlayerEntity)) {
            return false;
        }
        AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) entity;

        try
        {
            Object animator = getAnimatorMethod.invoke(null, player);
            if (animator == null)
            {
                return false;
            }

            return !(Boolean) isIdleMethod.invoke(animator);
        }
        catch (Exception e)
        {
            api = Api.NONE;
            return false;
        }
    }

    private static boolean isAnimatingLegacy(LivingEntity entity)
    {
        if (!(entity instanceof PlayerEntity)) {
            return false;
        }
        PlayerEntity player = (PlayerEntity) entity;

        try
        {
            Object animation = getAnimationMethod.invoke(null, player);
            if (animation == null)
            {
                return false;
            }

            if (!(Boolean) hasAnimatorMethod.invoke(animation))
            {
                return false;
            }

            return !(Boolean) shouldCancelAnimationMethod.invoke(animation, player);
        }
        catch (Exception e)
        {
            api = Api.NONE;
            return false;
        }
    }

    public static void applyPose(LivingEntity entity, BipedMutator<?, ?, ?> mutator, BipedModel<?> vanillaModel)
    {
        if (mutator == null || !(vanillaModel instanceof PlayerModel<?>))
        {
            return;
        }

        if (!isAnimating(entity))
        {
            return;
        }

        mutator.adoptPoseFromVanillaModel(vanillaModel, null, null);
    }
}
