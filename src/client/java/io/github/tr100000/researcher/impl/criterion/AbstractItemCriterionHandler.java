package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.CriterionDisplay;
import io.github.tr100000.researcher.api.CriterionHandler;
import io.github.tr100000.researcher.impl.criterion.element.ItemCriterionDisplayElement;
import io.github.tr100000.researcher.impl.criterion.element.SpacingCriterionDisplayElement;
import io.github.tr100000.researcher.impl.criterion.element.TextCriterionDisplayElement;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public abstract class AbstractItemCriterionHandler<T extends CriterionConditions> implements CriterionHandler<T> {
    private final String textBeforeKey;
    private final String textAfterKey;
    private final String countTextKey;

    protected AbstractItemCriterionHandler(String textBeforeKey, String textAfterKey, String countTextKey) {
        this.textBeforeKey = textBeforeKey;
        this.textAfterKey = textAfterKey;
        this.countTextKey = countTextKey;
    }

    @Override
    public CriterionDisplay prepare(ResearchCriterion<T> criterion) {
        ItemStack stack = getItem(criterion).getDefaultStack();

        return new CriterionDisplay(
                new SpacingCriterionDisplayElement(2),
                new TextCriterionDisplayElement(Text.translatable(textBeforeKey)),
                new SpacingCriterionDisplayElement(2),
                new ItemCriterionDisplayElement(stack, true),
                new SpacingCriterionDisplayElement(2),
                new TextCriterionDisplayElement(Text.translatable(countTextKey, criterion.count(), stack.getName().getString())),
                new TextCriterionDisplayElement(Text.translatable(textAfterKey))
        );
    }

    public abstract Item getItem(ResearchCriterion<T> criterion);
}
