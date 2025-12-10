package io.github.tr100000.researcher;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.graph.Graph;
import io.github.tr100000.researcher.api.PlayerResearchHolder;
import io.github.tr100000.researcher.api.ResearchHolder;
import io.github.tr100000.researcher.config.ResearcherConfigs;
import io.github.tr100000.researcher.criteria.ResearchItemsCriterion;
import io.github.tr100000.researcher.networking.ResearchUpdateS2CPacket;
import io.github.tr100000.researcher.networking.StartResearchC2SPacket;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@SuppressWarnings("UnstableApiUsage")
public class ClientResearchTracker implements ResearchHolder, PlayerResearchHolder {
    private static final MinecraftClient client = MinecraftClient.getInstance();

    private final Map<Research, ResearchProgress> progressMap = new Object2ObjectOpenHashMap<>();
    private @Nullable Identifier currentResearching;
    private List<Identifier> pinnedResearches = List.of();

    private final BiMap<Identifier, Research> researchMap = HashBiMap.create();
    private final Set<Identifier> unlockableRecipes = new ObjectOpenHashSet<>();
    private final Map<Research, ResearchItemsCriterion.Conditions> researchConditions = new Object2ObjectOpenHashMap<>();
    private Graph<Research> graph;

    public ClientResearchTracker() {
        ResearcherConfigs.client.pinAvailableResearches.listenToEntry(entry -> {
            if (entry.get()) {
                pinAvailableResearches();
            }
        });
    }

    @ApiStatus.Internal
    public void handleUpdate(ResearchUpdateS2CPacket packet) {
        if (packet.clearCurrent()) {
            Researcher.LOGGER.info("Loaded {} researches on client", packet.loadedResearch().size());
            progressMap.clear();
            researchMap.clear();
            unlockableRecipes.clear();
            researchConditions.clear();
            graph = null;
        }

        putAllResearch(packet.loadedResearch());
        AtomicBoolean shouldPinAvailable = new AtomicBoolean(false);
        packet.progressMap().forEach((id, progress) -> {
            Research research = get(id);
            progressMap.put(research, progress);
            if (!packet.clearCurrent() && progress.isFinished()) {
                client.getToastManager().add(new ResearchToast(research));
                shouldPinAvailable.set(true);
            }
            if (!progress.hasProgress()) {
                shouldPinAvailable.set(true);
            }
        });
        currentResearching = packet.currentResearching().orElse(null);
        pinnedResearches = packet.pinnedResearches();

        if (shouldPinAvailable.get() || packet.clearCurrent()) {
            pinAvailableResearches();
        }
    }

    private void putAllResearch(Map<Identifier, Research> prepared) {
        if (prepared.isEmpty()) return;
        prepared.forEach((id, entry) -> {
            researchMap.put(id, entry);
            unlockableRecipes.addAll(entry.recipeUnlocks());
            if (entry.getConditions() instanceof ResearchItemsCriterion.Conditions conditions) {
                researchConditions.put(entry, conditions);
            }
        });
        graph = ResearchManager.buildResearchGraph(researchMap);
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

    public boolean isRecipeUnlockable(Identifier recipeId) {
        return unlockableRecipes.contains(recipeId);
    }

    public @Nullable ResearchItemsCriterion.Conditions getResearchConditions(Research research) {
        return researchConditions.get(research);
    }

    public @Nullable Identifier getCurrentResearchingId() {
        return currentResearching;
    }

    public @Nullable Research getCurrentResearching() {
        return get(currentResearching);
    }

    public List<Identifier> getPinnedResearches() {
        return pinnedResearches;
    }

    public boolean isCurrentOrPinned(Research research) {
        return getCurrentResearching() == research || pinnedResearches.contains(getId(research));
    }

    public boolean canResearch(@Nullable Research research) {
        if (research == null) return false;
        if (hasFinished(research)) return false;
        return research.prerequisites(this).stream().allMatch(this::hasFinished);
    }

    public boolean isAvailableOrFinished(Research research) {
        return hasFinished(research) || canResearch(research);
    }

    public Set<Research> getResearchableSuccessors(Research research) {
        return graph.successors(research).stream().filter(this::isAvailableOrFinished).collect(Collectors.toCollection(ObjectOpenHashSet::new));
    }

    public boolean canCraftRecipe(Identifier recipeId) {
        return !isRecipeUnlockable(recipeId) || hasUnlockedRecipe(recipeId);
    }

    public boolean hasUnlockedRecipe(Identifier recipeId) {
        for (Map.Entry<Research, ResearchProgress> entry : progressMap.entrySet()) {
            if (entry.getValue().isFinished() && entry.getKey().recipeUnlocks().contains(recipeId)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasFinished(@Nullable Research research) {
        if (research == null) return false;
        return getProgress(research).isFinished();
    }

    public ResearchProgress getProgress(Research research) {
        return progressMap.computeIfAbsent(research, r -> new ResearchProgress());
    }

    public void pinAvailableResearches() {
        if (!ResearcherConfigs.client.pinAvailableResearches.get()) return;
        researchMap.values().forEach(research -> {
            if (getResearchConditions(research) == null && canResearch(research) && !pinnedResearches.contains(getId(research))) {
                ClientPlayNetworking.send(new StartResearchC2SPacket(StartResearchC2SPacket.Mode.PIN, Optional.ofNullable(getId(research))));
                Researcher.LOGGER.info("pinned research {}", getId(research));
            }
        });
    }

    public Text getTitleWithStatus(Research research) {
        ResearchProgress progress = getProgress(research);
        String titleKey;
        float progressPercentage = 0;
        if (progress.isFinished()) {
            titleKey = "finished";
            progressPercentage = 1;
        }
        else if (canResearch(research)) {
            titleKey = "available";
            if (progress.hasProgress()) {
                titleKey = "progress";
                progressPercentage = progress.getProgress01(research.trigger().count());
            }
        }
        else {
            titleKey = "locked";
        }
        return ModUtils.getScreenTranslated(titleKey, research.getTitle(this).getString(), (int)Math.floor(progressPercentage * 100));
    }
}
