package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.CriterionDisplay;
import io.github.tr100000.researcher.api.criterion.CriterionDisplayElement;
import io.github.tr100000.researcher.api.criterion.CriterionHandler;
import io.github.tr100000.researcher.api.criterion.element.GroupedElement;
import io.github.tr100000.researcher.api.criterion.element.TextElement;
import io.github.tr100000.researcher.api.criterion.util.BlockPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.EntityPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.PredicateHelper;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.advancement.criterion.EnterBlockCriterion;
import net.minecraft.text.Text;

public class EnterBlockCriterionHandler implements CriterionHandler<EnterBlockCriterion.Conditions> {
    private static final Text PLAYER_CONDITIONS_HEADER = ModUtils.getScreenTranslated("predicate.player");
    private static final Text BLOCK_CONDITIONS_HEADER = ModUtils.getScreenTranslated("predicate.block");
    private static final Text BEFORE_KEY = ModUtils.getScreenTranslated("criterion.enter_block.before");
    private static final Text BEFORE_WITH_CONDITIONS_KEY = ModUtils.getScreenTranslated("criterion.enter_block.before.with_conditions");
    private static final Text AFTER_KEY = ModUtils.getScreenTranslated("criterion.enter_block.after");
    private static final Text AFTER_WITH_CONDITIONS = ModUtils.getScreenTranslated("criterion.enter_block.after.with_conditions");
    private static final Text ANY_BLOCK = ModUtils.getScreenTranslated("criterion.enter_block.any");

    @Override
    public CriterionDisplayElement prepare(ResearchCriterion<EnterBlockCriterion.Conditions> criterion) {
        IndentedTextHolder playerConditionTextHolder = new IndentedTextHolder();
        PredicateHelper.optionalTooltip(criterion.conditions().player(), EntityPredicateHelper::tooltip, PLAYER_CONDITIONS_HEADER)
                .ifPresent(playerConditionTextHolder::accept);

        CriterionDisplayElement textBefore = new TextElement(playerConditionTextHolder.isEmpty() ? BEFORE_KEY : BEFORE_WITH_CONDITIONS_KEY);
        CriterionDisplayElement textAfter = new TextElement(playerConditionTextHolder.isEmpty() ? AFTER_KEY : AFTER_WITH_CONDITIONS);

        if (!playerConditionTextHolder.isEmpty()) {
            textBefore = textBefore.withTextTooltip(playerConditionTextHolder.getText());
            textAfter = textAfter.withTextTooltip(playerConditionTextHolder.getText());
        }

        IndentedTextHolder blockConditionTextHolder = new IndentedTextHolder();
        PredicateHelper.optionalTooltip(criterion.conditions().state(), PredicateHelper::stateTooltip, BLOCK_CONDITIONS_HEADER)
                .ifPresent(blockConditionTextHolder::accept);

        CriterionDisplayElement block = criterion.conditions().block().map(BlockPredicateHelper::element).orElseGet(() -> new TextElement(ANY_BLOCK));

        if (!blockConditionTextHolder.isEmpty()) {
            block = new GroupedElement(block, new TextElement(Text.literal("*"))).withTextTooltip(blockConditionTextHolder.getText());
        }

        return new CriterionDisplay(
                CriterionDisplay.makeCountElement(criterion),
                textBefore,
                block,
                textAfter
        );
    }
}
