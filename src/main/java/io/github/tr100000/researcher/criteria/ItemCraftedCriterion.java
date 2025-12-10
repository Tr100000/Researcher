package io.github.tr100000.researcher.criteria;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.tr100000.researcher.ResearcherCriteria;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public class ItemCraftedCriterion extends AbstractCriterion<ItemCraftedCriterion.Conditions> {
    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player, ItemStack stack) {
        for (int i = 0; i < stack.getCount(); i++) {
            this.trigger(player, conditions -> conditions.matches(stack));
        }
    }

    public record Conditions(Optional<LootContextPredicate> player, Optional<ItemPredicate> item) implements AbstractCriterion.Conditions {
        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(Conditions::player),
                        ItemPredicate.CODEC.optionalFieldOf("item").forGetter(Conditions::item)
                ).apply(instance, Conditions::new)
        );

        public static AdvancementCriterion<Conditions> any() {
            return ResearcherCriteria.ITEM_CRAFTED.create(new Conditions(Optional.empty(), Optional.empty()));
        }

        public static AdvancementCriterion<Conditions> of(ItemPredicate.Builder item) {
            return ResearcherCriteria.ITEM_CRAFTED.create(new Conditions(Optional.empty(), Optional.of(item.build())));
        }

        public boolean matches(ItemStack stack) {
            return this.item.map(itemPredicate -> itemPredicate.test(stack)).orElse(true);
        }
    }
}
