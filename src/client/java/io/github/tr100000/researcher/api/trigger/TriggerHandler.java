package io.github.tr100000.researcher.api.trigger;

import io.github.tr100000.researcher.ResearchCriterion;
import net.minecraft.advancements.CriterionTriggerInstance;

public interface TriggerHandler<T extends CriterionTriggerInstance> {
    TriggerDisplayElement prepare(ResearchCriterion<T> criterion);
}
