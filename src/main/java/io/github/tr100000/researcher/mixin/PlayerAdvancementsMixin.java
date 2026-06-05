package io.github.tr100000.researcher.mixin;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancements.class)
public abstract class PlayerAdvancementsMixin {
    @Shadow
    public ServerPlayer player;

    @Inject(method = "award", at = @At("HEAD"), cancellable = true)
    public void award(@Nullable AdvancementHolder holder, String criterion, CallbackInfoReturnable<Boolean> cir) {
        if (holder == null) {
            player.researcher$getPlayerTracker().incrementCriterion(criterion);
            cir.cancel();
        }
    }
}
