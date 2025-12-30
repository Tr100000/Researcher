package io.github.tr100000.researcher.screen;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import io.github.tr100000.researcher.Researcher;
import io.github.tr100000.researcher.ResearcherClient;
import io.github.tr100000.researcher.api.recipe.RecipeUnlockDisplay;
import io.github.tr100000.researcher.api.recipe.RecipeUnlockDisplayRegistry;
import io.github.tr100000.trutils.api.gui.GuiHelper;
import io.github.tr100000.trutils.api.gui.Icon;
import io.github.tr100000.trutils.api.gui.IconRenderers;
import io.github.tr100000.trutils.api.gui.ItemIcon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

public class RecipeUnlockWidget extends AbstractWidget {
    private static final Minecraft client = Minecraft.getInstance();

    public final Identifier id;
    public final Icon icon;
    public final ClientTooltipComponent tooltip;
    public final boolean isError;

    public static RecipeUnlockWidget fromId(int x, int y, Identifier id) {
        RecipeHolder<?> entry = client.level.recipeAccess().getSynchronizedRecipes().get(ResourceKey.create(Registries.RECIPE, id));
        if (entry != null) {
            RecipeUnlockDisplay display = RecipeUnlockDisplayRegistry.getDisplay(entry);
            if (display != null) {
                return new RecipeUnlockWidget(x, y, id, display, false);
            }
            else {
                Researcher.LOGGER.warn("Couldn't display recipe unlock with id {}", id);
                return new RecipeUnlockWidget(x, y, id, RecipeUnlockDisplay.ERROR, true);
            }
        }
        else {
            Researcher.LOGGER.warn("Couldn't find recipe with id {}", id);
            return new RecipeUnlockWidget(x, y, id, RecipeUnlockDisplay.ERROR, true);
        }
    }

    private RecipeUnlockWidget(int x, int y, Identifier id, RecipeUnlockDisplay display, boolean isError) {
        super(x, y, 16, 16, Component.empty());
        this.id = id;
        this.icon = display.icon();
        this.tooltip = display.tooltip();
        this.isError = isError;
    }

    @Override
    protected void renderWidget(GuiGraphics draw, int mouseX, int mouseY, float delta) {
        IconRenderers.draw(icon, draw, getX(), getY());
        if (isHovered() && draw.containsPointInScissor(mouseX, mouseY)) {
            GuiHelper.drawTooltip(draw, client.font, List.of(tooltip), mouseX, mouseY, GuiHelper.widgetPositionerFor(this));
            draw.requestCursor(isError ? CursorTypes.NOT_ALLOWED : CursorTypes.POINTING_HAND);
        }
    }

    @Override
    public void onClick(MouseButtonEvent click, boolean doubled) {
        if (isError) return;
        if (!ResearcherClient.recipeViewerDelegate.showRecipe(id)) {
            // We tried, and that's what matters
            ResearcherClient.recipeViewerDelegate.showRecipes(tryGetResult());
        }
    }

    private ItemStack tryGetResult() {
        return icon instanceof ItemIcon(ItemStack stack) ? stack : ItemStack.EMPTY;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput builder) { /* do nothing */ }

    @Override
    public void playDownSound(SoundManager soundManager) { /* do nothing */ }
}
