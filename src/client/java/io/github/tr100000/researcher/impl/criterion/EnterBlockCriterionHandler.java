package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.CriterionDisplay;
import io.github.tr100000.researcher.api.criterion.CriterionDisplayElement;
import io.github.tr100000.researcher.api.criterion.CriterionHandler;
import io.github.tr100000.researcher.api.criterion.element.GroupedElement;
import io.github.tr100000.researcher.api.criterion.element.TextElement;
import io.github.tr100000.researcher.api.criterion.util.BlockPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.EntityPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.PredicateHelper;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.advancement.criterion.EnterBlockCriterion;
import net.minecraft.block.Block;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;

import java.util.Optional;

public class EnterBlockCriterionHandler implements CriterionHandler<EnterBlockCriterion.Conditions> {
    private static final Text BEFORE_KEY = ModUtils.getScreenTranslated("criterion.enter_block.before");
    private static final Text BEFORE_WITH_CONDITIONS_KEY = ModUtils.getScreenTranslated("criterion.enter_block.before_with_conditions");
    private static final Text AFTER_KEY = ModUtils.getScreenTranslated("criterion.enter_block.after");
    private static final Text AFTER_WITH_CONDITIONS = ModUtils.getScreenTranslated("criterion.enter_block.after_with_conditions");
    private static final Text ANY_BLOCK = ModUtils.getScreenTranslated("criterion.enter_block.any");

    private static final Text PLAYER_CONDITIONS_HEADER = ModUtils.getScreenTranslated("predicate.player");
    private static final Text BLOCK_CONDITIONS_HEADER = ModUtils.getScreenTranslated("predicate.block");

    @Override
    public CriterionDisplayElement prepare(ResearchCriterion<EnterBlockCriterion.Conditions> criterion) {
        Optional<LootContextPredicate> playerPredicate = criterion.conditions().player();
        Optional<RegistryEntry<Block>> blockPredicate = criterion.conditions().block();
        Optional<StatePredicate> statePredicate = criterion.conditions().state();

        boolean playerHasConditions = false;
        IndentedTextHolder playerConditionTextHolder = new IndentedTextHolder();
        if (playerPredicate.isPresent()) {
            playerConditionTextHolder.accept(PLAYER_CONDITIONS_HEADER);
            playerConditionTextHolder.push();
            EntityPredicateHelper.tooltip(playerPredicate.get(), playerConditionTextHolder);
            playerConditionTextHolder.pop();
            if (playerConditionTextHolder.count() > 1) playerHasConditions = true;
        }

        CriterionDisplayElement textBefore = new TextElement(playerHasConditions ? BEFORE_KEY : BEFORE_WITH_CONDITIONS_KEY);
        CriterionDisplayElement textAfter = new TextElement(playerHasConditions ? AFTER_KEY : AFTER_WITH_CONDITIONS);

        if (playerHasConditions) {
            textBefore = textBefore.withTextTooltip(playerConditionTextHolder.getText());
            textAfter = textAfter.withTextTooltip(playerConditionTextHolder.getText());
        }

        boolean blockHasConditions = false;
        IndentedTextHolder blockConditionTextHolder = new IndentedTextHolder();
        if (statePredicate.isPresent()) {
            blockConditionTextHolder.accept(BLOCK_CONDITIONS_HEADER);
            blockConditionTextHolder.push();
            PredicateHelper.stateTooltip(statePredicate.get(), blockConditionTextHolder);
            blockConditionTextHolder.pop();
            if (blockConditionTextHolder.count() > 1) blockHasConditions = true;
        }

        CriterionDisplayElement block = blockPredicate.map(BlockPredicateHelper::element).orElseGet(() -> new TextElement(ANY_BLOCK));

        if (blockHasConditions) {
            block = new GroupedElement(block, new TextElement(Text.literal("*"))).withTextTooltip(blockConditionTextHolder.getText());
        }

        return new CriterionDisplay(
                new TextElement(Text.literal(criterion.count() + "x")),
                textBefore,
                block,
                textAfter
        );
    }
}
