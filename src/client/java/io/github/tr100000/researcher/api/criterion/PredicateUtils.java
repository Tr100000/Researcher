package io.github.tr100000.researcher.api.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.api.criterion.element.GroupedElement;
import io.github.tr100000.researcher.api.criterion.element.TextElement;
import io.github.tr100000.researcher.api.criterion.element.TimedSwitchingElement;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.loot.condition.EntityPropertiesLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.predicate.entity.DistancePredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.entity.MovementPredicate;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class PredicateUtils {
    private PredicateUtils() {}

    private static final Text DISTANCE_ABSOLUTE_KEY = ModUtils.getScreenTranslated("predicate.distance.absolute");
    private static final Text DISTANCE_HORIZONTAL_KEY = ModUtils.getScreenTranslated("predicate.distance.horizontal");
    private static final Text DISTANCE_X_KEY = ModUtils.getScreenTranslated("predicate.distance.x");
    private static final Text DISTANCE_Y_KEY = ModUtils.getScreenTranslated("predicate.distance.y");
    private static final Text DISTANCE_Z_KEY = ModUtils.getScreenTranslated("predicate.distance.z");

    private static final Text MOVEMENT_ABSOLUTE_KEY = ModUtils.getScreenTranslated("predicate.movement.absolute");
    private static final Text MOVEMENT_HORIZONTAL_KEY = ModUtils.getScreenTranslated("predicate.movement.horizontal");
    private static final Text MOVEMENT_VERTICAL_KEY = ModUtils.getScreenTranslated("predicate.movement.vertical");
    private static final Text MOVEMENT_FALL_DISTANCE_KEY = ModUtils.getScreenTranslated("predicate.movement.fallDistance");
    private static final Text MOVEMENT_X_KEY = ModUtils.getScreenTranslated("predicate.movement.x");
    private static final Text MOVEMENT_Y_KEY = ModUtils.getScreenTranslated("predicate.movement.y");
    private static final Text MOVEMENT_Z_KEY = ModUtils.getScreenTranslated("predicate.movement.z");

    private static final String RANGE_BETWEEN_KEY = ModUtils.getScreenTranslationKey("predicate.range.between");
    private static final String RANGE_LESS_THAN_KEY = ModUtils.getScreenTranslationKey("predicate.range.lessThan");
    private static final String RANGE_GREATER_THAN_KEY = ModUtils.getScreenTranslationKey("predicate.range.greaterThan");

    private static final Text ENTITY_PREDICATE_MISSING = ModUtils.getScreenTranslated("predicate.entity.propertiesMissing");

    private static final Text DAMAGE_SOURCE_DIRECT_HEADER = ModUtils.getScreenTranslated("predicate.damage.directEntityHeader");
    private static final Text DAMAGE_SOURCE_SOURCE_HEADER = ModUtils.getScreenTranslated("predicate.damage.sourceEntityHeader");

    private static <T extends Number & Comparable<T>> Optional<Text> numberRange(NumberRange<T> range) {
        if (range.getMax().isPresent() && range.getMin().isPresent()) {
            return Optional.of(Text.translatable(RANGE_BETWEEN_KEY, range.getMax().get(), range.getMin().get()));
        }
        else if (range.getMax().isPresent() && range.getMin().isEmpty()) {
            return Optional.of(Text.translatable(RANGE_LESS_THAN_KEY, range.getMax().get()));
        }
        else if (range.getMax().isEmpty() && range.getMin().isPresent()) {
            return Optional.of(Text.translatable(RANGE_GREATER_THAN_KEY, range.getMin().get()));
        }
        else {
            return Optional.empty();
        }
    }

    private static <T extends Number & Comparable<T>> void numberRange(NumberRange<T> range, Text label, IndentedTextHolder textHolder) {
        numberRange(range).ifPresent(t -> textHolder.accept(label.copy().append(t)));
    }

    private static void distanceTooltip(DistancePredicate predicate, IndentedTextHolder textHolder) {
        numberRange(predicate.absolute(), DISTANCE_ABSOLUTE_KEY, textHolder);
        numberRange(predicate.horizontal(), DISTANCE_HORIZONTAL_KEY, textHolder);
        numberRange(predicate.x(), DISTANCE_X_KEY, textHolder);
        numberRange(predicate.y(), DISTANCE_Y_KEY, textHolder);
        numberRange(predicate.z(), DISTANCE_Z_KEY, textHolder);
    }

    private static void movementTooltip(MovementPredicate predicate, IndentedTextHolder textHolder) {
        numberRange(predicate.speed(), MOVEMENT_ABSOLUTE_KEY, textHolder);
        numberRange(predicate.horizontalSpeed(), MOVEMENT_HORIZONTAL_KEY, textHolder);
        numberRange(predicate.verticalSpeed(), MOVEMENT_VERTICAL_KEY, textHolder);
        numberRange(predicate.fallDistance(), MOVEMENT_FALL_DISTANCE_KEY, textHolder);
        numberRange(predicate.x(), MOVEMENT_X_KEY, textHolder);
        numberRange(predicate.y(), MOVEMENT_Y_KEY, textHolder);
        numberRange(predicate.z(), MOVEMENT_Z_KEY, textHolder);
    }

    public static void entityTooltip(EntityPredicate predicate, IndentedTextHolder textHolder) {
        if (predicate.type().isPresent() && predicate.type().get().types().size() > 1) {
            textHolder.accept(ModUtils.getScreenTranslated("predicate.entity.type"));
            textHolder.push();
            predicate.type().get().types().forEach(entry -> textHolder.accept(entry.value().getName()));
            textHolder.pop();
        }
        if (predicate.distance().isPresent()) {
            distanceTooltip(predicate.distance().get(), textHolder);
        }
        if (predicate.movement().isPresent()) {
            movementTooltip(predicate.movement().get(), textHolder);
        }
        // TODO finish this
    }

    public static void entityTooltip(LootContextPredicate predicate, IndentedTextHolder textHolder) {
        Optional<EntityPredicate> entityPredicate = entityPredicateFromLootContextPredicate(predicate);
        entityPredicate.ifPresentOrElse(p -> entityTooltip(p, textHolder), () -> textHolder.accept(ENTITY_PREDICATE_MISSING));
    }

    public static void damageSourceTooltip(DamageSourcePredicate predicate, IndentedTextHolder textHolder) {
        predicate.isDirect().ifPresent(mustBeDirect -> {
            String key = mustBeDirect ? "predicate.damage.mustBeDirect" : "predicate.damage.mustBeIndirect";
            textHolder.accept(ModUtils.getScreenTranslated(key));
        });

        predicate.tags().forEach(tagPredicate -> {
            String key = tagPredicate.expected() ? "predicate.tag.expected" : "predicate.tag.notExpected";
            textHolder.accept(ModUtils.getScreenTranslated(key, tagPredicate.tag().id().toString()));
        });

        predicate.directEntity().ifPresent(directEntityPredicate -> {
            textHolder.accept(DAMAGE_SOURCE_DIRECT_HEADER);
            textHolder.push(2);
            entityTooltip(directEntityPredicate, textHolder);
            textHolder.push(-2);
        });

        predicate.sourceEntity().ifPresent(sourceEntityPredicate -> {
            textHolder.accept(DAMAGE_SOURCE_SOURCE_HEADER);
            textHolder.push(2);
            entityTooltip(sourceEntityPredicate, textHolder);
            textHolder.push(-2);
        });
    }

    public static CriterionDisplayElement entityElement(@Nullable EntityPredicate predicate, boolean withTooltip) {
        CriterionDisplayElement element;
        if (predicate != null && predicate.type().isPresent()) {
            List<CriterionDisplayElement> list = predicate.type().get().types().stream().map(RegistryEntry::value).map(ElementUtils::fromEntityType).toList();
            element = new GroupedElement(new TimedSwitchingElement(list), new TextElement(Text.literal("*")));
        }
        else {
            element = new TextElement(ModUtils.getScreenTranslated("predicate.entity.any"));
        }

        if (predicate != null && withTooltip) {
            IndentedTextHolder textHolder = new IndentedTextHolder();
            entityTooltip(predicate, textHolder);
            element = element.withTextTooltip(textHolder.getText());
        }

        return element;
    }

    public static CriterionDisplayElement entityElement(LootContextPredicate predicate, boolean withTooltip) {
        Optional<EntityPredicate> entityPredicate = entityPredicateFromLootContextPredicate(predicate);
        if (entityPredicate.isPresent()) {
            return entityElement(entityPredicate.get(), withTooltip);
        }
        else {
            return new TextElement(ModUtils.getScreenTranslated("predicate.entity.any"));
        }
    }

    private static Optional<EntityPredicate> entityPredicateFromLootContextPredicate(LootContextPredicate predicate) {
        for (LootCondition condition : predicate.conditions) {
            if (condition instanceof EntityPropertiesLootCondition entityPropertiesCondition) {
                return entityPropertiesCondition.predicate();
            }
        }
        return Optional.empty();
    }
}
