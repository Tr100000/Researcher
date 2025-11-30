package io.github.tr100000.researcher.api.criterion.element;

import io.github.tr100000.researcher.api.criterion.CriterionDisplayElement;
import io.github.tr100000.trutils.api.gui.GuiHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.HoveredTooltipPositioner;
import net.minecraft.client.gui.tooltip.TooltipComponent;

import java.util.List;

public class ElementWithComponentTooltip extends AbstractElementWithTooltip {
    protected final List<TooltipComponent> tooltip;

    public ElementWithComponentTooltip(CriterionDisplayElement element, List<TooltipComponent> tooltip) {
        super(element);
        this.tooltip = tooltip;
    }

    public ElementWithComponentTooltip(CriterionDisplayElement element, TooltipComponent... tooltip) {
        super(element);
        this.tooltip = List.of(tooltip);
    }

    public ElementWithComponentTooltip(CriterionDisplayElement element, TooltipComponent tooltip) {
        super(element);
        this.tooltip = List.of(tooltip);
    }

    @Override
    protected void drawTooltip(DrawContext draw, int x, int y, int mouseX, int mouseY, float delta) {
        GuiHelper.drawTooltip(draw, client.textRenderer, tooltip, mouseX, mouseY, HoveredTooltipPositioner.INSTANCE);
    }
}
