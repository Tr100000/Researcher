package io.github.tr100000.researcher;

import io.github.tr100000.researcher.criteria.BlockBrokenCriterion;
import io.github.tr100000.researcher.criteria.HasResearchCriterion;
import io.github.tr100000.researcher.criteria.ItemCraftedCriterion;
import io.github.tr100000.researcher.criteria.ResearchItemsCriterion;
import io.github.tr100000.trutils.api.utils.RegistryHelper;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.registry.Registries;

public final class ResearcherCriteria {
    private ResearcherCriteria() {}

    public static final RegistryHelper<Criterion<?>> REGISTRY = new RegistryHelper<>(Registries.CRITERION, Researcher.MODID);

    public static final BlockBrokenCriterion BLOCK_BROKEN = REGISTRY.add(new BlockBrokenCriterion(), "block_broken");
    public static final ItemCraftedCriterion ITEM_CRAFTED = REGISTRY.add(new ItemCraftedCriterion(), "item_crafted");
    public static final ResearchItemsCriterion RESEARCH_ITEMS = REGISTRY.add(new ResearchItemsCriterion(), "research_items");
    public static final HasResearchCriterion HAS_RESEARCH = REGISTRY.add(new HasResearchCriterion(), "has_research");

    public static void register() {
        REGISTRY.register();
    }
}
