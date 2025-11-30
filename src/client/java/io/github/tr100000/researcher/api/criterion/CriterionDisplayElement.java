package io.github.tr100000.researcher.api.criterion;

import io.github.tr100000.researcher.api.criterion.element.ElementWithComponentTooltip;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.List;

public interface CriterionDisplayElement {
    int render(DrawContext draw, int x, int y, int mouseX, int mouseY, float delta);
    int getWidth();

    default CriterionDisplayElement withTooltip(TooltipComponent tooltip) {
        return new ElementWithComponentTooltip(this, tooltip);
    }

    default CriterionDisplayElement withTooltip(TooltipComponent... tooltip) {
        return new ElementWithComponentTooltip(this, tooltip);
    }

    default CriterionDisplayElement withTooltip(List<TooltipComponent> tooltip) {
        return new ElementWithComponentTooltip(this, tooltip);
    }

    default CriterionDisplayElement withTextTooltip(List<? extends Text> text) {
        return withTooltip(text.stream().map(t -> TooltipComponent.of(t.asOrderedText())).toList());
    }

    default CriterionDisplayElement withTextTooltip(Text... text) {
        return withTooltip(Arrays.stream(text).map(t -> TooltipComponent.of(t.asOrderedText())).toList());
    }
}
