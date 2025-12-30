package io.github.tr100000.researcher.mixin.client.compat.jei;

import mezz.jei.gui.recipes.IRecipeGuiLogic;
import mezz.jei.gui.recipes.RecipesGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RecipesGui.class)
public interface RecipesGuiAccessor {
    @Accessor
    IRecipeGuiLogic getLogic();
}
