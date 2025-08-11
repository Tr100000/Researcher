package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.CriterionDisplay;
import io.github.tr100000.researcher.api.CriterionDisplayElement;
import io.github.tr100000.researcher.api.CriterionHandler;
import io.github.tr100000.researcher.criteria.ResearchItemsCriterion;
import io.github.tr100000.researcher.impl.criterion.element.ItemCriterionDisplayElement;
import io.github.tr100000.researcher.impl.criterion.element.SpacingCriterionDisplayElement;
import io.github.tr100000.researcher.impl.criterion.element.TextCriterionDisplayElement;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;

@Environment(EnvType.CLIENT)
public class ResearchItemsCriterionHandler implements CriterionHandler<ResearchItemsCriterion.Conditions> {
    @Override
    public CriterionDisplay prepare(ResearchCriterion<ResearchItemsCriterion.Conditions> criterion) {
        List<CriterionDisplayElement> elements = new ObjectArrayList<>();

        ResearchItemsCriterion.Conditions conditions = criterion.conditions();
        for (int i = 0; i < conditions.items().size(); i++) {
            ItemStack stack = conditions.items().get(i).getDefaultStack();
            elements.add(new ItemCriterionDisplayElement(stack, true));
            elements.add(new SpacingCriterionDisplayElement(2));
        }

        Text text = getText(criterion);
        elements.add(new TextCriterionDisplayElement(text));
        elements.add(new SpacingCriterionDisplayElement(2));

        return new CriterionDisplay(elements);
    }

    private Text getText(ResearchCriterion<ResearchItemsCriterion.Conditions> criterion) {
        return Text.literal(String.format("%ds × %d", criterion.conditions().time(), criterion.count()));
    }
}
