package goblinbob.mobends.standard.previewer;

import goblinbob.mobends.standard.data.BipedEntityData;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;

public class PiglinPreviewer<D extends BipedEntityData<? extends AbstractPiglinEntity>> extends BipedPreviewer<D>
{
    @Override
    public void prePreview(D data, String animationToPreview)
    {
        final AbstractPiglinEntity piglin = data.getEntity();
        if (piglin != null)
        {
            piglin.setImmuneToZombification(true);
        }

        super.prePreview(data, animationToPreview);
    }
}
