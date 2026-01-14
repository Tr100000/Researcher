package io.github.tr100000.researcher.testmod.datagen;

import io.github.tr100000.trutils.api.datagen.RuntimeDatagenEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ResearcherTestmodDatagen implements RuntimeDatagenEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();

        pack.addProvider(TestmodResearchProvider::new);
    }
}
