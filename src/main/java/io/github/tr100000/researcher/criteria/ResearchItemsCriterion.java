package io.github.tr100000.researcher.criteria;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.tr100000.researcher.ResearcherCriteria;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.item.Item;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.util.dynamic.Codecs;

import java.util.List;
import java.util.Optional;

public class ResearchItemsCriterion extends AbstractCriterion<ResearchItemsCriterion.Conditions> {
    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public record Conditions(int time, List<Item> items) implements AbstractCriterion.Conditions {
        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        Codecs.POSITIVE_INT.fieldOf("time").forGetter(Conditions::time),
                        Registries.ITEM.getCodec().listOf(1, Integer.MAX_VALUE).fieldOf("items").forGetter(Conditions::items)
                ).apply(instance, Conditions::new)
        );

        public static AdvancementCriterion<Conditions> of(int time, Item... items) {
            return ResearcherCriteria.RESEARCH_ITEMS.create(new Conditions(time, List.of(items)));
        }

        @Override
        public Optional<LootContextPredicate> player() {
            return Optional.empty();
        }
    }
}
