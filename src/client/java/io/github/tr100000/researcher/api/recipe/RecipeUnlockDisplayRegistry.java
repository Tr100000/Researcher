package io.github.tr100000.researcher.api.recipe;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public final class RecipeUnlockDisplayRegistry {
    private RecipeUnlockDisplayRegistry() {}

    private static final Map<RecipeSerializer<?>, Function<RecipeHolder<?>, RecipeUnlockDisplay>> REGISTRY = new Object2ObjectOpenHashMap<>();

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T extends Recipe<?>> void register(RecipeSerializer<T> recipeType, Function<RecipeHolder<T>, RecipeUnlockDisplay> displayFactory) {
        Objects.requireNonNull(recipeType, "recipeType must not be null");
        Objects.requireNonNull(displayFactory, "displayFactory must not be null");
        REGISTRY.put(recipeType, (Function)displayFactory);
    }

    public static @Nullable RecipeUnlockDisplay getDisplay(RecipeHolder<?> recipeEntry) {
        Objects.requireNonNull(recipeEntry, "recipeEntry must not be null");
        if (REGISTRY.containsKey(recipeEntry.value().getSerializer())) {
            return REGISTRY.get(recipeEntry.value().getSerializer()).apply(recipeEntry);
        }
        return null;
    }
}
