package io.github.tr100000.researcher.api.trigger.util;

import com.mojang.serialization.Codec;
import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.Researcher;
import io.github.tr100000.researcher.api.trigger.TriggerDisplayElement;
import io.github.tr100000.researcher.api.trigger.element.GroupedElement;
import io.github.tr100000.researcher.api.trigger.element.IconElement;
import io.github.tr100000.researcher.api.trigger.element.TextElement;
import io.github.tr100000.researcher.api.trigger.element.TimedSwitchingElement;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import io.github.tr100000.trutils.api.gui.Icon;
import io.github.tr100000.trutils.api.gui.ItemIcon;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.advancements.predicates.ContextAwarePredicate;
import net.minecraft.advancements.predicates.GameTypePredicate;
import net.minecraft.advancements.predicates.MobEffectsPredicate;
import net.minecraft.advancements.predicates.entity.CubeMobPredicate;
import net.minecraft.advancements.predicates.entity.DistanceToPlayerPredicate;
import net.minecraft.advancements.predicates.entity.EntityEffectsPredicate;
import net.minecraft.advancements.predicates.entity.EntityEquipmentPredicate;
import net.minecraft.advancements.predicates.entity.EntityExactDataComponentsPredicate;
import net.minecraft.advancements.predicates.entity.EntityFlagsPredicate;
import net.minecraft.advancements.predicates.entity.EntityLocationPredicate;
import net.minecraft.advancements.predicates.entity.EntityNbtPredicate;
import net.minecraft.advancements.predicates.entity.EntityPartialComponentsPredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.predicates.entity.EntitySlotsPredicate;
import net.minecraft.advancements.predicates.entity.EntitySubPredicate;
import net.minecraft.advancements.predicates.entity.EntityTagPredicate;
import net.minecraft.advancements.predicates.entity.EntityTypePredicate;
import net.minecraft.advancements.predicates.entity.FishingHookPredicate;
import net.minecraft.advancements.predicates.entity.LightningBoltPredicate;
import net.minecraft.advancements.predicates.entity.MovementAffectedByPredicate;
import net.minecraft.advancements.predicates.entity.MovementPredicate;
import net.minecraft.advancements.predicates.entity.PassengerPredicate;
import net.minecraft.advancements.predicates.entity.PeriodicEntityTickPredicate;
import net.minecraft.advancements.predicates.entity.PlayerPredicate;
import net.minecraft.advancements.predicates.entity.RaiderPredicate;
import net.minecraft.advancements.predicates.entity.SheepPredicate;
import net.minecraft.advancements.predicates.entity.SteppingOnPredicate;
import net.minecraft.advancements.predicates.entity.TargetedEntityPredicate;
import net.minecraft.advancements.predicates.entity.TeamPredicate;
import net.minecraft.advancements.predicates.entity.VehiclePredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.HangingEntityItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

public final class EntityPredicateHelper {
    private EntityPredicateHelper() {}

    private static final Map<Codec<? extends EntitySubPredicate>, BiConsumer<Object, IndentedTextHolder>> ENTITY_SUB_PREDICATE_HANDLERS = new Object2ObjectOpenHashMap<>();

    private static final Map<EntityType<?>, Icon> ENTITY_TYPE_ICONS = new Object2ObjectOpenHashMap<>();

    private static final Component PREDICATE_MISSING = ModUtils.getScreenTranslated("predicate.entity.properties_missing");
    private static final Component ANY_ENTITY = ModUtils.getScreenTranslated("predicate.entity.any");

    private static final Component ENTITY_TYPE = ModUtils.getScreenTranslated("predicate.entity.type");
    private static final Component ENTITY_LOCATION = ModUtils.getScreenTranslated("predicate.entity.location");
    private static final Component ENTITY_STEPPING_ON = ModUtils.getScreenTranslated("predicate.entity.stepping_on");
    private static final Component ENTITY_MOVEMENT_AFFECTED_BY = ModUtils.getScreenTranslated("predicate.entity.movement_affected_by");
    private static final Component ENTITY_DISTANCE_TO_PLAYER = ModUtils.getScreenTranslated("predicate.entity.distance_to_player");
    private static final Component ENTITY_EFFECTS_HEADER = ModUtils.getScreenTranslated("predicate.entity.effects");
    private static final String ENTITY_PERIODIC_KEY = ModUtils.getScreenTranslationKey("predicate.entity.periodic");
    private static final Component ENTITY_VEHICLE = ModUtils.getScreenTranslated("predicate.entity.vehicle");
    private static final Component ENTITY_PASSENGER = ModUtils.getScreenTranslated("predicate.entity.passenger");
    private static final Component ENTITY_TARGETED = ModUtils.getScreenTranslated("predicate.entity.targeted");
    private static final String ENTITY_TEAM_KEY = ModUtils.getScreenTranslationKey("predicate.entity.team");
    private static final Component ENTITY_EXACT_COMPONENTS_HEADER = ModUtils.getScreenTranslated("predicate.components");
    private static final Component ENTITY_TAGS_ALL_OF_HEADER = ModUtils.getScreenTranslated("predicate.entity.tag.all_of");
    private static final Component ENTITY_TAGS_ANY_OF_HEADER = ModUtils.getScreenTranslated("predicate.entity.tag.any_of");
    private static final Component ENTITY_TAGS_NONE_OF_HEADER = ModUtils.getScreenTranslated("predicate.entity.tag.none_of");

