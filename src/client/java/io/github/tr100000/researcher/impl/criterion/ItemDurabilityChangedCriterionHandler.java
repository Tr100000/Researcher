package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.util.ItemPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.NumberRangeUtils;
import io.github.tr100000.researcher.api.criterion.util.PredicateHelper;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.advancement.criterion.ItemDurabilityChangedCriterion;
import net.minecraft.text.Text;

public class ItemDurabilityChangedCriterionHandler extends AbstractCriterionHandler<ItemDurabilityChangedCriterion.Conditions> {
    private static final Text DURABILITY = ModUtils.getScreenTranslated("criterion.item_durability_changed.durability");
    private static final Text DELTA = ModUtils.getScreenTranslated("criterion.item_durability_changed.item");
    private static final Text ITEM_CONDITIONS_HEADER = ModUtils.getScreenTranslated("criterion.item_durability_changed.item");

    public ItemDurabilityChangedCriterionHandler() {
        super(ModUtils.getScreenTranslationKey("criterion.item_durability_changed"));
    }

    @Override
    protected void fillTooltip(ResearchCriterion<ItemDurabilityChangedCriterion.Conditions> criterion, IndentedTextHolder textHolder) {
        NumberRangeUtils.tooltip(criterion.conditions().durability(), DURABILITY, textHolder);
        NumberRangeUtils.tooltip(criterion.conditions().delta(), DELTA, textHolder);
        PredicateHelper.tooltip(criterion.conditions().item(), ItemPredicateHelper::tooltip, ITEM_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);
        playerTooltip(criterion, textHolder);
    }
}
