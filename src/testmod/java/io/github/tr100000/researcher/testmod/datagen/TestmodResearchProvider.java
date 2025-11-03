package io.github.tr100000.researcher.testmod.datagen;

import io.github.tr100000.researcher.ResearcherCriteria;
import io.github.tr100000.researcher.api.data.ResearchBuilder;
import io.github.tr100000.researcher.api.data.ResearchExporter;
import io.github.tr100000.researcher.api.data.ResearchProvider;
import io.github.tr100000.researcher.criteria.BlockBrokenCriterion;
import io.github.tr100000.researcher.criteria.ItemCraftedCriterion;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

import static io.github.tr100000.researcher.testmod.ResearcherTestmod.id;

public class TestmodResearchProvider extends ResearchProvider {
    public TestmodResearchProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    protected void configure(ResearchExporter exporter, RegistryWrapper.WrapperLookup lookup) {
        Identifier blastFurnace = new ResearchBuilder(id("blast_furnace"))
                .title(Text.literal("Blast Furnace"))
                .display(Items.BLAST_FURNACE)
                .recipeUnlocks(
                        Identifier.ofVanilla("blast_furnace"),
                        Identifier.ofVanilla("this_recipe_does_not_exist")
                )
                .toUnlock(ResearcherCriteria.ITEM_CRAFTED, new ItemCraftedCriterion.Conditions(Items.FURNACE), 2)
                .export(exporter);

        new ResearchBuilder(id("iron_tools"))
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
                .toUnlock(ResearcherCriteria.ITEM_CRAFTED, new ItemCraftedCriterion.Conditions(Items.IRON_INGOT), 10)
                .export(exporter);

        new ResearchBuilder(id("testing"))
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
                        Identifier.ofVanilla("diamond_sword"),
                        Identifier.ofVanilla("diamond_axe"),
                        Identifier.ofVanilla("diamond_pickaxe"),
                        Identifier.ofVanilla("diamond_shovel"),
                        Identifier.ofVanilla("diamond_hoe"),
                        Identifier.ofVanilla("diamond_helmet"),
                        Identifier.ofVanilla("diamond_chestplate"),
                        Identifier.ofVanilla("diamond_leggings"),
                        Identifier.ofVanilla("diamond_boots"),
                        Identifier.ofVanilla("brown_shulker_box")
                )
                .toUnlock(ResearcherCriteria.BLOCK_BROKEN, new BlockBrokenCriterion.Conditions(Blocks.BEDROCK), 20)
                .export(exporter);
    }
}
