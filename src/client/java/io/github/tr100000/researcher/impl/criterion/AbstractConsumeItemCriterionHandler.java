package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.CriterionDisplay;
import io.github.tr100000.researcher.api.criterion.CriterionDisplayElement;
import io.github.tr100000.researcher.api.criterion.CriterionHandler;
import io.github.tr100000.researcher.impl.criterion.element.ItemElement;
import io.github.tr100000.researcher.impl.criterion.element.SpacingElement;
import io.github.tr100000.researcher.impl.criterion.element.TextElement;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.item.Item;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public abstract class AbstractConsumeItemCriterionHandler<T extends CriterionConditions> implements CriterionHandler<T> {
    @Override
    public CriterionDisplayElement prepare(ResearchCriterion<T> criterion) {
        Optional<Item> item = getItem(criterion.conditions());
        if (item.isEmpty()) {
            return ErrorCriterionHandler.ERROR.prepareWithErrorTooltip(Text.literal("Invalid unlock conditions!"));
        }

        List<CriterionDisplayElement> elements = new ObjectArrayList<>();
        Text[] text = getText(criterion, item.get());

        elements.add(new SpacingElement(2));
        elements.add(new TextElement(text[0]));
        elements.add(new SpacingElement(2));
        elements.add(new ItemElement(item.get().getDefaultStack(), true));
        elements.add(new SpacingElement(2));
        elements.add(new TextElement(text[1]));
        elements.add(new TextElement(text[2]));

        return new CriterionDisplay(elements);
    }

    private Text[] getText(ResearchCriterion<T> criterion, Item item) {
        String textBeforeKey;
        String textAfterKey;
        String consumeKey;
        if (ModUtils.isEdibleItem(item)) {
            textBeforeKey = ModUtils.getScreenTranslationKey("criterion.consume.food.before");
            textAfterKey = ModUtils.getScreenTranslationKey("criterion.consume.food.after");
            consumeKey = ModUtils.getScreenTranslationKey("criterion.consume.food.count");
        }
        else if (ModUtils.isDrinkableItem(item)) {
            textBeforeKey = ModUtils.getScreenTranslationKey("criterion.consume.drink.before");
            textAfterKey = ModUtils.getScreenTranslationKey("criterion.consume.drink.after");
            consumeKey = ModUtils.getScreenTranslationKey("criterion.consume.drink.count");
        }
        else {
            textBeforeKey = ModUtils.getScreenTranslationKey("criterion.consume.before");
            textAfterKey = ModUtils.getScreenTranslationKey("criterion.consume.after");
            consumeKey = ModUtils.getScreenTranslationKey("criterion.consume.count");
        }
        Text textBefore = Text.translatable(textBeforeKey);
        Text textAfter = Text.translatable(textAfterKey);
        Text consumeText = Text.translatable(consumeKey, criterion.count(), item.getName().getString());

        return new Text[] { textBefore, consumeText, textAfter };
    }

    public abstract Optional<Item> getItem(T conditions);
}
