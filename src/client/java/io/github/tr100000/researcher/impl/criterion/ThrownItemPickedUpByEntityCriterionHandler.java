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
import net.minecraft.advancement.criterion.ThrownItemPickedUpByEntityCriterion;
import net.minecraft.text.Text;

import java.util.Optional;

public class ThrownItemPickedUpByEntityCriterionHandler implements CriterionHandler<ThrownItemPickedUpByEntityCriterion.Conditions> {
    public static final ThrownItemPickedUpByEntityCriterionHandler THROWN_ITEM_PICKED_UP_BY_ENTITY = new ThrownItemPickedUpByEntityCriterionHandler(
            ModUtils.getScreenTranslationKey("criterion.thrown_item_picked_up_by_entity"),
            ModUtils.getScreenTranslationKey("criterion.thrown_item_picked_up_by_entity.specific.before"),
            ModUtils.getScreenTranslationKey("criterion.thrown_item_picked_up_by_entity.specific.after"),
            ModUtils.getScreenTranslationKey("criterion.thrown_item_picked_up_by_entity.entity")
    );

    public static final ThrownItemPickedUpByEntityCriterionHandler THROWN_ITEM_PICKED_UP_BY_PLAYER = new ThrownItemPickedUpByEntityCriterionHandler(
            ModUtils.getScreenTranslationKey("criterion.thrown_item_picked_up_by_player"),
            ModUtils.getScreenTranslationKey("criterion.thrown_item_picked_up_by_player.specific.before"),
            ModUtils.getScreenTranslationKey("criterion.thrown_item_picked_up_by_player.specific.after"),
            ModUtils.getScreenTranslationKey("criterion.thrown_item_picked_up_by_player.entity")
    );

    private static final Text ITEM_CONDITIONS_HEADER = ModUtils.getScreenTranslated("predicate.item");
    private static final Text PLAYER_CONDITIONS_HEADER = ModUtils.getScreenTranslated("predicate.player");

    private final Text entityConditionsHeader;
    private final Text text;
    private final Text textWithConditions;
    private final Text textSpecificBefore;
    private final Text textSpecificBeforeWithConditions;
    private final Text textSpecificAfter;
    private final Text textSpecificAfterWithConditions;

    public ThrownItemPickedUpByEntityCriterionHandler(String textKey, String textSpecificBeforeKey, String textSpecificAfterKey, String entityConditionsHeaderKey) {
        this.entityConditionsHeader = Text.translatable(entityConditionsHeaderKey);
        this.text = Text.translatable(textKey);
        this.textWithConditions = Text.translatable(textKey + ".with_conditions");
        this.textSpecificBefore = Text.translatable(textSpecificBeforeKey);
        this.textSpecificBeforeWithConditions = Text.translatable(textSpecificBeforeKey + ".with_conditions");
        this.textSpecificAfter = Text.translatable(textSpecificAfterKey);
        this.textSpecificAfterWithConditions = Text.translatable(textSpecificAfterKey + ".with_conditions");
    }

    @Override
    public CriterionDisplayElement prepare(ResearchCriterion<ThrownItemPickedUpByEntityCriterion.Conditions> criterion) {
        IndentedTextHolder textHolder = new IndentedTextHolder();

        PredicateHelper.tooltip(criterion.conditions().item(), ItemPredicateHelper::tooltip, ITEM_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);
        PredicateHelper.tooltip(criterion.conditions().entity(), EntityPredicateHelper::tooltip, entityConditionsHeader)
                .ifPresent(textHolder::accept);
        PredicateHelper.tooltip(criterion.conditions().player(), EntityPredicateHelper::tooltip, PLAYER_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);

        Optional<CriterionDisplayElement> itemElement = criterion.conditions().item().flatMap(ItemPredicateHelper::element);

        if (itemElement.isPresent()) {
            CriterionDisplayElement beforeElement = new TextElement(textHolder.isEmpty() ? textSpecificBefore : textSpecificBeforeWithConditions);
            CriterionDisplayElement afterElement = new TextElement(textHolder.isEmpty() ? textSpecificAfter : textSpecificAfterWithConditions);
            if (!textHolder.isEmpty()) {
                beforeElement = beforeElement.withTextTooltip(textHolder.getText());
                afterElement = afterElement.withTextTooltip(textHolder.getText());
            }

            return new CriterionDisplay(
                    CriterionDisplay.makeCountElement(criterion),
                    beforeElement,
                    itemElement.get(),
                    afterElement
            );
        }
        else {
            CriterionDisplayElement element = new TextElement(textHolder.isEmpty() ? text : textWithConditions);
            if (!textHolder.isEmpty()) element = element.withTextTooltip(textHolder.getText());

            return new CriterionDisplay(
                    CriterionDisplay.makeCountElement(criterion),
                    element
            );
        }
    }
}
