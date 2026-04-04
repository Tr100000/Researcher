package io.github.tr100000.researcher;

import io.github.tr100000.researcher.api.ResearchRewardType;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class ResearcherRegistries {
    private ResearcherRegistries() {}

    public static final ResourceKey<Registry<ResearchRewardType<?>>> RESEARCH_REWARD_TYPE_KEY = ResourceKey.createRegistryKey(ModUtils.id("research_reward_type"));
    public static final Registry<ResearchRewardType<?>> RESEARCH_REWARD_TYPE = FabricRegistryBuilder.create(RESEARCH_REWARD_TYPE_KEY).buildAndRegister();

    public static void register() {}
}
