package io.github.tr100000.researcher.api.recipe;

import io.github.tr100000.trutils.api.gui.Icon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;

import java.util.Objects;

public interface RecipeUnlockDisplay {
    RecipeUnlockDisplay ERROR = new Impl(Icon.ERROR, ClientTooltipComponent.create(Component.literal("Error").getVisualOrderText()));

    Icon icon();
    ClientTooltipComponent tooltip();

    record Impl(Icon icon, ClientTooltipComponent tooltip) implements RecipeUnlockDisplay {}

    static ContextMap contextParameterMap() {
        return SlotDisplayContext.fromLevel(Objects.requireNonNull(Minecraft.getInstance().level));
    }
}
