package io.github.tr100000.researcher;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.consume.UseAction;
import net.minecraft.potion.Potion;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

// TODO move some of these to TrUtils
public final class ModUtils {
    private ModUtils() {}

    public static Identifier id(String path) {
        return Identifier.of(Researcher.MODID, path);
    }

    public static String getTranslationKey(String group, String key) {
        return String.format("%s.%s.%s", group, Researcher.MODID, key);
    }

    public static MutableText getTranslated(String group, String key) {
        return Text.translatable(getTranslationKey(group, key));
    }

    public static MutableText getTranslated(String group, String key, Object... args) {
        return Text.translatable(getTranslationKey(group, key), args);
    }

    public static String getScreenTranslationKey(String key) {
        return getTranslationKey("screen", key);
    }

    public static MutableText getScreenTranslated(String key) {
        return Text.translatable(getScreenTranslationKey(key));
    }

    public static MutableText getScreenTranslated(String key, Object... args) {
        return Text.translatable(getScreenTranslationKey(key), args);
    }

    private static boolean itemHasConsumableAction(Item item, UseAction action) {
        return item.getComponents().get(DataComponentTypes.CONSUMABLE) != null
                && item.getComponents().get(DataComponentTypes.CONSUMABLE).useAction() == action;
    }

    public static boolean isEdibleItem(Item item) {
        return item.getComponents().get(DataComponentTypes.FOOD) != null
                || itemHasConsumableAction(item, UseAction.EAT);
    }

    public static boolean isDrinkableItem(Item item) {
        return itemHasConsumableAction(item, UseAction.DRINK);
    }

    public static String getPotionTranslationKey(RegistryEntry<Potion> potion) {
        String id = potion.value().getBaseName();
        if (id == null) id = "empty";
        return String.format("item.minecraft.potion.effect.%s", id);
    }

    public static ItemStack getPotionStack(RegistryEntry<Potion> potion) {
        ItemStack stack = Items.POTION.getDefaultStack();
        stack.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(potion));
        return stack;
    }
}
