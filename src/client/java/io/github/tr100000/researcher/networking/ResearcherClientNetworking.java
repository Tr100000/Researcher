package io.github.tr100000.researcher.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public final class ResearcherClientNetworking extends ResearcherNetworking {
    private ResearcherClientNetworking() {}

    public static void registerClientRecievers() {
        ClientPlayNetworking.registerGlobalReceiver(ResearchUpdateS2CPacket.ID, (payload, context) -> {
            context.client().execute(() -> context.client().getNetworkHandler().researcher$getClientTracker().handleUpdate(payload));
        });
    }
}
