package goblinbob.mobends.forge.platform;

import goblinbob.mobends.platform.armor.IArmorTextureProvider;
import goblinbob.mobends.core.util.ResourceLocationFactory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class ForgeArmorTextureProvider implements IArmorTextureProvider
{
    @Override
    @Nullable
    public <E extends LivingEntity> ResourceLocation getArmorTexture(
            ArmorItem armorItem, ItemStack itemStack, E entity,
            EquipmentSlotType slot, @Nullable Object layer, @Nullable String type, boolean isInnerModel)
    {
        String texture = armorItem.getArmorTexture(itemStack, entity, slot, type);
        if (texture != null)
        {
            return ResourceLocationFactory.parse(texture);
        }
        return null;
    }
}