    private static final String EFFECT_HEADER_KEY = ModUtils.getScreenTranslationKey("predicate.entity.effect.header");
    private static final Component EFFECT_AMPLIFIER = ModUtils.getScreenTranslated("predicate.entity.effect.amplifier");
    private static final Component EFFECT_DURATION = ModUtils.getScreenTranslated("predicate.entity.effect.duration");
    private static final Component EFFECT_AMBIENT = ModUtils.getScreenTranslated("predicate.entity.effect.ambient");
    private static final Component EFFECT_NOT_AMBIENT = ModUtils.getScreenTranslated("predicate.entity.effect.not_ambient");
    private static final Component EFFECT_VISIBLE = ModUtils.getScreenTranslated("predicate.entity.effect.visible");
    private static final Component EFFECT_NOT_VISIBLE = ModUtils.getScreenTranslated("predicate.entity.effect.not_visible");

    private static final Component FLAGS_ON_GROUND = ModUtils.getScreenTranslated("predicate.entity.flags.on_ground");
    private static final Component FLAGS_NOT_ON_GROUND = ModUtils.getScreenTranslated("predicate.entity.flags.not_on_ground");
    private static final Component FLAGS_ON_FIRE = ModUtils.getScreenTranslated("predicate.entity.flags.on_fire");
    private static final Component FLAGS_NOT_ON_FIRE = ModUtils.getScreenTranslated("predicate.entity.flags.not_on_fire");
    private static final Component FLAGS_SNEAKING = ModUtils.getScreenTranslated("predicate.entity.flags.sneaking");
    private static final Component FLAGS_NOT_SNEAKING = ModUtils.getScreenTranslated("predicate.entity.flags.not_sneaking");
    private static final Component FLAGS_SPRINTING = ModUtils.getScreenTranslated("predicate.entity.flags.sprinting");
    private static final Component FLAGS_NOT_SPRINTING = ModUtils.getScreenTranslated("predicate.entity.flags.not_sprinting");
    private static final Component FLAGS_SWIMMING = ModUtils.getScreenTranslated("predicate.entity.flags.swimming");
    private static final Component FLAGS_NOT_SWIMMING = ModUtils.getScreenTranslated("predicate.entity.flags.not_swimming");
    private static final Component FLAGS_FLYING = ModUtils.getScreenTranslated("predicate.entity.flags.flying");
    private static final Component FLAGS_NOT_FLYING = ModUtils.getScreenTranslated("predicate.entity.flags.not_flying");
    private static final Component FLAGS_BABY = ModUtils.getScreenTranslated("predicate.entity.flags.baby");
    private static final Component FLAGS_NOT_BABY = ModUtils.getScreenTranslated("predicate.entity.flags.not_baby");
    private static final Component FLAGS_IN_WATER = ModUtils.getScreenTranslated("predicate.entity.flags.in_water");
    private static final Component FLAGS_NOT_IN_WATER = ModUtils.getScreenTranslated("predicate.entity.flags.not_in_water");
    private static final Component FLAGS_FALL_FLYING = ModUtils.getScreenTranslated("predicate.entity.flags.fall_flying");
    private static final Component FLAGS_NOT_FALL_FLYING = ModUtils.getScreenTranslated("predicate.entity.flags.not_fall_flying");

    private static final Component EQUIPMENT_HEAD = ModUtils.getScreenTranslated("predicate.entity.equipment.head");
    private static final Component EQUIPMENT_CHEST = ModUtils.getScreenTranslated("predicate.entity.equipment.chest");
    private static final Component EQUIPMENT_LEGS = ModUtils.getScreenTranslated("predicate.entity.equipment.legs");
    private static final Component EQUIPMENT_FEET = ModUtils.getScreenTranslated("predicate.entity.equipment.feet");
    private static final Component EQUIPMENT_BODY = ModUtils.getScreenTranslated("predicate.entity.equipment.body");
    private static final Component EQUIPMENT_MAINHAND = ModUtils.getScreenTranslated("predicate.entity.equipment.mainhand");
    private static final Component EQUIPMENT_OFFHAND = ModUtils.getScreenTranslated("predicate.entity.equipment.offhand");

