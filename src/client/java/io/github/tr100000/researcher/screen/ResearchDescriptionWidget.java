package io.github.tr100000.researcher.screen;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractTextAreaWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.CommonColors;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class ResearchDescriptionWidget extends AbstractTextAreaWidget {
    private final int maxHeight;
    private final Font textRenderer;
    private final List<FormattedCharSequence> wrappedText;

    public ResearchDescriptionWidget(int x, int y, int width, int height, Component text, Font textRenderer) {
        super(x, y, width, height, text);
        this.maxHeight = height;
        this.textRenderer = textRenderer;
        this.wrappedText = textRenderer.split(text, width - 10);
    }

    @Override
    public int getHeight() {
        return Math.min(maxHeight, getInnerHeight() + 7);
    }

    @Override
    protected int getInnerHeight() {
        return wrappedText.size() * 9 - 3;
    }

    @Override
    protected double scrollRate() {
        return 9.0;
    }

    @Override
    public void renderWidget(GuiGraphics context, int mouseX, int mouseY, float delta) {
        if (getMessage().getString().isBlank()) return;
        super.renderWidget(context, mouseX, mouseY, delta);
    }

    @Override
    protected void renderContents(GuiGraphics draw, int mouseX, int mouseY, float delta) {
        for (int i = 0; i < wrappedText.size(); i++) {
            draw.drawString(textRenderer, wrappedText.get(i), getX() + 3, getY() + 3 + 9 * i, CommonColors.WHITE);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput builder) {
        builder.add(NarratedElementType.TITLE, getMessage());
    }
}
