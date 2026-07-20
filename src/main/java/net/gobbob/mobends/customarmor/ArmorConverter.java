package net.gobbob.mobends.customarmor;

import net.gobbob.mobends.client.model.ModelCustomArmor;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;

public class ArmorConverter {
   public static CustomArmor convert(float argModelBuffnes, ModelBiped original, String argTexture) {
      ModelCustomArmor newModel = new ModelCustomArmor(argModelBuffnes, 0.0F, original.textureWidth, original.textureHeight);
      newModel.bipedBody = convertBox((ModelRendererBends)newModel.bipedBody, original.bipedBody);
      newModel.bipedCloak = convertBox((ModelRendererBends)newModel.bipedCloak, original.bipedCloak);
      newModel.bipedEars = convertBox((ModelRendererBends)newModel.bipedEars, original.bipedEars);
      newModel.bipedHead = convertBox((ModelRendererBends)newModel.bipedHead, original.bipedHead);
      newModel.bipedHeadwear = convertBox((ModelRendererBends)newModel.bipedHeadwear, original.bipedHeadwear);
      newModel.bipedLeftArm = convertBox((ModelRendererBends)newModel.bipedLeftArm, original.bipedLeftArm);
      newModel.bipedLeftLeg = convertBox((ModelRendererBends)newModel.bipedBody, original.bipedBody);
      newModel.bipedRightArm = convertBox((ModelRendererBends)newModel.bipedBody, original.bipedBody);
      newModel.bipedRightLeg = convertBox((ModelRendererBends)newModel.bipedBody, original.bipedBody);
      CustomArmor newArmor = new CustomArmor(newModel, argTexture);
      return newArmor;
   }

   public static ModelRendererBends convertBox(ModelRendererBends argBox, ModelRenderer argOld) {
      if (argOld.childModels != null) {
         for(int i = 0; i < argOld.childModels.size(); ++i) {
            argBox.childModels.add(argOld.childModels.get(i));
         }
      }

      return argBox;
   }
}
