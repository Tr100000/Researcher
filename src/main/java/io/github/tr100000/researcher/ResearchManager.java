package io.github.tr100000.researcher;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.ImmutableGraph;
import io.github.tr100000.researcher.api.ResearchHolder;
import io.github.tr100000.researcher.config.ResearcherConfigs;
import io.github.tr100000.researcher.criteria.ResearchItemsCriterion;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.server.DataPackContents;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.profiler.Profiler;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

@SuppressWarnings("UnstableApiUsage")
public class ResearchManager extends JsonDataLoader<Research> implements ResearchHolder {
    public static final String PATH = "research";
    public static final Identifier ID = Researcher.id(PATH);
    private final DataPackContents parent;
    private final BiMap<Identifier, Research> researchMap = HashBiMap.create();
    private final Set<Identifier> unlockableRecipes = new ObjectOpenHashSet<>();
    private final Map<Research, ResearchItemsCriterion.Conditions> researchConditions = new Object2ObjectOpenHashMap<>();
    private Graph<Research> graph;

    public static final WorldSavePath WORLD_SAVE_PATH = new WorldSavePath(PATH);

    public ResearchManager(DataPackContents parent) {
        super(Research.CODEC, ResourceFinder.json(PATH));
        this.parent = parent;
    }

    @Override
    protected void apply(Map<Identifier, Research> prepared, ResourceManager manager, Profiler profiler) {
        researchMap.clear();
        unlockableRecipes.clear();
        researchConditions.clear();
        graph = null;

        LockableRecipeTypesList.reload(manager);

        final float researchCostMultiplier = ResearcherConfigs.server.researchCostMultiplier.get();
        prepared.forEach((id, entry) -> {
            if (researchCostMultiplier > 1) {
                int cost = Math.max(1, (int)Math.ceil(entry.trigger().count() * researchCostMultiplier));
                entry = new Research(entry.titleText(), entry.descriptionText(), new ResearchCriterion<>(entry.trigger().criterion(), cost), entry.prerequisiteIds(), entry.recipeUnlocks(), entry.display());
            }
            researchMap.put(id, entry);
            entry.recipeUnlocks().forEach(unlock -> parent.getRecipeManager().get(RegistryKey.of(RegistryKeys.RECIPE, unlock)).ifPresentOrElse(
                    recipe -> {
                        if (LockableRecipeTypesList.getTypes().contains(recipe.value().getSerializer())) {
                            unlockableRecipes.add(unlock);
                        }
                        else {
                            Researcher.LOGGER.warn("Recipe {} of type {} is not lockable!", unlock, Registries.RECIPE_TYPE.getId(recipe.value().getType()));
                        }
                    },
                    () -> Researcher.LOGGER.warn("Recipe with id {} doesn't exist!", unlock)
            ));
            if (entry.getConditions() instanceof ResearchItemsCriterion.Conditions conditions) {
                researchConditions.put(entry, conditions);
            }
        });

        graph = buildResearchGraph(researchMap);
        Researcher.LOGGER.info("Loaded {} researches", researchMap.size());
    }

    public static Graph<Research> buildResearchGraph(Map<Identifier, Research> research) {
        ImmutableGraph.Builder<Research> graphBuilder = GraphBuilder.directed()
                .allowsSelfLoops(false)
                .expectedNodeCount(research.size())
                .immutable();

        research.values().forEach(researchEntry -> {
            graphBuilder.addNode(researchEntry);
            researchEntry.prerequisiteIds().forEach(prerequisite -> {
                if (!research.containsKey(prerequisite)) {
                    Researcher.LOGGER.warn("Research with id {} was not found!", prerequisite);
                }
                graphBuilder.putEdge(research.get(prerequisite), researchEntry);
            });
        });

        return graphBuilder.build();
    }

    @Override
    public boolean isInitialized() {
        return graph != null;
    }

    public Map<Identifier, Research> getAll() {
        return researchMap;
    }

    public Graph<Research> getGraph() {
        return graph;
    }

    public @Nullable Research get(Identifier id) {
        return researchMap.get(id);
    }

    public @Nullable Identifier getId(Research research) {
        return researchMap.inverse().get(research);
    }

    public @Nullable ResearchItemsCriterion.Conditions getResearchConditions(Research research) {
        return researchConditions.get(research);
    }

    public boolean isRecipeUnlockable(Identifier recipeId) {
        return unlockableRecipes.contains(recipeId);
    }
}
