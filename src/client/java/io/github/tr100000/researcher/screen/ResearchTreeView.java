package io.github.tr100000.researcher.screen;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.github.tr100000.researcher.ClientResearchTracker;
import io.github.tr100000.researcher.Research;
import io.github.tr100000.researcher.config.ResearcherConfigs;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public class ResearchTreeView extends AbstractResearchView implements ScrollableView {
    private final BiMap<Research, ResearchNodeWidget> nodeWidgets = HashBiMap.create();
    private final Multimap<ResearchNodeWidget, ResearchNodeWidget> successorConnections = HashMultimap.create();
    private final Multimap<ResearchNodeWidget, ResearchNodeWidget> predecessorConnections = HashMultimap.create();
    private final List<ResearchNodeWidget> topNodes = new ObjectArrayList<>();
    private final List<ResearchNodeWidget> bottomNodes = new ObjectArrayList<>();

    private double offsetX;
    private double offsetY;

    private final ClientResearchTracker researchTracker;

    private static final int CONNECTION_COLOR = 0xFF888888;
    private static final int CONNECTION_COLOR_HOVERED = 0xFFE0B804;

    public ResearchTreeView(ResearchScreen parent, int width, int height) {
        super(parent, ResearchScreen.sidebarWidth, 0, width, height);
        this.researchTracker = parent.researchManager;
        this.scissorRect = new ScreenRect(x, y, width, height);
    }

    public void initWith(Research research) {
        clearChildren();
        offsetX = 0;
        offsetY = 0;

        addRootResearchNode(research);

        switch (ResearcherConfigs.client.researchTreeMode.get()) {
            case DIRECTLY_RELATED:
                addResearchNodes(researchTracker.directPredecessorsOf(research), -1, x -> !researchTracker.directPredecessorsOf(x).isEmpty());
                addResearchNodes(researchTracker.directSuccessorsOf(research), 1, x -> !researchTracker.directSuccessorsOf(x).isEmpty());
                break;
            case ALL_RELATED:
                Multimap<Integer, Research> map = researchTracker.getRelatedMap(research, researchTracker::getDepthMapPredecessors, ResearcherConfigs.client.discoveryResearchMode.get() ? researchTracker::getResearchableSuccessors : researchTracker::getDepthMapSuccessors);
                map.asMap().forEach((i, nodeCollection) -> {
                    if (i != 0) {
                        addResearchNodes(nodeCollection, i, x -> false);
                    }
                });
                break;
        }

        initConnections();
    }

    private void addRootResearchNode(Research research) {
        nodeWidgets.put(research, addDrawableChild(new ResearchNodeWidget(parent, this, getX() + width / 2 - 32, height / 2 - 32, research, true)));
    }

    private void addResearchNodes(Collection<Research> nodeCollection, int depth, Predicate<Research> shouldAddFakeConnections) {
        List<Research> nodes = new ObjectArrayList<>(nodeCollection);
        nodes.sort(Research.idComparator(researchTracker));

        final int heightPerLevel = 96;
        final int widthPerNode = 64;

        int j = 0;
        for (Research node : nodes) {
            int x = getX() + Math.round(width / 2f - 24 + (j - ((nodes.size() - 1) / 2f)) * widthPerNode);
            int y = height / 2 - 24 + heightPerLevel * depth;
            ResearchNodeWidget nodeWidget = addDrawableChild(new ResearchNodeWidget(parent, this, x, y, node));
            j++;

            nodeWidgets.put(node, nodeWidget);
            if (shouldAddFakeConnections.test(node)) {
                if (depth > 0) {
                    bottomNodes.add(nodeWidget);
                }
                else {
                    topNodes.add(nodeWidget);
                }
            }
        }
    }

    private void initConnections() {
        nodeWidgets.forEach((research, widget) -> {
            researchTracker.directSuccessorsOf(research).forEach(successor -> {
                if (nodeWidgets.containsKey(successor)) {
                    successorConnections.put(widget, nodeWidgets.get(successor));
                }
            });
            researchTracker.directPredecessorsOf(research).forEach(successor -> {
                if (nodeWidgets.containsKey(successor)) {
                    predecessorConnections.put(widget, nodeWidgets.get(successor));
                }
            });
        });
    }

    @Override
    public void renderView(DrawContext draw, int mouseX, int mouseY, float delta) {
        draw.getMatrices().translate((float)offsetX, (float)offsetY);

        int newMouseX = mouseX - (int)offsetX;
        int newMouseY = mouseY - (int)offsetY;

        ResearchNodeWidget hovered = getCurrentHovered(newMouseX, newMouseY);
        renderConnections(draw, hovered);
        if (hovered != null) {
            highlightConnected(draw, hovered);
        }

        super.renderView(draw, newMouseX, newMouseY, delta);
    }

    private @Nullable ResearchNodeWidget getCurrentHovered(int mouseX, int mouseY) {
        for (ResearchNodeWidget node : nodeWidgets.values()) {
            if (node.isMouseOver(mouseX, mouseY)) {
                return node;
            }
        }
        return null;
    }

    private void renderConnections(DrawContext draw, ResearchNodeWidget currentHovered) {
        drawFakeConnections(draw, currentHovered);

        successorConnections.forEach((from, to) -> {
            int color = from == currentHovered || to == currentHovered ? CONNECTION_COLOR_HOVERED : CONNECTION_COLOR;
            drawConnection(draw, from, to, color);
        });
        if (currentHovered != null) {
            successorConnections.get(currentHovered).forEach(node -> drawConnection(draw, currentHovered, node, CONNECTION_COLOR_HOVERED));
            predecessorConnections.get(currentHovered).forEach(node -> drawConnection(draw, node, currentHovered ,CONNECTION_COLOR_HOVERED));
        }
    }

    private void drawConnection(DrawContext draw, ResearchNodeWidget from, ResearchNodeWidget to, int color) {
        if (from.getY() > to.getY()) drawConnection(draw, to, from, color);
        int startX = from.getX() + from.getWidth() / 2;
        int startY = from.getY() + from.getHeight() - 1;
        int endX = to.getX() + to.getWidth() / 2;
        int endY = to.getY();
        int midY = (startY + endY) / 2;
        draw.drawVerticalLine(startX, startY, midY, color);
        draw.drawHorizontalLine(startX, endX, midY, color);
        draw.drawVerticalLine(endX, midY, endY, color);
    }

    private void highlightConnected(DrawContext draw, ResearchNodeWidget node) {
        successorConnections.get(node).forEach(successor -> drawHighlight(draw, successor));
        predecessorConnections.get(node).forEach(predecessor -> drawHighlight(draw, predecessor));
        drawHighlight(draw, node);
    }

    private void drawFakeConnections(DrawContext draw, ResearchNodeWidget currentHovered) {
        final int length = 20;

        topNodes.forEach(node -> {
            int x = node.getX() + node.getWidth() / 2;
            int startY = node.getY();
            int endY = startY - length;
            drawFakeConnection(draw, x, startY, endY, node == currentHovered ? CONNECTION_COLOR_HOVERED : CONNECTION_COLOR);
        });

        bottomNodes.forEach(node -> {
            int x = node.getX() + node.getWidth() / 2;
            int startY = node.getY() + node.getHeight();
            int endY = startY + length;
            drawFakeConnection(draw, x, startY, endY, node == currentHovered ? CONNECTION_COLOR_HOVERED : CONNECTION_COLOR);
        });
    }

    private void drawFakeConnection(DrawContext draw, int x, int startY, int endY, int color) {
        final int segmentCount = 3;
        final int height = endY - startY;
        final int segmentHeight = height / segmentCount;

        // Draw segments
        for (int i = 0; i < segmentCount; i += 2) {
            int segmentStartY = startY + segmentHeight * i;
            int segmentEndY = (int)(segmentStartY + (segmentHeight + 1.25F));
            draw.drawVerticalLine(x, segmentStartY, segmentEndY, color);
        }
    }

    private void drawHighlight(DrawContext draw, ResearchNodeWidget node) {
        draw.drawBorder(node.getX() - 1, node.getY() - 1, node.getWidth() + 2, node.getHeight() + 2, CONNECTION_COLOR_HOVERED);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (button == 0 || button == 2) {
            offsetX += deltaX;
            offsetY += deltaY;
            return true;
        }
        return super.mouseDragged(mouseX - (int)offsetX, mouseY - (int)offsetY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX - (int)offsetX, mouseY - (int)offsetY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return super.mouseReleased(mouseX - (int)offsetX, mouseY - (int)offsetY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        offsetX += horizontalAmount * ResearcherConfigs.client.researchTreeScrollSensitivity.get();
        offsetY += verticalAmount * ResearcherConfigs.client.researchTreeScrollSensitivity.get();
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

    @Override
    public void clearChildren() {
        super.clearChildren();
        successorConnections.clear();
        nodeWidgets.clear();
    }
}
