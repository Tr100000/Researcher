package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.util.DistancePredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.EntityPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.LocationPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.PredicateHelper;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.advancement.criterion.FallAfterExplosionCriterion;
import net.minecraft.text.Text;

public class FallAfterExplosionCriterionHandler extends AbstractCriterionHandler<FallAfterExplosionCriterion.Conditions> {
    private static final Text START_CONDITIONS_HEADER = ModUtils.getScreenTranslated("criterion.fall_after_explosion.start");
    private static final Text DISTANCE_CONDITIONS_HEADER = ModUtils.getScreenTranslated("criterion.fall_after_explosion.distance");
    private static final Text CAUSE_CONDITIONS_HEADER = ModUtils.getScreenTranslated("criterion.fall_after_explosion.cause");

    public FallAfterExplosionCriterionHandler() {
        super(ModUtils.getScreenTranslationKey("criterion.fall_after_explosion"));
    }

    @Override
    protected void fillTooltip(ResearchCriterion<FallAfterExplosionCriterion.Conditions> criterion, IndentedTextHolder textHolder) {
        PredicateHelper.optionalTooltip(criterion.conditions().startPosition(), LocationPredicateHelper::tooltip, START_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);
        PredicateHelper.optionalTooltip(criterion.conditions().distance(), DistancePredicateHelper::tooltip, DISTANCE_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);
        PredicateHelper.optionalTooltip(criterion.conditions().cause(), EntityPredicateHelper::tooltip, CAUSE_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);
        playerTooltip(criterion, textHolder);
    }
}
