package io.github.tr100000.researcher.api;

import io.github.tr100000.researcher.PlayerResearchTracker;
import io.github.tr100000.researcher.Research;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public final class ResearcherEvents {
    private ResearcherEvents() {}

    public static final Event<ResearchFinished> RESEARCH_FINISHED = EventFactory.createArrayBacked(ResearchFinished.class,
            listeners -> (researchTracker, research) -> {
                for (ResearchFinished listener : listeners) {
                    listener.onResearchFinished(researchTracker, research);
                }
            });

    @FunctionalInterface
    public interface ResearchFinished {
        void onResearchFinished(PlayerResearchTracker researchTracker, Research research);
    }
}
