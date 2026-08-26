package goblinbob.mobends.forge.player;

import goblinbob.mobends.api.player.IPlayerSkinProvider;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;

public class ForgePlayerSkinProvider implements IPlayerSkinProvider
{
    @Override
    public boolean isSlimModel(Object player)
    {
        if (player instanceof AbstractClientPlayerEntity) {
            AbstractClientPlayerEntity acp = (AbstractClientPlayerEntity) player;
            return "slim".equals(acp.getModelName());
        }
        return false;
    }

    @Override
    public Object getCapeTexture(Object player)
    {
        if (player instanceof AbstractClientPlayerEntity) {
            AbstractClientPlayerEntity acp = (AbstractClientPlayerEntity) player;
            return acp.getCloakTextureLocation();
        }
        return null;
    }

    @Override
    public Object getElytraTexture(Object player)
    {
        return null;
    }
}
