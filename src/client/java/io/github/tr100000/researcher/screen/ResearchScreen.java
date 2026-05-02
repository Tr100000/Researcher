package io.github.tr100000.researcher.screen;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import io.github.tr100000.researcher.ClientResearchTracker;
import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.Research;
import io.github.tr100000.researcher.Researcher;
import io.github.tr100000.researcher.config.ResearcherConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
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

    protected ResearchTreeView treeView = null;
    protected ResearchInfoView infoView = null;
    protected ResearchListView listView = null;
    protected Button zoomInButton = null;
    protected Button zoomOutButton = null;

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

    private static boolean allowResize() {
        return ResearcherConfigs.client.researchScreenAllowResize.get();
    }

    private static boolean allowZoom() {
        return ResearcherConfigs.client.researchScreenAllowZoom.get();
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

        if (allowZoom())
            initTreeViewZoomButtons();

        assert selected != null;
        initWith(selected);
    }

    public void initWith(Research current) {
        clearWidgets();
        setSelected(current);

        addRenderableWidget(infoView);
        infoView.initWith(current);

        addRenderableWidget(listView);

        addRenderableWidget(treeView);
        treeView.initWith(current);

        if (allowZoom()) {
            addRenderableWidget(zoomInButton);
            addRenderableWidget(zoomOutButton);
            updateZoomButtons();
        }

        setInitialFocus(treeView);
    }

    private void initTreeViewZoomButtons() {
        zoomInButton = new ResearchTreeZoomButton(sidebarWidth + 5, height - 40, ModUtils.getScreenTranslated("zoom_in"), _ -> {
                    treeView.zoomIn();
                    updateZoomButtons();
                });
        zoomOutButton = new ResearchTreeZoomButton(sidebarWidth + 5, height - 20, ModUtils.getScreenTranslated("zoom_out"), _ -> {
                    treeView.zoomOut();
                    updateZoomButtons();
                });
        updateZoomButtons();
    }

    @Override
    public void extractRenderState(final GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        extractBlurredBackground(graphics);
        extractMenuBackground(graphics);
        if (selected != null) {
            super.extractRenderState(graphics, mouseX, mouseY, delta);

            if (allowResize()) {
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
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
        if (allowResize()) {
            if ((isDraggingHorizontal && !isDraggingVertical) || (isWithinRange((int) event.y(), infoViewHeight - 1, 1) && event.x() < sidebarWidth)) {
                infoViewHeight = Mth.clamp((int) event.y() + 1, 50, height - 50);
                resizeChildren();
                isDraggingHorizontal = true;
                return true;
            }
            if ((isDraggingVertical && !isDraggingHorizontal) || isWithinRange((int) event.x(), sidebarWidth - 1, 1)) {
                sidebarWidth = Mth.clamp((int) event.x() + 1, 50, width - 50);
                resizeChildren();
                isDraggingVertical = true;
                return true;
            }
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
        treeView.onResize();
        infoView.onResize();
        listView.onResize();

        if (allowZoom()) {
            zoomInButton.setPosition(sidebarWidth + 5, height - 40);
            zoomInButton.setSize(15, 15);
            zoomOutButton.setPosition(sidebarWidth + 5, height - 20);
            zoomOutButton.setSize(15, 15);
        }

        ResearcherConfigs.client.researchScreenSidebarWidth.validateAndSet(sidebarWidth);
        ResearcherConfigs.client.researchScreenInfoViewHeight.validateAndSet(infoViewHeight);
    }

    @Override
    public void mouseMoved(double x, double y) {
        if (allowZoom()) {
            if (checkZoomButtonNotHovered(x, y, zoomInButton) && checkZoomButtonNotHovered(x, y, zoomOutButton)) {
                listView.setIsHovered(true);
                treeView.setIsHovered(true);
            }
        }
        else {
            listView.setIsHovered(true);
            treeView.setIsHovered(true);
        }
    }

    private boolean checkZoomButtonNotHovered(double mouseX, double mouseY, Button button) {
        if (button.isMouseOver(mouseX, mouseY)) {
            listView.setIsHovered(false);
            treeView.setIsHovered(false);
            return false;
        }
        else return true;
    }

    private void updateZoomButtons() {
        zoomInButton.active = treeView.canZoomIn();
        zoomOutButton.active = treeView.canZoomOut();
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
