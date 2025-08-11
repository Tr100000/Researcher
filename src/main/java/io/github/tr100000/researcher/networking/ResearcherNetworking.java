package io.github.tr100000.researcher.networking;

import io.github.tr100000.researcher.PlayerResearchTracker;
import io.github.tr100000.researcher.Researcher;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public class ResearcherNetworking {
    protected ResearcherNetworking() {}

    public static void registerPayloads() {
        PayloadTypeRegistry.playS2C().register(ResearchUpdateS2CPacket.ID, ResearchUpdateS2CPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(StartResearchC2SPacket.ID, StartResearchC2SPacket.CODEC);
    }

    public static void registerServerRecievers() {
        ServerPlayNetworking.registerGlobalReceiver(StartResearchC2SPacket.ID, (payload, context) -> {
            context.server().execute(() -> {
                PlayerResearchTracker researchTracker = context.player().researcher$getPlayerTracker();
                researchTracker.handleUpdate(payload);
            });
        });
    }

    public static <T extends CustomPayload> CustomPayload.Id<T> payloadId(String path) {
        return new CustomPayload.Id<>(Researcher.id(path));
    }
}
