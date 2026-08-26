package goblinbob.mobends.standard.client.model.armor;

import com.mojang.blaze3d.matrix.MatrixStack;
import goblinbob.mobends.core.client.model.ModelPartTransform;
import goblinbob.mobends.standard.data.BipedEntityData;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

import javax.annotation.Nullable;

public class BoneTransformProvider
{
    private static final float MODEL_SCALE = 1.0f / 16.0f;

    private final CoordinateSpaceManager coordinateSpaceManager;

    public BoneTransformProvider()
    {
        this.coordinateSpaceManager = new CoordinateSpaceManager();
    }

    public BoneTransformProvider(CoordinateSpaceManager coordinateSpaceManager)
    {
        this.coordinateSpaceManager = coordinateSpaceManager;
    }

    @Nullable
    public ModelPartTransform getTransform(BoneRegion region, BipedEntityData<?> entityData)
    {
        if (entityData == null) return null;

        switch (region) {
case HEAD:
return entityData.head;
case BODY:
return entityData.body;
case LEFT_ARM_UPPER:
return entityData.leftArm;
case LEFT_ARM_LOWER:
return entityData.leftForeArm;
case RIGHT_ARM_UPPER:
return entityData.rightArm;
case RIGHT_ARM_LOWER:
return entityData.rightForeArm;
case LEFT_LEG_UPPER:
return entityData.leftLeg;
case LEFT_LEG_LOWER:
return entityData.leftForeLeg;
case RIGHT_LEG_UPPER:
return entityData.rightLeg;
case RIGHT_LEG_LOWER:
return entityData.rightForeLeg;
case ROOT:
return null;
        }
        return null;
    }

    public BoneRegion getParentRegion(BoneRegion region)
    {
        switch (region) {
case HEAD:
return BoneRegion.BODY;
case LEFT_ARM_UPPER:
case RIGHT_ARM_UPPER:
return BoneRegion.BODY;
case LEFT_ARM_LOWER:
return BoneRegion.LEFT_ARM_UPPER;
case RIGHT_ARM_LOWER:
return BoneRegion.RIGHT_ARM_UPPER;
case LEFT_LEG_LOWER:
return BoneRegion.LEFT_LEG_UPPER;
case RIGHT_LEG_LOWER:
return BoneRegion.RIGHT_LEG_UPPER;
default:
return BoneRegion.ROOT;
}
    }

    public boolean isChildOf(BoneRegion child, BoneRegion parent)
    {
        if (child == parent) return false;

        BoneRegion current = child;
        while (current != BoneRegion.ROOT)
        {
            current = getParentRegion(current);
            if (current == parent) return true;
        }
        return false;
    }

    public Quaternion getRotation(BoneRegion region, BipedEntityData<?> entityData)
    {
        ModelPartTransform transform = getTransform(region, entityData);
        if (transform == null || transform.rotation == null)
        {
            return new Quaternion(0.0F, 0.0F, 0.0F, 1.0F);
        }

        goblinbob.mobends.lib.math.Quaternion smoothRot = transform.rotation.getSmooth();
        if (smoothRot == null)
        {
            return new Quaternion(0.0F, 0.0F, 0.0F, 1.0F);
        }

        return new Quaternion(
            (float) smoothRot.x,
            (float) smoothRot.y,
            (float) smoothRot.z,
            (float) smoothRot.w
        );
    }

    public Vector3f getPosition(BoneRegion region, BipedEntityData<?> entityData)
    {
        ModelPartTransform transform = getTransform(region, entityData);
        if (transform == null)
        {
            return new Vector3f();
        }

        return new Vector3f(
            transform.position.x * MODEL_SCALE,
            transform.position.y * MODEL_SCALE,
            transform.position.z * MODEL_SCALE
        );
    }

    public Matrix4f getFullTransform(BoneRegion region, BipedEntityData<?> entityData)
    {
        return coordinateSpaceManager.getFullBoneTransform(region, entityData);
    }

    public void applyBoneTransform(MatrixStack poseStack, BoneRegion region, BipedEntityData<?> entityData)
    {
        ModelPartTransform transform = getTransform(region, entityData);
        if (transform != null)
        {
            coordinateSpaceManager.applyToPoseStack(poseStack, transform);
        }
    }

    public void applyFullBoneTransform(MatrixStack poseStack, BoneRegion region, BipedEntityData<?> entityData)
    {
        java.util.List<BoneRegion> chain = new java.util.ArrayList<>();
        BoneRegion current = region;
        while (current != BoneRegion.ROOT)
        {
            chain.add(0, current);
            current = getParentRegion(current);
        }

        for (BoneRegion boneRegion : chain)
        {
            applyBoneTransform(poseStack, boneRegion, entityData);
        }
    }

    public boolean hasAnimation(BoneRegion region, BipedEntityData<?> entityData)
    {
        ModelPartTransform transform = getTransform(region, entityData);
        if (transform == null)
        {
            return false;
        }

        goblinbob.mobends.lib.math.Quaternion smoothRot = transform.rotation.getSmooth();
        if (smoothRot != null)
        {
            if (Math.abs(smoothRot.w - 1.0) > 0.001 ||
                Math.abs(smoothRot.x) > 0.001 ||
                Math.abs(smoothRot.y) > 0.001 ||
                Math.abs(smoothRot.z) > 0.001)
            {
                return true;
            }
        }

        if (Math.abs(transform.offset.x) > 0.001 ||
            Math.abs(transform.offset.y) > 0.001 ||
            Math.abs(transform.offset.z) > 0.001)
        {
            return true;
        }

        return false;
    }

    public BoneRegion getUpperLimbRegion(BoneRegion lowerRegion)
    {
        switch (lowerRegion) {
case LEFT_ARM_LOWER:
return BoneRegion.LEFT_ARM_UPPER;
case RIGHT_ARM_LOWER:
return BoneRegion.RIGHT_ARM_UPPER;
case LEFT_LEG_LOWER:
return BoneRegion.LEFT_LEG_UPPER;
case RIGHT_LEG_LOWER:
return BoneRegion.RIGHT_LEG_UPPER;
default:
return lowerRegion;
}
    }

    public BoneRegion getLowerLimbRegion(BoneRegion upperRegion)
    {
        switch (upperRegion) {
case LEFT_ARM_UPPER:
return BoneRegion.LEFT_ARM_LOWER;
case RIGHT_ARM_UPPER:
return BoneRegion.RIGHT_ARM_LOWER;
case LEFT_LEG_UPPER:
return BoneRegion.LEFT_LEG_LOWER;
case RIGHT_LEG_UPPER:
return BoneRegion.RIGHT_LEG_LOWER;
default:
return upperRegion;
}
    }

    public boolean isLimbRegion(BoneRegion region)
    {
        switch (region) {
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

    public boolean isArmRegion(BoneRegion region)
    {
        switch (region) {
case LEFT_ARM_UPPER:
case LEFT_ARM_LOWER:
case RIGHT_ARM_UPPER:
case RIGHT_ARM_LOWER:
return true;
default:
return false;
}
    }

    public boolean isLegRegion(BoneRegion region)
    {
        switch (region) {
case LEFT_LEG_UPPER:
case LEFT_LEG_LOWER:
case RIGHT_LEG_UPPER:
case RIGHT_LEG_LOWER:
return true;
default:
return false;
}
    }
}
