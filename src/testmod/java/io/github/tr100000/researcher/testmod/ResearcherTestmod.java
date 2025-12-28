package io.github.tr100000.researcher.testmod;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;

public class ResearcherTestmod implements ModInitializer {
    public static final String MODID = "researcher-testmod";

    @Override
    public void onInitialize() {}

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
    }
}
