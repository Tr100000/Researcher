package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.CriterionDisplay;
import io.github.tr100000.researcher.api.criterion.CriterionDisplayElement;
import io.github.tr100000.researcher.api.criterion.CriterionHandler;
import io.github.tr100000.researcher.api.criterion.element.EmptyElement;
import io.github.tr100000.researcher.api.criterion.element.ItemElement;
import io.github.tr100000.researcher.api.criterion.element.TextElement;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

public class RecipeUnlockedCriterionHandler implements CriterionHandler<RecipeUnlockedCriterion.Conditions> {
    private static final String TEXT_KEY = ModUtils.getScreenTranslationKey("criterion.recipe_unlocked");

    @Override
    public CriterionDisplayElement prepare(ResearchCriterion<RecipeUnlockedCriterion.Conditions> criterion) {
        return new CriterionDisplay(
                makeWarningElement(criterion),
                new TextElement(Text.translatable(TEXT_KEY, criterion.conditions().recipe().getValue()))
        );
    }

    private CriterionDisplayElement makeWarningElement(ResearchCriterion<RecipeUnlockedCriterion.Conditions> criterion) {
        if (criterion.count() != 1) {
            return new ItemElement(Items.BARRIER.getDefaultStack(), false).withTextTooltip(Text.literal("Criterion count should not 1, not " + criterion.count()));
        }
        if (criterion.conditions().player().isPresent()) {
            return new ItemElement(Items.BARRIER.getDefaultStack(), false).withTextTooltip(Text.literal("Criterion should not have player conditions"));
        }

        return EmptyElement.INSTANCE;
    }
}
