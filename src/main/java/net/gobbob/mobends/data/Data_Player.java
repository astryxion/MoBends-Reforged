package net.gobbob.mobends.data;

import java.util.ArrayList;
import java.util.List;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.entity.ModelBendsPlayer;
import net.gobbob.mobends.client.renderer.SwordTrail;
import net.gobbob.mobends.util.SmoothVector3f;
import net.gobbob.mobends.util.Quaternion;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockVine;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class Data_Player extends EntityData {
   public static List<Data_Player> dataList = new ArrayList();
   public ModelRendererBends head;
   public ModelRendererBends headwear;
   public ModelRendererBends body;
   public ModelRendererBends rightArm;
   public ModelRendererBends leftArm;
   public ModelRendererBends rightLeg;
   public ModelRendererBends leftLeg;
   public ModelRendererBends ears;
   public ModelRendererBends cloak;
   public ModelRendererBends rightForeArm;
   public ModelRendererBends leftForeArm;
   public ModelRendererBends rightForeLeg;
   public ModelRendererBends leftForeLeg;
   public SmoothVector3f renderOffset = new SmoothVector3f();
   public SmoothVector3f renderRotation = new SmoothVector3f();
   public SmoothVector3f centerRotation = new SmoothVector3f();
   public Quaternion centerQuat = new Quaternion();
   public Quaternion centerQuatTarget = new Quaternion();
   public boolean centerQuatActive = false;
   /** Low-passed look yaw for flight body twist — stops F5 camera fighting raw mouse look. */
   public float flightBodyLookYaw = 0.0F;
   public SmoothVector3f renderItemRotation = new SmoothVector3f();
   public SwordTrail swordTrail = new SwordTrail();
   public boolean sprintJumpLeg = false;
   public boolean fistPunchArm = false;
   public int currentAttack = 0;
   public float climbingCycle = 0.0F;
   public boolean climbing = false;
   /** Sprint-jump foreleg relax progress (0-1), per-player for 1.12.2 SprintJumpAnimationBit. */
   public float sprintJumpRelax = 0.0F;
   public boolean jumpPoseInitialized = false;
   public float capeWavePhase = 0.0F;
   public float capeWaveSpeed = 1.0F;

   public Data_Player(int argEntityID) {
      super(argEntityID);
   }

   public void syncModelInfo(ModelBendsPlayer argModel) {
      if (this.head == null) {
         this.head = new ModelRendererBends(argModel);
      }

      this.head.sync((ModelRendererBends)argModel.bipedHead);
      if (this.headwear == null) {
         this.headwear = new ModelRendererBends(argModel);
      }

      this.headwear.sync((ModelRendererBends)argModel.bipedHeadwear);
      if (this.body == null) {
         this.body = new ModelRendererBends(argModel);
      }

      this.body.sync((ModelRendererBends)argModel.bipedBody);
      if (this.rightArm == null) {
         this.rightArm = new ModelRendererBends(argModel);
      }

      this.rightArm.sync((ModelRendererBends)argModel.bipedRightArm);
      if (this.leftArm == null) {
         this.leftArm = new ModelRendererBends(argModel);
      }

      this.leftArm.sync((ModelRendererBends)argModel.bipedLeftArm);
      if (this.rightLeg == null) {
         this.rightLeg = new ModelRendererBends(argModel);
      }

      this.rightLeg.sync((ModelRendererBends)argModel.bipedRightLeg);
      if (this.leftLeg == null) {
         this.leftLeg = new ModelRendererBends(argModel);
      }

      this.leftLeg.sync((ModelRendererBends)argModel.bipedLeftLeg);
      if (this.rightForeArm == null) {
         this.rightForeArm = new ModelRendererBends(argModel);
      }

      this.rightForeArm.sync((ModelRendererBends)argModel.bipedRightForeArm);
      if (this.leftForeArm == null) {
         this.leftForeArm = new ModelRendererBends(argModel);
      }

      this.leftForeArm.sync((ModelRendererBends)argModel.bipedLeftForeArm);
      if (this.rightForeLeg == null) {
         this.rightForeLeg = new ModelRendererBends(argModel);
      }

      this.rightForeLeg.sync((ModelRendererBends)argModel.bipedRightForeLeg);
      if (this.leftForeLeg == null) {
         this.leftForeLeg = new ModelRendererBends(argModel);
      }

      this.leftForeLeg.sync((ModelRendererBends)argModel.bipedLeftForeLeg);
      this.renderOffset.set(argModel.renderOffset);
      this.renderRotation.set(argModel.renderRotation);
      this.centerRotation.set(argModel.centerRotation);
      this.centerQuat.set(argModel.centerQuat);
      this.centerQuatTarget.set(argModel.centerQuatTarget);
      this.centerQuatActive = argModel.centerQuatActive;
      this.renderItemRotation.set(argModel.renderItemRotation);
      this.swordTrail = argModel.swordTrail;
   }

   public static void add(Data_Player argData) {
      dataList.add(argData);
   }

   public static Data_Player get(int argEntityID) {
      for(int i = 0; i < dataList.size(); ++i) {
         if (((Data_Player)dataList.get(i)).entityID == argEntityID) {
            return (Data_Player)dataList.get(i);
         }
      }

      Data_Player newData = new Data_Player(argEntityID);
      if (Minecraft.getMinecraft().theWorld.getEntityByID(argEntityID) != null) {
         dataList.add(newData);
      }

      return newData;
   }

   public void update(float argPartialTicks) {
      super.update(argPartialTicks);
      if (this.ticksAfterPunch > 20.0F) {
         this.currentAttack = 0;
      }

      if (this.calcClimbing()) {
         this.climbingCycle += this.motion.y * 2.6F * this.ticksPerFrame;
         this.climbing = true;
      } else {
         this.climbing = false;
      }

      this.capeWavePhase += this.capeWaveSpeed * this.ticksPerFrame;
      if (this.capeWavePhase > 380.0F) {
         this.capeWavePhase -= 380.0F;
      }

   }

   public void setCapeWaveSpeed(float value) {
      this.capeWaveSpeed = value;
   }

   public float getCapeWavePhase() {
      return this.capeWavePhase;
   }

   public boolean isClimbing() {
      return this.climbing;
   }

   public boolean calcClimbing() {
      EntityLivingBase entity = this.getEntity();
      if (entity != null && entity.worldObj != null) {
         int x = (int)Math.floor(entity.posX);
         int y = (int)Math.floor(entity.posY);
         int z = (int)Math.floor(entity.posZ);
         World world = entity.worldObj;
         return entity.isOnLadder() && !this.isOnGround() && (isBlockClimbable(world, x, y, z) || isBlockClimbable(world, x, y - 1, z) || isBlockClimbable(world, x, y - 2, z));
      } else {
         return false;
      }
   }

   public float getClimbingRotation() {
      return this.getLadderFacingYaw() + 180.0F;
   }

   public float getLadderFacingYaw() {
      EntityLivingBase entity = this.getEntity();
      if (entity != null && entity.worldObj != null) {
         int x = (int)Math.floor(entity.posX);
         int y = (int)Math.floor(entity.posY);
         int z = (int)Math.floor(entity.posZ);
         World world = entity.worldObj;
         float facing = getClimbableFacingYaw(world, x, y, z);
         if (Float.isNaN(facing)) {
            facing = getClimbableFacingYaw(world, x, y - 1, z);
         }

         if (Float.isNaN(facing)) {
            facing = getClimbableFacingYaw(world, x, y - 2, z);
         }

         return Float.isNaN(facing) ? 0.0F : facing;
      } else {
         return 0.0F;
      }
   }

   public float getLedgeHeight() {
      EntityLivingBase entity = this.getEntity();
      if (entity != null && entity.worldObj != null) {
         float clientY = (float)entity.posY;
         int x = (int)Math.floor(entity.posX);
         int y = (int)Math.floor(entity.posY);
         int z = (int)Math.floor(entity.posZ);
         World world = entity.worldObj;
         if (!isBlockClimbable(world, x, y + 2, z)) {
            if (!isBlockClimbable(world, x, y + 1, z)) {
               return !isBlockClimbable(world, x, y, z) ? clientY - (float)((int)clientY) + 2.0F : clientY - (float)((int)clientY) + 1.0F;
            } else {
               return clientY - (float)((int)clientY);
            }
         } else {
            return -2.0F;
         }
      } else {
         return -2.0F;
      }
   }

   private static boolean isBlockClimbable(World world, int x, int y, int z) {
      Block block = world.getBlock(x, y, z);
      return block instanceof BlockLadder || block instanceof BlockVine;
   }

   private static float getClimbableFacingYaw(World world, int x, int y, int z) {
      Block block = world.getBlock(x, y, z);
      int meta = world.getBlockMetadata(x, y, z);
      if (block instanceof BlockLadder) {
         switch(meta) {
         case 2:
            return 180.0F;
         case 3:
            return 0.0F;
         case 4:
            return 90.0F;
         case 5:
            return 270.0F;
         default:
            return 0.0F;
         }
      } else if (block instanceof BlockVine) {
         if ((meta & 8) != 0) {
            return 90.0F;
         } else if ((meta & 2) != 0) {
            return 270.0F;
         } else if ((meta & 4) != 0) {
            return 0.0F;
         } else {
            return (meta & 1) != 0 ? 180.0F : Float.NaN;
         }
      } else {
         return Float.NaN;
      }
   }

   public void onLiftoff() {
      super.onLiftoff();
      this.sprintJumpLeg = !this.sprintJumpLeg;
      this.sprintJumpRelax = 0.0F;
      this.jumpPoseInitialized = false;
   }

   public void onPunch() {
      if (this.getEntity().getHeldItem() != null) {
         if (this.ticksAfterPunch > 6.0F) {
            if (this.currentAttack == 0) {
               this.currentAttack = 1;
               this.ticksAfterPunch = 0.0F;
            } else if (this.ticksAfterPunch < 15.0F) {
               if (this.currentAttack == 1) {
                  this.currentAttack = 2;
               } else if (this.currentAttack == 2) {
                  this.currentAttack = this.getEntity().isRiding() ? 1 : 3;
               } else if (this.currentAttack == 3) {
                  this.currentAttack = 1;
               }

               this.ticksAfterPunch = 0.0F;
            }
         }
      } else {
         this.fistPunchArm = !this.fistPunchArm;
         this.ticksAfterPunch = 0.0F;
      }

   }
}
