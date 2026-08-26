package goblinbob.mobends.standard.mutators;

import goblinbob.mobends.core.data.IEntityDataFactory;
import goblinbob.mobends.standard.data.ZombieData;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.monster.ZombieEntity;

public class ZombieMutator extends ZombieMutatorBase<ZombieData, ZombieEntity, BipedModel<ZombieEntity>>
{

    public ZombieMutator(IEntityDataFactory<ZombieEntity> dataFactory)
    {
        super(dataFactory);
    }

    @Override
    public void storeVanillaModel(BipedModel<ZombieEntity> model)
    {
        super.storeVanillaModel(model);
    }

    @Override
    public boolean shouldModelBeSkipped(EntityModel<?> model)
    {
        return !(model instanceof BipedModel);
    }
}
