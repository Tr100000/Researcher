package io.github.tr100000.researcher.api.criterion.util;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.predicate.NbtPredicate;
import net.minecraft.predicate.TagPredicate;
import net.minecraft.text.Text;

import java.util.Optional;
import java.util.function.Supplier;

public final class PredicateHelper {
    private PredicateHelper() {}

    private static final String TAG_EXPECTED = ModUtils.getScreenTranslationKey("predicate.tag.expected");
    private static final String TAG_NOT_EXPECTED = ModUtils.getScreenTranslationKey("predicate.tag.not_expected");

    private static final String NBT_KEY = ModUtils.getScreenTranslationKey("predicate.nbt");

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
}
