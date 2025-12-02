package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.CriterionDisplay;
import io.github.tr100000.researcher.api.criterion.CriterionDisplayElement;
import io.github.tr100000.researcher.api.criterion.CriterionHandler;
import io.github.tr100000.researcher.api.criterion.element.TextElement;
import io.github.tr100000.researcher.api.criterion.util.EntityPredicateHelper;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.advancement.criterion.TickCriterion;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.text.Text;

import java.util.Optional;

public class TickCriterionHandler implements CriterionHandler<TickCriterion.Conditions> {
    public static final TickCriterionHandler TICK = new TickCriterionHandler(
            "screen.researcher.criterion.tick.one",
            "screen.researcher.criterion.tick.one_with_conditions",
            "screen.researcher.criterion.tick.many",
            "screen.researcher.criterion.tick.many_with_conditions",
            true
    );

    public static final TickCriterionHandler SLEPT_IN_BED = new TickCriterionHandler(
            "screen.researcher.criterion.slept_in_bed.one",
            "screen.researcher.criterion.slept_in_bed.one_with_conditions",
            "screen.researcher.criterion.slept_in_bed.many",
            "screen.researcher.criterion.slept_in_bed.many_with_conditions",
            false
    );

    public static final TickCriterionHandler HERO_OF_THE_VILLAGE = new TickCriterionHandler(
            "screen.researcher.criterion.hero_of_the_village.one",
            "screen.researcher.criterion.hero_of_the_village.one_with_conditions",
            "screen.researcher.criterion.hero_of_the_village.many",
            "screen.researcher.criterion.hero_of_the_village.many_with_conditions",
            false
    );

    public static final TickCriterionHandler VOLUNTARY_EXILE = new TickCriterionHandler(
            "screen.researcher.criterion.voluntary_exile.one",
            "screen.researcher.criterion.voluntary_exile.one_with_conditions",
            "screen.researcher.criterion.voluntary_exile.many",
            "screen.researcher.criterion.voluntary_exile.many_with_conditions",
            false
    );

    public static final TickCriterionHandler AVOID_VIBRATION = new TickCriterionHandler(
            "screen.researcher.criterion.avoid_vibration.one",
            "screen.researcher.criterion.avoid_vibration.one_with_conditions",
            "screen.researcher.criterion.avoid_vibration.many",
            "screen.researcher.criterion.avoid_vibration.many_with_conditions",
            false
    );

    private final String oneTextKey;
    private final String oneWithConditionsTextKey;
    private final String manyTextKey;
    private final String manyWithConditionsTextKey;
    private final boolean countIsTime;

    public TickCriterionHandler(String oneTextKey, String oneWithConditionsTextKey, String manyTextKey, String manyWithConditionsTextKey, boolean countIsTime) {
        this.oneTextKey = oneTextKey;
        this.oneWithConditionsTextKey = oneWithConditionsTextKey;
        this.manyTextKey = manyTextKey;
        this.manyWithConditionsTextKey = manyWithConditionsTextKey;
        this.countIsTime = countIsTime;
    }

    @Override
    public CriterionDisplayElement prepare(ResearchCriterion<TickCriterion.Conditions> criterion) {
        boolean hasConditions = false;
        Optional<LootContextPredicate> playerPredicate = criterion.conditions().player();
        IndentedTextHolder textHolder = new IndentedTextHolder();
        if (playerPredicate.isPresent()) {
            textHolder.accept(ModUtils.getScreenTranslated("predicate.player"));
            textHolder.push();
            EntityPredicateHelper.tooltip(playerPredicate.get(), textHolder);
            textHolder.pop();

            if (textHolder.count() > 1) {
                hasConditions = true;
            }
        }

        CriterionDisplayElement element;
        if (criterion.count() == 1) {
            element = new TextElement(Text.translatable(hasConditions ? oneWithConditionsTextKey : oneTextKey));
        }
        else {
            element = new TextElement(Text.translatable(hasConditions ? manyWithConditionsTextKey : manyTextKey, criterion.count() * (countIsTime ? 20 : 1)));
        }

        if (hasConditions) {
            element = element.withTextTooltip(textHolder.getText());
        }

        return new CriterionDisplay(
                element
        );
    }
}
