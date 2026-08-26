package goblinbob.mobends.forge.platform;

import goblinbob.mobends.api.rendering.IArmorColorProvider;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.ItemStack;

public class ForgeArmorColorProvider implements IArmorColorProvider
{
    @Override
    public int getDyedColor(Object itemStack)
    {
        if (!(itemStack instanceof ItemStack)) {
            return -1;
        }
        ItemStack stack = (ItemStack) itemStack;

        if (stack.getItem() instanceof IDyeableArmorItem) {
            IDyeableArmorItem dyeable = (IDyeableArmorItem) stack.getItem();
            return dyeable.getColor(stack) & 0xFFFFFF;
        }

        return -1;
    }

    @Override
    public boolean hasDyedColor(Object itemStack)
    {
        if (!(itemStack instanceof ItemStack)) return false;
        ItemStack stack = (ItemStack) itemStack;
        if (!(stack.getItem() instanceof IDyeableArmorItem)) return false;
        IDyeableArmorItem dyeable = (IDyeableArmorItem) stack.getItem();
        return dyeable.hasCustomColor(stack);
    }

    @Override
    public boolean isDyeable(Object itemStack)
    {
        if (!(itemStack instanceof ItemStack)) return false;
        ItemStack stack = (ItemStack) itemStack;
        return stack.getItem() instanceof IDyeableArmorItem;
    }
}
