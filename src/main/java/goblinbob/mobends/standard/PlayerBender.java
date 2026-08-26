package goblinbob.mobends.standard;

import goblinbob.mobends.core.bender.EntityBender;
import goblinbob.mobends.core.bender.IPreviewer;
import goblinbob.mobends.core.bender.PreviewHelper;
import goblinbob.mobends.core.data.IEntityDataFactory;
import goblinbob.mobends.core.data.LivingEntityData;
import goblinbob.mobends.core.mutators.IMutatorFactory;
import goblinbob.mobends.standard.client.renderer.entity.mutated.PlayerRenderer;
import goblinbob.mobends.standard.data.PlayerData;
import goblinbob.mobends.standard.main.ModStatics;
import goblinbob.mobends.standard.mutators.PlayerMutator;
import goblinbob.mobends.standard.previewer.PlayerPreviewer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import goblinbob.mobends.standard.previewer.PreviewPlayer;

public class PlayerBender extends EntityBender<AbstractClientPlayerEntity>
{

    private PlayerPreviewer previewer;
    private String[] alterableParts = {
        "head", "body", "leftArm", "rightArm", "leftForeArm", "rightForeArm", "leftLeg", "rightLeg",
        "leftForeLeg", "rightForeLeg", "totalRotation", "leftItemRotation", "rightItemRotation"
    };

    public PlayerBender()
    {
        super(ModStatics.MODID, "player", "mobends.player", AbstractClientPlayerEntity.class, new PlayerRenderer());
        this.previewer = new PlayerPreviewer();
    }

    @Override
    public AbstractClientPlayerEntity createPreviewEntity()
    {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return null;

        try
        {
            PreviewPlayer preview = new PreviewPlayer(
                (ClientWorld) mc.level,
                mc.player.getGameProfile()
            );
            preview.copySkinCustomisation(mc.player);
            preview.moveTo(0, 0, 0, 0, 0);
            PreviewHelper.registerPreviewEntity(preview);
            return preview;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String[] getAlterableParts()
    {
        return alterableParts;
    }

    @Override
    public String[] getSupportedAnimations()
    {
        return new String[] {
            "walk", "sprint", "jump", "fall", "sneak", "swim", "attack", "ride", "climb"
        };
    }

    @Override
    public IEntityDataFactory<AbstractClientPlayerEntity> getDataFactory()
    {
        return PlayerData::new;
    }

    @Override
    public IMutatorFactory<AbstractClientPlayerEntity> getMutatorFactory()
    {
        return PlayerMutator::new;
    }

    @Override
    public IPreviewer<?> getPreviewer()
    {
        return previewer;
    }

    @Override
    public LivingEntityData<?> getDataForPreview()
    {
        return PlayerPreviewer.getPreviewData();
    }

}
