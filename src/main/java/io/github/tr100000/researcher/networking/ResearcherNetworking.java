package io.github.tr100000.researcher.networking;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.PlayerResearchTracker;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public class ResearcherNetworking {
    protected ResearcherNetworking() {}

    public static void registerPayloads() {
        PayloadTypeRegistry.playS2C().registerLarge(ResearchUpdateS2CPacket.ID, ResearchUpdateS2CPacket.CODEC, Integer.MAX_VALUE);
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
        return new CustomPayload.Id<>(ModUtils.id(path));
    }
}
