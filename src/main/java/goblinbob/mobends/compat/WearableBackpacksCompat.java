package goblinbob.mobends.compat;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraftforge.fml.ModList;
import goblinbob.mobends.core.client.MoBendsRenderContext;
import goblinbob.mobends.standard.mutators.BipedMutator;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

import java.util.ArrayDeque;

public class WearableBackpacksCompat
{
    private static final String MOD_ID = "wearablebackpacks";

    private static final Follow NONE = new Follow(null, 0.0F);

    private static final ThreadLocal<ArrayDeque<Follow>> follows =
            ThreadLocal.withInitial(ArrayDeque::new);

    private static boolean initialized = false;
    private static boolean isLoaded = false;

    private static Class<?> backpackItemClass;

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
                backpackItemClass = Class.forName("com.nyfaria.wearablebackpacks.item.BackpackItem");
            }
            catch (Exception e)
            {
                backpackItemClass = null;
                isLoaded = false;
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

    public static boolean isBackpackItem(ItemStack itemStack)
    {
        if (itemStack == null || itemStack.isEmpty() || !isModLoaded() || backpackItemClass == null)
        {
            return false;
        }
        return backpackItemClass.isInstance(itemStack.getItem());
    }

    private static ItemStack getBackpackStack(LivingEntity entity)
    {
        if (entity == null)
        {
            return null;
        }

        final ItemStack chest = entity.getItemBySlot(net.minecraft.inventory.EquipmentSlotType.CHEST);
        if (isBackpackItem(chest))
        {
            return chest;
        }
        return null;
    }

    public static void beginFollow(MatrixStack poseStack, LivingEntity entity)
    {
        final ArrayDeque<Follow> stack = follows.get();

        final BipedModel<?> model = resolveAnimatedModel(poseStack, entity);
        if (model == null)
        {
            stack.push(NONE);
            return;
        }

        poseStack.pushPose();
        model.body.translateAndRotate(poseStack);

        final float savedXRot = model.body.xRot;
        model.body.xRot = 0.0F;

        stack.push(new Follow(model, savedXRot));
    }

    public static boolean isFollowing()
    {
        final Follow follow = follows.get().peek();
        return follow != null && follow != NONE;
    }

    public static void endFollow(MatrixStack poseStack)
    {
        final ArrayDeque<Follow> stack = follows.get();
        if (stack.isEmpty())
        {
            return;
        }

        final Follow follow = stack.pop();
        if (follow == NONE || follow.model == null)
        {
            return;
        }

        follow.model.body.xRot = follow.savedXRot;
        poseStack.popPose();
    }

    private static BipedModel<?> resolveAnimatedModel(MatrixStack poseStack, LivingEntity entity)
    {
        if (poseStack == null || entity == null || !isModLoaded())
        {
            return null;
        }

        if (MoBendsRenderContext.getCurrentEntity() != entity)
        {
            return null;
        }

        final BipedMutator<?, ?, ?> mutator = MoBendsRenderContext.getCurrentBipedMutator();
        if (mutator == null || !mutator.shouldRenderCustom())
        {
            return null;
        }

        final ItemStack stack = getBackpackStack(entity);
        if (stack == null || stack.isEmpty())
        {
            return null;
        }

        return MoBendsRenderContext.getCurrentVanillaModel();
    }

    private static final class Follow
    {
        final BipedModel<?> model;
        final float savedXRot;

        Follow(BipedModel<?> model, float savedXRot)
        {
            this.model = model;
            this.savedXRot = savedXRot;
        }
    }
}
