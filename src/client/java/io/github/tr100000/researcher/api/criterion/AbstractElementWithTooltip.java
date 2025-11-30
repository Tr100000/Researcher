package io.github.tr100000.researcher.api.criterion;

import io.github.tr100000.trutils.api.gui.GuiHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public abstract class AbstractElementWithTooltip implements CriterionDisplayElement {
    protected static final MinecraftClient client = MinecraftClient.getInstance();

    protected final CriterionDisplayElement element;

    protected AbstractElementWithTooltip(CriterionDisplayElement element) {
        if (element instanceof AbstractElementWithTooltip elementWithTooltip) {
            element = elementWithTooltip.element;
        }
        this.element = element;
    }

    protected abstract void drawTooltip(DrawContext draw, int x, int y, int mouseX, int mouseY, float delta);

    @Override
    public int render(DrawContext draw, int x, int y, int mouseX, int mouseY, float delta) {
        int width = element.render(draw, x, y, mouseX, mouseY, delta);

        if (GuiHelper.isMouseTouching(x, y, width, 16, mouseX, mouseY)) {
            drawTooltip(draw, x, y, mouseX, mouseY, delta);
        }

        return width;
    }

    @Override
    public int getWidth() {
        return element.getWidth();
    }
}
