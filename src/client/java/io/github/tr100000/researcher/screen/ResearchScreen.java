package io.github.tr100000.researcher.screen;

import io.github.tr100000.researcher.ClientResearchTracker;
import io.github.tr100000.researcher.Research;
import io.github.tr100000.researcher.Researcher;
import io.github.tr100000.researcher.config.ResearcherConfigs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class ResearchScreen extends Screen {
    protected static final MinecraftClient client = MinecraftClient.getInstance();

    public static int sidebarWidth;
    public static int infoViewHeight;

    public static @Nullable Research selected;

    protected final ClientResearchTracker researchManager = client.getNetworkHandler().researcher$getClientTracker();

    public final Screen parent;

    protected ResearchTreeView treeView;
    protected ResearchInfoView infoView;
    protected ResearchListView listView;

    public ResearchScreen(Screen parent) {
        super(Text.translatable("screen.researcher"));
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
            close();
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
        clearChildren();
        setSelected(current);

        addDrawableChild(infoView);
        infoView.initWith(current);
        addDrawableChild(listView);

        treeView = addDrawableChild(new ResearchTreeView(this, width - sidebarWidth, height));
        treeView.initWith(current);

        setInitialFocus(treeView);
    }

    @Override
    public void render(DrawContext draw, int mouseX, int mouseY, float delta) {
        applyBlur(draw);
        renderDarkening(draw);
        if (selected != null) super.render(draw, mouseX, mouseY, delta);
    }

    @Override
    public void renderBackground(DrawContext draw, int mouseX, int mouseY, float delta) {}

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void close() {
        client.setScreen(parent);
    }
}
