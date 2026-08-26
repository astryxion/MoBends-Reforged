package goblinbob.mobends.core.util;

import net.minecraft.client.gui.screen.Screen;
import com.mojang.blaze3d.matrix.MatrixStack;

public final class ScreenHelper
{
    private ScreenHelper()
    {
    }

    public static void renderBackground(Screen screen, MatrixStack poseStack, int mouseX, int mouseY, float partialTicks)
    {
        screen.renderBackground(poseStack);
    }
}
