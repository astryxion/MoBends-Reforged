package goblinbob.mobends.standard.animation.bit.biped;

import goblinbob.mobends.compat.ArtifactsCompat;
import goblinbob.mobends.core.animation.bit.AnimationBit;
import goblinbob.mobends.core.client.model.IModelPart;
import goblinbob.mobends.standard.data.BipedEntityData;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.entity.LivingEntity;

public class UmbrellaHoldingAnimationBit extends AnimationBit<BipedEntityData<?>>
{

	private static final String[] ACTIONS = new String[] { "umbrella_holding" };

	private static final float RAISE_PITCH = -180.0F;
	private static final float ELBOW_BEND = 20.0F;

	@Override
	public String[] getActions(BipedEntityData<?> data)
	{
		return ACTIONS;
	}

	@Override
	public void perform(BipedEntityData<?> data)
	{
		final LivingEntity living = data.getEntity();
		final boolean rightHanded = living.getMainArm() == HandSide.RIGHT;
		final boolean mainHand = ArtifactsCompat.isHoldingUmbrellaUpright(living, Hand.MAIN_HAND);
		final boolean offHand = ArtifactsCompat.isHoldingUmbrellaUpright(living, Hand.OFF_HAND);

		if (mainHand && rightHanded || offHand && !rightHanded)
		{
			raiseArm(data.rightArm, data.rightForeArm);
		}

		if (mainHand && !rightHanded || offHand && rightHanded)
		{
			raiseArm(data.leftArm, data.leftForeArm);
		}
	}

	private void raiseArm(IModelPart arm, IModelPart foreArm)
	{
		arm.getRotation().orientX(RAISE_PITCH + ELBOW_BEND);
		foreArm.getRotation().orientX(-ELBOW_BEND);
	}

}
