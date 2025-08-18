package io.github.tr100000.researcher.criteria;

import com.mojang.serialization.Codec;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.block.Block;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public class BlockBrokenCriterion extends AbstractCriterion<BlockBrokenCriterion.Conditions> {
    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player, Block brokenBlock) {
        this.trigger(player, conditions -> conditions.matches(brokenBlock));
    }

    public record Conditions(Block block) implements AbstractCriterion.Conditions {
        public static final Codec<Conditions> CODEC = Registries.BLOCK.getCodec().xmap(Conditions::new, Conditions::block);

        public boolean matches(Block brokenBlock) {
            return brokenBlock == block();
        }

        @Override
        public Optional<LootContextPredicate> player() {
            return Optional.empty();
        }
    }
}
