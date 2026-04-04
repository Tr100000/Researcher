package io.github.tr100000.researcher.mixin.client;

import io.github.tr100000.researcher.api.reward.ResearchClientRewardRegistry;
import io.github.tr100000.researcher.api.trigger.TriggerHandlerRegistry;
import io.github.tr100000.researcher.api.trigger.util.ComponentsPredicateHelper;
import io.github.tr100000.researcher.api.trigger.util.EntityPredicateHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuiltInRegistries.class)
public class BuiltInRegistriesMixin {
    @Inject(method = "bootStrap", at = @At("TAIL"))
    private static void bootStrap(CallbackInfo ci) {
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            TriggerHandlerRegistry.printNonRegistered();
            EntityPredicateHelper.printNonRegistered();
            ComponentsPredicateHelper.printNonRegistered();
            ResearchClientRewardRegistry.printNonRegistered();
        }
    }
}
