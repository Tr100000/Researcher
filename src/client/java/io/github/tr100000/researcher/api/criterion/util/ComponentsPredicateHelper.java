package io.github.tr100000.researcher.api.criterion.util;

import com.mojang.serialization.JsonOps;
import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.Researcher;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import io.github.tr100000.trutils.api.item.ItemUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.component.ComponentType;
import net.minecraft.predicate.component.ComponentPredicate;
import net.minecraft.predicate.component.ComponentPredicateTypes;
import net.minecraft.predicate.component.ComponentsPredicate;
import net.minecraft.predicate.component.CustomDataPredicate;
import net.minecraft.predicate.item.AttributeModifiersPredicate;
import net.minecraft.predicate.item.BundleContentsPredicate;
import net.minecraft.predicate.item.ContainerPredicate;
import net.minecraft.predicate.item.DamagePredicate;
import net.minecraft.predicate.item.EnchantmentsPredicate;
import net.minecraft.predicate.item.FireworkExplosionPredicate;
import net.minecraft.predicate.item.FireworksPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.predicate.item.JukeboxPlayablePredicate;
import net.minecraft.predicate.item.PotionContentsPredicate;
import net.minecraft.predicate.item.TrimPredicate;
import net.minecraft.predicate.item.WritableBookContentPredicate;
import net.minecraft.predicate.item.WrittenBookContentPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

public final class ComponentsPredicateHelper {
    private ComponentsPredicateHelper() {}

    private static final Map<ComponentPredicate.Type<?>, BiConsumer<ComponentPredicate, IndentedTextHolder>> PREDICATE_HANDLER_REGISTRY = new Object2ObjectOpenHashMap<>();

    private static final Text COMPONENTS_HEADER = ModUtils.getScreenTranslated("predicate.components");
    private static final String EXACT_VALUE_KEY = ModUtils.getScreenTranslationKey("predicate.components.exact");

    private static final Text DAMAGE_HEADER = ModUtils.getScreenTranslated("predicate.components.damage");
    private static final Text DAMAGE_DURABILITY = ModUtils.getScreenTranslated("predicate.components.damage.durability");
    private static final Text DAMAGE_DAMAGE = ModUtils.getScreenTranslated("predicate.components.damage.damage");

    private static final Text ENCHANTMENTS_HEADER = ModUtils.getScreenTranslated("predicate.components.enchantments");
    private static final Text ENCHANTMENTS_LIST = ModUtils.getScreenTranslated("predicate.components.enchantments.list");
    private static final Text ENCHANTMENTS_LEVELS = ModUtils.getScreenTranslated("predicate.components.enchantments.levels");

    private static final Text POTION_CONTENTS_HEADER = ModUtils.getScreenTranslated("predicate.components.potion_contents");

    private static final Text CONTAINER_HEADER = ModUtils.getScreenTranslated("predicate.components.container");
    private static final Text CONTAINER_ITEM = ModUtils.getScreenTranslated("predicate.item");

    private static final Text BUNDLE_CONTENTS_HEADER = ModUtils.getScreenTranslated("predicate.components.bundle_contents");
    private static final Text BUNDLE_CONTENTS_ITEM = ModUtils.getScreenTranslated("predicate.item");

    private static final Text FIREWORK_EXPLOSION_HEADER = ModUtils.getScreenTranslated("predicate.components.firework_explosion");
    private static final String FIREWORK_EXPLOSION_SHAPE_KEY = ModUtils.getScreenTranslationKey("predicate.components.firework_explosion.shape");
    private static final Text FIREWORK_EXPLOSION_TWINKLE = ModUtils.getScreenTranslated("predicate.components.firework_explosion.twinkle");
    private static final Text FIREWORK_EXPLOSION_NO_TWINKLE = ModUtils.getScreenTranslated("predicate.components.firework_explosion.no_twinkle");
    private static final Text FIREWORK_EXPLOSION_TRAIL = ModUtils.getScreenTranslated("predicate.components.firework_explosion.trail");
    private static final Text FIREWORK_EXPLOSION_NO_TRAIL = ModUtils.getScreenTranslated("predicate.components.firework_explosion.no_trail");

