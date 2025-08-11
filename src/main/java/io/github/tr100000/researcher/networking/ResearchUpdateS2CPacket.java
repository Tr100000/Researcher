package io.github.tr100000.researcher.networking;

import io.github.tr100000.researcher.Research;
import io.github.tr100000.researcher.ResearchProgress;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public record ResearchUpdateS2CPacket(
        boolean clearCurrent,
        Map<Identifier, ResearchProgress> progressMap,
        Map<Identifier, Research> loadedResearch,
        Optional<Identifier> currentResearching,
        List<Identifier> pinnedResearches
) implements CustomPayload {
    public static final Id<ResearchUpdateS2CPacket> ID = ResearcherNetworking.payloadId("research_update");
    public static final PacketCodec<RegistryByteBuf, ResearchUpdateS2CPacket> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOLEAN, ResearchUpdateS2CPacket::clearCurrent,
            PacketCodecs.map(Object2ObjectOpenHashMap::new, Identifier.PACKET_CODEC, ResearchProgress.PACKET_CODEC), ResearchUpdateS2CPacket::progressMap,
            PacketCodecs.map(Object2ObjectOpenHashMap::new, Identifier.PACKET_CODEC, Research.PACKET_CODEC), ResearchUpdateS2CPacket::loadedResearch,
            PacketCodecs.optional(Identifier.PACKET_CODEC), ResearchUpdateS2CPacket::currentResearching,
            Identifier.PACKET_CODEC.collect(PacketCodecs.toList()), ResearchUpdateS2CPacket::pinnedResearches,
            ResearchUpdateS2CPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
