package io.github.tr100000.researcher.criteria;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.tr100000.researcher.ResearcherCriteria;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public class BlockBrokenCriterion extends AbstractCriterion<BlockBrokenCriterion.Conditions> {
    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player, BlockState brokenBlock) {
        this.trigger(player, conditions -> conditions.matches(brokenBlock));
    }

    public record Conditions(Optional<LootContextPredicate> player, Optional<RegistryEntry<Block>> block, Optional<StatePredicate> state) implements AbstractCriterion.Conditions {
        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(Conditions::player),
                        Registries.BLOCK.getEntryCodec().optionalFieldOf("block").forGetter(Conditions::block),
                        StatePredicate.CODEC.optionalFieldOf("state").forGetter(Conditions::state)
                ).apply(instance, Conditions::new)
        );

        @SuppressWarnings("deprecation")
        public static AdvancementCriterion<Conditions> of(Block block) {
            return ResearcherCriteria.BLOCK_BROKEN.create(new Conditions(Optional.empty(), Optional.of(block.getRegistryEntry()), Optional.empty()));
        }

        public static AdvancementCriterion<Conditions> of(StatePredicate.Builder state) {
            return ResearcherCriteria.BLOCK_BROKEN.create(new Conditions(Optional.empty(), Optional.empty(), state.build()));
        }

        @SuppressWarnings("deprecation")
        public static AdvancementCriterion<Conditions> of(Block block, StatePredicate.Builder state) {
            return ResearcherCriteria.BLOCK_BROKEN.create(new Conditions(Optional.empty(), Optional.of(block.getRegistryEntry()), state.build()));
        }

        public boolean matches(BlockState state) {
            return (this.block.isEmpty() || state.isOf(this.block.get())) && (this.state.isEmpty() || this.state.get().test(state));
        }
    }
}