    private static final Text FIREWORKS_HEADER = ModUtils.getScreenTranslated("predicate.components.fireworks");
    private static final Text FIREWORKS_FLIGHT_DURATION = ModUtils.getScreenTranslated("predicate.components.fireworks.flight_duration");

    private static final Text WRITABLE_BOOK_CONTENT_HEADER = ModUtils.getScreenTranslated("predicate.components.writable_book_content");

    private static final Text WRITTEN_BOOK_CONTENT_HEADER = ModUtils.getScreenTranslated("predicate.components.written_book_content");
    private static final String WRITTEN_BOOK_CONTENT_AUTHOR = ModUtils.getScreenTranslationKey("predicate.components.written_book_content.author");
    private static final String WRITTEN_BOOK_CONTENT_TITLE = ModUtils.getScreenTranslationKey("predicate.components.written_book_content.title");
    private static final Text WRITTEN_BOOK_CONTENT_GENERATION = ModUtils.getScreenTranslated("predicate.components.written_book_content.generation");
    private static final Text WRITTEN_BOOK_CONTENT_RESOLVED = ModUtils.getScreenTranslated("predicate.components.written_book_content.resolved");
    private static final Text WRITTEN_BOOK_CONTENT_NOT_RESOLVED = ModUtils.getScreenTranslated("predicate.components.written_book_content.not_resolved");

    private static final Text ATTRIBUTE_MODIFIERS_HEADER = ModUtils.getScreenTranslated("predicate.components.attribute_modifiers");
    private static final Text ATTRIBUTE_MODIFIER_LIST = ModUtils.getScreenTranslated("predicate.components.attribute_modifier.list");
    private static final String ATTRIBUTE_MODIFIER_ID_KEY = ModUtils.getScreenTranslationKey("predicate.components.attribute_modifier.id");
    private static final Text ATTRIBUTE_MODIFIER_AMOUNT = ModUtils.getScreenTranslated("predicate.components.attribute_modifier.amount");
    private static final String ATTRIBUTE_MODIFIER_OPERATION_KEY = ModUtils.getScreenTranslationKey("predicate.components.attribute_modifier.operation");
    private static final String ATTRIBUTE_MODIFIER_SLOT_KEY = ModUtils.getScreenTranslationKey("predicate.components.attribute_modifier.slot");

    private static final Text TRIM_MATERIAL_LIST = ModUtils.getScreenTranslated("predicate.components.trim.material");
    private static final Text TRIM_PATTERN_LIST = ModUtils.getScreenTranslated("predicate.components.trim.pattern");

    private static final Text JUKEBOX_PLAYABLE_LIST = ModUtils.getScreenTranslated("predicate.components.jukebox_playable.list");

    @SuppressWarnings("unchecked")
    public static void tooltip(ComponentsPredicate predicate, IndentedTextHolder textHolder) {
        textHolder.accept(COMPONENTS_HEADER);
        textHolder.push();

        predicate.exact().components.forEach(component -> {
            Identifier typeId = Registries.DATA_COMPONENT_TYPE.getId(component.type());
            if (component.type().getCodec() == null) {
                textHolder.accept(Text.literal(String.format("WARN: %s is not a persistent component and should not be used in a predicate!", typeId)));
                return;
            }

            String value = Researcher.GSON.toJson(((ComponentType<Object>)component.type()).getCodec().encodeStart(JsonOps.INSTANCE, component.value()));
            textHolder.accept(Text.translatable(EXACT_VALUE_KEY, typeId, value));
        });

        predicate.partial().forEach((type, partialPredicate) -> {
            if (PREDICATE_HANDLER_REGISTRY.containsKey(type)) {
                PREDICATE_HANDLER_REGISTRY.get(type).accept(partialPredicate, textHolder);
            }
            else {
                textHolder.accept(Text.literal(String.format("No handler for %s", Registries.DATA_COMPONENT_PREDICATE_TYPE.getId(type))));
            }
        });

        textHolder.pop();
    }

