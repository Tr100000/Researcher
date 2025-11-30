package io.github.tr100000.researcher.api;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.graph.Graph;
import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.Research;
import io.github.tr100000.researcher.criteria.ResearchItemsCriterion;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

@SuppressWarnings("UnstableApiUsage")
public interface ResearchHolder {
    boolean isInitialized();
    Map<Identifier, Research> getAll();
    Graph<Research> getGraph();
    @Nullable Research get(Identifier id);
    @Nullable Identifier getId(Research research);
    boolean isRecipeUnlockable(Identifier recipeId);
    @Nullable ResearchItemsCriterion.Conditions getResearchConditions(Research research);

    default Collection<Research> listAll() {
        return getAll().values();
    }

    default Set<Research> listAllAsSet() {
        return ImmutableSet.copyOf(getAll().values());
    }

    default Set<Identifier> listAllIds() {
        return getAll().keySet();
    }

    default Identifier getIdOrEmpty(Research research) {
        Identifier id = getId(research);
        if (id == null) id = ModUtils.id("");
        return id;
    }

    default Identifier getIdOrThrow(Research research) {
        Identifier id = getId(research);
        return Objects.requireNonNull(id, "id must not be null");
    }

    @Nullable
    default Research getUnlockingResearch(Identifier recipeId) {
        for (Research research : listAll()) {
            if (research.recipeUnlocks().contains(recipeId)) {
                return research;
            }
        }
        return null;
    }

    default Set<Research> directPredecessorsOf(Research research) {
        return getGraph().predecessors(research);
    }

    default Set<Research> getDepthMapPredecessors(Research research) {
        return new ObjectOpenHashSet<>(directPredecessorsOf(research));
    }

    default Set<Research> allPredecessorsOf(Research research) {
        Set<Research> predecessors = new ObjectOpenHashSet<>(getGraph().predecessors(research));
        if (!predecessors.isEmpty()) {
            predecessors.forEach(node -> predecessors.addAll(allPredecessorsOf(node)));
        }
        return predecessors;
    }

    private Map<Research, Integer> predecessorDepthMapImpl(Research research, int depth, Map<Research, Integer> depthMap, Function<Research, Set<Research>> researchToPredecessors) {
        Set<Research> predecessors = new ObjectOpenHashSet<>(researchToPredecessors.apply(research));
        if (!predecessors.isEmpty()) {
            predecessors.forEach(node -> {
                if (!depthMap.containsKey(node) || depthMap.get(node) > depth - 1) {
                    depthMap.put(node, depth - 1);
                }
                predecessorDepthMapImpl(node, depth - 1, depthMap, researchToPredecessors);
            });
        }
        return depthMap;
    }

    default Map<Research, Integer> predecessorDepthMap(Research research) {
        return predecessorDepthMapImpl(research, 0, new Object2IntLinkedOpenHashMap<>(), this::getDepthMapPredecessors);
    }

    default Map<Research, Integer> predecessorDepthMap(Research research, Function<Research, Set<Research>> researchToPredecessors) {
        return predecessorDepthMapImpl(research, 0, new Object2IntLinkedOpenHashMap<>(), researchToPredecessors);
    }

    default Set<Research> directSuccessorsOf(Research research) {
        return getGraph().successors(research);
    }

    default Set<Research> getDepthMapSuccessors(Research research) {
        return new ObjectOpenHashSet<>(directSuccessorsOf(research));
    }

    default Set<Research> allSuccessorsOf(Research research) {
        Set<Research> successors = new ObjectOpenHashSet<>(getGraph().successors(research));
        if (!successors.isEmpty()) {
            successors.forEach(node -> successors.addAll(allSuccessorsOf(node)));
        }
        return successors;
    }

    private Map<Research, Integer> successorDepthMapImpl(Research research, int depth, Map<Research, Integer> depthMap, Function<Research, Set<Research>> researchToSuccessors) {
        Set<Research> successors = new ObjectOpenHashSet<>(researchToSuccessors.apply(research));
        if (!successors.isEmpty()) {
            successors.forEach(node -> {
                if (!depthMap.containsKey(node) || depthMap.get(node) < depth + 1) {
                    depthMap.put(node, depth + 1);
                }
                successorDepthMapImpl(node, depth + 1, depthMap, researchToSuccessors);
            });
        }
        return depthMap;
    }

    default Map<Research, Integer> successorDepthMap(Research research) {
        return successorDepthMapImpl(research, 0, new Object2IntLinkedOpenHashMap<>(), this::getDepthMapSuccessors);
    }

    default Map<Research, Integer> successorDepthMap(Research research, Function<Research, Set<Research>> researchToSuccessors) {
        return successorDepthMapImpl(research, 0, new Object2IntLinkedOpenHashMap<>(), researchToSuccessors);
    }

    default Multimap<Integer, Research> getRelatedMap(Research research) {
        return getRelatedMap(research, this::getDepthMapPredecessors, this::getDepthMapSuccessors);
    }

    default Multimap<Integer, Research> getRelatedMap(Research research, Function<Research, Set<Research>> researchToPredecessors, Function<Research, Set<Research>> researchToSuccessors) {
        ImmutableListMultimap.Builder<Integer, Research> builder = ImmutableListMultimap.builder();

        predecessorDepthMap(research, researchToPredecessors).forEach((predecessor, depth) -> builder.put(depth, predecessor));
        builder.put(0, research);
        successorDepthMap(research, researchToSuccessors).forEach((successor, depth) -> builder.put(depth, successor));

        return builder.build();
    }

    default Set<Research> getRootNodes() {
        return getGraph().nodes().stream().filter(node -> getGraph().inDegree(node) == 0).collect(ImmutableSet.toImmutableSet());
    }

    default boolean isValid(Research research) {
        return getGraph().nodes().contains(research);
    }
}
