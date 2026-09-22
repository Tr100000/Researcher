package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.trigger.TriggerDisplay;
import io.github.tr100000.researcher.api.trigger.TriggerDisplayElement;
import io.github.tr100000.researcher.api.trigger.TriggerHandler;
import io.github.tr100000.researcher.api.trigger.element.TextElement;
import io.github.tr100000.researcher.api.trigger.element.TimedSwitchingElement;
import io.github.tr100000.researcher.api.trigger.util.EntityPredicateHelper;
import io.github.tr100000.researcher.api.trigger.util.PredicateHelper;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.advancements.triggers.LootTableTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.List;

public class PlayerGeneratesContainerLootTriggerHandler implements TriggerHandler<LootTableTrigger.TriggerInstance> {
    private static final Component PLAYER_CONDITIONS_HEADER = ModUtils.getScreenTranslated("trigger.player");
    private static final String TEXT_KEY = ModUtils.getScreenTranslationKey("trigger.player_generates_container_loot");
    private static final String TEXT_WITH_CONDITIONS_KEY = ModUtils.getScreenTranslationKey("trigger.player_generates_container_loot.with_conditions");
    private static final Component TEXT_ANY = ModUtils.getScreenTranslated("trigger.player_generates_container_loot.any");
    private static final Component TEXT_ANY_WITH_CONDITIONS = ModUtils.getScreenTranslated("trigger.player_generates_container_loot.any.with_conditions");

    @Override
    public TriggerDisplayElement prepare(ResearchCriterion<LootTableTrigger.TriggerInstance> criterion) {
        IndentedTextHolder textHolder = new IndentedTextHolder();

        PredicateHelper.optionalTooltip(criterion.conditions().player(), EntityPredicateHelper::tooltip, PLAYER_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);

        TriggerDisplayElement element = createElement(criterion.conditions().lootTable(), !textHolder.isEmpty());
        if (!textHolder.isEmpty()) element = element.withTextTooltip(textHolder.getText());

        return new TriggerDisplay(
                TriggerDisplay.makeCountElement(criterion),
                element
        );
    }

    private TriggerDisplayElement createElement(HolderSet<LootTable> lootTables, boolean hasConditions) {
        List<TriggerDisplayElement> elements = new ObjectArrayList<>();
        for (Holder<LootTable> lootTable : lootTables) {
            elements.add(new TextElement(Component.translatable(hasConditions ? TEXT_WITH_CONDITIONS_KEY : TEXT_KEY, lootTable.getRegisteredName())));
        }

        return elements.isEmpty()
                ? new TextElement(hasConditions ? TEXT_ANY_WITH_CONDITIONS : TEXT_ANY)
                : new TimedSwitchingElement(elements);
    }
}
