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
import net.minecraft.advancement.criterion.ShotCrossbowCriterion;
import net.minecraft.text.Text;

import java.util.Optional;

public class ShotCrossbowCriterionHandler implements CriterionHandler<ShotCrossbowCriterion.Conditions> {
    private static final Text ITEM_CONDITIONS_HEADER = ModUtils.getScreenTranslated("criterion.shot_crossbow.item");
    private static final Text PLAYER_CONDITIONS_HEADER = ModUtils.getScreenTranslated("predicate.player");
    private static final Text TEXT = ModUtils.getScreenTranslated("criterion.shot_crossbow");
    private static final Text TEXT_WITH_CONDITIONS = ModUtils.getScreenTranslated("criterion.shot_crossbow.with_conditions");
    private static final Text TEXT_SPECIFIC_BEFORE = ModUtils.getScreenTranslated("criterion.shot_crossbow.specific.before");
    private static final Text TEXT_SPECIFIC_BEFORE_WITH_CONDITIONS = ModUtils.getScreenTranslated("criterion.shot_crossbow.specific.with_conditions.before");
    private static final Text TEXT_SPECIFIC_AFTER = ModUtils.getScreenTranslated("criterion.shot_crossbow.specific.after");
    private static final Text TEXT_SPECIFIC_AFTER_WITH_CONDITIONS = ModUtils.getScreenTranslated("criterion.shot_crossbow.specific.with_conditions.after");

    @Override
    public CriterionDisplayElement prepare(ResearchCriterion<ShotCrossbowCriterion.Conditions> criterion) {
        IndentedTextHolder textHolder = new IndentedTextHolder();

        PredicateHelper.tooltip(criterion.conditions().item(), ItemPredicateHelper::tooltip, ITEM_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);
        PredicateHelper.tooltip(criterion.conditions().player(), EntityPredicateHelper::tooltip, PLAYER_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);

        Optional<CriterionDisplayElement> itemElement = criterion.conditions().item().flatMap(ItemPredicateHelper::element);

        if (itemElement.isPresent()) {
            CriterionDisplayElement beforeElement = new TextElement(textHolder.isEmpty() ? TEXT_SPECIFIC_BEFORE : TEXT_SPECIFIC_BEFORE_WITH_CONDITIONS);
            CriterionDisplayElement afterElement = new TextElement(textHolder.isEmpty() ? TEXT_SPECIFIC_AFTER : TEXT_SPECIFIC_AFTER_WITH_CONDITIONS);
            if (!textHolder.isEmpty()) {
                beforeElement = beforeElement.withTextTooltip(textHolder.getText());
                afterElement = afterElement.withTextTooltip(textHolder.getText());
            }

            return new CriterionDisplay(
                    CriterionDisplay.makeCountElement(criterion),
                    beforeElement,
                    itemElement.get(),
                    afterElement
            );
        }
        else {
            CriterionDisplayElement element = new TextElement(textHolder.isEmpty() ? TEXT : TEXT_WITH_CONDITIONS);
            if (!textHolder.isEmpty()) element = element.withTextTooltip(textHolder.getText());

            return new CriterionDisplay(
                    CriterionDisplay.makeCountElement(criterion),
                    element
            );
        }
    }
}
