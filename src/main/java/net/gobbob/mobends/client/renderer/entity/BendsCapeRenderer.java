package net.gobbob.mobends.client.renderer.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.gobbob.mobends.data.Data_Player;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

/**
 * Port of Mo' Bends 1.12.2 {@code BendsCapeRenderer}: cape as 16 hinged slabs with a wave.
 */
@SideOnly(Side.CLIENT)
public class BendsCapeRenderer {

   public static final int MODEL_WIDTH = 10;
   public static final int MODEL_LENGTH = 16;
   public static final int MODEL_DEPTH = 1;
   public static final int SLAB_AMOUNT = 16;

   public final Slab[] slabs;

   public BendsCapeRenderer() {
      this.slabs = new Slab[SLAB_AMOUNT];

      for(int i = 0; i < SLAB_AMOUNT; ++i) {
         this.slabs[i] = new Slab(i * MODEL_LENGTH / SLAB_AMOUNT);
         if (i > 0) {
            this.slabs[i - 1].setChildSlab(this.slabs[i]);
         }
      }

      this.slabs[0].rotationPointY = 0;
   }

   public void applyAnimation(Data_Player playerData) {
      double phase = (double)playerData.getCapeWavePhase();

      for(int i = 0; i < SLAB_AMOUNT; ++i) {
         float waveSpeed = 0.2F;
         float waveFrequency = 7.2F;
         float waveOffset = (float)i / (float)SLAB_AMOUNT;
         float magnitude = 80.0F / (float)SLAB_AMOUNT * (0.7F + (float)i / (float)SLAB_AMOUNT);
         float wave = (float)(Math.cos(phase * (double)waveSpeed + (double)(waveOffset * waveFrequency)) * (double)magnitude);
         // After the 180° Y flip, large positive angles fold slabs into the torso.
         // Keep the 1.12.2 wave feel but clamp how far it can dig inward.
         if (wave > magnitude * 0.35F) {
            wave = magnitude * 0.35F;
         }

         this.slabs[i].setRotateAngle(wave);
      }

      this.slabs[0].rotate(-10.0F);
   }

   public void render(float scale) {
      this.slabs[0].render(scale);
   }

   @SideOnly(Side.CLIENT)
   static class Slab {
      float rotateAngle;
      public float textureWidth = 64.0F;
      public float textureHeight = 32.0F;
      private boolean compiled;
      private int displayList;
      private Slab childSlab;
      public boolean showModel = true;
      public boolean isHidden;
      public int offsetX;
      public int offsetY;
      public int offsetZ;
      public int rotationPointX;
      public int rotationPointY;
      public int rotationPointZ;
      public int hingeOffset;
      private final TexturedQuad[] quadList;
      public final float posX1;
      public final float posY1;
      public final float posZ1;
      public final float posX2;
      public final float posY2;
      public final float posZ2;

