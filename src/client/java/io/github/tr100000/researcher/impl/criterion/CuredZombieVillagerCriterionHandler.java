package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.util.EntityPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.PredicateHelper;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.advancement.criterion.CuredZombieVillagerCriterion;
import net.minecraft.text.Text;

public class CuredZombieVillagerCriterionHandler extends AbstractCriterionHandler<CuredZombieVillagerCriterion.Conditions> {
    private static final Text ZOMBIE_CONDITIONS_HEADER = ModUtils.getScreenTranslated("criterion.cured_zombie_villager.zombie");
    private static final Text VILLAGER_CONDITIONS_HEADER = ModUtils.getScreenTranslated("criterion.cured_zombie_villager.villager");

    public CuredZombieVillagerCriterionHandler() {
        super(ModUtils.getScreenTranslationKey("criterion.cured_zombie_villager"));
    }

    @Override
    protected void fillTooltip(ResearchCriterion<CuredZombieVillagerCriterion.Conditions> criterion, IndentedTextHolder textHolder) {
        PredicateHelper.tooltip(criterion.conditions().zombie(), EntityPredicateHelper::tooltip, ZOMBIE_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);
        PredicateHelper.tooltip(criterion.conditions().villager(), EntityPredicateHelper::tooltip, VILLAGER_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);
        playerTooltip(criterion, textHolder);
    }
}
