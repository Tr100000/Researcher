package io.github.tr100000.researcher;

import io.github.tr100000.trutils.api.gui.IconRenderers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.CommonColors;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

@NullMarked
public class ResearchToast implements Toast {
    private static final Identifier TEXTURE = Identifier.withDefaultNamespace("toast/advancement");
    private static final Component TOAST_TEXT = Component.translatable("chat.researcher.toast");
    private static final int COLOR = 0xFFFF00;

    private final Research research;
    private Visibility visibility = Visibility.HIDE;

    public ResearchToast(Research research) {
        this.research = research;
    }

    @Override
    public Visibility getWantedVisibility() {
        return visibility;
    }

    @Override
    public void update(ToastManager manager, long time) {
        visibility = manager.getMinecraft().getConnection() == null || time >= 5000.0 * manager.getNotificationDisplayTimeMultiplier() ? Visibility.HIDE : Visibility.SHOW;
    }

    @Override
    public void render(GuiGraphics draw, Font textRenderer, long startTime) {
        draw.blitSprite(RenderPipelines.GUI_TEXTURED, TEXTURE, 0, 0, width(), height());

        Minecraft client = Minecraft.getInstance();
        if (client.getConnection() == null) return;

        List<FormattedCharSequence> list = client.font.split(research.getTitle(client.getConnection().researcher$getClientTracker()), 125);
        if (list.size() == 1) {
            draw.drawString(textRenderer, TOAST_TEXT, 30, 7, COLOR | CommonColors.BLACK, false);
            draw.drawString(textRenderer, list.getFirst(), 30, 18, -1, false);
        }
        else {
            // I copied this code from AdvancementToast, and I have no idea what it does
            if (startTime < 1500L) {
                int color = CommonColors.YELLOW | Mth.floor(Mth.clamp((1500L - startTime) / 300.0F, 0.0F, 1.0F) * 255.0F) << 24 | 67108864;
                draw.drawString(textRenderer, TOAST_TEXT, 30, 11, color, false);
            } else {
                int color = 16777215 | Mth.floor(Mth.clamp((startTime - 1500L) / 300.0F, 0.0F, 1.0F) * 252.0F) << 24 | 67108864;
                int y = this.height() / 2 - list.size() * 9 / 2;

                for (FormattedCharSequence orderedText : list) {
                    draw.drawString(textRenderer, orderedText, 30, y, color, false);
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
