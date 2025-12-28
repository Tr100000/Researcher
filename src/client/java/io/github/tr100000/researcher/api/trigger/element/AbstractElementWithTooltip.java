package io.github.tr100000.researcher.api.trigger.element;

import io.github.tr100000.researcher.api.trigger.TriggerDisplayElement;
import io.github.tr100000.trutils.api.gui.GuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public abstract class AbstractElementWithTooltip implements TriggerDisplayElement {
    protected static final Minecraft client = Minecraft.getInstance();

    protected final TriggerDisplayElement element;

    protected AbstractElementWithTooltip(TriggerDisplayElement element) {
        if (element instanceof AbstractElementWithTooltip elementWithTooltip) {
            element = elementWithTooltip.element;
        }
        this.element = element;
    }

    protected abstract void drawTooltip(GuiGraphics draw, int x, int y, int mouseX, int mouseY, float delta);

    @Override
    public int render(GuiGraphics draw, int x, int y, int mouseX, int mouseY, float delta) {
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
