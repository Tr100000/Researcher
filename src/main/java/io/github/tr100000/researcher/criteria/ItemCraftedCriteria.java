package io.github.tr100000.researcher.criteria;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.tr100000.researcher.ResearcherCriteria;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public class ItemCraftedCriteria extends AbstractCriterion<ItemCraftedCriteria.Conditions> {
    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player, Item item) {
        this.trigger(player, conditions -> conditions.matches(item));
    }

    public void trigger(ServerPlayerEntity player, ItemStack stack) {
        for (int i = 0; i < stack.getCount(); i++) {
            trigger(player, stack.getItem());
        }
    }

    public record Conditions(Item item) implements AbstractCriterion.Conditions {
        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        Registries.ITEM.getCodec().fieldOf("item").forGetter(Conditions::item)
                ).apply(instance, Conditions::new)
        );

        public static AdvancementCriterion<Conditions> of(Item item) {
            return ResearcherCriteria.ITEM_CRAFTED.create(new Conditions(item));
        }

        public boolean matches(Item item) {
            return this.item.equals(item);
        }

        @Override
        public Optional<LootContextPredicate> player() {
            return Optional.empty();
        }
    }
}
