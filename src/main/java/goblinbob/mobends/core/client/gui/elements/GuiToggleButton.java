package goblinbob.mobends.core.client.gui.elements;

import goblinbob.mobends.core.util.Draw;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiToggleButton
{

	protected static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation("textures/gui/widgets.png");
	
    private static final int FLIPPER_WIDTH = 30;
    private static final int HEIGHT = 20;

    protected int x;
    protected int y;
    protected boolean hovered;
    protected boolean enabled;
    protected boolean toggleState;
    protected final String title;
    protected final int labelWidth;
    
    public GuiToggleButton(String title, int minLabelWidth)
    {
        this.x = 0;
        this.y = 0;
        this.enabled = true;
        this.hovered = false;
        this.toggleState = false;
        
        this.title = title;
        
        int titleWidth = Minecraft.getInstance().font.width(title) + 20;
        
        this.labelWidth = titleWidth > minLabelWidth ? titleWidth : minLabelWidth;
    }

    public void initGui(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public void update(int mouseX, int mouseY)
    {
        this.hovered = mouseX >= x && mouseX <= x + this.labelWidth + FLIPPER_WIDTH &&
                mouseY >= y && mouseY <= y + HEIGHT;
    }

    public void draw()
    {
        Minecraft mc = Minecraft.getInstance();
        FontRenderer fontRenderer = mc.font;
        mc.getTextureManager().bind(BUTTON_TEXTURES);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        int k = this.hovered ? 1 : 0;
        
        GL11.glPushMatrix();
        	Draw.texturedModalRect(this.x, this.y, 0, 66 + k * 20, (this.labelWidth + FLIPPER_WIDTH) / 2, HEIGHT);
        	Draw.texturedModalRect(this.x + this.labelWidth / 2, this.y, 200 - (this.labelWidth + FLIPPER_WIDTH) / 2, 66 + k * 20, (this.labelWidth + FLIPPER_WIDTH) / 2, HEIGHT);
        GL11.glPopMatrix();
        
        GL11.glPushMatrix();
        	if(this.toggleState)
        		RenderSystem.color3f(0.3F, 1.0F, 0.5F);
        	else
        		RenderSystem.color3f(1.0F, 0.3F, 0.3F);
        	Draw.texturedModalRect(this.x + this.labelWidth, this.y, 0, 66 + k * 20, FLIPPER_WIDTH / 2, HEIGHT);
        	Draw.texturedModalRect(this.x + this.labelWidth + FLIPPER_WIDTH / 2, this.y, 200 - FLIPPER_WIDTH / 2, 66 + k * 20, FLIPPER_WIDTH / 2, HEIGHT);
        GL11.glPopMatrix();
        
        int l = 14737632;

        if (!this.enabled)
        {
            l = 10526880;
        }
        else if (this.hovered)
        {
            l = 16777120;
        }
        
        String stateText = this.toggleState ? "ON" : "OFF";
        int textWidth = fontRenderer.width(stateText);
        fontRenderer.draw(new com.mojang.blaze3d.matrix.MatrixStack(), stateText, this.x + this.labelWidth - textWidth/2 + FLIPPER_WIDTH/2, this.y + (HEIGHT - 8) / 2, l);
        
        fontRenderer.draw(new com.mojang.blaze3d.matrix.MatrixStack(), this.title, this.x + 10, this.y + (HEIGHT - 8) / 2, l);
    
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }
    
    public void setToggleState(boolean state)
    {
    	this.toggleState = state;
    }

    public boolean mouseClicked(int mouseX, int mouseY, int button)
    {
    	if (hovered && button == 0)
    	{
	    	this.toggleState = !this.toggleState;
	        return true;
    	}
    	
		return false;
    }
    
    public boolean getToggleState()
    {
    	return this.toggleState;
    }

}
