package goblinbob.mobends.standard.data;

import goblinbob.mobends.standard.animation.controller.ZombieVillagerController;
import net.minecraft.entity.monster.ZombieVillagerEntity;

public class ZombieVillagerData extends ZombieDataBase<ZombieVillagerEntity>
{

	private final ZombieVillagerController controller = new ZombieVillagerController();

	public ZombieVillagerData(ZombieVillagerEntity entity)
	{
		super(entity);
	}

	@Override
	public ZombieVillagerController getController()
	{
		return controller;
	}

	@Override
	public void onTicksRestart()
	{
	}

}
