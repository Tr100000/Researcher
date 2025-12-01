package io.github.tr100000.researcher.api.criterion.util;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.predicate.entity.MovementPredicate;
import net.minecraft.text.Text;

public final class MovementPredicateHelper {
    private MovementPredicateHelper() {}

    private static final Text MOVEMENT_ABSOLUTE = ModUtils.getScreenTranslated("predicate.movement.absolute");
    private static final Text MOVEMENT_HORIZONTAL = ModUtils.getScreenTranslated("predicate.movement.horizontal");
    private static final Text MOVEMENT_VERTICAL = ModUtils.getScreenTranslated("predicate.movement.vertical");
    private static final Text MOVEMENT_FALL_DISTANCE = ModUtils.getScreenTranslated("predicate.movement.fallDistance");
    private static final Text MOVEMENT_X = ModUtils.getScreenTranslated("predicate.movement.x");
    private static final Text MOVEMENT_Y = ModUtils.getScreenTranslated("predicate.movement.y");
    private static final Text MOVEMENT_Z = ModUtils.getScreenTranslated("predicate.movement.z");

    public static void tooltip(MovementPredicate predicate, IndentedTextHolder textHolder) {
        NumberRangeUtils.tooltip(predicate.speed(), MOVEMENT_ABSOLUTE, textHolder);
        NumberRangeUtils.tooltip(predicate.horizontalSpeed(), MOVEMENT_HORIZONTAL, textHolder);
        NumberRangeUtils.tooltip(predicate.verticalSpeed(), MOVEMENT_VERTICAL, textHolder);
        NumberRangeUtils.tooltip(predicate.fallDistance(), MOVEMENT_FALL_DISTANCE, textHolder);
        NumberRangeUtils.tooltip(predicate.x(), MOVEMENT_X, textHolder);
        NumberRangeUtils.tooltip(predicate.y(), MOVEMENT_Y, textHolder);
        NumberRangeUtils.tooltip(predicate.z(), MOVEMENT_Z, textHolder);
    }
}
