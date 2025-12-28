package io.github.tr100000.researcher.api.data;

import io.github.tr100000.researcher.Research;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.trutils.api.gui.Icon;
import io.github.tr100000.trutils.api.gui.ItemIcon;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class ResearchBuilder {
    private final Identifier id;
    private @Nullable Component titleText;
    private @Nullable Component descriptionText;
    private ResearchCriterion<?> trigger = ResearchCriterion.IMPOSSIBLE;
    private final List<Identifier> prerequisiteIds = new ObjectArrayList<>();
    private final List<Identifier> recipeUnlocks = new ObjectArrayList<>();
    private Icon display = Research.DEFAULT_ICON;

    public ResearchBuilder(Identifier id) {
        this.id = id;
    }

    public ResearchBuilder title(Component titleText) {
        this.titleText = titleText;
        return this;
    }

    public ResearchBuilder description(Component descriptionText) {
        this.descriptionText = descriptionText;
        return this;
    }

    public ResearchBuilder toUnlock(ResearchCriterion<?> trigger) {
        this.trigger = trigger;
        return this;
    }

    public ResearchBuilder toUnlock(Criterion<?> trigger, int count) {
        return toUnlock(new ResearchCriterion<>(trigger, count));
    }

    public ResearchBuilder toUnlock(Criterion<?> trigger) {
        return toUnlock(trigger, 1);
    }

    public <T extends CriterionTriggerInstance> ResearchBuilder toUnlock(CriterionTrigger<T> trigger, T conditions, int count) {
        return toUnlock(new Criterion<>(trigger, conditions), count);
    }

    public <T extends CriterionTriggerInstance> ResearchBuilder toUnlock(CriterionTrigger<T> trigger, T conditions) {
        return toUnlock(new Criterion<>(trigger, conditions));
    }

    public ResearchBuilder prerequisites(List<Identifier> prerequisiteIds) {
        this.prerequisiteIds.addAll(prerequisiteIds);
        return this;
    }

    public ResearchBuilder prerequisites(Identifier... prerequisiteIds) {
        return prerequisites(List.of(prerequisiteIds));
    }

    public ResearchBuilder recipeUnlocks(List<Identifier> recipeUnlocks) {
        this.recipeUnlocks.addAll(recipeUnlocks);
        return this;
    }

    public ResearchBuilder recipeUnlocks(Identifier... recipeUnlocks) {
        return recipeUnlocks(List.of(recipeUnlocks));
    }

    public ResearchBuilder display(Icon display) {
        this.display = display;
        return this;
    }

    public ResearchBuilder display(ItemLike item) {
        return display(new ItemIcon(item));
    }

    public Identifier export(ResearchExporter exporter) {
        exporter.accept(id, build());
        return id;
    }

    public Research build() {
        return new Research(Optional.ofNullable(titleText), Optional.ofNullable(descriptionText), trigger, prerequisiteIds, recipeUnlocks, display);
    }
}
