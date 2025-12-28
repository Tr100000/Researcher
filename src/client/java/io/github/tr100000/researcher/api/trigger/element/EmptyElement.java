package io.github.tr100000.researcher.api.trigger.element;

import io.github.tr100000.researcher.api.trigger.TriggerDisplayElement;
import net.minecraft.client.gui.GuiGraphics;

public final class EmptyElement implements TriggerDisplayElement {
    private EmptyElement() {}

    public static final EmptyElement INSTANCE = new EmptyElement();

    @Override
    public int render(GuiGraphics draw, int x, int y, int mouseX, int mouseY, float delta) {
        return 0;
    }

    @Override
    public int getWidth() {
        return 0;
    }
}
