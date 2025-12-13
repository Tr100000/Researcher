package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.CriterionDisplay;
import io.github.tr100000.researcher.api.criterion.CriterionDisplayElement;
import io.github.tr100000.researcher.api.criterion.CriterionHandler;
import io.github.tr100000.researcher.api.criterion.element.TextElement;
import io.github.tr100000.researcher.api.criterion.util.BlockPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.EntityPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.PredicateHelper;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import io.github.tr100000.researcher.criteria.BlockBrokenCriterion;
import net.minecraft.text.Text;

public class BlockBrokenCriterionHandler implements CriterionHandler<BlockBrokenCriterion.Conditions> {
    private static final Text BLOCK_CONDITIONS_HEADER = ModUtils.getScreenTranslated("predicate.block");
    private static final Text PLAYER_CONDITIONS_HEADER = ModUtils.getScreenTranslated("predicate.player");
    private static final Text TEXT_BEFORE = ModUtils.getScreenTranslated("criterion.block_broken.before");
    private static final Text TEXT_BEFORE_WITH_CONDITIONS = ModUtils.getScreenTranslated("criterion.block_broken.before.with_conditions");
    private static final Text TEXT_AFTER = ModUtils.getScreenTranslated("criterion.block_broken.after");
    private static final Text TEXT_AFTER_WITH_CONDITIONS = ModUtils.getScreenTranslated("criterion.block_broken.after.with_conditions");

    @Override
    public CriterionDisplayElement prepare(ResearchCriterion<BlockBrokenCriterion.Conditions> criterion) {
        IndentedTextHolder textHolder = new IndentedTextHolder();

        PredicateHelper.optionalTooltip(criterion.conditions().state(), PredicateHelper::stateTooltip, BLOCK_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);
        PredicateHelper.optionalTooltip(criterion.conditions().player(), EntityPredicateHelper::tooltip, PLAYER_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);

        CriterionDisplayElement beforeText = new TextElement(textHolder.isEmpty() ? TEXT_BEFORE : TEXT_BEFORE_WITH_CONDITIONS);
        CriterionDisplayElement afterText = new TextElement(textHolder.isEmpty() ? TEXT_AFTER : TEXT_AFTER_WITH_CONDITIONS);
        if (!textHolder.isEmpty()) {
            beforeText = beforeText.withTextTooltip(textHolder.getText());
            afterText = afterText.withTextTooltip(textHolder.getText());
        }

        CriterionDisplayElement blockElement = BlockPredicateHelper.element(criterion.conditions().block().orElse(null));

        return new CriterionDisplay(
                CriterionDisplay.makeCountElement(criterion),
                beforeText,
                blockElement,
                afterText
        );
    }
}
