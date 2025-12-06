package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.CriterionDisplay;
import io.github.tr100000.researcher.api.criterion.CriterionDisplayElement;
import io.github.tr100000.researcher.api.criterion.CriterionHandler;
import io.github.tr100000.researcher.api.criterion.element.ItemElement;
import io.github.tr100000.researcher.api.criterion.element.SpacingElement;
import io.github.tr100000.researcher.api.criterion.element.TextElement;
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

    protected AbstractItemCriterionHandler(String textBeforeKey, String textAfterKey) {
        this.textBeforeKey = textBeforeKey;
        this.textAfterKey = textAfterKey;
    }

    @Override
    public CriterionDisplayElement prepare(ResearchCriterion<T> criterion) {
        ItemStack stack = getItem(criterion).getDefaultStack();

        return new CriterionDisplay(
                CriterionDisplay.makeCountElement(criterion),
                new TextElement(stack.getName()),
                new TextElement(Text.translatable(textBeforeKey)),
                new SpacingElement(2),
                new ItemElement(stack, true),
                new SpacingElement(2),
                new TextElement(Text.translatable(textAfterKey))
        );
    }

    public abstract Item getItem(ResearchCriterion<T> criterion);
}
