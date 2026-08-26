package goblinbob.mobends.core.client.gui;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;

public class GuiHelper
{

    public static int[] getDeScaledCoords(int x, int y)
    {
        MainWindow window = Minecraft.getInstance().getWindow();
        int i1 = window.getGuiScaledWidth();
        int j1 = window.getGuiScaledHeight();
        int k1 = x * window.getWidth() / i1;
        int l1 = (j1 - y) * window.getHeight() / j1 + 1;
        return new int[]{k1, l1};
    }

    public static int[] getDeScaledVector(int x, int y)
    {
        MainWindow window = Minecraft.getInstance().getWindow();
        int i1 = window.getGuiScaledWidth();
        int j1 = window.getGuiScaledHeight();
        int k1 = x * window.getWidth() / i1;
        int l1 = y * window.getHeight() / j1;
        return new int[]{k1, l1};
    }

}
