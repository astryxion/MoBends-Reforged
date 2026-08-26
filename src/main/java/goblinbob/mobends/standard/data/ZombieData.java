package goblinbob.mobends.standard.data;

import goblinbob.mobends.standard.animation.controller.ZombieController;
import net.minecraft.entity.monster.ZombieEntity;

public class ZombieData extends ZombieDataBase<ZombieEntity>
{

    private final ZombieController controller = new ZombieController();

    public ZombieData(ZombieEntity entity)
    {
        super(entity);
    }

    @Override
    public ZombieController getController()
    {
        return this.controller;
    }

    @Override
    public void onTicksRestart()
    {
    }

}
