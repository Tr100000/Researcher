package io.github.tr100000.researcher.mixin.client.compat.jei;

import io.github.tr100000.researcher.ClientResearchTracker;
import io.github.tr100000.researcher.compat.JeiRecipeLockedWidget;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.library.plugins.vanilla.crafting.CraftingRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingRecipeCategory.class)
public abstract class CraftingRecipeCategoryMixin implements IRecipeCategory<RecipeHolder<CraftingRecipe>> {
    @Shadow @Final
    public static int height;

    @Inject(method = "createRecipeExtras(Lmezz/jei/api/gui/widgets/IRecipeExtrasBuilder;Lnet/minecraft/world/item/crafting/RecipeHolder;Lmezz/jei/api/recipe/IFocusGroup;)V", at = @At("TAIL"))
    private void addRecipeLockedWidget(IRecipeExtrasBuilder builder, RecipeHolder<CraftingRecipe> recipeHolder, IFocusGroup focuses, CallbackInfo ci) {
        ClientResearchTracker tracker = getTracker();
        if (tracker == null) return;
        if (tracker.canCraftRecipe(recipeHolder.id().identifier())) return;

        final int texHeight = 16;
        builder.addWidget(new JeiRecipeLockedWidget(61, (height - texHeight) / 2, recipeHolder, tracker));
    }

    @Unique
    private @Nullable ClientResearchTracker getTracker() {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        if (connection == null) return null;
        return connection.researcher$getClientTracker();
    }
}
