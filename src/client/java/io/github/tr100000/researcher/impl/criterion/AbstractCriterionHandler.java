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
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.text.Text;

public abstract class AbstractCriterionHandler<T extends AbstractCriterion.Conditions> implements CriterionHandler<T> {
    private static final Text PLAYER_CONDITIONS_HEADER = ModUtils.getScreenTranslated("predicate.player");

    private final Text text;
    private final Text textWithConditions;

    protected AbstractCriterionHandler(String textKey, String textWithConditionsKey) {
        this.text = Text.translatable(textKey);
        this.textWithConditions = Text.translatable(textWithConditionsKey);
    }

    protected AbstractCriterionHandler(String textKey) {
        this(textKey, textKey + ".with_conditions");
    }

    @Override
    public CriterionDisplayElement prepare(ResearchCriterion<T> criterion) {
        IndentedTextHolder textHolder = new IndentedTextHolder();

        fillTooltip(criterion, textHolder);

        CriterionDisplayElement element = new TextElement(textHolder.isEmpty() ? text : textWithConditions);
        if (!textHolder.isEmpty()) element = element.withTextTooltip(textHolder.getText());

        return new CriterionDisplay(
                CriterionDisplay.makeCountElement(criterion),
                element
        );
    }

    protected abstract void fillTooltip(ResearchCriterion<T> criterion, IndentedTextHolder textHolder);

    protected void playerTooltip(ResearchCriterion<T> criterion, IndentedTextHolder textHolder) {
        PredicateHelper.tooltip(criterion.conditions().player(), EntityPredicateHelper::tooltip, PLAYER_CONDITIONS_HEADER)
                .ifPresent(textHolder::accept);
    }
}
