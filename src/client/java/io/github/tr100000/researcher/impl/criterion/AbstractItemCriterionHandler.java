package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.CriterionDisplay;
import io.github.tr100000.researcher.api.criterion.CriterionDisplayElement;
import io.github.tr100000.researcher.api.criterion.CriterionHandler;
import io.github.tr100000.researcher.impl.criterion.element.ItemElement;
import io.github.tr100000.researcher.impl.criterion.element.SpacingElement;
import io.github.tr100000.researcher.impl.criterion.element.TextElement;
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
    public CriterionDisplayElement prepare(ResearchCriterion<T> criterion) {
        ItemStack stack = getItem(criterion).getDefaultStack();

        return new CriterionDisplay(
                new SpacingElement(2),
                new TextElement(Text.translatable(textBeforeKey)),
                new SpacingElement(2),
                new ItemElement(stack, true),
                new SpacingElement(2),
                new TextElement(Text.translatable(countTextKey, criterion.count(), stack.getName().getString())),
                new TextElement(Text.translatable(textAfterKey))
        );
    }

    public abstract Item getItem(ResearchCriterion<T> criterion);
}
