package io.github.tr100000.researcher;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.dynamic.Codecs;

public record ResearchCriterion<T extends CriterionConditions>(AdvancementCriterion<T> criterion, int count) {
    public static final Codec<ResearchCriterion<?>> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    AdvancementCriterion.CODEC.fieldOf("criterion").forGetter(ResearchCriterion::criterion),
                    Codecs.POSITIVE_INT.fieldOf("count").forGetter(ResearchCriterion::count)
            ).apply(instance, ResearchCriterion::new)
    );

    public static final PacketCodec<RegistryByteBuf, ResearchCriterion<?>> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.unlimitedRegistryCodec(AdvancementCriterion.CODEC), ResearchCriterion::criterion,
            PacketCodecs.VAR_INT, ResearchCriterion::count,
            ResearchCriterion::new
    );

    public static final ResearchCriterion<ImpossibleCriterion.Conditions> IMPOSSIBLE = new ResearchCriterion<>(Criteria.IMPOSSIBLE, new ImpossibleCriterion.Conditions(), Integer.MAX_VALUE);

    public ResearchCriterion(Criterion<T> trigger, T conditions, int count) {
        this(new AdvancementCriterion<>(trigger, conditions), count);
    }

    public T conditions() {
        return criterion().conditions();
    }

    public Criterion<T> trigger() {
        return criterion().trigger();
    }
}
