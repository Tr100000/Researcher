package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.CriterionDisplay;
import io.github.tr100000.researcher.api.criterion.CriterionDisplayElement;
import io.github.tr100000.researcher.api.criterion.CriterionHandler;
import io.github.tr100000.researcher.api.criterion.element.TextElement;
import io.github.tr100000.researcher.api.criterion.util.BlockPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.EntityPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.ItemPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.NumberRangeUtils;
import io.github.tr100000.researcher.api.criterion.util.PredicateHelper;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.advancement.criterion.BeeNestDestroyedCriterion;
import net.minecraft.text.Text;

public class BeeNestDestroyedCriterionHandler implements CriterionHandler<BeeNestDestroyedCriterion.Conditions> {
    private static final Text ITEM_CONDITIONS_HEADER = ModUtils.getScreenTranslated("criterion.bee_nest_destroyed.item");
    private static final Text NUM_BEES_INSIDE = ModUtils.getScreenTranslated("criterion.bee_nest_destroyed.bees_inside");
    private static final Text PLAYER_CONDITIONS_HEADER = ModUtils.getScreenTranslated("predicate.player");
    private static final Text BEFORE_KEY = ModUtils.getScreenTranslated("criterion.bee_nest_destroyed.before");
    private static final Text BEFORE_WITH_CONDITIONS_KEY = ModUtils.getScreenTranslated("criterion.bee_nest_destroyed.before.with_conditions");
    private static final Text AFTER_KEY = ModUtils.getScreenTranslated("criterion.bee_nest_destroyed.after");
    private static final Text AFTER_WITH_CONDITIONS = ModUtils.getScreenTranslated("criterion.bee_nest_destroyed.after.with_conditions");
    private static final Text ANY_BLOCK = ModUtils.getScreenTranslated("criterion.bee_nest_destroyed.any");

    @Override
    public CriterionDisplayElement prepare(ResearchCriterion<BeeNestDestroyedCriterion.Conditions> criterion) {
        IndentedTextHolder textHolder = new IndentedTextHolder();

        PredicateHelper.tooltip(criterion.conditions().item(), ItemPredicateHelper::tooltip, ITEM_CONDITIONS_HEADER)
                    .ifPresent(textHolder::accept);
        NumberRangeUtils.tooltip(criterion.conditions().beesInside(), NUM_BEES_INSIDE, textHolder);
        PredicateHelper.tooltip(criterion.conditions().player(), EntityPredicateHelper::tooltip, PLAYER_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);

        CriterionDisplayElement textBefore = new TextElement(textHolder.isEmpty() ? BEFORE_KEY : BEFORE_WITH_CONDITIONS_KEY);
        CriterionDisplayElement textAfter = new TextElement(textHolder.isEmpty() ? AFTER_KEY : AFTER_WITH_CONDITIONS);

        if (!textHolder.isEmpty()) {
            textBefore = textBefore.withTextTooltip(textHolder.getText());
            textAfter = textAfter.withTextTooltip(textHolder.getText());
        }

        CriterionDisplayElement block = criterion.conditions().block().map(BlockPredicateHelper::element).orElseGet(() -> new TextElement(ANY_BLOCK));

        return new CriterionDisplay(
                CriterionDisplay.makeCountElement(criterion),
                textBefore,
                block,
                textAfter
        );
    }
}
