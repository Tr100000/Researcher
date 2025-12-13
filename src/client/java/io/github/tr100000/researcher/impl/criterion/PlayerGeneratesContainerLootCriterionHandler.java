package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.CriterionDisplay;
import io.github.tr100000.researcher.api.criterion.CriterionDisplayElement;
import io.github.tr100000.researcher.api.criterion.CriterionHandler;
import io.github.tr100000.researcher.api.criterion.element.TextElement;
import io.github.tr100000.researcher.api.criterion.util.EntityPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.PredicateHelper;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.advancement.criterion.PlayerGeneratesContainerLootCriterion;
import net.minecraft.text.Text;

public class PlayerGeneratesContainerLootCriterionHandler implements CriterionHandler<PlayerGeneratesContainerLootCriterion.Conditions> {
    private static final Text PLAYER_CONDITIONS_HEADER = ModUtils.getScreenTranslated("predicate.player");
    private static final String TEXT = ModUtils.getScreenTranslationKey("criterion.player_generates_container_loot");
    private static final String TEXT_WITH_CONDITIONS = ModUtils.getScreenTranslationKey("criterion.player_generates_container_loot");

    @Override
    public CriterionDisplayElement prepare(ResearchCriterion<PlayerGeneratesContainerLootCriterion.Conditions> criterion) {
        IndentedTextHolder textHolder = new IndentedTextHolder();

        PredicateHelper.optionalTooltip(criterion.conditions().player(), EntityPredicateHelper::tooltip, PLAYER_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);

        CriterionDisplayElement element = new TextElement(Text.translatable(textHolder.isEmpty() ? TEXT : TEXT_WITH_CONDITIONS, criterion.conditions().lootTable().getValue()));
        if (!textHolder.isEmpty()) element = element.withTextTooltip(textHolder.getText());

        return new CriterionDisplay(
                CriterionDisplay.makeCountElement(criterion),
                element
        );
    }
}
