package io.github.tr100000.researcher.screen;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractTextAreaWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.CommonColors;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class ResearchDescriptionWidget extends AbstractTextAreaWidget {
    private final int maxHeight;
    private final Font font;
    private final List<FormattedCharSequence> wrappedText;

    public ResearchDescriptionWidget(int x, int y, int width, int height, Component text, Font font) {
        super(x, y, width, height, text, defaultSettings(9));
        this.maxHeight = height;
        this.font = font;
        this.wrappedText = font.split(text, width - 10);
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
    public void extractWidgetRenderState(final GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        if (getMessage().getString().isBlank()) return;
        super.extractWidgetRenderState(graphics, mouseX, mouseY, delta);
    }

    @Override
    protected void extractContents(final GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        for (int i = 0; i < wrappedText.size(); i++) {
            graphics.text(font, wrappedText.get(i), getX() + 3, getY() + 3 + 9 * i, CommonColors.WHITE);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput builder) {
        builder.add(NarratedElementType.TITLE, getMessage());
    }
}
