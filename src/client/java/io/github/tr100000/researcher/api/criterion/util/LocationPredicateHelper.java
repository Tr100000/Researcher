package io.github.tr100000.researcher.api.criterion.util;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.text.Text;

public final class LocationPredicateHelper {
    private LocationPredicateHelper() {}

    private static final Text LOCATION_POSITION = ModUtils.getScreenTranslated("predicate.location.position");
    private static final Text LOCATION_POSITION_X = ModUtils.getScreenTranslated("predicate.location.position.x");
    private static final Text LOCATION_POSITION_Y = ModUtils.getScreenTranslated("predicate.location.position.y");
    private static final Text LOCATION_POSITION_Z = ModUtils.getScreenTranslated("predicate.location.position.z");
    private static final Text LOCATION_BIOMES = ModUtils.getScreenTranslated("predicate.location.biome");
    private static final Text LOCATION_STRUCTURES = ModUtils.getScreenTranslated("predicate.location.structure");
    private static final String LOCATION_DIMENSION_KEY = ModUtils.getScreenTranslationKey("predicate.location.dimension");
    private static final Text LOCATION_SMOKEY = ModUtils.getScreenTranslated("predicate.location.smokey");
    private static final Text LOCATION_NOT_SMOKEY = ModUtils.getScreenTranslated("predicate.location.not_smokey");
    private static final Text LOCATION_LIGHT = ModUtils.getScreenTranslated("predicate.location.light");
    private static final Text LOCATION_BLOCK = ModUtils.getScreenTranslated("predicate.location.block");
    private static final Text LOCATION_FLUID = ModUtils.getScreenTranslated("predicate.location.fluid");
    private static final Text LOCATION_SKY_VISIBLE = ModUtils.getScreenTranslated("predicate.location.sky_visible");
    private static final Text LOCATION_SKY_NOT_VISIBLE = ModUtils.getScreenTranslated("predicate.location.sky_not_visible");

    public static void tooltip(LocationPredicate predicate, IndentedTextHolder textHolder) {
        if (predicate.position().isPresent()) {
            textHolder.accept(LOCATION_POSITION);
            textHolder.push();

            NumberRange.DoubleRange xRange = predicate.position().get().x();
            NumberRange.DoubleRange yRange = predicate.position().get().y();
            NumberRange.DoubleRange zRange = predicate.position().get().z();

            NumberRangeUtils.tooltip(xRange, LOCATION_POSITION_X, textHolder);
            NumberRangeUtils.tooltip(yRange, LOCATION_POSITION_Y, textHolder);
            NumberRangeUtils.tooltip(zRange, LOCATION_POSITION_Z, textHolder);

            textHolder.pop();
        }
        if (predicate.biomes().isPresent()) {
            textHolder.accept(LOCATION_BIOMES);
            textHolder.push();
            predicate.biomes().get().forEach(entry -> textHolder.accept(Text.translatable(entry.getKey().orElseThrow().getValue().toTranslationKey("biome"))));
            textHolder.pop();
        }
        if (predicate.structures().isPresent()) {
            textHolder.accept(LOCATION_STRUCTURES);
            textHolder.push();
            predicate.structures().get().forEach(entry -> textHolder.accept(Text.literal(entry.getKey().orElseThrow().getValue().toString())));
            textHolder.pop();
        }
        if (predicate.dimension().isPresent()) {
            textHolder.accept(Text.translatable(LOCATION_DIMENSION_KEY, predicate.dimension().get().getValue().toString()));
        }
        PredicateHelper.optionalBooleanTooltip(predicate.smokey(), LOCATION_SMOKEY, LOCATION_NOT_SMOKEY, textHolder);
        if (predicate.light().isPresent()) {
            NumberRangeUtils.tooltip(predicate.light().get().range(), LOCATION_LIGHT, textHolder);
        }
        if (predicate.block().isPresent()) {
            textHolder.accept(LOCATION_BLOCK);
            textHolder.push();
            BlockPredicateHelper.tooltip(predicate.block().get(), textHolder);
            textHolder.pop();
        }
        if (predicate.fluid().isPresent()) {
            textHolder.accept(LOCATION_FLUID);
            textHolder.push();
            FluidPredicateHelper.tooltip(predicate.fluid().get(), textHolder);
            textHolder.pop();
        }
        PredicateHelper.optionalBooleanTooltip(predicate.canSeeSky(), LOCATION_SKY_VISIBLE, LOCATION_SKY_NOT_VISIBLE, textHolder);
    }
}
