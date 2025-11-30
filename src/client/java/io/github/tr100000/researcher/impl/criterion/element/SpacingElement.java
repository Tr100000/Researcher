package io.github.tr100000.researcher.impl.criterion.element;

import io.github.tr100000.researcher.api.criterion.CriterionDisplayElement;
import net.minecraft.client.gui.DrawContext;

public class SpacingElement implements CriterionDisplayElement {
    protected final int width;

    public SpacingElement(int width) {
        this.width = width;
    }

    @Override
    public int render(DrawContext draw, int x, int y, int mouseX, int mouseY, float delta) {
        return width;
    }

    @Override
    public int getWidth() {
        return width;
    }
}
