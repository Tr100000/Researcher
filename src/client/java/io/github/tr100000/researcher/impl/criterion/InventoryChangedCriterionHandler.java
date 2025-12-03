package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.CriterionDisplay;
import io.github.tr100000.researcher.api.criterion.CriterionDisplayElement;
import io.github.tr100000.researcher.api.criterion.CriterionHandler;
import io.github.tr100000.researcher.api.criterion.element.TextElement;
import io.github.tr100000.researcher.api.criterion.util.EntityPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.ItemPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.NumberRangeUtils;
import io.github.tr100000.researcher.api.criterion.util.PredicateHelper;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.text.Text;

public class InventoryChangedCriterionHandler implements CriterionHandler<InventoryChangedCriterion.Conditions> {
    private static final Text TEXT = ModUtils.getScreenTranslated("criterion.inventory_changed");
    private static final Text TEXT_WITH_CONDITIONS = ModUtils.getScreenTranslated("criterion.inventory_changed_with_conditions");
    private static final Text ITEM_LIST = ModUtils.getScreenTranslated("criterion.inventory_changed.items");
    private static final Text INVENTORY_OCCUPIED_SLOTS = ModUtils.getScreenTranslated("criterion.inventory_changed.occupied_slots");
    private static final Text INVENTORY_FULL_SLOTS = ModUtils.getScreenTranslated("criterion.inventory_changed.full_slots");
    private static final Text INVENTORY_EMPTY_SLOTS = ModUtils.getScreenTranslated("criterion.inventory_changed.empty_slots");
    private static final Text PLAYER_CONDITIONS_HEADER = ModUtils.getScreenTranslated("predicate.player");

    @Override
    public CriterionDisplayElement prepare(ResearchCriterion<InventoryChangedCriterion.Conditions> criterion) {
        IndentedTextHolder textHolder = new IndentedTextHolder();

        if (!criterion.conditions().items().isEmpty()) {
            textHolder.accept(ITEM_LIST);
            textHolder.push();
            criterion.conditions().items().forEach(itemPredicate -> ItemPredicateHelper.tooltip(itemPredicate, textHolder));
            textHolder.pop();
        }

        PredicateHelper.tooltip(criterion.conditions().player(), EntityPredicateHelper::tooltip, PLAYER_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);

        if (!criterion.conditions().items().isEmpty()) {
            slotConditionsTooltip(criterion.conditions().slots(), textHolder);
        }

        boolean hasConditions = !textHolder.isEmpty();
        CriterionDisplayElement element = new TextElement(hasConditions ? TEXT_WITH_CONDITIONS : TEXT);
        if (hasConditions) element = element.withTextTooltip(textHolder.getText());

        return new CriterionDisplay(
                new TextElement(Text.literal(criterion.count() + "x")),
                element
        );
    }

    private static void slotConditionsTooltip(InventoryChangedCriterion.Conditions.Slots slots, IndentedTextHolder textHolder) {
        NumberRangeUtils.tooltip(slots.occupied(), INVENTORY_OCCUPIED_SLOTS, textHolder);
        NumberRangeUtils.tooltip(slots.full(), INVENTORY_FULL_SLOTS, textHolder);
        NumberRangeUtils.tooltip(slots.empty(), INVENTORY_EMPTY_SLOTS, textHolder);
    }
}
