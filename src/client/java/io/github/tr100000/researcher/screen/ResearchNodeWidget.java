package io.github.tr100000.researcher.screen;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import io.github.tr100000.researcher.Research;
import io.github.tr100000.researcher.ResearchProgress;
import io.github.tr100000.trutils.api.gui.GuiHelper;
import io.github.tr100000.trutils.api.gui.IconRenderers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.util.CommonColors;
import org.joml.Vector2i;
import org.joml.Vector2ic;

public class ResearchNodeWidget extends AbstractButton {
    private static final Minecraft client = Minecraft.getInstance();
    private final ClientTooltipPositioner tooltipPositioner = new TooltipPositionerImpl();
    private final ResearchTooltipWrapper tooltipWrapper = new ResearchTooltipWrapper(true);
    private final ResearchScreen screen;
    private final ScrollableView parentView;

    public final Research research;
    private int depth = 0;

    public static final int FILL_LOCKED = 0xFFB82121;
    public static final int FILL_AVAILABLE = 0xFFBABABA;
    public static final int FILL_FINISHED = 0xFF149900;
    public static final int FILL_PROGRESS_BACKGROUND = CommonColors.GRAY;
    public static final int FILL_PROGRESS_BAR = 0xFF28C900;

    public ResearchNodeWidget(ResearchScreen screen, ScrollableView parentView, int x, int y, int depth, Research research) {
        this(screen, parentView, x, y, depth, research, false);
    }

    public ResearchNodeWidget(ResearchScreen screen, ScrollableView parentView, int x, int y, int depth, Research research, boolean isSelected) {
        this(screen, parentView, x, y, isSelected ? 64 : 48, isSelected ? 64 : 48, research);
        this.depth = depth;
    }

    public ResearchNodeWidget(ResearchScreen screen, ScrollableView parentView, int x, int y, int width, int height, Research research) {
        super(x, y, width, height, research.getTitle(Minecraft.getInstance().getConnection().researcher$getClientTracker()));
        this.screen = screen;
        this.parentView = parentView;
        this.research = research;
    }

    @Override
    public void renderContents(GuiGraphics draw, int mouseX, int mouseY, float delta) {
        ResearchProgress progress = screen.researchManager.getProgress(research);
        boolean showProgressBar = !progress.isFinished() && progress.getCount() > 0;
        draw.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), getFillColor(progress));

        int iconSize = (int)(getWidth() * 0.85F);
        if (iconSize % 2 == 1) iconSize++;
        int iconOffset = (getWidth() - iconSize) / 2;
        IconRenderers.drawWithSize(research.display(), draw, getX() + iconOffset, getY() + iconOffset - (showProgressBar ? 1 : 0), iconSize, delta);
        if (showProgressBar) {
            draw.fill(getX(), getY() + getHeight() - 2, getX() + getWidth(), getY() + getHeight(), FILL_PROGRESS_BACKGROUND);
            draw.fill(getX(), getY() + getHeight() - 2, getX() + progress.getScaledProgress(research.trigger().count(), getWidth()), getY() + getHeight(), FILL_PROGRESS_BAR);
        }

        if (isHovered()) {
            GuiHelper.drawSlotHighlight(draw, this);
            GuiHelper.drawTooltip(draw, client.font, tooltipWrapper.getOrCreate(research), mouseX, mouseY, tooltipPositioner);
            draw.requestCursor(CursorTypes.POINTING_HAND);
        }
    }

    private int getFillColor(ResearchProgress progress) {
        if (progress.isFinished()) return FILL_FINISHED;
        return screen.researchManager.canResearch(research) ? FILL_AVAILABLE : FILL_LOCKED;
    }

    public int getDepth() {
        return depth;
    }

    @Override
    public void onPress(InputWithModifiers input) {
        screen.initWith(research);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput builder) {
        defaultButtonNarrationText(builder);
    }

    /**
     * Special implementation of {@link ClientTooltipPositioner} to take view offsets into account.
     * @implNote Modified version of {@link net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner}.
     */
    private class TooltipPositionerImpl implements ClientTooltipPositioner {
        @Override
        public Vector2ic positionTooltip(int screenWidth, int screenHeight, int x, int y, int width, int height) {
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
