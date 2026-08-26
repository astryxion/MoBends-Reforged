package goblinbob.mobends.api.skeleton;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Quaternion;

import javax.annotation.Nullable;

public interface IAnimatedSkeleton
{
    @Nullable
    IBoneTransform getBone(MoBendsBone bone);

    boolean hasBone(MoBendsBone bone);

    @Nullable
    default LivingEntity getEntity()
    {
        return null;
    }

    @Nullable
    default Quaternion getBoneRotation(MoBendsBone bone)
    {
        return null;
    }

    @Nullable
    default Vector3d getBoneModelPosition(MoBendsBone bone)
    {
        return null;
    }

    @Nullable
    default Vector3d getBoneWorldPosition(MoBendsBone bone, float partialTicks)
    {
        return null;
    }
}
