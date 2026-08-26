package goblinbob.mobends.forge.client.event;

import com.mojang.blaze3d.matrix.MatrixStack;
import goblinbob.mobends.api.addon.Addons;
import goblinbob.mobends.core.bender.EntityBender;
import goblinbob.mobends.core.bender.EntityBenderRegistry;
import goblinbob.mobends.core.client.MoBendsRenderContext;
import goblinbob.mobends.core.client.AnimatedRiderAnchor;
import goblinbob.mobends.core.client.OffscreenAnimationUpdater;
import goblinbob.mobends.core.client.event.DataUpdateHandler;
import goblinbob.mobends.core.data.EntityDatabase;
import goblinbob.mobends.core.data.LivingEntityData;
import goblinbob.mobends.lib.flux.ComputedDependencyHelper;
import goblinbob.mobends.core.mutators.Mutator;
import goblinbob.mobends.core.util.BenderHelper;
import goblinbob.mobends.compat.ModCompatManager;
import goblinbob.mobends.standard.mutators.BipedMutator;
import goblinbob.mobends.standard.mutators.PlayerMutator;
import goblinbob.mobends.standard.mutators.SpiderMutator;
import goblinbob.mobends.standard.mutators.SquidMutator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashSet;
import java.util.Set;

public class RenderingEventHandler
{
    private static final Set<Integer> entitiesWithPushedPose = new HashSet<>();
    private static final Set<Integer> ridersWithPushedPose = new HashSet<>();

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END)
            return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.isPaused())
            return;

        EntityDatabase.instance.updateClient();
        Addons.onClientTick();
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event)
    {
        if (event.phase != TickEvent.Phase.START)
            return;

        advanceAnimations(event.renderTickTime);
        renderTickDrivenThisFrame = true;
    }

    private static boolean renderTickDrivenThisFrame = false;

    private static void advanceAnimations(float renderTickTime)
    {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null)
            return;

        ComputedDependencyHelper.reevaluateDirty();

        if (!mc.isPaused())
        {
            DataUpdateHandler.partialTicks = renderTickTime;
        }

        goblinbob.mobends.core.client.TrailRenderQueue.clear();

        final float newTicks = mc.player.tickCount + renderTickTime;

        if (DataUpdateHandler.checkTicksRestart(newTicks))
        {
            EntityDatabase.instance.onTicksRestart();
        }

        if (!(mc.level.isClientSide && mc.isPaused()))
        {
            DataUpdateHandler.update(renderTickTime, newTicks);
            EntityDatabase.instance.updateRender(renderTickTime);
            Addons.onRenderTick(renderTickTime);
            OffscreenAnimationUpdater.updateIfNotRendered(renderTickTime);
        }
        else
        {
            DataUpdateHandler.onPaused();
        }
    }

    @SubscribeEvent
    public void beforeHandRender(RenderHandEvent event)
    {
        Minecraft mc = Minecraft.getInstance();
        Entity viewEntity = mc.getCameraEntity();

        if (!(viewEntity instanceof AbstractClientPlayerEntity))
            return;

        AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) viewEntity;

        if (!BenderHelper.isEntityAnimated(player))
            return;

        PlayerRenderer renderPlayer = (PlayerRenderer) mc.getEntityRenderDispatcher().getRenderer(player);
        PlayerMutator mutator = (PlayerMutator) BenderHelper.getMutatorForRenderer(AbstractClientPlayerEntity.class, renderPlayer);
        if (mutator != null)
        {
            mutator.poseForFirstPersonView();
            mutator.restoreVanillaPivots(renderPlayer.getModel());
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @SubscribeEvent
    public void beforeLivingRender(RenderLivingEvent.Pre<?, ?> event)
    {
        LivingEntity entity = event.getEntity();
        LivingRenderer renderer = event.getRenderer();
        float partialTicks = event.getPartialRenderTick();
        MatrixStack poseStack = event.getMatrixStack();

        if (entity == Minecraft.getInstance().player)
        {
            OffscreenAnimationUpdater.markPlayerRendered();
        }

        net.minecraft.util.math.vector.Vector3d riderOffset = AnimatedRiderAnchor.getRenderOffset(entity, partialTicks);
        if (riderOffset != null)
        {
            poseStack.pushPose();
            poseStack.translate(riderOffset.x, riderOffset.y, riderOffset.z);
            ridersWithPushedPose.add(entity.getId());
        }

        EntityBender bender = EntityBenderRegistry.instance.getForEntity(entity);
        if (bender == null)
            return;

        if (ModCompatManager.shouldDeferAnimation(entity))
        {
            bender.deapplyMutation(renderer, entity);
            return;
        }

        if (entity.isSpectator())
        {
            bender.deapplyMutation(renderer, entity);
            return;
        }

        poseStack.pushPose();
        entitiesWithPushedPose.add(entity.getId());

        if (bender.isAnimated())
        {
            boolean mutationApplied = bender.applyMutation(renderer, entity, partialTicks);

            if (mutationApplied)
            {
                final Object rawMutator = bender.getMutator(renderer);
                if (rawMutator == null)
                {
                    return;
                }

                final Mutator<?, LivingEntity, ?> mutator = (Mutator<?, LivingEntity, ?>) rawMutator;
                final LivingEntityData<LivingEntity> data = (LivingEntityData<LivingEntity>) mutator.getData(entity);

                MoBendsRenderContext.setCurrentEntity(entity);

                if (rawMutator instanceof BipedMutator<?, ?, ?>) {
            BipedMutator<?, ?, ?> bipedMutator = (BipedMutator<?, ?, ?>) rawMutator;
                    MoBendsRenderContext.setCurrentBipedMutator(bipedMutator);
                    MoBendsRenderContext.beginMainModelRender();

                    EntityModel<?> model = renderer.getModel();
                    BipedModel<?> humanoidModel = bipedMutator.humanoidViewOf(model);
                    if (humanoidModel != null)
                    {
                        MoBendsRenderContext.setCurrentVanillaModel(humanoidModel);
                        bipedMutator.syncPosesToVanillaModel(humanoidModel);
                    }
                }
                else if (rawMutator instanceof SpiderMutator) {
            SpiderMutator spiderMutator = (SpiderMutator) rawMutator;
                    MoBendsRenderContext.setCurrentSpiderMutator(spiderMutator);
                    MoBendsRenderContext.beginMainModelRender();
                }
                else if (rawMutator instanceof SquidMutator) {
            SquidMutator squidMutator = (SquidMutator) rawMutator;
                    MoBendsRenderContext.setCurrentSquidMutator(squidMutator);
                    MoBendsRenderContext.beginMainModelRender();
                }
                else if (rawMutator instanceof goblinbob.mobends.standard.mutators.WolfMutator) {
            goblinbob.mobends.standard.mutators.WolfMutator wolfMutator = (goblinbob.mobends.standard.mutators.WolfMutator) rawMutator;
                    MoBendsRenderContext.setCurrentWolfMutator(wolfMutator);
                    MoBendsRenderContext.beginMainModelRender();
                }
                MoBendsRenderContext.setCurrentRenderBuffers(event.getBuffers(), event.getLight());
                bender.beforeRender(data, entity, partialTicks, poseStack);
            }
        }
        else
        {
            bender.deapplyMutation(renderer, entity);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @SubscribeEvent
    public void afterLivingRender(RenderLivingEvent.Post<?, ?> event)
    {
        goblinbob.mobends.compat.CarryOnCompat.captureAnchor(
                event.getEntity(), MoBendsRenderContext.getCurrentBipedMutator());

        MoBendsRenderContext.clear();

        LivingEntity entity = event.getEntity();
        EntityBender bender = EntityBenderRegistry.instance.getForEntity(entity);

        if (bender != null && entitiesWithPushedPose.remove(entity.getId()))
        {
            bender.afterRender(entity, event.getPartialRenderTick(), event.getMatrixStack());
            event.getMatrixStack().popPose();
        }

        if (ridersWithPushedPose.remove(entity.getId()))
        {
            event.getMatrixStack().popPose();
        }
    }

    @SubscribeEvent
    public void onRenderWorldLast(net.minecraftforge.client.event.RenderWorldLastEvent event)
    {
        if (!renderTickDrivenThisFrame)
        {
            advanceAnimations(event.getPartialTicks());
        }
        renderTickDrivenThisFrame = false;

        goblinbob.mobends.core.client.TrailRenderQueue.flush();
    }
}
