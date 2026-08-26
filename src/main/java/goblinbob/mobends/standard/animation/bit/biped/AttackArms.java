package goblinbob.mobends.standard.animation.bit.biped;

import goblinbob.mobends.standard.data.BipedEntityData;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.SwordItem;

public final class AttackArms
{
    private AttackArms()
    {
    }

    public static HandSide offArm(HandSide arm)
    {
        return arm == HandSide.RIGHT ? HandSide.LEFT : HandSide.RIGHT;
    }

    public static HandSide attackingArm(BipedEntityData<?> data, LivingEntity living)
    {
        final HandSide mainArm = living.getMainArm();

        if (!goblinbob.mobends.compat.OffHandCombatCompat.isModLoaded())
        {
            return mainArm;
        }

        return data.getTicksAfterOffHandAttack() < data.getTicksAfterAttack()
                ? offArm(mainArm)
                : mainArm;
    }

    public static boolean isDualWielding(BipedEntityData<?> data)
    {
        return goblinbob.mobends.compat.OffHandCombatCompat.isModLoaded()
                && data.isOffHandAttacking()
                && data.getTicksAfterAttack() < 10.0F;
    }

    public static Hand handOf(LivingEntity living, HandSide arm)
    {
        return arm == living.getMainArm() ? Hand.MAIN_HAND : Hand.OFF_HAND;
    }

    private static boolean holdsSword(LivingEntity living, HandSide arm)
    {
        return living.getItemInHand(handOf(living, arm)).getItem() instanceof SwordItem;
    }

    public static void resetTrails(BipedEntityData<?> data)
    {
        data.swordTrail.reset();
        data.offHandSwordTrail.reset();
    }

    public static void emitTrails(BipedEntityData<?> data, LivingEntity living, HandSide attackArm,
                                  float attackTicks, boolean dualWielding)
    {
        if (attackTicks < 4F && holdsSword(living, attackArm))
        {
            data.swordTrail.add(data, attackArm);
        }

        if (!dualWielding)
        {
            return;
        }

        final HandSide other = offArm(attackArm);

        if (ticksAfterAttack(data, living, other) < 4F && holdsSword(living, other))
        {
            data.offHandSwordTrail.add(data, other);
        }
    }

    public static float ticksAfterAttack(BipedEntityData<?> data, LivingEntity living, HandSide arm)
    {
        if (!goblinbob.mobends.compat.OffHandCombatCompat.isModLoaded())
        {
            return data.getTicksAfterAttack();
        }

        return arm == living.getMainArm()
                ? data.getTicksAfterAttack()
                : data.getTicksAfterOffHandAttack();
    }
}
