package net.gobbob.mobends.compat.skinlayers3d;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.gobbob.mobends.client.model.entity.ModelBendsPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Glue for 3D Skin Layers on Mo' Bends.
 * <p>
 * Stock Skin Layers only {@code postRender}s each limb. That works for legs (vanilla pivots) and
 * for arms ({@code ModelRendererBends_SeperatedChild} already posts the body mother), but:
 * <ul>
 *   <li>Head is a body child with {@code rotationPointY = -12} — without body first, hair floats up.</li>
 *   <li>Body pivot is at the hips (not the neck) — jacket voxels sit at the waist (gray "belt").</li>
 * </ul>
 */
@SideOnly(Side.CLIENT)
public final class SkinLayersGluedRenderer {

   private static final float SCALE = 0.0625F;
   /** Mo' Bends body pivot is at hips; Skin Layers jacket expects neck (vanilla body origin). */
   private static final float BODY_TO_NECK = -12.0F * SCALE;
   private static final float HEIGHT_SCALING = 1.035F;
   private static final float Y_BODY = 0.6F;
   private static final float Y_LEGS = -0.2F;
   private static final float Y_ARMS = 0.4F;

   private static boolean ready;

   private static Class<?> playerSettingsClass;
   private static Method setup3dLayers;
   private static Method hasCustomSkin;
   private static Method hasThinArms;
   private static Method squareDistance;
   private static Method bindFullSkin;
   private static Method prepareTexturedModelDraw;
   private static Method restoreTexturedModelDraw;
   private static Method getHeadLayers;
   private static Method getSkinLayers;
   private static Method setupHeadLayers;
   private static Method setupSkinLayers;
   private static Method partIsEmpty;
   private static Method partRender;
   private static Field partX;
   private static Field partY;

   private static Field configField;
   private static Field enableHatField;
   private static Field enableJacketField;
   private static Field enableLeftSleeveField;
   private static Field enableRightSleeveField;
   private static Field enableLeftPantsField;
   private static Field enableRightPantsField;
   private static Field baseVoxelSizeField;
   private static Field bodyVoxelWidthSizeField;
   private static Field headVoxelSizeField;
   private static Field renderDistanceLODField;

   private SkinLayersGluedRenderer() {
   }

   public static void init() throws Exception {
      playerSettingsClass = Class.forName("dev.tr7zw.skinlayers.accessor.PlayerSettings");
      Class<?> skinUtil = Class.forName("dev.tr7zw.skinlayers.SkinUtil");
      Class<?> modBase = Class.forName("dev.tr7zw.skinlayers.SkinLayersModBase");
      Class<?> fullSkin = Class.forName("dev.tr7zw.skinlayers.FullSkinTextureManager");
      Class<?> renderState = Class.forName("dev.tr7zw.skinlayers.opengl.RenderState");
      Class<?> modelPart = Class.forName("dev.tr7zw.skinlayers.render.CustomizableModelPart");
      Class<?> configClass = Class.forName("dev.tr7zw.skinlayers.Config");

      configField = modBase.getField("config");
      enableHatField = configClass.getField("enableHat");
      enableJacketField = configClass.getField("enableJacket");
      enableLeftSleeveField = configClass.getField("enableLeftSleeve");
      enableRightSleeveField = configClass.getField("enableRightSleeve");
      enableLeftPantsField = configClass.getField("enableLeftPants");
      enableRightPantsField = configClass.getField("enableRightPants");
      baseVoxelSizeField = configClass.getField("baseVoxelSize");
      bodyVoxelWidthSizeField = configClass.getField("bodyVoxelWidthSize");
      headVoxelSizeField = configClass.getField("headVoxelSize");
      renderDistanceLODField = configClass.getField("renderDistanceLOD");

      hasCustomSkin = skinUtil.getMethod("hasCustomSkin", AbstractClientPlayer.class);
      hasThinArms = skinUtil.getMethod("hasThinArms", AbstractClientPlayer.class);
      squareDistance = skinUtil.getMethod("squareDistance", AbstractClientPlayer.class, AbstractClientPlayer.class);
      setup3dLayers = skinUtil.getMethod("setup3dLayers", AbstractClientPlayer.class, playerSettingsClass,
            Boolean.TYPE, net.minecraft.client.model.ModelBiped.class);

      bindFullSkin = fullSkin.getMethod("bindFullSkin", AbstractClientPlayer.class);
      prepareTexturedModelDraw = renderState.getMethod("prepareTexturedModelDraw");
      restoreTexturedModelDraw = renderState.getMethod("restoreTexturedModelDraw");

      getHeadLayers = playerSettingsClass.getMethod("getHeadLayers");
      getSkinLayers = playerSettingsClass.getMethod("getSkinLayers");
      setupHeadLayers = playerSettingsClass.getMethod("setupHeadLayers", modelPart);
      setupSkinLayers = playerSettingsClass.getMethod("setupSkinLayers",
            Class.forName("[Ldev.tr7zw.skinlayers.render.CustomizableModelPart;"));

      partIsEmpty = modelPart.getMethod("isEmpty");
      partRender = modelPart.getMethod("render", Boolean.TYPE);
      partX = modelPart.getField("x");
      partY = modelPart.getField("y");

      ready = true;
   }

