package goblinbob.mobends.forge.platform;

import goblinbob.mobends.api.rendering.IArmorHelper;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;

public class ForgeArmorHelper implements IArmorHelper
{
    @Override
    public String getArmorMaterialName(Object armorItem)
    {
        if (!(armorItem instanceof ArmorItem)) {
            return "unknown";
        }
        ArmorItem item = (ArmorItem) armorItem;

        IArmorMaterial material = item.getMaterial();
        return material.getName();
    }
}
