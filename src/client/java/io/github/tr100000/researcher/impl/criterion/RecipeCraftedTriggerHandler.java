package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.trigger.TriggerDisplay;
import io.github.tr100000.researcher.api.trigger.TriggerDisplayElement;
import io.github.tr100000.researcher.api.trigger.TriggerHandler;
import io.github.tr100000.researcher.api.trigger.element.TextElement;
import io.github.tr100000.researcher.api.trigger.element.TimedSwitchingElement;
import io.github.tr100000.researcher.api.trigger.util.EntityPredicateHelper;
import io.github.tr100000.researcher.api.trigger.util.ItemPredicateHelper;
import io.github.tr100000.researcher.api.trigger.util.PredicateHelper;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.advancements.triggers.RecipeCraftedTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Recipe;

import java.util.List;

public class RecipeCraftedTriggerHandler implements TriggerHandler<RecipeCraftedTrigger.TriggerInstance> {
    public static final RecipeCraftedTriggerHandler RECIPE_CRAFTED = new RecipeCraftedTriggerHandler(ModUtils.getScreenTranslationKey("trigger.recipe_crafted"));
    public static final RecipeCraftedTriggerHandler CRAFTER_RECIPE_CRAFTED = new RecipeCraftedTriggerHandler(ModUtils.getScreenTranslationKey("trigger.crafter_recipe_crafted"));

    private static final Component INGREDIENT_CONDITIONS_HEADER = ModUtils.getScreenTranslated("trigger.recipe_crafted.ingredient");
    private static final Component PLAYER_CONDITIONS_HEADER = ModUtils.getScreenTranslated("predicate.player");

    private final String text;
    private final String textWithConditions;
    private final String textAny;
    private final String textAnyWithConditions;

    public RecipeCraftedTriggerHandler(String textKey) {
        this.text = textKey;
        this.textWithConditions = textKey + ".with_conditions";
        this.textAny = textKey + ".any";
        this.textAnyWithConditions = textKey + ".any.with_conditions";
    }

    @Override
    public TriggerDisplayElement prepare(ResearchCriterion<RecipeCraftedTrigger.TriggerInstance> criterion) {
        IndentedTextHolder textHolder = new IndentedTextHolder();
        criterion.conditions().ingredients().forEach(ingredient -> {
            textHolder.accept(INGREDIENT_CONDITIONS_HEADER);
            textHolder.push();
            ItemPredicateHelper.tooltip(ingredient, textHolder);
            textHolder.pop();
        });
        PredicateHelper.optionalTooltip(criterion.conditions().player(), EntityPredicateHelper::tooltip, PLAYER_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);

        TriggerDisplayElement element = createElement(criterion.conditions().recipes(), !textHolder.isEmpty());
        if (!textHolder.isEmpty()) element = element.withTextTooltip(textHolder.getText());

        return new TriggerDisplay(
                TriggerDisplay.makeCountElement(criterion),
                element
        );
    }

    private TriggerDisplayElement createElement(HolderSet<Recipe<?>> recipes, boolean hasConditions) {
        List<TriggerDisplayElement> elements = new ObjectArrayList<>();
        for (Holder<Recipe<?>> recipe : recipes) {
            elements.add(new TextElement(Component.translatable(hasConditions ? textWithConditions : text, recipe.getRegisteredName())));
        }
        return elements.isEmpty()
                ? new TextElement(Component.translatable(hasConditions ? textAnyWithConditions : textAny))
                : new TimedSwitchingElement(elements);
    }
}
