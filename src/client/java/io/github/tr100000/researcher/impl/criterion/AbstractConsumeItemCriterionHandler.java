package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.CriterionDisplay;
import io.github.tr100000.researcher.api.CriterionDisplayElement;
import io.github.tr100000.researcher.api.CriterionHandler;
import io.github.tr100000.researcher.impl.criterion.element.ItemCriterionDisplayElement;
import io.github.tr100000.researcher.impl.criterion.element.SpacingCriterionDisplayElement;
import io.github.tr100000.researcher.impl.criterion.element.TextCriterionDisplayElement;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public abstract class AbstractConsumeItemCriterionHandler<T extends CriterionConditions> implements CriterionHandler<T> {
    @Override
    public CriterionDisplay prepare(ResearchCriterion<T> criterion) {
        Optional<Item> item = getItem(criterion.conditions());
        if (item.isEmpty()) {
            return ErrorCriterionHandler.ERROR.prepareWithErrorTooltip(Text.literal("Invalid unlock conditions!"));
        }

        List<CriterionDisplayElement> elements = new ObjectArrayList<>();
        Text[] text = getText(criterion, item.get());

        elements.add(new SpacingCriterionDisplayElement(2));
        elements.add(new TextCriterionDisplayElement(text[0]));
        elements.add(new SpacingCriterionDisplayElement(2));
        elements.add(new ItemCriterionDisplayElement(item.get().getDefaultStack(), true));
        elements.add(new SpacingCriterionDisplayElement(2));
        elements.add(new TextCriterionDisplayElement(text[1]));
        elements.add(new TextCriterionDisplayElement(text[2]));

        return new CriterionDisplay(elements);
    }

    private Text[] getText(ResearchCriterion<T> criterion, Item item) {
        String textBeforeKey;
        String textAfterKey;
        String consumeKey;
        if (item.getComponents().get(DataComponentTypes.FOOD) != null) {
            textBeforeKey = "research.criterion.consume.food.before";
            textAfterKey = "research.criterion.consume.food.after";
            consumeKey = "research.criterion.consume.food.count";
        }
        else {
            textBeforeKey = "research.criterion.consume.before";
            textAfterKey = "research.criterion.consume.after";
            consumeKey = "research.criterion.consume.count";
        }
        Text textBefore = Text.translatable(textBeforeKey);
        Text textAfter = Text.translatable(textAfterKey);
        Text consumeText = Text.translatable(consumeKey, criterion.count(), item.getName().getString());

        return new Text[] { textBefore, consumeText, textAfter };
    }

    public abstract Optional<Item> getItem(T conditions);
}
