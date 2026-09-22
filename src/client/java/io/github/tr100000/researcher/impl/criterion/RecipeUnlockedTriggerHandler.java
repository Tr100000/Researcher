package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.trigger.TriggerDisplay;
import io.github.tr100000.researcher.api.trigger.TriggerDisplayElement;
import io.github.tr100000.researcher.api.trigger.TriggerHandler;
import io.github.tr100000.researcher.api.trigger.element.EmptyElement;
import io.github.tr100000.researcher.api.trigger.element.ItemElement;
import io.github.tr100000.researcher.api.trigger.element.TextElement;
import io.github.tr100000.researcher.api.trigger.element.TimedSwitchingElement;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.advancements.triggers.RecipeUnlockedTrigger;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;

import java.util.List;

public class RecipeUnlockedTriggerHandler implements TriggerHandler<RecipeUnlockedTrigger.TriggerInstance> {
    private static final String TEXT_KEY = ModUtils.getScreenTranslationKey("trigger.recipe_unlocked");
    private static final Component TEXT_ANY = ModUtils.getScreenTranslated("trigger.recipe_unlocked.any");

    @Override
    public TriggerDisplayElement prepare(ResearchCriterion<RecipeUnlockedTrigger.TriggerInstance> criterion) {
        List<TriggerDisplayElement> elements = new ObjectArrayList<>();
        for (Holder<Recipe<?>> recipe : criterion.conditions().recipes()) {
            elements.add(new TextElement(Component.translatable(TEXT_KEY, recipe.getRegisteredName())));
        }

        return new TriggerDisplay(
                makeWarningElement(criterion),
                elements.isEmpty() ? new TextElement(TEXT_ANY) : new TimedSwitchingElement(elements)
        );
    }

    private TriggerDisplayElement makeWarningElement(ResearchCriterion<RecipeUnlockedTrigger.TriggerInstance> criterion) {
        if (criterion.count() != 1) {
            return new ItemElement(Items.BARRIER, false).withTextTooltip(Component.literal("Criterion count should not 1, not " + criterion.count()));
        }
        if (criterion.conditions().player().isPresent()) {
            return new ItemElement(Items.BARRIER, false).withTextTooltip(Component.literal("Criterion should not have player conditions"));
        }

        return EmptyElement.INSTANCE;
    }
}
