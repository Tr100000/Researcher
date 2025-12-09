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
import net.minecraft.advancement.criterion.StartedRidingCriterion;
import net.minecraft.text.Text;

public class StartedRidingCriterionHandler implements CriterionHandler<StartedRidingCriterion.Conditions> {
    private static final Text PLAYER_CONDITIONS_HEADER = ModUtils.getScreenTranslated("predicate.player");
    private static final Text TEXT_BEFORE = ModUtils.getScreenTranslated("criterion.started_riding.before");
    private static final Text TEXT_BEFORE_WITH_CONDITIONS = ModUtils.getScreenTranslated("criterion.started_riding.before.with_conditions");
    private static final Text TEXT_AFTER = ModUtils.getScreenTranslated("criterion.started_riding.after");
    private static final Text TEXT_AFTER_WITH_CONDITIONS = ModUtils.getScreenTranslated("criterion.started_riding.after.with_conditions");

    @Override
    public CriterionDisplayElement prepare(ResearchCriterion<StartedRidingCriterion.Conditions> criterion) {
        IndentedTextHolder playerTextHolder = new IndentedTextHolder();
        PredicateHelper.tooltip(criterion.conditions().player(), EntityPredicateHelper::tooltip, PLAYER_CONDITIONS_HEADER)
                .ifPresent(playerTextHolder::accept);

        CriterionDisplayElement beforeText = new TextElement(playerTextHolder.isEmpty() ? TEXT_BEFORE : TEXT_BEFORE_WITH_CONDITIONS);
        CriterionDisplayElement afterText = new TextElement(playerTextHolder.isEmpty() ? TEXT_AFTER : TEXT_AFTER_WITH_CONDITIONS);

        if (!playerTextHolder.isEmpty()) {
            beforeText = beforeText.withTextTooltip(playerTextHolder.getText());
            afterText = afterText.withTextTooltip(playerTextHolder.getText());
        }

        CriterionDisplayElement entityElement = EntityPredicateHelper.vehicleElement(criterion.conditions().player().orElse(null));

        return new CriterionDisplay(
                CriterionDisplay.makeCountElement(criterion),
                beforeText,
                entityElement,
                afterText
        );
    }
}
