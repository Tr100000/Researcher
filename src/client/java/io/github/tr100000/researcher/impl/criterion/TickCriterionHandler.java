package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.CriterionDisplayElement;
import io.github.tr100000.researcher.api.criterion.CriterionHandler;
import io.github.tr100000.researcher.api.criterion.element.TextElement;
import net.minecraft.advancement.criterion.TickCriterion;
import net.minecraft.text.Text;

public class TickCriterionHandler implements CriterionHandler<TickCriterion.Conditions> {
    public static final TickCriterionHandler TICK = new TickCriterionHandler(
            "screen.researcher.criterion.tick.one",
            "screen.researcher.criterion.tick.many",
            true
    );

    public static final TickCriterionHandler SLEPT_IN_BED = new TickCriterionHandler(
            "screen.researcher.criterion.slept_in_bed.one",
            "screen.researcher.criterion.slept_in_bed.many",
            false
    );

    public static final TickCriterionHandler HERO_OF_THE_VILLAGE = new TickCriterionHandler(
            "screen.researcher.criterion.hero_of_the_village.one",
            "screen.researcher.criterion.hero_of_the_village.many",
            false
    );

    public static final TickCriterionHandler VOLUNTARY_EXILE = new TickCriterionHandler(
            "screen.researcher.criterion.voluntary_exile.one",
            "screen.researcher.criterion.voluntary_exile.many",
            false
    );

    public static final TickCriterionHandler AVOID_VIBRATION = new TickCriterionHandler(
            "screen.researcher.criterion.avoid_vibration.one",
            "screen.researcher.criterion.avoid_vibration.many",
            false
    );

    private final String oneTextKey;
    private final String manyTextKey;
    private final boolean countIsTime;

    public TickCriterionHandler(String oneTextKey, String manyTextKey, boolean countIsTime) {
        this.oneTextKey = oneTextKey;
        this.manyTextKey = manyTextKey;
        this.countIsTime = countIsTime;
    }

    @Override
    public CriterionDisplayElement prepare(ResearchCriterion<TickCriterion.Conditions> criterion) {
        if (criterion.count() == 1) {
            return new TextElement(Text.translatable(oneTextKey));
        }
        else {
            return new TextElement(Text.translatable(manyTextKey, criterion.count() * (countIsTime ? 20 : 1)));
        }
    }
}
