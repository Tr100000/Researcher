package io.github.tr100000.researcher.mixin.client.compat.jei;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.tr100000.researcher.compat.JeiDelegate;
import mezz.jei.library.runtime.JeiRuntime;
import mezz.jei.library.startup.JeiStarter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JeiStarter.class)
public abstract class JeiStarterMixin {
    @Inject(method = "start", at = @At(value = "INVOKE", target = "Lmezz/jei/common/Internal;setRuntime(Lmezz/jei/api/runtime/IJeiRuntime;)V"))
    private void onRuntimeAvailable(CallbackInfo ci, @Local(name = "jeiRuntime") JeiRuntime jeiRuntime) {
        JeiDelegate.runtimeAvailable(jeiRuntime);
    }

    @Inject(method = "stop", at = @At(value = "INVOKE", target = "Lmezz/jei/common/Internal;onRuntimeStopped()V"))
    private void onRuntimeUnavailable(CallbackInfo ci) {
        JeiDelegate.runtimeUnavailable();
    }
}