   public static boolean isReady() {
      return ready;
   }

   /**
    * First-person right sleeve only. Mo' Bends overrides {@code renderFirstPersonArm} so Skin
    * Layers' mixin never runs — glue the sleeve onto the same arm bone that was just drawn.
    * Do not use bent {@code modelBipedMain} here (SeparatedChild would post the body).
    */
   public static void renderFirstPersonRightSleeve(AbstractClientPlayer player, ModelRenderer rightArm) {
      if (!ready || player == null || rightArm == null || player.isInvisible()) {
         return;
      }
      try {
         Object config = configField.get(null);
         if (config == null || !Boolean.TRUE.equals(hasCustomSkin.invoke(null, player))) {
            return;
         }
         if (!Boolean.TRUE.equals(enableRightSleeveField.get(config))) {
            return;
         }
         if (!playerSettingsClass.isInstance(player)) {
            return;
         }

         ensureLayersBuilt(player);
         Object[] layers = (Object[]) getSkinLayers.invoke(player);
         if (layers == null || layers.length <= 3 || layers[3] == null || isEmpty(layers[3])) {
            return;
         }

         boolean thinArms = Boolean.TRUE.equals(hasThinArms.invoke(null, player));
         float pixelScaling = baseVoxelSizeField.getFloat(config);
         // Match stock Skin Layers FP: right sleeve index 3, negative X.
         float x = thinArms ? -0.499F * 16.0F : -0.998F * 16.0F;

         // Prefer the 64x64 atlas, but still draw if only the normal skin is available
         // (bindFullSkin returns false after falling back — stock FP never required it).
         bindFullSkin.invoke(null, player);
         prepareTexturedModelDraw.invoke(null);
         GL11.glPushMatrix();
         rightArm.postRender(SCALE);
         partX.setFloat(layers[3], x);
         // Stock FP only sets X and uses uniform voxel scale (no body-layer Y magic).
         GL11.glScalef(SCALE, SCALE, SCALE);
         GL11.glScalef(pixelScaling, pixelScaling, pixelScaling);
         partRender.invoke(layers[3], Boolean.FALSE);
         GL11.glPopMatrix();
         restoreTexturedModelDraw.invoke(null);
      } catch (Throwable t) {
         Throwable cause = t.getCause() != null ? t.getCause() : t;
         System.err.println("(MO'BENDS) 3D Skin Layers first-person sleeve error: " + cause);
         cause.printStackTrace();
      }
   }

   public static void renderLayers(AbstractClientPlayer player, ModelBendsPlayer model, float scale) {
      if (!ready || player == null || model == null || player.isInvisible()) {
         return;
      }

      try {
         Object config = configField.get(null);
         if (config == null || !Boolean.TRUE.equals(hasCustomSkin.invoke(null, player))) {
            return;
         }
         if (!playerSettingsClass.isInstance(player)) {
            return;
         }

         Minecraft mc = Minecraft.getMinecraft();
         if (mc.theWorld == null || mc.thePlayer == null) {
            return;
         }
         int lod = renderDistanceLODField.getInt(config);
         double distSq = ((Double) squareDistance.invoke(null, mc.thePlayer, player)).doubleValue();
         if (distSq > (double) lod * (double) lod) {
            return;
         }

         ensureLayersBuilt(player);
         if (!Boolean.TRUE.equals(bindFullSkin.invoke(null, player))) {
            return;
         }

         boolean redTint = player.hurtTime > 0 || player.deathTime > 0;
         boolean thinArms = Boolean.TRUE.equals(hasThinArms.invoke(null, player));
         float pixelScaling = baseVoxelSizeField.getFloat(config);

         prepareTexturedModelDraw.invoke(null);

         if (Boolean.TRUE.equals(enableHatField.get(config)) && !wearingSkull(player) && !model.bipedHead.isHidden) {
            Object head = getHeadLayers.invoke(player);
            if (head != null && !isEmpty(head)) {
               model.bipedHeadwear.isHidden = true;
               drawHead(player, model, head, headVoxelSizeField.getFloat(config), redTint);
            }
         }

         Object[] layers = (Object[]) getSkinLayers.invoke(player);
         if (layers != null) {
            // Stock order: left pants, right pants, left sleeve, right sleeve, jacket
            if (Boolean.TRUE.equals(enableLeftPantsField.get(config)) && !model.bipedLeftLeg.isHidden) {
               drawLimb(player, model.bipedLeftLeg, layers, 0, Y_LEGS, pixelScaling, 0.0F, redTint);
            }
            if (Boolean.TRUE.equals(enableRightPantsField.get(config)) && !model.bipedRightLeg.isHidden) {
               drawLimb(player, model.bipedRightLeg, layers, 1, Y_LEGS, pixelScaling, 0.0F, redTint);
            }

            float armX = thinArms ? 0.499F * 16.0F : 0.998F * 16.0F;
            if (Boolean.TRUE.equals(enableLeftSleeveField.get(config)) && !model.bipedLeftArm.isHidden) {
               drawLimb(player, model.bipedLeftArm, layers, 2, Y_ARMS, pixelScaling, armX, redTint);
            }
            if (Boolean.TRUE.equals(enableRightSleeveField.get(config)) && !model.bipedRightArm.isHidden) {
               drawLimb(player, model.bipedRightArm, layers, 3, Y_ARMS, pixelScaling, -armX, redTint);
            }

            if (Boolean.TRUE.equals(enableJacketField.get(config)) && !model.bipedBody.isHidden) {
               drawJacket(player, model, layers, 4, bodyVoxelWidthSizeField.getFloat(config), pixelScaling, redTint);
            }
         }

         restoreTexturedModelDraw.invoke(null);
      } catch (Throwable t) {
         Throwable cause = t.getCause() != null ? t.getCause() : t;
         System.err.println("(MO'BENDS) 3D Skin Layers render error: " + cause);
         cause.printStackTrace();
      }
   }

