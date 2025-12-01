package io.github.tr100000.researcher.api.criterion.util;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.predicate.entity.DistancePredicate;
import net.minecraft.text.Text;

public final class DistancePredicateHelper {
    private DistancePredicateHelper() {}

    private static final Text DISTANCE_ABSOLUTE_KEY = ModUtils.getScreenTranslated("predicate.distance.absolute");
    private static final Text DISTANCE_HORIZONTAL_KEY = ModUtils.getScreenTranslated("predicate.distance.horizontal");
    private static final Text DISTANCE_X_KEY = ModUtils.getScreenTranslated("predicate.distance.x");
    private static final Text DISTANCE_Y_KEY = ModUtils.getScreenTranslated("predicate.distance.y");
    private static final Text DISTANCE_Z_KEY = ModUtils.getScreenTranslated("predicate.distance.z");

    public static void tooltip(DistancePredicate predicate, IndentedTextHolder textHolder) {
        NumberRangeUtils.tooltip(predicate.absolute(), DISTANCE_ABSOLUTE_KEY, textHolder);
        NumberRangeUtils.tooltip(predicate.horizontal(), DISTANCE_HORIZONTAL_KEY, textHolder);
        NumberRangeUtils.tooltip(predicate.x(), DISTANCE_X_KEY, textHolder);
        NumberRangeUtils.tooltip(predicate.y(), DISTANCE_Y_KEY, textHolder);
        NumberRangeUtils.tooltip(predicate.z(), DISTANCE_Z_KEY, textHolder);
    }
}
