package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.util.ItemPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.NumberRangeUtils;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.text.Text;

public class InventoryChangedCriterionHandler extends AbstractCriterionHandler<InventoryChangedCriterion.Conditions> {
    private static final Text ITEM_LIST = ModUtils.getScreenTranslated("criterion.inventory_changed.items");
    private static final Text INVENTORY_OCCUPIED_SLOTS = ModUtils.getScreenTranslated("criterion.inventory_changed.occupied_slots");
    private static final Text INVENTORY_FULL_SLOTS = ModUtils.getScreenTranslated("criterion.inventory_changed.full_slots");
    private static final Text INVENTORY_EMPTY_SLOTS = ModUtils.getScreenTranslated("criterion.inventory_changed.empty_slots");

    public InventoryChangedCriterionHandler() {
        super(ModUtils.getScreenTranslationKey("criterion.inventory_changed"));
    }

    @Override
    protected void fillTooltip(ResearchCriterion<InventoryChangedCriterion.Conditions> criterion, IndentedTextHolder textHolder) {
        if (!criterion.conditions().items().isEmpty()) {
            textHolder.accept(ITEM_LIST);
            textHolder.push();
            criterion.conditions().items().forEach(itemPredicate -> ItemPredicateHelper.tooltip(itemPredicate, textHolder));
            textHolder.pop();
        }

        playerTooltip(criterion, textHolder);

        if (!criterion.conditions().items().isEmpty()) {
            slotConditionsTooltip(criterion.conditions().slots(), textHolder);
        }
    }

    private void slotConditionsTooltip(InventoryChangedCriterion.Conditions.Slots slots, IndentedTextHolder textHolder) {
        NumberRangeUtils.tooltip(slots.occupied(), INVENTORY_OCCUPIED_SLOTS, textHolder);
        NumberRangeUtils.tooltip(slots.full(), INVENTORY_FULL_SLOTS, textHolder);
        NumberRangeUtils.tooltip(slots.empty(), INVENTORY_EMPTY_SLOTS, textHolder);
    }
}