    private static void damageTooltip(DamagePredicate predicate, IndentedTextHolder textHolder) {
        NumberRangeUtils.tooltip(predicate.durability(), DAMAGE_DURABILITY, textHolder);
        NumberRangeUtils.tooltip(predicate.damage(), DAMAGE_DAMAGE, textHolder);
    }

    private static void enchantmentsTooltip(EnchantmentsPredicate predicate, IndentedTextHolder textHolder) {
        predicate.enchantments.forEach(enchantmentPredicate -> {
            PredicateHelper.tooltip(enchantmentPredicate, (p, t) -> {
                if (p.enchantments().isPresent() && p.enchantments().get().stream().findAny().isPresent()) {
                    t.accept(ENCHANTMENTS_LIST);
                    t.push();
                    p.enchantments().get().forEach(entry -> textHolder.accept(entry.value().description()));
                    t.pop();
                }
                NumberRangeUtils.tooltip(p.levels(), ENCHANTMENTS_LEVELS, t);
            }, ENCHANTMENTS_HEADER);
        });
    }

    private static void potionContentsTooltip(PotionContentsPredicate predicate, IndentedTextHolder textHolder) {
        predicate.potions().forEach(entry -> textHolder.accept(Text.translatable(ItemUtils.getPotionTranslationKey(entry))));
    }

    private static void customDataTooltip(CustomDataPredicate predicate, IndentedTextHolder textHolder) {
        PredicateHelper.nbtTooltip(predicate.value(), textHolder);
    }

    private static void containerTooltip(ContainerPredicate predicate, IndentedTextHolder textHolder) {
        if (predicate.items().isPresent()) {
            PredicateHelper.collectionTooltip(predicate.items().get(), ComponentsPredicateHelper::containerItemTooltip, textHolder);
        }
    }

    private static void containerItemTooltip(ItemPredicate predicate, IndentedTextHolder textHolder) {
        PredicateHelper.tooltip(predicate, ItemPredicateHelper::tooltip, CONTAINER_ITEM);
    }

    private static void bundleContentsTooltip(BundleContentsPredicate predicate, IndentedTextHolder textHolder) {
        if (predicate.items().isPresent()) {
            PredicateHelper.collectionTooltip(predicate.items().get(), ComponentsPredicateHelper::bundleItemTooltip, textHolder);
        }
    }

    private static void bundleItemTooltip(ItemPredicate predicate, IndentedTextHolder textHolder) {
        PredicateHelper.tooltip(predicate, ItemPredicateHelper::tooltip, BUNDLE_CONTENTS_ITEM);
    }

    private static void fireworkExplosionTooltip(FireworkExplosionPredicate predicate, IndentedTextHolder textHolder) {
        fireworkExplosionPredicateTooltip(predicate.predicate(), textHolder);
    }

    private static void fireworkExplosionPredicateTooltip(FireworkExplosionPredicate.Predicate predicate, IndentedTextHolder textHolder) {
        if (predicate.shape().isPresent()) {
            textHolder.accept(Text.translatable(FIREWORK_EXPLOSION_SHAPE_KEY, predicate.shape().get().getName().getString()));
        }
        PredicateHelper.optionalBooleanTooltip(predicate.twinkle(), FIREWORK_EXPLOSION_TWINKLE, FIREWORK_EXPLOSION_NO_TWINKLE, textHolder);
        PredicateHelper.optionalBooleanTooltip(predicate.trail(), FIREWORK_EXPLOSION_TRAIL, FIREWORK_EXPLOSION_NO_TRAIL, textHolder);

    }

