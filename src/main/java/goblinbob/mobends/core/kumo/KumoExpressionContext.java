package goblinbob.mobends.core.kumo;

import goblinbob.mobends.core.expression.ExpressionContext;

import goblinbob.mobends.core.client.event.DataUpdateHandler;
import goblinbob.mobends.core.data.EntityData;
import goblinbob.mobends.core.data.LivingEntityData;
import goblinbob.mobends.core.kumo.state.condition.ITriggerConditionContext;
import net.minecraft.entity.LivingEntity;

public class KumoExpressionContext implements ExpressionContext {
    private final ITriggerConditionContext kumoContext;
    private final EntityData<?> entityData;

    public KumoExpressionContext(ITriggerConditionContext kumoContext) {
        this.kumoContext = kumoContext;
        this.entityData = kumoContext.getEntityData();
    }

    public KumoExpressionContext(EntityData<?> entityData) {
        this.kumoContext = null;
        this.entityData = entityData;
    }

    @Override
    public double getVariable(String name) {
        switch (name) {
case "ticks":
return DataUpdateHandler.getTicks();
case "PI":
return Math.PI;
case "E":
return Math.E;
case "random":
return Math.random();
case "motionX":
return entityData.getMotionX();
case "motionY":
return entityData.getMotionY();
case "motionZ":
return entityData.getMotionZ();
case "interpMotionX":
return entityData.getInterpolatedMotionX();
case "interpMotionY":
return entityData.getInterpolatedMotionY();
case "interpMotionZ":
return entityData.getInterpolatedMotionZ();
case "motionMagnitude":
return entityData.getMotionMagnitude();
case "xzMotionMagnitude":
return entityData.getXZMotionMagnitude();
case "interpMotionMagnitude":
return entityData.getInterpolatedMotionMagnitude();
case "interpXZMotionMagnitude":
return entityData.getInterpolatedXZMotionMagnitude();
case "forwardMomentum":
return entityData.getForwardMomentum();
case "sidewaysMomentum":
return entityData.getSidewaysMomentum();
case "movementAngle":
return entityData.getMovementAngle();
case "lookAngle":
return entityData.getLookAngle();
case "onGround":
return entityData.isOnGround() ? 1.0 : 0.0;
case "isStill":
return entityData.isStillHorizontally() ? 1.0 : 0.0;
case "isStrafing":
return entityData.isStrafing() ? 1.0 : 0.0;
case "isUnderwater":
return entityData.isUnderwater() ? 1.0 : 0.0;
case "health":
return getLivingValue(e -> (double) e.getHealth(), 0.0);
case "maxHealth":
return getLivingValue(e -> (double) e.getMaxHealth(), 0.0);
case "healthPercent":
return getLivingValue(e -> (double) (e.getHealth() / e.getMaxHealth()), 0.0);
case "ticksInAir":
return getLivingDataValue(d -> (double) d.getTicksInAir(), 0.0);
case "ticksAfterTouchdown":
return getLivingDataValue(d -> (double) d.getTicksAfterTouchdown(), 0.0);
case "ticksAfterPunch":
case "ticksAfterAttack":
return getLivingDataValue(d -> (double) d.getTicksAfterAttack(), 0.0);
case "ticksFalling":
return getLivingDataValue(d -> (double) d.getTicksFalling(), 0.0);
case "limbSwing":
return getLivingDataValue(d -> (double) d.limbSwing.get(), 0.0);
case "limbSwingAmount":
return getLivingDataValue(d -> (double) d.limbSwingAmount.get(), 0.0);
case "headYaw":
return getLivingDataValue(d -> (double) d.headYaw.get(), 0.0);
case "headPitch":
return getLivingDataValue(d -> (double) d.headPitch.get(), 0.0);
case "swingProgress":
return getLivingDataValue(d -> (double) d.swingProgress.get(), 0.0);
case "isClimbing":
return getLivingDataValue(d -> d.isClimbing() ? 1.0 : 0.0, 0.0);
case "climbingCycle":
return getLivingDataValue(d -> (double) d.getClimbingCycle(), 0.0);
case "climbingRotation":
return getLivingDataValue(d -> (double) d.getClimbingRotation(), 0.0);
case "ledgeHeight":
return getLivingDataValue(d -> (double) d.getLedgeHeight(), 0.0);
case "isDrawingBow":
return getLivingDataValue(d -> d.isDrawingBow() ? 1.0 : 0.0, 0.0);
case "nodeProgress":
return getNodeProgress();
case "nodeAnimationFinished":
return isNodeAnimationFinished() ? 1.0 : 0.0;
case "posX":
return entityData.getPositionX();
case "posY":
return entityData.getPositionY();
case "posZ":
return entityData.getPositionZ();
default:
return 0.0;
}
    }

    @Override
    public boolean hasVariable(String name) {
        switch (name) {
case "ticks":
case "PI":
case "E":
case "random":
case "motionX":
case "motionY":
case "motionZ":
case "interpMotionX":
case "interpMotionY":
case "interpMotionZ":
case "motionMagnitude":
case "xzMotionMagnitude":
case "interpMotionMagnitude":
case "interpXZMotionMagnitude":
case "forwardMomentum":
case "sidewaysMomentum":
case "movementAngle":
case "lookAngle":
case "onGround":
case "isStill":
case "isStrafing":
case "isUnderwater":
case "health":
case "maxHealth":
case "healthPercent":
case "ticksInAir":
case "ticksAfterTouchdown":
case "ticksAfterPunch":
case "ticksAfterAttack":
case "ticksFalling":
case "limbSwing":
case "limbSwingAmount":
case "headYaw":
case "headPitch":
case "swingProgress":
case "isClimbing":
case "climbingCycle":
case "climbingRotation":
case "ledgeHeight":
case "isDrawingBow":
case "nodeProgress":
case "nodeAnimationFinished":
case "posX":
case "posY":
case "posZ":
return true;
default:
return false;
}
    }

    private double getLivingValue(java.util.function.Function<LivingEntity, Double> getter, double defaultValue) {
        if (entityData.getEntity() instanceof LivingEntity) {
            LivingEntity living = (LivingEntity) entityData.getEntity();
            return getter.apply(living);
        }
        return defaultValue;
    }

    private double getLivingDataValue(java.util.function.Function<LivingEntityData<?>, Double> getter, double defaultValue) {
        if (entityData instanceof LivingEntityData<?>) {
            LivingEntityData<?> livingData = (LivingEntityData<?>) entityData;
            return getter.apply(livingData);
        }
        return defaultValue;
    }

    private double getNodeProgress() {
        if (kumoContext != null && kumoContext.getCurrentNode() != null) {
            return kumoContext.getCurrentNode().getProgress();
        }
        return 0.0;
    }

    private boolean isNodeAnimationFinished() {
        if (kumoContext != null && kumoContext.getCurrentNode() != null) {
            return kumoContext.getCurrentNode().isAnimationFinished();
        }
        return false;
    }
}
