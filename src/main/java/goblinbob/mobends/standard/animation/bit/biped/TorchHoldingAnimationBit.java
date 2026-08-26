package goblinbob.mobends.standard.animation.bit.biped;

import goblinbob.mobends.core.animation.bit.AnimationBit;
import goblinbob.mobends.core.client.model.IModelPart;
import goblinbob.mobends.standard.data.BipedEntityData;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class TorchHoldingAnimationBit extends AnimationBit<BipedEntityData<?>>
{

	private static final String[] ACTIONS = new String[] { "torch_holding" };

	@Override
	public String[] getActions(BipedEntityData<?> data)
	{
		return ACTIONS;
	}

	private HandSide getTorchHand(LivingEntity living)
	{
		final HandSide mainHand = living.getMainArm();
		final HandSide offHand = mainHand == HandSide.LEFT ? HandSide.RIGHT : HandSide.LEFT;

		final Item mainItem = living.getItemInHand(Hand.MAIN_HAND).getItem();
		final Item offItem = living.getItemInHand(Hand.OFF_HAND).getItem();

		if (mainItem.equals(Items.TORCH))
			return mainHand;
		else if (offItem.equals(Items.TORCH))
			return offHand;
		else
			return null;
	}

	@Override
	public void perform(BipedEntityData<?> data)
	{
		final LivingEntity living = data.getEntity();
		final HandSide torchHand = getTorchHand(living);

		if (torchHand == null)
			return;

		final IModelPart mainArm = torchHand == HandSide.RIGHT ? data.rightArm : data.leftArm;
		final IModelPart mainForeArm = torchHand == HandSide.RIGHT ? data.rightForeArm : data.leftForeArm;

		mainArm.getRotation().orientX(-90.0F + data.headPitch.get() * 0.5F)
							 .rotateY(data.headYaw.get() * 0.7F);
		mainForeArm.getRotation().orientX(-5.0F);
	}

}
