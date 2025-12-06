package io.github.tr100000.researcher.api.criterion;

import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.element.GroupedElement;
import io.github.tr100000.researcher.api.criterion.element.SpacingElement;
import io.github.tr100000.researcher.api.criterion.element.TextElement;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.List;

public class CriterionDisplay implements CriterionDisplayElement {
    private static final int EDGE_PADDING = 2;

    private final List<CriterionDisplayElement> elements;

    public CriterionDisplay(List<CriterionDisplayElement> elements) {
        this.elements = elements;
    }

    public CriterionDisplay(CriterionDisplayElement... elements) {
        this(List.of(elements));
    }

    public int render(DrawContext draw, int x, int y, int mouseX, int mouseY, float delta) {
        int totalWidth = EDGE_PADDING;
        for (CriterionDisplayElement element : elements) {
            element.render(draw, x + totalWidth, y, mouseX, mouseY, delta);
            totalWidth += element.getWidth();
        }
        return totalWidth + EDGE_PADDING;
    }

    public int getWidth() {
        int totalWidth = EDGE_PADDING;
        for (CriterionDisplayElement element : elements) {
            totalWidth += element.getWidth();
        }
        return totalWidth + EDGE_PADDING;
    }

    public static CriterionDisplayElement makeCountElement(ResearchCriterion<?> criterion) {
        return new GroupedElement(
                new TextElement(Text.literal(String.valueOf(criterion.count()))),
                new SpacingElement(2),
                new TextElement(Text.literal("×")),
                new TextElement(Text.literal(" "))
        );
    }
}
