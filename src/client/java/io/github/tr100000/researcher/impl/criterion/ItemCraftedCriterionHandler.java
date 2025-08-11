package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.criteria.ItemCraftedCriteria;
import net.minecraft.item.Item;

public class ItemCraftedCriterionHandler extends AbstractCraftCriterionHandler<ItemCraftedCriteria.Conditions> {
    public ItemCraftedCriterionHandler() {
        super("screen.researcher.criterion.item_crafted.before", "screen.researcher.criterion.item_crafted.after", "screen.researcher.criterion.item_crafted.count");
    }

    @Override
    public Item getItem(ResearchCriterion<ItemCraftedCriteria.Conditions> criterion) {
        return criterion.conditions().item();
    }
}
