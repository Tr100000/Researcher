package io.github.tr100000.researcher.mixin;

import io.github.tr100000.researcher.ResearchManager;
import io.github.tr100000.researcher.api.ResearchManagerGetter;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.registry.CombinedDynamicRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.ServerDynamicRegistryType;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.server.DataPackContents;
import net.minecraft.server.command.CommandManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(DataPackContents.class)
public class DataPackContentsMixin implements ResearchManagerGetter {
    @Unique
    private ResearchManager researchManager;

    @Inject(method = "<init>", at = @At(("TAIL")))
    private void init(CombinedDynamicRegistries<ServerDynamicRegistryType> dynamicRegistries, RegistryWrapper.WrapperLookup registries, FeatureSet enabledFeatures, CommandManager.RegistrationEnvironment environment, List<Registry.PendingTagLoad<?>> pendingTagLoads, int functionPermissionLevel, CallbackInfo ci) {
        researchManager = new ResearchManager((DataPackContents)(Object)this);
    }

    @Inject(method = "getContents", at = @At("RETURN"), cancellable = true)
    private void modifyGetContents(CallbackInfoReturnable<List<ResourceReloader>> cir) {
        List<ResourceReloader> contents = new ObjectArrayList<>(cir.getReturnValue());
        contents.add(researchManager);
        cir.setReturnValue(contents);
    }

    @Override @Unique
    public ResearchManager researcher$getServerManager() {
        return researchManager;
    }
}
