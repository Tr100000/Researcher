package io.github.tr100000.researcher;

import io.github.tr100000.researcher.api.CriterionHandler;
import io.github.tr100000.researcher.api.CriterionHandlerRegistry;
import io.github.tr100000.researcher.impl.criterion.BlockBrokenCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.ConsumeItemCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.ErrorCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.ItemCraftedCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.ResearchItemsCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.TickCriterionHandler;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.text.Text;

public final class ResearcherCriterionHandlers {
    private ResearcherCriterionHandlers() {}

    public static void register() {
        register(null, ErrorCriterionHandler.NULL);
        register(Criteria.IMPOSSIBLE, new ErrorCriterionHandler<>(Text.translatable("screen.researcher.criterion.impossible")));

        register(Criteria.SLEPT_IN_BED, TickCriterionHandler.SLEPT_IN_BED);
        register(Criteria.TICK, TickCriterionHandler.TICK);
        register(Criteria.CONSUME_ITEM, new ConsumeItemCriterionHandler());
        register(Criteria.HERO_OF_THE_VILLAGE, TickCriterionHandler.HERO_OF_THE_VILLAGE);
        register(Criteria.VOLUNTARY_EXILE, TickCriterionHandler.VOLUNTARY_EXILE);
        register(Criteria.AVOID_VIBRATION, TickCriterionHandler.AVOID_VIBRATION);

        register(ResearcherCriteria.BLOCK_BROKEN, new BlockBrokenCriterionHandler());
        register(ResearcherCriteria.ITEM_CRAFTED, new ItemCraftedCriterionHandler());
        register(ResearcherCriteria.RESEARCH_ITEMS, new ResearchItemsCriterionHandler());
    }

    private static <T extends CriterionConditions> void register(Criterion<T> criterion, CriterionHandler<T> handler) {
        CriterionHandlerRegistry.register(criterion, handler);
    }
}
