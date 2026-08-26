package goblinbob.mobends.platform;

import goblinbob.mobends.api.entity.IItemStack;
import goblinbob.mobends.api.resource.IResourcePath;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.*;

import javax.annotation.Nullable;

public class McItemStack implements IItemStack
{
    private final ItemStack itemStack;

    public McItemStack(ItemStack itemStack)
    {
        this.itemStack = itemStack;
    }

    @Override
    public boolean isEmpty()
    {
        return itemStack.isEmpty();
    }

    @Override
    public int getCount()
    {
        return itemStack.getCount();
    }

    @Override
    @Nullable
    public IResourcePath getItemId()
    {
        ResourceLocation key = Registry.ITEM.getKey(itemStack.getItem());
        return new McResourcePath(key);
    }

    @Override
    public boolean isTool()
    {
        Item item = itemStack.getItem();
        return item instanceof SwordItem ||
               item instanceof AxeItem ||
               item instanceof PickaxeItem ||
               item instanceof ShovelItem ||
               item instanceof HoeItem ||
               item instanceof TridentItem;
    }

    @Override
    public boolean isBow()
    {
        return itemStack.getItem() instanceof BowItem;
    }

    @Override
    public boolean isCrossbow()
    {
        return itemStack.getItem() instanceof CrossbowItem;
    }

    @Override
    public boolean isShield()
    {
        return itemStack.getItem() instanceof ShieldItem;
    }

    @Override
    public boolean isFood()
    {
        return itemStack.isEdible();
    }

    @Override
    public boolean isTrident()
    {
        return itemStack.getItem() instanceof TridentItem;
    }

    @Override
    public boolean isSpyglass()
    {
        ResourceLocation name = itemStack.getItem().getRegistryName();
        return name != null && "minecraft:spyglass".equals(name.toString());
    }

    @Override
    public Object getNative()
    {
        return itemStack;
    }

    public ItemStack getItemStack()
    {
        return itemStack;
    }
}
