package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.CriterionDisplay;
import io.github.tr100000.researcher.api.criterion.CriterionDisplayElement;
import io.github.tr100000.researcher.api.criterion.CriterionHandler;
import io.github.tr100000.researcher.api.criterion.element.TextElement;
import io.github.tr100000.researcher.api.criterion.util.EntityPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.ItemPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.NumberRangeUtils;
import io.github.tr100000.researcher.api.criterion.util.PredicateHelper;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.advancement.criterion.EnchantedItemCriterion;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.text.Text;

import java.util.Optional;

public class EnchantedItemCriterionHandler implements CriterionHandler<EnchantedItemCriterion.Conditions> {
    private static final Text PLAYER_CONDITIONS_HEADER = ModUtils.getScreenTranslated("predicate.player");
    private static final Text ITEM_CONDITIONS_HEADER = ModUtils.getScreenTranslated("criterion.enchanted_item.result_conditions");
    private static final Text LEVEL_TEXT = ModUtils.getScreenTranslated("criterion.enchanted_item.level");
    private static final Text TEXT = ModUtils.getScreenTranslated("criterion.enchanted_item");
    private static final Text TEXT_WITH_CONDITIONS = ModUtils.getScreenTranslated("criterion.enchanted_item_with_conditions");

    @Override
    public CriterionDisplayElement prepare(ResearchCriterion<EnchantedItemCriterion.Conditions> criterion) {
        Optional<LootContextPredicate> playerPredicate = criterion.conditions().player();
        Optional<ItemPredicate> itemPredicate = criterion.conditions().item();

        IndentedTextHolder textHolder = new IndentedTextHolder();
        PredicateHelper.tooltip(playerPredicate, EntityPredicateHelper::tooltip, PLAYER_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);
        PredicateHelper.tooltip(itemPredicate, ItemPredicateHelper::tooltip, ITEM_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);
        NumberRangeUtils.tooltip(criterion.conditions().levels(), LEVEL_TEXT, textHolder);

        CriterionDisplayElement element = new TextElement(textHolder.isEmpty() ? TEXT : TEXT_WITH_CONDITIONS);
        if (!textHolder.isEmpty()) element = element.withTextTooltip(textHolder.getText());

        return new CriterionDisplay(
                CriterionDisplay.getCountElement(criterion),
                element
        );
    }
}
