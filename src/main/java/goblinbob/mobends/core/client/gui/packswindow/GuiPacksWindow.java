package goblinbob.mobends.core.client.gui.packswindow;

import com.mojang.blaze3d.matrix.MatrixStack;
import goblinbob.mobends.core.client.gui.GuiBendsMenu;
import goblinbob.mobends.core.pack.InvalidPackFormatException;
import goblinbob.mobends.core.pack.PackManager;
import goblinbob.mobends.core.util.Draw;
import goblinbob.mobends.core.util.ErrorReporter;
import goblinbob.mobends.core.util.GuiHelper;
import goblinbob.mobends.core.util.Timer;
import goblinbob.mobends.standard.main.ModStatics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.glfw.GLFW;

public class GuiPacksWindow extends Screen
{

    public static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(ModStatics.MODID,
            "textures/gui/pack_window.png");
    public static final int EDITOR_WIDTH = 280;
    public static final int EDITOR_HEIGHT = 177;

    private int x;
    private int y;

    private final GuiTabNavigation tabNavigation;
    private final GuiPackTab localPacksTab;
    private final GuiPackTab publicPacksTab;

    private GuiLocalPacks localPacks;

    private Timer timer;

    public GuiPacksWindow()
    {
        super(new TranslationTextComponent("mobends.gui.packs"));

        this.localPacks = new GuiLocalPacks();
        this.tabNavigation = new GuiTabNavigation();
        this.localPacksTab = this.tabNavigation.addTab("mobends.gui.localpacks", 0);
        this.publicPacksTab = this.tabNavigation.addTab("mobends.gui.publicpacks", 1);
        this.tabNavigation.selectTab(0);

        this.timer = new Timer();

        // Initializing local packs from disk.
        try
        {
            PackManager.INSTANCE.initLocalPacks();
        }
        catch (InvalidPackFormatException e)
        {
            // Some of the packs were in an invalid format.
            e.printStackTrace();
            ErrorReporter.showErrorToPlayer(e);
        }
    }

    @Override
    public void removed()
    {
        this.localPacks.dispose();
    }

    @Override
    protected void init()
    {
        this.x = (this.width - EDITOR_WIDTH) / 2;
        this.y = (this.height - EDITOR_HEIGHT) / 2;

        this.tabNavigation.initGui(this.x + 5, this.y);

        this.addButton(new Button(10, height - 30, 60, 20, new TranslationTextComponent("mobends.gui.back"), button -> goBack()));
        this.localPacks.initGui(this.x, this.y);
    }

    @Override
    public void tick()
    {
        int mouseX = (int) (this.minecraft.mouseHandler.xpos() * this.width / this.minecraft.getWindow().getScreenWidth());
        int mouseY = (int) (this.minecraft.mouseHandler.ypos() * this.height / this.minecraft.getWindow().getScreenHeight());

        this.localPacks.update(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        boolean handled = super.mouseClicked(mouseX, mouseY, button);

        this.tabNavigation.mouseClicked((int) mouseX, (int) mouseY, button);
        this.localPacks.mouseClicked((int) mouseX, (int) mouseY, button);
        return handled;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int state)
    {
        boolean handled = super.mouseReleased(mouseX, mouseY, state);

        this.localPacks.mouseReleased((int) mouseX, (int) mouseY, state);
        return handled;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta)
    {
        if (this.localPacks.handleMouseScroll(delta))
        {
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public void render(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(poseStack);

        this.timer.tick();

        Minecraft.getInstance().getTextureManager().bind(BACKGROUND_TEXTURE);
        // Container
        Draw.borderBox(x + 4, y + 4, EDITOR_WIDTH, EDITOR_HEIGHT, 4, 36, 126);
        // Title background
        Draw.texturedModalRect(x, y - 13, 101, 0, 4, 16);
        Draw.texturedModalRect(x + 4, y - 13, EDITOR_WIDTH - 16, 16, 105, 0, 1, 16);
        Draw.texturedModalRect(x + EDITOR_WIDTH - 17, y - 13, 106, 0, 19, 16);

        this.tabNavigation.draw(mouseX, mouseY);
        if (this.tabNavigation.getSelectedTab() == this.localPacksTab)
        {
            this.localPacks.draw(partialTicks);
        }
        else if (this.tabNavigation.getSelectedTab() == this.publicPacksTab)
        {
            font.drawShadow(poseStack, "Coming soon...", x + EDITOR_WIDTH / 2 - font.width("Coming soon...") / 2, y + EDITOR_HEIGHT / 2 - 10,
                    0xffffff);
        }

        super.render(poseStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE)
        {
            GuiHelper.closeGui();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void goBack()
    {
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

}
