package io.github.tr100000.researcher.api.data;

import io.github.tr100000.researcher.Research;
import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.trutils.api.gui.IconRenderer;
import io.github.tr100000.trutils.api.gui.ItemIconRenderer;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.item.ItemConvertible;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Optional;

public class ResearchBuilder {
    public final Identifier id;
    private Text titleText;
    private Text descriptionText;
    private ResearchCriterion<?> trigger = ResearchCriterion.IMPOSSIBLE;
    private final List<Identifier> prerequisiteIds = new ObjectArrayList<>();
    private final List<Identifier> recipeUnlocks = new ObjectArrayList<>();
    private IconRenderer display = Research.DEFAULT_ICON;

    public ResearchBuilder(Identifier id) {
        this.id = id;
    }

    public ResearchBuilder title(Text titleText) {
        this.titleText = titleText;
        return this;
    }

    public ResearchBuilder description(Text descriptionText) {
        this.descriptionText = descriptionText;
        return this;
    }

    public ResearchBuilder trigger(ResearchCriterion<?> trigger) {
        this.trigger = trigger;
        return this;
    }

    public ResearchBuilder trigger(AdvancementCriterion<?> trigger, int count) {
        return trigger(new ResearchCriterion<>(trigger, count));
    }

    public ResearchBuilder trigger(AdvancementCriterion<?> trigger) {
        return trigger(trigger, 1);
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

    public ResearchBuilder display(IconRenderer display) {
        this.display = display;
        return this;
    }

    public ResearchBuilder display(ItemConvertible item) {
        return display(ItemIconRenderer.of(item));
    }

    public Identifier export(ResearchExporter exporter) {
        exporter.accept(id, build());
        return id;
    }

    public Research build() {
        return new Research(Optional.ofNullable(titleText), Optional.ofNullable(descriptionText), trigger, prerequisiteIds, recipeUnlocks, display);
    }
}
