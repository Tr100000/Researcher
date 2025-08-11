package io.github.tr100000.researcher.mixin;

import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.advancement.criterion.Criterion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Criterion.ConditionsContainer.class)
public abstract class CriterionConditionsContainerMixin {
    @Shadow @Final private String id;

    @Shadow @Final private AdvancementEntry advancement;

    @Inject(method = "grant", at = @At("HEAD"), cancellable = true)
    public void grant(PlayerAdvancementTracker tracker, CallbackInfo ci) {
        if (advancement == null) {
            tracker.owner.researcher$getPlayerTracker().incrementCriterion(id);
            ci.cancel();
        }
    }
}
