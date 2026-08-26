package goblinbob.mobends.standard.client.model.armor;

import goblinbob.mobends.core.util.ResourceLocationFactory;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ImmersiveArmorsSupport
{
    private static final boolean AVAILABLE;

    private static Class<?> materialClass;
    private static Class<?> layerPieceClass;
    private static Method materialGetPieces;
    private static Method pieceGetTexture;
    private static Method pieceIsTranslucent;
    private static Method pieceIsGlowing;
    private static Method pieceIsColored;
    private static Method pieceGetModel;

    private static final Map<String, List<Layer>> CACHE = new ConcurrentHashMap<>();

    static
    {
        boolean available = false;

        try
        {
            materialClass = Class.forName("immersive_armors.item.ExtendedArmorMaterial");
            layerPieceClass = Class.forName("immersive_armors.client.render.entity.piece.LayerPiece");

            Class<?> pieceClass = Class.forName("immersive_armors.client.render.entity.piece.Piece");

            materialGetPieces = materialClass.getMethod("getPieces", EquipmentSlotType.class);
            pieceGetTexture = pieceClass.getMethod("getTexture");
            pieceIsTranslucent = pieceClass.getMethod("isTranslucent");
            pieceIsGlowing = pieceClass.getMethod("isGlowing");
            pieceIsColored = pieceClass.getMethod("isColored");

            pieceGetModel = layerPieceClass.getDeclaredMethod("getModel");
            pieceGetModel.setAccessible(true);

            available = true;
        }
        catch (Throwable ignored)
        {
        }

        AVAILABLE = available;
    }

    private ImmersiveArmorsSupport()
    {
    }

    public static boolean isAvailable()
    {
        return AVAILABLE;
    }

    public static List<Layer> getLayers(ArmorItem armorItem, EquipmentSlotType slot, String materialName)
    {
        if (!AVAILABLE || armorItem == null || materialName == null || materialName.isEmpty())
        {
            return Collections.emptyList();
        }

        Object material = armorItem.getMaterial();
        if (!materialClass.isInstance(material))
        {
            return Collections.emptyList();
        }

        String key = armorItem.getDescriptionId() + "|" + slot;
        List<Layer> cached = CACHE.get(key);
        if (cached != null)
        {
            return cached;
        }

        List<Layer> layers = new ArrayList<>();

        try
        {
            Object pieces = materialGetPieces.invoke(material, slot);

            if (pieces instanceof List<?>) {
            List<?> pieceList = (List<?>) pieces;
                for (Object piece : pieceList)
                {
                    Layer layer = readLayer(piece, materialName);

                    if (layer != null)
                    {
                        layers.add(layer);
                    }
                }
            }
        }
        catch (Throwable ignored)
        {
        }

        List<Layer> result = Collections.unmodifiableList(layers);
        CACHE.put(key, result);

        return result;
    }

    private static Layer readLayer(Object piece, String materialName) throws Exception
    {
        if (piece == null || !layerPieceClass.isInstance(piece))
        {
            return null;
        }

        Object textureName = pieceGetTexture.invoke(piece);
        if (!(textureName instanceof String))
        {
            return null;
        }
        String texture = (String) textureName;
        if (texture.isEmpty())
        {
            return null;
        }

        Object model = pieceGetModel.invoke(piece);
        if (!(model instanceof BipedModel<?>))
        {
            return null;
        }
        BipedModel<?> humanoidModel = (BipedModel<?>) model;

        String base = "textures/models/armor/" + materialName + "/" + texture;

        return new Layer(
                humanoidModel,
                ResourceLocationFactory.create("immersive_armors", base + ".png"),
                ResourceLocationFactory.create("immersive_armors", base + "_overlay.png"),
                Boolean.TRUE.equals(pieceIsTranslucent.invoke(piece)),
                Boolean.TRUE.equals(pieceIsGlowing.invoke(piece)),
                Boolean.TRUE.equals(pieceIsColored.invoke(piece)));
    }

    public static final class Layer
    {
        public final BipedModel<?> model;
        public final ResourceLocation texture;
        public final ResourceLocation overlayTexture;
        public final boolean translucent;
        public final boolean glowing;
        public final boolean colored;

        private Layer(BipedModel<?> model, ResourceLocation texture, ResourceLocation overlayTexture,
                      boolean translucent, boolean glowing, boolean colored)
        {
            this.model = model;
            this.texture = texture;
            this.overlayTexture = overlayTexture;
            this.translucent = translucent;
            this.glowing = glowing;
            this.colored = colored;
        }
    }
}
