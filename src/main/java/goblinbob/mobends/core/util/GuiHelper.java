package goblinbob.mobends.core.util;

import org.apache.logging.log4j.LogManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.SoundEvents;
import org.apache.logging.log4j.Logger;

import java.net.URI;

public class GuiHelper
{
    private static final Logger LOGGER = LogManager.getLogger(GuiHelper.class);

    public static void closeGui()
    {
        Minecraft.getInstance().setScreen(null);
    }

    public static void playButtonSound(SoundHandler soundManager)
    {
        soundManager.play(SimpleSound.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    public static boolean openUrlInBrowser(String url)
    {
        try
        {
            Class<?> oclass = Class.forName("java.awt.Desktop");
            Object object = oclass.getMethod("getDesktop").invoke((Object)null);
            oclass.getMethod("browse", URI.class).invoke(object, new URI(url));
            return true;
        }
        catch (Throwable throwable)
        {
            LOGGER.warn(String.format("Couldn't open link %s", url));
            return false;
        }
    }

}
