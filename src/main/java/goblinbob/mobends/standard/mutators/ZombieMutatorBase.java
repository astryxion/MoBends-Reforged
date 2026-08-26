package goblinbob.mobends.standard.mutators;

import goblinbob.mobends.core.data.IEntityDataFactory;
import goblinbob.mobends.standard.data.ZombieDataBase;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.entity.monster.ZombieEntity;

public abstract class ZombieMutatorBase<D extends ZombieDataBase<E>,
                                        E extends ZombieEntity,
                                        M extends BipedModel<E>>
                                       extends BipedMutator<D, E, M>
{

    protected boolean halfTexture = false;

    public ZombieMutatorBase(IEntityDataFactory<E> dataCreationFunction)
    {
        super(dataCreationFunction);
    }

    @Override
    public void fetchFields(LivingRenderer<E, M> renderer)
    {
        super.fetchFields(renderer);

        this.halfTexture = false;
    }
}
