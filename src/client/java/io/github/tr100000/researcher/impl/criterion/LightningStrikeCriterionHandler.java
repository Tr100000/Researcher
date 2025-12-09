package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.util.EntityPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.PredicateHelper;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.advancement.criterion.LightningStrikeCriterion;
import net.minecraft.text.Text;

public class LightningStrikeCriterionHandler extends AbstractCriterionHandler<LightningStrikeCriterion.Conditions> {
    private static final Text LIGHTNING_CONDITIONS_HEADER = ModUtils.getScreenTranslated("criterion.lightning_strike.lightning");
    private static final Text BYSTANDER_CONDITIONS_HEADER = ModUtils.getScreenTranslated("criterion.lightning_strike.bystander");

    public LightningStrikeCriterionHandler() {
        super(ModUtils.getScreenTranslationKey("criterion.lightning_strike"));
    }

    @Override
    protected void fillTooltip(ResearchCriterion<LightningStrikeCriterion.Conditions> criterion, IndentedTextHolder textHolder) {
        PredicateHelper.tooltip(criterion.conditions().lightning(), EntityPredicateHelper::tooltip, LIGHTNING_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);
        PredicateHelper.tooltip(criterion.conditions().bystander(), EntityPredicateHelper::tooltip, BYSTANDER_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);
        playerTooltip(criterion, textHolder);
    }
}
