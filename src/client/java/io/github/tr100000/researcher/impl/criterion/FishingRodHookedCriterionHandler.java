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
import net.minecraft.advancement.criterion.FishingRodHookedCriterion;
import net.minecraft.text.Text;

import java.util.Optional;

public class FishingRodHookedCriterionHandler implements CriterionHandler<FishingRodHookedCriterion.Conditions> {
    private static final Text ROD_CONDITIONS_HEADER = ModUtils.getScreenTranslated("criterion.fishing_rod_hooked.rod");
    private static final Text ENTITY_CONDITIONS_HEADER = ModUtils.getScreenTranslated("criterion.fishing_rod_hooked.entity");
    private static final Text PLAYER_CONDITIONS_HEADER = ModUtils.getScreenTranslated("predicate.player");
    private static final Text TEXT = ModUtils.getScreenTranslated("criterion.fishing_rod_hooked");
    private static final Text TEXT_WITH_CONDITIONS = ModUtils.getScreenTranslated("criterion.fishing_rod_hooked.with_conditions");
    private static final Text TEXT_SPECIFIC_BEFORE = ModUtils.getScreenTranslated("criterion.fishing_rod_hooked.specific.before");
    private static final Text TEXT_SPECIFIC_BEFORE_WITH_CONDITIONS = ModUtils.getScreenTranslated("criterion.fishing_rod_hooked.specific.with_conditions.before");
    private static final Text TEXT_SPECIFIC_AFTER = ModUtils.getScreenTranslated("criterion.fishing_rod_hooked.specific.after");
    private static final Text TEXT_SPECIFIC_AFTER_WITH_CONDITIONS = ModUtils.getScreenTranslated("criterion.fishing_rod_hooked.specific.with_conditions.after");

    @Override
    public CriterionDisplayElement prepare(ResearchCriterion<FishingRodHookedCriterion.Conditions> criterion) {
        IndentedTextHolder textHolder = new IndentedTextHolder();

        PredicateHelper.tooltip(criterion.conditions().rod(), ItemPredicateHelper::tooltip, ROD_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);
        PredicateHelper.tooltip(criterion.conditions().entity(), EntityPredicateHelper::tooltip, ENTITY_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);
        PredicateHelper.tooltip(criterion.conditions().player(), EntityPredicateHelper::tooltip, PLAYER_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);

        Optional<CriterionDisplayElement> itemElement = criterion.conditions().item().flatMap(ItemPredicateHelper::element);

        if (itemElement.isPresent()) {
            CriterionDisplayElement beforeElement = new TextElement(textHolder.isEmpty() ? TEXT_SPECIFIC_BEFORE : TEXT_SPECIFIC_BEFORE_WITH_CONDITIONS);
            CriterionDisplayElement afterElement = new TextElement(textHolder.isEmpty() ? TEXT_SPECIFIC_AFTER : TEXT_SPECIFIC_AFTER_WITH_CONDITIONS);
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
            CriterionDisplayElement element = new TextElement(textHolder.isEmpty() ? TEXT : TEXT_WITH_CONDITIONS);
            if (!textHolder.isEmpty()) element = element.withTextTooltip(textHolder.getText());

            return new CriterionDisplay(
                    CriterionDisplay.makeCountElement(criterion),
                    element
            );
        }
    }
}
