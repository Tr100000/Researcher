package io.github.tr100000.researcher;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import org.jetbrains.annotations.UnmodifiableView;

import java.io.Reader;
import java.util.Collections;
import java.util.List;

public final class LockableRecipeTypesList {
    private LockableRecipeTypesList() {}

    public static final Codec<List<RecipeType<?>>> CODEC = Registries.RECIPE_TYPE.getCodec().listOf();
    private static List<RecipeType<?>> types = Collections.emptyList();

    @UnmodifiableView
    public static List<RecipeType<?>> getTypes() {
        return types;
    }

    public static void reload(ResourceManager manager) {
        List<RecipeType<?>> lockable = new ObjectArrayList<>();
        List<Resource> resources = manager.getAllResources(Researcher.id("lockable_recipe_types.json"));
        resources.forEach(resource -> {
            try (Reader reader = resource.getReader()) {
                JsonElement json = JsonParser.parseReader(reader);
                List<RecipeType<?>> types = LockableRecipeTypesList.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
                lockable.addAll(types);
            }
            catch (Exception e) {
                Researcher.LOGGER.error("Error reading file in pack {}", resource.getPackId(), e);
            }
        });
        types = List.copyOf(lockable);
    }
}
