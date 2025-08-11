package io.github.tr100000.researcher.screen;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.ScrollableTextFieldWidget;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

import java.util.List;

public class ResearchDescriptionWidget extends ScrollableTextFieldWidget {
    private final int maxHeight;
    private final TextRenderer textRenderer;
    private final List<OrderedText> wrappedText;

    public ResearchDescriptionWidget(int x, int y, int width, int height, Text text, TextRenderer textRenderer) {
        super(x, y, width, height, text);
        this.maxHeight = height;
        this.textRenderer = textRenderer;
        this.wrappedText = textRenderer.wrapLines(text, width - 10);
    }

    @Override
    public int getHeight() {
        return Math.min(maxHeight, getContentsHeight() + 7);
    }

    @Override
    protected int getContentsHeight() {
        return wrappedText.size() * 9 - 3;
    }

    @Override
    protected double getDeltaYPerScroll() {
        return 9.0;
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        if (getMessage().getString().isBlank()) return;
        super.renderWidget(context, mouseX, mouseY, delta);
    }

    @Override
    protected void renderContents(DrawContext draw, int mouseX, int mouseY, float delta) {
        for (int i = 0; i < wrappedText.size(); i++) {
            draw.drawTextWithShadow(textRenderer, wrappedText.get(i), getX() + 3, getY() + 3 + 9 * i, Colors.WHITE);
        }
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        builder.put(NarrationPart.TITLE, getMessage());
    }
}
