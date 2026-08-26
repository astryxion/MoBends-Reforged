package goblinbob.mobends.core.client;

import goblinbob.mobends.core.client.model.ModelPartTransform;
import goblinbob.mobends.core.data.EntityDatabase;
import goblinbob.mobends.core.data.LivingEntityData;
import goblinbob.mobends.core.util.BenderHelper;
import goblinbob.mobends.lib.math.Quaternion;
import goblinbob.mobends.standard.data.BipedEntityData;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

import javax.annotation.Nullable;

public final class AnimatedRiderAnchor
{
    private static final float MODEL_TO_BLOCKS = 1.0F / 16.0F;

    private static final float BODY_REST_Y = 12.0F;
    private static final float HEAD_REST_LOCAL_Y = -12.0F;

    private static final float HEAD_ANCHOR_RATIO = 0.6F;
    private static final double NEGLIGIBLE = 1.0E-4;

    private AnimatedRiderAnchor()
    {
    }

    @Nullable
    public static Vector3d getRenderOffset(Entity rider, float partialTicks)
    {
        if (!(rider.getVehicle() instanceof LivingEntity)) return null;
        LivingEntity host = (LivingEntity) rider.getVehicle();
        if (!BenderHelper.isEntityAnimated(host)) return null;

        LivingEntityData<?> data = EntityDatabase.instance.get(host);
        if (!(data instanceof BipedEntityData<?>)) return null;
        BipedEntityData<?> biped = (BipedEntityData<?>) data;

        ModelPartTransform body = biped.body;
        ModelPartTransform head = biped.head;
        if (body == null) return null;

        Vector3f delta = new Vector3f(
                body.position.x + body.offset.x,
                body.position.y + body.offset.y - BODY_REST_Y,
                body.position.z + body.offset.z);

        if (head != null && anchorsToHead(rider, host))
        {
            Vector3f headLocal = new Vector3f(
                    head.position.x + head.offset.x,
                    head.position.y + head.offset.y,
                    head.position.z + head.offset.z);

            Quaternion bodyRotation = body.rotation.getSmooth();
            headLocal.transform(new net.minecraft.util.math.vector.Quaternion(bodyRotation.x, bodyRotation.y, bodyRotation.z, bodyRotation.w));

            delta.add(headLocal.x, headLocal.y - HEAD_REST_LOCAL_Y, headLocal.z);
        }

        return toWorldOffset(delta, host, partialTicks);
    }

    private static boolean anchorsToHead(Entity rider, LivingEntity host)
    {
        float hostHeight = host.getBbHeight();
        if (hostHeight <= 0.0F) return true;

        return (rider.getY() - host.getY()) > hostHeight * HEAD_ANCHOR_RATIO;
    }

    @Nullable
    private static Vector3d toWorldOffset(Vector3f modelDelta, LivingEntity host, float partialTicks)
    {
        double localX = -modelDelta.x * MODEL_TO_BLOCKS;
        double localY = -modelDelta.y * MODEL_TO_BLOCKS;
        double localZ = modelDelta.z * MODEL_TO_BLOCKS;

        if (Math.abs(localX) < NEGLIGIBLE && Math.abs(localY) < NEGLIGIBLE && Math.abs(localZ) < NEGLIGIBLE)
        {
            return null;
        }

        float bodyYaw = MathHelper.rotLerp(partialTicks, host.yBodyRotO, host.yBodyRot);
        float angle = (180.0F - bodyYaw) * (float) (Math.PI / 180.0);
        float sin = MathHelper.sin(angle);
        float cos = MathHelper.cos(angle);

        return new Vector3d(
                localX * cos + localZ * sin,
                localY,
                -localX * sin + localZ * cos);
    }
}
