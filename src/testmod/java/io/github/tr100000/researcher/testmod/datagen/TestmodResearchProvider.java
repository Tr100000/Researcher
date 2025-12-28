package io.github.tr100000.researcher.testmod.datagen;

import io.github.tr100000.researcher.api.data.ResearchBuilder;
import io.github.tr100000.researcher.api.data.ResearchExporter;
import io.github.tr100000.researcher.api.data.ResearchProvider;
import io.github.tr100000.researcher.criterion.BlockBrokenTrigger;
import io.github.tr100000.researcher.criterion.ItemCraftedTrigger;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.criterion.DistancePredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.KilledTrigger;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.advancements.criterion.TagPredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static io.github.tr100000.researcher.testmod.ResearcherTestmod.id;

public class TestmodResearchProvider extends ResearchProvider {
    public TestmodResearchProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    protected void configure(ResearchExporter exporter, HolderLookup.Provider lookup) {
        HolderGetter<Item> itemLookup = lookup.lookupOrThrow(Registries.ITEM);

        Identifier blastFurnace = new ResearchBuilder(id("blast_furnace"))
                .title(Component.literal("Blast Furnace"))
                .display(Items.BLAST_FURNACE)
                .recipeUnlocks(
                        Identifier.withDefaultNamespace("blast_furnace"),
                        Identifier.withDefaultNamespace("this_recipe_does_not_exist")
                )
                .toUnlock(ItemCraftedTrigger.TriggerInstance.of(ItemPredicate.Builder.item().of(itemLookup, Items.BLAST_FURNACE)), 2)
                .export(exporter);

        Identifier ironTools = new ResearchBuilder(id("iron_tools"))
                .title(Component.literal("Iron Tools"))
                .display(Items.IRON_PICKAXE)
                .prerequisites(blastFurnace)
                .recipeUnlocks(
                        Identifier.withDefaultNamespace("iron_sword"),
                        Identifier.withDefaultNamespace("iron_axe"),
                        Identifier.withDefaultNamespace("iron_pickaxe"),
                        Identifier.withDefaultNamespace("iron_shovel"),
                        Identifier.withDefaultNamespace("iron_hoe"),
                        Identifier.withDefaultNamespace("iron_helmet"),
                        Identifier.withDefaultNamespace("iron_chestplate"),
                        Identifier.withDefaultNamespace("iron_leggings"),
                        Identifier.withDefaultNamespace("iron_boots")
                )
                .toUnlock(ItemCraftedTrigger.TriggerInstance.of(ItemPredicate.Builder.item().of(itemLookup, Items.IRON_INGOT)), 10)
                .export(exporter);

        Identifier testing = new ResearchBuilder(id("testing"))
                .title(Component.literal("Testing"))
                .prerequisites(blastFurnace)
                .recipeUnlocks(
                        Identifier.withDefaultNamespace("wooden_sword"),
                        Identifier.withDefaultNamespace("wooden_axe"),
                        Identifier.withDefaultNamespace("wooden_pickaxe"),
                        Identifier.withDefaultNamespace("wooden_shovel"),
                        Identifier.withDefaultNamespace("wooden_hoe"),
                        Identifier.withDefaultNamespace("stone_sword"),
                        Identifier.withDefaultNamespace("stone_axe"),
                        Identifier.withDefaultNamespace("stone_pickaxe"),
                        Identifier.withDefaultNamespace("stone_shovel"),
                        Identifier.withDefaultNamespace("stone_hoe"),
                        Identifier.withDefaultNamespace("diamond_helmet"),
                        Identifier.withDefaultNamespace("diamond_chestplate"),
                        Identifier.withDefaultNamespace("diamond_leggings"),
                        Identifier.withDefaultNamespace("diamond_boots"),
                        Identifier.withDefaultNamespace("brown_shulker_box")
                )
                .toUnlock(BlockBrokenTrigger.Conditions.of(Blocks.OBSIDIAN), 20)
                .export(exporter);

        Identifier killTest = new ResearchBuilder(id("kill_test"))
                .title(Component.literal("Kill Test"))
                .description(Component.literal("Kill a skeleton from at least 50 meters away!"))
                .prerequisites(ironTools, testing)
                .toUnlock(KilledTrigger.TriggerInstance.playerKilledEntity(
                        net.minecraft.advancements.criterion.EntityPredicate.Builder.entity().of(lookup.lookupOrThrow(Registries.ENTITY_TYPE), EntityTypeTags.SKELETONS).distance(DistancePredicate.horizontal(MinMaxBounds.Doubles.atLeast(50.0))),
                        net.minecraft.advancements.criterion.DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_PROJECTILE))
                ), 5)
                .export(exporter);

        new ResearchBuilder(id("inventory_change"))
                .title(Component.literal("Change Your Inventory"))
                .prerequisites(ironTools)
                .toUnlock(CriteriaTriggers.INVENTORY_CHANGED, new net.minecraft.advancements.criterion.InventoryChangeTrigger.TriggerInstance(
                        Optional.empty(),
                        net.minecraft.advancements.criterion.InventoryChangeTrigger.TriggerInstance.Slots.ANY,
                        List.of()
                ), 1000)
                .export(exporter);

        new ResearchBuilder(id("brew_potion"))
                .title(Component.literal("Potion Brewing Test"))
                .prerequisites(killTest)
                .toUnlock(
                        CriteriaTriggers.BREWED_POTION,
                        new net.minecraft.advancements.criterion.BrewedPotionTrigger.TriggerInstance(Optional.empty(), Optional.of(Potions.STRONG_TURTLE_MASTER)),
                        10
                )
                .export(exporter);
    }
}
