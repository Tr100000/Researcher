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
import net.minecraft.advancement.criterion.FilledBucketCriterion;
import net.minecraft.text.Text;

public class FilledBucketCriterionHandler implements CriterionHandler<FilledBucketCriterion.Conditions> {
    private static final Text PLAYER_CONDITIONS_HEADER = ModUtils.getScreenTranslated("predicate.player");
    private static final Text ITEM_CONDITIONS_HEADER = ModUtils.getScreenTranslated("criterion.filled_bucket.result_conditions");
    private static final Text TEXT = ModUtils.getScreenTranslated("criterion.filled_bucket");
    private static final Text TEXT_WITH_CONDITIONS = ModUtils.getScreenTranslated("criterion.filled_bucket_with_conditions");

    @Override
    public CriterionDisplayElement prepare(ResearchCriterion<FilledBucketCriterion.Conditions> criterion) {
        IndentedTextHolder textHolder = new IndentedTextHolder();
        PredicateHelper.tooltip(criterion.conditions().player(), EntityPredicateHelper::tooltip, PLAYER_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);
        PredicateHelper.tooltip(criterion.conditions().item(), ItemPredicateHelper::tooltip, ITEM_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);

        CriterionDisplayElement element = new TextElement(textHolder.isEmpty() ? TEXT : TEXT_WITH_CONDITIONS);
        if (!textHolder.isEmpty()) element = element.withTextTooltip(TEXT_WITH_CONDITIONS);

        return new CriterionDisplay(
                CriterionDisplay.getCountElement(criterion),
                element
        );
    }
}
