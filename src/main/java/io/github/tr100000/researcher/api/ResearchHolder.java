package io.github.tr100000.researcher.api;

import com.google.common.collect.ImmutableSet;
import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.Research;
import io.github.tr100000.researcher.criterion.ResearchItemsTrigger;
import io.github.tr100000.researcher.graph.ResearchGraph;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public interface ResearchHolder {
    boolean isInitialized();
    Map<Identifier, Research> getAll();
    ResearchGraph getGraph();
    @Nullable Research get(@Nullable Identifier id);
    @Nullable Identifier getId(@Nullable Research research);
    boolean isRecipeUnlockable(Identifier recipeId);
    ResearchItemsTrigger.@Nullable TriggerInstance getResearchConditions(Research research);

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

    default List<Research> directPredecessorsOf(Research research) {
        return getGraph().predecessors(research);
    }

    default Set<Research> getDepthMapPredecessors(Research research) {
        return new ObjectOpenHashSet<>(directPredecessorsOf(research));
    }

    default Set<Research> allPredecessorsOf(@Nullable Research research) {
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

    default List<Research> directSuccessorsOf(Research research) {
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

    default Map<Research, Integer> getRelatedMap(Research research) {
        return getRelatedMap(research, this::getDepthMapPredecessors, this::getDepthMapSuccessors);
    }

    default Map<Research, Integer> getRelatedMap(Research research, Function<Research, Set<Research>> researchToPredecessors, Function<Research, Set<Research>> researchToSuccessors) {
        Map<Research, Integer> relatedMap = new Object2IntOpenHashMap<>();

        relatedMap.putAll(predecessorDepthMap(research, researchToPredecessors));
        relatedMap.put(research, 0);
        relatedMap.putAll(successorDepthMap(research, researchToSuccessors));

        return relatedMap;
    }

    default Set<Research> getRootNodes() {
        return new ObjectOpenHashSet<>(getGraph().rootNodes());
    }

    default boolean isValid(Research research) {
        return getGraph().containsNode(research);
    }
}