    private static void fireworksTooltip(FireworksPredicate predicate, IndentedTextHolder textHolder) {
        NumberRangeUtils.tooltip(predicate.flightDuration(), FIREWORKS_FLIGHT_DURATION, textHolder);
        if (predicate.explosions().isPresent()) {
            PredicateHelper.collectionTooltip(predicate.explosions().get(), (p, t) -> {
                PredicateHelper.tooltip(p, ComponentsPredicateHelper::fireworkExplosionPredicateTooltip, FIREWORK_EXPLOSION_HEADER);
            }, textHolder);
        }
    }

    private static void writableBookContentTooltip(WritableBookContentPredicate predicate, IndentedTextHolder textHolder) {
        if (predicate.pages().isPresent()) {
            PredicateHelper.collectionTooltip(predicate.pages().get(), (p, t) -> t.accept(Text.literal(p.contents())), textHolder);
        }
    }

    private static void writtenBookContentTooltip(WrittenBookContentPredicate predicate, IndentedTextHolder textHolder) {
        if (predicate.pages().isPresent()) {
            PredicateHelper.collectionTooltip(predicate.pages().get(), (p, t) -> t.accept(p.contents()), textHolder);
        }
        if (predicate.author().isPresent()) {
            textHolder.accept(Text.translatable(WRITTEN_BOOK_CONTENT_AUTHOR, predicate.author()));
        }
        if (predicate.title().isPresent()) {
            textHolder.accept(Text.translatable(WRITTEN_BOOK_CONTENT_TITLE, predicate.title()));
        }
        NumberRangeUtils.tooltip(predicate.generation(), WRITTEN_BOOK_CONTENT_GENERATION, textHolder);
        PredicateHelper.optionalBooleanTooltip(predicate.resolved(), WRITTEN_BOOK_CONTENT_RESOLVED, WRITTEN_BOOK_CONTENT_NOT_RESOLVED, textHolder);
    }

    private static void attributeModifiersTooltip(AttributeModifiersPredicate predicate, IndentedTextHolder textHolder) {
        if (predicate.modifiers().isPresent()) {
            PredicateHelper.collectionTooltip(predicate.modifiers().get(), ComponentsPredicateHelper::attributeModifierTooltip, textHolder);
        }
    }

    private static void attributeModifierTooltip(AttributeModifiersPredicate.AttributeModifierPredicate predicate, IndentedTextHolder textHolder) {
        textHolder.accept(ATTRIBUTE_MODIFIERS_HEADER);
        textHolder.push();

        if (predicate.attribute().isPresent() && !predicate.attribute().get().stream().findAny().isPresent()) {
            textHolder.accept(ATTRIBUTE_MODIFIER_LIST);
            textHolder.push();
            predicate.attribute().get().forEach(entry -> textHolder.accept(Text.translatable(entry.value().getTranslationKey())));
            textHolder.pop();
        }
        if (predicate.id().isPresent()) {
            textHolder.accept(Text.translatable(ATTRIBUTE_MODIFIER_ID_KEY, predicate.id().get()));
        }
        NumberRangeUtils.tooltip(predicate.amount(), ATTRIBUTE_MODIFIER_AMOUNT, textHolder);
        if (predicate.operation().isPresent()) {
            textHolder.accept(Text.translatable(ATTRIBUTE_MODIFIER_OPERATION_KEY, predicate.operation().get().asString()));
        }
        if (predicate.slot().isPresent()) {
            textHolder.accept(Text.translatable(ATTRIBUTE_MODIFIER_SLOT_KEY, predicate.slot().get().asString()));
        }

        textHolder.pop();
    }

    private static void trimTooltip(TrimPredicate predicate, IndentedTextHolder textHolder) {
        if (predicate.material().isPresent() && predicate.material().get().stream().findAny().isPresent()) {
            textHolder.accept(TRIM_MATERIAL_LIST);
            textHolder.push();
            predicate.material().get().forEach(entry -> textHolder.accept(entry.value().description()));
            textHolder.pop();
        }
        if (predicate.pattern().isPresent() && predicate.pattern().get().stream().findAny().isPresent()) {
            textHolder.accept(TRIM_PATTERN_LIST);
            textHolder.push();
            predicate.pattern().get().forEach(entry -> textHolder.accept(entry.value().description()));
            textHolder.pop();
        }
    }

