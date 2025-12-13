package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.CriterionDisplay;
import io.github.tr100000.researcher.api.criterion.CriterionDisplayElement;
import io.github.tr100000.researcher.api.criterion.CriterionHandler;
import io.github.tr100000.researcher.api.criterion.element.TextElement;
import io.github.tr100000.researcher.api.criterion.util.EntityPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.ItemPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.PredicateHelper;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import io.github.tr100000.researcher.criteria.ItemCraftedCriterion;
import net.minecraft.text.Text;

public class ItemCraftedCriterionHandler implements CriterionHandler<ItemCraftedCriterion.Conditions> {
    private static final Text ITEM_CONDITIONS_HEADER = ModUtils.getScreenTranslated("predicate.item");
    private static final Text PLAYER_CONDITIONS_HEADER = ModUtils.getScreenTranslated("predicate.player");
    private static final Text TEXT_BEFORE = ModUtils.getScreenTranslated("criterion.item_crafted.before");
    private static final Text TEXT_BEFORE_WITH_CONDITIONS = ModUtils.getScreenTranslated("criterion.item_crafted.before.with_conditions");
    private static final Text TEXT_AFTER = ModUtils.getScreenTranslated("criterion.item_crafted.after");
    private static final Text TEXT_AFTER_WITH_CONDITIONS = ModUtils.getScreenTranslated("criterion.item_crafted.after.with_conditions");

    @Override
    public CriterionDisplayElement prepare(ResearchCriterion<ItemCraftedCriterion.Conditions> criterion) {
        IndentedTextHolder textHolder = new IndentedTextHolder();

        PredicateHelper.optionalTooltip(criterion.conditions().item(), ItemPredicateHelper::tooltip, ITEM_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);
        PredicateHelper.optionalTooltip(criterion.conditions().player(), EntityPredicateHelper::tooltip, PLAYER_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);

        CriterionDisplayElement beforeText = new TextElement(textHolder.isEmpty() ? TEXT_BEFORE : TEXT_BEFORE_WITH_CONDITIONS);
        CriterionDisplayElement afterText = new TextElement(textHolder.isEmpty() ? TEXT_AFTER : TEXT_AFTER_WITH_CONDITIONS);
        if (!textHolder.isEmpty()) {
            beforeText = beforeText.withTextTooltip(textHolder.getText());
            afterText = afterText.withTextTooltip(textHolder.getText());
        }

        CriterionDisplayElement itemElement = ItemPredicateHelper.element(criterion.conditions().item());

        return new CriterionDisplay(
                CriterionDisplay.makeCountElement(criterion),
                beforeText,
                itemElement,
                afterText
        );
    }
}
