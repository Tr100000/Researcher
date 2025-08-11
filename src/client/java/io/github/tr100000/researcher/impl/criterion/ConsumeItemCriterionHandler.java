package io.github.tr100000.researcher.impl.criterion;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.criterion.ConsumeItemCriterion;
import net.minecraft.item.Item;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.entry.RegistryEntryList;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public class ConsumeItemCriterionHandler extends AbstractConsumeItemCriterionHandler<ConsumeItemCriterion.Conditions> {
    @Override
    public Optional<Item> getItem(ConsumeItemCriterion.Conditions conditions) {
        Optional<ItemPredicate> predicate = conditions.item();
        if (predicate.isPresent()) {
            Optional<RegistryEntryList<Item>> entryList = predicate.get().items();
            if (entryList.isPresent() && entryList.get().size() > 0) {
                return Optional.ofNullable(entryList.get().get(0).value());
            }
        }
        return Optional.empty();
    }
}
