package io.github.tr100000.researcher.api;

import net.minecraft.client.gui.DrawContext;

import java.util.List;

public class CriterionDisplay implements CriterionDisplayElement {
    protected final List<CriterionDisplayElement> elements;

    public CriterionDisplay(List<CriterionDisplayElement> elements) {
        this.elements = elements;
    }

    public CriterionDisplay(CriterionDisplayElement... elements) {
        this(List.of(elements));
    }

    public int render(DrawContext draw, int x, int y, int mouseX, int mouseY, float delta) {
        int totalWidth = 0;
        for (CriterionDisplayElement element : elements) {
            element.render(draw, x + totalWidth, y, mouseX, mouseY, delta);
            totalWidth += element.getWidth();
        }
        return totalWidth;
    }

    public int getWidth() {
        int totalWidth = 0;
        for (CriterionDisplayElement element : elements) {
            totalWidth += element.getWidth();
        }
        return totalWidth;
    }
}
