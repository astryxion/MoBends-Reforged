package goblinbob.mobends.core.bender;

import goblinbob.mobends.core.configuration.CoreClientConfig;
import goblinbob.mobends.core.mutators.IMutatorFactory;
import goblinbob.mobends.core.mutators.Mutator;
import goblinbob.mobends.standard.main.ModStatics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class BenderDiscovery
{
    private static boolean scanned = false;

    private BenderDiscovery()
    {
    }

    public static void scanForDerivedBenders()
    {
        if (scanned) return;

        World level = Minecraft.getInstance().level;
        if (level == null) return;

        scanned = true;

        try
        {
            runScan(level);
        }
        catch (Throwable ignored)
        {
        }
    }

    private static void runScan(World level)
    {
        List<EntityBender<?>> originals = new ArrayList<>(EntityBenderRegistry.instance.getRegistered());
        List<Candidate> candidates = new ArrayList<>();
        Set<String> claimedAppearances = new HashSet<>();

        for (EntityType<?> entityType : Registry.ENTITY_TYPE)
        {
            if (entityType.getCategory() == EntityClassification.MISC) continue;

            Entity probe;
            try
            {
                probe = entityType.create(level);
            }
            catch (Throwable t)
            {
                continue;
            }

            try
            {
                if (!(probe instanceof LivingEntity)) continue;
                LivingEntity living = (LivingEntity) probe;

                if (goblinbob.mobends.api.animation.MoBendsAnimationControl.isExcluded(entityType, living.getClass()))
                    continue;

                LivingRenderer<?, ?> renderer = resolveRenderer(living);
                String appearance = describeAppearance(living, renderer);

                if (EntityBenderRegistry.instance.hasBenderForClass(living.getClass()))
                {
                    if (appearance != null)
                    {
                        claimedAppearances.add(appearance);
                    }
                    continue;
                }

                EntityBender<?> parent = findMostSpecificParent(originals, living);
                if (parent == null) continue;
                if (!isModelAnimatable(renderer, parent)) continue;

                candidates.add(new Candidate(entityType, living.getClass(), parent, appearance));
            }
            catch (Throwable ignored)
            {
            }
            finally
            {
                discardQuietly(probe);
            }
        }

        List<EntityBender<?>> derived = new ArrayList<>();
        Set<EntityBender<?>> parentsWithDerived = new HashSet<>();

        for (Candidate candidate : candidates)
        {
            if (candidate.appearance != null && !claimedAppearances.add(candidate.appearance)) continue;

            EntityBender<?> bender = createDerived(candidate);
            if (bender == null) continue;

            derived.add(bender);
            parentsWithDerived.add(candidate.parent);
        }

        for (EntityBender<?> bender : derived)
        {
            EntityBenderRegistry.instance.registerBender(bender);
            bender.setAnimate(CoreClientConfig.getInstance().isEntityAnimated(bender.getKey()));
        }

        if (!derived.isEmpty())
        {
            for (EntityBender<?> parent : parentsWithDerived)
            {
                parent.demutateAll();
            }
            EntityBenderRegistry.instance.clearCache();
        }
    }

    @Nullable
    private static LivingRenderer<?, ?> resolveRenderer(LivingEntity entity)
    {
        try
        {
            EntityRenderer<?> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity);
            return renderer instanceof LivingRenderer<?, ?> ? (LivingRenderer<?, ?>) renderer : null;
        }
        catch (Throwable t)
        {
            return null;
        }
    }

    @Nullable
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static String describeAppearance(LivingEntity entity, @Nullable LivingRenderer<?, ?> renderer)
    {
        if (renderer == null) return null;

        EntityModel<?> model = renderer.getModel();
        if (model == null) return null;

        ResourceLocation texture;
        try
        {
            texture = ((EntityRenderer) renderer).getTextureLocation(entity);
        }
        catch (Throwable t)
        {
            return null;
        }

        return renderer.getClass().getName() + '|' + texture + '|' + model.getClass().getName();
    }

    private static boolean isModelAnimatable(@Nullable LivingRenderer<?, ?> renderer, EntityBender<?> parent)
    {
        if (renderer == null) return false;

        EntityModel<?> model = renderer.getModel();
        if (model == null) return false;

        Mutator<?, ?, ?> mutator = createProbeMutator(parent);
        return mutator != null && !mutator.shouldModelBeSkipped(model);
    }

    @Nullable
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Mutator<?, ?, ?> createProbeMutator(EntityBender<?> parent)
    {
        try
        {
            IMutatorFactory factory = parent.getMutatorFactory();
            return factory != null ? factory.createMutator(parent.getDataFactory()) : null;
        }
        catch (Throwable t)
        {
            return null;
        }
    }

    private static void discardQuietly(@Nullable Entity entity)
    {
        if (entity == null) return;

        try
        {
            entity.remove();
        }
        catch (Throwable ignored)
        {
        }
    }

    @Nullable
    private static EntityBender<?> findMostSpecificParent(List<EntityBender<?>> candidates, LivingEntity entity)
    {
        EntityBender<?> best = null;

        for (EntityBender<?> candidate : candidates)
        {
            if (!candidate.entityClass.isInstance(entity)) continue;

            if (best == null || best.entityClass.isAssignableFrom(candidate.entityClass))
            {
                best = candidate;
            }
        }

        return best;
    }

    @Nullable
    @SuppressWarnings("unchecked")
    private static <T extends LivingEntity> EntityBender<?> createDerived(Candidate candidate)
    {
        ResourceLocation location = Registry.ENTITY_TYPE.getKey(candidate.entityType);
        if (location == null) return null;

        String unlocalizedName = "entity." + location.getNamespace() + "." + location.getPath();

        return new DerivedEntityBender<>(ModStatics.MODID, location.toString(), unlocalizedName,
                (Class<T>) candidate.entityClass, candidate.entityType, candidate.parent);
    }

    private static final class Candidate
    {
        final EntityType<?> entityType;
        final Class<? extends LivingEntity> entityClass;
        final EntityBender<?> parent;
        @Nullable
        final String appearance;

        Candidate(EntityType<?> entityType, Class<? extends LivingEntity> entityClass,
                  EntityBender<?> parent, @Nullable String appearance)
        {
            this.entityType = entityType;
            this.entityClass = entityClass;
            this.parent = parent;
            this.appearance = appearance;
        }
    }
}
