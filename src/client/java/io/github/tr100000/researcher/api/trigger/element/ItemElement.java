package io.github.tr100000.researcher.api.trigger.element;

import io.github.tr100000.researcher.api.trigger.TriggerDisplayElement;
import io.github.tr100000.trutils.api.gui.GuiHelper;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class ItemElement implements TriggerDisplayElement {
    protected final ItemStack stack;
    protected final boolean useStackTooltip;

    public ItemElement(ItemLike item, boolean useStackTooltip) {
        this(item.asItem().getDefaultInstance(), useStackTooltip);
    }

    public ItemElement(ItemStack stack, boolean useStackTooltip) {
        this.stack = stack;
        this.useStackTooltip = useStackTooltip;
    }

    @Override
    public int extractRenderState(GuiGraphicsExtractor graphics, int x, int y, int mouseX, int mouseY, float delta) {
        if (useStackTooltip) {
            GuiHelper.fakeItem(graphics, stack, x, y, mouseX, mouseY);
        }
        else {
            graphics.fakeItem(stack, x, y);
        }

        return 16;
    }

    @Override
    public int width() {
        return 16;
    }
}
