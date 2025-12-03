package io.github.tr100000.researcher.screen;

import io.github.tr100000.researcher.ClientResearchTracker;
import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.Research;
import io.github.tr100000.researcher.ResearchProgress;
import io.github.tr100000.researcher.config.ResearcherConfigs;
import io.github.tr100000.trutils.api.gui.GuiHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;

public final class ResearchHud {
    private ResearchHud() {}

    public static final Identifier LAYER_ID = ModUtils.id("hud");

    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static int percentageTextMaxWidth;

    private static final int BACKGROUND_COLOR = 0xFF202020;
    private static final int BORDER_COLOR = 0xFF000000;
    private static final int BORDER_COLOR_HOVERED = 0xFFFF0000;
    private static final int PROGRESS_BACKGROUND_COLOR = 0xFF707070;
    private static final int PROGRESS_FILL_COLOR = 0xFF28C900;

    public static void render(DrawContext draw, RenderTickCounter tickCounter) {
        if (client.world == null || client.player == null || !ResearcherConfigs.client.researchHud.get()) return;
        if (client.debugHudEntryList.isF3Enabled()) return; // Don't show when F3 is open
        if (client.getNetworkHandler() == null) return;

        ClientResearchTracker researchTracker = client.getNetworkHandler().researcher$getClientTracker();
        if (researchTracker == null) return;

        if (percentageTextMaxWidth <= 0) percentageTextMaxWidth = client.textRenderer.getWidth("99%");

        int y = 0;

        if (researchTracker.getCurrentResearching() != null) {
            y += render(draw, researchTracker, researchTracker.getCurrentResearching(), 0);
        }
        for (Identifier researchId : researchTracker.getPinnedResearches()) {
            y += render(draw, researchTracker, researchTracker.get(researchId), y);
        }
    }

    private static int render(DrawContext draw, ClientResearchTracker researchTracker, Research research, int y) {
        if (research == null) return 0;

        boolean hovered = client.currentScreen instanceof ChatScreen && GuiHelper.isMouseTouching(0, y + 1, 150, 26);

        draw.fill(0, y, 150, y + 28, BACKGROUND_COLOR);
        draw.drawStrokedRectangle(0, y, 150, 28, hovered ? BORDER_COLOR_HOVERED : BORDER_COLOR);
        draw.drawText(client.textRenderer, research.getTitle(researchTracker), 4, y + 4, Colors.WHITE, true);

        Text progressText = Text.literal(String.format("%s%%", getProgressPercentage(researchTracker, research)));
        draw.drawText(client.textRenderer, progressText, 146 - percentageTextMaxWidth, y + 16, Colors.WHITE, true);

        int scaledProgress = getScaledProgress(researchTracker, research, 138 - percentageTextMaxWidth);
        draw.fill(4, y + 18, 142 - percentageTextMaxWidth, y + 22, PROGRESS_BACKGROUND_COLOR);
        draw.fill(4, y + 18, 4 + scaledProgress, y + 22, PROGRESS_FILL_COLOR);

        return 27;
    }

    private static int getScaledProgress(ClientResearchTracker researchTracker, Research research, int width) {
        ResearchProgress progress = researchTracker.getProgress(research);
        if (progress.isFinished()) return width;
        int count = progress.getCount();
        int max = research.trigger().count();
        return (int)((float)count / max * width);
    }

    private static int getProgressPercentage(ClientResearchTracker researchTracker, Research research) {
        return getScaledProgress(researchTracker, research, 100);
    }
}
