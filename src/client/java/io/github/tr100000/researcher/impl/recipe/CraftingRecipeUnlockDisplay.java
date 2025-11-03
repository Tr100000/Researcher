package io.github.tr100000.researcher.impl.recipe;

import io.github.tr100000.researcher.api.RecipeUnlockDisplay;
import io.github.tr100000.trutils.api.gui.ItemIcon;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.recipe.TransmuteRecipe;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.util.Identifier;
import net.minecraft.util.context.ContextParameterMap;

public final class CraftingRecipeUnlockDisplay {
    private CraftingRecipeUnlockDisplay() {}

    public static RecipeUnlockDisplay createShaped(RecipeEntry<ShapedRecipe> entry) {
        return createNormal(entry.value(), entry.id().getValue());
    }

    public static RecipeUnlockDisplay createShapeless(RecipeEntry<ShapelessRecipe> entry) {
        return createNormal(entry.value(), entry.id().getValue());
    }

    private static RecipeUnlockDisplay createNormal(CraftingRecipe recipe, Identifier recipeId) {
        if (recipe.getDisplays().isEmpty()) return RecipeUnlockDisplay.ERROR;
        if (recipe.getIngredientPlacement().hasNoPlacement()) return RecipeUnlockDisplay.ERROR;

        ContextParameterMap contextParameterMap = RecipeUnlockDisplay.contextParameterMap();
        RecipeDisplay recipeDisplay = recipe.getDisplays().getFirst();
        CraftingRecipeTooltipComponent tooltip = CraftingRecipeTooltipComponent.create(
                recipeId,
                recipe instanceof ShapedRecipe shaped ? shaped.getWidth() : 3,
                recipe.getIngredientPlacement(),
                recipeDisplay.result(),
                contextParameterMap
        );
        ItemStack resultStack = recipeDisplay.result().getFirst(contextParameterMap);
        return new RecipeUnlockDisplay.Impl(new ItemIcon(resultStack), tooltip);
    }

    public static RecipeUnlockDisplay createTransmute(RecipeEntry<TransmuteRecipe> entry) {
        TransmuteRecipe recipe = entry.value();
        if (recipe.getDisplays().isEmpty()) return RecipeUnlockDisplay.ERROR;
        if (recipe.getIngredientPlacement().hasNoPlacement()) return RecipeUnlockDisplay.ERROR;

        ContextParameterMap contextParameterMap = RecipeUnlockDisplay.contextParameterMap();
        RecipeDisplay recipeDisplay = recipe.getDisplays().getFirst();
        CraftingRecipeTooltipComponent tooltip = CraftingRecipeTooltipComponent.create(
                entry.id().getValue(),
                2,
                recipe.getIngredientPlacement(),
                recipeDisplay.result(),
                contextParameterMap
        );
        ItemStack resultStack = recipeDisplay.result().getFirst(contextParameterMap);
        return new RecipeUnlockDisplay.Impl(new ItemIcon(resultStack), tooltip);
    }
}
