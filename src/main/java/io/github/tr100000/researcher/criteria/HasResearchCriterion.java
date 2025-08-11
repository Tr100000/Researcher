package io.github.tr100000.researcher.criteria;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.tr100000.researcher.Researcher;
import io.github.tr100000.researcher.ResearcherCriteria;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class HasResearchCriterion extends AbstractCriterion<HasResearchCriterion.Conditions> {
    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player, Identifier id) {
        this.trigger(player, conditions -> conditions.matches(id));
    }

    public record Conditions(Identifier researchId) implements AbstractCriterion.Conditions {
        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        Identifier.CODEC.fieldOf("research").forGetter(Conditions::researchId)
                ).apply(instance, Conditions::new)
        );

        public static AdvancementCriterion<Conditions> of(Identifier researchId) {
            return ResearcherCriteria.HAS_RESEARCH.create(new Conditions(researchId));
        }

        public static AdvancementCriterion<Conditions> of(String researchId) {
            return of(Researcher.id(researchId));
        }

        public boolean matches(Identifier id) {
            return this.researchId.equals(id);
        }

        @Override
        public Optional<LootContextPredicate> player() {
            return Optional.empty();
        }
    }
}
