package io.github.tr100000.researcher.mixin;

import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeUnlocker;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RecipeUnlocker.class)
public interface RecipeUnlockerMixin {
    @Inject(method = "shouldCraftRecipe", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/RecipeUnlocker;setLastRecipe(Lnet/minecraft/recipe/RecipeEntry;)V"), cancellable = true)
    // Only used by crafting (as of 1.21.8)
    private void shouldCraftRecipe(ServerPlayerEntity player, RecipeEntry<?> recipe, CallbackInfoReturnable<Boolean> cir) {
        if (!player.researcher$getPlayerTracker().canCraftRecipe(recipe.id().getValue())) {
            cir.setReturnValue(false);
        }
    }
}
