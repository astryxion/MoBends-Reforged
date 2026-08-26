package goblinbob.mobends.standard;

import goblinbob.mobends.api.addon.AddonAnimationRegistry;
import goblinbob.mobends.api.addon.IAddon;
import goblinbob.mobends.standard.client.model.armor.ArmorModelFactory;
import goblinbob.mobends.standard.client.renderer.entity.ArrowTrailManager;
import goblinbob.mobends.standard.client.renderer.entity.mutated.*;
import goblinbob.mobends.standard.data.*;
import goblinbob.mobends.standard.kumo.WolfStateCondition;
import goblinbob.mobends.standard.main.ModConfig;
import goblinbob.mobends.standard.mutators.*;
import goblinbob.mobends.standard.previewer.BipedPreviewer;
import goblinbob.mobends.standard.previewer.PiglinPreviewer;
import goblinbob.mobends.standard.previewer.PlayerPreviewer;
import goblinbob.mobends.standard.previewer.SpiderPreviewer;
import goblinbob.mobends.standard.previewer.SquidPreviewer;
import goblinbob.mobends.standard.previewer.WolfPreviewer;
import goblinbob.mobends.standard.previewer.ZombiePreviewer;
import net.minecraft.entity.monster.EvokerEntity;
import net.minecraft.entity.monster.IllusionerEntity;
import net.minecraft.entity.monster.PillagerEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.StrayEntity;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinBruteEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.passive.WolfEntity;

public class DefaultAddon implements IAddon
{
	protected static final String[] BIPED_ANIMATIONS = {"walk", "jump", "fall", "attack", "ride"};
	protected static final String[] SPRINTING_BIPED_ANIMATIONS = {"walk", "sprint", "jump", "fall", "attack", "ride"};
	private static final String[] SPIDER_ANIMATIONS = {"walk", "jump"};
	private static final String[] SQUID_ANIMATIONS = {"swim"};
	private static final String[] WOLF_ANIMATIONS = {"walk", "sit"};

	protected static final String[] BIPED_PARTS = {
			"head", "body", "leftArm", "rightArm", "leftForeArm", "rightForeArm",
			"leftLeg", "rightLeg", "leftForeLeg", "rightForeLeg"};

	protected static final float STRAY_CLOTHING_DEFORMATION = 0.25F;
	protected static final float BOGGED_CLOTHING_DEFORMATION = 0.2F;

