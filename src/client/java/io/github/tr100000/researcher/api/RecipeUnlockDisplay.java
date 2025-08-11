package io.github.tr100000.researcher.api;

import io.github.tr100000.trutils.api.gui.IconRenderer;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.text.Text;

public interface RecipeUnlockDisplay {
    RecipeUnlockDisplay ERROR = new Impl(IconRenderer.ERROR, TooltipComponent.of(Text.literal("Error").asOrderedText()));

    IconRenderer icon();
    TooltipComponent tooltip();

    record Impl(IconRenderer icon, TooltipComponent tooltip) implements RecipeUnlockDisplay {}
}
