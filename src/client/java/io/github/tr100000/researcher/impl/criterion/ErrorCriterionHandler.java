package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.CriterionDisplay;
import io.github.tr100000.researcher.api.criterion.CriterionDisplayElement;
import io.github.tr100000.researcher.api.criterion.CriterionHandler;
import io.github.tr100000.researcher.api.criterion.element.ItemElement;
import io.github.tr100000.researcher.api.criterion.element.SpacingElement;
import io.github.tr100000.researcher.api.criterion.element.TextElement;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class ErrorCriterionHandler<T extends CriterionConditions> implements CriterionHandler<T> {
    public static final ErrorCriterionHandler<CriterionConditions> NULL = new ErrorCriterionHandler<>(ModUtils.getScreenTranslated("criterion.null"));
    public static final ErrorCriterionHandler<ImpossibleCriterion.Conditions> IMPOSSIBLE = new ErrorCriterionHandler<>(ModUtils.getScreenTranslated("criterion.impossible"));
    public static final ErrorCriterionHandler<CriterionConditions> ERROR = new ErrorCriterionHandler<>(ModUtils.getScreenTranslated("criterion.error"));

    public static final ErrorCriterionHandler<RecipeUnlockedCriterion.Conditions> WARN_RECIPE_UNLOCKED_AS_CONDITION = new ErrorCriterionHandler<>(ModUtils.getScreenTranslated("criterion.warn.recipe_unlocked_as_condition"));

    private final ItemStack stack = Items.BARRIER.getDefaultStack();
    private final Text text;

    public ErrorCriterionHandler(Text text) {
        this.text = text;
    }

    @Override
    public CriterionDisplayElement prepare(ResearchCriterion<T> criterion) {
        return prepare();
    }

    public CriterionDisplayElement prepare() {
        return new CriterionDisplay(
                new ItemElement(stack, false),
                new SpacingElement(2),
                new TextElement(text)
        );
    }

    public CriterionDisplayElement prepareWithErrorTooltip(Text errorText) {
        return new CriterionDisplay(
                new ItemElement(stack, false).withTextTooltip(errorText),
                new SpacingElement(2),
                new TextElement(text)
        );
    }
}
