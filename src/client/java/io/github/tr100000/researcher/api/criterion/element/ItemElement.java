package io.github.tr100000.researcher.api.criterion.element;

import io.github.tr100000.researcher.api.criterion.CriterionDisplayElement;
import io.github.tr100000.trutils.api.gui.GuiHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;

public class ItemElement implements CriterionDisplayElement {
    protected final ItemStack stack;
    protected final boolean useStackTooltip;

    public ItemElement(ItemConvertible item, boolean useStackTooltip) {
        this(item.asItem().getDefaultStack(), useStackTooltip);
    }

    public ItemElement(ItemStack stack, boolean useStackTooltip) {
        this.stack = stack;
        this.useStackTooltip = useStackTooltip;
    }

    @Override
    public int render(DrawContext draw, int x, int y, int mouseX, int mouseY, float delta) {
        if (useStackTooltip) {
            GuiHelper.drawItemWithoutEntityAndTooltip(draw, stack, x, y, mouseX, mouseY);
        }
        else {
            draw.drawItemWithoutEntity(stack, x, y);
        }

        return 16;
    }

    @Override
    public int getWidth() {
        return 16;
    }
}
