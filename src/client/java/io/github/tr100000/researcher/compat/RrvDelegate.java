package io.github.tr100000.researcher.compat;

import cc.cassian.rrv.api.ActionType;
import cc.cassian.rrv.api.recipe.ReliableClientRecipe;
import cc.cassian.rrv.common.overlay.itemlist.view.ItemViewOverlay;
import cc.cassian.rrv.common.recipe.ClientRecipeCache;
import cc.cassian.rrv.common.recipe.inventory.RecipeViewMenu;
import cc.cassian.rrv.common.recipe.inventory.RecipeViewScreen;
import io.github.tr100000.trutils.api.utils.RecipeViewerDelegate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RrvDelegate implements RecipeViewerDelegate {
    private static final Minecraft client = Minecraft.getInstance();

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public boolean showAllRecipes() {
        LocalPlayer player = client.player;
        if (player == null) return false;

        List<ReliableClientRecipe> recipes = ClientRecipeCache.INSTANCE.getRecipes();
        return openRecipeScreen(client.screen, player.getInventory(), recipes);
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public boolean showRecipe(Identifier id) {
        LocalPlayer player = client.player;
        if (player == null) return false;

        ReliableClientRecipe recipe = ClientRecipeCache.INSTANCE.getRecipe(id);
        if (recipe == null) return false;
        return openRecipeScreen(client.screen, player.getInventory(), List.of(recipe));
    }

    @Override
    public boolean showRecipes(ItemStack stack) {
        ItemViewOverlay.INSTANCE.openRecipeView(stack, ActionType.RESULT);
        return didOpenRecipeScreen();
    }

    @Override
    public boolean showUses(ItemStack stack) {
        ItemViewOverlay.INSTANCE.openRecipeView(stack, ActionType.INPUT);
        return didOpenRecipeScreen();
    }

    private boolean openRecipeScreen(@Nullable Screen parent, Inventory inventory, List<? extends ReliableClientRecipe> recipes) {
        ArrayList<RecipeViewScreen> viewHistory = new ArrayList<>();
        if (parent instanceof RecipeViewScreen recipeViewScreen) {
            parent = recipeViewScreen.getMenu().getParentScreen();
            viewHistory = recipeViewScreen.getMenu().getViewHistory();
        }
        int containerId = parent instanceof AbstractContainerScreen<?> containerScreen ? containerScreen.getMenu().containerId : 0;
        client.setScreen(new RecipeViewScreen(new RecipeViewMenu(parent, containerId, inventory, recipes, ItemStack.EMPTY, ActionType.ANY, viewHistory), inventory, Component.empty()));
        return true;
    }

    private boolean didOpenRecipeScreen() {
        return client.screen instanceof RecipeViewScreen;
    }
}
