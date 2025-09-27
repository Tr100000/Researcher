package io.github.tr100000.researcher.api;

import io.github.tr100000.researcher.ResearchCriterion;
import net.minecraft.advancement.criterion.CriterionConditions;

public interface CriterionHandler<T extends CriterionConditions> {
    CriterionDisplayElement prepare(ResearchCriterion<T> criterion);
}
