package goblinbob.mobends.core.client.gui.settingswindow;

import com.mojang.blaze3d.matrix.MatrixStack;
import goblinbob.mobends.core.Core;
import goblinbob.mobends.core.bender.EntityBender;
import goblinbob.mobends.core.bender.EntityBenderRegistry;
import goblinbob.mobends.core.client.event.DataUpdateHandler;
import goblinbob.mobends.core.client.gui.GuiBendsMenu;
import goblinbob.mobends.core.client.gui.elements.GuiCompactTextField;
import goblinbob.mobends.core.util.Draw;
import goblinbob.mobends.core.util.GuiHelper;
import goblinbob.mobends.standard.main.ModStatics;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.glfw.GLFW;

public class GuiSettingsWindow extends Screen
{

    public static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(ModStatics.MODID,
            "textures/gui/pack_window.png");
    public static final int EDITOR_WIDTH = 280;
    public static final int EDITOR_HEIGHT = 177;

    private int x, y;

    private GuiCompactTextField filterQueryInput;
    private final GuiBenderList bendsSettingsListUI = new GuiBenderList(0, 0, EDITOR_WIDTH - 10, EDITOR_HEIGHT - 10 - 20);

    private final EntityBenderRegistry.Filter filter = new EntityBenderRegistry.Filter();

    public GuiSettingsWindow()
    {
        super(new TranslationTextComponent("mobends.gui.settings"));

        fetchBenders();
    }

    @Override
    protected void init()
    {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);

        this.x = (this.width - EDITOR_WIDTH) / 2;
        this.y = (this.height - EDITOR_HEIGHT) / 2;

        this.addButton(new Button(10, height - 30, 60, 20, new TranslationTextComponent("mobends.gui.back"), button -> goBack()));
        filterQueryInput = new GuiCompactTextField(this.font, x + 6, y + 6, 150, 16);
        filterQueryInput.setFocus(true);
        filterQueryInput.setPlaceholderText(I18n.get("mobends.gui.search"));
        bendsSettingsListUI.initGui(this.x + 9, this.y + 9 + 20);
    }

    @Override
    public void render(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(poseStack);

        minecraft.getTextureManager().bind(BACKGROUND_TEXTURE);
        // Container
        Draw.borderBox(x + 4, y + 4, EDITOR_WIDTH, EDITOR_HEIGHT, 4, 36, 126);
        // Title background
        Draw.texturedModalRect(x, y - 13, 101, 0, 4, 16);
        Draw.texturedModalRect(x + 4, y - 13, EDITOR_WIDTH - 16, 16, 105, 0, 1, 16);
        Draw.texturedModalRect(x + EDITOR_WIDTH - 17, y - 13, 106, 0, 19, 16);

        bendsSettingsListUI.draw(DataUpdateHandler.partialTicks);

        font.drawShadow(poseStack, I18n.get("mobends.gui.settings"), this.x + 6, this.y - 9, 0xffffff);
        filterQueryInput.render(poseStack, mouseX, mouseY, partialTicks);

        super.render(poseStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void tick()
    {
        final int mouseX = (int) (this.minecraft.mouseHandler.xpos() * this.width / this.minecraft.getWindow().getScreenWidth());
        final int mouseY = (int) (this.minecraft.mouseHandler.ypos() * this.height / this.minecraft.getWindow().getScreenHeight());

        bendsSettingsListUI.update(mouseX, mouseY);
        filterQueryInput.tick();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
    {
        boolean handled = super.mouseClicked(mouseX, mouseY, mouseButton);

        bendsSettingsListUI.handleMouseClicked((int) mouseX, (int) mouseY, mouseButton);
        filterQueryInput.mouseClicked(mouseX, mouseY, mouseButton);
        return handled;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton)
    {
        boolean handled = super.mouseReleased(mouseX, mouseY, mouseButton);

        bendsSettingsListUI.handleMouseReleased((int) mouseX, (int) mouseY, mouseButton);
        return handled;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta)
    {
        if (bendsSettingsListUI.handleMouseScroll(delta))
        {
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean charTyped(char typedChar, int modifiers)
    {
        filterQueryInput.charTyped(typedChar, modifiers);
        if (!filterQueryInput.getValue().equals(filter.query))
        {
            filter.query = filterQueryInput.getValue();
            fetchBenders();
        }
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        filterQueryInput.keyPressed(keyCode, scanCode, modifiers);
        if (!filterQueryInput.getValue().equals(filter.query))
        {
            filter.query = filterQueryInput.getValue();
            fetchBenders();
        }

        if (keyCode == GLFW.GLFW_KEY_ESCAPE)
        {
            Core.saveConfiguration();
            GuiHelper.closeGui();
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void goBack()
    {
        Core.saveConfiguration();
        this.minecraft.setScreen(new GuiBendsMenu());
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc()
    {
        return false;
    }

    public void fetchBenders()
    {
        bendsSettingsListUI.clearElements();
        for (final EntityBender<?> bender : EntityBenderRegistry.instance.getRegistered(filter))
        {
            bendsSettingsListUI.addElement(new GuiBenderSettings(bender));
        }
    }

}
