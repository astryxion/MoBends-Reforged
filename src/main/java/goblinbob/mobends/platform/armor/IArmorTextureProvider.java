package goblinbob.mobends.platform.armor;

import net.minecraft.util.ResourceLocation;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public interface IArmorTextureProvider
{
    @Nullable
    <E extends LivingEntity> ResourceLocation getArmorTexture(
            ArmorItem armorItem,
            ItemStack itemStack,
            E entity,
            EquipmentSlotType slot,
            @Nullable Object layer,
            @Nullable String type,
            boolean isInnerModel
    );

    IArmorTextureProvider DEFAULT = new IArmorTextureProvider()
    {
        @Override
        public <E extends LivingEntity> ResourceLocation getArmorTexture(
                ArmorItem armorItem, ItemStack itemStack, E entity,
                EquipmentSlotType slot, Object layer, String type, boolean isInnerModel)
        {
            return null;
        }
    };

    class Holder
    {
        private static IArmorTextureProvider provider = DEFAULT;

        public static void setProvider(IArmorTextureProvider provider)
        {
            Holder.provider = provider;
        }

        public static IArmorTextureProvider getProvider()
        {
            return provider;
        }
    }
}
