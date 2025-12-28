package io.github.tr100000.researcher.api.trigger;

import io.github.tr100000.researcher.Researcher;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public final class TriggerHandlerRegistry {
    private TriggerHandlerRegistry() {}

    private static final Map<CriterionTrigger<?>, Supplier<TriggerHandler<?>>> REGISTRY = new Object2ObjectOpenHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends CriterionTriggerInstance> TriggerHandler<T> get(CriterionTrigger<?> criterion) {
        return (TriggerHandler<T>)REGISTRY.getOrDefault(criterion, REGISTRY.get(null)).get();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T extends CriterionTriggerInstance> void register(CriterionTrigger<T> criterion, Supplier<TriggerHandler<T>> handler) {
        Objects.requireNonNull(handler, "handler is null");
        REGISTRY.put(criterion, (Supplier)handler);
    }

    public static void printNonRegistered() {
        BuiltInRegistries.TRIGGER_TYPES.forEach(criterion -> {
            if (!REGISTRY.containsKey(criterion)) {
                Researcher.LOGGER.warn("Trigger {} does not have a handler", BuiltInRegistries.TRIGGER_TYPES.getKey(criterion));
            }
        });
    }
}
