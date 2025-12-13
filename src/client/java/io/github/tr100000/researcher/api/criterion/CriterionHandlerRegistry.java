package io.github.tr100000.researcher.api.criterion;

import io.github.tr100000.researcher.Researcher;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.registry.Registries;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public final class CriterionHandlerRegistry {
    private CriterionHandlerRegistry() {}

    private static final Map<Criterion<?>, Supplier<CriterionHandler<?>>> REGISTRY = new Object2ObjectOpenHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends CriterionConditions> CriterionHandler<T> get(@NotNull Criterion<?> criterion) {
        return (CriterionHandler<T>)REGISTRY.getOrDefault(criterion, REGISTRY.get(null)).get();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T extends CriterionConditions> void register(Criterion<T> criterion, Supplier<CriterionHandler<T>> handler) {
        Objects.requireNonNull(handler, "handler is null");
        REGISTRY.put(criterion, (Supplier)handler);
    }

    public static void printNonRegistered() {
        Registries.CRITERION.forEach(criterion -> {
            if (!REGISTRY.containsKey(criterion)) {
                Researcher.LOGGER.warn("Criterion {} does not have a handler", Registries.CRITERION.getId(criterion));
            }
        });
    }
}
