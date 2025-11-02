package io.github.tr100000.researcher.api;

import io.github.tr100000.trutils.api.gui.IconRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.recipe.display.SlotDisplayContexts;
import net.minecraft.text.Text;
import net.minecraft.util.context.ContextParameterMap;

import java.util.Objects;

public interface RecipeUnlockDisplay {
    RecipeUnlockDisplay ERROR = new Impl(IconRenderer.ERROR, TooltipComponent.of(Text.literal("Error").asOrderedText()));

    IconRenderer icon();
    TooltipComponent tooltip();

    record Impl(IconRenderer icon, TooltipComponent tooltip) implements RecipeUnlockDisplay {}

    static ContextParameterMap contextParameterMap() {
        return SlotDisplayContexts.createParameters(Objects.requireNonNull(MinecraftClient.getInstance().world));
    }
}
