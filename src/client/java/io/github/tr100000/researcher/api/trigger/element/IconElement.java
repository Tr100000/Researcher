package io.github.tr100000.researcher.api.trigger.element;

import io.github.tr100000.researcher.api.trigger.TriggerDisplayElement;
import io.github.tr100000.trutils.api.gui.Icon;
import io.github.tr100000.trutils.api.gui.IconRenderers;
import net.minecraft.client.gui.GuiGraphics;

public class IconElement implements TriggerDisplayElement {
    protected final Icon icon;

    public IconElement(Icon icon) {
        this.icon = icon;
    }

    @Override
    public int render(GuiGraphics draw, int x, int y, int mouseX, int mouseY, float delta) {
        IconRenderers.draw(icon, draw, x, y, delta);
        return 16;
    }

    @Override
    public int getWidth() {
        return 16;
    }
}
