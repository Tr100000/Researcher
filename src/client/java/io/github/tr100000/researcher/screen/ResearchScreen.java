package io.github.tr100000.researcher.screen;

import io.github.tr100000.researcher.ClientResearchTracker;
import io.github.tr100000.researcher.Research;
import io.github.tr100000.researcher.Researcher;
import io.github.tr100000.researcher.config.ResearcherConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
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

        initWith(selected);
    }

    public void initWith(Research current) {
        clearWidgets();
        setSelected(current);

        addRenderableWidget(infoView);
        infoView.initWith(current);
        addRenderableWidget(listView);

        treeView = addRenderableWidget(new ResearchTreeView(this, width - sidebarWidth, height));
        treeView.initWith(current);

        setInitialFocus(treeView);
    }

    @Override
    public void render(GuiGraphics draw, int mouseX, int mouseY, float delta) {
        renderBlurredBackground(draw);
        renderMenuBackground(draw);
        if (selected != null) super.render(draw, mouseX, mouseY, delta);
    }

    @Override
    public void renderBackground(GuiGraphics draw, int mouseX, int mouseY, float delta) {}

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        client.setScreen(parent);
    }
}
