package io.github.tr100000.researcher.screen;

import com.google.common.base.Predicates;
import io.github.tr100000.researcher.Research;
import io.github.tr100000.researcher.config.ResearcherConfigs;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

// TODO fix scrolling
public class ResearchListView extends ResearchNodeContainingView {
    private static final int BACKGROUND_COLOR = 0xFF202020;
    private static final int BORDER_COLOR = 0xFF000000;

    private final List<ResearchNodeWidget> researchWidgets = new ObjectArrayList<>();
    private final Map<ResearchNodeWidget, String> researchTitles = new Object2ObjectOpenHashMap<>();

    private boolean isScrollable;
    private int maxScroll;
    private double offsetY;

    public ResearchListView(ResearchScreen parent, int height) {
        super(parent, 0, ResearchScreen.infoViewHeight, ResearchScreen.sidebarWidth, height);
        this.scissorRect = new ScreenRectangle(0, y, width, height);

        EditBox searchField = addDrawableChild(new EditBox(client.font, 4, ResearchScreen.infoViewHeight + 4, ResearchScreen.sidebarWidth - 8, 14, Component.translatable("screen.researcher.search")));
        searchField.setResponder(this::searchAndReposition);

        List<Research> researchList = new ObjectArrayList<>(parent.researchManager.listAll());
        researchList.sort(Research.statusComparator(parent.researchManager).thenComparing(Research.idComparator(parent.researchManager)));
        for (Research research : researchList) {
            if (ResearcherConfigs.client.discoveryResearchMode.get() && !parent.researchManager.isAvailableOrFinished(research)) {
                continue;
            }

            ResearchNodeWidget widget = addDrawableChild(new ResearchNodeWidget(parent, this, 0, 0, 30, 30, research));
            researchWidgets.add(widget);
            researchTitles.put(widget, research.getTitle(parent.researchManager).getString().toUpperCase());
        }

        searchAndReposition("");
    }

    public void searchAndReposition(String text) {
        Predicate<CharSequence> predicate = Predicates.containsPattern(text.toUpperCase());

        int x = 4;
        int y = getY() + 22;
        for (ResearchNodeWidget widget : researchWidgets) {
            if (predicate.test(researchTitles.get(widget))) {
                widget.setPosition(x, y);

                x += 32;
                if (x > getWidth() - 30) {
                    x = 4;
                    y += 32;
                }
            } else {
                widget.setPosition(-100, -100);
            }
        }

        offsetY = 0;
        ScreenRectangle rect = getContentsRect();
        isScrollable = rect.height() > getHeight();
        maxScroll = rect.height() - getHeight() - ResearchScreen.infoViewHeight;
    }

    @Override
    public ScreenRectangle getContentsRect() {
        return children().stream()
                .filter(g -> !(g instanceof LayoutElement e && e.getX() < 0))
                .map(GuiEventListener::getRectangle)
                .reduce(ScreenRectangle.empty(), AbstractResearchView::rectUnionOf);
    }

    @Override
    public void visitWidgets(Consumer<AbstractWidget> consumer) {
        children().forEach(child -> {
            if (child instanceof AbstractWidget widgetChild && widgetChild.getX() > 0) {
                consumer.accept(widgetChild);
            }
        });
    }

    @Override
    public void extractView(final GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        graphics.fill(0, getY() - 1, getWidth(), parent.height, BACKGROUND_COLOR);
        graphics.outline(0, getY() - 1, getWidth(), parent.height, BORDER_COLOR);

        graphics.pose().translate(getOffsetX(), getOffsetY());

        int newMouseX = mouseX - getOffsetX();
        int newMouseY = mouseY - getOffsetY();

        super.extractView(graphics, newMouseX, newMouseY, delta);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (isScrollable) {
            offsetY += verticalAmount * ResearcherConfigs.client.researchTreeScrollSensitivity.get();
            offsetY = Mth.clamp(offsetY, -maxScroll, 0);
        }
        return true;
    }

    @Override
    public int getOffsetY() {
        return (int)offsetY;
    }
}
