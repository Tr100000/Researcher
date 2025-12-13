package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.CriterionDisplay;
import io.github.tr100000.researcher.api.criterion.CriterionDisplayElement;
import io.github.tr100000.researcher.api.criterion.CriterionHandler;
import io.github.tr100000.researcher.api.criterion.element.GroupedElement;
import io.github.tr100000.researcher.api.criterion.element.TextElement;
import io.github.tr100000.researcher.api.criterion.util.EntityPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.PredicateHelper;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.advancement.criterion.SummonedEntityCriterion;
import net.minecraft.text.Text;

public class SummonedEntityCriterionHandler implements CriterionHandler<SummonedEntityCriterion.Conditions> {
    private static final Text PLAYER_CONDITIONS_HEADER = ModUtils.getScreenTranslated("predicate.player");
    private static final Text ENTITY_CONDITIONS_HEADER = ModUtils.getScreenTranslated("criterion.summoned_entity.spawned");
    private static final Text TEXT_BEFORE = ModUtils.getScreenTranslated("criterion.summoned_entity.before");
    private static final Text TEXT_BEFORE_WITH_CONDITIONS = ModUtils.getScreenTranslated("criterion.summoned_entity.before.with_conditions");
    private static final Text TEXT_AFTER = ModUtils.getScreenTranslated("criterion.summoned_entity.after");
    private static final Text TEXT_AFTER_WITH_CONDITIONS = ModUtils.getScreenTranslated("criterion.summoned_entity.after.with_conditions");

    @Override
    public CriterionDisplayElement prepare(ResearchCriterion<SummonedEntityCriterion.Conditions> criterion) {
        IndentedTextHolder playerTextHolder = new IndentedTextHolder();
        PredicateHelper.optionalTooltip(criterion.conditions().player(), EntityPredicateHelper::tooltip, PLAYER_CONDITIONS_HEADER)
                .ifPresent(playerTextHolder::accept);

        CriterionDisplayElement beforeText = new TextElement(playerTextHolder.isEmpty() ? TEXT_BEFORE : TEXT_BEFORE_WITH_CONDITIONS);
        CriterionDisplayElement afterText = new TextElement(playerTextHolder.isEmpty() ? TEXT_AFTER : TEXT_AFTER_WITH_CONDITIONS);

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
