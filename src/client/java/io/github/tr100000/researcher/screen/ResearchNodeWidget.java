package io.github.tr100000.researcher.screen;

import io.github.tr100000.researcher.Research;
import io.github.tr100000.researcher.ResearchProgress;
import io.github.tr100000.trutils.api.gui.GuiHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.util.Colors;
import org.joml.Vector2i;
import org.joml.Vector2ic;

@Environment(EnvType.CLIENT)
public class ResearchNodeWidget extends PressableWidget {
    private final MinecraftClient client = MinecraftClient.getInstance();
    private final ResearchTooltipWrapper tooltipWrapper = new ResearchTooltipWrapper(true);
    private final ResearchScreen screen;
    private final ScrollableView parentView;
    private final TooltipPositioner tooltipPositioner = new TooltipPositionerImpl();
    public final Research research;

    public static final int FILL_LOCKED = 0xFFB82121;
    public static final int FILL_AVAILABLE = 0xFFBABABA;
    public static final int FILL_FINISHED = 0xFF149900;
    public static final int FILL_PROGRESS_BACKGROUND = Colors.GRAY;
    public static final int FILL_PROGRESS_BAR = 0xFF28C900;

    public ResearchNodeWidget(ResearchScreen screen, ScrollableView parentView, int x, int y, Research research) {
        this(screen, parentView, x, y, research, false);
    }

    public ResearchNodeWidget(ResearchScreen screen, ScrollableView parentView, int x, int y, Research research, boolean isSelected) {
        this(screen, parentView, x, y, isSelected ? 64 : 48, isSelected ? 64 : 48, research);
    }

    public ResearchNodeWidget(ResearchScreen screen, ScrollableView parentView, int x, int y, int width, int height, Research research) {
        super(x, y, width, height, research.getTitle(MinecraftClient.getInstance().getNetworkHandler().researcher$getClientTracker()));
        this.screen = screen;
        this.parentView = parentView;
        this.research = research;
    }

    @Override
    public void renderWidget(DrawContext draw, int mouseX, int mouseY, float delta) {
        ResearchProgress progress = screen.researchManager.getProgress(research);
        boolean showProgressBar = !progress.isFinished() && progress.getCount() > 0;
        draw.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), getFillColor(progress));

        int iconSize = (int)(getWidth() * 0.85F);
        if (iconSize % 2 == 1) iconSize++;
        int iconOffset = (getWidth() - iconSize) / 2;
        research.display().drawWithSize(draw, getX() + iconOffset, getY() + iconOffset - (showProgressBar ? 1 : 0), iconSize, delta);
        if (showProgressBar) {
            draw.fill(getX(), getY() + getHeight() - 2, getX() + getWidth(), getY() + getHeight(), FILL_PROGRESS_BACKGROUND);
            draw.fill(getX(), getY() + getHeight() - 2, getX() + progress.getScaledProgress(research.trigger().count(), getWidth()), getY() + getHeight(), FILL_PROGRESS_BAR);
        }

        if (GuiHelper.isMouseTouching(this, mouseX, mouseY) && draw.scissorContains(mouseX + parentView.getOffsetX(), mouseY + parentView.getOffsetY())) {
            GuiHelper.drawSlotHighlight(draw, this);
            GuiHelper.drawTooltip(draw, client.textRenderer, tooltipWrapper.getOrCreate(research), mouseX, mouseY, tooltipPositioner);
        }
    }

    private int getFillColor(ResearchProgress progress) {
        if (progress.isFinished()) return FILL_FINISHED;
        return screen.researchManager.canResearch(research) ? FILL_AVAILABLE : FILL_LOCKED;
    }

    @Override
    public void onPress() {
        screen.initWith(research);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        appendDefaultNarrations(builder);
    }

    /**
     * Special implementation of {@link TooltipPositioner} to take view offsets into account.
     * @implNote Modified version of {@link net.minecraft.client.gui.tooltip.HoveredTooltipPositioner}.
     */
    private class TooltipPositionerImpl implements TooltipPositioner {
        @Override
        public Vector2ic getPosition(int screenWidth, int screenHeight, int x, int y, int width, int height) {
            Vector2i vector2i = new Vector2i(x, y).add(12, -12).add(parentView.getOffsetX(), parentView.getOffsetY());
            this.preventOverflow(screenWidth, screenHeight, vector2i, width, height);
            return vector2i;
        }

        private void preventOverflow(int screenWidth, int screenHeight, Vector2i pos, int width, int height) {
            if (pos.x + width > screenWidth) {
                pos.x = Math.max(pos.x - 24 - width, 4);
            }

            int i = height + 3;
            if (pos.y + i > screenHeight) {
                pos.y = screenHeight - i;
            }
        }
    }
}
