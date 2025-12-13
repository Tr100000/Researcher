package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.util.EntityPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.NumberRangeUtils;
import io.github.tr100000.researcher.api.criterion.util.PredicateHelper;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.advancement.criterion.TargetHitCriterion;
import net.minecraft.text.Text;

public class TargetHitCriterionHandler extends AbstractCriterionHandler<TargetHitCriterion.Conditions> {
    private static final Text SIGNAL_STRENGTH = ModUtils.getScreenTranslated("criterion.target_hit.signal_strength");
    private static final Text PROJECTILE_CONDITIONS_HEADER = ModUtils.getScreenTranslated("criterion.target_hit.projectile");

    public TargetHitCriterionHandler() {
        super(ModUtils.getScreenTranslationKey("criterion.target_hit"));
    }

    @Override
    protected void fillTooltip(ResearchCriterion<TargetHitCriterion.Conditions> criterion, IndentedTextHolder textHolder) {
        NumberRangeUtils.tooltip(criterion.conditions().signalStrength(), SIGNAL_STRENGTH, textHolder);
        PredicateHelper.optionalTooltip(criterion.conditions().projectile(), EntityPredicateHelper::tooltip, PROJECTILE_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);
        playerTooltip(criterion, textHolder);
    }
}
