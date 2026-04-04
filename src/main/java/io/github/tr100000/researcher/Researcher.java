package io.github.tr100000.researcher;

import com.google.gson.Gson;
import io.github.tr100000.researcher.command.ResearchCommand;
import io.github.tr100000.researcher.command.ResearcherCommand;
import io.github.tr100000.researcher.config.ResearcherConfigs;
import io.github.tr100000.researcher.networking.ResearcherNetworking;
import io.github.tr100000.trutils.TrUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.recipe.v1.sync.RecipeSynchronization;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.reloader.ResourceReloaderKeys;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.Version;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.item.crafting.TransmuteRecipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Researcher implements ModInitializer {
    public static final String MODID = "researcher";
    public static final Logger LOGGER = LoggerFactory.getLogger("Researcher");
    public static final Gson GSON = TrUtils.GSON;

    @Override
    public void onInitialize() {
        ResearcherConfigs.init();

        ResearcherRegistries.register();
        ResearcherRewardTypes.register();
        ResearcherCriteriaTriggers.register();

        CommandRegistrationCallback.EVENT.register(ResearcherCommand::register);
        CommandRegistrationCallback.EVENT.register(ResearchCommand::register);

        PlayerBlockBreakEvents.AFTER.register((_, player, _, state, _) -> {
            if (player instanceof ServerPlayer serverPlayer) {
                ResearcherCriteriaTriggers.BLOCK_BROKEN.trigger(serverPlayer, state);
            }
        });

        ResearcherNetworking.registerPayloads();
        ResearcherNetworking.registerServerRecievers();

        ResourceLoader.get(PackType.SERVER_DATA).addListenerOrdering(
                ResourceReloaderKeys.Server.RECIPES,
                ResearchManager.ID
        );

        RecipeSynchronization.synchronizeRecipeSerializer(ShapedRecipe.SERIALIZER);
        RecipeSynchronization.synchronizeRecipeSerializer(ShapelessRecipe.SERIALIZER);
        RecipeSynchronization.synchronizeRecipeSerializer(TransmuteRecipe.SERIALIZER);
    }

    public static Version getVersion() {
        return FabricLoader.getInstance().getModContainer(MODID).orElseThrow().getMetadata().getVersion();
    }
}
