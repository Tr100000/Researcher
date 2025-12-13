package io.github.tr100000.researcher;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public final class ModUtils {
    private ModUtils() {}

    public static Identifier id(String path) {
        return Identifier.of(Researcher.MODID, path);
    }

    public static String getTranslationKey(String group, String key) {
        return String.format("%s.%s.%s", group, Researcher.MODID, key);
    }

    public static MutableText getTranslated(String group, String key) {
        return Text.translatable(getTranslationKey(group, key));
    }

    public static MutableText getTranslated(String group, String key, Object... args) {
        return Text.translatable(getTranslationKey(group, key), args);
    }

    public static String getScreenTranslationKey(String key) {
        return getTranslationKey("screen", key);
    }

    public static MutableText getScreenTranslated(String key) {
        return Text.translatable(getScreenTranslationKey(key));
    }

    public static MutableText getScreenTranslated(String key, Object... args) {
        return Text.translatable(getScreenTranslationKey(key), args);
    }
}
