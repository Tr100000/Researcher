package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.trigger.TriggerDisplay;
import io.github.tr100000.researcher.api.trigger.TriggerDisplayElement;
import io.github.tr100000.researcher.api.trigger.TriggerHandler;
import io.github.tr100000.researcher.api.trigger.element.GroupedElement;
import io.github.tr100000.researcher.api.trigger.element.ItemElement;
import io.github.tr100000.researcher.api.trigger.element.TextElement;
import io.github.tr100000.researcher.api.trigger.element.TimedSwitchingElement;
import io.github.tr100000.researcher.api.trigger.util.EntityPredicateHelper;
import io.github.tr100000.researcher.api.trigger.util.PredicateHelper;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import io.github.tr100000.trutils.api.item.ItemUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.advancements.triggers.BrewedPotionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.predicates.PotionsPredicate;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.alchemy.Potion;

import java.util.List;
import java.util.Optional;

public class BrewedPotionTriggerHandler implements TriggerHandler<BrewedPotionTrigger.TriggerInstance> {
    private static final Component PLAYER_CONDITIONS_HEADER = ModUtils.getScreenTranslated("trigger.player");
    private static final Component TEXT = ModUtils.getScreenTranslated("trigger.brewed_potion");
    private static final Component TEXT_WITH_CONDITIONS = ModUtils.getScreenTranslated("trigger.brewed_potion.with_condition");
    private static final Component TEXT_SPECIFIC_BEFORE = ModUtils.getScreenTranslated("trigger.brewed_potion.specific.before");
    private static final Component TEXT_SPECIFIC_WITH_CONDITIONS_BEFORE = ModUtils.getScreenTranslated("trigger.brewed_potion.specific.with_condition.before");
    private static final Component TEXT_SPECIFIC_AFTER = ModUtils.getScreenTranslated("trigger.brewed_potion.specific.after");
    private static final Component TEXT_SPECIFIC_WITH_CONDITIONS_AFTER = ModUtils.getScreenTranslated("trigger.brewed_potion.specific.with_condition.after");
    private static final Component TEXT_ANY_POTION_WITH_CONDITIONS = ModUtils.getScreenTranslated("trigger.brewed_potion.any");

    @Override
    public TriggerDisplayElement prepare(ResearchCriterion<BrewedPotionTrigger.TriggerInstance> criterion) {
        Optional<PotionsPredicate> potion = criterion.conditions().potion();

        IndentedTextHolder textHolder = new IndentedTextHolder();
        PredicateHelper.optionalTooltip(criterion.conditions().player(), EntityPredicateHelper::tooltip, PLAYER_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);

        TriggerDisplayElement element;
        if (potion.isPresent()) {
            if (potion.get().effects().isPresent())
                PredicateHelper.collectionTooltip(potion.get().effects().get(), EntityPredicateHelper::effectTooltip, textHolder);

            TriggerDisplayElement beforeText = new TextElement(textHolder.isEmpty() ? TEXT_SPECIFIC_BEFORE : TEXT_SPECIFIC_WITH_CONDITIONS_BEFORE);
            TriggerDisplayElement afterText = new TextElement(textHolder.isEmpty() ? TEXT_SPECIFIC_AFTER : TEXT_SPECIFIC_WITH_CONDITIONS_AFTER);
            if (!textHolder.isEmpty()) {
                beforeText = beforeText.withTextTooltip(textHolder.getText());
                afterText = afterText.withTextTooltip(textHolder.getText());
            }

            element = new GroupedElement(
                    beforeText,
                    getPotionElement(potion.flatMap(PotionsPredicate::potions).orElse(HolderSet.empty())),
                    afterText
            );
        }
        else {
            element = new TextElement(textHolder.isEmpty() ? TEXT : TEXT_WITH_CONDITIONS);
            if (!textHolder.isEmpty()) element = element.withTextTooltip(textHolder.getText());
        }

        return new TriggerDisplay(
                TriggerDisplay.makeCountElement(criterion),
                element
        );
    }

    private TriggerDisplayElement getPotionElement(HolderSet<Potion> potions) {
        List<TriggerDisplayElement> elements = new ObjectArrayList<>();
        for (Holder<Potion> potion : potions) {
            elements.add(new GroupedElement(
                    new ItemElement(ItemUtils.getPotionStack(potion), false),
                    new TextElement(Component.translatable(ItemUtils.getPotionTranslationKey(potion)))
            ));
        }

        return elements.isEmpty()
                ? new TextElement(TEXT_ANY_POTION_WITH_CONDITIONS)
                : new TimedSwitchingElement(elements);
    }
}
