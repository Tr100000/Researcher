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
import net.minecraft.advancement.criterion.OnKilledCriterion;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.text.Text;

import java.util.Optional;

public class OnKilledCriterionHandler implements CriterionHandler<OnKilledCriterion.Conditions> {
    public static final OnKilledCriterionHandler PLAYER_KILLED_ENTITY = new OnKilledCriterionHandler(
            ModUtils.getScreenTranslationKey("criterion.player_killed_entity.before"),
            ModUtils.getScreenTranslationKey("criterion.player_killed_entity.after")
    );

    public static final OnKilledCriterionHandler ENTITY_KILLED_PLAYER = new OnKilledCriterionHandler(
            ModUtils.getScreenTranslationKey("criterion.player_killed_entity.before"),
            ModUtils.getScreenTranslationKey("criterion.player_killed_entity.after")
    );

    private final String beforeKey;
    private final String beforeWithConditionsKey;
    private final String afterKey;
    private final String afterWithConditionsKey;

    private static final Text PLAYER_CONDITIONS_HEADER = ModUtils.getScreenTranslated("predicate.player");
    private static final Text KILLING_BLOW_CONDITIONS_HEADER = ModUtils.getScreenTranslated("predicate.kill.damage_source");
    private static final Text ENTITY_CONDITIONS_HEADER = ModUtils.getScreenTranslated("predicate.entity");

    public OnKilledCriterionHandler(String beforeKey, String beforeWithConditionsKey, String afterKey, String afterWithConditionsKey) {
        this.beforeKey = beforeKey;
        this.beforeWithConditionsKey = beforeWithConditionsKey;
        this.afterKey = afterKey;
        this.afterWithConditionsKey = afterWithConditionsKey;
    }

    public OnKilledCriterionHandler(String beforeKey, String afterKey) {
        this(
                beforeKey,
                beforeKey + ".with_conditions",
                afterKey,
                afterKey + ".with_conditions"
        );
    }

    @Override
    public CriterionDisplayElement prepare(ResearchCriterion<OnKilledCriterion.Conditions> criterion) {
        Optional<LootContextPredicate> playerPredicate = criterion.conditions().player();
        Optional<DamageSourcePredicate> killingBlowPredicate = criterion.conditions().killingBlow();
        Optional<LootContextPredicate> entityPredicate = criterion.conditions().entity();

        IndentedTextHolder killConditionTextHolder = new IndentedTextHolder();
        PredicateHelper.tooltip(playerPredicate, EntityPredicateHelper::tooltip, PLAYER_CONDITIONS_HEADER)
                .ifPresent(killConditionTextHolder::accept);
        PredicateHelper.tooltip(killingBlowPredicate, DamagePredicateHelper::tooltip, KILLING_BLOW_CONDITIONS_HEADER)
                .ifPresent(killConditionTextHolder::accept);

        boolean hasKillConditions = !killConditionTextHolder.isEmpty();
        CriterionDisplayElement beforeText = new TextElement(Text.translatable(hasKillConditions ? beforeWithConditionsKey : beforeKey));
        CriterionDisplayElement afterText = new TextElement(Text.translatable(hasKillConditions ? afterWithConditionsKey : afterKey));
        if (hasKillConditions) {
            beforeText = beforeText.withTextTooltip(killConditionTextHolder.getText());
            afterText = afterText.withTextTooltip(killConditionTextHolder.getText());
        }

        CriterionDisplayElement entityElement = EntityPredicateHelper.element(entityPredicate.orElse(null));
        IndentedTextHolder entityConditionTextHolder = new IndentedTextHolder();
        PredicateHelper.tooltip(entityPredicate, EntityPredicateHelper::tooltip, ENTITY_CONDITIONS_HEADER)
                .ifPresent(entityConditionTextHolder::accept);
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
