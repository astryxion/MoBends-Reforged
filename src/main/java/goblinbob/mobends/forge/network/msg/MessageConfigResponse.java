package goblinbob.mobends.forge.network.msg;

import org.apache.logging.log4j.LogManager;
import goblinbob.mobends.core.network.SharedNetworkConfiguration;
import goblinbob.mobends.core.network.SharedProperty;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class MessageConfigResponse
{
    private static final Logger LOGGER = LogManager.getLogger(MessageConfigResponse.class);

    private CompoundNBT configData;

    public MessageConfigResponse()
    {
        this.configData = new CompoundNBT();
        SharedNetworkConfiguration.INSTANCE.getSharedConfig().writeToNBT(this.configData);
    }

    private MessageConfigResponse(CompoundNBT configData)
    {
        this.configData = configData;
    }

    public static void encode(MessageConfigResponse message, PacketBuffer buf)
    {
        buf.writeNbt(message.configData);
    }

    public static MessageConfigResponse decode(PacketBuffer buf)
    {
        return new MessageConfigResponse(buf.readNbt());
    }

    public static void handle(MessageConfigResponse message, Supplier<NetworkEvent.Context> contextSupplier)
    {
        final NetworkEvent.Context context = contextSupplier.get();

        context.enqueueWork(() -> {
            if (message.configData == null)
            {
                LOGGER.error("An error occurred while receiving server configuration.");
                return;
            }

            SharedNetworkConfiguration.INSTANCE.getSharedConfig().readFromNBT(message.configData);
        });

        context.setPacketHandled(true);
    }

}
