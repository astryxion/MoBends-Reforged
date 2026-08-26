package goblinbob.mobends.core.client.skeleton;

import goblinbob.mobends.api.skeleton.IAnimatedSkeleton;
import goblinbob.mobends.api.skeleton.IBoneTransform;
import goblinbob.mobends.api.skeleton.MoBendsBone;
import goblinbob.mobends.core.client.model.IModelPart;
import goblinbob.mobends.lib.math.Quaternion;
import goblinbob.mobends.lib.math.vector.IVec3fRead;
import goblinbob.mobends.standard.data.BipedEntityData;
import goblinbob.mobends.standard.mutators.BipedMutator;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class BipedSkeleton implements IAnimatedSkeleton
{
    private static final float MODEL_TO_BLOCKS = 1.0F / 16.0F;
    private static final double MODEL_ORIGIN_HEIGHT = 1.501;
    private static final float CHILD_SCALE = 0.5F;

    private final Function<MoBendsBone, IModelPart> resolver;
    private final LivingEntity entity;

    private BipedSkeleton(Function<MoBendsBone, IModelPart> resolver, @Nullable LivingEntity entity)
    {
        this.resolver = resolver;
        this.entity = entity;
    }

    public static BipedSkeleton of(BipedMutator<?, ?, ?> mutator, @Nullable LivingEntity entity)
    {
        return new BipedSkeleton(bone -> resolveFromMutator(mutator, bone), entity);
    }

    public static BipedSkeleton of(BipedEntityData<?> data)
    {
        return new BipedSkeleton(bone -> resolveFromData(data, bone), data.getEntity());
    }

    @Override
    public boolean hasBone(MoBendsBone bone)
    {
        return resolve(bone) != null;
    }

    @Override
    public LivingEntity getEntity()
    {
        return this.entity;
    }

    @Override
    public IBoneTransform getBone(MoBendsBone bone)
    {
        final IModelPart part = resolve(bone);
        if (part == null)
        {
            return null;
        }
        return new BoneTransform(part);
    }

    @Override
    public net.minecraft.util.math.vector.Quaternion getBoneRotation(MoBendsBone bone)
    {
        final IModelPart part = resolve(bone);
        return part == null ? null : toMc(part.getRotation().getSmooth());
    }

    @Override
    public Vector3d getBoneModelPosition(MoBendsBone bone)
    {
        final IModelPart part = resolve(bone);
        if (part == null)
        {
            return null;
        }

        final Vector3f position = accumulateModelPosition(part);
        return new Vector3d(position.x, position.y, position.z);
    }

    @Override
    public Vector3d getBoneWorldPosition(MoBendsBone bone, float partialTicks)
    {
        if (this.entity == null)
        {
            return null;
        }

        final IModelPart part = resolve(bone);
        if (part == null)
        {
            return null;
        }

        final Vector3f model = accumulateModelPosition(part);
        final float scale = this.entity.isBaby() ? CHILD_SCALE : 1.0F;

        final double localX = -model.x * MODEL_TO_BLOCKS * scale;
        final double localY = (MODEL_ORIGIN_HEIGHT - model.y * MODEL_TO_BLOCKS) * scale;
        final double localZ = model.z * MODEL_TO_BLOCKS * scale;

        final float bodyYaw = MathHelper.rotLerp(partialTicks, this.entity.yBodyRotO, this.entity.yBodyRot);
        final float angle = (180.0F - bodyYaw) * (float) (Math.PI / 180.0);
        final float sin = MathHelper.sin(angle);
        final float cos = MathHelper.cos(angle);

        return new Vector3d(
                MathHelper.lerp(partialTicks, this.entity.xOld, this.entity.getX()) + localX * cos + localZ * sin,
                MathHelper.lerp(partialTicks, this.entity.yOld, this.entity.getY()) + localY,
                MathHelper.lerp(partialTicks, this.entity.zOld, this.entity.getZ()) - localX * sin + localZ * cos);
    }

    private static Vector3f accumulateModelPosition(IModelPart part)
    {
        final List<IModelPart> chain = new ArrayList<>(4);
        for (IModelPart current = part; current != null; current = current.getParent())
        {
            chain.add(current);
        }

        final Vector3f position = new Vector3f();
        final net.minecraft.util.math.vector.Quaternion rotation = new net.minecraft.util.math.vector.Quaternion(0.0F, 0.0F, 0.0F, 1.0F);

        for (int i = chain.size() - 1; i >= 0; --i)
        {
            final IModelPart current = chain.get(i);
            final IVec3fRead local = current.getPosition();
            final IVec3fRead offset = current.getOffset();

            final Vector3f step = new Vector3f(
                    local.getX() + offset.getX(),
                    local.getY() + offset.getY(),
                    local.getZ() + offset.getZ());

            step.transform(rotation);
            position.add(step);
            rotation.mul(toMc(current.getRotation().getSmooth()));
        }

        return position;
    }

    private static net.minecraft.util.math.vector.Quaternion toMc(Quaternion quaternion)
    {
        return new net.minecraft.util.math.vector.Quaternion(quaternion.x, quaternion.y, quaternion.z, quaternion.w);
    }

    private IModelPart resolve(MoBendsBone bone)
    {
        return bone == null ? null : this.resolver.apply(bone);
    }

    private static IModelPart resolveFromMutator(BipedMutator<?, ?, ?> mutator, MoBendsBone bone)
    {
        switch (bone)
        {
            case HEAD: return mutator.getHead();
            case BODY: return mutator.getBody();
            case LEFT_ARM: return mutator.getLeftArm();
            case LEFT_FORE_ARM: return mutator.getLeftForeArm();
            case RIGHT_ARM: return mutator.getRightArm();
            case RIGHT_FORE_ARM: return mutator.getRightForeArm();
            case LEFT_LEG: return mutator.getLeftLeg();
            case LEFT_FORE_LEG: return mutator.getLeftForeLeg();
            case RIGHT_LEG: return mutator.getRightLeg();
            case RIGHT_FORE_LEG: return mutator.getRightForeLeg();
            default: return null;
        }
    }

    private static IModelPart resolveFromData(BipedEntityData<?> data, MoBendsBone bone)
    {
        switch (bone)
        {
            case HEAD: return data.head;
            case BODY: return data.body;
            case LEFT_ARM: return data.leftArm;
            case LEFT_FORE_ARM: return data.leftForeArm;
            case RIGHT_ARM: return data.rightArm;
            case RIGHT_FORE_ARM: return data.rightForeArm;
            case LEFT_LEG: return data.leftLeg;
            case LEFT_FORE_LEG: return data.leftForeLeg;
            case RIGHT_LEG: return data.rightLeg;
            case RIGHT_FORE_LEG: return data.rightForeLeg;
            default: return null;
        }
    }

    private static final class BoneTransform implements IBoneTransform
    {
        private final IModelPart part;
        private final float[] euler;

        private BoneTransform(IModelPart part)
        {
            this.part = part;
            this.euler = quaternionToEulerXYZ(part.getRotation().getSmooth());
        }

        @Override
        public float positionX()
        {
            return this.part.getPosition().getX() + this.part.getOffset().getX();
        }

        @Override
        public float positionY()
        {
            return this.part.getPosition().getY() + this.part.getOffset().getY();
        }

        @Override
        public float positionZ()
        {
            return this.part.getPosition().getZ() + this.part.getOffset().getZ();
        }

        @Override
        public float rotationX()
        {
            return this.euler[0];
        }

        @Override
        public float rotationY()
        {
            return this.euler[1];
        }

        @Override
        public float rotationZ()
        {
            return this.euler[2];
        }

        @Override
        public boolean visible()
        {
            return this.part.isShowing();
        }

        private static float[] quaternionToEulerXYZ(Quaternion q)
        {
            final float[] euler = new float[3];

            final float sinX = 2.0f * (q.w * q.x + q.y * q.z);
            final float cosX = 1.0f - 2.0f * (q.x * q.x + q.y * q.y);
            euler[0] = (float) Math.atan2(sinX, cosX);

            final float sinY = 2.0f * (q.w * q.y - q.z * q.x);
            if (Math.abs(sinY) >= 1.0f)
            {
                euler[1] = (float) Math.copySign(Math.PI / 2, sinY);
            }
            else
            {
                euler[1] = (float) Math.asin(sinY);
            }

            final float sinZ = 2.0f * (q.w * q.z + q.x * q.y);
            final float cosZ = 1.0f - 2.0f * (q.y * q.y + q.z * q.z);
            euler[2] = (float) Math.atan2(sinZ, cosZ);

            return euler;
        }
    }
}
