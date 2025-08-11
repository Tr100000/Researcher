package io.github.tr100000.researcher.mixin.client;

import io.github.tr100000.researcher.ClientResearchTracker;
import io.github.tr100000.researcher.api.ClientResearchTrackerGetter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientConnectionState;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.ClientConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin implements ClientResearchTrackerGetter {
    @Unique
    private ClientResearchTracker researchTracker;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(MinecraftClient client, ClientConnection clientConnection, ClientConnectionState clientConnectionState, CallbackInfo ci) {
        researchTracker = new ClientResearchTracker();
    }

    @Override @Unique
    public ClientResearchTracker researcher$getClientTracker() {
        return researchTracker;
    }
}
