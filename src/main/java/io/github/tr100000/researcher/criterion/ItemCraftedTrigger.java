package io.github.tr100000.researcher.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.tr100000.researcher.ResearcherCriteriaTriggers;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Optional;

public class ItemCraftedTrigger extends SimpleCriterionTrigger<ItemCraftedTrigger.TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, ItemStack stack) {
        for (int i = 0; i < stack.getCount(); i++) {
            this.trigger(player, triggerInstance -> triggerInstance.matches(stack));
        }
    }

    public record TriggerInstance(Optional<Holder<LootItemCondition>> player, Optional<ItemPredicate> item) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        LootItemCondition.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                        ItemPredicate.CODEC.optionalFieldOf("item").forGetter(TriggerInstance::item)
                ).apply(instance, TriggerInstance::new)
        );

        public static Criterion<TriggerInstance> any() {
            return ResearcherCriteriaTriggers.ITEM_CRAFTED.createCriterion(new TriggerInstance(Optional.empty(), Optional.empty()));
        }

        public static Criterion<TriggerInstance> of(ItemPredicate.Builder item) {
            return ResearcherCriteriaTriggers.ITEM_CRAFTED.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(item.build())));
        }

        public boolean matches(ItemStack stack) {
            return this.item.map(itemPredicate -> itemPredicate.test(stack)).orElse(true);
        }
    }
}
