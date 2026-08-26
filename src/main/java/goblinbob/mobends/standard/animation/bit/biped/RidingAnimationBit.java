package goblinbob.mobends.standard.animation.bit.biped;

import goblinbob.mobends.core.animation.bit.AnimationBit;
import goblinbob.mobends.core.client.event.DataUpdateHandler;
import goblinbob.mobends.standard.data.BipedEntityData;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

public class RidingAnimationBit<T extends BipedEntityData<?>> extends AnimationBit<T>
{
	private static final String[] ACTIONS = new String[] { "riding" };

	private static final float PI = (float) Math.PI;

	@Override
	public String[] getActions(T entityData)
	{
		return ACTIONS;
	}

	@Override
	public void perform(T data)
	{
		final LivingEntity living = data.getEntity();
		final float squareUp = data.getRidingBodyYaw();

		data.localOffset.slideToZero(0.3F);
		data.renderRotation.orientZero();
		data.centerRotation.setSmoothness(.3F).orientZero();
		data.renderLeftItemRotation.orientZero();
		data.renderRightItemRotation.orientZero();

		data.head.rotation.orientX(data.headPitch.get())
		  				  .rotateY(MathHelper.clamp(MathHelper.wrapDegrees(data.headYaw.get() + squareUp), -85.0F, 85.0F));
		data.body.rotation.orientY(0).setSmoothness(0.5F);

		data.leftLeg.rotation.orientX(-90.0F).rotateZ(-10.0F).rotateY(-25.0F);
		data.rightLeg.rotation.orientX(-90.0F).rotateZ(10.0F).rotateY(25.0F);
		data.leftForeLeg.rotation.orientX(60.0F);
		data.rightForeLeg.rotation.orientX(60.0F);

		data.leftArm.rotation.orientX(0.0F).rotateZ(-10F);
		data.leftForeArm.rotation.orientX(-10.0F);
		data.rightArm.rotation.orientX(0.0F).rotateZ(10F);
		data.rightForeArm.rotation.orientX(-10.0F);

		Entity ridden = living.getVehicle();
		if (ridden instanceof LivingEntity) {
            LivingEntity riddenLiving = (LivingEntity) ridden;
			float turnRate = MathHelper.wrapDegrees(riddenLiving.yBodyRot - riddenLiving.yBodyRotO);

			data.body.rotation.orientZ(MathHelper.clamp(-turnRate * 2.0F, -20.0F, 20.0F));

			float legSwing = MathHelper.clamp(turnRate * 3.0F, -30.0F, 30.0F);
			data.leftLeg.rotation.rotateX(-legSwing);
			data.rightLeg.rotation.rotateX(legSwing);
		}

		if (!data.isStillHorizontally())
		{
			data.body.rotation.orientX(25.0F);
			data.leftArm.rotation.orientX(-45.0F).rotateZ(10F);
			data.leftForeArm.rotation.orientX(-10.0F);
			data.rightArm.rotation.orientX(-45.0F).rotateZ(-10F);
			data.rightForeArm.rotation.orientX(-10.0F);

			float motionMagnitude = (float) (Math.sqrt(living.getDeltaMovement().x*living.getDeltaMovement().x + living.getDeltaMovement().z*living.getDeltaMovement().z)) * 100;
			if (motionMagnitude > 1)
			{
				float ticks = DataUpdateHandler.getTicks() * 0.5F;
				float bodyRotation = 45.0F + MathHelper.cos(ticks) * 10F;
				data.body.rotation.orientX(bodyRotation);
				data.head.rotation.rotateX(-bodyRotation);
				data.leftArm.rotation.rotateX(-bodyRotation);
				data.rightArm.rotation.rotateX(-bodyRotation);
				data.globalOffset.slideY(MathHelper.sin(ticks) * 0.3F);
			}
			else
			{
				data.head.rotation.rotateX(-25.0F);
			}
		}

		if (squareUp != 0.0F)
		{
			data.body.rotation.rotateY(-squareUp);
		}
	}
}
