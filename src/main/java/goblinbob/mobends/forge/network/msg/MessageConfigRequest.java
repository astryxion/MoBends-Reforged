package goblinbob.mobends.forge.network.msg;

import goblinbob.mobends.forge.network.ForgeNetworkHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

public class MessageConfigRequest
{

    public MessageConfigRequest()
    {
    }

    public static void encode(MessageConfigRequest message, PacketBuffer buf)
    {
    }

    public static MessageConfigRequest decode(PacketBuffer buf)
    {
        return new MessageConfigRequest();
    }

    public static void handle(MessageConfigRequest message, Supplier<NetworkEvent.Context> contextSupplier)
    {
        final NetworkEvent.Context context = contextSupplier.get();

        context.enqueueWork(() -> {
            final ServerPlayerEntity sender = context.getSender();
            if (sender == null)
            {
                return;
            }

            ForgeNetworkHandler.getChannel().send(
                    PacketDistributor.PLAYER.with(() -> sender),
                    new MessageConfigResponse());
        });

        context.setPacketHandled(true);
    }

}
