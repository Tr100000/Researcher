package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.util.ItemPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.NumberRangeUtils;
import io.github.tr100000.researcher.api.criterion.util.PredicateHelper;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.advancement.criterion.EnchantedItemCriterion;
import net.minecraft.text.Text;

public class EnchantedItemCriterionHandler extends AbstractCriterionHandler<EnchantedItemCriterion.Conditions> {
    private static final Text ITEM_CONDITIONS_HEADER = ModUtils.getScreenTranslated("criterion.enchanted_item.result_conditions");
    private static final Text LEVEL_TEXT = ModUtils.getScreenTranslated("criterion.enchanted_item.level");

    public EnchantedItemCriterionHandler() {
        super(ModUtils.getScreenTranslationKey("criterion.enchanted_item"));
    }

    @Override
    protected void fillTooltip(ResearchCriterion<EnchantedItemCriterion.Conditions> criterion, IndentedTextHolder textHolder) {
        PredicateHelper.tooltip(criterion.conditions().item(), ItemPredicateHelper::tooltip, ITEM_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);
        NumberRangeUtils.tooltip(criterion.conditions().levels(), LEVEL_TEXT, textHolder);
        playerTooltip(criterion, textHolder);
    }
}
