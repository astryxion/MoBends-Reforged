package goblinbob.mobends.core.client.gui.elements;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.text.StringTextComponent;

public class GuiCompactTextField extends TextFieldWidget
{

    String placeholderText;

    public GuiCompactTextField(FontRenderer fontrendererObj, int width, int height)
    {
        super(fontrendererObj, 0, 0, width, height, StringTextComponent.EMPTY);
    }

    public GuiCompactTextField(int componentId, FontRenderer fontrendererObj, int x, int y, int width,
                               int height)
    {
        super(fontrendererObj, x, y, width, height, StringTextComponent.EMPTY);
    }

    public GuiCompactTextField(FontRenderer fontrendererObj, int x, int y, int width, int height)
    {
        super(fontrendererObj, x, y, width, height, StringTextComponent.EMPTY);
    }

    @Override
    public void renderButton(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks)
    {
        super.renderButton(poseStack, mouseX, mouseY, partialTicks);
        if (!this.isFocused() && this.getValue().length() == 0 && this.placeholderText != null)
            Minecraft.getInstance().font.drawShadow(poseStack, this.placeholderText, this.x + 4, this.y + (this.height - 8) / 2, 0x707070);
    }

    public void drawTextBox()
    {
        this.render(new MatrixStack(), 0, 0, 0);
    }

    public GuiCompactTextField setPlaceholderText(String placeholderText)
    {
        this.placeholderText = placeholderText;
        return this;
    }

    public void setPosition(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public void updateCursorCounter()
    {
        this.tick();
    }

    public boolean textboxKeyTyped(char typedChar, int keyCode)
    {
        boolean handled = false;
        if (typedChar != 0)
        {
            handled = this.charTyped(typedChar, 0);
        }
        handled |= this.keyPressed(keyCode, 0, 0);
        return handled;
    }

}
