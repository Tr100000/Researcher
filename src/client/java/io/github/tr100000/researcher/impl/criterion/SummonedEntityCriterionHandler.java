package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.util.EntityPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.PredicateHelper;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.advancement.criterion.SummonedEntityCriterion;
import net.minecraft.text.Text;

// TODO improve this
public class SummonedEntityCriterionHandler extends AbstractCriterionHandler<SummonedEntityCriterion.Conditions> {
    private static final Text ENTITY_CONDITIONS_HEADER = ModUtils.getScreenTranslated("criterion.summoned_entity.spawned");

    public SummonedEntityCriterionHandler() {
        super(ModUtils.getScreenTranslationKey("criterion.summoned_entity"));
    }

    @Override
    protected void fillTooltip(ResearchCriterion<SummonedEntityCriterion.Conditions> criterion, IndentedTextHolder textHolder) {
        PredicateHelper.tooltip(criterion.conditions().entity(), EntityPredicateHelper::tooltip, ENTITY_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);
        super.playerTooltip(criterion, textHolder);
    }
}
