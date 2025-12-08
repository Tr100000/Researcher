package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.util.EntityPredicateHelper;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.advancement.criterion.ChanneledLightningCriterion;
import net.minecraft.text.Text;

public class ChanneledLightningCriterionHandler extends AbstractCriterionHandler<ChanneledLightningCriterion.Conditions> {
    private static final Text VICTIM_CONDITIONS_HEADER = ModUtils.getScreenTranslated("criterion.channeled_lightning.victim");

    public ChanneledLightningCriterionHandler() {
        super(ModUtils.getScreenTranslationKey("criterion.channeled_lightning"));
    }

    @Override
    protected void fillTooltip(ResearchCriterion<ChanneledLightningCriterion.Conditions> criterion, IndentedTextHolder textHolder) {
        criterion.conditions().victims().forEach(victim -> {
            textHolder.accept(VICTIM_CONDITIONS_HEADER);
            textHolder.push();
            EntityPredicateHelper.tooltip(victim, textHolder);
            textHolder.pop();
        });
        playerTooltip(criterion, textHolder);
    }
}
