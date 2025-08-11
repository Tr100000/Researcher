package io.github.tr100000.researcher.api;

import io.github.tr100000.researcher.ResearchCriterion;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.client.MinecraftClient;

public interface CriterionHandler<T extends CriterionConditions> {
    MinecraftClient client = MinecraftClient.getInstance();

    CriterionDisplay prepare(ResearchCriterion<T> criterion);
}
