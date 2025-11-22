package io.github.tr100000.researcher.compat;

import io.github.tr100000.trutils.api.utils.RecipeViewerDelegate;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.runtime.IJeiRuntime;
import mezz.jei.common.Internal;
import mezz.jei.gui.util.FocusUtil;
import mezz.jei.library.runtime.JeiRuntime;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class JeiDelegate implements RecipeViewerDelegate {
    private static JeiRuntime runtime;
    private static FocusUtil focusUtil;

    public static void runtimeAvailable(IJeiRuntime runtime) {
        JeiDelegate.runtime = (JeiRuntime)runtime;
        focusUtil = new FocusUtil(runtime.getJeiHelpers().getFocusFactory(), Internal.getJeiClientConfigs().getClientConfig(), runtime.getIngredientManager());
    }

    public static void runtimeUnavailable() {
        runtime = null;
        focusUtil = null;
    }

    @Override
    public boolean showAllRecipes() {
        // This code throws an error for some reason
//        if (runtime == null) return false;
//
//        RecipesGui recipesGui = (RecipesGui)runtime.getRecipesGui();
//        return ((RecipesGuiAccessor)recipesGui).getLogic().showAllRecipes();

        return false;
    }

    @Override
    public boolean showRecipe(@NotNull Identifier id) {
        // I have no idea whether this is possible or not.
        return false;
    }

    @Override
    public boolean showRecipes(@NotNull ItemStack stack) {
        return show(stack, List.of(RecipeIngredientRole.OUTPUT));
    }

    @Override
    public boolean showUses(@NotNull ItemStack stack) {
        return show(stack, List.of(RecipeIngredientRole.INPUT));
    }

    private boolean show(@NotNull ItemStack stack, List<RecipeIngredientRole> roles) {
        if (runtime == null || focusUtil == null) return false;

        var typedIngredient = runtime.getIngredientManager().createTypedIngredient(stack, true);
        if (typedIngredient.isPresent()) {
            var focuses = focusUtil.createFocuses(typedIngredient.get(), roles);
            runtime.getRecipesGui().show(focuses);
            return true;
        }
        return false;
    }
}
