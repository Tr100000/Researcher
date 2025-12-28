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
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.Version;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Researcher implements ModInitializer {
    public static final String MODID = "researcher";
    public static final Logger LOGGER = LoggerFactory.getLogger("Researcher");
    public static final Gson GSON = TrUtils.GSON;

    @Override
    public void onInitialize() {
        ResearcherConfigs.init();

        ResearcherCriteriaTriggers.register();

        CommandRegistrationCallback.EVENT.register(ResearcherCommand::register);
        CommandRegistrationCallback.EVENT.register(ResearchCommand::register);

        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            if (player instanceof ServerPlayer serverPlayer) {
                ResearcherCriteriaTriggers.BLOCK_BROKEN.trigger(serverPlayer, state);
            }
        });

        ResearcherNetworking.registerPayloads();
        ResearcherNetworking.registerServerRecievers();

//        ResourceLoader.get(ResourceType.SERVER_DATA).addReloaderOrdering(
//                ResourceReloaderKeys.Server.RECIPES,
//                ResearchManager.ID
//        );

        RecipeSynchronization.synchronizeRecipeSerializer(RecipeSerializer.SHAPED_RECIPE);
        RecipeSynchronization.synchronizeRecipeSerializer(RecipeSerializer.SHAPELESS_RECIPE);
        RecipeSynchronization.synchronizeRecipeSerializer(RecipeSerializer.TRANSMUTE);
    }

    public static Version getVersion() {
        return FabricLoader.getInstance().getModContainer(MODID).orElseThrow().getMetadata().getVersion();
    }
}
