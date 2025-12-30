package io.github.tr100000.researcher.compat;

import io.github.tr100000.researcher.mixin.client.compat.jei.RecipesGuiAccessor;
import io.github.tr100000.trutils.api.utils.RecipeViewerDelegate;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.runtime.IJeiRuntime;
import mezz.jei.common.Internal;
import mezz.jei.gui.recipes.RecipesGui;
import mezz.jei.gui.util.FocusUtil;
import mezz.jei.library.runtime.JeiRuntime;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JeiDelegate implements RecipeViewerDelegate {
    private static @Nullable JeiRuntime runtime;
    private static @Nullable FocusUtil focusUtil;
    private static final Map<Object, IRecipeCategory<?>> CATEGORY_MAP = new Object2ObjectOpenHashMap<>();

    @ApiStatus.Internal
    public static void runtimeAvailable(IJeiRuntime runtime) {
        JeiDelegate.runtime = (JeiRuntime)runtime;
        focusUtil = new FocusUtil(runtime.getJeiHelpers().getFocusFactory(), Internal.getJeiClientConfigs().getClientConfig(), runtime.getIngredientManager());
    }

    @ApiStatus.Internal
    public static void runtimeUnavailable() {
        runtime = null;
        focusUtil = null;
        CATEGORY_MAP.clear();
    }

    @ApiStatus.Internal
    public static <T> void addRecipe(T recipe, IRecipeCategory<T> category) {
        CATEGORY_MAP.put(recipe, category);
    }

    @Override
    public boolean showAllRecipes() {
        if (runtime == null) return false;

        RecipesGui recipesGui = (RecipesGui)runtime.getRecipesGui();
        return ((RecipesGuiAccessor)recipesGui).getLogic().showAllRecipes();
    }

    @Override
    public boolean showRecipe(Identifier id) {
        if (runtime == null) return false;
        RecipeHolder<?> recipeHolder = Internal.getClientSyncedRecipes().byKey(ResourceKey.create(Registries.RECIPE, id));
        if (recipeHolder == null) return false;
        return showRecipeTyped(recipeHolder);
    }

    @SuppressWarnings("unchecked")
    private <T extends Recipe<?>> boolean showRecipeTyped(RecipeHolder<T> recipeHolder) {
        assert runtime != null;
        IRecipeCategory<RecipeHolder<T>> holderRecipeCategory = (IRecipeCategory<@NotNull RecipeHolder<T>>)CATEGORY_MAP.get(recipeHolder);
        if (holderRecipeCategory != null) {
            runtime.getRecipesGui().showRecipes(holderRecipeCategory, List.of(recipeHolder), List.of());
            return true;
        }

        IRecipeCategory<T> recipeCategory = (IRecipeCategory<T>)CATEGORY_MAP.get(recipeHolder.value());
        if (recipeCategory != null) {
            runtime.getRecipesGui().showRecipes(recipeCategory, List.of(recipeHolder.value()), List.of());
            return true;
        }

        return false;
    }

    @Override
    public boolean showRecipes(ItemStack stack) {
        return show(stack, List.of(RecipeIngredientRole.OUTPUT));
    }

    @Override
    public boolean showUses(ItemStack stack) {
        return show(stack, List.of(RecipeIngredientRole.INPUT));
    }

    private boolean show(ItemStack stack, List<RecipeIngredientRole> roles) {
        if (runtime == null || focusUtil == null) return false;

        Optional<ITypedIngredient<ItemStack>> typedIngredient = runtime.getIngredientManager().createTypedIngredient(stack, true);
        if (typedIngredient.isPresent()) {
            List<IFocus<?>> focuses = focusUtil.createFocuses(typedIngredient.get(), roles);
            runtime.getRecipesGui().show(focuses);
            return true;
        }
        return false;
    }
}
