package goblinbob.mobends.standard.data;

import goblinbob.mobends.standard.animation.controller.PigZombieController;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;

public class PigZombieData extends BipedEntityData<ZombifiedPiglinEntity>
{

	private final PigZombieController controller = new PigZombieController();

	public PigZombieData(ZombifiedPiglinEntity entity)
	{
		super(entity);
	}

	@Override
	public PigZombieController getController()
	{
		return controller;
	}

	@Override
	public void onTicksRestart()
	{
	}

}
