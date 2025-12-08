package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.util.ItemPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.PredicateHelper;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.advancement.criterion.ItemCriterion;
import net.minecraft.block.Block;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.condition.MatchToolLootCondition;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;

import java.util.Optional;

public class ItemCriterionHandler extends AbstractCriterionHandler<ItemCriterion.Conditions> {
    public static final ItemCriterionHandler PLACED_BLOCK = new ItemCriterionHandler(ModUtils.getScreenTranslationKey("criterion.placed_block"));
    public static final ItemCriterionHandler ITEM_USED_ON_BLOCK = new ItemCriterionHandler(ModUtils.getScreenTranslationKey("criterion.item_used_on_block"));

    private static final String BLOCK_TEXT = ModUtils.getScreenTranslationKey("criterion.item.block");
    private static final Text BLOCK_PROPERTIES_TEXT = ModUtils.getScreenTranslated("criterion.item.block_properties");
    private static final Text TOOL_TEXT = ModUtils.getScreenTranslated("criterion.item.tool");

    public ItemCriterionHandler(String key) {
        super(key);
    }

    @Override
    protected void fillTooltip(ResearchCriterion<ItemCriterion.Conditions> criterion, IndentedTextHolder textHolder) {
        PredicateHelper.tooltip(criterion.conditions().location(), ItemCriterionHandler::locationTooltip, null)
                .ifPresent(textHolder::accept);
        playerTooltip(criterion, textHolder);
    }

    public static void locationTooltip(LootContextPredicate predicate, IndentedTextHolder textHolder) {
        predicate.conditions.forEach(condition -> {
            if (condition instanceof BlockStatePropertyLootCondition(RegistryEntry<Block> block, Optional<StatePredicate> properties)) {
                textHolder.accept(Text.translatable(BLOCK_TEXT, block.getKey().orElseThrow().getValue()));
                PredicateHelper.tooltip(properties, PredicateHelper::stateTooltip, BLOCK_PROPERTIES_TEXT)
                        .ifPresent(textHolder::accept);
            }
            else if (condition instanceof MatchToolLootCondition(Optional<ItemPredicate> toolItem)) {
                PredicateHelper.tooltip(toolItem, ItemPredicateHelper::tooltip, TOOL_TEXT)
                        .ifPresent(textHolder::accept);
            }
        });
    }
}
