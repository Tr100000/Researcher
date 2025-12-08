package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.util.NumberRangeUtils;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.advancement.criterion.ConstructBeaconCriterion;
import net.minecraft.text.Text;

public class ConstructBeaconCriterionHandler extends AbstractCriterionHandler<ConstructBeaconCriterion.Conditions> {
    private static final Text BEACON_LEVEL = ModUtils.getScreenTranslated("criterion.construct_beacon.beacon_level");

    public ConstructBeaconCriterionHandler() {
        super(ModUtils.getScreenTranslationKey("criterion.construct_beacon"));
    }

    @Override
    protected void fillTooltip(ResearchCriterion<ConstructBeaconCriterion.Conditions> criterion, IndentedTextHolder textHolder) {
        NumberRangeUtils.tooltip(criterion.conditions().level(), BEACON_LEVEL, textHolder);
        playerTooltip(criterion, textHolder);
    }
}
