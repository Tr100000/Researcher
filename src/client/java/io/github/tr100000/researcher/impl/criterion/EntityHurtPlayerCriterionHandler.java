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
import net.minecraft.advancement.criterion.EntityHurtPlayerCriterion;
import net.minecraft.predicate.DamagePredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.text.Text;

import java.util.Optional;

public class EntityHurtPlayerCriterionHandler implements CriterionHandler<EntityHurtPlayerCriterion.Conditions> {
    private static final Text PLAYER_CONDITIONS_HEADER = ModUtils.getScreenTranslated("predicate.player");
    private static final Text DAMAGE_CONDITIONS_HEADER = ModUtils.getScreenTranslated("predicate.damage");

    private static final Text TEXT = ModUtils.getScreenTranslated("predicate.entity_hurt_player");
    private static final Text WITH_CONDITIONS_TEXT = ModUtils.getScreenTranslated("predicate.entity_hurt_player.with_conditions");

    @Override
    public CriterionDisplayElement prepare(ResearchCriterion<EntityHurtPlayerCriterion.Conditions> criterion) {
        Optional<LootContextPredicate> playerPredicate = criterion.conditions().player();
        Optional<DamagePredicate> damagePredicate = criterion.conditions().damage();

        IndentedTextHolder conditionTextHolder = new IndentedTextHolder();
        PredicateHelper.tooltip(playerPredicate, EntityPredicateHelper::tooltip, PLAYER_CONDITIONS_HEADER)
                .ifPresent(conditionTextHolder::accept);
        PredicateHelper.tooltip(damagePredicate, DamagePredicateHelper::tooltip, DAMAGE_CONDITIONS_HEADER)
                .ifPresent(conditionTextHolder::accept);

        CriterionDisplayElement textElement = new TextElement(conditionTextHolder.isEmpty() ? TEXT : WITH_CONDITIONS_TEXT);
        if (!conditionTextHolder.isEmpty()) {
            textElement = textElement.withTextTooltip(conditionTextHolder.getText());
        }

        return new CriterionDisplay(
                CriterionDisplay.getCountElement(criterion),
                textElement
        );
    }
}
