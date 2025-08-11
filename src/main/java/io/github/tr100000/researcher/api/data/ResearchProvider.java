package io.github.tr100000.researcher.api.data;

import io.github.tr100000.researcher.Research;
import io.github.tr100000.researcher.ResearchManager;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public abstract class ResearchProvider extends FabricCodecDataProvider<Research> {
    protected ResearchProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup, DataOutput.OutputType.DATA_PACK, ResearchManager.PATH, Research.CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, Research> provider, RegistryWrapper.WrapperLookup lookup) {
        configure(provider::accept, lookup);
    }

    protected abstract void configure(ResearchExporter exporter, RegistryWrapper.WrapperLookup lookup);

    @Override
    public String getName() {
        return "Research";
    }
}
