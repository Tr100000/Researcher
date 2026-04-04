package io.github.tr100000.researcher.api.data;

import io.github.tr100000.researcher.Research;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.api.ResearchReward;
import io.github.tr100000.trutils.api.gui.Icon;
import io.github.tr100000.trutils.api.gui.ItemIcon;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ResearchBuilder {
    private final Identifier id;
    private @Nullable Component titleText;
    private @Nullable Component descriptionText;
    private ResearchCriterion<?> trigger = ResearchCriterion.IMPOSSIBLE;
    private final List<Identifier> prerequisiteIds = new ObjectArrayList<>();
    private final List<Identifier> recipeUnlocks = new ObjectArrayList<>();
    private final List<ResearchReward> rewards = new ObjectArrayList<>();
    private Icon display = Research.DEFAULT_ICON;

    public ResearchBuilder(Identifier id) {
        this.id = id;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public ResearchBuilder title(Component titleText) {
        this.titleText = titleText;
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public ResearchBuilder description(Component descriptionText) {
        this.descriptionText = descriptionText;
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public ResearchBuilder toUnlock(ResearchCriterion<?> trigger) {
        this.trigger = trigger;
        return this;
    }

    @Contract(value = "_, _ -> this", mutates = "this")
    public ResearchBuilder toUnlock(Criterion<?> trigger, int count) {
        return toUnlock(new ResearchCriterion<>(trigger, count));
    }

    @Contract(value = "_ -> this", mutates = "this")
    public ResearchBuilder toUnlock(Criterion<?> trigger) {
        return toUnlock(trigger, 1);
    }

    @Contract(value = "_, _, _ -> this", mutates = "this")
    public <T extends CriterionTriggerInstance> ResearchBuilder toUnlock(CriterionTrigger<T> trigger, T conditions, int count) {
        return toUnlock(new Criterion<>(trigger, conditions), count);
    }

    @Contract(value = "_, _ -> this", mutates = "this")
    public <T extends CriterionTriggerInstance> ResearchBuilder toUnlock(CriterionTrigger<T> trigger, T conditions) {
        return toUnlock(new Criterion<>(trigger, conditions));
    }

    @Contract(value = "_ -> this", mutates = "this")
    public ResearchBuilder prerequisites(Collection<Identifier> prerequisiteIds) {
        this.prerequisiteIds.addAll(prerequisiteIds);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public ResearchBuilder prerequisites(Identifier... prerequisiteIds) {
        return prerequisites(List.of(prerequisiteIds));
    }

    @Contract(value = "_ -> this", mutates = "this")
    public ResearchBuilder recipeUnlocks(Collection<Identifier> recipeUnlocks) {
        this.recipeUnlocks.addAll(recipeUnlocks);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public ResearchBuilder recipeUnlocks(Identifier... recipeUnlocks) {
        return recipeUnlocks(List.of(recipeUnlocks));
    }

    public ResearchBuilder rewards(Collection<ResearchReward> rewards) {
        this.rewards.addAll(rewards);
        return this;
    }

    public ResearchBuilder rewards(ResearchReward... rewards) {
        return rewards(List.of(rewards));
    }

    @Contract(value = "_ -> this", mutates = "this")
    public ResearchBuilder display(Icon display) {
        this.display = display;
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public ResearchBuilder display(ItemLike item) {
        return display(new ItemIcon(item));
    }

    public Identifier export(ResearchExporter exporter) {
        exporter.accept(id, build());
        return id;
    }

    @Contract(value = "-> new", pure = true)
    public Research build() {
        return new Research(
                Optional.ofNullable(titleText),
                Optional.ofNullable(descriptionText),
                trigger,
                prerequisiteIds,
                recipeUnlocks,
                rewards,
                display
        );
    }
}
