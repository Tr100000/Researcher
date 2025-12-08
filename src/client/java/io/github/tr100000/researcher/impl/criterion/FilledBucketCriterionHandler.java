package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.util.ItemPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.PredicateHelper;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.advancement.criterion.FilledBucketCriterion;
import net.minecraft.text.Text;

public class FilledBucketCriterionHandler extends AbstractCriterionHandler<FilledBucketCriterion.Conditions> {
    private static final Text ITEM_CONDITIONS_HEADER = ModUtils.getScreenTranslated("criterion.filled_bucket.result_conditions");

    public FilledBucketCriterionHandler() {
        super(ModUtils.getScreenTranslationKey("criterion.filled_bucket"));
    }

    @Override
    protected void fillTooltip(ResearchCriterion<FilledBucketCriterion.Conditions> criterion, IndentedTextHolder textHolder) {
        PredicateHelper.tooltip(criterion.conditions().item(), ItemPredicateHelper::tooltip, ITEM_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);
        playerTooltip(criterion, textHolder);
    }
}
