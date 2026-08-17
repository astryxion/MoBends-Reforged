package net.gobbob.mobends.client.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import net.gobbob.mobends.client.model.entity.ModelBendsPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

/**
 * Renders another mod's vanilla-layout {@link ModelBiped} on Mo' Bends bones.
 * Cube-copy conversion drops extra children (TF horns, LOTR crests) and uses the
 * wrong pivots, which is why 3D helmets float. This path postRenders the bent
 * bone then draws the original part at identity, the same idea as Hats glue.
 */
public class ModelArmorGlue extends ModelBiped {
   private static final Map<ModelBiped, ModelArmorGlue> CACHE = new IdentityHashMap<ModelBiped, ModelArmorGlue>();
   private static final float NECK_FROM_HIPS = -12.0F;

   private static ModelRenderer explorersHatRestPose;

   private final ModelBiped original;
   private final boolean explorersHat;
   private final boolean fiery;
   private final List<ModelRenderer> extraHeadParts = new ArrayList<ModelRenderer>();
   private final List<ModelRenderer> extraBodyParts = new ArrayList<ModelRenderer>();
   private ModelBendsPlayer sourceModel;

   public static ModelArmorGlue get(ModelBiped original) {
      ModelArmorGlue glue = CACHE.get(original);
      if (glue == null) {
         glue = new ModelArmorGlue(original);
         CACHE.put(original, glue);
      }
      return glue;
   }

   public static boolean needsGlue(ModelBiped model) {
      return model != null
            && !(model instanceof ModelBendsPlayer)
            && !(model instanceof ModelCustomArmor)
            && !(model instanceof ModelArmorGlue)
            && model.getClass() != ModelBiped.class;
   }

   public void setSourceModel(ModelBendsPlayer model) {
      this.sourceModel = model;
   }

   private ModelArmorGlue(ModelBiped original) {
      this.original = original;
      String name = original.getClass().getName();
      this.explorersHat = name.endsWith("ModelExplorersHat");
      this.fiery = name.contains("Fiery");
      this.collectExtraParts();
   }

   private void collectExtraParts() {
      List<ModelRenderer> standard = new ArrayList<ModelRenderer>();
      addIfPresent(standard, this.original.bipedHead);
      addIfPresent(standard, this.original.bipedHeadwear);
      addIfPresent(standard, this.original.bipedBody);
      addIfPresent(standard, this.original.bipedRightArm);
      addIfPresent(standard, this.original.bipedLeftArm);
      addIfPresent(standard, this.original.bipedRightLeg);
      addIfPresent(standard, this.original.bipedLeftLeg);

      for (Class<?> type = this.original.getClass(); type != null && type != ModelBiped.class && type != Object.class; type = type.getSuperclass()) {
         Field[] fields = type.getDeclaredFields();
         for (int i = 0; i < fields.length; ++i) {
            Field field = fields[i];
            if (!ModelRenderer.class.isAssignableFrom(field.getType())) {
               continue;
            }
            field.setAccessible(true);
            ModelRenderer extra;
            try {
               extra = (ModelRenderer) field.get(this.original);
            } catch (Throwable t) {
               continue;
            }
            if (extra == null || standard.contains(extra) || isDescendantOfAny(standard, extra)) {
               continue;
            }
            String fieldName = field.getName().toLowerCase();
            if (fieldName.contains("hat") || fieldName.contains("head") || fieldName.contains("hood")
                  || fieldName.contains("rim") || fieldName.contains("crest") || fieldName.contains("horn")
                  || fieldName.contains("jaw") || fieldName.contains("visor")) {
               this.extraHeadParts.add(extra);
            } else {
               this.extraBodyParts.add(extra);
            }
         }
      }
   }

   public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
      if (this.sourceModel == null || this.original == null) {
         return;
      }

      if (this.fiery) {
         Minecraft.getMinecraft().entityRenderer.disableLightmap(0.0D);
      }

      if (this.bipedHead.showModel || this.bipedHeadwear.showModel) {
         GL11.glPushMatrix();
         this.sourceModel.bipedBody.postRender(scale);
         this.sourceModel.bipedHead.postRender(scale);
         if (this.explorersHat) {
            renderRestPoseExplorersHat(scale);
         } else {
            if (this.bipedHead.showModel && isPartVisible(this.original.bipedHead)) {
               renderVanillaPart(this.original.bipedHead, scale);
            }
            if (this.bipedHeadwear.showModel && isPartVisible(this.original.bipedHeadwear)) {
               renderVanillaPart(this.original.bipedHeadwear, scale);
            }
            for (int i = 0; i < this.extraHeadParts.size(); ++i) {
               if (isPartVisible(this.extraHeadParts.get(i))) {
                  renderVanillaPart(this.extraHeadParts.get(i), scale);
               }
            }
         }
         GL11.glPopMatrix();
      }

      if (this.bipedBody.showModel) {
         GL11.glPushMatrix();
         this.sourceModel.bipedBody.postRender(scale);
         GL11.glTranslatef(0.0F, NECK_FROM_HIPS * scale, 0.0F);
         if (isPartVisible(this.original.bipedBody)) {
            renderVanillaPart(this.original.bipedBody, scale);
         }
         for (int i = 0; i < this.extraBodyParts.size(); ++i) {
            if (isPartVisible(this.extraBodyParts.get(i))) {
               renderVanillaPart(this.extraBodyParts.get(i), scale);
            }
         }
         GL11.glPopMatrix();
      }

