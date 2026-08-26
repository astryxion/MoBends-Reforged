package goblinbob.mobends.forge.network;

import org.apache.logging.log4j.LogManager;
import goblinbob.mobends.forge.MoBendsForge;
import goblinbob.mobends.forge.network.msg.MessageConfigRequest;
import goblinbob.mobends.forge.network.msg.MessageConfigResponse;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.Logger;

public class ForgeNetworkHandler {
    private static final Logger LOGGER = LogManager.getLogger(ForgeNetworkHandler.class);
    private static final String PROTOCOL_VERSION = "1";

    private static SimpleChannel CHANNEL;

    public static void register() {

        CHANNEL = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(MoBendsForge.MOD_ID, "main"),
                () -> PROTOCOL_VERSION,
                s -> true,
                s -> true
        );

        int id = 0;

        CHANNEL.registerMessage(id++,
                MessageConfigRequest.class,
                MessageConfigRequest::encode,
                MessageConfigRequest::decode,
                MessageConfigRequest::handle);

        CHANNEL.registerMessage(id++,
                MessageConfigResponse.class,
                MessageConfigResponse::encode,
                MessageConfigResponse::decode,
                MessageConfigResponse::handle);

    }

    public static SimpleChannel getChannel() {
        return CHANNEL;
    }
}
