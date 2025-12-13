package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.util.DamagePredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.PredicateHelper;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.advancement.criterion.EntityHurtPlayerCriterion;
import net.minecraft.text.Text;

public class EntityHurtPlayerCriterionHandler extends AbstractCriterionHandler<EntityHurtPlayerCriterion.Conditions> {
    private static final Text DAMAGE_CONDITIONS_HEADER = ModUtils.getScreenTranslated("predicate.damage");

    public EntityHurtPlayerCriterionHandler() {
        super(ModUtils.getScreenTranslationKey("criterion.entity_hurt_player"));
    }

    @Override
    protected void fillTooltip(ResearchCriterion<EntityHurtPlayerCriterion.Conditions> criterion, IndentedTextHolder textHolder) {
        playerTooltip(criterion, textHolder);
        PredicateHelper.optionalTooltip(criterion.conditions().damage(), DamagePredicateHelper::tooltip, DAMAGE_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);
    }
}
