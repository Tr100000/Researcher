package io.github.tr100000.researcher;

import io.github.tr100000.trutils.api.gui.IconRenderers;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ResearchToast implements Toast {
    private static final Identifier TEXTURE = Identifier.ofVanilla("toast/advancement");
    private static final Text TOAST_TEXT = Text.translatable("chat.researcher.toast");
    private static final int COLOR = 0xFFFF00;

    private final Research research;
    private Visibility visibility = Visibility.HIDE;

    public ResearchToast(Research research) {
        this.research = research;
    }

    @Override
    public Visibility getVisibility() {
        return visibility;
    }

    @Override
    public void update(ToastManager manager, long time) {
        visibility = manager.getClient().getNetworkHandler() == null || time >= 5000.0 * manager.getNotificationDisplayTimeMultiplier() ? Visibility.HIDE : Visibility.SHOW;
    }

    @Override
    public void draw(DrawContext draw, TextRenderer textRenderer, long startTime) {
        draw.drawGuiTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, 0, 0, getWidth(), getHeight());

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.getNetworkHandler() == null) return;

        List<OrderedText> list = client.textRenderer.wrapLines(research.getTitle(client.getNetworkHandler().researcher$getClientTracker()), 125);
        if (list.size() == 1) {
            draw.drawText(textRenderer, TOAST_TEXT, 30, 7, COLOR | Colors.BLACK, false);
            draw.drawText(textRenderer, list.getFirst(), 30, 18, -1, false);
        }
        else {
            // I copied this code from AdvancementToast, and I have no idea what it does
            if (startTime < 1500L) {
                int color = Colors.YELLOW | MathHelper.floor(MathHelper.clamp((1500L - startTime) / 300.0F, 0.0F, 1.0F) * 255.0F) << 24 | 67108864;
                draw.drawText(textRenderer, TOAST_TEXT, 30, 11, color, false);
            } else {
                int color = 16777215 | MathHelper.floor(MathHelper.clamp((startTime - 1500L) / 300.0F, 0.0F, 1.0F) * 252.0F) << 24 | 67108864;
                int y = this.getHeight() / 2 - list.size() * 9 / 2;

                for (OrderedText orderedText : list) {
                    draw.drawText(textRenderer, orderedText, 30, y, color, false);
                    y += 9;
                }
            }
        }

        IconRenderers.draw(research.display(), draw, 8, 8);
    }

    @Override
    public @Nullable SoundEvent getSoundEvent() {
        return ResearcherClient.RESEARCH_FINISHED_SOUND;
    }
}
