package io.github.tr100000.researcher.api.criterion;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.advancement.criterion.CriterionConditions;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

public final class CriterionHandlerRegistry {
    private CriterionHandlerRegistry() {}

    private static final Map<Criterion<?>, CriterionHandler<?>> REGISTRY = new Object2ObjectOpenHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends CriterionConditions> CriterionHandler<T> get(@NotNull Criterion<?> criterion) {
        return (CriterionHandler<T>)REGISTRY.getOrDefault(criterion, REGISTRY.get(null));
    }

    public static <T extends CriterionConditions> void register(Criterion<T> criterion, CriterionHandler<T> handler) {
        Objects.requireNonNull(handler, "handler is null");
        REGISTRY.put(criterion, handler);
    }
}
