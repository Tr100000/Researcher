package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.CriterionDisplay;
import io.github.tr100000.researcher.api.criterion.CriterionDisplayElement;
import io.github.tr100000.researcher.api.criterion.CriterionHandler;
import io.github.tr100000.researcher.api.criterion.element.TextElement;
import io.github.tr100000.researcher.api.criterion.util.DamagePredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.EntityPredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.PredicateHelper;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.advancement.criterion.PlayerHurtEntityCriterion;
import net.minecraft.predicate.DamagePredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.text.Text;

import java.util.Optional;

public class PlayerHurtEntityCriterionHandler implements CriterionHandler<PlayerHurtEntityCriterion.Conditions> {
    private static final Text PLAYER_CONDITIONS_HEADER = ModUtils.getScreenTranslated("predicate.player");
    private static final Text DAMAGE_CONDITIONS_HEADER = ModUtils.getScreenTranslated("predicate.damage");
    private static final Text ENTITY_CONDITIONS_HEADER = ModUtils.getScreenTranslated("predicate.entity");

    private static final Text BEFORE_TEXT = ModUtils.getScreenTranslated("predicate.player_hurt_entity.before");
    private static final Text BEFORE_WITH_CONDITIONS_TEXT = ModUtils.getScreenTranslated("predicate.player_hurt_entity.before_with_conditions");
    private static final Text AFTER_TEXT = ModUtils.getScreenTranslated("predicate.player_hurt_entity.after");
    private static final Text AFTER_WITH_CONDITIONS_TEXT = ModUtils.getScreenTranslated("predicate.player_hurt_entity.after_with_conditions");

    @Override
    public CriterionDisplayElement prepare(ResearchCriterion<PlayerHurtEntityCriterion.Conditions> criterion) {
        Optional<LootContextPredicate> playerPredicate = criterion.conditions().player();
        Optional<DamagePredicate> damagePredicate = criterion.conditions().damage();
        Optional<LootContextPredicate> entityPredicate = criterion.conditions().entity();

        IndentedTextHolder conditionTextHolder = new IndentedTextHolder();
        PredicateHelper.tooltip(playerPredicate, EntityPredicateHelper::tooltip, PLAYER_CONDITIONS_HEADER)
                .ifPresent(conditionTextHolder::accept);
        PredicateHelper.tooltip(damagePredicate, DamagePredicateHelper::tooltip, DAMAGE_CONDITIONS_HEADER)
                .ifPresent(conditionTextHolder::accept);

        CriterionDisplayElement beforeText = new TextElement(conditionTextHolder.isEmpty() ? BEFORE_TEXT : BEFORE_WITH_CONDITIONS_TEXT);
        CriterionDisplayElement afterText = new TextElement(conditionTextHolder.isEmpty() ? AFTER_TEXT : AFTER_WITH_CONDITIONS_TEXT);
        if (!conditionTextHolder.isEmpty()) {
            beforeText = beforeText.withTextTooltip(conditionTextHolder.getText());
            afterText = afterText.withTextTooltip(conditionTextHolder.getText());
        }

        IndentedTextHolder entityConditionTextHolder = new IndentedTextHolder();
        PredicateHelper.tooltip(entityPredicate, EntityPredicateHelper::tooltip, ENTITY_CONDITIONS_HEADER)
                .ifPresent(entityConditionTextHolder::accept);

        CriterionDisplayElement entityElement = EntityPredicateHelper.element(entityPredicate.orElse(null));
        if (!entityConditionTextHolder.isEmpty()) {
            entityElement = entityElement.withTextTooltip(entityConditionTextHolder.getText());
        }

        return new CriterionDisplay(
                CriterionDisplay.makeCountElement(criterion),
                beforeText,
                entityElement,
                afterText
        );
    }
}
