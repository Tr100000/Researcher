package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.advancement.criterion.SpearMobsCriterion;
import net.minecraft.text.Text;

public class SpearMobsCriterionHandler extends AbstractCriterionHandler<SpearMobsCriterion.Conditions> {
    private static final String COUNT_KEY = ModUtils.getScreenTranslationKey("criterion.spear_mobs.count");

    public SpearMobsCriterionHandler() {
        super(ModUtils.getScreenTranslationKey("criterion.spear_mobs"));
    }

    @Override
    protected void fillTooltip(ResearchCriterion<SpearMobsCriterion.Conditions> criterion, IndentedTextHolder textHolder) {
        if (criterion.conditions().count().isPresent()) {
            textHolder.accept(Text.translatable(COUNT_KEY, criterion.conditions().count().get()));
        }
        playerTooltip(criterion, textHolder);
    }
}
