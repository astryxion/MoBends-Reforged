package goblinbob.mobends.compat;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraftforge.fml.ModList;
import goblinbob.mobends.core.client.model.BendsModelPart;
import goblinbob.mobends.core.client.model.ModelPartTransform;
import goblinbob.mobends.core.util.GlHelper;
import goblinbob.mobends.lib.math.Quaternion;
import goblinbob.mobends.lib.math.SmoothOrientation;
import goblinbob.mobends.standard.data.BipedEntityData;
import goblinbob.mobends.standard.mutators.BipedMutator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CarryOnCompat
{
    private static final String MOD_ID = "carryon";

    private static final float RAD_TO_DEG = (float) (180.0 / Math.PI);
    private static final float ARM_ROLL = 0.05F;
    private static final float MODEL_UNIT = 1.0F / 16.0F;
    private static final float CARRY_SCALE = 0.6F;
    private static final float HAND_Y = 5.0F;
    private static final float HAND_Z = -2.0F;
    private static final float BACK_Y = -6.0F;
    private static final float BACK_Z = 7.0F;

    private static final Map<Integer, Boolean> pendingAnchors = new HashMap<>();
    private static final Map<Integer, Anchor> anchors = new HashMap<>();

    private static boolean initialized = false;
    private static boolean isLoaded = false;

    private static Method hasTileDataMethod;
    private static Method hasEntityDataMethod;
    private static Method getOverrideMethod;
    private static Method renderLeftArmMethod;
    private static Method renderRightArmMethod;
    private static Method rotationLeftArmMethod;
    private static Method rotationRightArmMethod;
    private static Method getXYZArrayMethod;
    private static Object renderArmsConfig;
    private static Method renderArmsGetMethod;

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
        Class<?> blockItemClass = Class.forName("tschipp.carryon.common.item.ItemCarryonBlock");
        Class<?> entityItemClass = Class.forName("tschipp.carryon.common.item.ItemCarryonEntity");
        hasTileDataMethod = blockItemClass.getMethod("hasTileData", net.minecraft.item.ItemStack.class);
        hasEntityDataMethod = entityItemClass.getMethod("hasEntityData", net.minecraft.item.ItemStack.class);

        try
        {
            Class<?> checkerClass = Class.forName("tschipp.carryon.common.scripting.ScriptChecker");
            Class<?> overrideClass = Class.forName("tschipp.carryon.common.scripting.CarryOnOverride");
            Class<?> parseClass = Class.forName("tschipp.carryon.common.helper.ScriptParseHelper");

            getOverrideMethod = checkerClass.getMethod("getOverride", PlayerEntity.class);
            renderLeftArmMethod = overrideClass.getMethod("isRenderLeftArm");
            renderRightArmMethod = overrideClass.getMethod("isRenderRightArm");
            rotationLeftArmMethod = overrideClass.getMethod("getRenderRotationLeftArm");
            rotationRightArmMethod = overrideClass.getMethod("getRenderRotationRightArm");
            getXYZArrayMethod = parseClass.getMethod("getXYZArray", String.class);
        }
        catch (Exception e)
        {
            getOverrideMethod = null;
        }

        try
        {
            Class<?> settingsClass = Class.forName("tschipp.carryon.common.config.Configs$Settings");
            renderArmsConfig = settingsClass.getField("renderArms").get(null);
            renderArmsGetMethod = renderArmsConfig.getClass().getMethod("get");
        }
        catch (Exception e)
        {
            renderArmsConfig = null;
            renderArmsGetMethod = null;
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

    private static net.minecraft.item.ItemStack getCarriedStack(PlayerEntity player)
    {
        if (player == null)
        {
            return net.minecraft.item.ItemStack.EMPTY;
        }
        return player.getMainHandItem();
    }

    public static boolean isCarrying(net.minecraft.entity.LivingEntity entity)
    {
        if (!isModLoaded() || !(entity instanceof PlayerEntity))
        {
            return false;
        }
        PlayerEntity player = (PlayerEntity) entity;

        try
        {
            net.minecraft.item.ItemStack stack = getCarriedStack(player);
            return isTile(stack) || isEntity(stack);
        }
        catch (Exception e)
        {
            return false;
        }
    }

    private static boolean isTile(net.minecraft.item.ItemStack stack) throws Exception
    {
        return stack != null && !stack.isEmpty() && Boolean.TRUE.equals(hasTileDataMethod.invoke(null, stack));
    }

    private static boolean isEntity(net.minecraft.item.ItemStack stack) throws Exception
    {
        return stack != null && !stack.isEmpty() && Boolean.TRUE.equals(hasEntityDataMethod.invoke(null, stack));
    }

    private static boolean shouldRenderArms()
    {
        if (renderArmsGetMethod == null || renderArmsConfig == null)
        {
            return true;
        }

        try
        {
            Object value = renderArmsGetMethod.invoke(renderArmsConfig);
            return value instanceof Boolean ? (Boolean) value : true;
        }
        catch (Exception e)
        {
            return true;
        }
    }

    private static float getRenderWidth(PlayerEntity player)
    {
        return 1.0F;
    }

    private static Object getScriptRender(PlayerEntity player)
    {
        if (getOverrideMethod == null)
        {
            return null;
        }

        try
        {
            return getOverrideMethod.invoke(null, player);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static boolean applyToPose(BipedEntityData<?> data)
    {
        if (data == null || !isModLoaded())
        {
            return false;
        }

        if (!(data.getEntity() instanceof PlayerEntity)) {
            return false;
        }
        PlayerEntity player = (PlayerEntity) data.getEntity();

        if (!shouldRenderArms())
        {
            return false;
        }

        final net.minecraft.item.ItemStack carried = getCarriedStack(player);
        try
        {
            if (!isTile(carried) && !isEntity(carried))
            {
                return false;
            }

            if (player.isVisuallySwimming() || player.isFallFlying())
            {
                pendingAnchors.put(player.getId(), Boolean.TRUE);
                return true;
            }

            final boolean isBlock = isTile(carried);

            final boolean sneaking = !player.abilities.flying && player.isShiftKeyDown() || player.isCrouching();
            final float pitch = 1.0F + (sneaking ? 0.2F : 0.0F) + (isBlock ? 0.0F : 0.3F);
            final float offset = Math.min((getRenderWidth(player) - 1.0F) / 1.5F, 0.2F);

            final Object render = getScriptRender(player);
            if (render != null)
            {
                Boolean left = (Boolean) renderLeftArmMethod.invoke(render);
                Boolean right = (Boolean) renderRightArmMethod.invoke(render);

                if (right != null && right)
                {
                    float[] rot = scriptArmRotation(render, rotationRightArmMethod, -pitch, offset, -ARM_ROLL);
                    applyArm(data.rightArm, data.rightForeArm, rot[0], rot[1], rot[2]);
                }

                if (left != null && left)
                {
                    float[] rot = scriptArmRotation(render, rotationLeftArmMethod, -pitch, -offset, ARM_ROLL);
                    applyArm(data.leftArm, data.leftForeArm, rot[0], rot[1], rot[2]);
                }
            }
            else
            {
                applyArm(data.rightArm, data.rightForeArm, -pitch, offset, -ARM_ROLL);
                applyArm(data.leftArm, data.leftForeArm, -pitch, -offset, ARM_ROLL);
            }

            pendingAnchors.put(player.getId(), Boolean.FALSE);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    private static float[] scriptArmRotation(Object render, Method rotationMethod, float defaultX, float defaultY, float defaultZ) throws Exception
    {
        String encoded = (String) rotationMethod.invoke(render);
        if (encoded != null && !encoded.isEmpty() && getXYZArrayMethod != null)
        {
            float[] parsed = (float[]) getXYZArrayMethod.invoke(null, encoded);
            if (parsed != null && parsed.length >= 3)
            {
                return parsed;
            }
        }
        return new float[] { defaultX, defaultY, defaultZ };
    }

    public static void captureAnchor(net.minecraft.entity.LivingEntity entity, Object rawMutator)
    {
        if (!(entity instanceof PlayerEntity)) {
            return;
        }
        PlayerEntity player = (PlayerEntity) entity;

        final Boolean backMounted = pendingAnchors.remove(player.getId());
        if (backMounted == null || !(rawMutator instanceof BipedMutator<?, ?, ?>)) {
            return;
        }
        BipedMutator<?, ?, ?> mutator = (BipedMutator<?, ?, ?>) rawMutator;

        final Matrix4f pose = mutator.getRenderAnchorPose();
        final BendsModelPart body = mutator.getBody();
        if (pose == null || body == null)
        {
            return;
        }

        final Vector3f point = backMounted ? backAnchor(body) : handAnchor(mutator, body);

        final Matrix4f frame = new Matrix4f(pose);
        frame.translate(new Vector3f(point.x() * MODEL_UNIT, point.y() * MODEL_UNIT, point.z() * MODEL_UNIT));
        frame.multiply(GlHelper.toJomlQuaternion(quaternionOf(body)));
        frame.multiply(Matrix4f.createScaleMatrix(1.0F, -1.0F, -1.0F));

        if (net.minecraft.client.Minecraft.getInstance().options.getCameraType().isMirrored())
        {
            frame.multiply(Vector3f.YP.rotation((float) Math.PI));
        }

        net.minecraft.util.math.vector.Quaternion rotation = new Matrix3f(frame).svdDecompose().getLeft();
        anchors.put(player.getId(), new Anchor(
                new Vector3f(frame.m03, frame.m13, frame.m23),
                new Quaternion(rotation.i(), rotation.j(), rotation.k(), rotation.r())));
    }

    public static void applyAnchor(PlayerEntity player, MatrixStack poseStack)
    {
        if (player == null || poseStack == null)
        {
            return;
        }

        final Anchor anchor = anchors.remove(player.getId());
        if (anchor == null)
        {
            return;
        }

        Matrix4f pose = poseStack.last().pose();
        pose.setIdentity();
        pose.translate(new Vector3f(anchor.position.x(), anchor.position.y(), anchor.position.z()));
        pose.multiply(GlHelper.toJomlQuaternion(anchor.rotation));
        pose.multiply(Matrix4f.createScaleMatrix(CARRY_SCALE, CARRY_SCALE, CARRY_SCALE));

        Matrix3f normal = poseStack.last().normal();
        normal.setIdentity();
        normal.mul(GlHelper.toJomlQuaternion(anchor.rotation));
    }

    private static final class Anchor
    {
        final Vector3f position;
        final Quaternion rotation;

        Anchor(Vector3f position, Quaternion rotation)
        {
            this.position = position;
            this.rotation = rotation;
        }
    }

    private static Vector3f backAnchor(BendsModelPart body)
    {
        final Vector3f point = pivotOf(body);
        point.add(transform(quaternionOf(body), new Vector3f(0.0F, BACK_Y, BACK_Z)));
        return point;
    }

    private static Vector3f handAnchor(BipedMutator<?, ?, ?> mutator, BendsModelPart body)
    {
        final Vector3f anchor = new Vector3f();

        for (int i = 0; i < 2; ++i)
        {
            final BendsModelPart arm = i == 0 ? mutator.getRightArm() : mutator.getLeftArm();
            final BendsModelPart foreArm = i == 0 ? mutator.getRightForeArm() : mutator.getLeftForeArm();
            if (arm == null || foreArm == null)
            {
                return backAnchor(body);
            }

            final Quaternion chain = quaternionOf(body);
            final Vector3f point = pivotOf(body);

            advance(point, chain, arm);
            advance(point, chain, foreArm);
            point.add(transform(chain, new Vector3f(0.0F, HAND_Y, HAND_Z)));

            anchor.add(point);
        }

        anchor.mul(0.5F);
        return anchor;
    }

    private static void advance(Vector3f point, Quaternion chain, BendsModelPart part)
    {
        point.add(transform(chain, pivotOf(part)));
        Quaternion.mul(chain, quaternionOf(part), chain);
    }

    private static Vector3f transform(Quaternion quaternion, Vector3f vector)
    {
        Vector3f result = vector.copy();
        result.transform(GlHelper.toJomlQuaternion(quaternion));
        return result;
    }

    private static Vector3f pivotOf(BendsModelPart part)
    {
        return new Vector3f(part.position.x + part.offset.x,
                part.position.y + part.offset.y,
                part.position.z + part.offset.z);
    }

    private static Quaternion quaternionOf(BendsModelPart part)
    {
        final Quaternion rotation = part.rotation.getSmooth();
        return new Quaternion(rotation.x, rotation.y, rotation.z, rotation.w);
    }

    private static void applyArm(ModelPartTransform arm, ModelPartTransform foreArm,
                                 float x, float y, float z)
    {
        if (arm == null)
        {
            return;
        }

        setRotation(arm.rotation, x * RAD_TO_DEG, y * RAD_TO_DEG, z * RAD_TO_DEG);

        if (foreArm != null)
        {
            foreArm.rotation.orientInstantX(0.0F);
        }
    }

    private static void setRotation(SmoothOrientation rotation, float x, float y, float z)
    {
        rotation.orientInstantX(x).rotateInstantY(y).rotateInstantZ(z);
    }
}
