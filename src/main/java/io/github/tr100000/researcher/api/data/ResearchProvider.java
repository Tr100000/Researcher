package io.github.tr100000.researcher.api.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import io.github.tr100000.researcher.Research;
import io.github.tr100000.researcher.ResearchManager;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public abstract class ResearchProvider implements DataProvider {
    protected final FabricPackOutput output;
    private final PackOutput.PathProvider pathProvider;
    private final CompletableFuture<HolderLookup.Provider> registries;

    protected ResearchProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        this.output = output;
        this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, ResearchManager.PATH);
        this.registries = registries;
    }

    protected abstract void configure(ResearchExporter exporter, HolderLookup.Provider registryLookup);

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public CompletableFuture<?> run(CachedOutput output) {
        return this.registries.thenCompose(lookup -> {
            final Set<Identifier> identifiers = new ObjectOpenHashSet<>();
            final Map<Identifier, Research> researches = new Object2ObjectOpenHashMap<>();

            configure(researches::put, lookup);

            RegistryOps<JsonElement> ops = lookup.createSerializationContext(JsonOps.INSTANCE);
            final List<CompletableFuture<?>> futures = new ArrayList<>();

            for (Map.Entry<Identifier, Research> entry : researches.entrySet()) {
                if (!identifiers.add(entry.getKey())) {
                    throw new IllegalStateException("Duplicate research " + entry.getKey());
                }

                JsonObject researchJson = Research.CODEC.encodeStart(ops, entry.getValue()).getOrThrow(IllegalStateException::new).getAsJsonObject();
                FabricDataGenHelper.addConditions(researchJson, FabricDataGenHelper.consumeConditions(entry.getValue()));
                futures.add(DataProvider.saveStable(output, researchJson, getOutputPath(entry)));
            }

            return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
        });
    }


    private Path getOutputPath(Map.Entry<Identifier, Research> entry) {
        return pathProvider.json(entry.getKey());
    }

    @Override
    public String getName() {
        return "Research";
    }
}
