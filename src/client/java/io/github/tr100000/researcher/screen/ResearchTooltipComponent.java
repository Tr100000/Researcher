package io.github.tr100000.researcher.screen;

import io.github.tr100000.researcher.ClientResearchTracker;
import io.github.tr100000.researcher.Research;
import io.github.tr100000.researcher.ResearchProgress;
import io.github.tr100000.researcher.api.trigger.TriggerDisplayElement;
import io.github.tr100000.trutils.api.gui.ExtendedTooltipComponent;
import io.github.tr100000.trutils.api.utils.GameUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;

public class ResearchTooltipComponent extends ExtendedTooltipComponent {
    private final Minecraft client = Minecraft.getInstance();
    public final Research research;

    private final FormattedCharSequence title;
    private final FormattedCharSequence idText;
    private final FormattedCharSequence modNameText;
    private final TriggerDisplayElement criterionDisplay;

    public ResearchTooltipComponent(Research research, boolean showStatus) {
        this.research = research;
        ClientResearchTracker researchManager = client.getConnection().researcher$getClientTracker();

        if (showStatus) {
            title = researchManager.getTitleWithStatus(research).getVisualOrderText();
        }
        else {
            title = research.getTitle(researchManager).getVisualOrderText();
        }

        Identifier researchId = researchManager.getIdOrEmpty(research);
        idText = Component.literal(researchId.toString()).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText();
        modNameText = Component.literal(GameUtils.getModName(researchId)).withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC).getVisualOrderText();

        criterionDisplay = ResearchInfoView.prepareDisplay(research.trigger());
    }

    @Override
    public void renderText(GuiGraphics draw, Font textRenderer, int x, int y) {
        text(draw, textRenderer, title, x, y); y += 10;
        y += 22;
        if (client.options.advancedItemTooltips) {
            text(draw, textRenderer, idText, x, y); y += 10;
        }
        text(draw, textRenderer, modNameText, x, y);
    }

    @Override
    public void renderImage(Font textRenderer, int x, int y, int width, int height, GuiGraphics draw) {
        ResearchProgress progress = client.getConnection().researcher$getClientTracker().getProgress(research);
        ResearchInfoView.drawCriterion(criterionDisplay, research.trigger(), progress, draw, -1, -1, x + 1, y + 11, 0);
    }

    @Override
    public int getHeight(Font textRenderer) {
        return client.options.advancedItemTooltips ? 52 : 42;
    }

    @Override
    public int getWidth(Font textRenderer) {
        int longest = longestText(textRenderer, title, modNameText);
        if (client.options.advancedItemTooltips) {
            longest = longestText(textRenderer, longest, idText);
        }
        int criterionWidth = criterionDisplay.getWidth() + 5;
        return Math.max(longest, criterionWidth);
    }
}
