package io.github.tr100000.researcher.api.criterion;

import io.github.tr100000.researcher.api.criterion.element.GroupedElement;
import io.github.tr100000.researcher.api.criterion.element.ItemElement;
import io.github.tr100000.researcher.api.criterion.element.TextElement;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.TypedEntityData;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;

import java.util.Map;

public final class ElementUtils {
    private ElementUtils() {}

    private static final Map<EntityType<?>, Item> ENTITY_TYPE_ITEMS = new Object2ObjectOpenHashMap<>();

    public static CriterionDisplayElement fromEntityType(EntityType<?> entityType) {
        return new GroupedElement(
                new ItemElement(ENTITY_TYPE_ITEMS.getOrDefault(entityType, Items.BARRIER).getDefaultStack(), false),
                new TextElement(entityType.getName())
        );
    }

    static {
        Registries.ITEM.forEach(item -> {
            if (item.getComponents().contains(DataComponentTypes.ENTITY_DATA)) {
                TypedEntityData<EntityType<?>> data = item.getComponents().get(DataComponentTypes.ENTITY_DATA);
                if (data != null && data.getType() != null) {
                    ENTITY_TYPE_ITEMS.put(data.getType(), item);
                }
            }
        });
    }
}
