package io.github.tr100000.researcher.screen;

import io.github.tr100000.researcher.ClientResearchTracker;
import io.github.tr100000.researcher.Research;
import io.github.tr100000.researcher.ResearchProgress;
import io.github.tr100000.researcher.api.CriterionDisplayElement;
import io.github.tr100000.trutils.api.gui.ExtendedTooltipComponent;
import io.github.tr100000.trutils.api.utils.GameUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ResearchTooltipComponent extends ExtendedTooltipComponent {
    private final MinecraftClient client = MinecraftClient.getInstance();
    public final Research research;

    private final OrderedText title;
    private final OrderedText idText;
    private final OrderedText modNameText;
    private final CriterionDisplayElement criterionDisplay;

    public ResearchTooltipComponent(Research research, boolean showStatus) {
        this.research = research;
        ClientResearchTracker researchManager = client.getNetworkHandler().researcher$getClientTracker();

        if (showStatus) {
            title = researchManager.getTitleWithStatus(research).asOrderedText();
        }
        else {
            title = research.getTitle(researchManager).asOrderedText();
        }

        Identifier researchId = researchManager.getId(research);
        idText = Text.literal(researchId.toString()).formatted(Formatting.DARK_GRAY).asOrderedText();
        modNameText = Text.literal(GameUtils.getModName(researchId)).formatted(Formatting.BLUE, Formatting.ITALIC).asOrderedText();

        criterionDisplay = ResearchInfoView.prepareDisplay(research.trigger());
    }

    @Override
    public void drawText(DrawContext draw, TextRenderer textRenderer, int x, int y) {
        text(draw, textRenderer, title, x, y); y += 10;
        y += 22;
        if (client.options.advancedItemTooltips) {
            text(draw, textRenderer, idText, x, y); y += 10;
        }
        text(draw, textRenderer, modNameText, x, y);
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, int width, int height, DrawContext draw) {
        ResearchProgress progress = client.getNetworkHandler().researcher$getClientTracker().getProgress(research);
        ResearchInfoView.drawCriterion(criterionDisplay, research.trigger(), progress, draw, -1, -1, x + 1, y + 11, 0);
    }

    @Override
    public int getHeight(TextRenderer textRenderer) {
        return client.options.advancedItemTooltips ? 52 : 42;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        int longest = longestText(textRenderer, title, modNameText);
        if (client.options.advancedItemTooltips) {
            longest = longestText(textRenderer, longest, idText);
        }
        int criterionWidth = criterionDisplay.getWidth() + 2;
        return Math.max(longest, criterionWidth);
    }
}
