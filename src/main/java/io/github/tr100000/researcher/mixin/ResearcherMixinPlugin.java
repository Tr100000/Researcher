package io.github.tr100000.researcher.mixin;

import net.fabricmc.loader.api.FabricLoader;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;

public class ResearcherMixinPlugin implements IMixinConfigPlugin {
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.contains("compat.jei")) {
            return FabricLoader.getInstance().isModLoaded("jei");
        }
        else if (mixinClassName.contains("compat.rrv")) {
            return FabricLoader.getInstance().isModLoaded("rrv");
        }
        else {
            return true;
        }
    }
}
