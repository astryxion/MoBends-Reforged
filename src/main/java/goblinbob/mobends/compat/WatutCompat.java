package goblinbob.mobends.compat;

import net.minecraftforge.fml.ModList;
import goblinbob.mobends.core.client.model.BendsModelPart;
import goblinbob.mobends.core.client.model.ModelPartTransform;
import goblinbob.mobends.lib.math.Quaternion;
import goblinbob.mobends.lib.math.SmoothOrientation;
import goblinbob.mobends.standard.data.BipedEntityData;
import goblinbob.mobends.standard.mutators.BipedMutator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class WatutCompat
{
    private static final String MOD_ID = "watut";

    private static final float RAD_TO_DEG = (float) (180.0 / Math.PI);

    private static boolean initialized = false;
    private static boolean isLoaded = false;

    private static Method getManagerMethod;
    private static Method getStatusMethod;
    private static Method isLerpingMethod;
    private static Method getGuiStateMethod;
    private static Object guiStateNone;

    private static Field clientAnimationsField;
    private static Field syncedAnimationsField;

    private static final SmoothOrientation scratchOrientation = new SmoothOrientation();
    private static final Quaternion scratchDesired = new Quaternion();
    private static final Quaternion scratchParentInverse = new Quaternion();
    private static final Quaternion scratchLocal = new Quaternion();

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
                initReflection();
            }
            catch (Exception e)
            {
                isLoaded = false;
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void initReflection() throws Exception
    {
        Class<?> modClass = Class.forName("com.corosus.watut.WatutMod");
        Class<?> managerClass = Class.forName("com.corosus.watut.PlayerStatusManagerClient");
        Class<?> statusClass = Class.forName("com.corosus.watut.PlayerStatus");
        Class<?> guiStateClass = Class.forName("com.corosus.watut.PlayerStatus$PlayerGuiState");
        Class<?> clientConfigClass = Class.forName("com.corosus.watut.config.ConfigClient");
        Class<?> syncedConfigClass = Class.forName("com.corosus.watut.config.ConfigServerControlledSyncedToClient");

        getManagerMethod = modClass.getMethod("getPlayerStatusManagerClient");
        getStatusMethod = managerClass.getMethod("getStatus", PlayerEntity.class);
        isLerpingMethod = statusClass.getMethod("isLerping");
        getGuiStateMethod = statusClass.getMethod("getPlayerGuiState");

        guiStateNone = Enum.valueOf((Class<Enum>) guiStateClass, "NONE");

        clientAnimationsField = clientConfigClass.getField("showPlayerAnimations");
        syncedAnimationsField = syncedConfigClass.getField("showPlayerAnimations");
    }

    public static boolean isModLoaded()
    {
        if (!initialized)
        {
            init();
        }
        return isLoaded;
    }

    public static boolean isPosing(LivingEntity entity)
    {
        if (!isModLoaded() || !(entity instanceof PlayerEntity))
        {
            return false;
        }
        PlayerEntity player = (PlayerEntity) entity;

        try
        {
            if (!clientAnimationsField.getBoolean(null) || !syncedAnimationsField.getBoolean(null))
            {
                return false;
            }

            if (!player.level.players().contains(player))
            {
                return false;
            }

            if (!wouldWatutAnimate(player))
            {
                return false;
            }

            Object manager = getManagerMethod.invoke(null);
            if (manager == null)
            {
                return false;
            }

            Object status = getStatusMethod.invoke(manager, player);
            if (status == null)
            {
                return false;
            }

            if (getGuiStateMethod.invoke(status) != guiStateNone)
            {
                return true;
            }

            return (Boolean) isLerpingMethod.invoke(status);
        }
        catch (Exception e)
        {
            isLoaded = false;
            return false;
        }
    }

    private static boolean wouldWatutAnimate(PlayerEntity player)
    {
        final Minecraft mc = Minecraft.getInstance();

        if (player != mc.player)
        {
            return true;
        }

        if (mc.screen instanceof DisplayEffectsScreen && player.isAlive())
        {
            return true;
        }

        return !mc.options.getCameraType().isFirstPerson();
    }

    public static void applyPose(LivingEntity entity, BipedMutator<?, ?, ?> mutator, BipedModel<?> vanillaModel)
    {
        if (mutator == null || !(vanillaModel instanceof PlayerModel<?>)) {
            return;
        }
        PlayerModel<?> playerModel = (PlayerModel<?>) vanillaModel;

        if (!isPosing(entity))
        {
            return;
        }

        final BendsModelPart body = mutator.getBody();
        if (body == null)
        {
            return;
        }

        final BipedEntityData<?> data = mutator.getRenderData();

        final Quaternion bodyRotation = body.rotation.getSmooth();
        scratchParentInverse.set(-bodyRotation.x, -bodyRotation.y, -bodyRotation.z, bodyRotation.w);

        adopt(playerModel.head, mutator.getHead(), data == null ? null : data.head, null, null);
        adopt(playerModel.rightArm, mutator.getRightArm(), data == null ? null : data.rightArm,
                mutator.getRightForeArm(), data == null ? null : data.rightForeArm);
        adopt(playerModel.leftArm, mutator.getLeftArm(), data == null ? null : data.leftArm,
                mutator.getLeftForeArm(), data == null ? null : data.leftForeArm);
    }

    private static void adopt(ModelRenderer source, BendsModelPart target, ModelPartTransform dataTarget,
                              BendsModelPart child, ModelPartTransform dataChild)
    {
        if (source == null || target == null)
        {
            return;
        }

        scratchOrientation
                .orientInstantX(source.xRot * RAD_TO_DEG)
                .rotateInstantY(source.yRot * RAD_TO_DEG)
                .rotateInstantZ(source.zRot * RAD_TO_DEG);
        scratchDesired.set(scratchOrientation.getSmooth());

        Quaternion.mul(scratchParentInverse, scratchDesired, scratchLocal);
        BipedMutator.applyAdoptedRotation(target, dataTarget, scratchLocal);

        if (child != null || dataChild != null)
        {
            BipedMutator.straightenJoint(child, dataChild);
        }
    }
}
