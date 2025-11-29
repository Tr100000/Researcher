package io.github.tr100000.researcher.api;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;

public interface CriterionDisplayElement {
    int render(DrawContext draw, int x, int y, int mouseX, int mouseY, float delta);
    int getWidth();

    default CriterionDisplayElement withTooltip(TooltipComponent tooltip) {
        return new CriterionDisplayElementWithTooltip(this, tooltip);
    }
}
