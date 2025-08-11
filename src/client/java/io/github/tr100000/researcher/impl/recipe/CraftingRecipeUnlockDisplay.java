package io.github.tr100000.researcher.impl.recipe;

import io.github.tr100000.researcher.api.RecipeUnlockDisplay;
import io.github.tr100000.trutils.api.gui.ItemIconRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.recipe.TransmuteRecipe;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.SlotDisplayContexts;
import net.minecraft.util.Identifier;
import net.minecraft.util.context.ContextParameterMap;

public final class CraftingRecipeUnlockDisplay {
    private CraftingRecipeUnlockDisplay() {}

    public static RecipeUnlockDisplay create(RecipeEntry<CraftingRecipe> entry) {
        CraftingRecipe recipe = entry.value();
        return switch (recipe) {
            case ShapedRecipe shapedRecipe -> createNormal(shapedRecipe, entry.id().getValue());
            case ShapelessRecipe shapelessRecipe -> createNormal(shapelessRecipe, entry.id().getValue());
            case TransmuteRecipe transmuteRecipe -> createTransmute(transmuteRecipe, entry.id().getValue());
            default -> throw new IllegalStateException("Unexpected value: " + recipe);
        };
    }

    private static RecipeUnlockDisplay createNormal(CraftingRecipe recipe, Identifier recipeId) {
        if (recipe.getDisplays().isEmpty()) return RecipeUnlockDisplay.ERROR;
        if (recipe.getIngredientPlacement().hasNoPlacement()) return RecipeUnlockDisplay.ERROR;

        ContextParameterMap contextParameterMap = SlotDisplayContexts.createParameters(MinecraftClient.getInstance().world);
        RecipeDisplay recipeDisplay = recipe.getDisplays().getFirst();
        CraftingRecipeTooltipComponent tooltip = CraftingRecipeTooltipComponent.create(
                recipeId,
                recipe instanceof ShapedRecipe shaped ? shaped.getWidth() : 3,
                recipe.getIngredientPlacement(),
                recipeDisplay.result(),
                contextParameterMap
        );
        ItemStack resultStack = recipeDisplay.result().getFirst(contextParameterMap);
        return new RecipeUnlockDisplay.Impl(new ItemIconRenderer(resultStack), tooltip);
    }

    private static RecipeUnlockDisplay createTransmute(TransmuteRecipe recipe, Identifier recipeId) {
        ContextParameterMap contextParameterMap = SlotDisplayContexts.createParameters(MinecraftClient.getInstance().world);
        RecipeDisplay recipeDisplay = recipe.getDisplays().getFirst();
        CraftingRecipeTooltipComponent tooltip = CraftingRecipeTooltipComponent.create(
                recipeId,
                2,
                recipe.getIngredientPlacement(),
                recipeDisplay.result(),
                contextParameterMap
        );
        ItemStack resultStack = recipeDisplay.result().getFirst(contextParameterMap);
        return new RecipeUnlockDisplay.Impl(new ItemIconRenderer(resultStack), tooltip);
    }

}
