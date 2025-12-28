package io.github.tr100000.researcher;

import io.github.tr100000.researcher.api.recipe.RecipeUnlockDisplayRegistry;
import io.github.tr100000.researcher.api.trigger.TriggerHandlerRegistry;
import io.github.tr100000.researcher.api.trigger.util.ComponentsPredicateHelper;
import io.github.tr100000.researcher.api.trigger.util.EntityPredicateHelper;
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
import net.minecraft.client.KeyMapping;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jspecify.annotations.NullMarked;
import org.lwjgl.glfw.GLFW;

@NullMarked
public class ResearcherClient implements ClientModInitializer {
    public static final KeyMapping.Category KEY_CATEGORY = KeyMapping.Category.register(ModUtils.id("main"));
    public static final KeyMapping OPEN_RESEARCH_SCREEN_KEY = KeyBindingHelper.registerKeyBinding(new KeyMapping(ModUtils.id("open_research_screen").toLanguageKey("key"), GLFW.GLFW_KEY_R, KEY_CATEGORY));

    public static final SoundEvent RESEARCH_FINISHED_SOUND = SoundEvent.createVariableRangeEvent(ModUtils.id("ui.toast.research_finished"));

    public static RecipeViewerDelegate recipeViewerDelegate;

    @Override
    public void onInitializeClient() {
        ResearcherTriggerHandlers.register();

        Registry.register(BuiltInRegistries.SOUND_EVENT, ModUtils.id("ui.toast.research_finished"), RESEARCH_FINISHED_SOUND);

        ResearcherClientNetworking.registerClientRecievers();

        ClientCommandRegistrationCallback.EVENT.register(ResearcherClientCommand::register);

        RecipeUnlockDisplayRegistry.register(RecipeSerializer.SHAPED_RECIPE, CraftingRecipeUnlockDisplay::createShaped);
        RecipeUnlockDisplayRegistry.register(RecipeSerializer.SHAPELESS_RECIPE, CraftingRecipeUnlockDisplay::createShapeless);
        RecipeUnlockDisplayRegistry.register(RecipeSerializer.TRANSMUTE, CraftingRecipeUnlockDisplay::createTransmute);

        HudElementRegistry.attachElementAfter(VanillaHudElements.CHAT, ResearchHud.LAYER_ID, ResearchHud::render);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (OPEN_RESEARCH_SCREEN_KEY.consumeClick()) {
                if (client.screen instanceof ResearchScreen researchScreen) {
                    client.setScreen(researchScreen.parent);
                }
                else {
                    client.setScreen(new ResearchScreen(client.screen));
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
            TriggerHandlerRegistry.printNonRegistered();
            EntityPredicateHelper.printNonRegistered();
            ComponentsPredicateHelper.printNonRegistered();
        }
    }
}
