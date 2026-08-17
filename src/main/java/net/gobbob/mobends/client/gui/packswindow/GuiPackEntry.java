package net.gobbob.mobends.client.gui.packswindow;

import net.gobbob.mobends.client.gui.IGuiDraggable;
import net.gobbob.mobends.client.gui.elements.IGuiListElement;
import net.gobbob.mobends.pack.BendsPack;
import net.gobbob.mobends.util.Draw;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiPackEntry implements IGuiListElement, IGuiDraggable {
   private static final int HEIGHT = 31;
   private static final ResourceLocation DEFAULT_THUMBNAIL = new ResourceLocation("mobends", "textures/gui/default_pack_thumbnail.png");
   protected final FontRenderer fontRenderer;
   protected BendsPack pack;
   protected String name;
   protected String author;
   protected String description;
   private String displayName;
   private ResourceLocation thumbnailLocation;
   private int x;
   private int y;
   private int smoothX;
   private int smoothY;
   private int dragX;
   private int dragY;
   private int dragPivotX;
   private int dragPivotY;
   private int order;
   private GuiPackList parentList;
   private boolean firstInit = false;
   private boolean hover;
   private boolean selected;
   private boolean dragged;

   public GuiPackEntry(BendsPack pack) {
      this.fontRenderer = Minecraft.getMinecraft().fontRenderer;
      this.pack = pack;
      this.name = pack.filename != null ? pack.filename : "default";
      this.displayName = pack.displayName;
      this.author = pack.author;
      this.description = pack.description;
      this.thumbnailLocation = DEFAULT_THUMBNAIL;
      this.dragX = 0;
      this.dragY = 0;
      this.dragPivotX = 0;
      this.dragPivotY = 0;
      this.hover = false;
      this.selected = false;
   }

   public BendsPack getPack() {
      return this.pack;
   }

   public void initGui(int x, int y) {
      this.x = x;
      this.y = y;
      if (!this.firstInit) {
         this.firstInit = true;
         this.smoothX = x;
         this.smoothY = y;
      }
   }

   public void update(int mouseX, int mouseY) {
      this.hover = mouseX >= this.x && mouseX <= this.x + 102 && mouseY >= this.y && mouseY <= this.y + HEIGHT;
      this.smoothX = (int)((float)this.smoothX + (float)(this.x - this.smoothX) * 0.7F);
      this.smoothY = (int)((float)this.smoothY + (float)(this.y - this.smoothY) * 0.7F);
   }

   public boolean handleMouseClicked(int mouseX, int mouseY, int state) {
      this.update(mouseX, mouseY);
      this.dragPivotX = mouseX - this.x;
      this.dragPivotY = mouseY - this.y;
      return this.hover;
   }

   public void dragTo(int x, int y) {
      this.dragX = x;
      this.dragY = y;
   }

   public void setSelected(boolean selected) {
      this.selected = selected;
   }

   public void setDragged(boolean dragged) {
      this.dragged = dragged;
   }

   public void setOrder(int order) {
      this.order = order;
   }

   public void setParentList(GuiPackList parentList) {
      this.parentList = parentList;
   }

   public boolean isDragged() {
      return this.dragged;
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public int getDragX() {
      return this.dragX;
   }

   public int getDragY() {
      return this.dragY;
   }

   public int getDragPivotX() {
      return this.dragPivotX;
   }

   public int getDragPivotY() {
      return this.dragPivotY;
   }

   public int getOrder() {
      return this.order;
   }

   public GuiPackList getParentList() {
      return this.parentList;
   }

   public int getHeight() {
      return HEIGHT;
   }

   public void draw(float partialTicks) {
      int viewX = this.dragged ? this.dragX - this.dragPivotX : this.smoothX;
      int viewY = this.dragged ? this.dragY - this.dragPivotY : this.smoothY;
      Minecraft mc = Minecraft.getMinecraft();
      mc.renderEngine.bindTexture(GuiPacksWindow.BACKGROUND_TEXTURE);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      int selectedTextureY = 62;
      int hoverTextureY = 31;
      int neutralTextureY = 0;
      int textureY = this.selected ? selectedTextureY : (this.hover ? hoverTextureY : neutralTextureY);
      Draw.texturedModalRect(viewX - 1, viewY - (this.selected ? 1 : 0), 0, textureY, 102, HEIGHT);
      mc.renderEngine.bindTexture(this.thumbnailLocation);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      Draw.texturedRectangle(viewX + 2, viewY + 2, 25, 25, 0.0F, 0.0F, 0.78125F, 0.78125F);
      this.fontRenderer.drawStringWithShadow(this.fontRenderer.trimStringToWidth(this.displayName, 70), viewX + 32, viewY + 1, 16777215);
      Draw.rectangleHorizontalGradient((float)(viewX + 101 - 40), (float)(viewY + 1), 39.0F, 9.0F, 0x004e4e4e, 0xff4e4e4e);
   }
}
