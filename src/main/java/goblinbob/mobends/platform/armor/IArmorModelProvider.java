package goblinbob.mobends.platform.armor;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public interface IArmorModelProvider
{
    <E extends LivingEntity> Model getCustomArmorModel(
            E entity,
            ItemStack itemStack,
            EquipmentSlotType slot,
            BipedModel<E> defaultModel
    );

    IArmorModelProvider DEFAULT = new IArmorModelProvider()
    {
        @Override
        public <E extends LivingEntity> Model getCustomArmorModel(
                E entity, ItemStack itemStack, EquipmentSlotType slot, BipedModel<E> defaultModel)
        {
            return defaultModel;
        }
    };
}
