package io.github.tr100000.researcher.api.criterion.util;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.api.criterion.CriterionDisplayElement;
import io.github.tr100000.researcher.api.criterion.element.GroupedElement;
import io.github.tr100000.researcher.api.criterion.element.ItemElement;
import io.github.tr100000.researcher.api.criterion.element.TextElement;
import io.github.tr100000.researcher.api.criterion.element.TimedSwitchingElement;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.block.Block;
import net.minecraft.predicate.BlockPredicate;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class BlockPredicateHelper {
    private BlockPredicateHelper() {}

    private static final Text HEADER = ModUtils.getScreenTranslated("predicate.block");
    private static final Text ANY_BLOCK = ModUtils.getScreenTranslated("predicate.block.any");

    private static final Text BLOCK_LIST = ModUtils.getScreenTranslated("predicate.block.list");

    public static void tooltip(BlockPredicate predicate, IndentedTextHolder textHolder) {
        if (predicate.blocks().isPresent()) {
            textHolder.accept(BLOCK_LIST);
            textHolder.push();
            predicate.blocks().get().forEach(entry -> textHolder.accept(entry.value().getName()));
            textHolder.pop();
        }
        if (predicate.state().isPresent()) {
            textHolder.accept(PredicateHelper.STATE_HEADER);
            textHolder.push();
            PredicateHelper.stateTooltip(predicate.state().get(), textHolder);
            textHolder.pop();
        }
        if (predicate.nbt().isPresent()) {
            // TODO
            textHolder.accept(Text.literal("TODO nbt conditions"));
        }
        if (!predicate.components().isEmpty()) {
            // TODO
            textHolder.accept(Text.literal("TODO component conditions"));
        }
    }

    public static CriterionDisplayElement element(@Nullable BlockPredicate predicate) {
        if (predicate == null) return anyBlockElement();

        CriterionDisplayElement element;
        boolean shouldHaveTooltip = false;
        if (predicate.blocks().isPresent()) {
            if (predicate.blocks().get().size() > 1) shouldHaveTooltip = true;

            List<CriterionDisplayElement> list = predicate.blocks().get().stream().map(BlockPredicateHelper::element).toList();
            element = new TimedSwitchingElement(list);
        }
        else {
            element = new TextElement(ANY_BLOCK);
        }

        if (shouldHaveTooltip || predicate.state().isPresent() || predicate.nbt().isPresent() || !predicate.components().isEmpty()) {
            IndentedTextHolder textHolder = new IndentedTextHolder();
            textHolder.accept(HEADER);
            textHolder.push();
            tooltip(predicate, textHolder);
            textHolder.pop();

            if (textHolder.count() > 1) element = new GroupedElement(element.withTextTooltip(), new TextElement(Text.literal("*")));
        }

        return element;
    }

    public static CriterionDisplayElement element(@Nullable Block block) {
        if (block == null) return anyBlockElement();

        return new GroupedElement(
                new ItemElement(block.asItem().getDefaultStack(), false),
                new TextElement(block.getName())
        );
    }

    public static CriterionDisplayElement element(@Nullable RegistryEntry<Block> block) {
        if (block == null) return anyBlockElement();

        return element(block.value());
    }

    private static CriterionDisplayElement anyBlockElement() {
        return new TextElement(ANY_BLOCK);
    }
}
