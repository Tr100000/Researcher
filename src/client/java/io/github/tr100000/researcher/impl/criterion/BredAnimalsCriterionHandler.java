package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.util.EntityPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.PredicateHelper;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.advancement.criterion.BredAnimalsCriterion;
import net.minecraft.text.Text;

public class BredAnimalsCriterionHandler extends AbstractCriterionHandler<BredAnimalsCriterion.Conditions> {
    private static final Text PARENT_CONDITIONS_HEADER = ModUtils.getScreenTranslated("criterion.bred_animals.parent");
    private static final Text PARTNER_CONDITIONS_HEADER = ModUtils.getScreenTranslated("criterion.bred_animals.partner");
    private static final Text CHILD_CONDITIONS_HEADER = ModUtils.getScreenTranslated("criterion.bred_animals.child");

    public BredAnimalsCriterionHandler() {
        super(ModUtils.getScreenTranslationKey("criterion.bred_animals"));
    }

    @Override
    protected void fillTooltip(ResearchCriterion<BredAnimalsCriterion.Conditions> criterion, IndentedTextHolder textHolder) {
        PredicateHelper.tooltip(criterion.conditions().parent(), EntityPredicateHelper::tooltip, PARENT_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);
        PredicateHelper.tooltip(criterion.conditions().partner(), EntityPredicateHelper::tooltip, PARTNER_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);
        PredicateHelper.tooltip(criterion.conditions().child(), EntityPredicateHelper::tooltip, CHILD_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);
        playerTooltip(criterion, textHolder);
    }
}
