package io.github.tr100000.researcher.api;

import net.minecraft.client.gui.DrawContext;

public interface CriterionDisplayElement {
    int render(DrawContext draw, int x, int y, int mouseX, int mouseY, float delta);
    int getWidth();
}
