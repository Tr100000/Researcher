package io.github.tr100000.researcher.api.trigger.util;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.advancements.criterion.MovementPredicate;
import net.minecraft.network.chat.Component;

public final class MovementPredicateHelper {
    private MovementPredicateHelper() {}

    private static final Component MOVEMENT_ABSOLUTE = ModUtils.getScreenTranslated("predicate.movement.absolute");
    private static final Component MOVEMENT_HORIZONTAL = ModUtils.getScreenTranslated("predicate.movement.horizontal");
    private static final Component MOVEMENT_VERTICAL = ModUtils.getScreenTranslated("predicate.movement.vertical");
    private static final Component MOVEMENT_FALL_DISTANCE = ModUtils.getScreenTranslated("predicate.movement.fall_distance");
    private static final Component MOVEMENT_X = ModUtils.getScreenTranslated("predicate.movement.x");
    private static final Component MOVEMENT_Y = ModUtils.getScreenTranslated("predicate.movement.y");
    private static final Component MOVEMENT_Z = ModUtils.getScreenTranslated("predicate.movement.z");

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