   private static void ensureLayersBuilt(AbstractClientPlayer player) throws Exception {
      Object head = getHeadLayers.invoke(player);
      if (head != null && isEmpty(head)) {
         setupHeadLayers.invoke(player, (Object) null);
         head = null;
      }
      Object[] body = (Object[]) getSkinLayers.invoke(player);
      if (body != null && !hasAny(body)) {
         setupSkinLayers.invoke(player, (Object) null);
         body = null;
      }
      if (head == null || body == null) {
         boolean thin = Boolean.TRUE.equals(hasThinArms.invoke(null, player));
         setup3dLayers.invoke(null, player, player, Boolean.valueOf(thin), null);
      }
   }

   private static void drawHead(AbstractClientPlayer player, ModelBendsPlayer model, Object head, float voxelSize,
         boolean redTint) throws Exception {
      GL11.glPushMatrix();
      // No vanilla +0.2 sneak translate — Mo' Bends crouch is bone rotations only.
      // Head is a child of body — must post body first or hair floats above the skull.
      model.bipedBody.postRender(SCALE);
      model.bipedHead.postRender(SCALE);
      GL11.glScalef(SCALE, SCALE, SCALE);
      GL11.glScalef(voxelSize, voxelSize, voxelSize);
      partRender.invoke(head, Boolean.valueOf(redTint));
      GL11.glPopMatrix();
   }

   private static void drawJacket(AbstractClientPlayer player, ModelBendsPlayer model, Object[] layers, int index,
         float widthScaling, float pixelScaling, boolean redTint) throws Exception {
      if (layers == null || index >= layers.length || layers[index] == null || isEmpty(layers[index])) {
         return;
      }
      GL11.glPushMatrix();
      model.bipedBody.postRender(SCALE);
      // Move from hip pivot up to neck so jacket voxels match vanilla body space.
      GL11.glTranslatef(0.0F, BODY_TO_NECK, 0.0F);
      partX.setFloat(layers[index], 0.0F);
      partY.setFloat(layers[index], Y_BODY);
      GL11.glScalef(SCALE, SCALE, SCALE);
      GL11.glScalef(widthScaling, HEIGHT_SCALING, pixelScaling);
      partRender.invoke(layers[index], Boolean.valueOf(redTint));
      GL11.glPopMatrix();
   }

   private static void drawLimb(AbstractClientPlayer player, ModelRenderer bone, Object[] layers, int index,
         float yMagic, float pixelScaling, float xMagic, boolean redTint) throws Exception {
      if (bone == null || bone.isHidden || layers == null || index >= layers.length || layers[index] == null
            || isEmpty(layers[index])) {
         return;
      }
      GL11.glPushMatrix();
      // Arms already post mother body via SeparatedChild; legs use vanilla-like pivots.
      bone.postRender(SCALE);
      partX.setFloat(layers[index], xMagic);
      partY.setFloat(layers[index], yMagic);
      GL11.glScalef(SCALE, SCALE, SCALE);
      GL11.glScalef(pixelScaling, HEIGHT_SCALING, pixelScaling);
      partRender.invoke(layers[index], Boolean.valueOf(redTint));
      GL11.glPopMatrix();
   }

   private static boolean wearingSkull(AbstractClientPlayer player) {
      ItemStack stack = player.inventory.armorItemInSlot(3);
      return stack != null && stack.getItem() == Items.skull;
   }

   private static boolean isEmpty(Object part) throws Exception {
      return part == null || Boolean.TRUE.equals(partIsEmpty.invoke(part));
   }

   private static boolean hasAny(Object[] layers) throws Exception {
      if (layers == null) {
         return false;
      }
      for(int i = 0; i < layers.length; ++i) {
         if (layers[i] != null && !isEmpty(layers[i])) {
            return true;
         }
      }
      return false;
   }
}
