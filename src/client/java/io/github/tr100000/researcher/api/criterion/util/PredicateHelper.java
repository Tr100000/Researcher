package io.github.tr100000.researcher.api.criterion.util;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.predicate.NbtPredicate;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.predicate.TagPredicate;
import net.minecraft.text.Text;

import java.util.Optional;
import java.util.function.Supplier;

public final class PredicateHelper {
    private PredicateHelper() {}

    private static final String TAG_EXPECTED = ModUtils.getScreenTranslationKey("predicate.tag.expected");
    private static final String TAG_NOT_EXPECTED = ModUtils.getScreenTranslationKey("predicate.tag.not_expected");

    private static final String NBT_KEY = ModUtils.getScreenTranslationKey("predicate.nbt");

    public static final Text STATE_HEADER = ModUtils.getScreenTranslated("predicate.state");
    private static final String STATE_VALUE_EXACTLY_KEY = ModUtils.getScreenTranslationKey("predicate.state.exactly");
    private static final String STATE_VALUE_BETWEEN_KEY = ModUtils.getScreenTranslationKey("predicate.state.between");
    private static final String STATE_VALUE_LESS_THAN_KEY = ModUtils.getScreenTranslationKey("predicate.state.less_than");
    private static final String STATE_VALUE_GREATER_THAN_KEY = ModUtils.getScreenTranslationKey("predicate.state.greater_than");

    public static void optionalBooleanTooltip(Optional<Boolean> optional, Text trueText, Text falseText, IndentedTextHolder textHolder) {
        optional.ifPresent(value -> textHolder.accept(value ? trueText : falseText));
    }

    public static void optionalBooleanTooltip(Optional<Boolean> optional, Supplier<Text> trueText, Supplier<Text> falseText, IndentedTextHolder textHolder) {
        optional.ifPresent(value -> textHolder.accept(value ? trueText.get() : falseText.get()));
    }

    public static <T> void tagTooltip(TagPredicate<T> predicate, IndentedTextHolder textHolder) {
        textHolder.accept(Text.translatable(predicate.expected() ? TAG_EXPECTED : TAG_NOT_EXPECTED, predicate.tag().id().toString()));
    }

    public static void nbtTooltip(NbtPredicate predicate, IndentedTextHolder textHolder) {
        textHolder.accept(Text.translatable(NBT_KEY, predicate.nbt().toString()));
    }

    public static void stateTooltip(StatePredicate predicate, IndentedTextHolder textHolder) {
        predicate.conditions().forEach(condition -> {
            if (condition.valueMatcher() instanceof StatePredicate.ExactValueMatcher(String value)) {
                textHolder.accept(Text.translatable(STATE_VALUE_EXACTLY_KEY, condition.key(), value));
            }
            else if (condition.valueMatcher() instanceof StatePredicate.RangedValueMatcher(Optional<String> min, Optional<String> max)) {
                if (min.isPresent() && max.isPresent()) {
                    textHolder.accept(Text.translatable(STATE_VALUE_BETWEEN_KEY, condition.key(), min.get(), max.get()));
                }
                else if (!min.isPresent() && max.isPresent()) {
                    textHolder.accept(Text.translatable(STATE_VALUE_LESS_THAN_KEY, condition.key(), max.get()));
                }
                else if (min.isPresent() && !max.isPresent()) {
                    textHolder.accept(Text.translatable(STATE_VALUE_GREATER_THAN_KEY, condition.key(), min.get()));
                }
            }
        });
    }
}
