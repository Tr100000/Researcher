package io.github.tr100000.researcher.api.criterion.util;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.api.criterion.CriterionDisplayElement;
import io.github.tr100000.researcher.api.criterion.element.GroupedElement;
import io.github.tr100000.researcher.api.criterion.element.ItemElement;
import io.github.tr100000.researcher.api.criterion.element.TextElement;
import io.github.tr100000.researcher.api.criterion.element.TimedSwitchingElement;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.item.Item;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Optional;

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

    public static Optional<CriterionDisplayElement> element(ItemPredicate predicate) {
        if (predicate.items().isPresent() && predicate.items().get().size() > 0) {
            List<CriterionDisplayElement> subElements = predicate.items().get()
                    .stream()
                    .map(ItemPredicateHelper::entryElement)
                    .toList();

            return Optional.of(new TimedSwitchingElement(subElements));
        }

        return Optional.empty();
    }

    public static CriterionDisplayElement entryElement(RegistryEntry<Item> item) {
        return new GroupedElement(
                new ItemElement(item.value().getDefaultStack(), false),
                new TextElement(item.value().getName())
        );
    }
}
