package net.gobbob.mobends.customarmor;

import java.util.ArrayList;
import java.util.List;
import net.gobbob.mobends.client.model.ModelCustomArmor;
import net.minecraft.client.model.ModelBiped;

public class CustomArmor {
   public static List<CustomArmor> armorList = new ArrayList();
   public ModelCustomArmor armorModel;
   public String texturePath;

   public static CustomArmor get(ModelBiped argOriginal, String argTexture, float argModelBuffnes) {
      for(int i = 0; i < armorList.size(); ++i) {
         if (((CustomArmor)armorList.get(i)).texturePath.equalsIgnoreCase(argTexture)) {
            return (CustomArmor)armorList.get(i);
         }
      }

      CustomArmor newArmor = ArmorConverter.convert(argModelBuffnes, argOriginal, argTexture);
      armorList.add(newArmor);
      return newArmor;
   }

   public CustomArmor() {
      this.armorModel = null;
      this.texturePath = null;
   }

   public CustomArmor(ModelCustomArmor argArmor, String argTexture) {
      this.armorModel = argArmor;
      this.texturePath = argTexture;
   }
}
