package io.github.tr100000.researcher.impl.criterion.element;

import io.github.tr100000.researcher.api.criterion.CriterionDisplayElement;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;

import java.util.List;

// TODO think of a better name
public class TimedSwitchingElement implements CriterionDisplayElement {
    private final List<CriterionDisplayElement> elements;
    private final float switchTime;

    private float time;

    public TimedSwitchingElement(float switchTime, List<CriterionDisplayElement> elements) {
        this.elements = elements;
        this.switchTime = switchTime;
    }

    public TimedSwitchingElement(float switchTime, CriterionDisplayElement... elements) {
        this(switchTime, List.of(elements));
    }

    public TimedSwitchingElement(List<CriterionDisplayElement> elements) {
        this(1.0F, elements);
    }

    public TimedSwitchingElement(CriterionDisplayElement... elements) {
        this(List.of(elements));
    }

    private CriterionDisplayElement getCurrentElement() {
        int index = MathHelper.floor(time / (switchTime * 20)) % elements.size();
        return elements.get(index);
    }

    @Override
    public int render(DrawContext draw, int x, int y, int mouseX, int mouseY, float delta) {
        int width = getCurrentElement().render(draw, x, y, mouseX, mouseY, delta);
        time += delta;
        return width;
    }

    @Override
    public int getWidth() {
        return getCurrentElement().getWidth();
    }
}
