package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.util.EntityPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.ItemPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.NumberRangeUtils;
import io.github.tr100000.researcher.api.criterion.util.PredicateHelper;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.advancement.criterion.KilledByArrowCriterion;
import net.minecraft.text.Text;

public class KilledByArrowCriterionHandler extends AbstractCriterionHandler<KilledByArrowCriterion.Conditions> {
    private static final Text NUM_ENTITY_TYPES = ModUtils.getScreenTranslated("criterion.killed_by_arrow.entity_type_count");
    private static final Text WEAPON_CONDITIONS_HEADER = ModUtils.getScreenTranslated("criterion.killed_by_arrow.weapon");
    private static final Text VICTIM_CONDITIONS_HEADER = ModUtils.getScreenTranslated("criterion.killed_by_arrow.victim");

    public KilledByArrowCriterionHandler() {
        super(ModUtils.getScreenTranslationKey("criterion.killed_by_arrow"));
    }

    @Override
    protected void fillTooltip(ResearchCriterion<KilledByArrowCriterion.Conditions> criterion, IndentedTextHolder textHolder) {
        NumberRangeUtils.tooltip(criterion.conditions().uniqueEntityTypes(), NUM_ENTITY_TYPES, textHolder);
        PredicateHelper.tooltip(criterion.conditions().firedFromWeapon(), ItemPredicateHelper::tooltip, WEAPON_CONDITIONS_HEADER);
        playerTooltip(criterion, textHolder);
        criterion.conditions().victims().forEach(victim -> {
            textHolder.accept(VICTIM_CONDITIONS_HEADER);
            textHolder.push();
            EntityPredicateHelper.tooltip(victim, textHolder);
            textHolder.pop();
        });
    }
}
