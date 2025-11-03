package io.github.tr100000.researcher.screen;

import io.github.tr100000.researcher.ClientResearchTracker;
import io.github.tr100000.researcher.Research;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.ResearchProgress;
import io.github.tr100000.researcher.api.CriterionDisplayElement;
import io.github.tr100000.researcher.api.CriterionHandler;
import io.github.tr100000.researcher.api.CriterionHandlerRegistry;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class ResearchInfoView extends AbstractResearchView {
    private static final int BACKGROUND_COLOR = 0xFF202020;
    private static final int BORDER_COLOR = 0xFF000000;
    private static final int CRITERION_BACKGROUND = 0xFF555555;
    private static final int CRITERION_BORDER = 0xFFABABAB;
    private static final int CRITERION_PROGRESS = 0xFF149900;

    private boolean showUnlocks = true;
    private ClientResearchTracker researchTracker;

    private @Nullable CriterionDisplayElement currentDisplay;

    public ResearchInfoView(ResearchScreen parent) {
        super(parent, 0, 0, ResearchScreen.sidebarWidth, ResearchScreen.infoViewHeight);
        this.scissorRect = new ScreenRect(0, 0, parent.width, parent.height);
    }

    public void initWith(Research research) {
        clearChildren();

        researchTracker = client.getNetworkHandler().researcher$getClientTracker();
        boolean isResearchable = researchTracker.getResearchConditions(research) != null && !researchTracker.hasFinished(research) && researchTracker.canResearch(research);

        int y = 52;
        if (!research.recipeUnlocks().isEmpty()) {
            int x = 14;
            for (Identifier unlock : research.recipeUnlocks()) {
                if (x > getWidth() - 12) {
                    x = 14;
                    y += 18;
                }
                addDrawableChild(RecipeUnlockWidget.fromId(x, y, unlock));
                x += 18;
            }

            showUnlocks = true;
        }
        else {
            showUnlocks = false;
        }

        addDrawableChild(new ResearchDescriptionWidget(12, y + 18, getWidth() - 24, getHeight() - y - 23, research.getDescription(parent.researchManager), client.textRenderer));
        if (researchTracker.canResearch(research)) {
            boolean isCurrent = researchTracker.isCurrentOrPinned(research);
            addDrawableChild(StartResearchButton.create(getWidth() - 8, getHeight() - 28, researchTracker, research, !isResearchable, isCurrent));
        }

        currentDisplay = prepareDisplay(research.trigger());
    }

    @Override
    public void renderView(DrawContext draw, int mouseX, int mouseY, float delta) {
        if (currentDisplay == null || ResearchScreen.selected == null) return;

        ResearchProgress progress = researchTracker.getProgress(ResearchScreen.selected);
        draw.fill(0, 0, width, height, BACKGROUND_COLOR);
        draw.drawTextWithShadow(client.textRenderer, researchTracker.getTitleWithStatus(ResearchScreen.selected), 8, 8, Colors.WHITE);
        drawCriterion(currentDisplay, ResearchScreen.selected.trigger(), progress, draw, mouseX, mouseY, 12, 20, delta);
        if (showUnlocks) {
            draw.drawTextWithShadow(client.textRenderer, Text.translatable("screen.researcher.unlocks"), 12, 42, Colors.WHITE);
        }
        draw.drawStrokedRectangle(0, 0, width, height, BORDER_COLOR);
        super.renderView(draw, mouseX, mouseY, delta);
    }

    public static <T extends CriterionConditions> CriterionDisplayElement prepareDisplay(ResearchCriterion<T> criterion) {
        CriterionHandler<T> handler = CriterionHandlerRegistry.get(criterion.trigger());
        return handler.prepare(criterion);
    }

    public static void drawCriterion(CriterionDisplayElement display, ResearchCriterion<?> criterion, ResearchProgress progress, DrawContext draw, int mouseX, int mouseY, int x, int y, float delta) {
        int criterionWidth = display.getWidth();
        draw.fill(x, y, x + criterionWidth + 4, y + 18, CRITERION_BACKGROUND);
        draw.fill(x, y, x + getScaledProgress(criterion, progress, criterionWidth + 4), y + 18, CRITERION_PROGRESS);
        draw.drawStrokedRectangle(x, y, criterionWidth + 4, 18, CRITERION_BORDER);
        display.render(draw, x + 1, y + 1, mouseX, mouseY, delta);
    }

    public static int getScaledProgress(ResearchCriterion<?> criterion, ResearchProgress progress, int width) {
        if (progress.isFinished()) return width;
        int count = progress.getCount();
        int max = criterion.count();
        return (int)((float)count / max * width);
    }
}
