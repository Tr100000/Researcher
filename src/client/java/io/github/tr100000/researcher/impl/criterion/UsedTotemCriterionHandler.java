package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.util.ItemPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.PredicateHelper;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.advancement.criterion.UsedTotemCriterion;

public class UsedTotemCriterionHandler extends AbstractCriterionHandler<UsedTotemCriterion.Conditions> {
    public UsedTotemCriterionHandler() {
        super(ModUtils.getScreenTranslationKey("criterion.used_totem"));
    }

    @Override
    protected void fillTooltip(ResearchCriterion<UsedTotemCriterion.Conditions> criterion, IndentedTextHolder textHolder) {
        PredicateHelper.tooltip(criterion.conditions().item(), ItemPredicateHelper::tooltip, null)
                .ifPresent(textHolder::accept);
        playerTooltip(criterion, textHolder);
    }
}
