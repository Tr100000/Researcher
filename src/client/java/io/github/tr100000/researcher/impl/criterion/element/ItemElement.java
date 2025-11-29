package io.github.tr100000.researcher.impl.criterion.element;

import io.github.tr100000.researcher.api.CriterionDisplayElement;
import io.github.tr100000.trutils.api.gui.GuiHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.Collections;
import java.util.List;

public final class ItemElement implements CriterionDisplayElement {
    private final ItemStack stack;
    private final List<Text> tooltip;
    private boolean useStackTooltip = false;

    public ItemElement(ItemStack stack, List<Text> tooltip) {
        this.stack = stack;
        this.tooltip = tooltip;
    }

    public ItemElement(ItemStack stack, boolean useStackTooltip) {
        this(stack, Collections.emptyList());
        this.useStackTooltip = useStackTooltip;
    }

    @Override
    public int render(DrawContext draw, int x, int y, int mouseX, int mouseY, float delta) {
        if (useStackTooltip || tooltip == null) {
            GuiHelper.drawItemWithoutEntityAndTooltip(draw, stack, x, y, mouseX, mouseY);
        }
        else {
            GuiHelper.drawItemWithoutEntityAndTooltip(draw, stack, x, y, mouseX, mouseY, tooltip);
        }

        return 16;
    }

    @Override
    public int getWidth() {
        return 16;
    }
}
