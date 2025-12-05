package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.criteria.ItemCraftedCriterion;
import net.minecraft.item.Item;

public class ItemCraftedCriterionHandler extends AbstractItemCriterionHandler<ItemCraftedCriterion.Conditions> {
    public ItemCraftedCriterionHandler() {
        super("screen.researcher.criterion.item_crafted.before", "screen.researcher.criterion.item_crafted.after");
    }

    @Override
    public Item getItem(ResearchCriterion<ItemCraftedCriterion.Conditions> criterion) {
        return criterion.conditions().item();
    }
}
