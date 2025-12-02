package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.CriterionDisplay;
import io.github.tr100000.researcher.api.criterion.CriterionDisplayElement;
import io.github.tr100000.researcher.api.criterion.CriterionHandler;
import io.github.tr100000.researcher.api.criterion.element.TextElement;
import io.github.tr100000.researcher.api.criterion.util.DamageSourcePredicateHelper;
import io.github.tr100000.researcher.api.criterion.util.EntityPredicateHelper;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.advancement.criterion.OnKilledCriterion;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.text.Text;

import java.util.List;
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
            killConditionTextHolder.accept(ModUtils.getScreenTranslated("predicate.kill.damage_source"));
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
        entityConditionTextHolder.push();
        EntityPredicateHelper.tooltip(entityPredicate.orElse(null), entityConditionTextHolder);
        if (!entityConditionTextHolder.isEmpty()) {
            List<Text> text = new ObjectArrayList<>();
            text.add(ModUtils.getScreenTranslated("predicate.entity"));
            text.addAll(entityConditionTextHolder.getText());
            entityElement = entityElement.withTextTooltip(text);
        }

        return new CriterionDisplay(
                new TextElement(Text.literal(criterion.count() + "x")),
                beforeText,
                entityElement,
                afterText
        );
    }
}
