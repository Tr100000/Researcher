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
import net.minecraft.advancement.criterion.ChangedDimensionCriterion;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.Optional;

public class ChangedDimensionCriterionHandler implements CriterionHandler<ChangedDimensionCriterion.Conditions> {
    private static final Text PLAYER_CONDITIONS_HEADER = ModUtils.getScreenTranslated("predicate.player");
    private static final String TEXT = ModUtils.getScreenTranslationKey("criterion.changed_dimension");
    private static final String TEXT_WITH_CONDITIONS = ModUtils.getScreenTranslationKey("criterion.changed_dimension.with_conditions");
    private static final String FROM_TEXT = ModUtils.getScreenTranslationKey("criterion.changed_dimension.from");
    private static final String FROM_TEXT_WITH_CONDITIONS = ModUtils.getScreenTranslationKey("criterion.changed_dimension.from.with_conditions");
    private static final String TO_TEXT = ModUtils.getScreenTranslationKey("criterion.changed_dimension.to");
    private static final String TO_TEXT_WITH_CONDITIONS = ModUtils.getScreenTranslationKey("criterion.changed_dimension.to.with_conditions");
    private static final String ANY_TEXT = ModUtils.getScreenTranslationKey("criterion.changed_dimension.any");
    private static final String ANY_TEXT_WITH_CONDITIONS = ModUtils.getScreenTranslationKey("criterion.changed_dimension.any.with_conditions");

    @Override
    public CriterionDisplayElement prepare(ResearchCriterion<ChangedDimensionCriterion.Conditions> criterion) {
        IndentedTextHolder textHolder = new IndentedTextHolder();

        PredicateHelper.optionalTooltip(criterion.conditions().player(), EntityPredicateHelper::tooltip, PLAYER_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);

        CriterionDisplayElement element;
        Optional<RegistryKey<World>> worldFrom = criterion.conditions().from();
        Optional<RegistryKey<World>> worldTo = criterion.conditions().to();
        if (worldFrom.isPresent() && worldTo.isPresent()) {
            element = new TextElement(Text.translatable(textHolder.isEmpty() ? TEXT : TEXT_WITH_CONDITIONS, worldFrom.get().getValue(), worldTo.get().getValue()));
        }
        else if (worldFrom.isPresent()) {
            element = new TextElement(Text.translatable(textHolder.isEmpty() ? FROM_TEXT : FROM_TEXT_WITH_CONDITIONS, worldFrom.get().getValue()));
        }
        else if (worldTo.isPresent()) {
            element = new TextElement(Text.translatable(textHolder.isEmpty() ? TO_TEXT : TO_TEXT_WITH_CONDITIONS, worldTo.get().getValue()));
        }
        else {
            element = new TextElement(Text.translatable(textHolder.isEmpty() ? ANY_TEXT : ANY_TEXT_WITH_CONDITIONS));
        }

        if (!textHolder.isEmpty()) element = element.withTextTooltip(textHolder.getText());

        return new CriterionDisplay(
                CriterionDisplay.makeCountElement(criterion),
                element
        );
    }
}
