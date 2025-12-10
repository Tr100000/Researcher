package io.github.tr100000.researcher.testmod.datagen;

import io.github.tr100000.researcher.api.data.ResearchBuilder;
import io.github.tr100000.researcher.api.data.ResearchExporter;
import io.github.tr100000.researcher.api.data.ResearchProvider;
import io.github.tr100000.researcher.criteria.BlockBrokenCriterion;
import io.github.tr100000.researcher.criteria.ItemCraftedCriterion;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.advancement.criterion.BrewedPotionCriterion;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.advancement.criterion.OnKilledCriterion;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.TagPredicate;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.predicate.entity.DistancePredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static io.github.tr100000.researcher.testmod.ResearcherTestmod.id;

public class TestmodResearchProvider extends ResearchProvider {
    public TestmodResearchProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    protected void configure(ResearchExporter exporter, RegistryWrapper.WrapperLookup lookup) {
        RegistryEntryLookup<Item> itemLookup = lookup.getOrThrow(RegistryKeys.ITEM);

        Identifier blastFurnace = new ResearchBuilder(id("blast_furnace"))
                .title(Text.literal("Blast Furnace"))
                .display(Items.BLAST_FURNACE)
                .recipeUnlocks(
                        Identifier.ofVanilla("blast_furnace"),
                        Identifier.ofVanilla("this_recipe_does_not_exist")
                )
                .toUnlock(ItemCraftedCriterion.Conditions.of(ItemPredicate.Builder.create().items(itemLookup, Items.BLAST_FURNACE)), 2)
                .export(exporter);

        Identifier ironTools = new ResearchBuilder(id("iron_tools"))
                .title(Text.literal("Iron Tools"))
                .display(Items.IRON_PICKAXE)
                .prerequisites(blastFurnace)
                .recipeUnlocks(
                        Identifier.ofVanilla("iron_sword"),
                        Identifier.ofVanilla("iron_axe"),
                        Identifier.ofVanilla("iron_pickaxe"),
                        Identifier.ofVanilla("iron_shovel"),
                        Identifier.ofVanilla("iron_hoe"),
                        Identifier.ofVanilla("iron_helmet"),
                        Identifier.ofVanilla("iron_chestplate"),
                        Identifier.ofVanilla("iron_leggings"),
                        Identifier.ofVanilla("iron_boots")
                )
                .toUnlock(ItemCraftedCriterion.Conditions.of(ItemPredicate.Builder.create().items(itemLookup, Items.IRON_INGOT)), 10)
                .export(exporter);

        Identifier testing = new ResearchBuilder(id("testing"))
                .title(Text.literal("Testing"))
                .prerequisites(blastFurnace)
                .recipeUnlocks(
                        Identifier.ofVanilla("wooden_sword"),
                        Identifier.ofVanilla("wooden_axe"),
                        Identifier.ofVanilla("wooden_pickaxe"),
                        Identifier.ofVanilla("wooden_shovel"),
                        Identifier.ofVanilla("wooden_hoe"),
                        Identifier.ofVanilla("stone_sword"),
                        Identifier.ofVanilla("stone_axe"),
                        Identifier.ofVanilla("stone_pickaxe"),
                        Identifier.ofVanilla("stone_shovel"),
                        Identifier.ofVanilla("stone_hoe"),
                        Identifier.ofVanilla("diamond_helmet"),
                        Identifier.ofVanilla("diamond_chestplate"),
                        Identifier.ofVanilla("diamond_leggings"),
                        Identifier.ofVanilla("diamond_boots"),
                        Identifier.ofVanilla("brown_shulker_box")
                )
                .toUnlock(BlockBrokenCriterion.Conditions.of(Blocks.OBSIDIAN), 20)
                .export(exporter);

        Identifier killTest = new ResearchBuilder(id("kill_test"))
                .title(Text.literal("Kill Test"))
                .description(Text.literal("Kill a skeleton from at least 50 meters away!"))
                .prerequisites(ironTools, testing)
                .toUnlock(OnKilledCriterion.Conditions.createPlayerKilledEntity(
                        EntityPredicate.Builder.create().type(lookup.getOrThrow(RegistryKeys.ENTITY_TYPE), EntityTypeTags.SKELETONS).distance(DistancePredicate.horizontal(NumberRange.DoubleRange.atLeast(50.0))),
                        DamageSourcePredicate.Builder.create().tag(TagPredicate.expected(DamageTypeTags.IS_PROJECTILE))
                ), 5)
                .export(exporter);

        new ResearchBuilder(id("inventory_change"))
                .title(Text.literal("Change Your Inventory"))
                .prerequisites(ironTools)
                .toUnlock(Criteria.INVENTORY_CHANGED, new InventoryChangedCriterion.Conditions(
                        Optional.empty(),
                        InventoryChangedCriterion.Conditions.Slots.ANY,
                        List.of()
                ), 1000)
                .export(exporter);

        new ResearchBuilder(id("brew_potion"))
                .title(Text.literal("Potion Brewing Test"))
                .prerequisites(killTest)
                .toUnlock(
                        Criteria.BREWED_POTION,
                        new BrewedPotionCriterion.Conditions(Optional.empty(), Optional.of(Potions.STRONG_TURTLE_MASTER)),
                        10
                )
                .export(exporter);
    }
}
