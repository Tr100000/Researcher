package io.github.tr100000.researcher.api.criterion.util;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.api.criterion.CriterionDisplayElement;
import io.github.tr100000.researcher.api.criterion.element.GroupedElement;
import io.github.tr100000.researcher.api.criterion.element.ItemElement;
import io.github.tr100000.researcher.api.criterion.element.TextElement;
import io.github.tr100000.researcher.api.criterion.element.TimedSwitchingElement;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.predicate.FluidPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;

import java.util.List;

public final class FluidPredicateHelper {
    private FluidPredicateHelper() {}

    private static final Text HEADER = ModUtils.getScreenTranslated("predicate.fluid");
    private static final Text ANY_FLUID = ModUtils.getScreenTranslated("predicate.fluid.any");

    private static final Text FLUID_LIST = ModUtils.getScreenTranslated("predicate.fluid.list");

    public static void tooltip(FluidPredicate predicate, IndentedTextHolder textHolder) {
        if (predicate.fluids().isPresent()) {
            textHolder.accept(FLUID_LIST);
            textHolder.push();
            predicate.fluids().get().forEach(entry -> textHolder.accept(Text.literal(entry.getIdAsString())));
            textHolder.pop();
        }
        if (predicate.state().isPresent()) {
            textHolder.accept(PredicateHelper.STATE_HEADER);
            textHolder.push();
            PredicateHelper.stateTooltip(predicate.state().get(), textHolder);
            textHolder.pop();
        }
    }

    public static CriterionDisplayElement element(FluidPredicate predicate) {
        CriterionDisplayElement element;
        boolean shouldHaveTooltip = false;
        if (predicate.fluids().isPresent()) {
            if (predicate.fluids().get().size() > 1) shouldHaveTooltip = true;

            List<CriterionDisplayElement> list = predicate.fluids().get().stream().map(FluidPredicateHelper::element).toList();
            element = new TimedSwitchingElement(list);
        }
        else {
            element = new TextElement(ANY_FLUID);
        }

        if (shouldHaveTooltip || predicate.state().isPresent()) {
            IndentedTextHolder textHolder = new IndentedTextHolder();
            textHolder.accept(HEADER);
            textHolder.push();
            tooltip(predicate, textHolder);
            textHolder.pop();

            if (textHolder.count() > 1) element = new GroupedElement(element.withTextTooltip(), new TextElement(Text.literal("*")));
        }

        return element;
    }

    public static CriterionDisplayElement element(Fluid fluid) {
        return new GroupedElement(
                new ItemElement(fluid.getBucketItem().getDefaultStack(), false),
                new TextElement(getFluidName(fluid))
        );
    }

    public static CriterionDisplayElement element(RegistryEntry<Fluid> fluid) {
        return element(fluid.value());
    }

    private static Text getFluidName(Fluid fluid) {
        if (fluid instanceof FlowableFluid flowable) {
            return Text.translatable(Registries.FLUID.getId(flowable.getStill()).toTranslationKey("fluid"));
        }
        else {
            return Text.translatable(Registries.FLUID.getId(fluid).toTranslationKey("fluid"));
        }
    }
}
