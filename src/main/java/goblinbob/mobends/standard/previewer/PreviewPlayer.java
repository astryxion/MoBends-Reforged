package goblinbob.mobends.standard.previewer;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;

public class PreviewPlayer extends RemoteClientPlayerEntity
{
    public PreviewPlayer(ClientWorld level, GameProfile profile)
    {
        super(level, profile);
    }

    public void copySkinCustomisation(PlayerEntity source)
    {
        if (source == null)
        {
            return;
        }

        this.getEntityData().set(DATA_PLAYER_MODE_CUSTOMISATION,
                source.getEntityData().get(DATA_PLAYER_MODE_CUSTOMISATION));
    }
}
