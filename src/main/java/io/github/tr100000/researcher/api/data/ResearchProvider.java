package io.github.tr100000.researcher.api.data;

import io.github.tr100000.researcher.Research;
import io.github.tr100000.researcher.ResearchManager;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public abstract class ResearchProvider extends FabricCodecDataProvider<Research> {
    protected ResearchProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup, PackOutput.Target.DATA_PACK, ResearchManager.PATH, Research.CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, Research> provider, HolderLookup.Provider lookup) {
        configure(provider::accept, lookup);
    }

    protected abstract void configure(ResearchExporter exporter, HolderLookup.Provider lookup);

    @Override
    public String getName() {
        return "Research";
    }
}
