package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.util.NumberRangeUtils;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.advancement.criterion.UsedEnderEyeCriterion;
import net.minecraft.text.Text;

public class UsedEnderEyeCriterionHandler extends AbstractCriterionHandler<UsedEnderEyeCriterion.Conditions> {
    private static final Text STRONGHOLD_DISTANCE = ModUtils.getScreenTranslated("criterion.used_ender_eye.stronghold_distance");

    public UsedEnderEyeCriterionHandler() {
        super(ModUtils.getScreenTranslationKey("criterion.used_ender_eye"));
    }

    @Override
    protected void fillTooltip(ResearchCriterion<UsedEnderEyeCriterion.Conditions> criterion, IndentedTextHolder textHolder) {
        NumberRangeUtils.tooltip(criterion.conditions().distance(), STRONGHOLD_DISTANCE, textHolder);
        super.playerTooltip(criterion, textHolder);
    }
}