      public Slab(int texV) {
         this.rotationPointX = 0;
         this.rotationPointY = MODEL_LENGTH / SLAB_AMOUNT;
         this.rotationPointZ = 0;
         this.offsetX = -MODEL_WIDTH / 2;
         this.offsetY = 0;
         this.offsetZ = 0;
         int slabLength = MODEL_LENGTH / SLAB_AMOUNT;
         this.posX1 = (float)this.offsetX;
         this.posY1 = (float)this.offsetY;
         this.posZ1 = (float)this.offsetZ;
         this.posX2 = (float)(this.offsetX + MODEL_WIDTH);
         this.posY2 = (float)(this.offsetY + slabLength);
         this.posZ2 = (float)(this.offsetZ + MODEL_DEPTH);
         int texU = 0;
         this.quadList = new TexturedQuad[6];
         PositionTextureVertex v7 = new PositionTextureVertex(this.posX1, this.posY1, this.posZ1, 0.0F, 0.0F);
         PositionTextureVertex v0 = new PositionTextureVertex(this.posX2, this.posY1, this.posZ1, 0.0F, 8.0F);
         PositionTextureVertex v1 = new PositionTextureVertex(this.posX2, this.posY2, this.posZ1, 8.0F, 8.0F);
         PositionTextureVertex v2 = new PositionTextureVertex(this.posX1, this.posY2, this.posZ1, 8.0F, 0.0F);
         PositionTextureVertex v3 = new PositionTextureVertex(this.posX1, this.posY1, this.posZ2, 0.0F, 0.0F);
         PositionTextureVertex v4 = new PositionTextureVertex(this.posX2, this.posY1, this.posZ2, 0.0F, 8.0F);
         PositionTextureVertex v5 = new PositionTextureVertex(this.posX2, this.posY2, this.posZ2, 8.0F, 8.0F);
         PositionTextureVertex v6 = new PositionTextureVertex(this.posX1, this.posY2, this.posZ2, 8.0F, 0.0F);
         this.quadList[0] = new TexturedQuad(new PositionTextureVertex[]{v4, v0, v1, v5}, texU + MODEL_DEPTH + MODEL_WIDTH, texV + MODEL_DEPTH, texU + MODEL_DEPTH + MODEL_WIDTH + MODEL_DEPTH, texV + MODEL_DEPTH + slabLength, this.textureWidth, this.textureHeight);
         this.quadList[1] = new TexturedQuad(new PositionTextureVertex[]{v7, v3, v6, v2}, texU, texV + MODEL_DEPTH, texU + MODEL_DEPTH, texV + MODEL_DEPTH + slabLength, this.textureWidth, this.textureHeight);
         this.quadList[2] = new TexturedQuad(new PositionTextureVertex[]{v4, v3, v7, v0}, texU + MODEL_DEPTH, texV, texU + MODEL_DEPTH + MODEL_WIDTH, texV + MODEL_DEPTH, this.textureWidth, this.textureHeight);
         this.quadList[3] = new TexturedQuad(new PositionTextureVertex[]{v1, v2, v6, v5}, texU + MODEL_DEPTH + MODEL_WIDTH, texV + MODEL_DEPTH, texU + MODEL_DEPTH + MODEL_WIDTH + MODEL_WIDTH, texV, this.textureWidth, this.textureHeight);
         this.quadList[4] = new TexturedQuad(new PositionTextureVertex[]{v0, v7, v2, v1}, texU + MODEL_DEPTH, texV + MODEL_DEPTH, texU + MODEL_DEPTH + MODEL_WIDTH, texV + MODEL_DEPTH + slabLength, this.textureWidth, this.textureHeight);
         this.quadList[5] = new TexturedQuad(new PositionTextureVertex[]{v3, v4, v5, v6}, texU + MODEL_DEPTH + MODEL_WIDTH + MODEL_DEPTH, texV + MODEL_DEPTH, texU + MODEL_DEPTH + MODEL_WIDTH + MODEL_DEPTH + MODEL_WIDTH, texV + MODEL_DEPTH + slabLength, this.textureWidth, this.textureHeight);
      }

      public void rotate(float f) {
         this.setRotateAngle(this.rotateAngle + f);
      }

      public Slab setChildSlab(Slab slab) {
         this.childSlab = slab;
         return this;
      }

      public void setRotateAngle(float rotateAngle) {
         this.rotateAngle = rotateAngle;
         this.hingeOffset = this.rotateAngle < 0.0F ? MODEL_DEPTH : 0;
      }

      public void render(float scale) {
         if (!this.isHidden && this.showModel) {
            if (!this.compiled) {
               this.compileDisplayList(scale);
            }

            GL11.glPushMatrix();
            GL11.glTranslatef((float)this.rotationPointX * scale, (float)this.rotationPointY * scale, (float)(this.rotationPointZ + this.hingeOffset) * scale);
            GL11.glRotatef(this.rotateAngle, 1.0F, 0.0F, 0.0F);
            GL11.glTranslatef(0.0F, 0.0F, (float)(-this.hingeOffset) * scale);
            GL11.glCallList(this.displayList);
            if (this.childSlab != null) {
               this.childSlab.render(scale);
            }

            GL11.glPopMatrix();
         }
      }

      private void compileDisplayList(float scale) {
         this.displayList = GLAllocation.generateDisplayLists(1);
         GL11.glNewList(this.displayList, GL11.GL_COMPILE);
         Tessellator tessellator = Tessellator.instance;

         for(int i = 0; i < this.quadList.length; ++i) {
            this.quadList[i].draw(tessellator, scale);
         }

         GL11.glEndList();
         this.compiled = true;
      }
   }
}
