package io.github.tr100000.researcher.impl.criterion.element;

import io.github.tr100000.researcher.api.CriterionDisplayElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

public class TextCriterionDisplayElement implements CriterionDisplayElement {
    protected static final MinecraftClient client = MinecraftClient.getInstance();

    protected final OrderedText text;
    protected final int color;
    protected final boolean shadow;

    protected int cachedWidth = -1;

    public TextCriterionDisplayElement(OrderedText text, int color, boolean shadow) {
        this.text = text;
        this.color = color;
        this.shadow = shadow;
    }

    public TextCriterionDisplayElement(OrderedText text) {
        this(text, Colors.WHITE, true);
    }

    public TextCriterionDisplayElement(Text text, int color, boolean shadow) {
        this(text.asOrderedText(), color, shadow);
    }

    public TextCriterionDisplayElement(Text text) {
        this(text.asOrderedText());
    }

    @Override
    public int render(DrawContext draw, int x, int y, int mouseX, int mouseY, float delta) {
        draw.drawText(client.textRenderer, text, x, y + 4, color, shadow);
        return getWidth();
    }

    @Override
    public int getWidth() {
        if (cachedWidth < 0) cachedWidth = client.textRenderer.getWidth(text);
        return cachedWidth;
    }
}
