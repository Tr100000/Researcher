package io.github.tr100000.researcher.api.trigger;

import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.trigger.element.GroupedElement;
import io.github.tr100000.researcher.api.trigger.element.SpacingElement;
import io.github.tr100000.researcher.api.trigger.element.TextElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.List;

public class TriggerDisplay implements TriggerDisplayElement {
    private static final int EDGE_PADDING = 2;

    private final List<TriggerDisplayElement> elements;

    public TriggerDisplay(List<TriggerDisplayElement> elements) {
        this.elements = elements;
    }

    public TriggerDisplay(TriggerDisplayElement... elements) {
        this(List.of(elements));
    }

    public int render(GuiGraphics draw, int x, int y, int mouseX, int mouseY, float delta) {
        int totalWidth = EDGE_PADDING;
        for (TriggerDisplayElement element : elements) {
            element.render(draw, x + totalWidth, y, mouseX, mouseY, delta);
            totalWidth += element.getWidth();
        }
        return totalWidth + EDGE_PADDING;
    }

    public int getWidth() {
        int totalWidth = EDGE_PADDING;
        for (TriggerDisplayElement element : elements) {
            totalWidth += element.getWidth();
        }
        return totalWidth + EDGE_PADDING;
    }

    public static TriggerDisplayElement makeCountElement(ResearchCriterion<?> criterion) {
        return new GroupedElement(
                new TextElement(Component.literal(String.valueOf(criterion.count()))),
                new SpacingElement(2),
                new TextElement(Component.literal("×")),
                new TextElement(Component.literal(" "))
        );
    }
}
