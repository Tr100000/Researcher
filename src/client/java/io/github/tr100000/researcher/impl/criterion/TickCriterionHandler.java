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
import net.minecraft.advancement.criterion.TickCriterion;
import net.minecraft.text.Text;

public class TickCriterionHandler implements CriterionHandler<TickCriterion.Conditions> {
    public static final TickCriterionHandler LOCATION = new TickCriterionHandler(
            ModUtils.getScreenTranslationKey("criterion.location.one"),
            ModUtils.getScreenTranslationKey("criterion.location.many"),
            false
    );

    public static final TickCriterionHandler TICK = new TickCriterionHandler(
            ModUtils.getScreenTranslationKey("criterion.tick.one"),
            ModUtils.getScreenTranslationKey("criterion.tick.many"),
            true
    );

    public static final TickCriterionHandler SLEPT_IN_BED = new TickCriterionHandler(
            ModUtils.getScreenTranslationKey("criterion.slept_in_bed.one"),
            ModUtils.getScreenTranslationKey("criterion.slept_in_bed.many"),
            false
    );

    public static final TickCriterionHandler HERO_OF_THE_VILLAGE = new TickCriterionHandler(
            ModUtils.getScreenTranslationKey("criterion.hero_of_the_village.one"),
            ModUtils.getScreenTranslationKey("criterion.hero_of_the_village.many"),
            false
    );

    public static final TickCriterionHandler VOLUNTARY_EXILE = new TickCriterionHandler(
            ModUtils.getScreenTranslationKey("criterion.voluntary_exile.one"),
            ModUtils.getScreenTranslationKey("criterion.voluntary_exile.many"),
            false
    );

    public static final TickCriterionHandler AVOID_VIBRATION = new TickCriterionHandler(
            ModUtils.getScreenTranslationKey("criterion.avoid_vibration.one"),
            ModUtils.getScreenTranslationKey("criterion.avoid_vibration.many"),
            false
    );

    private static final Text PLAYER_CONDITIONS_HEADER = ModUtils.getScreenTranslated("predicate.player");

    private final String oneTextKey;
    private final String oneWithConditionsTextKey;
    private final String manyTextKey;
    private final String manyWithConditionsTextKey;
    private final boolean countIsTicks;

    public TickCriterionHandler(String oneTextKey, String oneWithConditionsTextKey, String manyTextKey, String manyWithConditionsTextKey, boolean countIsTicks) {
        this.oneTextKey = oneTextKey;
        this.oneWithConditionsTextKey = oneWithConditionsTextKey;
        this.manyTextKey = manyTextKey;
        this.manyWithConditionsTextKey = manyWithConditionsTextKey;
        this.countIsTicks = countIsTicks;
    }

    public TickCriterionHandler(String oneTextKey, String manyTextKey, boolean countIsTicks) {
        this(
                oneTextKey,
                oneTextKey + ".with_conditions",
                manyTextKey,
                manyTextKey + ".with_conditions",
                countIsTicks
        );
    }

    @Override
    public CriterionDisplayElement prepare(ResearchCriterion<TickCriterion.Conditions> criterion) {
        IndentedTextHolder textHolder = new IndentedTextHolder();
        PredicateHelper.optionalTooltip(criterion.conditions().player(), EntityPredicateHelper::tooltip, PLAYER_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);

        CriterionDisplayElement element;
        if (criterion.count() == 1) {
            element = new TextElement(Text.translatable(textHolder.isEmpty() ? oneTextKey : oneWithConditionsTextKey));
        }
        else {
            element = new TextElement(Text.translatable(textHolder.isEmpty() ? manyTextKey : manyWithConditionsTextKey, criterion.count() * (countIsTicks ? 20 : 1)));
        }

        if (!textHolder.isEmpty()) element = element.withTextTooltip(textHolder.getText());

        return new CriterionDisplay(
                element
        );
    }
}
