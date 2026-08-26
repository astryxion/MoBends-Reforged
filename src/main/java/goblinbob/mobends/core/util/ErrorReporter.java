package goblinbob.mobends.core.util;


import net.minecraft.util.text.StringTextComponent;
import goblinbob.mobends.core.pack.InvalidPackFormatException;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.Util;

public class ErrorReporter
{

    public static IFormattableTextComponent createErrorHeader()
    {
        return new StringTextComponent("[Mo' Bends (Reforged)] ").withStyle(TextFormatting.YELLOW);
    }

    public static void showErrorToPlayer(ITextComponent textComponent)
    {
        if (Minecraft.getInstance().player == null)
        {
            return;
        }

        IFormattableTextComponent base = new StringTextComponent("").withStyle(TextFormatting.WHITE);
        base.append(createErrorHeader());
        base.append(textComponent);

        Minecraft.getInstance().player.sendMessage(base, Util.NIL_UUID);
    }

    public static void showErrorToPlayer(String error)
    {
        showErrorToPlayer(new StringTextComponent(error));
    }

    public static void showErrorToPlayer(InvalidPackFormatException ex)
    {
        IFormattableTextComponent textComponent = new StringTextComponent("A pack has been disabled due to it's wrong format: ");

        IFormattableTextComponent packName = new StringTextComponent(ex.getPackName()).withStyle(TextFormatting.BOLD);
        textComponent.append(packName);

        textComponent.append(new StringTextComponent(". Check the logs for more details..."));

        showErrorToPlayer(textComponent);
    }

}
