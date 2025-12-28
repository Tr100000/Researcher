package io.github.tr100000.researcher.api.trigger.element;

import io.github.tr100000.researcher.api.trigger.TriggerDisplayElement;
import net.minecraft.client.gui.GuiGraphics;

public class SpacingElement implements TriggerDisplayElement {
    protected final int width;

    public SpacingElement(int width) {
        this.width = width;
    }

    @Override
    public int render(GuiGraphics draw, int x, int y, int mouseX, int mouseY, float delta) {
        return width;
    }

    @Override
    public int getWidth() {
        return width;
    }
}
