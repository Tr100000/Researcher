package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.util.EntityPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.PredicateHelper;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.advancement.criterion.EffectsChangedCriterion;
import net.minecraft.text.Text;

public class EffectsChangedCriterionHandler extends AbstractCriterionHandler<EffectsChangedCriterion.Conditions> {
    private static final Text SOURCE_CONDITIONS_HEADER = ModUtils.getScreenTranslated("criterion.effects_changed.source");

    public EffectsChangedCriterionHandler() {
        super(ModUtils.getScreenTranslationKey("criterion.effects_changed"));
    }

    @Override
    protected void fillTooltip(ResearchCriterion<EffectsChangedCriterion.Conditions> criterion, IndentedTextHolder textHolder) {
        PredicateHelper.optionalTooltip(criterion.conditions().effects(), EntityPredicateHelper::effectTooltip, null)
                .ifPresent(textHolder::accept);
        PredicateHelper.optionalTooltip(criterion.conditions().source(), EntityPredicateHelper::tooltip, SOURCE_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);
        playerTooltip(criterion, textHolder);
    }
}
