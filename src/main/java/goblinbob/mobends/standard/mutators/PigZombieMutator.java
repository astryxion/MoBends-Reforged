package goblinbob.mobends.standard.mutators;

import goblinbob.mobends.core.data.IEntityDataFactory;
import goblinbob.mobends.standard.data.PigZombieData;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;

public class PigZombieMutator extends PiglinMutatorBase<PigZombieData, ZombifiedPiglinEntity>
{
    public PigZombieMutator(IEntityDataFactory<ZombifiedPiglinEntity> dataFactory)
    {
        super(dataFactory);
    }
}
