package goblinbob.mobends.core.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import goblinbob.mobends.core.WebAPI;
import goblinbob.mobends.core.client.gui.elements.GuiSectionButton;
import goblinbob.mobends.core.client.gui.packswindow.GuiPacksWindow;
import goblinbob.mobends.core.client.gui.popup.GuiEditorNotFound;
import goblinbob.mobends.core.client.gui.popup.GuiPopUp;
import goblinbob.mobends.core.client.gui.settingswindow.GuiSettingsWindow;
import goblinbob.mobends.core.network.NetworkConfiguration;
import goblinbob.mobends.core.util.Draw;
import goblinbob.mobends.core.util.GuiHelper;
import goblinbob.mobends.standard.main.ModStatics;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.glfw.GLFW;

public class GuiBendsMenu extends Screen
{
	
	private static final ResourceLocation MENU_TITLE_TEXTURE = new ResourceLocation(ModStatics.MODID,
			"textures/gui/title.png");
	public static final ResourceLocation ICONS_TEXTURE = new ResourceLocation(ModStatics.MODID,
			"textures/gui/icons.png");

	private GuiSectionButton settingsButton;
	private GuiSectionButton packsButton;
	private GuiSectionButton customizeButton;
	//private GuiSectionButton addonsButton;
	private GuiPopUp popUp;

	public GuiBendsMenu()
	{
		super(new TranslationTextComponent("mobends.gui.title"));

		this.settingsButton = new GuiSectionButton(I18n.get("mobends.gui.section.settings"), 0xFFDA3A00)
				.setLeftIcon(0, 43, 19, 19).setRightIcon(19, 43, 19, 19);
		this.packsButton = new GuiSectionButton(I18n.get("mobends.gui.section.packs"), 0xFF4577DE)
				.setLeftIcon(38, 43, 23, 20).setRightIcon(38, 43, 23, 20);
		this.customizeButton = new GuiSectionButton(I18n.get("mobends.gui.section.customize"), 0xFF26DAA3)
				.setLeftIcon(80, 43, 19, 14).setRightIcon(80, 43, 19, 14);
//		this.addonsButton = new GuiSectionButton(I18n.get("mobends.gui.section.addons"), 0xFFFFE565)
//				.setLeftIcon(61, 43, 19, 18).setRightIcon(61, 43, 19, 18);

		this.popUp = null;
	}

	@Override
	protected void init()
	{
		this.minecraft.keyboardHandler.setSendRepeatsToGui(true);

		if (this.popUp != null)
			this.popUp.initGui(this.width / 2, this.height / 2);

		int startY = height / 2 - 32;
		int distance = 49;

		if (NetworkConfiguration.instance.areBendsPacksAllowed())
		{
			this.settingsButton.initGui((this.width - 318) / 2, startY);
			this.packsButton.initGui((this.width - 318) / 2, startY + distance);
			this.customizeButton.initGui((this.width - 318) / 2, startY + distance * 2);
		}
		else
		{
			this.settingsButton.initGui((this.width - 318) / 2, startY);
			this.customizeButton.initGui((this.width - 318) / 2, startY + distance);
		}
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers)
	{
		if (popUp != null)
		{
			return true;
		}

		if (keyCode == GLFW.GLFW_KEY_ESCAPE)
		{
			GuiHelper.closeGui();
			return true;
		}

		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public void removed()
	{
		this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
	}

	@Override
	public void tick()
	{
		int mouseX = (int) (this.minecraft.mouseHandler.xpos() * this.width / this.minecraft.getWindow().getScreenWidth());
		int mouseY = (int) (this.minecraft.mouseHandler.ypos() * this.height / this.minecraft.getWindow().getScreenHeight());

		if (this.popUp != null)
		{
			this.popUp.update(mouseX, mouseY);
			return;
		}

		this.settingsButton.update(mouseX, mouseY);
		this.packsButton.update(mouseX, mouseY);
		this.customizeButton.update(mouseX, mouseY);
	}

	@Override
	public boolean mouseClicked(double x, double y, int state)
	{
		if (popUp != null)
		{
			popUp.mouseClicked((int) x, (int) y, state);
			return true;
		}

		if (settingsButton.mouseClicked((int) x, (int) y, state))
		{
			minecraft.setScreen(new GuiSettingsWindow());
			return true;
		}
		else if (packsButton.mouseClicked((int) x, (int) y, state))
		{
			minecraft.setScreen(new GuiPacksWindow());
			return true;
		}
		else if (customizeButton.mouseClicked((int) x, (int) y, state))
		{
			IAnimationEditor editor = AnimationEditorRegistry.INSTANCE.getPrimaryEditor();

			if (editor == null)
			{
				openPopUp(new GuiEditorNotFound(this::closePopUp, () -> {
					GuiEditorNotFound editorNotFoundPopup = (GuiEditorNotFound) popUp;
					String downloadUrl = WebAPI.INSTANCE.getOfficialAnimationEditorUrl();

					if (downloadUrl == null || !GuiHelper.openUrlInBrowser(downloadUrl))
					{
						editorNotFoundPopup.setErrorOccurred(true);
					}
					else
					{
						closePopUp();
					}
				}));
			}
			else
			{
				// Opens the animation editor.
				editor.openEditorGui();
			}
			return true;
		}

		return super.mouseClicked(x, y, state);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int state)
	{
		this.settingsButton.mouseReleased((int) mouseX, (int) mouseY, state);
		this.packsButton.mouseReleased((int) mouseX, (int) mouseY, state);
		this.customizeButton.mouseReleased((int) mouseX, (int) mouseY, state);
		return super.mouseReleased(mouseX, mouseY, state);
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void render(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(poseStack);

		RenderSystem.disableLighting();
		RenderSystem.enableBlend();

		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(MENU_TITLE_TEXTURE);
		int titleWidth = 167 * 2;
		int titleHeight = 37 * 2;

		Draw.texturedRectangle((width - titleWidth) / 2, (height - titleHeight) / 2 - 70, titleWidth, titleHeight, 0, 0, 1, 1);

		this.settingsButton.display();
		if (NetworkConfiguration.instance.areBendsPacksAllowed())
		{
			this.packsButton.display();
		}
		this.customizeButton.display();

		super.render(poseStack, mouseX, mouseY, partialTicks);

		if (this.popUp != null)
		{
			RenderSystem.disableDepthTest();
			this.renderBackground(poseStack);
			this.popUp.display(mouseX, mouseY, partialTicks);
			RenderSystem.enableDepthTest();
		}
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

	private void closePopUp()
	{
		this.popUp = null;
		this.init(this.minecraft, this.width, this.height);
	}

	private void openPopUp(GuiPopUp popUp)
	{
		this.popUp = popUp;
		this.init(this.minecraft, this.width, this.height);
	}

}
