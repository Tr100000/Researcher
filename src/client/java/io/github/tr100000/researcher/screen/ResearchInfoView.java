package io.github.tr100000.researcher.screen;

import io.github.tr100000.researcher.ClientResearchTracker;
import io.github.tr100000.researcher.Research;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.ResearchProgress;
import io.github.tr100000.researcher.api.trigger.TriggerDisplayElement;
import io.github.tr100000.researcher.api.trigger.TriggerHandler;
import io.github.tr100000.researcher.api.trigger.TriggerHandlerRegistry;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.CommonColors;
import org.jspecify.annotations.Nullable;

public class ResearchInfoView extends AbstractResearchView {
    private static final int BACKGROUND_COLOR = 0xFF202020;
    private static final int BORDER_COLOR = 0xFF000000;
    private static final int CRITERION_BACKGROUND = 0xFF555555;
    private static final int CRITERION_BORDER = 0xFFABABAB;
    private static final int CRITERION_PROGRESS = 0xFF149900;

    private boolean showUnlocks = true;
    private @Nullable ClientResearchTracker researchTracker;

    private @Nullable TriggerDisplayElement currentDisplay;

    public ResearchInfoView(ResearchScreen parent) {
        super(parent, 0, 0, ResearchScreen.sidebarWidth, ResearchScreen.infoViewHeight);
        this.scissorRect = new ScreenRectangle(0, 0, parent.width, parent.height);
    }

    public void initWith(Research research) {
        clearChildren();

        researchTracker = client.getConnection().researcher$getClientTracker();
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

        addDrawableChild(new ResearchDescriptionWidget(12, y + 18, getWidth() - 24, getHeight() - y - 23, research.getDescription(parent.researchManager), client.font));
        if (researchTracker.canResearch(research)) {
            boolean isCurrent = researchTracker.isCurrentOrPinned(research);
            addDrawableChild(StartResearchButton.create(getWidth() - 8, getHeight() - 28, researchTracker, research, !isResearchable, isCurrent));
        }

        currentDisplay = prepareDisplay(research.trigger());
    }

    @Override
    public void renderView(GuiGraphics draw, int mouseX, int mouseY, float delta) {
        if (currentDisplay == null || ResearchScreen.selected == null || researchTracker == null) return;

        ResearchProgress progress = researchTracker.getProgress(ResearchScreen.selected);
        draw.fill(0, 0, width, height, BACKGROUND_COLOR);
        draw.drawString(client.font, researchTracker.getTitleWithStatus(ResearchScreen.selected), 8, 8, CommonColors.WHITE);
        drawCriterion(currentDisplay, ResearchScreen.selected.trigger(), progress, draw, mouseX, mouseY, 12, 20, delta);
        if (showUnlocks) {
            draw.drawString(client.font, Component.translatable("screen.researcher.unlocks"), 12, 42, CommonColors.WHITE);
        }
        draw.renderOutline(0, 0, width, height, BORDER_COLOR);
        super.renderView(draw, mouseX, mouseY, delta);
    }

    public static <T extends CriterionTriggerInstance> TriggerDisplayElement prepareDisplay(ResearchCriterion<T> criterion) {
        TriggerHandler<T> handler = TriggerHandlerRegistry.get(criterion.trigger());
        return handler.prepare(criterion);
    }

    public static void drawCriterion(TriggerDisplayElement display, ResearchCriterion<?> criterion, ResearchProgress progress, GuiGraphics draw, int mouseX, int mouseY, int x, int y, float delta) {
        int criterionWidth = display.getWidth();
        draw.fill(x, y, x + criterionWidth + 4, y + 18, CRITERION_BACKGROUND);
        draw.fill(x, y, x + getScaledProgress(criterion, progress, criterionWidth + 4), y + 18, CRITERION_PROGRESS);
        draw.renderOutline(x, y, criterionWidth + 4, 18, CRITERION_BORDER);
        display.render(draw, x + 1, y + 1, mouseX, mouseY, delta);
    }

    public static int getScaledProgress(ResearchCriterion<?> criterion, ResearchProgress progress, int width) {
        if (progress.isFinished()) return width;
        int count = progress.getCount();
        int max = criterion.count();
        return (int)((float)count / max * width);
    }
}
