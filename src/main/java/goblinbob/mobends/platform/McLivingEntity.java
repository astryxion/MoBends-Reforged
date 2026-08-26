package goblinbob.mobends.platform;

import goblinbob.mobends.api.entity.IEntity;
import goblinbob.mobends.api.entity.IEquipmentSlot;
import goblinbob.mobends.api.entity.IItemStack;
import goblinbob.mobends.api.entity.ILivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.entity.LivingEntity;

import javax.annotation.Nullable;

public class McLivingEntity extends McEntity implements ILivingEntity
{
    protected final LivingEntity livingEntity;

    public McLivingEntity(LivingEntity entity)
    {
        super(entity);
        this.livingEntity = entity;
    }

    @Override
    public float getHealth()
    {
        return livingEntity.getHealth();
    }

    @Override
    public float getMaxHealth()
    {
        return livingEntity.getMaxHealth();
    }

    @Override
    public float getLimbSwing()
    {
        return livingEntity.animationPosition;
    }

    @Override
    public float getLimbSwingAmount()
    {
        return livingEntity.animationSpeed;
    }

    @Override
    public float getAttackAnim()
    {
        return livingEntity.attackAnim;
    }

    @Override
    public int getHurtTime()
    {
        return livingEntity.hurtTime;
    }

    @Override
    public int getDeathTime()
    {
        return livingEntity.deathTime;
    }

    @Override
    public float getHeadYaw()
    {
        return livingEntity.yHeadRot;
    }

    @Override
    public float getPrevHeadYaw()
    {
        return livingEntity.yHeadRotO;
    }

    @Override
    public float getBodyYaw()
    {
        return livingEntity.yBodyRot;
    }

    @Override
    public float getPrevBodyYaw()
    {
        return livingEntity.yBodyRotO;
    }

    @Override
    public boolean isBaby()
    {
        return livingEntity.isBaby();
    }

    @Override
    public boolean isSleeping()
    {
        return livingEntity.isSleeping();
    }

    @Override
    public boolean isDead()
    {
        return livingEntity.isDeadOrDying();
    }

    @Override
    public boolean isUsingItem()
    {
        return livingEntity.isUsingItem();
    }

    @Override
    public int getTicksUsingItem()
    {
        return livingEntity.getTicksUsingItem();
    }

    @Override
    public int getUsedItemHand()
    {
        if (!livingEntity.isUsingItem())
        {
            return -1;
        }
        return livingEntity.getUsedItemHand() == Hand.MAIN_HAND ? 0 : 1;
    }

    @Override
    public boolean isFallFlying()
    {
        return livingEntity.isFallFlying();
    }

    @Override
    public boolean isSwimming()
    {
        return livingEntity.isSwimming();
    }

    @Override
    public boolean isVisuallySwimming()
    {
        return livingEntity.isVisuallySwimming();
    }

    @Override
    public boolean isCrouching()
    {
        return livingEntity.isCrouching();
    }

    @Override
    public IItemStack getItemBySlot(IEquipmentSlot slot)
    {
        EquipmentSlotType mcSlot = convertSlot(slot);
        return new McItemStack(livingEntity.getItemBySlot(mcSlot));
    }

    @Override
    @Nullable
    public IEntity getVehicle()
    {
        if (livingEntity.getVehicle() == null)
        {
            return null;
        }
        return new McEntity(livingEntity.getVehicle());
    }

    private EquipmentSlotType convertSlot(IEquipmentSlot slot)
    {
        switch (slot) {
case MAINHAND:
return EquipmentSlotType.MAINHAND;
case OFFHAND:
return EquipmentSlotType.OFFHAND;
case FEET:
return EquipmentSlotType.FEET;
case LEGS:
return EquipmentSlotType.LEGS;
case CHEST:
return EquipmentSlotType.CHEST;
case HEAD:
return EquipmentSlotType.HEAD;
        }
        throw new IllegalStateException("Unknown equipment slot: " + slot);
    }

    public LivingEntity getLivingEntity()
    {
        return livingEntity;
    }
}
