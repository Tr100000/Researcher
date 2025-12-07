package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.util.DistancePredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.NumberRangeUtils;
import io.github.tr100000.researcher.api.criterion.util.PredicateHelper;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.advancement.criterion.LevitationCriterion;
import net.minecraft.text.Text;

public class LevitationCriterionHandler extends AbstractCriterionHandler<LevitationCriterion.Conditions> {
    public static final Text DISTANCE = ModUtils.getScreenTranslated("criterion.levitation.distance");
    public static final Text DURATION = ModUtils.getScreenTranslated("criterion.levitation.duration");

    public LevitationCriterionHandler() {
        super(ModUtils.getScreenTranslationKey("criterion.levitation"));
    }

    @Override
    protected void fillTooltip(ResearchCriterion<LevitationCriterion.Conditions> criterion, IndentedTextHolder textHolder) {
        PredicateHelper.tooltip(criterion.conditions().distance(), DistancePredicateHelper::tooltip, DISTANCE)
                .ifPresent(textHolder::accept);
        NumberRangeUtils.tooltip(criterion.conditions().duration(), DURATION, textHolder);
        playerTooltip(criterion, textHolder);
    }
}
