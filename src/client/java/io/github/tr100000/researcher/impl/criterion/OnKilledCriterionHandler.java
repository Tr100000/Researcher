package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.CriterionDisplay;
import io.github.tr100000.researcher.api.criterion.CriterionDisplayElement;
import io.github.tr100000.researcher.api.criterion.CriterionHandler;
import io.github.tr100000.researcher.api.criterion.element.SpacingElement;
import io.github.tr100000.researcher.api.criterion.element.TextElement;
import io.github.tr100000.researcher.api.criterion.util.DamageSourcePredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.EntityPredicateHelper;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.advancement.criterion.OnKilledCriterion;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.text.Text;

import java.util.Optional;

public class OnKilledCriterionHandler implements CriterionHandler<OnKilledCriterion.Conditions> {
    public static final OnKilledCriterionHandler PLAYER_KILLED_ENTITY = new OnKilledCriterionHandler(
            "criterion.player_killed_entity.before",
            "criterion.player_killed_entity.beforeWithCondition",
            "criterion.player_killed_entity.after",
            "criterion.player_killed_entity.afterWithCondition"
    );

    public static final OnKilledCriterionHandler ENTITY_KILLED_PLAYER = new OnKilledCriterionHandler(
            "criterion.player_killed_entity.before",
            "criterion.player_killed_entity.beforeWithCondition",
            "criterion.player_killed_entity.after",
            "criterion.player_killed_entity.afterWithCondition"
    );

    private final String beforeKey;
    private final String beforeWithConditionsKey;
    private final String afterKey;
    private final String afterWithConditionsKey;

    public OnKilledCriterionHandler(String beforeKey, String beforeWithConditionsKey, String afterKey, String afterWithConditionsKey) {
        this.beforeKey = beforeKey;
        this.beforeWithConditionsKey = beforeWithConditionsKey;
        this.afterKey = afterKey;
        this.afterWithConditionsKey = afterWithConditionsKey;
    }

    @Override
    public CriterionDisplayElement prepare(ResearchCriterion<OnKilledCriterion.Conditions> criterion) {
        Optional<LootContextPredicate> playerPredicate = criterion.conditions().player();
        Optional<DamageSourcePredicate> killingBlowPredicate = criterion.conditions().killingBlow();
        Optional<LootContextPredicate> entityPredicate = criterion.conditions().entity();

        IndentedTextHolder killConditionTextHolder = new IndentedTextHolder();
        playerPredicate.ifPresent(predicate -> {
            killConditionTextHolder.accept(ModUtils.getScreenTranslated("predicate.player"));
            killConditionTextHolder.push();
            EntityPredicateHelper.tooltip(predicate, killConditionTextHolder);
            killConditionTextHolder.pop();
        });
        killingBlowPredicate.ifPresent(predicate -> {
            killConditionTextHolder.accept(ModUtils.getScreenTranslated("predicate.kill.damageSource"));
            killConditionTextHolder.push();
            DamageSourcePredicateHelper.tooltip(predicate, killConditionTextHolder);
            killConditionTextHolder.pop();
        });

        boolean hasKillConditions = !killConditionTextHolder.isEmpty();
        CriterionDisplayElement beforeText = new TextElement(ModUtils.getScreenTranslated(hasKillConditions ? beforeWithConditionsKey : beforeKey));
        if (hasKillConditions) beforeText = beforeText.withTextTooltip(killConditionTextHolder.getText());
        CriterionDisplayElement afterText = new TextElement(ModUtils.getScreenTranslated(hasKillConditions ? afterWithConditionsKey : afterKey));
        if (hasKillConditions) afterText = afterText.withTextTooltip(killConditionTextHolder.getText());

        CriterionDisplayElement entityElement = EntityPredicateHelper.element(entityPredicate.orElse(null));
        IndentedTextHolder entityConditionTextHolder = new IndentedTextHolder();
        EntityPredicateHelper.tooltip(entityPredicate.orElse(null), entityConditionTextHolder);
        if (!entityConditionTextHolder.isEmpty()) {
            entityElement = entityElement.withTextTooltip(entityConditionTextHolder.getText());
        }

        return new CriterionDisplay(
                new SpacingElement(2),
                beforeText,
                new TextElement(Text.literal(criterion.count() + "x")),
                entityElement,
                afterText,
                new SpacingElement(2)
        );
    }
}
