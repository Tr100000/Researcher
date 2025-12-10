package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.CriterionDisplay;
import io.github.tr100000.researcher.api.criterion.CriterionDisplayElement;
import io.github.tr100000.researcher.api.criterion.CriterionHandler;
import io.github.tr100000.researcher.api.criterion.element.EmptyElement;
import io.github.tr100000.researcher.api.criterion.element.ItemElement;
import io.github.tr100000.researcher.api.criterion.element.TextElement;
import io.github.tr100000.researcher.criteria.HasResearchCriterion;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

public class HasResearchCriterionHandler implements CriterionHandler<HasResearchCriterion.Conditions> {
    private static final String TEXT_KEY = ModUtils.getScreenTranslationKey("criterion.has_research");

    @Override
    public CriterionDisplayElement prepare(ResearchCriterion<HasResearchCriterion.Conditions> criterion) {
        return new CriterionDisplay(
                makeWarningElement(criterion),
                new TextElement(Text.translatable(TEXT_KEY, criterion.conditions().researchId().toTranslationKey("research")))
        );
    }

    private CriterionDisplayElement makeWarningElement(ResearchCriterion<HasResearchCriterion.Conditions> criterion) {
        if (criterion.count() != 1) {
            return new ItemElement(Items.BARRIER.getDefaultStack(), false).withTextTooltip(Text.literal("Criterion count should not 1, not " + criterion.count()));
        }

        return EmptyElement.INSTANCE;
    }
}
