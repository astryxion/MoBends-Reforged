package goblinbob.mobends.core.client.gui.elements;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

public class GuiCustomButton extends Button
{

    private final Minecraft mc;

    public GuiCustomButton(int buttonId, int width, int height)
    {
        super(0, 0, width, height, StringTextComponent.EMPTY, b -> {});
        mc = Minecraft.getInstance();
    }

    public GuiCustomButton(int width, int height, String text)
    {
        super(0, 0, width, height, new StringTextComponent(text), b -> {});
        mc = Minecraft.getInstance();
    }

    public GuiCustomButton setPosition(int x, int y)
    {
        this.x = x;
        this.y = y;
        return this;
    }

    public void drawButton(int mouseX, int mouseY, float partialTicks)
    {
        this.render(new MatrixStack(), mouseX, mouseY, partialTicks);
    }

    public boolean mousePressed(int mouseX, int mouseY)
    {
        final boolean clicked = this.clicked((double) mouseX, (double) mouseY);

        if (clicked)
        {
            this.playDownSound(mc.getSoundManager());
        }

        return clicked;
    }

    public boolean mousePressed(Minecraft minecraft, int mouseX, int mouseY)
    {
        return mousePressed(mouseX, mouseY);
    }

    public GuiCustomButton setText(String text)
    {
        this.setMessage(new StringTextComponent(text));
        return this;
    }

}
