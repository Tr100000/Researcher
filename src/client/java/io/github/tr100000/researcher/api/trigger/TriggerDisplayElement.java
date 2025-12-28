package io.github.tr100000.researcher.api.trigger;

import io.github.tr100000.researcher.api.trigger.element.ElementWithComponentTooltip;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.List;

public interface TriggerDisplayElement {
    int render(GuiGraphics draw, int x, int y, int mouseX, int mouseY, float delta);
    int getWidth();

    default TriggerDisplayElement withTooltip(ClientTooltipComponent tooltip) {
        return new ElementWithComponentTooltip(this, tooltip);
    }

    default TriggerDisplayElement withTooltip(ClientTooltipComponent... tooltip) {
        return new ElementWithComponentTooltip(this, tooltip);
    }

    default TriggerDisplayElement withTooltip(List<ClientTooltipComponent> tooltip) {
        return new ElementWithComponentTooltip(this, tooltip);
    }

    default TriggerDisplayElement withTextTooltip(List<? extends Component> text) {
        return withTooltip(text.stream().map(t -> ClientTooltipComponent.create(t.getVisualOrderText())).toList());
    }

    default TriggerDisplayElement withTextTooltip(Component... text) {
        return withTooltip(Arrays.stream(text).map(t -> ClientTooltipComponent.create(t.getVisualOrderText())).toList());
    }
}
