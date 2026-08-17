package net.gobbob.mobends.client.model.entity;

import net.gobbob.mobends.client.model.ModelRendererBends;
import net.minecraft.client.model.ModelRenderer;

/**
 * 1.7.10 {@code zombie_villager.png} is 64x64: top 32 rows are a normal zombie skin,
 * villager skull/nose are at UV y=32. Vanilla only swaps the head to those UVs.
 * Parts must use textureHeight 64 or y=32 wraps back to the zombie face and the
 * body samples the villager half of the sheet.
 */
public class ModelBendsZombieVillager extends ModelBendsZombie {
   public ModelBendsZombieVillager() {
      this(0.0F, 0.0F, false);
   }

   public ModelBendsZombieVillager(float inflate, float yOffset, boolean armor) {
      super(inflate, 0.0F, 64, armor ? 32 : 64);
      this.textureWidth = 64;
      this.textureHeight = armor ? 32 : 64;
      this.forceTextureSize(this.textureWidth, this.textureHeight);
      this.applyVillagerHead(inflate, yOffset, armor);
   }

   private void forceTextureSize(int width, int height) {
      for (int i = 0; i < this.boxList.size(); ++i) {
         ((ModelRenderer)this.boxList.get(i)).setTextureSize(width, height);
      }
   }

   private void applyVillagerHead(float inflate, float yOffset, boolean armor) {
      if (this.bipedBody.childModels != null) {
         this.bipedBody.childModels.remove(this.bipedHead);
      }
      if (this.bipedHead.childModels != null) {
         this.bipedHead.childModels.remove(this.bipedHeadwear);
      }

      ModelRendererBends head = new ModelRendererBends(this, 0, armor ? 0 : 32);
      head.setTextureSize(this.textureWidth, this.textureHeight);
      head.setRotationPoint(0.0F, yOffset - 12.0F, 0.0F);
      if (armor) {
         head.addBox(-4.0F, -10.0F, -4.0F, 8, 6, 8, inflate);
      } else {
         head.setTextureOffset(0, 32).addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, inflate);
         head.setTextureOffset(24, 32).addBox(-1.0F, -3.0F, -6.0F, 2, 4, 2, inflate);
      }
      this.bipedHead = head;
      this.bipedBody.addChild(head);

      this.bipedHeadwear.isHidden = true;
      this.bipedHeadwear.showModel = false;
      head.addChild(this.bipedHeadwear);
   }
}
