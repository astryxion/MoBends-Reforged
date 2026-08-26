package goblinbob.mobends.core.client.gui.addonswindow;

import goblinbob.mobends.api.addon.Addons;
import goblinbob.mobends.api.addon.IAddon;
import goblinbob.mobends.standard.main.ModStatics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import com.mojang.blaze3d.matrix.MatrixStack;

public class GuiAddonsWindow extends AbstractGui
{
	public static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(ModStatics.MODID,
			"textures/gui/addons_window.png");
	
	private static final int WIDTH = 210;
	private static final int HEIGHT = 122;
	static final int SCROLLBAR_WIDTH = 5;
	
	private int x, y;
	private FontRenderer fontRenderer;
	
	public GuiAddonsWindow()
	{
		this.fontRenderer = Minecraft.getInstance().font;
	}
	
	public void initGui(int x, int y)
	{
		this.x = x - WIDTH/2;
		this.y = y - HEIGHT/2;
	}
	
	public void onOpened()
	{
	}
	
	public void display(int mouseX, int mouseY, float partialTicks)
	{
		Minecraft.getInstance().getTextureManager().bind(BACKGROUND_TEXTURE);
		MatrixStack poseStack = new MatrixStack();
		this.blit(poseStack, this.x, this.y, 0, 0, WIDTH, HEIGHT);

		this.drawCenteredString(poseStack, this.fontRenderer, I18n.get("mobends.gui.addons"),
				(int) (this.x + WIDTH/2), this.y + 4, 0xFFFFFF);
		
		int y = this.y + 50;
		for (IAddon addon : Addons.getRegistered()) {
			this.drawCenteredString(poseStack, this.fontRenderer, addon.getDisplayName(),
					(int) (this.x + WIDTH/2), y, 0xFFFFFF);
			y += 50;
		}
	}
	
	public void update(int mouseX, int mouseY)
	{
	}
	
	public boolean mouseClicked(int mouseX, int mouseY, int state)
	{
		return false;
	}
	
	public void mouseReleased(int mouseX, int mouseY, int event)
	{
		
	}
}
