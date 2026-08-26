package goblinbob.mobends.standard.mutators;

import goblinbob.mobends.core.data.IEntityDataFactory;
import goblinbob.mobends.standard.data.PiglinData;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;

public class PiglinMutator<E extends AbstractPiglinEntity> extends PiglinMutatorBase<PiglinData<E>, E>
{
    public PiglinMutator(IEntityDataFactory<E> dataFactory)
    {
        super(dataFactory);
    }
}
