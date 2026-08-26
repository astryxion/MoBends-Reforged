package goblinbob.mobends.standard.client.model.armor.tier;

import goblinbob.mobends.standard.client.model.armor.BoneRegion;
import net.minecraft.client.renderer.model.ModelRenderer;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PartClassification
{
    private final BoneRegion boneRegion;

    private final float confidence;

    private final ModelRenderer modelPart;

    private final Map<String, PartClassification> childClassifications;

    @Nullable
    private final String partName;

    private PartClassification(BoneRegion boneRegion,
                               float confidence,
                               ModelRenderer modelPart,
                               @Nullable String partName,
                               Map<String, PartClassification> childClassifications)
    {
        this.boneRegion = boneRegion;
        this.confidence = Math.max(0.0f, Math.min(1.0f, confidence));
        this.modelPart = modelPart;
        this.partName = partName;
        this.childClassifications = Collections.unmodifiableMap(new HashMap<>(childClassifications));
    }

    public static PartClassification of(BoneRegion boneRegion, float confidence, ModelRenderer modelPart, @Nullable String partName)
    {
        return new PartClassification(boneRegion, confidence, modelPart, partName, Collections.emptyMap());
    }

    public static PartClassification of(BoneRegion boneRegion, float confidence, ModelRenderer modelPart, @Nullable String partName, Map<String, PartClassification> children)
    {
        return new PartClassification(boneRegion, confidence, modelPart, partName, children);
    }

    public static PartClassification unknown(ModelRenderer modelPart, @Nullable String partName)
    {
        return new PartClassification(BoneRegion.ROOT, 0.0f, modelPart, partName, Collections.emptyMap());
    }

    public BoneRegion getBoneRegion()
    {
        return boneRegion;
    }

    public float getConfidence()
    {
        return confidence;
    }

    public ModelRenderer getModelPart()
    {
        return modelPart;
    }

    @Nullable
    public String getPartName()
    {
        return partName;
    }

    public Map<String, PartClassification> getChildClassifications()
    {
        return childClassifications;
    }

    public boolean isHighConfidence()
    {
        return confidence >= 0.8f;
    }

    public boolean isModerateConfidence()
    {
        return confidence >= 0.5f;
    }

    public boolean isLimbBone()
    {
        switch (boneRegion) {
case LEFT_ARM_UPPER:
case LEFT_ARM_LOWER:
case RIGHT_ARM_UPPER:
case RIGHT_ARM_LOWER:
case LEFT_LEG_UPPER:
case LEFT_LEG_LOWER:
case RIGHT_LEG_UPPER:
case RIGHT_LEG_LOWER:
return true;
default:
return false;
}
    }

    public boolean isUpperLimbSegment()
    {
        switch (boneRegion) {
case LEFT_ARM_UPPER:
case RIGHT_ARM_UPPER:
case LEFT_LEG_UPPER:
case RIGHT_LEG_UPPER:
return true;
default:
return false;
}
    }

    public boolean isLowerLimbSegment()
    {
        switch (boneRegion) {
case LEFT_ARM_LOWER:
case RIGHT_ARM_LOWER:
case LEFT_LEG_LOWER:
case RIGHT_LEG_LOWER:
return true;
default:
return false;
}
    }

    @Nullable
    public PartClassification getChildClassification(String childName)
    {
        return childClassifications.get(childName);
    }

    @Override
    public String toString()
    {
        return String.format("PartClassification{region=%s, confidence=%.2f, part=%s, children=%d}",
            boneRegion, confidence, partName, childClassifications.size());
    }
}
