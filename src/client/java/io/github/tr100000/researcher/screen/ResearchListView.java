package io.github.tr100000.researcher.screen;

import com.google.common.base.Predicates;
import io.github.tr100000.researcher.Research;
import io.github.tr100000.researcher.config.ResearcherConfigs;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ResearchListView extends AbstractResearchView implements ScrollableView {
    private static final int BACKGROUND_COLOR = 0xFF202020;
    private static final int BORDER_COLOR = 0xFF000000;

    private final List<ResearchNodeWidget> researchWidgets = new ObjectArrayList<>();
    private final Map<ResearchNodeWidget, String> researchTitles = new Object2ObjectOpenHashMap<>();

    public ResearchListView(ResearchScreen parent, int height) {
        super(parent, 0, ResearchScreen.infoViewHeight, ResearchScreen.sidebarWidth, height);
        this.scissorRect = new ScreenRect(0, y, parent.width, height);

        TextFieldWidget searchField = addDrawableChild(new TextFieldWidget(client.textRenderer, 4, ResearchScreen.infoViewHeight + 4, ResearchScreen.sidebarWidth - 8, 14, Text.translatable("screen.researcher.search")));
        searchField.setChangedListener(this::searchAndReposition);

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
    public void renderView(DrawContext draw, int mouseX, int mouseY, float delta) {
        draw.fill(0, getY() - 1, getWidth(), parent.height, BACKGROUND_COLOR);
        draw.drawStrokedRectangle(0, getY() - 1, getWidth(), parent.height, BORDER_COLOR);
        super.renderView(draw, mouseX, mouseY, delta);
    }
}
