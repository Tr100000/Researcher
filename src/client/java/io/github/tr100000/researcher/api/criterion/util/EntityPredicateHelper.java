package io.github.tr100000.researcher.api.criterion.util;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.Researcher;
import io.github.tr100000.researcher.api.criterion.CriterionDisplayElement;
import io.github.tr100000.researcher.api.criterion.element.GroupedElement;
import io.github.tr100000.researcher.api.criterion.element.ItemElement;
import io.github.tr100000.researcher.api.criterion.element.TextElement;
import io.github.tr100000.researcher.api.criterion.element.TimedSwitchingElement;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.TypedEntityData;
import net.minecraft.item.BoatItem;
import net.minecraft.item.DecorationItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.MinecartItem;
import net.minecraft.loot.condition.EntityPropertiesLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.EntityEffectPredicate;
import net.minecraft.predicate.entity.EntityEquipmentPredicate;
import net.minecraft.predicate.entity.EntityFlagsPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.EntitySubPredicate;
import net.minecraft.predicate.entity.FishingHookPredicate;
import net.minecraft.predicate.entity.InputPredicate;
import net.minecraft.predicate.entity.LightningBoltPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.entity.PlayerPredicate;
import net.minecraft.predicate.entity.RaiderPredicate;
import net.minecraft.predicate.entity.SheepPredicate;
import net.minecraft.predicate.entity.SlimePredicate;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameModeList;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class EntityPredicateHelper {
    private EntityPredicateHelper() {}

    private static final Map<EntityType<?>, Item> ENTITY_TYPE_ITEMS = new Object2ObjectOpenHashMap<>();

    private static final Text PREDICATE_MISSING = ModUtils.getScreenTranslated("predicate.entity.properties_missing");
    private static final Text ANY_ENTITY = ModUtils.getScreenTranslated("predicate.entity.any");

    private static final Text ENTITY_TYPE = ModUtils.getScreenTranslated("predicate.entity.type");
    private static final Text ENTITY_LOCATED = ModUtils.getScreenTranslated("predicate.entity.location");
    private static final Text ENTITY_STEPPING_ON = ModUtils.getScreenTranslated("predicate.entity.stepping_on");
    private static final Text ENTITY_MOVEMENT_AFFECTED_BY = ModUtils.getScreenTranslated("predicate.entity.movement_affected_by");
    private static final Text ENTITY_EFFECTS_HEADER = ModUtils.getScreenTranslated("predicate.entity.effects");
    private static final String ENTITY_PERIODIC_KEY = ModUtils.getScreenTranslationKey("predicate.entity.periodic");
    private static final Text ENTITY_VEHICLE = ModUtils.getScreenTranslated("predicate.entity.vehicle");
    private static final Text ENTITY_PASSENGER = ModUtils.getScreenTranslated("predicate.entity.passenger");
    private static final Text ENTITY_TARGETED = ModUtils.getScreenTranslated("predicate.entity.targeted");
    private static final String ENTITY_TEAM_KEY = ModUtils.getScreenTranslationKey("predicate.entity.team");

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
    private static final Text FLAGS_IN_WATER = ModUtils.getScreenTranslated("predicate.entity.flags.in_water");
    private static final Text FLAGS_NOT_IN_WATER = ModUtils.getScreenTranslated("predicate.entity.flags.not_in_water");
    private static final Text FLAGS_FALL_FLYING = ModUtils.getScreenTranslated("predicate.entity.flags.fall_flying");
    private static final Text FLAGS_NOT_FALL_FLYING = ModUtils.getScreenTranslated("predicate.entity.flags.not_fall_flying");

    private static final Text EQUIPMENT_HEAD = ModUtils.getScreenTranslated("predicate.entity.equipment.head");
    private static final Text EQUIPMENT_CHEST = ModUtils.getScreenTranslated("predicate.entity.equipment.chest");
    private static final Text EQUIPMENT_LEGS = ModUtils.getScreenTranslated("predicate.entity.equipment.legs");
    private static final Text EQUIPMENT_FEET = ModUtils.getScreenTranslated("predicate.entity.equipment.feet");
    private static final Text EQUIPMENT_BODY = ModUtils.getScreenTranslated("predicate.entity.equipment.body");
    private static final Text EQUIPMENT_MAINHAND = ModUtils.getScreenTranslated("predicate.entity.equipment.mainhand");
    private static final Text EQUIPMENT_OFFHAND = ModUtils.getScreenTranslated("predicate.entity.equipment.offhand");

    private static final Text LIGHTNING_BLOCKS_SET_ON_FIRE = ModUtils.getScreenTranslated("predicate.entity.lightning.blocks_set_on_fire");
    private static final Text LIGHTNING_ENTITY_STRUCK = ModUtils.getScreenTranslated("predicate.entity.lightning.entity_struck");
    private static final Text FISHING_OPEN_WATER = ModUtils.getScreenTranslated("predicate.entity.fishing.open_water");
    private static final Text FISHING_NOT_OPEN_WATER = ModUtils.getScreenTranslated("predicate.entity.fishing.not_open_water");
    private static final Text PLAYER_EXPERIENCE_LEVEL = ModUtils.getScreenTranslated("predicate.entity.player.xp_level");
    private static final Text PLAYER_GAME_MODE = ModUtils.getScreenTranslated("predicate.entity.player.game_mode");
    private static final Text PLAYER_STATS = ModUtils.getScreenTranslated("predicate.entity.player.stats");
    private static final String PLAYER_HAS_RECIPE = ModUtils.getScreenTranslationKey("predicate.entity.player.has_recipe");
    private static final String PLAYER_DOESNT_HAVE_RECIPE = ModUtils.getScreenTranslationKey("predicate.entity.player.doesnt_have_recipe");
    private static final String PLAYER_ADVANCEMENT = ModUtils.getScreenTranslationKey("predicate.entity.player.advancement");
    private static final String PLAYER_ADVANCEMENT_CRITERIA_DONE = ModUtils.getScreenTranslationKey("predicate.entity.player.advancement.criteria.done");
    private static final String PLAYER_ADVANCEMENT_CRITERIA_NOT_DONE = ModUtils.getScreenTranslationKey("predicate.entity.player.advancement.criteria.not_done");
    private static final Text PLAYER_ADVANCEMENT_DONE = ModUtils.getScreenTranslated("predicate.entity.player.advancement.done");
    private static final Text PLAYER_ADVANCEMENT_NOT_DONE = ModUtils.getScreenTranslated("predicate.entity.player.advancement.not_done");
    private static final Text PLAYER_LOOKING_AT = ModUtils.getScreenTranslated("predicate.entity.player.looking_at");
    private static final Text PLAYER_INPUT_FORWARD = ModUtils.getScreenTranslated("predicate.entity.player.input.forward");
    private static final Text PLAYER_INPUT_NOT_FORWARD = ModUtils.getScreenTranslated("predicate.entity.player.input.not_forward");
    private static final Text PLAYER_INPUT_BACKWARD = ModUtils.getScreenTranslated("predicate.entity.player.input.backward");
    private static final Text PLAYER_INPUT_NOT_BACKWARD = ModUtils.getScreenTranslated("predicate.entity.player.input.not_backward");
    private static final Text PLAYER_INPUT_LEFT = ModUtils.getScreenTranslated("predicate.entity.player.input.left");
    private static final Text PLAYER_INPUT_NOT_LEFT = ModUtils.getScreenTranslated("predicate.entity.player.input.not_left");
    private static final Text PLAYER_INPUT_RIGHT = ModUtils.getScreenTranslated("predicate.entity.player.input.right");
    private static final Text PLAYER_INPUT_NOT_RIGHT = ModUtils.getScreenTranslated("predicate.entity.player.input.not_right");
    private static final Text PLAYER_INPUT_JUMP = ModUtils.getScreenTranslated("predicate.entity.player.input.jump");
    private static final Text PLAYER_INPUT_NOT_JUMP = ModUtils.getScreenTranslated("predicate.entity.player.input.not_jump");
    private static final Text PLAYER_INPUT_SNEAK = ModUtils.getScreenTranslated("predicate.entity.player.input.sneak");
    private static final Text PLAYER_INPUT_NOT_SNEAK = ModUtils.getScreenTranslated("predicate.entity.player.input.not_sneak");
    private static final Text PLAYER_INPUT_SPRINT = ModUtils.getScreenTranslated("predicate.entity.player.input.sprint");
    private static final Text PLAYER_INPUT_NOT_SPRINT = ModUtils.getScreenTranslated("predicate.entity.player.input.not_sprint");
    private static final Text SLIME_SIZE = ModUtils.getScreenTranslated("predicate.entity.slime.size");
    private static final Text RAIDER_HAS_RAID = ModUtils.getScreenTranslated("predicate.entity.raider.has_raid");
    private static final Text RAIDER_DOESNT_HAVE_RAID = ModUtils.getScreenTranslated("predicate.entity.raider.doesnt_have_raid");
    private static final Text RAIDER_IS_CAPTAIN = ModUtils.getScreenTranslated("predicate.entity.raider.is_captain");
    private static final Text RAIDER_NOT_CAPTAIN = ModUtils.getScreenTranslated("predicate.entity.raider.not_captain");
    private static final Text SHEEP_SHEARED = ModUtils.getScreenTranslated("predicate.entity.sheep.sheared");
    private static final Text SHEEP_NOT_SHEARED = ModUtils.getScreenTranslated("predicate.entity.sheep.not_sheared");

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
        if (predicate.equipment().isPresent()) {
            equipmentTooltip(predicate.equipment().get(), textHolder);
        }
        if (predicate.typeSpecific().isPresent()) {
            typeSpecificTooltip(predicate.typeSpecific().get(), textHolder);
        }
        if (predicate.periodicTick().isPresent()) {
            textHolder.accept(Text.translatable(ENTITY_PERIODIC_KEY, predicate.periodicTick().get()));
        }
        if (predicate.vehicle().isPresent()) {
            textHolder.accept(ENTITY_VEHICLE);
            textHolder.push();
            tooltip(predicate.vehicle().get(), textHolder);
            textHolder.pop();
        }
        if (predicate.passenger().isPresent()) {
            textHolder.accept(ENTITY_PASSENGER);
            textHolder.push();
            tooltip(predicate.passenger().get(), textHolder);
            textHolder.pop();
        }
        if (predicate.targetedEntity().isPresent()) {
            textHolder.accept(ENTITY_TARGETED);
            textHolder.push();
            tooltip(predicate.targetedEntity().get(), textHolder);
            textHolder.pop();
        }
        if (predicate.team().isPresent()) {
            textHolder.accept(Text.translatable(ENTITY_TEAM_KEY, predicate.team().get()));
        }
        if (predicate.slots().isPresent()) {
            // TODO
            textHolder.accept(Text.literal("TODO slot conditions"));
        }
        if (!predicate.components().isEmpty()) {
            // TODO
            textHolder.accept(Text.literal("TODO component conditions"));
        }
    }

    public static void tooltip(@Nullable LootContextPredicate predicate, IndentedTextHolder textHolder) {
        Optional<EntityPredicate> entityPredicate = entityPredicateFromLootContextPredicate(predicate);
        entityPredicate.ifPresentOrElse(p -> tooltip(p, textHolder), () -> textHolder.accept(PREDICATE_MISSING));
    }

    public static Optional<List<MutableText>> tooltip(Optional<LootContextPredicate> predicate, @Nullable Text headerText) {
        return PredicateHelper.tooltip(predicate, EntityPredicateHelper::tooltip, headerText);
    }

    public static CriterionDisplayElement element(EntityType<?> entityType) {
        return new GroupedElement(
                new ItemElement(ENTITY_TYPE_ITEMS.getOrDefault(entityType, Items.BARRIER).getDefaultStack(), false),
                new TextElement(entityType.getName())
        );
    }

    public static CriterionDisplayElement element(@Nullable EntityPredicate predicate) {
        if (predicate != null && predicate.type().isPresent()) {
            List<CriterionDisplayElement> list = predicate.type().get().types().stream().map(RegistryEntry::value).map(EntityPredicateHelper::element).toList();
            if (list.size() == 1) {
                return list.getFirst();
            }
            else if (list.size() >= 2) {
                return new GroupedElement(new TimedSwitchingElement(list), new TextElement(Text.literal("*")));
            }
        }
        return new TextElement(ANY_ENTITY);
    }

    public static CriterionDisplayElement element(@Nullable LootContextPredicate predicate) {
        Optional<EntityPredicate> entityPredicate = entityPredicateFromLootContextPredicate(predicate);
        return entityPredicate.map(EntityPredicateHelper::element).orElseGet(() -> new TextElement(ANY_ENTITY));
    }

    public static CriterionDisplayElement vehicleElement(@Nullable LootContextPredicate predicate) {
        Optional<EntityPredicate> entityPredicate = entityPredicateFromLootContextPredicate(predicate);
        Optional<EntityPredicate> vehiclePredicate = entityPredicate.flatMap(EntityPredicate::vehicle);
        return element(vehiclePredicate.orElse(null));
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
        PredicateHelper.optionalBooleanTooltip(predicate.isInWater(), FLAGS_IN_WATER, FLAGS_NOT_IN_WATER, textHolder);
        PredicateHelper.optionalBooleanTooltip(predicate.isFallFlying(), FLAGS_FALL_FLYING, FLAGS_NOT_FALL_FLYING, textHolder);
    }

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

    public static void typeSpecificTooltip(EntitySubPredicate predicate, IndentedTextHolder textHolder) {
        if (predicate instanceof LightningBoltPredicate(
                NumberRange.IntRange blocksSetOnFire, Optional<EntityPredicate> entityStruck
        )) {
            NumberRangeUtils.tooltip(blocksSetOnFire, LIGHTNING_BLOCKS_SET_ON_FIRE, textHolder);
            if (entityStruck.isPresent()) {
                textHolder.accept(LIGHTNING_ENTITY_STRUCK);
                textHolder.push();
                tooltip(entityStruck.get(), textHolder);
                textHolder.pop();
            }
        }

        if (predicate instanceof FishingHookPredicate(
                Optional<Boolean> inOpenWater
        )) {
            PredicateHelper.optionalBooleanTooltip(inOpenWater, FISHING_OPEN_WATER, FISHING_NOT_OPEN_WATER, textHolder);
        }

        if (predicate instanceof PlayerPredicate(
                NumberRange.IntRange experienceLevel, GameModeList gameMode, List<PlayerPredicate.StatMatcher<?>> stats, Object2BooleanMap<RegistryKey<Recipe<?>>> recipes, Map<Identifier, PlayerPredicate.AdvancementPredicate> advancements, Optional<EntityPredicate> lookingAt, Optional<InputPredicate> input
        )) {
            NumberRangeUtils.tooltip(experienceLevel, PLAYER_EXPERIENCE_LEVEL, textHolder);
            if (gameMode != GameModeList.ALL) {
                textHolder.accept(PLAYER_GAME_MODE);
                textHolder.push();
                gameMode.gameModes().forEach(mode -> textHolder.accept(mode.getTranslatableName()));
                textHolder.pop();
            }
            if (!stats.isEmpty()) {
                textHolder.accept(PLAYER_STATS);
                textHolder.push();
                stats.forEach(matcher -> NumberRangeUtils.tooltip(matcher.range(), Text.translatable(matcher.value().getKey().orElseThrow().getValue().toTranslationKey("stat")), textHolder));
                textHolder.pop();
            }
            recipes.forEach((key, expected) -> {
                textHolder.accept(Text.translatable(expected ? PLAYER_HAS_RECIPE : PLAYER_DOESNT_HAVE_RECIPE, key.getValue().toString()));
            });
            if (!advancements.isEmpty()) {
                advancements.forEach((id, advPredicate) -> {
                    textHolder.accept(Text.translatable(PLAYER_ADVANCEMENT, id.toString()));
                    textHolder.push();

                    if (advPredicate instanceof PlayerPredicate.AdvancementCriteriaPredicate(Object2BooleanMap<String> criteriaMap)) {
                        criteriaMap.forEach((criteria, done) -> {
                            textHolder.accept(Text.translatable(done ? PLAYER_ADVANCEMENT_CRITERIA_DONE : PLAYER_ADVANCEMENT_CRITERIA_NOT_DONE, criteria));
                        });
                    }
                    else if (advPredicate instanceof PlayerPredicate.CompletedAdvancementPredicate(boolean done)) {
                        textHolder.accept(done ? PLAYER_ADVANCEMENT_DONE : PLAYER_ADVANCEMENT_NOT_DONE);
                    }

                    textHolder.pop();
                });
            }
            if (lookingAt.isPresent()) {
                textHolder.accept(PLAYER_LOOKING_AT);
                textHolder.push();
                tooltip(lookingAt.get(), textHolder);
                textHolder.pop();
            }
            if (input.isPresent()) {
                PredicateHelper.optionalBooleanTooltip(input.get().forward(), PLAYER_INPUT_FORWARD, PLAYER_INPUT_NOT_FORWARD, textHolder);
                PredicateHelper.optionalBooleanTooltip(input.get().backward(), PLAYER_INPUT_BACKWARD, PLAYER_INPUT_NOT_BACKWARD, textHolder);
                PredicateHelper.optionalBooleanTooltip(input.get().left(), PLAYER_INPUT_LEFT, PLAYER_INPUT_NOT_LEFT, textHolder);
                PredicateHelper.optionalBooleanTooltip(input.get().right(), PLAYER_INPUT_RIGHT, PLAYER_INPUT_NOT_RIGHT, textHolder);
                PredicateHelper.optionalBooleanTooltip(input.get().jump(), PLAYER_INPUT_JUMP, PLAYER_INPUT_NOT_JUMP, textHolder);
                PredicateHelper.optionalBooleanTooltip(input.get().sneak(), PLAYER_INPUT_SNEAK, PLAYER_INPUT_NOT_SNEAK, textHolder);
                PredicateHelper.optionalBooleanTooltip(input.get().sprint(), PLAYER_INPUT_SPRINT, PLAYER_INPUT_NOT_SPRINT, textHolder);
            }
        }

        if (predicate instanceof SlimePredicate(
                NumberRange.IntRange size
        )) {
            NumberRangeUtils.tooltip(size, SLIME_SIZE, textHolder);
        }

        if (predicate instanceof RaiderPredicate(
                boolean hasRaid, boolean isCaptain
        )) {
            textHolder.accept(hasRaid ? RAIDER_HAS_RAID : RAIDER_DOESNT_HAVE_RAID);
            textHolder.accept(isCaptain ? RAIDER_IS_CAPTAIN : RAIDER_NOT_CAPTAIN);
        }

        if (predicate instanceof SheepPredicate(
                Optional<Boolean> sheared
        )) {
            PredicateHelper.optionalBooleanTooltip(sheared, SHEEP_SHEARED, SHEEP_NOT_SHEARED, textHolder);
        }
    }

    public static void registerItemForEntityType(EntityType<?> entityType, Item item) {
        Objects.requireNonNull(entityType, "entityType is null");
        Objects.requireNonNull(item, "item is null");
        ENTITY_TYPE_ITEMS.put(entityType, item);
    }

    static {
        Registries.ITEM.forEach(item -> {
            if (item.getComponents().contains(DataComponentTypes.ENTITY_DATA)) {
                TypedEntityData<EntityType<?>> data = item.getComponents().get(DataComponentTypes.ENTITY_DATA);
                if (data != null && data.getType() != null) {
                    registerItemForEntityType(data.getType(), item);
                }
            }
            else if (item instanceof BoatItem boatItem) {
                registerItemForEntityType(boatItem.boatEntityType, item);
            }
            else if (item instanceof MinecartItem minecartItem) {
                registerItemForEntityType(minecartItem.type, item);
            }
            else if (item instanceof DecorationItem decorationItem) {
                registerItemForEntityType(decorationItem.entityType, item);
            }
        });

        registerItemForEntityType(EntityType.ARMOR_STAND, Items.ARMOR_STAND);
        registerItemForEntityType(EntityType.ARROW, Items.ARROW);
        registerItemForEntityType(EntityType.SPECTRAL_ARROW, Items.SPECTRAL_ARROW);
        registerItemForEntityType(EntityType.BREEZE_WIND_CHARGE, Items.WIND_CHARGE);
        registerItemForEntityType(EntityType.EGG, Items.EGG);
        registerItemForEntityType(EntityType.ENDER_PEARL, Items.ENDER_PEARL);
        registerItemForEntityType(EntityType.END_CRYSTAL, Items.END_CRYSTAL);
        registerItemForEntityType(EntityType.EXPERIENCE_BOTTLE, Items.EXPERIENCE_BOTTLE);
        registerItemForEntityType(EntityType.EXPERIENCE_ORB, Items.EXPERIENCE_BOTTLE);
        registerItemForEntityType(EntityType.EYE_OF_ENDER, Items.ENDER_EYE);
        registerItemForEntityType(EntityType.FIREBALL, Items.FIRE_CHARGE);
        registerItemForEntityType(EntityType.FIREWORK_ROCKET, Items.FIREWORK_ROCKET);
        registerItemForEntityType(EntityType.LEASH_KNOT, Items.LEAD);
        registerItemForEntityType(EntityType.SMALL_FIREBALL, Items.FIRE_CHARGE);
        registerItemForEntityType(EntityType.SPLASH_POTION, Items.SPLASH_POTION);
        registerItemForEntityType(EntityType.LINGERING_POTION, Items.LINGERING_POTION);
        registerItemForEntityType(EntityType.SNOWBALL, Items.SNOWBALL);
        registerItemForEntityType(EntityType.TNT, Items.TNT);
        registerItemForEntityType(EntityType.TRIDENT, Items.TRIDENT);
        registerItemForEntityType(EntityType.WIND_CHARGE, Items.WIND_CHARGE);

        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            Registries.ENTITY_TYPE.forEach(entityType -> {
                if (!ENTITY_TYPE_ITEMS.containsKey(entityType)) {
                    Researcher.LOGGER.warn("{} doesn't have a registered item", Registries.ENTITY_TYPE.getId(entityType));
                }
            });
        }
    }
}
