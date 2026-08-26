package goblinbob.mobends.standard.mutators;

import goblinbob.mobends.core.client.MoBendsRenderContext;
import goblinbob.mobends.core.data.IEntityDataFactory;
import goblinbob.mobends.standard.data.IllagerData;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.IllagerModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.AbstractIllagerEntity;

import java.util.IdentityHashMap;
import java.util.Map;

public class IllagerMutator<E extends AbstractIllagerEntity>
        extends BipedMutator<IllagerData<E>, E, IllagerModel<E>>
{
    private final Map<IllagerModel<?>, BipedModel<?>> views = new IdentityHashMap<>();

    public IllagerMutator(IEntityDataFactory<E> dataFactory)
    {
        super(dataFactory);
    }

    @Override
    public BipedModel<?> humanoidViewOf(EntityModel<?> model)
    {
        if (!(model instanceof IllagerModel<?>)) {
            return null;
        }
        IllagerModel<?> illagerModel = (IllagerModel<?>) model;

        return views.computeIfAbsent(illagerModel, IllagerMutator::buildView);
    }

    private static BipedModel<?> buildView(IllagerModel<?> model)
    {
        BipedModel<LivingEntity> view = new BipedModel<>(0.0F);
        view.head = model.getHead();
        view.hat = model.getHat();
        view.body = model.body;
        view.rightArm = model.rightArm;
        view.leftArm = model.leftArm;
        view.rightLeg = model.rightLeg;
        view.leftLeg = model.leftLeg;
        return view;
    }

    private boolean hatVisible = false;

    @Override
    protected void reconcileWithVanillaModel(BipedModel<?> original)
    {
        super.reconcileWithVanillaModel(original);

        this.hatVisible = original != null && original.hat.visible;
    }

    @Override
    protected void syncConcealmentFromVanillaModel()
    {
        final BipedModel<?> model = MoBendsRenderContext.getCurrentVanillaModel();
        if (model != null)
        {
            model.leftArm.visible = true;
            model.rightArm.visible = true;
            model.hat.visible = this.hatVisible;
        }

        super.syncConcealmentFromVanillaModel();

        if (model != null)
        {
            model.hat.visible = false;
        }
    }

    @Override
    public boolean shouldModelBeSkipped(EntityModel<?> model)
    {
        return !(model instanceof IllagerModel);
    }
}
