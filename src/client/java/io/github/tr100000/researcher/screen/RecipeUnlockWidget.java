package io.github.tr100000.researcher.screen;

import io.github.tr100000.researcher.Researcher;
import io.github.tr100000.researcher.api.RecipeUnlockDisplay;
import io.github.tr100000.researcher.api.RecipeUnlockDisplayRegistry;
import io.github.tr100000.trutils.TrUtilsClient;
import io.github.tr100000.trutils.api.gui.GuiHelper;
import io.github.tr100000.trutils.api.gui.IconRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class RecipeUnlockWidget extends ClickableWidget {
    private static final MinecraftClient client = MinecraftClient.getInstance();

    public final Identifier id;
    public final IconRenderer icon;
    public final TooltipComponent tooltip;

    public static RecipeUnlockWidget fromId(int x, int y, Identifier id) {
        RecipeEntry<?> entry = TrUtilsClient.getSyncedRecipes().get(RegistryKey.of(RegistryKeys.RECIPE, id));
        if (entry != null) {
            RecipeUnlockDisplay display = RecipeUnlockDisplayRegistry.getDisplay(entry);
            if (display != null) {
                return new RecipeUnlockWidget(x, y, id, display);
            }
            else {
                Researcher.LOGGER.warn("Couldn't display recipe unlock with id {}", id);
                return new RecipeUnlockWidget(x, y, id, RecipeUnlockDisplay.ERROR);
            }
        }
        else {
            Researcher.LOGGER.warn("Couldn't find recipe with id {}", id);
            return new RecipeUnlockWidget(x, y, id, RecipeUnlockDisplay.ERROR);
        }
    }

    private RecipeUnlockWidget(int x, int y, Identifier id, RecipeUnlockDisplay display) {
        super(x, y, 16, 16, Text.empty());
        this.id = id;
        this.icon = display.icon();
        this.tooltip = display.tooltip();
    }

    @Override
    protected void renderWidget(DrawContext draw, int mouseX, int mouseY, float delta) {
        if (tooltip == null) {
            IconRenderer.ERROR.draw(draw, getX(), getY(), delta);
            if (GuiHelper.isMouseTouching(this, mouseX, mouseY)) {
                draw.drawTooltip(client.textRenderer, Text.translatable("tooltip.researcher.invalid_unlock", id), mouseX, mouseY);
            }
            return;
        }

        icon.draw(draw, getX(), getY());
        if (GuiHelper.isMouseTouching(this, mouseX, mouseY) && draw.scissorContains(mouseX, mouseY)) {
            GuiHelper.drawTooltip(draw, client.textRenderer, List.of(tooltip), mouseX, mouseY, GuiHelper.widgetPositionerFor(this));
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        // TODO proper recipe viewer support
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) { /* do nothing */ }

    @Override
    public void playDownSound(SoundManager soundManager) { /* do nothing */ }
}
