package io.github.tr100000.researcher;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;

public final class ModUtils {
    private ModUtils() {}

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(Researcher.MODID, path);
    }

    public static String getTranslationKey(String group, String key) {
        return String.format("%s.%s.%s", group, Researcher.MODID, key);
    }

    public static MutableComponent getTranslated(String group, String key) {
        return Component.translatable(getTranslationKey(group, key));
    }

    public static MutableComponent getTranslated(String group, String key, Object... args) {
        return Component.translatable(getTranslationKey(group, key), args);
    }

    public static String getScreenTranslationKey(String key) {
        return getTranslationKey("screen", key);
    }

    public static MutableComponent getScreenTranslated(String key) {
        return Component.translatable(getScreenTranslationKey(key));
    }

    public static MutableComponent getScreenTranslated(String key, Object... args) {
        return Component.translatable(getScreenTranslationKey(key), args);
    }
}
