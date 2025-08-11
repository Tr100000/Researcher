package io.github.tr100000.researcher.mixin;

import io.github.tr100000.researcher.ResearcherCriteria;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Inject(method = "onCraftByPlayer", at = @At("TAIL"))
    public void onCraftByPlayer(ItemStack stack, PlayerEntity player, CallbackInfo ci) {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            ResearcherCriteria.ITEM_CRAFTED.trigger(serverPlayer, stack);
        }
    }
}
