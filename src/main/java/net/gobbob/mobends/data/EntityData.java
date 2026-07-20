package net.gobbob.mobends.data;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.util.vector.Vector3f;

public class EntityData {
   public int entityID;
   public String entityType;
   public ModelBase model;
   public Vector3f position = new Vector3f();
   public Vector3f motion_prev = new Vector3f();
   public Vector3f motion = new Vector3f();
   public float ticks = 0.0F;
   public float ticksPerFrame = 0.0F;
   public float lastTicks = 0.0F;
   public float lastTicksPerFrame = 0.0F;
   public boolean updatedThisFrame = false;
   public float ticksAfterLiftoff = 0.0F;
   public float ticksAfterTouchdown = 0.0F;
   public float ticksAfterPunch = 0.0F;
   public float ticksFalling = 0.0F;
   public boolean alreadyPunched = false;
   public boolean onGround;

   public EntityData(int argEntityID) {
      this.entityID = argEntityID;
      if (Minecraft.getMinecraft().theWorld.getEntityByID(argEntityID) != null) {
         this.entityType = Minecraft.getMinecraft().theWorld.getEntityByID(argEntityID).getCommandSenderName();
      } else {
         this.entityType = "NULL";
      }

      this.model = null;
   }

   public boolean canBeUpdated() {
      return !this.updatedThisFrame;
   }

   public boolean calcOnGround() {
      Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(this.entityID);
      if (entity == null) {
         return false;
      } else {
         AxisAlignedBB axisalignedbb = entity.boundingBox.copy();
         double var1 = (double)(this.position.y + this.motion.y);
         List list = entity.worldObj.getCollidingBoundingBoxes(entity, entity.boundingBox.addCoord((double)0.0F, (double)-0.001F, (double)0.0F));
         int i = 0;
         return i < list.size();
      }
   }

   public boolean isOnGround() {
      return this.onGround;
   }

   public void update(float argPartialTicks) {
      if (this.getEntity() != null) {
         this.ticksPerFrame = (float)Minecraft.getMinecraft().thePlayer.ticksExisted + argPartialTicks - this.ticks;
         this.ticks = (float)Minecraft.getMinecraft().thePlayer.ticksExisted + argPartialTicks;
         this.updatedThisFrame = false;
         if (this.calcOnGround() && !this.onGround) {
            this.onTouchdown();
            this.onGround = true;
         }

         if (!this.calcOnGround() && this.onGround || (this.motion_prev.y <= 0.0F && this.motion.y - this.motion_prev.y > 0.4F && this.ticksAfterLiftoff > 2.0F)) {
            this.onLiftoff();
            this.onGround = false;
         }

         if (this.getEntity().swingProgress > 0.0F) {
            if (!this.alreadyPunched) {
               this.onPunch();
               this.alreadyPunched = true;
            }
         } else {
            this.alreadyPunched = false;
         }

         if (!this.isOnGround()) {
            this.ticksAfterLiftoff += this.ticksPerFrame;
            if (this.motion.y < 0.0F) {
               this.ticksFalling += this.ticksPerFrame;
            } else {
               this.ticksFalling = 0.0F;
            }
         }

         if (this.isOnGround()) {
            this.ticksAfterTouchdown += this.ticksPerFrame;
         }

         this.ticksAfterPunch += this.ticksPerFrame;
      }
   }

   public EntityLivingBase getEntity() {
      return Minecraft.getMinecraft().theWorld.getEntityByID(this.entityID) instanceof EntityLivingBase ? (EntityLivingBase)Minecraft.getMinecraft().theWorld.getEntityByID(this.entityID) : null;
   }

   public void onTouchdown() {
      this.ticksAfterTouchdown = 0.0F;
      this.ticksFalling = 0.0F;
   }

   public void onLiftoff() {
      this.ticksAfterLiftoff = 0.0F;
      this.ticksFalling = 0.0F;
   }

   public void onPunch() {
      this.ticksAfterPunch = 0.0F;
   }
}
