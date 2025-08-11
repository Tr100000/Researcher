package io.github.tr100000.researcher.api;

import io.github.tr100000.researcher.Research;
import io.github.tr100000.researcher.ResearchProgress;
import io.github.tr100000.researcher.ResearchStatus;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface PlayerResearchHolder {
    @Nullable Identifier getCurrentResearchingId();
    @Nullable Research getCurrentResearching();
    List<Identifier> getPinnedResearches();
    boolean canResearch(@Nullable Research research);
    boolean canCraftRecipe(Identifier recipeId);
    boolean hasFinished(@Nullable Research research);
    ResearchProgress getProgress(Research research);

    default ResearchStatus getStatus(Research research) {
        ResearchProgress progress = getProgress(research);
        if (progress.isFinished()) {
            return ResearchStatus.FINISHED;
        }
        else if (canResearch(research)) {
            if (progress.getCount() > 0) {
                return ResearchStatus.IN_PROGRESS;
            }
            else {
                return ResearchStatus.AVAILABLE;
            }
        }
        else {
            return ResearchStatus.LOCKED;
        }
    }
}