    private static void jukeboxPlayableTooltip(JukeboxPlayablePredicate predicate, IndentedTextHolder textHolder) {
        if (predicate.song().isPresent() && predicate.song().get().stream().findAny().isPresent()) {
            textHolder.accept(JUKEBOX_PLAYABLE_LIST);
            textHolder.push();
            predicate.song().get().forEach(entry -> textHolder.accept(entry.value().description()));
            textHolder.pop();
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T extends ComponentPredicate> void registerHandler(ComponentPredicate.Type<T> type, BiConsumer<T, IndentedTextHolder> handler, @Nullable Text headerText) {
        Objects.requireNonNull(type, "type is null");
        Objects.requireNonNull(handler, "handler is null");
        if (headerText != null) {
            PREDICATE_HANDLER_REGISTRY.put(type, (p, t) -> PredicateHelper.tooltip(p, (BiConsumer)handler, headerText));
        }
        else {
            PREDICATE_HANDLER_REGISTRY.put(type, (BiConsumer)handler);
        }
    }

    public static void printNonRegistered() {
        Registries.DATA_COMPONENT_PREDICATE_TYPE.forEach(type -> {
            if (!PREDICATE_HANDLER_REGISTRY.containsKey(type)) {
                Researcher.LOGGER.warn("Data component predicate {} does not have a registered handler", Registries.DATA_COMPONENT_PREDICATE_TYPE.getId(type));
            }
        });
    }

    static {
        registerHandler(ComponentPredicateTypes.DAMAGE, ComponentsPredicateHelper::damageTooltip, DAMAGE_HEADER);
        registerHandler(ComponentPredicateTypes.ENCHANTMENTS, ComponentsPredicateHelper::enchantmentsTooltip, null);
        registerHandler(ComponentPredicateTypes.STORED_ENCHANTMENTS, ComponentsPredicateHelper::enchantmentsTooltip, null);
        registerHandler(ComponentPredicateTypes.POTION_CONTENTS, ComponentsPredicateHelper::potionContentsTooltip, POTION_CONTENTS_HEADER);
        registerHandler(ComponentPredicateTypes.CUSTOM_DATA, ComponentsPredicateHelper::customDataTooltip, null);
        registerHandler(ComponentPredicateTypes.CONTAINER, ComponentsPredicateHelper::containerTooltip, CONTAINER_HEADER);
        registerHandler(ComponentPredicateTypes.BUNDLE_CONTENTS, ComponentsPredicateHelper::bundleContentsTooltip, BUNDLE_CONTENTS_HEADER);
        registerHandler(ComponentPredicateTypes.FIREWORK_EXPLOSION, ComponentsPredicateHelper::fireworkExplosionTooltip, FIREWORK_EXPLOSION_HEADER);
        registerHandler(ComponentPredicateTypes.FIREWORKS, ComponentsPredicateHelper::fireworksTooltip, FIREWORKS_HEADER);
        registerHandler(ComponentPredicateTypes.WRITABLE_BOOK_CONTENT, ComponentsPredicateHelper::writableBookContentTooltip, WRITABLE_BOOK_CONTENT_HEADER);
        registerHandler(ComponentPredicateTypes.WRITTEN_BOOK_CONTENT, ComponentsPredicateHelper::writtenBookContentTooltip, WRITTEN_BOOK_CONTENT_HEADER);
        registerHandler(ComponentPredicateTypes.ATTRIBUTE_MODIFIERS, ComponentsPredicateHelper::attributeModifiersTooltip, ATTRIBUTE_MODIFIERS_HEADER);
        registerHandler(ComponentPredicateTypes.TRIM, ComponentsPredicateHelper::trimTooltip, null);
        registerHandler(ComponentPredicateTypes.JUKEBOX_PLAYABLE, ComponentsPredicateHelper::jukeboxPlayableTooltip, null);
    }
}
