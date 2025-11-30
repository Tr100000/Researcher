package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.CriterionDisplay;
import io.github.tr100000.researcher.api.criterion.CriterionDisplayElement;
import io.github.tr100000.researcher.api.criterion.CriterionHandler;
import io.github.tr100000.researcher.api.criterion.PredicateUtils;
import io.github.tr100000.researcher.api.criterion.element.SpacingElement;
import io.github.tr100000.researcher.api.criterion.element.TextElement;
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
            PredicateUtils.entityTooltip(predicate, killConditionTextHolder);
            killConditionTextHolder.pop();
        });
        killingBlowPredicate.ifPresent(predicate -> {
            killConditionTextHolder.accept(ModUtils.getScreenTranslated("predicate.kill.damageSource"));
            killConditionTextHolder.push();
            PredicateUtils.damageSourceTooltip(predicate, killConditionTextHolder);
            killConditionTextHolder.pop();
        });

        boolean hasKillConditions = !killConditionTextHolder.isEmpty();
        CriterionDisplayElement beforeText = new TextElement(ModUtils.getScreenTranslated(hasKillConditions ? beforeWithConditionsKey : beforeKey));
        if (hasKillConditions) beforeText = beforeText.withTextTooltip(killConditionTextHolder.getText());
        CriterionDisplayElement afterText = new TextElement(ModUtils.getScreenTranslated(hasKillConditions ? afterWithConditionsKey : afterKey));
        if (hasKillConditions) afterText = afterText.withTextTooltip(killConditionTextHolder.getText());

        // This handles condition tooltips for us
        CriterionDisplayElement entityElement = PredicateUtils.entityElement(entityPredicate.orElse(null), true);

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
