package io.github.tr100000.researcher.api;

import io.github.tr100000.trutils.api.gui.GuiHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.HoveredTooltipPositioner;
import net.minecraft.client.gui.tooltip.TooltipComponent;

import java.util.List;

public class CriterionDisplayElementWithTooltip implements CriterionDisplayElement {
    private static final MinecraftClient client = MinecraftClient.getInstance();

    private final CriterionDisplayElement element;
    private final TooltipComponent tooltip;

    public CriterionDisplayElementWithTooltip(CriterionDisplayElement element, TooltipComponent tooltip) {
        if (element instanceof CriterionDisplayElementWithTooltip elementWithTooltip) {
            element = elementWithTooltip.element;
        }
        this.element = element;
        this.tooltip = tooltip;
    }

    @Override
    public int render(DrawContext draw, int x, int y, int mouseX, int mouseY, float delta) {
        int width = element.render(draw, x, y, mouseX, mouseY, delta);

        if (GuiHelper.isMouseTouching(x, y, width, 16, mouseX, mouseY)) {
            GuiHelper.drawTooltip(draw, client.textRenderer, List.of(tooltip), mouseX, mouseY, HoveredTooltipPositioner.INSTANCE);
        }

        return width;
    }

    @Override
    public int getWidth() {
        return element.getWidth();
    }
}
