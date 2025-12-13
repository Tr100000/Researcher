package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.CriterionDisplay;
import io.github.tr100000.researcher.api.criterion.CriterionDisplayElement;
import io.github.tr100000.researcher.api.criterion.CriterionHandler;
import io.github.tr100000.researcher.api.criterion.element.GroupedElement;
import io.github.tr100000.researcher.api.criterion.element.TextElement;
import io.github.tr100000.researcher.api.criterion.util.EntityPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.ItemPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.PredicateHelper;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.advancement.criterion.PlayerInteractedWithEntityCriterion;
import net.minecraft.text.Text;

public class PlayerInteractedWithEntityCriterionHandler implements CriterionHandler<PlayerInteractedWithEntityCriterion.Conditions> {
    public static final PlayerInteractedWithEntityCriterionHandler PLAYER_INTERACTED_WITH_ENTITY = new PlayerInteractedWithEntityCriterionHandler(
            ModUtils.getScreenTranslationKey("criterion.player_interacted_with_entity.before"),
            ModUtils.getScreenTranslationKey("criterion.player_interacted_with_entity.after"),
            ModUtils.getScreenTranslationKey("criterion.player_interacted_with_entity.item")
    );

    public static final PlayerInteractedWithEntityCriterionHandler PLAYER_SHEARED_EQUIPMENT = new PlayerInteractedWithEntityCriterionHandler(
            ModUtils.getScreenTranslationKey("criterion.player_sheared_equipment.before"),
            ModUtils.getScreenTranslationKey("criterion.player_sheared_equipment.after"),
            ModUtils.getScreenTranslationKey("criterion.player_sheared_equipment.item")
    );

    private static final Text PLAYER_CONDITIONS_HEADER = ModUtils.getScreenTranslated("predicate.player");
    private static final Text ENTITY_CONDITIONS_HEADER = ModUtils.getScreenTranslated("criterion.summoned_entity.spawned");

    private final Text itemConditionsHeader;
    private final Text textBefore;
    private final Text textBeforeWithConditions;
    private final Text textAfter;
    private final Text textAfterWithConditions;

    public PlayerInteractedWithEntityCriterionHandler(String textBeforeKey, String textAfterKey, String itemConditionsHeaderKey) {
        itemConditionsHeader = Text.translatable(itemConditionsHeaderKey);
        textBefore = Text.translatable(textBeforeKey);
        textBeforeWithConditions = Text.translatable(textBeforeKey + ".with_conditions");
        textAfter = Text.translatable(textAfterKey);
        textAfterWithConditions = Text.translatable(textAfterKey + ".with_conditions");
    }

    @Override
    public CriterionDisplayElement prepare(ResearchCriterion<PlayerInteractedWithEntityCriterion.Conditions> criterion) {
        IndentedTextHolder playerTextHolder = new IndentedTextHolder();
        PredicateHelper.optionalTooltip(criterion.conditions().item(), ItemPredicateHelper::tooltip, itemConditionsHeader)
                .ifPresent(playerTextHolder::accept);
        PredicateHelper.optionalTooltip(criterion.conditions().player(), EntityPredicateHelper::tooltip, PLAYER_CONDITIONS_HEADER)
                .ifPresent(playerTextHolder::accept);

        CriterionDisplayElement beforeText = new TextElement(playerTextHolder.isEmpty() ? textBefore : textBeforeWithConditions);
        CriterionDisplayElement afterText = new TextElement(playerTextHolder.isEmpty() ? textAfter : textAfterWithConditions);

        if (!playerTextHolder.isEmpty()) {
            beforeText = beforeText.withTextTooltip(playerTextHolder.getText());
            afterText = afterText.withTextTooltip(playerTextHolder.getText());
        }

        IndentedTextHolder entityTextHolder = new IndentedTextHolder();
        PredicateHelper.optionalTooltip(criterion.conditions().entity(), EntityPredicateHelper::tooltip, ENTITY_CONDITIONS_HEADER)
                .ifPresent(entityTextHolder::accept);

        CriterionDisplayElement entityElement = EntityPredicateHelper.element(criterion.conditions().entity().orElse(null));
        if (!entityTextHolder.isEmpty()) {
            entityElement = new GroupedElement(entityElement, new TextElement(Text.literal("*")));
            entityElement = entityElement.withTextTooltip(entityTextHolder.getText());
        }

        return new CriterionDisplay(
                CriterionDisplay.makeCountElement(criterion),
                beforeText,
                entityElement,
                afterText
        );
    }
}
