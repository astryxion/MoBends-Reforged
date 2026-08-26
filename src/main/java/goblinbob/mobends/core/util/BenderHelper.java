package goblinbob.mobends.core.util;

import goblinbob.mobends.core.bender.EntityBender;
import goblinbob.mobends.core.bender.EntityBenderRegistry;
import goblinbob.mobends.core.data.LivingEntityData;
import goblinbob.mobends.core.mutators.Mutator;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.entity.LivingEntity;

public class BenderHelper
{

    public static boolean isEntityAnimated(LivingEntity entity)
    {
        final EntityBender<?> bender = EntityBenderRegistry.instance.getForEntity(entity);
        return bender != null && bender.isAnimated();
    }

    public static <T extends LivingEntity> Mutator<?, ?, ?> getMutatorForRenderer(Class<T> entityClass, LivingRenderer<T, ?> renderer)
    {
        final EntityBender<?> bender = EntityBenderRegistry.instance.getForEntityClass(entityClass);
        return bender != null ? bender.getMutator(renderer) : null;
    }

    public static <D extends LivingEntityData<E>, E extends LivingEntity> D getData(E entity, LivingRenderer<? extends LivingEntity, ?> renderer)
    {
        final EntityBender<LivingEntity> entityBender = EntityBenderRegistry.instance.getForEntity(entity);

        if (entityBender == null)
            return null;

        final Mutator<D, E, ?> mutator = (Mutator<D, E, ?>) entityBender.getMutator(renderer);

        if (mutator == null)
            return null;

        return mutator.getData(entity);
    }

}
