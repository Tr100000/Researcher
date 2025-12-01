package io.github.tr100000.researcher.api.criterion.util;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.predicate.NumberRange;
import net.minecraft.text.Text;

import java.util.Optional;

public final class NumberRangeUtils {
    private NumberRangeUtils() {}

    private static final String RANGE_EXACTLY_KEY = ModUtils.getScreenTranslationKey("predicate.range.exactly");
    private static final String RANGE_BETWEEN_KEY = ModUtils.getScreenTranslationKey("predicate.range.between");
    private static final String RANGE_LESS_THAN_KEY = ModUtils.getScreenTranslationKey("predicate.range.less_than");
    private static final String RANGE_GREATER_THAN_KEY = ModUtils.getScreenTranslationKey("predicate.range.greater_than");

    public static <T extends Number & Comparable<T>> Optional<Text> getText(NumberRange<T> range) {
        if (range.getMax().isPresent() && range.getMin().isPresent()) {
            // Both max and min values are present (either range or exact)
            if (range.getMax().get().equals(range.getMin().get())) {
                return Optional.of(Text.translatable(RANGE_EXACTLY_KEY, range.getMin().get()));
            }
            else {
                return Optional.of(Text.translatable(RANGE_BETWEEN_KEY, range.getMax().get(), range.getMin().get()));
            }
        }
        else if (range.getMax().isPresent() && range.getMin().isEmpty()) {
            // Only max value is present
            return Optional.of(Text.translatable(RANGE_LESS_THAN_KEY, range.getMax().get()));
        }
        else if (range.getMax().isEmpty() && range.getMin().isPresent()) {
            // Only min value is present
            return Optional.of(Text.translatable(RANGE_GREATER_THAN_KEY, range.getMin().get()));
        }
        else {
            // No values are present
            return Optional.empty();
        }
    }

    public static <T extends Number & Comparable<T>> void tooltip(NumberRange<T> range, Text label, IndentedTextHolder textHolder) {
        getText(range).ifPresent(t -> textHolder.accept(label.copy().append(t)));
    }
}
