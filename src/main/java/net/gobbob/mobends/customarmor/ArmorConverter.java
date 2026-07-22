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
      newModel.bipedLeftLeg = convertBox((ModelRendererBends)newModel.bipedLeftLeg, original.bipedLeftLeg);
      newModel.bipedRightArm = convertBox((ModelRendererBends)newModel.bipedRightArm, original.bipedRightArm);
      newModel.bipedRightLeg = convertBox((ModelRendererBends)newModel.bipedRightLeg, original.bipedRightLeg);
      CustomArmor newArmor = new CustomArmor(newModel, argTexture);
      return newArmor;
   }

   public static ModelRendererBends convertBox(ModelRendererBends argBox, ModelRenderer argOld) {
      if (argOld == null) {
         return argBox;
      }

      argBox.cubeList.clear();
      if (argOld.cubeList != null) {
         for (int i = 0; i < argOld.cubeList.size(); ++i) {
            argBox.cubeList.add(argOld.cubeList.get(i));
         }
      }

      argBox.mirror = argOld.mirror;
      argBox.compiled = false;

      return argBox;
   }
}
