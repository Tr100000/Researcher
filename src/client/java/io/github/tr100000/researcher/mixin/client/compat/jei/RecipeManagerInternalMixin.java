package io.github.tr100000.researcher.mixin.client.compat.jei;

import io.github.tr100000.researcher.compat.JeiDelegate;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.library.recipes.RecipeManagerInternal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(RecipeManagerInternal.class)
public abstract class RecipeManagerInternalMixin {
    @Inject(method = "addRecipe", at = @At("RETURN"))
    private <T> void addRecipe(IRecipeCategory<T> recipeCategory, T recipe, Set<T> hiddenRecipes, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ()) {
            JeiDelegate.addRecipe(recipe, recipeCategory);
        }
    }
}
