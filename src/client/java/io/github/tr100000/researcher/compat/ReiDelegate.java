package io.github.tr100000.researcher.compat;

import io.github.tr100000.trutils.api.utils.RecipeViewerDelegate;
import me.shedaniel.rei.api.client.view.ViewSearchBuilder;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class ReiDelegate implements RecipeViewerDelegate {
    @Override
    public boolean showAllRecipes() {
        return ViewSearchBuilder.builder().addAllCategories().open();
    }

    @Override
    public boolean showRecipe(@NotNull Identifier id) {
        // Unfortunately this doesn't work
        return false;
    }

    @Override
    public boolean showRecipes(@NotNull ItemStack stack) {
        return ViewSearchBuilder.builder().addRecipesFor(EntryStacks.of(stack)).open();
    }

    @Override
    public boolean showUses(@NotNull ItemStack stack) {
        return ViewSearchBuilder.builder().addUsagesFor(EntryStacks.of(stack)).open();
    }
}
