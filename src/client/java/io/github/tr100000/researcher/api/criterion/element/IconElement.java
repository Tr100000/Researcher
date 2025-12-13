package io.github.tr100000.researcher.api.criterion.element;

import io.github.tr100000.researcher.api.criterion.CriterionDisplayElement;
import io.github.tr100000.trutils.api.gui.Icon;
import io.github.tr100000.trutils.api.gui.IconRenderers;
import net.minecraft.client.gui.DrawContext;

public class IconElement implements CriterionDisplayElement {
    protected final Icon icon;

    public IconElement(Icon icon) {
        this.icon = icon;
    }

    @Override
    public int render(DrawContext draw, int x, int y, int mouseX, int mouseY, float delta) {
        IconRenderers.draw(icon, draw, x, y, delta);
        return 16;
    }

    @Override
    public int getWidth() {
        return 16;
    }
}
