package io.github.tr100000.researcher.api.criterion.element;

import io.github.tr100000.researcher.api.criterion.CriterionDisplayElement;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class TimedSwitchingElement implements CriterionDisplayElement {
    private final List<CriterionDisplayElement> elements;
    private final float switchTimeSeconds;

    private float timeNanoseconds;

    public TimedSwitchingElement(float switchTimeSeconds, List<CriterionDisplayElement> elements) {
        this.elements = elements;
        this.switchTimeSeconds = switchTimeSeconds;
    }

    public TimedSwitchingElement(float switchTimeSeconds, CriterionDisplayElement... elements) {
        this(switchTimeSeconds, List.of(elements));
    }

    public TimedSwitchingElement(List<CriterionDisplayElement> elements) {
        this(1.0F, elements);
    }

    public TimedSwitchingElement(CriterionDisplayElement... elements) {
        this(List.of(elements));
    }

    private CriterionDisplayElement getCurrentElement() {
        final long timeMilliseconds = Util.getMeasuringTimeMs();
        int index = MathHelper.floor((double)timeMilliseconds / 1000L / switchTimeSeconds) % elements.size();
        return elements.get(index);
    }

    @Override
    public int render(DrawContext draw, int x, int y, int mouseX, int mouseY, float delta) {
        return getCurrentElement().render(draw, x, y, mouseX, mouseY, delta);
    }

    @Override
    public int getWidth() {
        return getCurrentElement().getWidth();
    }
}
