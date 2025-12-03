package io.github.tr100000.researcher.api.criterion.util;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.text.Text;

public final class ItemPredicateHelper {
    private ItemPredicateHelper() {}

    private static final Text ITEM_LIST = ModUtils.getScreenTranslated("predicate.item.list");
    private static final Text ITEM_COUNT = ModUtils.getScreenTranslated("predicate.item.count");

    public static void tooltip(ItemPredicate predicate, IndentedTextHolder textHolder) {
        if (predicate.items().isPresent()) {
            textHolder.accept(ITEM_LIST);
            textHolder.push();
            predicate.items().get().forEach(entry -> textHolder.accept(entry.value().getName()));
            textHolder.pop();
        }
        NumberRangeUtils.tooltip(predicate.count(), ITEM_COUNT, textHolder);
        if (!predicate.components().isEmpty()) {
            // TODO
            textHolder.accept(Text.literal("TODO component conditions"));
        }
    }
}
