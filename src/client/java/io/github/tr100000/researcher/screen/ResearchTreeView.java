package io.github.tr100000.researcher.screen;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.github.tr100000.researcher.ClientResearchTracker;
import io.github.tr100000.researcher.Research;
import io.github.tr100000.researcher.Researcher;
import io.github.tr100000.researcher.config.ResearcherClientConfig;
import io.github.tr100000.researcher.config.ResearcherConfigs;
import io.github.tr100000.researcher.graph.GraphLayout;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.input.MouseButtonEvent;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class ResearchTreeView extends AbstractResearchView implements ScrollableView {
    private final BiMap<GraphLayout.RenderedNode, ResearchNodeWidget> renderedNodeWidgets = HashBiMap.create();
    private GraphLayout.@Nullable RenderedGraph renderedGraph;
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
        this.scissorRect = new ScreenRectangle(x, y, width, height);
    }

    public void initWith(Research research) {
        clearChildren();
        offsetX = 0;
        offsetY = 0;

        final int nodeSize = 48;
        final int centeredNodeSize = 64;

        GraphLayout graphLayout = new GraphLayout(researchTracker.getGraph());
        Map<Research, Integer> researchDepthMap;

        switch (ResearcherConfigs.client.researchTreeMode.get()) {
            case DIRECTLY_RELATED:
                researchDepthMap = new Object2IntOpenHashMap<>();
                researchTracker.directPredecessorsOf(research).forEach(r -> researchDepthMap.put(r, -1));
                researchDepthMap.put(research, 0);
                researchTracker.directSuccessorsOf(research).forEach(r -> researchDepthMap.put(r, 1));
                break;
            case ALL_RELATED:
                researchDepthMap = researchTracker.getRelatedMap(research, researchTracker::getDepthMapPredecessors, ResearcherConfigs.client.discoveryResearchMode.get() ? researchTracker::getResearchableSuccessors : researchTracker::getDepthMapSuccessors);
                break;
            case ALL:
                researchDepthMap = graphLayout.createNodeLayerMap();
                break;
            default:
                researchDepthMap = new Object2IntOpenHashMap<>();
                break;
        }

        GraphLayout.Settings settings = new GraphLayout.Settings(research, nodeSize, centeredNodeSize, 2, 4, 8, 4, 4);
        renderedGraph = graphLayout.render(researchDepthMap, settings);

        for (GraphLayout.RenderedNode node : renderedGraph.nodes()) {
            Research r = node.researchOrThrow();
            ResearchNodeWidget widget = new ResearchNodeWidget(parent, this, node.x() - node.width() / 2, node.y() - node.height() / 2, node.width(), node.height(), node.layer(), r);
            nodeWidgets.put(r, widget);
            renderedNodeWidgets.put(node, widget);
            addDrawableChild(widget);

            if (ResearcherConfigs.client.researchTreeMode.get() == ResearcherClientConfig.ResearchTreeMode.DIRECTLY_RELATED) {
                (researchDepthMap.get(r) < 0 ? topNodes : bottomNodes).add(widget);
            }
        }

        for (GraphLayout.RenderedEdge edge : renderedGraph.edges()) {
            ResearchNodeWidget from = renderedNodeWidgets.get(edge.from());
            ResearchNodeWidget to = renderedNodeWidgets.get(edge.to());
            successorConnections.put(from, to);
            predecessorConnections.put(to, from);
        }

        GraphLayout.RenderedNode centerNode = renderedGraph.centeredNode();
        if (centerNode != null) {
            offsetX = width / 2.0 - centerNode.x();
            offsetY = height / 2.0 - centerNode.y();
        }

        Researcher.LOGGER.debug("Rendered graph with {} nodes and {} edges", renderedGraph.nodes().size(), renderedGraph.edges().size());
    }

    private GraphLayout.@Nullable RenderedEdge getRenderedEdge(ResearchNodeWidget from, ResearchNodeWidget to) {
        if (renderedGraph == null) return null;
        for (GraphLayout.RenderedEdge c : renderedGraph.edges()) {
            if (c.matches(from.research, to.research)) return c;
        }
        return null;
    }

    @Override
    public void renderView(GuiGraphics draw, int mouseX, int mouseY, float delta) {
        draw.pose().translate(getOffsetX(), getOffsetY());

        int newMouseX = mouseX - getOffsetX();
        int newMouseY = mouseY - getOffsetY();

        ResearchNodeWidget hovered = getCurrentHovered(draw, newMouseX, newMouseY);
        if (hovered != null) {
            highlightConnected(draw, hovered);
        }
        renderConnections(draw, hovered);

        super.renderView(draw, newMouseX, newMouseY, delta);

    }

    private @Nullable ResearchNodeWidget getCurrentHovered(GuiGraphics draw, int mouseX, int mouseY) {
        if (!draw.containsPointInScissor(mouseX + getOffsetX(), mouseY + getOffsetY())) return null;

        for (ResearchNodeWidget node : nodeWidgets.values()) {
            if (node.isMouseOver(mouseX, mouseY)) {
                return node;
            }
        }
        return null;
    }

    private void renderConnections(GuiGraphics draw, @Nullable ResearchNodeWidget currentHovered) {
        if (renderedGraph == null) return;

        drawFakeConnections(draw, currentHovered);

        renderedGraph.edges().forEach(edge -> drawConnection(draw, edge, CONNECTION_COLOR));

        if (currentHovered != null) {
            successorConnections.get(currentHovered).forEach(node -> drawConnection(draw, currentHovered, node, CONNECTION_COLOR_HOVERED));
            predecessorConnections.get(currentHovered).forEach(node -> drawConnection(draw, node, currentHovered, CONNECTION_COLOR_HOVERED));
        }
    }

    private void drawConnection(GuiGraphics draw, ResearchNodeWidget from, ResearchNodeWidget to, int color) {
        GraphLayout.RenderedEdge edge = getRenderedEdge(from, to);
        if (edge == null) return;
        drawConnection(draw, edge, color);
    }

    private void drawConnection(GuiGraphics draw, GraphLayout.RenderedEdge edge, int color) {
        for (GraphLayout.RenderedEdgeSegment segment : edge.segments()) {
            draw.vLine(segment.fromX(), segment.fromY(), segment.midY(), color); // down
            draw.hLine(segment.fromX(), segment.toX(), segment.midY(), color); // right
            draw.vLine(segment.toX(), segment.midY(), segment.toY(), color); // down
        }
    }

    private void highlightConnected(GuiGraphics draw, ResearchNodeWidget node) {
        successorConnections.get(node).forEach(successor -> drawHighlight(draw, successor));
        predecessorConnections.get(node).forEach(predecessor -> drawHighlight(draw, predecessor));
        drawHighlight(draw, node);
    }

    private void drawFakeConnections(GuiGraphics draw, @Nullable ResearchNodeWidget currentHovered) {
        final int length = 20;

        for (ResearchNodeWidget node : topNodes) {
            int x = node.getX() + node.getWidth() / 2;
            int startY = node.getY();
            int endY = startY - length;
            drawFakeConnection(draw, x, startY, endY, node == currentHovered ? CONNECTION_COLOR_HOVERED : CONNECTION_COLOR);
        }

        for (ResearchNodeWidget node : bottomNodes) {
            int x = node.getX() + node.getWidth() / 2;
            int startY = node.getY() + node.getHeight();
            int endY = startY + length;
            drawFakeConnection(draw, x, startY, endY, node == currentHovered ? CONNECTION_COLOR_HOVERED : CONNECTION_COLOR);
        }
    }

    private void drawFakeConnection(GuiGraphics draw, int x, int startY, int endY, int color) {
        final int segmentCount = 3;
        final int height = endY - startY;
        final int segmentHeight = height / segmentCount;

        // Draw segments
        for (int i = 0; i < segmentCount; i += 2) {
            int segmentStartY = startY + segmentHeight * i;
            int segmentEndY = (int)(segmentStartY + (segmentHeight + 1.25F));
            draw.vLine(x, segmentStartY, segmentEndY, color);
        }
    }

    private void drawHighlight(GuiGraphics draw, ResearchNodeWidget node) {
        draw.renderOutline(node.getX() - 1, node.getY() - 1, node.getWidth() + 2, node.getHeight() + 2, CONNECTION_COLOR_HOVERED);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX - getOffsetX(), mouseY - getOffsetY());
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
        if (event.button() == 0) {
            this.offsetX += dx;
            this.offsetY += dy;
            return true;
        }
        else {
            return super.mouseDragged(new MouseButtonEvent(event.x() - getOffsetX(), event.y() - getOffsetY(), event.buttonInfo()), dx, dy);
        }
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
        offsetX += horizontalAmount * ResearcherConfigs.client.researchTreeScrollSensitivity.get();
        offsetY += verticalAmount * ResearcherConfigs.client.researchTreeScrollSensitivity.get();
        return true;
    }

    @Override
    public int getOffsetX() {
        return (int)offsetX + getX();
    }

    @Override
    public int getOffsetY() {
        return (int)offsetY + getY();
    }

    @Override
    public void clearChildren() {
        super.clearChildren();
        renderedGraph = null;
        renderedNodeWidgets.clear();
        nodeWidgets.clear();
        successorConnections.clear();
        predecessorConnections.clear();
        topNodes.clear();
        bottomNodes.clear();
    }
}
