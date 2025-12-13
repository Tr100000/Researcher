package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.util.PredicateHelper;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.advancement.criterion.AnyBlockUseCriterion;

public class AnyBlockUseCriterionHandler extends AbstractCriterionHandler<AnyBlockUseCriterion.Conditions> {
    public AnyBlockUseCriterionHandler() {
        super(ModUtils.getScreenTranslationKey("criterion.any_block_use"));
    }

    @Override
    protected void fillTooltip(ResearchCriterion<AnyBlockUseCriterion.Conditions> criterion, IndentedTextHolder textHolder) {
        PredicateHelper.optionalTooltip(criterion.conditions().location(), ItemCriterionHandler::locationTooltip, null)
                .ifPresent(textHolder::accept);
    }
}
