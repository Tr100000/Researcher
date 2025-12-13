package io.github.tr100000.researcher.api.criterion.element;

import io.github.tr100000.researcher.api.criterion.CriterionDisplayElement;
import net.minecraft.client.gui.DrawContext;

import java.util.List;

public class GroupedElement implements CriterionDisplayElement {
    protected final List<CriterionDisplayElement> elements;

    public GroupedElement(List<CriterionDisplayElement> elements) {
        this.elements = elements;
    }

    public GroupedElement(CriterionDisplayElement... elements) {
        this.elements = List.of(elements);
    }

    @Override
    public int render(DrawContext draw, int x, int y, int mouseX, int mouseY, float delta) {
        int totalWidth = 0;

        for (CriterionDisplayElement element : elements) {
            totalWidth += element.render(draw, x + totalWidth, y, mouseX, mouseY, delta);
        }

        return totalWidth;
    }

    @Override
    public int getWidth() {
        int totalWidth = 0;

        for (CriterionDisplayElement element : elements) {
            totalWidth += element.getWidth();
        }

        return totalWidth;
    }
}
