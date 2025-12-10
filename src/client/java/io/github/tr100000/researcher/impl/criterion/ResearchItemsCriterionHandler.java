package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.criterion.CriterionDisplay;
import io.github.tr100000.researcher.api.criterion.CriterionDisplayElement;
import io.github.tr100000.researcher.api.criterion.CriterionHandler;
import io.github.tr100000.researcher.api.criterion.element.ItemElement;
import io.github.tr100000.researcher.api.criterion.element.SpacingElement;
import io.github.tr100000.researcher.api.criterion.element.TextElement;
import io.github.tr100000.researcher.criteria.ResearchItemsCriterion;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;

public class ResearchItemsCriterionHandler implements CriterionHandler<ResearchItemsCriterion.Conditions> {
    @Override
    public CriterionDisplayElement prepare(ResearchCriterion<ResearchItemsCriterion.Conditions> criterion) {
        List<CriterionDisplayElement> elements = new ObjectArrayList<>();

        ResearchItemsCriterion.Conditions conditions = criterion.conditions();
        for (int i = 0; i < conditions.items().size(); i++) {
            ItemStack stack = conditions.items().get(i).getDefaultStack();
            elements.add(new ItemElement(stack, true));
            elements.add(new SpacingElement(2));
        }

        Text text = getText(criterion);
        elements.add(new TextElement(text));
        elements.add(new SpacingElement(2));

        return new CriterionDisplay(elements);
    }

    private Text getText(ResearchCriterion<ResearchItemsCriterion.Conditions> criterion) {
        return Text.literal(String.format("%ds × %d", criterion.conditions().time(), criterion.count()));
    }
}
