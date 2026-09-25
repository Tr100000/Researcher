package io.github.tr100000.researcher.compat;

import io.github.tr100000.researcher.ClientResearchTracker;
import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.Research;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.widgets.IRecipeWidget;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeHolder;

public final class JeiRecipeLockedWidget implements IRecipeWidget {
    private static final Identifier ERROR_TEXTURE = ModUtils.id("textures/gui/error_overlay.png");
    private static final int WIDTH = 22;
    private static final int HEIGHT = 16;

    private final RecipeHolder<?> recipeHolder;
    private final ClientResearchTracker tracker;
    private final ScreenPosition screenPosition;
    private final ScreenRectangle screenRectangle;

    public JeiRecipeLockedWidget(int x, int y, RecipeHolder<?> recipeHolder, ClientResearchTracker tracker) {
        this.recipeHolder = recipeHolder;
        this.tracker = tracker;
        this.screenPosition = new ScreenPosition(x, y);
        this.screenRectangle = new ScreenRectangle(screenPosition, WIDTH, HEIGHT);
    }

    @Override
    public ScreenPosition getPosition() {
        return screenPosition;
    }

    @Override
    public ScreenRectangle getScreenRectangle() {
        return screenRectangle;
    }

    @Override
    public void drawWidget(GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, ERROR_TEXTURE, 0, 0, 0, 0, WIDTH, HEIGHT, WIDTH, HEIGHT);
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, double mouseX, double mouseY) {
        tooltip.add(ModUtils.getScreenTranslated("unlocked_by_header"));
        tracker.recipeUnlockedBy(recipeHolder.id().identifier()).forEach(id -> {
            Research research = tracker.get(id);
            assert research != null;
            tooltip.add(Component.literal("  ").append(research.getTitle(tracker)));
        });
    }
}
