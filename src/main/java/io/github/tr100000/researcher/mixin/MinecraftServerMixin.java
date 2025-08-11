package io.github.tr100000.researcher.mixin;

import io.github.tr100000.researcher.ResearchManager;
import io.github.tr100000.researcher.api.ResearchManagerGetter;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements ResearchManagerGetter {
    @Shadow private MinecraftServer.ResourceManagerHolder resourceManagerHolder;

    @Override @Unique
    public ResearchManager researcher$getServerManager() {
        return resourceManagerHolder.dataPackContents().researcher$getServerManager();
    }
}
