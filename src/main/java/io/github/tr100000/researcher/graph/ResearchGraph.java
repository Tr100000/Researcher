package io.github.tr100000.researcher.graph;

import io.github.tr100000.researcher.Research;
import io.github.tr100000.researcher.Researcher;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ResearchGraph {
    public static final ResearchGraph EMPTY = new ResearchGraph();

    private final List<Research> nodes = new ObjectArrayList<>();
    private final List<Edge> edges = new ObjectArrayList<>();
    private final Map<Research, List<Research>> downward = new Object2ObjectOpenHashMap<>();
    private final Map<Research, List<Research>> upward = new Object2ObjectOpenHashMap<>();
    private final List<Research> rootNodes = new ObjectArrayList<>();
    private final Map<Research, Identifier> nodeIds = new Object2ObjectOpenHashMap<>();

    private ResearchGraph() {}

    public static ResearchGraph build(Map<Identifier, Research> map) {
        ResearchGraph graph = new ResearchGraph();

        for (Map.Entry<Identifier, Research> entry : map.entrySet()) {
            Identifier id = entry.getKey();
            Research node = entry.getValue();
            graph.nodes.add(node);
            graph.nodeIds.put(node, id);
            for (Identifier prerequisiteId : node.prerequisiteIds()) {
                if (!map.containsKey(prerequisiteId)) {
                    Researcher.LOGGER.warn("Research {} has a nonexistent prerequisite {}", id, prerequisiteId);
                    continue;
                }
                if (id == prerequisiteId) {
                    Researcher.LOGGER.warn("Research {} depends on itself", id);
                    continue;
                }

                Research prerequisite = map.get(prerequisiteId);
                graph.edges.add(new Edge(prerequisite, node));
                graph.downward.computeIfAbsent(prerequisite, ignored -> new ObjectArrayList<>());
                graph.downward.get(prerequisite).add(node);
                graph.upward.computeIfAbsent(node, ignored -> new ObjectArrayList<>());
                graph.upward.get(node).add(prerequisite);
            }
        }

        for (Research node : graph.nodes) {
            if (!graph.upward.containsKey(node)) {
                graph.rootNodes.add(node);
            }
        }

        // Validation
        if (Researcher.LOGGER.isWarnEnabled()) {
            checkForCycles(graph);
            checkForRedundantEdges(graph, map);
        }

        return graph;
    }

    private static void checkForCycles(ResearchGraph graph) {
        Set<Research> marked = new ObjectOpenHashSet<>();
        Deque<Research> stack = new ArrayDeque<>();

        for (Research node : graph.nodes) {
            checkForCyclesImpl(graph, node, marked, stack);
        }
    }

    private static void checkForCyclesImpl(ResearchGraph graph, Research node, Set<Research> marked, Deque<Research> stack) {
        if (marked.contains(node)) return;

        marked.add(node);
        stack.push(node);

        for (Research successor : graph.successors(node)) {
            if (stack.contains(successor)) {
                Researcher.LOGGER.warn("Cycle found in research graph: {}", stack.stream().map(graph.nodeIds::get).map(Identifier::toString).collect(Collectors.joining("->")));
            }
            else if (!marked.contains(successor)) {
                checkForCyclesImpl(graph, successor, marked, stack);
            }
        }

        stack.pop();
    }

    private static void checkForRedundantEdges(ResearchGraph graph, Map<Identifier, Research> map) {
        for (Research node : graph.nodes) {
            for (Identifier parentId : node.prerequisiteIds()) {
                Research parent = map.get(parentId);
                if (checkForAlternatePaths(graph, parent, node)) {
                    Researcher.LOGGER.warn("Redundant edge found: {} -> {}", graph.nodeIds.get(parent), graph.nodeIds.get(node));
                }
            }
        }
    }

    private static boolean checkForAlternatePaths(ResearchGraph graph, Research from, Research to) {
        Set<Research> marked = new ObjectOpenHashSet<>();
        Deque<Research> stack = new ArrayDeque<>();
        stack.push(from);

        while (!stack.isEmpty()) {
            Research current = stack.pop();
            if (!marked.add(current)) continue;

            for (Research successor : graph.successors(current)) {
                if (current == from && successor == to) continue;

                if (successor == to) return true;

                stack.push(successor);
            }
        }

        return false;
    }

    public List<Research> nodes() {
        return Collections.unmodifiableList(nodes);
    }

    public List<Edge> edges() {
        return Collections.unmodifiableList(edges);
    }

    public List<Research> rootNodes() {
        return Collections.unmodifiableList(rootNodes);
    }

    public List<Research> predecessors(@Nullable Research node) {
        return Collections.unmodifiableList(upward.getOrDefault(node, Collections.emptyList()));
    }

    public List<Research> successors(@Nullable Research node) {
        return Collections.unmodifiableList(downward.getOrDefault(node, Collections.emptyList()));
    }

    public int inDegree(@Nullable Research node) {
        return predecessors(node).size();
    }

    public int outDegree(@Nullable Research node) {
        return successors(node).size();
    }

    public boolean containsNode(@Nullable Research node) {
        return nodes.contains(node);
    }

    public List<Research> getTopologicalOrder() {
        Map<Research, Integer> inDegreeMap = new Object2IntOpenHashMap<>(nodes.size());
        for (Research node : nodes) {
            inDegreeMap.put(node, inDegree(node));
        }
        Deque<Research> deque = new ArrayDeque<>(rootNodes);

        List<Research> order = new ObjectArrayList<>();

        while (!deque.isEmpty()) {
            Research node = deque.pop();
            order.add(node);
            for (Research child : successors(node)) {
                inDegreeMap.put(child, inDegreeMap.get(child) - 1);
                if (inDegreeMap.get(child) == 0) {
                    deque.add(child);
                }
            }
        }

        assert nodes.size() == order.size();

        return order;
    }

    public record Edge(Research parent, Research child) {}
}
