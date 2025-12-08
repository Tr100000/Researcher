package io.github.tr100000.researcher;

import io.github.tr100000.researcher.api.criterion.CriterionHandler;
import io.github.tr100000.researcher.api.criterion.CriterionHandlerRegistry;
import io.github.tr100000.researcher.impl.criterion.AnyBlockUseCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.BeeNestDestroyedCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.BlockBrokenCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.BredAnimalsCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.BrewedPotionCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.ChangedDimensionCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.ChanneledLightningCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.ConstructBeaconCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.ConsumeItemCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.CuredZombieVillagerCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.DefaultBlockUseCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.EffectsChangedCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.EnchantedItemCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.EnterBlockCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.EntityHurtPlayerCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.ErrorCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.FilledBucketCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.FishingRodHookedCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.InventoryChangedCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.ItemCraftedCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.ItemCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.ItemDurabilityChangedCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.KilledByArrowCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.LevitationCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.OnKilledCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.PlayerGeneratesContainerLootCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.PlayerHurtEntityCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.ResearchItemsCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.ShotCrossbowCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.SlideDownBlockCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.SummonedEntityCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.TameAnimalCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.TargetHitCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.TickCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.TravelCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.UsedEnderEyeCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.UsedTotemCriterionHandler;
import io.github.tr100000.researcher.impl.criterion.VillagerTradeCriterionHandler;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.advancement.criterion.CriterionConditions;

import java.util.function.Supplier;

public final class ResearcherCriterionHandlers {
    private ResearcherCriterionHandlers() {}

    public static void register() {
        register(null, () -> ErrorCriterionHandler.NULL);
        register(Criteria.IMPOSSIBLE, () -> ErrorCriterionHandler.IMPOSSIBLE);

        register(Criteria.PLAYER_KILLED_ENTITY, () -> OnKilledCriterionHandler.PLAYER_KILLED_ENTITY);
        register(Criteria.ENTITY_KILLED_PLAYER, () -> OnKilledCriterionHandler.ENTITY_KILLED_PLAYER);
        register(Criteria.ENTER_BLOCK, EnterBlockCriterionHandler::new);
        register(Criteria.INVENTORY_CHANGED, InventoryChangedCriterionHandler::new);
        register(Criteria.RECIPE_UNLOCKED, () -> ErrorCriterionHandler.WARN_RECIPE_UNLOCKED_AS_CONDITION);
        register(Criteria.PLAYER_HURT_ENTITY, PlayerHurtEntityCriterionHandler::new);
        register(Criteria.ENTITY_HURT_PLAYER, EntityHurtPlayerCriterionHandler::new);
        register(Criteria.ENCHANTED_ITEM, EnchantedItemCriterionHandler::new);
        register(Criteria.FILLED_BUCKET, FilledBucketCriterionHandler::new);
        register(Criteria.BREWED_POTION, BrewedPotionCriterionHandler::new);
        register(Criteria.CONSTRUCT_BEACON, ConstructBeaconCriterionHandler::new);
        register(Criteria.USED_ENDER_EYE, UsedEnderEyeCriterionHandler::new);
        register(Criteria.SUMMONED_ENTITY, SummonedEntityCriterionHandler::new);
        register(Criteria.BRED_ANIMALS, BredAnimalsCriterionHandler::new);
        register(Criteria.LOCATION, () -> TickCriterionHandler.LOCATION);
        register(Criteria.SLEPT_IN_BED, () -> TickCriterionHandler.SLEPT_IN_BED);
        register(Criteria.CURED_ZOMBIE_VILLAGER, CuredZombieVillagerCriterionHandler::new);
        register(Criteria.VILLAGER_TRADE, VillagerTradeCriterionHandler::new);
        register(Criteria.ITEM_DURABILITY_CHANGED, ItemDurabilityChangedCriterionHandler::new);
        register(Criteria.LEVITATION, LevitationCriterionHandler::new);
        register(Criteria.CHANGED_DIMENSION, ChangedDimensionCriterionHandler::new);
        register(Criteria.TICK, () -> TickCriterionHandler.TICK);
        register(Criteria.TAME_ANIMAL, TameAnimalCriterionHandler::new);
        register(Criteria.PLACED_BLOCK, () -> ItemCriterionHandler.PLACED_BLOCK);
        register(Criteria.CONSUME_ITEM, ConsumeItemCriterionHandler::new);
        register(Criteria.EFFECTS_CHANGED, EffectsChangedCriterionHandler::new);
        register(Criteria.USED_TOTEM, UsedTotemCriterionHandler::new);
        register(Criteria.NETHER_TRAVEL, () -> TravelCriterionHandler.NETHER_TRAVEL);
        register(Criteria.FISHING_ROD_HOOKED, FishingRodHookedCriterionHandler::new);
        register(Criteria.CHANNELED_LIGHTNING, ChanneledLightningCriterionHandler::new);
        register(Criteria.SHOT_CROSSBOW, ShotCrossbowCriterionHandler::new);
        register(Criteria.KILLED_BY_ARROW, KilledByArrowCriterionHandler::new);
        register(Criteria.HERO_OF_THE_VILLAGE, () -> TickCriterionHandler.HERO_OF_THE_VILLAGE);
        register(Criteria.VOLUNTARY_EXILE, () -> TickCriterionHandler.VOLUNTARY_EXILE);
        register(Criteria.SLIDE_DOWN_BLOCK, SlideDownBlockCriterionHandler::new);
        register(Criteria.BEE_NEST_DESTROYED, BeeNestDestroyedCriterionHandler::new);
        register(Criteria.TARGET_HIT, TargetHitCriterionHandler::new);
        register(Criteria.ITEM_USED_ON_BLOCK, () -> ItemCriterionHandler.ITEM_USED_ON_BLOCK);
        register(Criteria.DEFAULT_BLOCK_USE, DefaultBlockUseCriterionHandler::new);
        register(Criteria.ANY_BLOCK_USE, AnyBlockUseCriterionHandler::new);
        register(Criteria.PLAYER_GENERATES_CONTAINER_LOOT, PlayerGeneratesContainerLootCriterionHandler::new);

        register(Criteria.AVOID_VIBRATION, () -> TickCriterionHandler.AVOID_VIBRATION);

        register(ResearcherCriteria.BLOCK_BROKEN, BlockBrokenCriterionHandler::new);
        register(ResearcherCriteria.ITEM_CRAFTED, ItemCraftedCriterionHandler::new);
        register(ResearcherCriteria.RESEARCH_ITEMS, ResearchItemsCriterionHandler::new);

        CriterionHandlerRegistry.printNonRegistered();
    }

    private static <T extends CriterionConditions> void register(Criterion<T> criterion, Supplier<CriterionHandler<T>> handler) {
        CriterionHandlerRegistry.register(criterion, handler);
    }
}
