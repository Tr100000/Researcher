package io.github.tr100000.researcher.screen;

import com.google.common.base.Predicates;
import io.github.tr100000.researcher.Research;
import io.github.tr100000.researcher.config.ResearcherConfigs;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

// TODO fix scrolling
public class ResearchListView extends AbstractResearchView implements ScrollableView {
    private static final int BACKGROUND_COLOR = 0xFF202020;
    private static final int BORDER_COLOR = 0xFF000000;

    private final List<ResearchNodeWidget> researchWidgets = new ObjectArrayList<>();
    private final Map<ResearchNodeWidget, String> researchTitles = new Object2ObjectOpenHashMap<>();

    private final Renderable searchFieldRenderable;

    private double offsetX;
    private double offsetY;

    public ResearchListView(ResearchScreen parent, int height) {
        super(parent, 0, ResearchScreen.infoViewHeight, ResearchScreen.sidebarWidth, height);
        this.scissorRect = new ScreenRectangle(0, y, parent.width, height);

        EditBox searchField = addChild(new EditBox(client.font, 4, ResearchScreen.infoViewHeight + 4, ResearchScreen.sidebarWidth - 8, 14, Component.translatable("screen.researcher.search")));
        searchField.setResponder(this::searchAndReposition);
        this.searchFieldRenderable = searchField;

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
    }

    @Override
    public void renderView(GuiGraphics draw, int mouseX, int mouseY, float delta) {
        draw.fill(0, getY() - 1, getWidth(), parent.height, BACKGROUND_COLOR);
        draw.renderOutline(0, getY() - 1, getWidth(), parent.height, BORDER_COLOR);
        searchFieldRenderable.render(draw, mouseX, mouseY, delta);

        draw.pose().translate(getOffsetX(), getOffsetY());

        int newMouseX = mouseX - getOffsetX();
        int newMouseY = mouseY - getOffsetY();

        super.renderView(draw, newMouseX, newMouseY, delta);
    }


    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX - getOffsetX(), mouseY - getOffsetY());
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
        return super.mouseDragged(new MouseButtonEvent(event.x() - getOffsetX(), event.y() - getOffsetY(), event.buttonInfo()), dx, dy);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubled) {
        return super.mouseClicked(new MouseButtonEvent(event.x() - getOffsetX(), event.y() - getOffsetY(), event.buttonInfo()), doubled);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        return super.mouseReleased(new MouseButtonEvent(event.x() - getOffsetX(), event.y() - getOffsetY(), event.buttonInfo()));
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
//        offsetX += horizontalAmount * ResearcherConfigs.client.researchTreeScrollSensitivity.get();
//        offsetY += verticalAmount * ResearcherConfigs.client.researchTreeScrollSensitivity.get();
        return true;
    }

    @Override
    public int getOffsetX() {
        return (int)offsetX;
    }

    @Override
    public int getOffsetY() {
        return (int)offsetY;
    }
}