    private static final Component LIGHTNING_BLOCKS_SET_ON_FIRE = ModUtils.getScreenTranslated("predicate.entity.lightning.blocks_set_on_fire");
    private static final Component LIGHTNING_ENTITY_STRUCK = ModUtils.getScreenTranslated("predicate.entity.lightning.entity_struck");
    private static final Component FISHING_OPEN_WATER = ModUtils.getScreenTranslated("predicate.entity.fishing.open_water");
    private static final Component FISHING_NOT_OPEN_WATER = ModUtils.getScreenTranslated("predicate.entity.fishing.not_open_water");
    private static final Component PLAYER_EXPERIENCE_LEVEL = ModUtils.getScreenTranslated("predicate.entity.player.xp_level");
    private static final Component PLAYER_FOOD_LEVEL = ModUtils.getScreenTranslated("predicate.entity.player.food.level");
    private static final Component PLAYER_FOOD_SATURATION = ModUtils.getScreenTranslated("predicate.entity.food.saturation");
    private static final Component PLAYER_GAME_MODE = ModUtils.getScreenTranslated("predicate.entity.player.game_mode");
    private static final Component PLAYER_STATS = ModUtils.getScreenTranslated("predicate.entity.player.stats");
    private static final String PLAYER_HAS_RECIPE = ModUtils.getScreenTranslationKey("predicate.entity.player.has_recipe");
    private static final String PLAYER_DOESNT_HAVE_RECIPE = ModUtils.getScreenTranslationKey("predicate.entity.player.doesnt_have_recipe");
    private static final String PLAYER_ADVANCEMENT = ModUtils.getScreenTranslationKey("predicate.entity.player.advancement");
    private static final String PLAYER_ADVANCEMENT_CRITERIA_DONE = ModUtils.getScreenTranslationKey("predicate.entity.player.advancement.criteria.done");
    private static final String PLAYER_ADVANCEMENT_CRITERIA_NOT_DONE = ModUtils.getScreenTranslationKey("predicate.entity.player.advancement.criteria.not_done");
    private static final Component PLAYER_ADVANCEMENT_DONE = ModUtils.getScreenTranslated("predicate.entity.player.advancement.done");
    private static final Component PLAYER_ADVANCEMENT_NOT_DONE = ModUtils.getScreenTranslated("predicate.entity.player.advancement.not_done");
    private static final Component PLAYER_LOOKING_AT = ModUtils.getScreenTranslated("predicate.entity.player.looking_at");
    private static final Component PLAYER_INPUT_FORWARD = ModUtils.getScreenTranslated("predicate.entity.player.input.forward");
    private static final Component PLAYER_INPUT_NOT_FORWARD = ModUtils.getScreenTranslated("predicate.entity.player.input.not_forward");
    private static final Component PLAYER_INPUT_BACKWARD = ModUtils.getScreenTranslated("predicate.entity.player.input.backward");
    private static final Component PLAYER_INPUT_NOT_BACKWARD = ModUtils.getScreenTranslated("predicate.entity.player.input.not_backward");
    private static final Component PLAYER_INPUT_LEFT = ModUtils.getScreenTranslated("predicate.entity.player.input.left");
    private static final Component PLAYER_INPUT_NOT_LEFT = ModUtils.getScreenTranslated("predicate.entity.player.input.not_left");
    private static final Component PLAYER_INPUT_RIGHT = ModUtils.getScreenTranslated("predicate.entity.player.input.right");
    private static final Component PLAYER_INPUT_NOT_RIGHT = ModUtils.getScreenTranslated("predicate.entity.player.input.not_right");
    private static final Component PLAYER_INPUT_JUMP = ModUtils.getScreenTranslated("predicate.entity.player.input.jump");
    private static final Component PLAYER_INPUT_NOT_JUMP = ModUtils.getScreenTranslated("predicate.entity.player.input.not_jump");
    private static final Component PLAYER_INPUT_SNEAK = ModUtils.getScreenTranslated("predicate.entity.player.input.sneak");
    private static final Component PLAYER_INPUT_NOT_SNEAK = ModUtils.getScreenTranslated("predicate.entity.player.input.not_sneak");
    private static final Component PLAYER_INPUT_SPRINT = ModUtils.getScreenTranslated("predicate.entity.player.input.sprint");
    private static final Component PLAYER_INPUT_NOT_SPRINT = ModUtils.getScreenTranslated("predicate.entity.player.input.not_sprint");
    private static final Component CUBE_MOB_SIZE = ModUtils.getScreenTranslated("predicate.entity.cube_mob.size");
    private static final Component RAIDER_HAS_RAID = ModUtils.getScreenTranslated("predicate.entity.raider.has_raid");
    private static final Component RAIDER_DOESNT_HAVE_RAID = ModUtils.getScreenTranslated("predicate.entity.raider.doesnt_have_raid");
    private static final Component RAIDER_IS_CAPTAIN = ModUtils.getScreenTranslated("predicate.entity.raider.is_captain");
    private static final Component RAIDER_NOT_CAPTAIN = ModUtils.getScreenTranslated("predicate.entity.raider.not_captain");
    private static final Component SHEEP_SHEARED = ModUtils.getScreenTranslated("predicate.entity.sheep.sheared");
    private static final Component SHEEP_NOT_SHEARED = ModUtils.getScreenTranslated("predicate.entity.sheep.not_sheared");

    @Contract(mutates = "param2")
    public static void tooltip(EntityPredicate predicate, IndentedTextHolder textHolder) {
        predicate.parts.forEach((type, value) -> {
            if (ENTITY_SUB_PREDICATE_HANDLERS.containsKey(type)) {
                ENTITY_SUB_PREDICATE_HANDLERS.get(type).accept(value, textHolder);
            }
        });
    }

    @Contract(mutates = "param2")
    public static void tooltip(@Nullable ContextAwarePredicate predicate, IndentedTextHolder textHolder) {
        Optional<EntityPredicate> entityPredicate = entityPredicateFromLootContextPredicate(predicate);
        entityPredicate.ifPresentOrElse(p -> tooltip(p, textHolder), () -> textHolder.accept(PREDICATE_MISSING));
    }

    public static Optional<List<MutableComponent>> tooltip(Optional<ContextAwarePredicate> predicate, @Nullable Component headerText) {
        return PredicateHelper.optionalTooltip(predicate, EntityPredicateHelper::tooltip, headerText);
    }

