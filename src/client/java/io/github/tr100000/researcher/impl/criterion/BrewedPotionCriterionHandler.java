package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.CriterionDisplay;
import io.github.tr100000.researcher.api.criterion.CriterionDisplayElement;
import io.github.tr100000.researcher.api.criterion.CriterionHandler;
import io.github.tr100000.researcher.api.criterion.element.GroupedElement;
import io.github.tr100000.researcher.api.criterion.element.ItemElement;
import io.github.tr100000.researcher.api.criterion.element.TextElement;
import io.github.tr100000.researcher.api.criterion.util.EntityPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.PredicateHelper;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.advancement.criterion.BrewedPotionCriterion;
import net.minecraft.potion.Potion;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;

import java.util.Optional;

public class BrewedPotionCriterionHandler implements CriterionHandler<BrewedPotionCriterion.Conditions> {
    private static final Text PLAYER_CONDITIONS_HEADER = ModUtils.getScreenTranslated("predicate.player");
    private static final Text TEXT = ModUtils.getScreenTranslated("criterion.brewed_potion");
    private static final Text TEXT_WITH_CONDITIONS = ModUtils.getScreenTranslated("criterion.brewed_potion.with_condition");
    private static final Text TEXT_SPECIFIC_BEFORE = ModUtils.getScreenTranslated("criterion.brewed_potion.specific.before");
    private static final Text TEXT_SPECIFIC_WITH_CONDITIONS_BEFORE = ModUtils.getScreenTranslated("criterion.brewed_potion.specific.with_condition.before");
    private static final Text TEXT_SPECIFIC_AFTER = ModUtils.getScreenTranslated("criterion.brewed_potion.specific.after");
    private static final Text TEXT_SPECIFIC_WITH_CONDITIONS_AFTER = ModUtils.getScreenTranslated("criterion.brewed_potion.specific.with_condition.after");

    @Override
    public CriterionDisplayElement prepare(ResearchCriterion<BrewedPotionCriterion.Conditions> criterion) {
        Optional<RegistryEntry<Potion>> potion = criterion.conditions().potion();

        IndentedTextHolder textHolder = new IndentedTextHolder();
        PredicateHelper.tooltip(criterion.conditions().player(), EntityPredicateHelper::tooltip, PLAYER_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);

        CriterionDisplayElement element;
        if (potion.isPresent()) {
            CriterionDisplayElement beforeText = new TextElement(textHolder.isEmpty() ? TEXT_SPECIFIC_BEFORE : TEXT_SPECIFIC_WITH_CONDITIONS_BEFORE);
            CriterionDisplayElement afterText = new TextElement(textHolder.isEmpty() ? TEXT_SPECIFIC_AFTER : TEXT_SPECIFIC_WITH_CONDITIONS_AFTER);
            if (!textHolder.isEmpty()) {
                beforeText = beforeText.withTextTooltip(textHolder.getText());
                afterText = afterText.withTextTooltip(textHolder.getText());
            }

            element = new GroupedElement(
                    beforeText,
                    getPotionElement(potion.get()),
                    afterText
            );
        }
        else {
            element = new TextElement(textHolder.isEmpty() ? TEXT : TEXT_WITH_CONDITIONS);
            if (!textHolder.isEmpty()) element = element.withTextTooltip(textHolder.getText());
        }

        return new CriterionDisplay(
                CriterionDisplay.getCountElement(criterion),
                element
        );
    }

    private CriterionDisplayElement getPotionElement(RegistryEntry<Potion> potion) {
        return new GroupedElement(
                new ItemElement(ModUtils.getPotionStack(potion), false),
                new TextElement(Text.translatable(ModUtils.getPotionTranslationKey(potion)))
        );
    }
}
