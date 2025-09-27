package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.CriterionDisplay;
import io.github.tr100000.researcher.api.CriterionDisplayElement;
import io.github.tr100000.researcher.api.CriterionHandler;
import io.github.tr100000.researcher.impl.criterion.element.ItemCriterionDisplayElement;
import io.github.tr100000.researcher.impl.criterion.element.SpacingCriterionDisplayElement;
import io.github.tr100000.researcher.impl.criterion.element.TextCriterionDisplayElement;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

import java.util.List;

@Environment(EnvType.CLIENT)
public class ErrorCriterionHandler<T extends CriterionConditions> implements CriterionHandler<T> {
    public static final ErrorCriterionHandler<CriterionConditions> NULL = new ErrorCriterionHandler<>(Text.translatable("screen.researcher.criterion.null"));
    public static final ErrorCriterionHandler<CriterionConditions> ERROR = new ErrorCriterionHandler<>(Text.translatable("screen.researcher.criterion.error"));

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
                new ItemCriterionDisplayElement(stack, false),
                new SpacingCriterionDisplayElement(4),
                new TextCriterionDisplayElement(text)
        );
    }

    public CriterionDisplayElement prepareWithErrorTooltip(Text errorText) {
        return new CriterionDisplay(
                new ItemCriterionDisplayElement(stack, List.of(errorText)),
                new SpacingCriterionDisplayElement(4),
                new TextCriterionDisplayElement(text)
        );
    }
}
