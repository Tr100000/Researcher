package io.github.tr100000.researcher;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.ImmutableGraph;
import com.mojang.serialization.JsonOps;
import io.github.tr100000.researcher.api.ResearchHolder;
import io.github.tr100000.researcher.config.ResearcherConfigs;
import io.github.tr100000.researcher.criterion.ResearchItemsTrigger;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.storage.LevelResource;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.Set;

@SuppressWarnings("UnstableApiUsage")
public class ResearchManager extends SimpleJsonResourceReloadListener<Research> implements ResearchHolder {
    public static final String PATH = "research";
    public static final Identifier ID = ModUtils.id(PATH);
    private final ReloadableServerResources parent;
    private final BiMap<Identifier, Research> researchMap = HashBiMap.create();
    private final Set<Identifier> unlockableRecipes = new ObjectOpenHashSet<>();
    private final Map<Research, ResearchItemsTrigger.TriggerInstance> researchConditions = new Object2ObjectOpenHashMap<>();
    private @Nullable Graph<Research> graph;

    public static final LevelResource WORLD_SAVE_PATH = new LevelResource(PATH);

    public ResearchManager(HolderLookup.Provider lookup, ReloadableServerResources parent) {
        super(lookup.createSerializationContext(JsonOps.INSTANCE), Research.CODEC, FileToIdConverter.json(PATH));
        this.parent = parent;
    }

    @Override
    protected void apply(Map<Identifier, Research> prepared, ResourceManager manager, ProfilerFiller profiler) {
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
            entry.recipeUnlocks().forEach(unlock -> parent.getRecipeManager().byKey(ResourceKey.create(Registries.RECIPE, unlock)).ifPresentOrElse(
                    recipe -> {
                        if (LockableRecipeTypesList.getTypes().contains(recipe.value().getSerializer())) {
                            unlockableRecipes.add(unlock);
                        }
                        else {
                            Researcher.LOGGER.warn("Recipe {} of type {} is not lockable!", unlock, BuiltInRegistries.RECIPE_TYPE.getKey(recipe.value().getType()));
                        }
                    },
                    () -> Researcher.LOGGER.warn("Recipe with id {} doesn't exist!", unlock)
            ));
            if (entry.getConditions() instanceof ResearchItemsTrigger.TriggerInstance triggerInstance) {
                researchConditions.put(entry, triggerInstance);
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

    public @Nullable Graph<Research> getGraph() {
        return graph;
    }

    public @Nullable Research get(@Nullable Identifier id) {
        if (id == null) return null;
        return researchMap.get(id);
    }

    public @Nullable Identifier getId(@Nullable Research research) {
        if (research == null) return null;
        return researchMap.inverse().get(research);
    }

    public ResearchItemsTrigger.@Nullable TriggerInstance getResearchConditions(Research research) {
        return researchConditions.get(research);
    }

    public boolean isRecipeUnlockable(Identifier recipeId) {
        return unlockableRecipes.contains(recipeId);
    }
}
