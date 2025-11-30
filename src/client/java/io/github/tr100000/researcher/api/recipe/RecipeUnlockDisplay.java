package io.github.tr100000.researcher.api.recipe;

import io.github.tr100000.trutils.api.gui.Icon;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.recipe.display.SlotDisplayContexts;
import net.minecraft.text.Text;
import net.minecraft.util.context.ContextParameterMap;

import java.util.Objects;

public interface RecipeUnlockDisplay {
    RecipeUnlockDisplay ERROR = new Impl(Icon.ERROR, TooltipComponent.of(Text.literal("Error").asOrderedText()));

    Icon icon();
    TooltipComponent tooltip();

    record Impl(Icon icon, TooltipComponent tooltip) implements RecipeUnlockDisplay {}

    static ContextParameterMap contextParameterMap() {
        return SlotDisplayContexts.createParameters(Objects.requireNonNull(MinecraftClient.getInstance().world));
    }
}
