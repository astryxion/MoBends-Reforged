package goblinbob.mobends.forge.platform;

import goblinbob.mobends.platform.armor.IArmorModelProvider;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class ForgeArmorModelProvider implements IArmorModelProvider
{
    @Override
    public <E extends LivingEntity> Model getCustomArmorModel(
            E entity, ItemStack itemStack, EquipmentSlotType slot, BipedModel<E> defaultModel)
    {
        try
        {
            Model model = itemStack.getItem().getArmorModel(entity, itemStack, slot, defaultModel);

            if (model != null)
            {
                return model;
            }
        }
        catch (Exception e)
        {
        }

        return defaultModel;
    }
}
