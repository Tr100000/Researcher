package io.github.tr100000.researcher.networking;

import io.github.tr100000.trutils.api.utils.PacketCodecUtils;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.Optional;

public record StartResearchC2SPacket(Mode mode, Optional<Identifier> researchId) implements CustomPayload {
    public static final Id<StartResearchC2SPacket> ID = ResearcherNetworking.payloadId("start_research");
    public static final PacketCodec<RegistryByteBuf, StartResearchC2SPacket> CODEC = PacketCodec.tuple(
            PacketCodecUtils.ofEnum(Mode.class), StartResearchC2SPacket::mode,
            PacketCodecs.optional(Identifier.PACKET_CODEC), StartResearchC2SPacket::researchId,
            StartResearchC2SPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public enum Mode {
        SET_CURRENT,
        PIN,
        UNPIN,
        GRANT,
        REVOKE,
    }
}