    @Contract(value = "_ -> new", pure = true)
    public static TriggerDisplayElement element(EntityType<?> entityType) {
        return new GroupedElement(
                new IconElement(ENTITY_TYPE_ICONS.getOrDefault(entityType, Icon.ERROR)),
                new TextElement(entityType.getDescription())
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public static TriggerDisplayElement element(@Nullable EntityPredicate predicate) {
        if (predicate != null && predicate.parts.containsKey(EntityTypePredicate.CODEC)) {
            EntityTypePredicate typePredicate = (EntityTypePredicate)predicate.parts.get(EntityTypePredicate.CODEC);
            List<TriggerDisplayElement> list = typePredicate.types().stream().map(Holder::value).map(EntityPredicateHelper::element).toList();
            if (list.size() == 1) {
                return list.getFirst();
            }
            else if (list.size() >= 2) {
                return new GroupedElement(new TimedSwitchingElement(list), new TextElement(Component.literal("*")));
            }
        }
        return new TextElement(ANY_ENTITY);
    }

    @Contract(value = "_ -> new", pure = true)
    public static TriggerDisplayElement element(@Nullable ContextAwarePredicate predicate) {
        Optional<EntityPredicate> entityPredicate = entityPredicateFromLootContextPredicate(predicate);
        return entityPredicate.map(EntityPredicateHelper::element).orElseGet(() -> new TextElement(ANY_ENTITY));
    }

    @Contract(value = "_ -> new", pure = true)
    public static TriggerDisplayElement vehicleElement(@Nullable ContextAwarePredicate predicate) {
        Optional<EntityPredicate> entityPredicate = entityPredicateFromLootContextPredicate(predicate);
        EntityPredicate vehiclePredicate = entityPredicate.map(p -> ((VehiclePredicate)p.parts.get(VehiclePredicate.CODEC)).vehicle()).orElse(null);
        return element(vehiclePredicate);
    }

    @Contract(pure = true)
    private static Optional<EntityPredicate> entityPredicateFromLootContextPredicate(@Nullable ContextAwarePredicate predicate) {
        if (predicate == null) return Optional.empty();

        for (LootItemCondition condition : predicate.conditions) {
            if (condition instanceof LootItemEntityPropertyCondition entityPropertiesCondition) {
                return entityPropertiesCondition.predicate();
            }
        }
        return Optional.empty();
    }

    @Contract(mutates = "param2")
    public static void entityTypeTooltip(EntityTypePredicate predicate, IndentedTextHolder textHolder) {
        textHolder.accept(ENTITY_TYPE);
        textHolder.push();
        predicate.types().forEach(entry -> textHolder.accept(entry.value().getDescription()));
        textHolder.pop();
    }

    @Contract(mutates = "param2")
    public static void locationTooltip(EntityLocationPredicate predicate, IndentedTextHolder textHolder) {
        textHolder.accept(ENTITY_LOCATION);
        textHolder.push();
        LocationPredicateHelper.tooltip(predicate.predicate(), textHolder);
        textHolder.pop();
    }

    @Contract(mutates = "param2")
    public static void steppingOnTooltip(SteppingOnPredicate predicate, IndentedTextHolder textHolder) {
        textHolder.accept(ENTITY_STEPPING_ON);
        textHolder.push();
        LocationPredicateHelper.tooltip(predicate.predicate(), textHolder);
        textHolder.pop();
    }

    @Contract(mutates = "param2")
    public static void movementAffectedByTooltip(MovementAffectedByPredicate predicate, IndentedTextHolder textHolder) {
        textHolder.accept(ENTITY_MOVEMENT_AFFECTED_BY);
        textHolder.push();
        LocationPredicateHelper.tooltip(predicate.predicate(), textHolder);
        textHolder.pop();
    }

    @Contract(mutates = "param2")
    public static void distanceToPlayerTooltip(DistanceToPlayerPredicate predicate, IndentedTextHolder textHolder) {
        textHolder.accept(ENTITY_DISTANCE_TO_PLAYER);
        textHolder.push();
        DistancePredicateHelper.tooltip(predicate.distance(), textHolder);
        textHolder.pop();
    }

    @Contract(mutates = "param2")
    public static void effectsTooltip(EntityEffectsPredicate predicate, IndentedTextHolder textHolder) {
        textHolder.accept(ENTITY_EFFECTS_HEADER);
        textHolder.push();
        effectTooltip(predicate.effects(), textHolder);
        textHolder.pop();
    }

    @Contract(mutates = "param2")
    public static void effectTooltip(MobEffectsPredicate predicate, IndentedTextHolder textHolder) {
        predicate.effectMap().forEach((entry, data) -> {
            textHolder.accept(Component.translatable(EFFECT_HEADER_KEY, entry.unwrapKey().orElseThrow().identifier().toLanguageKey("effect")));
            textHolder.push();

            MinMaxBoundsUtils.tooltip(data.amplifier(), EFFECT_AMPLIFIER, textHolder);
            MinMaxBoundsUtils.tooltip(data.duration(), EFFECT_DURATION, textHolder);
            PredicateHelper.optionalBooleanTooltip(data.ambient(), EFFECT_AMBIENT, EFFECT_NOT_AMBIENT, textHolder);
            PredicateHelper.optionalBooleanTooltip(data.visible(), EFFECT_VISIBLE, EFFECT_NOT_VISIBLE, textHolder);

            textHolder.pop();
        });
    }

    @Contract(mutates = "param2")
    public static void nbtTooltip(EntityNbtPredicate predicate, IndentedTextHolder textHolder) {
        PredicateHelper.nbtTooltip(predicate.nbt(), textHolder);
    }

    @Contract(mutates = "param2")
    public static void flagsTooltip(EntityFlagsPredicate predicate, IndentedTextHolder textHolder) {
        PredicateHelper.optionalBooleanTooltip(predicate.isOnGround(), FLAGS_ON_GROUND, FLAGS_NOT_ON_GROUND, textHolder);
        PredicateHelper.optionalBooleanTooltip(predicate.isOnFire(), FLAGS_ON_FIRE, FLAGS_NOT_ON_FIRE, textHolder);
        PredicateHelper.optionalBooleanTooltip(predicate.isCrouching(), FLAGS_SNEAKING, FLAGS_NOT_SNEAKING, textHolder);
        PredicateHelper.optionalBooleanTooltip(predicate.isSprinting(), FLAGS_SPRINTING, FLAGS_NOT_SPRINTING, textHolder);
        PredicateHelper.optionalBooleanTooltip(predicate.isSwimming(), FLAGS_SWIMMING, FLAGS_NOT_SWIMMING, textHolder);
        PredicateHelper.optionalBooleanTooltip(predicate.isFlying(), FLAGS_FLYING, FLAGS_NOT_FLYING, textHolder);
        PredicateHelper.optionalBooleanTooltip(predicate.isBaby(), FLAGS_BABY, FLAGS_NOT_BABY, textHolder);
        PredicateHelper.optionalBooleanTooltip(predicate.isInWater(), FLAGS_IN_WATER, FLAGS_NOT_IN_WATER, textHolder);
        PredicateHelper.optionalBooleanTooltip(predicate.isFallFlying(), FLAGS_FALL_FLYING, FLAGS_NOT_FALL_FLYING, textHolder);
    }

    @Contract(mutates = "param2")
    public static void equipmentTooltip(EntityEquipmentPredicate predicate, IndentedTextHolder textHolder) {
        if (predicate.head().isPresent()) {
            textHolder.accept(EQUIPMENT_HEAD);
            textHolder.push();
            ItemPredicateHelper.tooltip(predicate.head().get(), textHolder);
            textHolder.pop();
        }
        if (predicate.chest().isPresent()) {
            textHolder.accept(EQUIPMENT_CHEST);
            textHolder.push();
            ItemPredicateHelper.tooltip(predicate.chest().get(), textHolder);
            textHolder.pop();
        }
        if (predicate.legs().isPresent()) {
            textHolder.accept(EQUIPMENT_LEGS);
            textHolder.push();
            ItemPredicateHelper.tooltip(predicate.legs().get(), textHolder);
            textHolder.pop();
        }
        if (predicate.feet().isPresent()) {
            textHolder.accept(EQUIPMENT_FEET);
            textHolder.push();
            ItemPredicateHelper.tooltip(predicate.feet().get(), textHolder);
            textHolder.pop();
        }
        if (predicate.body().isPresent()) {
            textHolder.accept(EQUIPMENT_BODY);
            textHolder.push();
            ItemPredicateHelper.tooltip(predicate.body().get(), textHolder);
            textHolder.pop();
        }
        if (predicate.mainhand().isPresent()) {
            textHolder.accept(EQUIPMENT_MAINHAND);
            textHolder.push();
            ItemPredicateHelper.tooltip(predicate.mainhand().get(), textHolder);
            textHolder.pop();
        }
        if (predicate.offhand().isPresent()) {
            textHolder.accept(EQUIPMENT_OFFHAND);
            textHolder.push();
            ItemPredicateHelper.tooltip(predicate.offhand().get(), textHolder);
            textHolder.pop();
        }
    }

    @Contract(mutates = "param2")
    public static void periodicTickTooltip(PeriodicEntityTickPredicate predicate, IndentedTextHolder textHolder) {
        textHolder.accept(Component.translatable(ENTITY_PERIODIC_KEY, predicate.periodicTick()));
    }

    @Contract(mutates = "param2")
    public static void vehicleTooltip(VehiclePredicate predicate, IndentedTextHolder textHolder) {
        textHolder.accept(ENTITY_VEHICLE);
        textHolder.push();
        tooltip(predicate.vehicle(), textHolder);
        textHolder.pop();
    }

    @Contract(mutates = "param2")
    public static void passengerTooltip(PassengerPredicate predicate, IndentedTextHolder textHolder) {
        textHolder.accept(ENTITY_PASSENGER);
        textHolder.push();
        tooltip(predicate.passenger(), textHolder);
        textHolder.pop();
    }

    @Contract(mutates = "param2")
    public static void targetedEntityTooltip(TargetedEntityPredicate predicate, IndentedTextHolder textHolder) {
        textHolder.accept(ENTITY_TARGETED);
        textHolder.push();
        tooltip(predicate.targetedEntity(), textHolder);
        textHolder.pop();
    }

    @Contract(mutates = "param2")
    public static void teamTooltip(TeamPredicate predicate, IndentedTextHolder textHolder) {
        textHolder.accept(Component.translatable(ENTITY_TEAM_KEY, predicate.team()));
    }

    @Contract(mutates = "param2")
    public static void slotsTooltip(EntitySlotsPredicate predicate, IndentedTextHolder textHolder) {
        // TODO
        textHolder.accept(Component.literal("TODO slot conditions"));
    }

    @Contract(mutates = "param2")
    public static void componentsTooltip(EntityExactDataComponentsPredicate predicate, IndentedTextHolder textHolder) {
        textHolder.accept(ENTITY_EXACT_COMPONENTS_HEADER);
        textHolder.push();
        ComponentsPredicateHelper.exactTooltip(predicate.predicate(), textHolder);
        textHolder.pop();
    }

    @Contract(mutates = "param2")
    public static void predicatesTooltip(EntityPartialComponentsPredicate predicate, IndentedTextHolder textHolder) {
        ComponentsPredicateHelper.partialTooltip(predicate.predicates(), textHolder);
    }

    @Contract(mutates = "param2")
    public static void tagTooltip(EntityTagPredicate predicate, IndentedTextHolder textHolder) {
        if (predicate.allOf().isPresent() && !predicate.allOf().get().isEmpty()) {
            textHolder.accept(ENTITY_TAGS_ALL_OF_HEADER);
            textHolder.push();
            predicate.allOf().get().forEach(tag -> textHolder.accept(Component.literal(tag)));
            textHolder.pop();
        }
        if (predicate.anyOf().isPresent() && !predicate.anyOf().get().isEmpty()) {
            textHolder.accept(ENTITY_TAGS_ANY_OF_HEADER);
            textHolder.push();
            predicate.anyOf().get().forEach(tag -> textHolder.accept(Component.literal(tag)));
            textHolder.pop();
        }
        if (predicate.noneOf().isPresent() && !predicate.noneOf().get().isEmpty()) {
            textHolder.accept(ENTITY_TAGS_NONE_OF_HEADER);
            textHolder.push();
            predicate.noneOf().get().forEach(tag -> textHolder.accept(Component.literal(tag)));
            textHolder.pop();
        }
    }

    @Contract(mutates = "param2")
    public static void typeSpecificLightningTooltip(LightningBoltPredicate predicate, IndentedTextHolder textHolder) {
        MinMaxBoundsUtils.tooltip(predicate.blocksSetOnFire(), LIGHTNING_BLOCKS_SET_ON_FIRE, textHolder);
        if (predicate.entityStruck().isPresent()) {
            textHolder.accept(LIGHTNING_ENTITY_STRUCK);
            textHolder.push();
            tooltip(predicate.entityStruck().get(), textHolder);
            textHolder.pop();
        }
    }

    @Contract(mutates = "param2")
    public static void typeSpecificFishingHookTooltip(FishingHookPredicate predicate, IndentedTextHolder textHolder) {
        PredicateHelper.optionalBooleanTooltip(predicate.inOpenWater(), FISHING_OPEN_WATER, FISHING_NOT_OPEN_WATER, textHolder);
    }

    @Contract(mutates = "param2")
    public static void typeSpecificPlayerTooltip(PlayerPredicate predicate, IndentedTextHolder textHolder) {
        MinMaxBoundsUtils.tooltip(predicate.level(), PLAYER_EXPERIENCE_LEVEL, textHolder);
        if (predicate.gameType() != GameTypePredicate.ANY) {
            textHolder.accept(PLAYER_GAME_MODE);
            textHolder.push();
            predicate.gameType().types().forEach(mode -> textHolder.accept(mode.getLongDisplayName()));
            textHolder.pop();
        }
        MinMaxBoundsUtils.tooltip(predicate.food().level(), PLAYER_FOOD_LEVEL, textHolder);
        MinMaxBoundsUtils.tooltip(predicate.food().saturation(), PLAYER_FOOD_SATURATION, textHolder);
        if (!predicate.stats().isEmpty()) {
            textHolder.accept(PLAYER_STATS);
            textHolder.push();
            predicate.stats().forEach(matcher -> MinMaxBoundsUtils.tooltip(matcher.range(), Component.translatable(matcher.value().unwrapKey().orElseThrow().identifier().toLanguageKey("stat")), textHolder));
            textHolder.pop();
        }
        predicate.recipes().forEach((key, expected) -> {
            textHolder.accept(Component.translatable(expected ? PLAYER_HAS_RECIPE : PLAYER_DOESNT_HAVE_RECIPE, key.identifier().toString()));
        });
        if (!predicate.advancements().isEmpty()) {
            predicate.advancements().forEach((id, advPredicate) -> {
                textHolder.accept(Component.translatable(PLAYER_ADVANCEMENT, id.toString()));
                textHolder.push();

                if (advPredicate instanceof PlayerPredicate.AdvancementCriterionsPredicate(Object2BooleanMap<String> criteriaMap)) {
                    criteriaMap.forEach((criteria, done) -> {
                        textHolder.accept(Component.translatable(done ? PLAYER_ADVANCEMENT_CRITERIA_DONE : PLAYER_ADVANCEMENT_CRITERIA_NOT_DONE, criteria));
                    });
                }
                else if (advPredicate instanceof PlayerPredicate.AdvancementDonePredicate(boolean done)) {
                    textHolder.accept(done ? PLAYER_ADVANCEMENT_DONE : PLAYER_ADVANCEMENT_NOT_DONE);
                }

                textHolder.pop();
            });
        }
        if (predicate.lookingAt().isPresent()) {
            textHolder.accept(PLAYER_LOOKING_AT);
            textHolder.push();
            tooltip(predicate.lookingAt().get(), textHolder);
            textHolder.pop();
        }
        if (predicate.input().isPresent()) {
            PredicateHelper.optionalBooleanTooltip(predicate.input().get().forward(), PLAYER_INPUT_FORWARD, PLAYER_INPUT_NOT_FORWARD, textHolder);
            PredicateHelper.optionalBooleanTooltip(predicate.input().get().backward(), PLAYER_INPUT_BACKWARD, PLAYER_INPUT_NOT_BACKWARD, textHolder);
            PredicateHelper.optionalBooleanTooltip(predicate.input().get().left(), PLAYER_INPUT_LEFT, PLAYER_INPUT_NOT_LEFT, textHolder);
            PredicateHelper.optionalBooleanTooltip(predicate.input().get().right(), PLAYER_INPUT_RIGHT, PLAYER_INPUT_NOT_RIGHT, textHolder);
            PredicateHelper.optionalBooleanTooltip(predicate.input().get().jump(), PLAYER_INPUT_JUMP, PLAYER_INPUT_NOT_JUMP, textHolder);
            PredicateHelper.optionalBooleanTooltip(predicate.input().get().sneak(), PLAYER_INPUT_SNEAK, PLAYER_INPUT_NOT_SNEAK, textHolder);
            PredicateHelper.optionalBooleanTooltip(predicate.input().get().sprint(), PLAYER_INPUT_SPRINT, PLAYER_INPUT_NOT_SPRINT, textHolder);
        }
    }

    @Contract(mutates = "param2")
    public static void typeSpecificCubeMobPredicate(CubeMobPredicate predicate, IndentedTextHolder textHolder) {
        MinMaxBoundsUtils.tooltip(predicate.size(), CUBE_MOB_SIZE, textHolder);
    }

    @Contract(mutates = "param2")
    public static void typeSpecificRaiderPredicate(RaiderPredicate predicate, IndentedTextHolder textHolder) {
        textHolder.accept(predicate.hasRaid() ? RAIDER_HAS_RAID : RAIDER_DOESNT_HAVE_RAID);
        textHolder.accept(predicate.isCaptain() ? RAIDER_IS_CAPTAIN : RAIDER_NOT_CAPTAIN);
    }

    @Contract(mutates = "param2")
    public static void typeSpecificSheepPredicate(SheepPredicate predicate, IndentedTextHolder textHolder) {
        PredicateHelper.optionalBooleanTooltip(predicate.sheared(), SHEEP_SHEARED, SHEEP_NOT_SHEARED, textHolder);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T extends EntitySubPredicate> void registerEntitySubPredicateHandler(Codec<T> codec, BiConsumer<T, IndentedTextHolder> handler) {
        Objects.requireNonNull(codec, "codec is null");
        Objects.requireNonNull(handler, "handler in null");
        ENTITY_SUB_PREDICATE_HANDLERS.put(codec, (BiConsumer)handler);
    }

    public static <T extends EntitySubPredicate> void registerEntitySubPredicateHandler(Codec<T> codec, BiConsumer<T, IndentedTextHolder> handler, Component header) {
        registerEntitySubPredicateHandler(codec, (value, textHolder) -> {
            textHolder.accept(header);
            textHolder.push();
            handler.accept(value, textHolder);
            textHolder.pop();
        });
    }

    public static void registerItemForEntityType(EntityType<?> entityType, @Nullable Item item) {
        if (item != null) {
            registerIconForEntityType(entityType, new ItemIcon(item));
        }
    }

    public static void registerIconForEntityType(EntityType<?> entityType, Icon icon) {
        Objects.requireNonNull(entityType, "entityType is null");
        Objects.requireNonNull(icon, "icon in null");
        ENTITY_TYPE_ICONS.put(entityType, icon);
    }

    @ApiStatus.Internal
    public static void printNonRegistered() {
        BuiltInRegistries.ENTITY_SUB_PREDICATE_TYPE.forEach(type -> {
            if (!ENTITY_SUB_PREDICATE_HANDLERS.containsKey(type)) {
                Researcher.LOGGER.warn("{} doesn't have a registered handler", BuiltInRegistries.ENTITY_SUB_PREDICATE_TYPE.getKey(type));
            }
        });

        BuiltInRegistries.ENTITY_TYPE.forEach(entityType -> {
            if (!ENTITY_TYPE_ICONS.containsKey(entityType)) {
                Researcher.LOGGER.warn("{} doesn't have a registered item", BuiltInRegistries.ENTITY_TYPE.getKey(entityType));
            }
        });
    }

    @ApiStatus.Internal
    public static void onItemRegistered(Item item) {
        switch (item) {
            case BoatItem boatItem -> registerItemForEntityType(boatItem.entityType, item);
            case MinecartItem minecartItem -> registerItemForEntityType(minecartItem.type, item);
            case HangingEntityItem hangingEntityItem -> registerItemForEntityType(hangingEntityItem.type, item);
            default -> {}
        }
    }

    @ApiStatus.Internal
    public static void registerDefault() {
        registerEntitySubPredicateHandler(EntityTypePredicate.CODEC, EntityPredicateHelper::entityTypeTooltip);
        registerEntitySubPredicateHandler(EntityLocationPredicate.CODEC, EntityPredicateHelper::locationTooltip);
        registerEntitySubPredicateHandler(SteppingOnPredicate.CODEC, EntityPredicateHelper::steppingOnTooltip);
        registerEntitySubPredicateHandler(MovementAffectedByPredicate.CODEC, EntityPredicateHelper::movementAffectedByTooltip);
        registerEntitySubPredicateHandler(DistanceToPlayerPredicate.CODEC, EntityPredicateHelper::distanceToPlayerTooltip);
        registerEntitySubPredicateHandler(MovementPredicate.CODEC, MovementPredicateHelper::tooltip);
        registerEntitySubPredicateHandler(EntityEffectsPredicate.CODEC, EntityPredicateHelper::effectsTooltip);
        registerEntitySubPredicateHandler(EntityNbtPredicate.CODEC, EntityPredicateHelper::nbtTooltip);
        registerEntitySubPredicateHandler(EntityFlagsPredicate.CODEC, EntityPredicateHelper::flagsTooltip);
        registerEntitySubPredicateHandler(EntityEquipmentPredicate.CODEC, EntityPredicateHelper::equipmentTooltip);
        registerEntitySubPredicateHandler(PeriodicEntityTickPredicate.CODEC, EntityPredicateHelper::periodicTickTooltip);
        registerEntitySubPredicateHandler(VehiclePredicate.CODEC, EntityPredicateHelper::vehicleTooltip);
        registerEntitySubPredicateHandler(PassengerPredicate.CODEC, EntityPredicateHelper::passengerTooltip);
        registerEntitySubPredicateHandler(TargetedEntityPredicate.CODEC, EntityPredicateHelper::targetedEntityTooltip);
        registerEntitySubPredicateHandler(TeamPredicate.CODEC, EntityPredicateHelper::teamTooltip);
        registerEntitySubPredicateHandler(EntitySlotsPredicate.CODEC, EntityPredicateHelper::slotsTooltip);
        registerEntitySubPredicateHandler(EntityExactDataComponentsPredicate.CODEC, EntityPredicateHelper::componentsTooltip);
        registerEntitySubPredicateHandler(EntityPartialComponentsPredicate.CODEC, EntityPredicateHelper::predicatesTooltip);
        registerEntitySubPredicateHandler(EntityTagPredicate.CODEC, EntityPredicateHelper::tagTooltip);
        registerEntitySubPredicateHandler(LightningBoltPredicate.CODEC, EntityPredicateHelper::typeSpecificLightningTooltip);
        registerEntitySubPredicateHandler(FishingHookPredicate.CODEC, EntityPredicateHelper::typeSpecificFishingHookTooltip);
        registerEntitySubPredicateHandler(PlayerPredicate.CODEC, EntityPredicateHelper::typeSpecificPlayerTooltip);
        registerEntitySubPredicateHandler(CubeMobPredicate.CODEC, EntityPredicateHelper::typeSpecificCubeMobPredicate);
        registerEntitySubPredicateHandler(RaiderPredicate.CODEC, EntityPredicateHelper::typeSpecificRaiderPredicate);
        registerEntitySubPredicateHandler(SheepPredicate.CODEC, EntityPredicateHelper::typeSpecificSheepPredicate);

        registerItemForEntityType(EntityTypes.ARMOR_STAND, Items.ARMOR_STAND);
        registerItemForEntityType(EntityTypes.ARROW, Items.ARROW);
        registerItemForEntityType(EntityTypes.SPECTRAL_ARROW, Items.SPECTRAL_ARROW);
        registerItemForEntityType(EntityTypes.BREEZE_WIND_CHARGE, Items.WIND_CHARGE);
        registerItemForEntityType(EntityTypes.EGG, Items.EGG);
        registerItemForEntityType(EntityTypes.ENDER_PEARL, Items.ENDER_PEARL);
        registerItemForEntityType(EntityTypes.END_CRYSTAL, Items.END_CRYSTAL);
        registerItemForEntityType(EntityTypes.EXPERIENCE_BOTTLE, Items.EXPERIENCE_BOTTLE);
        registerItemForEntityType(EntityTypes.EXPERIENCE_ORB, Items.EXPERIENCE_BOTTLE);
        registerItemForEntityType(EntityTypes.EYE_OF_ENDER, Items.ENDER_EYE);
        registerItemForEntityType(EntityTypes.FIREBALL, Items.FIRE_CHARGE);
        registerItemForEntityType(EntityTypes.FIREWORK_ROCKET, Items.FIREWORK_ROCKET);
        registerItemForEntityType(EntityTypes.LEASH_KNOT, Items.LEAD);
        registerItemForEntityType(EntityTypes.SMALL_FIREBALL, Items.FIRE_CHARGE);
        registerItemForEntityType(EntityTypes.SPLASH_POTION, Items.SPLASH_POTION);
        registerItemForEntityType(EntityTypes.LINGERING_POTION, Items.LINGERING_POTION);
        registerItemForEntityType(EntityTypes.SNOWBALL, Items.SNOWBALL);
        registerItemForEntityType(EntityTypes.TNT, Items.TNT);
        registerItemForEntityType(EntityTypes.TRIDENT, Items.TRIDENT);
        registerItemForEntityType(EntityTypes.WIND_CHARGE, Items.WIND_CHARGE);
        registerItemForEntityType(EntityTypes.WITHER_SKULL, Items.WITHER_SKELETON_SKULL);
    }
}
