package goblinbob.mobends.forge.client.event;

import net.minecraft.client.util.InputMappings;
import goblinbob.mobends.core.client.gui.GuiBendsMenu;
import goblinbob.mobends.standard.main.ModStatics;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

public class KeyboardEventHandler
{
    public static final String KEY_CATEGORY = "key.categories." + ModStatics.MODID;

    public static KeyBinding openMenuKey;

    public static void registerKeyMappings(FMLClientSetupEvent event)
    {
        openMenuKey = new KeyBinding(
                "key." + ModStatics.MODID + ".menu",
                InputMappings.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                KEY_CATEGORY
        );
        ClientRegistry.registerKeyBinding(openMenuKey);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event)
    {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null || mc.screen != null)
            return;

        if (openMenuKey != null && openMenuKey.consumeClick())
        {
            mc.setScreen(new GuiBendsMenu());
        }
    }
}
