package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.CriterionDisplay;
import io.github.tr100000.researcher.api.criterion.CriterionDisplayElement;
import io.github.tr100000.researcher.api.criterion.CriterionHandler;
import io.github.tr100000.researcher.api.criterion.element.TextElement;
import io.github.tr100000.researcher.api.criterion.util.EntityPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.ItemPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.PredicateHelper;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.advancement.criterion.RecipeCraftedCriterion;
import net.minecraft.text.Text;

public class RecipeCraftedCriterionHandler implements CriterionHandler<RecipeCraftedCriterion.Conditions> {
    public static final RecipeCraftedCriterionHandler RECIPE_CRAFTED = new RecipeCraftedCriterionHandler(ModUtils.getScreenTranslationKey("criterion.recipe_crafted"));
    public static final RecipeCraftedCriterionHandler CRAFTER_RECIPE_CRAFTED = new RecipeCraftedCriterionHandler(ModUtils.getScreenTranslationKey("criterion.crafter_recipe_crafted"));

    private static final Text INGREDIENT_CONDITIONS_HEADER = ModUtils.getScreenTranslated("criterion.recipe_crafted.ingredient");
    private static final Text PLAYER_CONDITIONS_HEADER = ModUtils.getScreenTranslated("predicate.player");

    private final String text;
    private final String textWithConditions;

    public RecipeCraftedCriterionHandler(String textKey) {
        this.text = textKey;
        this.textWithConditions = textKey + ".with_conditions";
    }

    @Override
    public CriterionDisplayElement prepare(ResearchCriterion<RecipeCraftedCriterion.Conditions> criterion) {
        IndentedTextHolder textHolder = new IndentedTextHolder();
        criterion.conditions().ingredients().forEach(ingredient -> {
            textHolder.accept(INGREDIENT_CONDITIONS_HEADER);
            textHolder.push();
            ItemPredicateHelper.tooltip(ingredient, textHolder);
            textHolder.pop();
        });
        PredicateHelper.tooltip(criterion.conditions().player(), EntityPredicateHelper::tooltip, PLAYER_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);

        CriterionDisplayElement element = new TextElement(Text.translatable(textHolder.isEmpty() ? text : textWithConditions, criterion.conditions().recipeId().getValue()));
        if (!textHolder.isEmpty()) element = element.withTextTooltip(textHolder.getText());

        return new CriterionDisplay(
                CriterionDisplay.makeCountElement(criterion),
                element
        );
    }
}
