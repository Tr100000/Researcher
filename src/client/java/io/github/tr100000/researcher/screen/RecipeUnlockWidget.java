package io.github.tr100000.researcher.screen;

import io.github.tr100000.researcher.Researcher;
import io.github.tr100000.researcher.ResearcherClient;
import io.github.tr100000.researcher.api.RecipeUnlockDisplay;
import io.github.tr100000.researcher.api.RecipeUnlockDisplayRegistry;
import io.github.tr100000.trutils.api.gui.GuiHelper;
import io.github.tr100000.trutils.api.gui.Icon;
import io.github.tr100000.trutils.api.gui.IconRenderers;
import io.github.tr100000.trutils.api.gui.ItemIcon;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.cursor.StandardCursors;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class RecipeUnlockWidget extends ClickableWidget {
    private static final MinecraftClient client = MinecraftClient.getInstance();

    public final Identifier id;
    public final Icon icon;
    public final TooltipComponent tooltip;
    public final boolean isError;

    public static RecipeUnlockWidget fromId(int x, int y, Identifier id) {
        RecipeEntry<?> entry = client.world.getRecipeManager().getSynchronizedRecipes().get(RegistryKey.of(RegistryKeys.RECIPE, id));
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
        super(x, y, 16, 16, Text.empty());
        this.id = id;
        this.icon = display.icon();
        this.tooltip = display.tooltip();
        this.isError = isError;
    }

    @Override
    protected void renderWidget(DrawContext draw, int mouseX, int mouseY, float delta) {
        IconRenderers.draw(icon, draw, getX(), getY());
        if (isHovered() && draw.scissorContains(mouseX, mouseY)) {
            GuiHelper.drawTooltip(draw, client.textRenderer, List.of(tooltip), mouseX, mouseY, GuiHelper.widgetPositionerFor(this));
            draw.setCursor(isError ? StandardCursors.NOT_ALLOWED : StandardCursors.POINTING_HAND);
        }
    }

    @Override
    public void onClick(Click click, boolean doubled) {
        if (isError) return;
        if (!ResearcherClient.recipeViewerDelegate.showRecipe(id)) ResearcherClient.recipeViewerDelegate.showRecipes(tryGetResult());
    }

    private ItemStack tryGetResult() {
        return icon instanceof ItemIcon(ItemStack stack) ? stack : ItemStack.EMPTY;
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) { /* do nothing */ }

    @Override
    public void playDownSound(SoundManager soundManager) { /* do nothing */ }
}