      if (this.bipedRightArm.showModel && isPartVisible(this.original.bipedRightArm)) {
         this.glueUpperArm(this.sourceModel.bipedRightArm, this.original.bipedRightArm, scale);
      }
      if (this.bipedLeftArm.showModel && isPartVisible(this.original.bipedLeftArm)) {
         this.glueUpperArm(this.sourceModel.bipedLeftArm, this.original.bipedLeftArm, scale);
      }
      if (this.bipedRightLeg.showModel && isPartVisible(this.original.bipedRightLeg)) {
         this.glueLimb(this.sourceModel.bipedRightLeg, this.original.bipedRightLeg, scale);
      }
      if (this.bipedLeftLeg.showModel && isPartVisible(this.original.bipedLeftLeg)) {
         this.glueLimb(this.sourceModel.bipedLeftLeg, this.original.bipedLeftLeg, scale);
      }

      if (this.fiery) {
         Minecraft.getMinecraft().entityRenderer.enableLightmap(0.0D);
      }
   }

   private void glueUpperArm(ModelRenderer bendsArm, ModelRenderer vanillaArm, float scale) {
      GL11.glPushMatrix();
      this.sourceModel.bipedBody.postRender(scale);
      if (bendsArm instanceof ModelRendererBends) {
         ((ModelRendererBends) bendsArm).postRenderSelf(scale);
      } else {
         bendsArm.postRender(scale);
      }
      renderVanillaPart(vanillaArm, scale);
      GL11.glPopMatrix();
   }

   private void glueLimb(ModelRenderer bendsLimb, ModelRenderer vanillaLimb, float scale) {
      GL11.glPushMatrix();
      if (bendsLimb instanceof ModelRendererBends) {
         ((ModelRendererBends) bendsLimb).postRenderSelf(scale);
      } else {
         bendsLimb.postRender(scale);
      }
      renderVanillaPart(vanillaLimb, scale);
      GL11.glPopMatrix();
   }

   public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
      // Poses come from the Mo' Bends source model, not vanilla biped angles.
   }

   static boolean isPartVisible(ModelRenderer part) {
      return part != null && !part.isHidden && part.showModel;
   }

   public static void renderVanillaPartWithoutChildren(ModelRenderer part, float scale) {
      if (part == null) {
         return;
      }
      java.util.List children = part.childModels;
      part.childModels = null;
      renderVanillaPart(part, scale);
      part.childModels = children;
   }

   public static void renderVanillaPart(ModelRenderer part, float scale) {
      if (part == null) {
         return;
      }
      float rotateX = part.rotateAngleX;
      float rotateY = part.rotateAngleY;
      float rotateZ = part.rotateAngleZ;
      float pointX = part.rotationPointX;
      float pointY = part.rotationPointY;
      float pointZ = part.rotationPointZ;
      boolean hidden = part.isHidden;
      boolean show = part.showModel;
      part.rotateAngleX = 0.0F;
      part.rotateAngleY = 0.0F;
      part.rotateAngleZ = 0.0F;
      part.rotationPointX = 0.0F;
      part.rotationPointY = 0.0F;
      part.rotationPointZ = 0.0F;
      part.isHidden = false;
      part.showModel = true;
      part.render(scale);
      part.rotateAngleX = rotateX;
      part.rotateAngleY = rotateY;
      part.rotateAngleZ = rotateZ;
      part.rotationPointX = pointX;
      part.rotationPointY = pointY;
      part.rotationPointZ = pointZ;
      part.isHidden = hidden;
      part.showModel = show;
   }

   private static void renderRestPoseExplorersHat(float scale) {
      ModelRenderer hatrim = explorersHatRestPose();
      if (hatrim != null) {
         hatrim.render(scale);
      }
   }

   private static ModelRenderer explorersHatRestPose() {
      if (explorersHatRestPose != null) {
         return explorersHatRestPose;
      }
      try {
         Object hat = Class.forName("thebetweenlands.client.model.item.ModelExplorersHat").newInstance();
         Field field = hat.getClass().getField("hatrim");
         explorersHatRestPose = (ModelRenderer) field.get(hat);
      } catch (Throwable t) {
         explorersHatRestPose = null;
      }
      return explorersHatRestPose;
   }

   private static void addIfPresent(List<ModelRenderer> list, ModelRenderer part) {
      if (part != null) {
         list.add(part);
      }
   }

   private static boolean isDescendantOfAny(List<ModelRenderer> roots, ModelRenderer extra) {
      for (int i = 0; i < roots.size(); ++i) {
         if (isDescendant(roots.get(i), extra)) {
            return true;
         }
      }
      return false;
   }

   private static boolean isDescendant(ModelRenderer root, ModelRenderer extra) {
      if (root == extra) {
         return true;
      }
      if (root == null || root.childModels == null) {
         return false;
      }
      for (int i = 0; i < root.childModels.size(); ++i) {
         if (isDescendant((ModelRenderer) root.childModels.get(i), extra)) {
            return true;
         }
      }
      return false;
   }
}
