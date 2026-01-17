package io.github.tr100000.researcher.mixin;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.server.PlayerAdvancements;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CriterionTrigger.Listener.class)
public abstract class CriterionTriggerListenerMixin {
    @Shadow @Final private String criterion;

    @Shadow @Final private AdvancementHolder advancement;

    @Inject(method = "run", at = @At("HEAD"), cancellable = true)
    public void grant(PlayerAdvancements tracker, CallbackInfo ci) {
        if (advancement == null) {
            tracker.player.researcher$getPlayerTracker().incrementCriterion(criterion);
            ci.cancel();
        }
    }
}