	@Override
	public void registerContent(AddonAnimationRegistry registry)
	{
		registry.registerEntity(new PlayerBender());

		registry.registerNewEntity(ZombieEntity.class, ZombieData::new, ZombieMutator::new, new ZombieRenderer<>(),
				new ZombiePreviewer(), BIPED_ANIMATIONS,
				"head", "body", "leftArm", "rightArm", "leftForeArm", "rightForeArm",
				"leftLeg", "rightLeg", "leftForeLeg", "rightForeLeg");

		registry.registerNewEntity(SkeletonEntity.class, SkeletonData::new, SkeletonMutator::new, new BipedRenderer<>(),
				new BipedPreviewer<>(), SPRINTING_BIPED_ANIMATIONS, BIPED_PARTS);

		registry.registerNewEntity(WitherSkeletonEntity.class, SkeletonData::new, SkeletonMutator::new, new BipedRenderer<>(),
				new BipedPreviewer<>(), SPRINTING_BIPED_ANIMATIONS, BIPED_PARTS);

		registry.registerNewEntity(StrayEntity.class, SkeletonData::new,
				dataFactory -> new SkeletonMutator<>(dataFactory, STRAY_CLOTHING_DEFORMATION), new BipedRenderer<>(),
				new BipedPreviewer<>(), SPRINTING_BIPED_ANIMATIONS, BIPED_PARTS);

		registry.registerNewEntity(ZombifiedPiglinEntity.class, PigZombieData::new, PigZombieMutator::new, new ZombieRenderer<>(),
				new BipedPreviewer<>(), BIPED_ANIMATIONS,
				"head", "body", "leftArm", "rightArm", "leftForeArm", "rightForeArm",
				"leftLeg", "rightLeg", "leftForeLeg", "rightForeLeg");

		registry.registerNewEntity(PiglinEntity.class, PiglinData::new, PiglinMutator::new, new BipedRenderer<>(),
				new PiglinPreviewer<>(), SPRINTING_BIPED_ANIMATIONS, BIPED_PARTS);

		registry.registerNewEntity(PiglinBruteEntity.class, PiglinData::new, PiglinMutator::new, new BipedRenderer<>(),
				new PiglinPreviewer<>(), SPRINTING_BIPED_ANIMATIONS, BIPED_PARTS);

		registry.registerNewEntity(PillagerEntity.class, IllagerData::new, IllagerMutator::new, new BipedRenderer<>(),
				new BipedPreviewer<>(), SPRINTING_BIPED_ANIMATIONS, BIPED_PARTS);

		registry.registerNewEntity(VindicatorEntity.class, IllagerData::new, IllagerMutator::new, new BipedRenderer<>(),
				new BipedPreviewer<>(), SPRINTING_BIPED_ANIMATIONS, BIPED_PARTS);

		registry.registerNewEntity(EvokerEntity.class, IllagerData::new, IllagerMutator::new, new BipedRenderer<>(),
				new BipedPreviewer<>(), SPRINTING_BIPED_ANIMATIONS, BIPED_PARTS);

		registry.registerNewEntity(IllusionerEntity.class, IllagerData::new, IllagerMutator::new, new BipedRenderer<>(),
				new BipedPreviewer<>(), SPRINTING_BIPED_ANIMATIONS, BIPED_PARTS);

		registry.registerNewEntity(SpiderEntity.class, SpiderData::new, SpiderMutator::new, new SpiderRenderer<>(),
				new SpiderPreviewer(), SPIDER_ANIMATIONS,
				"head", "body", "neck", "leg1", "leg2", "leg3", "leg4", "leg5", "leg6", "leg7", "leg8",
				"foreLeg1", "foreLeg2", "foreLeg3", "foreLeg4", "foreLeg5", "foreLeg6", "foreLeg7", "foreLeg8");

		registry.registerNewEntity(SquidEntity.class, SquidData::new, SquidMutator::new, new SquidRenderer<>(),
				new SquidPreviewer(), SQUID_ANIMATIONS,
				"body", "tentacle1", "tentacle2", "tentacle3", "tentacle4", "tentacle5", "tentacle6", "tentacle7", "tentacle8");

		registry.registerNewEntity(WolfEntity.class, WolfData::new, WolfMutator::new, new WolfRenderer<>(),
				new WolfPreviewer(), WOLF_ANIMATIONS,
				"head", "body", "mane", "tail", "leg1", "leg2", "leg3", "leg4",
				"foreLeg1", "foreLeg2", "foreLeg3", "foreLeg4",
				"nose", "mouth", "tongue", "leftEar", "rightEar");

		registry.registerTriggerCondition("wolf_state", WolfStateCondition::new, WolfStateCondition.Template.class);

		registerVersionSpecificContent(registry);
	}

	protected void registerVersionSpecificContent(AddonAnimationRegistry registry)
	{
	}

	@Override
	public void onRenderTick(float partialTicks)
	{
		if (ModConfig.showArrowTrails || ModConfig.tridentTrail)
			ArrowTrailManager.onRenderTick();
		PlayerPreviewer.updatePreviewData(partialTicks);
	}

	@Override
	public void onClientTick()
	{
		PlayerPreviewer.updatePreviewDataClient();
	}

	@Override
	public void onRefresh()
	{
		ArmorModelFactory.refresh();
	}

	@Override
	public String getDisplayName()
	{
		return "Default";
	}
}
