package io.github.tr100000.researcher;

import io.github.tr100000.researcher.api.criterion.CriterionHandlerRegistry;
import io.github.tr100000.researcher.api.criterion.util.ComponentsPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.EntityPredicateHelper;
import io.github.tr100000.researcher.api.recipe.RecipeUnlockDisplayRegistry;
import io.github.tr100000.researcher.command.ResearcherClientCommand;
import io.github.tr100000.researcher.compat.JeiDelegate;
import io.github.tr100000.researcher.compat.ReiDelegate;
import io.github.tr100000.researcher.impl.recipe.CraftingRecipeUnlockDisplay;
import io.github.tr100000.researcher.networking.ResearcherClientNetworking;
import io.github.tr100000.researcher.screen.ResearchHud;
import io.github.tr100000.researcher.screen.ResearchScreen;
import io.github.tr100000.trutils.api.utils.RecipeViewerDelegate;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import org.lwjgl.glfw.GLFW;

public class ResearcherClient implements ClientModInitializer {
    public static final KeyBinding.Category KEY_CATEGORY = KeyBinding.Category.create(ModUtils.id("main"));
    public static final KeyBinding OPEN_RESEARCH_SCREEN_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(ModUtils.id("open_research_screen").toTranslationKey("key"), GLFW.GLFW_KEY_R, KEY_CATEGORY));

    public static final SoundEvent RESEARCH_FINISHED_SOUND = SoundEvent.of(ModUtils.id("ui.toast.research_finished"));

    public static RecipeViewerDelegate recipeViewerDelegate;

    @Override
    public void onInitializeClient() {
        ResearcherCriterionHandlers.register();

        Registry.register(Registries.SOUND_EVENT, ModUtils.id("ui.toast.research_finished"), RESEARCH_FINISHED_SOUND);

        ResearcherClientNetworking.registerClientRecievers();

        ClientCommandRegistrationCallback.EVENT.register(ResearcherClientCommand::register);

        RecipeUnlockDisplayRegistry.register(RecipeSerializer.SHAPED, CraftingRecipeUnlockDisplay::createShaped);
        RecipeUnlockDisplayRegistry.register(RecipeSerializer.SHAPELESS, CraftingRecipeUnlockDisplay::createShapeless);
        RecipeUnlockDisplayRegistry.register(RecipeSerializer.CRAFTING_TRANSMUTE, CraftingRecipeUnlockDisplay::createTransmute);

        HudElementRegistry.attachElementAfter(VanillaHudElements.CHAT, ResearchHud.LAYER_ID, ResearchHud::render);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (OPEN_RESEARCH_SCREEN_KEY.wasPressed()) {
                if (client.currentScreen instanceof ResearchScreen researchScreen) {
                    client.setScreen(researchScreen.parent);
                }
                else {
                    client.setScreen(new ResearchScreen(client.currentScreen));
                }
            }
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> ResearchScreen.setSelected(null));

        if (FabricLoader.getInstance().isModLoaded("rei")) {
            recipeViewerDelegate = new ReiDelegate();
        }
        else if (FabricLoader.getInstance().isModLoaded("jei")) {
            recipeViewerDelegate = new JeiDelegate();
        }
        else {
            recipeViewerDelegate = RecipeViewerDelegate.NONE;
        }

        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            CriterionHandlerRegistry.printNonRegistered();
            EntityPredicateHelper.printNonRegistered();
            ComponentsPredicateHelper.printNonRegistered();
        }
    }
}
