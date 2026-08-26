package goblinbob.mobends.standard.animation.bit.player;

import goblinbob.mobends.core.animation.bit.AnimationBit;
import goblinbob.mobends.core.animation.layer.HardAnimationLayer;
import goblinbob.mobends.standard.animation.bit.biped.*;
import goblinbob.mobends.standard.data.BipedEntityData;
import goblinbob.mobends.standard.data.PlayerData;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class AttackAnimationBit extends AnimationBit<PlayerData> {

    protected HardAnimationLayer<BipedEntityData<?>> layerBase;

    protected AttackSlashUpAnimationBit bitAttackSlashUp;
    protected AttackSlashDownAnimationBit bitAttackSlashDown;
    protected AttackSlashInwardAnimationBit bitAttackSlashInward;
    protected AttackSlashOutwardAnimationBit bitAttackSlashOutward;
    protected AttackWhirlSlashAnimationBit bitAttackWhirlSlash;
    protected FistGuardAnimationBit bitFistGuard;

    public AttackAnimationBit() {
        this.layerBase = new HardAnimationLayer<>();
        this.bitAttackSlashUp = new AttackSlashUpAnimationBit();
        this.bitAttackSlashDown = new AttackSlashDownAnimationBit();
        this.bitAttackSlashInward = new AttackSlashInwardAnimationBit();
        this.bitAttackSlashOutward = new AttackSlashOutwardAnimationBit();
        this.bitAttackWhirlSlash = new AttackWhirlSlashAnimationBit();
        this.bitFistGuard = new FistGuardAnimationBit();
    }

    @Override
    public String[] getActions(PlayerData entityData) {
        if (this.layerBase.isPlaying()) {
            return this.layerBase.getPerformedBit().getActions(entityData);
        }

        return null;
    }

    public boolean shouldPerformAttack(AbstractClientPlayerEntity player) {
        final ItemStack heldItemStack = player.getItemInHand(Hand.MAIN_HAND);
        return heldItemStack.getItem() != Items.AIR;
    }

    @Override
    public void perform(PlayerData playerData) {

        this.layerBase.perform(playerData);
    }
}
