package io.github.tr100000.researcher.api.criterion.util;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.api.criterion.CriterionDisplayElement;
import io.github.tr100000.researcher.api.criterion.element.GroupedElement;
import io.github.tr100000.researcher.api.criterion.element.TextElement;
import io.github.tr100000.researcher.api.criterion.element.TimedSwitchingElement;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.loot.condition.EntityPropertiesLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.predicate.entity.EntityEffectPredicate;
import net.minecraft.predicate.entity.EntityFlagsPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public final class EntityPredicateHelper {
    private EntityPredicateHelper() {}

    private static final Text PREDICATE_MISSING = ModUtils.getScreenTranslated("predicate.entity.properties_missing");
    private static final Text ANY_ENTITY = ModUtils.getScreenTranslated("predicate.entity.any");

    private static final Text ENTITY_TYPE = ModUtils.getScreenTranslated("predicate.entity.type");
    private static final Text ENTITY_LOCATED = ModUtils.getScreenTranslated("predicate.entity.location");
    private static final Text ENTITY_STEPPING_ON = ModUtils.getScreenTranslated("predicate.entity.stepping_on");
    private static final Text ENTITY_MOVEMENT_AFFECTED_BY = ModUtils.getScreenTranslated("predicate.entity.movement_affected_by");
    private static final Text ENTITY_EFFECTS_HEADER = ModUtils.getScreenTranslated("predicate.entity.effects");

    private static final String EFFECT_HEADER_KEY = ModUtils.getScreenTranslationKey("predicate.entity.effect.header");
    private static final Text EFFECT_AMPLIFIER = ModUtils.getScreenTranslated("predicate.entity.effect.amplifier");
    private static final Text EFFECT_DURATION = ModUtils.getScreenTranslated("predicate.entity.effect.duration");
    private static final Text EFFECT_AMBIENT = ModUtils.getScreenTranslated("predicate.entity.effect.ambient");
    private static final Text EFFECT_NOT_AMBIENT = ModUtils.getScreenTranslated("predicate.entity.effect.not_ambient");
    private static final Text EFFECT_VISIBLE = ModUtils.getScreenTranslated("predicate.entity.effect.visible");
    private static final Text EFFECT_NOT_VISIBLE = ModUtils.getScreenTranslated("predicate.entity.effect.not_visible");

    private static final Text FLAGS_ON_GROUND = ModUtils.getScreenTranslated("predicate.entity.flags.on_ground");
    private static final Text FLAGS_NOT_ON_GROUND = ModUtils.getScreenTranslated("predicate.entity.flags.not_on_ground");
    private static final Text FLAGS_ON_FIRE = ModUtils.getScreenTranslated("predicate.entity.flags.on_fire");
    private static final Text FLAGS_NOT_ON_FIRE = ModUtils.getScreenTranslated("predicate.entity.flags.not_on_fire");
    private static final Text FLAGS_SNEAKING = ModUtils.getScreenTranslated("predicate.entity.flags.sneaking");
    private static final Text FLAGS_NOT_SNEAKING = ModUtils.getScreenTranslated("predicate.entity.flags.not_sneaking");
    private static final Text FLAGS_SPRINTING = ModUtils.getScreenTranslated("predicate.entity.flags.sprinting");
    private static final Text FLAGS_NOT_SPRINTING = ModUtils.getScreenTranslated("predicate.entity.flags.not_sprinting");
    private static final Text FLAGS_SWIMMING = ModUtils.getScreenTranslated("predicate.entity.flags.swimming");
    private static final Text FLAGS_NOT_SWIMMING = ModUtils.getScreenTranslated("predicate.entity.flags.not_swimming");
    private static final Text FLAGS_FLYING = ModUtils.getScreenTranslated("predicate.entity.flags.flying");
    private static final Text FLAGS_NOT_FLYING = ModUtils.getScreenTranslated("predicate.entity.flags.not_flying");
    private static final Text FLAGS_BABY = ModUtils.getScreenTranslated("predicate.entity.flags.baby");
    private static final Text FLAGS_NOT_BABY = ModUtils.getScreenTranslated("predicate.entity.flags.not_baby");

    public static void tooltip(EntityPredicate predicate, IndentedTextHolder textHolder) {
        if (predicate.type().isPresent() && predicate.type().get().types().size() > 1) {
            textHolder.accept(ENTITY_TYPE);
            textHolder.push();
            predicate.type().get().types().forEach(entry -> textHolder.accept(entry.value().getName()));
            textHolder.pop();
        }
        if (predicate.distance().isPresent()) {
            DistancePredicateHelper.tooltip(predicate.distance().get(), textHolder);
        }
        if (predicate.movement().isPresent()) {
            MovementPredicateHelper.tooltip(predicate.movement().get(), textHolder);
        }
        if (predicate.location().located().isPresent()) {
            textHolder.accept(ENTITY_LOCATED);
            textHolder.push();
            LocationPredicateHelper.tooltip(predicate.location().located().get(), textHolder);
            textHolder.pop();
        }
        if (predicate.location().steppingOn().isPresent()) {
            textHolder.accept(ENTITY_STEPPING_ON);
            textHolder.push();
            LocationPredicateHelper.tooltip(predicate.location().steppingOn().get(), textHolder);
            textHolder.pop();
        }
        if (predicate.location().affectsMovement().isPresent()) {
            textHolder.accept(ENTITY_MOVEMENT_AFFECTED_BY);
            textHolder.push();
            LocationPredicateHelper.tooltip(predicate.location().affectsMovement().get(), textHolder);
            textHolder.pop();
        }
        if (predicate.effects().isPresent()) {
            textHolder.accept(ENTITY_EFFECTS_HEADER);
            textHolder.push();
            effectTooltip(predicate.effects().get(), textHolder);
            textHolder.pop();
        }
        if (predicate.nbt().isPresent()) {
            PredicateHelper.nbtTooltip(predicate.nbt().get(), textHolder);
        }
        if (predicate.flags().isPresent()) {
            flagsTooltip(predicate.flags().get(), textHolder);
        }
        // TODO finish this
    }

    public static void tooltip(@Nullable LootContextPredicate predicate, IndentedTextHolder textHolder) {
        Optional<EntityPredicate> entityPredicate = entityPredicateFromLootContextPredicate(predicate);
        entityPredicate.ifPresentOrElse(p -> tooltip(p, textHolder), () -> textHolder.accept(PREDICATE_MISSING));
    }

    public static CriterionDisplayElement element(@Nullable EntityPredicate predicate) {
        CriterionDisplayElement element;
        if (predicate != null && predicate.type().isPresent()) {
            List<CriterionDisplayElement> list = predicate.type().get().types().stream().map(RegistryEntry::value).map(ElementUtils::fromEntityType).toList();
            element = new GroupedElement(new TimedSwitchingElement(list), new TextElement(Text.literal("*")));
        }
        else {
            element = new TextElement(ANY_ENTITY);
        }

        return element;
    }

    public static CriterionDisplayElement element(LootContextPredicate predicate) {
        Optional<EntityPredicate> entityPredicate = entityPredicateFromLootContextPredicate(predicate);
        return entityPredicate.map(EntityPredicateHelper::element).orElseGet(() -> new TextElement(ANY_ENTITY));
    }

    private static Optional<EntityPredicate> entityPredicateFromLootContextPredicate(@Nullable LootContextPredicate predicate) {
        if (predicate == null) return Optional.empty();

        for (LootCondition condition : predicate.conditions) {
            if (condition instanceof EntityPropertiesLootCondition entityPropertiesCondition) {
                return entityPropertiesCondition.predicate();
            }
        }
        return Optional.empty();
    }

    public static void effectTooltip(EntityEffectPredicate predicate, IndentedTextHolder textHolder) {
        predicate.effects().forEach((entry, data) -> {
            textHolder.accept(Text.translatable(EFFECT_HEADER_KEY, entry.getKey().orElseThrow().getValue().toTranslationKey("effect")));
            textHolder.push();

            NumberRangeUtils.tooltip(data.amplifier(), EFFECT_AMPLIFIER, textHolder);
            NumberRangeUtils.tooltip(data.duration(), EFFECT_DURATION, textHolder);
            PredicateHelper.optionalBooleanTooltip(data.ambient(), EFFECT_AMBIENT, EFFECT_NOT_AMBIENT, textHolder);
            PredicateHelper.optionalBooleanTooltip(data.visible(), EFFECT_VISIBLE, EFFECT_NOT_VISIBLE, textHolder);

            textHolder.pop();
        });
    }

    public static void flagsTooltip(EntityFlagsPredicate predicate, IndentedTextHolder textHolder) {
        PredicateHelper.optionalBooleanTooltip(predicate.isOnGround(), FLAGS_ON_GROUND, FLAGS_NOT_ON_GROUND, textHolder);
        PredicateHelper.optionalBooleanTooltip(predicate.isOnFire(), FLAGS_ON_FIRE, FLAGS_NOT_ON_FIRE, textHolder);
        PredicateHelper.optionalBooleanTooltip(predicate.isSneaking(), FLAGS_SNEAKING, FLAGS_NOT_SNEAKING, textHolder);
        PredicateHelper.optionalBooleanTooltip(predicate.isSprinting(), FLAGS_SPRINTING, FLAGS_NOT_SPRINTING, textHolder);
        PredicateHelper.optionalBooleanTooltip(predicate.isSwimming(), FLAGS_SWIMMING, FLAGS_NOT_SWIMMING, textHolder);
        PredicateHelper.optionalBooleanTooltip(predicate.isFlying(), FLAGS_FLYING, FLAGS_NOT_FLYING, textHolder);
        PredicateHelper.optionalBooleanTooltip(predicate.isBaby(), FLAGS_BABY, FLAGS_NOT_BABY, textHolder);
    }
}
