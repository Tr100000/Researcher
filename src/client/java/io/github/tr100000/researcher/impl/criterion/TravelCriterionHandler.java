package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.util.DistancePredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.LocationPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.PredicateHelper;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.advancement.criterion.TravelCriterion;
import net.minecraft.text.Text;

public class TravelCriterionHandler extends AbstractCriterionHandler<TravelCriterion.Conditions> {
    public static final TravelCriterionHandler NETHER_TRAVEL = new TravelCriterionHandler(ModUtils.getScreenTranslationKey("criterion.nether_travel"));
    public static final TravelCriterionHandler FALL_FROM_HEIGHT = new TravelCriterionHandler(ModUtils.getScreenTranslationKey("criterion.fall_from_height"));
    public static final TravelCriterionHandler RIDE_ENTITY_IN_LAVA = new TravelCriterionHandler(ModUtils.getScreenTranslationKey("criterion.ride_entity_in_lava"));

    private static final Text START_CONDITIONS_HEADER = ModUtils.getScreenTranslated("criterion.travel.start");
    private static final Text DISTANCE_CONDITIONS_HEADER = ModUtils.getScreenTranslated("criterion.travel.distance");

    public TravelCriterionHandler(String textKey) {
        super(textKey);
    }

    @Override
    protected void fillTooltip(ResearchCriterion<TravelCriterion.Conditions> criterion, IndentedTextHolder textHolder) {
        PredicateHelper.optionalTooltip(criterion.conditions().startPosition(), LocationPredicateHelper::tooltip, START_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);
        PredicateHelper.optionalTooltip(criterion.conditions().distance(), DistancePredicateHelper::tooltip, DISTANCE_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);
        playerTooltip(criterion, textHolder);
    }
}
