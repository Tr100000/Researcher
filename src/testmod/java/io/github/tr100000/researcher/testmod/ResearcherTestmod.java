package io.github.tr100000.researcher.testmod;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class ResearcherTestmod implements ModInitializer {
    public static final String MODID = "researcher-testmod";

    @Override
    public void onInitialize() {

    }

    public static Identifier id(String path) {
        return Identifier.of(MODID, path);
    }
}
