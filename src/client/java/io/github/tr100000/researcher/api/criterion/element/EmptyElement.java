package io.github.tr100000.researcher.api.criterion.element;

import io.github.tr100000.researcher.api.criterion.CriterionDisplayElement;
import net.minecraft.client.gui.DrawContext;

public final class EmptyElement implements CriterionDisplayElement {
    private EmptyElement() {}

    public static final EmptyElement INSTANCE = new EmptyElement();

    @Override
    public int render(DrawContext draw, int x, int y, int mouseX, int mouseY, float delta) {
        return 0;
    }

    @Override
    public int getWidth() {
        return 0;
    }
}
