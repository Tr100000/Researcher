package io.github.tr100000.researcher.screen;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import io.github.tr100000.researcher.ClientResearchTracker;
import io.github.tr100000.researcher.Research;
import io.github.tr100000.researcher.Researcher;
import io.github.tr100000.researcher.config.ResearcherConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jspecify.annotations.Nullable;

public class ResearchScreen extends Screen {
    protected static final Minecraft client = Minecraft.getInstance();

    public static int sidebarWidth;
    public static int infoViewHeight;

    public static @Nullable Research selected;

    protected final ClientResearchTracker researchManager = client.getConnection().researcher$getClientTracker();

    public final @Nullable Screen parent;

    protected @Nullable ResearchTreeView treeView;
    protected @Nullable ResearchInfoView infoView;
    protected @Nullable ResearchListView listView;

    private boolean isDraggingHorizontal = false;
    private boolean isDraggingVertical = false;

    public ResearchScreen(@Nullable Screen parent) {
        super(Component.translatable("screen.researcher"));
        this.parent = parent;
        refreshSettings();
    }

    public static void refreshSettings() {
        sidebarWidth = ResearcherConfigs.client.researchScreenSidebarWidth.get();
        infoViewHeight = ResearcherConfigs.client.researchScreenInfoViewHeight.get();
    }

    public static void setSelected(@Nullable Research selected) {
        ResearchScreen.selected = selected;
    }

    @Override
    public void init() {
        if (!researchManager.isInitialized()) {
            Researcher.LOGGER.info("Tried to open research screen before initialization finished.");
            onClose();
            return;
        }

        if (selected == null || !researchManager.isValid(selected)) {
            setSelected(researchManager.getRootNodes().iterator().next());
        }

        infoView = new ResearchInfoView(this);
        listView = new ResearchListView(this, height - infoViewHeight);
        treeView = new ResearchTreeView(this, width - sidebarWidth, height);

        assert selected != null;
        initWith(selected);
    }

    public void initWith(Research current) {
        clearWidgets();
        setSelected(current);

        assert infoView != null;
        addRenderableWidget(infoView);
        infoView.initWith(current);

        assert listView != null;
        addRenderableWidget(listView);

        assert treeView != null;
        addRenderableWidget(treeView);
        treeView.initWith(current);

        setInitialFocus(treeView);
    }

    @Override
    public void extractRenderState(final GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        extractBlurredBackground(graphics);
        extractMenuBackground(graphics);
        if (selected != null) {
            super.extractRenderState(graphics, mouseX, mouseY, delta);

            if (isWithinRange(mouseY, infoViewHeight - 1, 1) && mouseX < sidebarWidth) {
                graphics.requestCursor(CursorTypes.RESIZE_NS);
                graphics.horizontalLine(0, sidebarWidth - 2, infoViewHeight - 1, ResearcherConfigs.client.highlightColor.toInt());
            }
            else if (isWithinRange(mouseX, sidebarWidth - 1, 1)) {
                graphics.requestCursor(CursorTypes.RESIZE_EW);
                graphics.verticalLine(sidebarWidth - 1, 0, height, ResearcherConfigs.client.highlightColor.toInt());
            }
        }
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
        if ((isDraggingHorizontal && !isDraggingVertical) || (isWithinRange((int)event.y(), infoViewHeight - 1, 1) && event.x() < sidebarWidth)) {
            infoViewHeight = Mth.clamp((int)event.y() + 1, 50, height - 50);
            resizeChildren();
            isDraggingHorizontal = true;
            return true;
        }
        if ((isDraggingVertical && !isDraggingHorizontal) || isWithinRange((int)event.x(), sidebarWidth - 1, 1)) {
            sidebarWidth = Mth.clamp((int)event.x() + 1, 50, width - 50);
            resizeChildren();
            isDraggingVertical = true;
            return true;
        }

        for (GuiEventListener element : children()) {
            if (element instanceof AbstractResearchView view && view.isMouseOver(event.x(), event.y())) {
                return view.mouseDragged(event, dx, dy);
            }
        }
        return false;
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        if (isDraggingHorizontal || isDraggingVertical) {
            isDraggingHorizontal = false;
            isDraggingVertical = false;
            return true;
        }
        return super.mouseReleased(event);
    }

    private boolean isWithinRange(int value, int target, int margin) {
        return value >= target - margin && value <= target + margin;
    }

    private void resizeChildren() {
        assert treeView != null;
        treeView.onResize();
        assert infoView != null;
        infoView.onResize();
        assert listView != null;
        listView.onResize();

        ResearcherConfigs.client.researchScreenSidebarWidth.validateAndSet(sidebarWidth);
        ResearcherConfigs.client.researchScreenInfoViewHeight.validateAndSet(infoViewHeight);
    }

    @Override
    public void extractBackground(final GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {}

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        client.setScreen(parent);
    }
}
