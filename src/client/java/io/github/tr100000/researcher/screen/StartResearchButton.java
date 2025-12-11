package io.github.tr100000.researcher.screen;

import io.github.tr100000.researcher.ClientResearchTracker;
import io.github.tr100000.researcher.Research;
import io.github.tr100000.researcher.networking.StartResearchC2SPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.input.AbstractInput;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class StartResearchButton extends PressableWidget {
    private final Identifier researchId;
    private final boolean pin;
    private final int originalX;
    private boolean isCurrent;

    private StartResearchButton(int x, int y, int width, int height, Text text, Identifier researchId, boolean pin, boolean isCurrent) {
        super(x, y, width, height, text);
        this.researchId = researchId;
        this.pin = pin;
        this.originalX = x;
        this.isCurrent = isCurrent;
    }

    public static StartResearchButton create(int x, int y, ClientResearchTracker researchTracker, Research research, boolean pin, boolean isCurrent) {
        StartResearchButton button = new StartResearchButton(x, y, 0, 20, Text.empty(), researchTracker.getIdOrEmpty(research), pin, isCurrent);
        button.updateText(isCurrent);
        return button;
    }

    @Override
    public void onPress(AbstractInput input) {
        if (isCurrent) {
            if (input.hasShift() && MinecraftClient.getInstance().player.isInCreativeMode()) {
                ClientPlayNetworking.send(new StartResearchC2SPacket(StartResearchC2SPacket.Mode.REVOKE, Optional.of(researchId)));
            }
            else if (pin) {
                ClientPlayNetworking.send(new StartResearchC2SPacket(StartResearchC2SPacket.Mode.UNPIN, Optional.of(researchId)));
            }
            else {
                ClientPlayNetworking.send(new StartResearchC2SPacket(StartResearchC2SPacket.Mode.SET_CURRENT, Optional.empty()));
            }
            isCurrent = false;
        }
        else {
            if (input.hasShift() && MinecraftClient.getInstance().player.isInCreativeMode()) {
                ClientPlayNetworking.send(new StartResearchC2SPacket(StartResearchC2SPacket.Mode.GRANT, Optional.of(researchId)));
            }
            else if (pin) {
                ClientPlayNetworking.send(new StartResearchC2SPacket(StartResearchC2SPacket.Mode.PIN, Optional.of(researchId)));
            }
            else {
                ClientPlayNetworking.send(new StartResearchC2SPacket(StartResearchC2SPacket.Mode.SET_CURRENT, Optional.of(researchId)));
            }
            isCurrent = true;
        }
        setFocused(false);
        updateText(isCurrent);
    }

    @Override
    protected void drawIcon(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        drawLabel(context.getTextConsumer());
    }

    public void updateText(boolean isCurrent) {
        if (pin) {
            setText(isCurrent ? "unpin" : "pin");
        }
        else {
            setText(isCurrent ? "stop" : "start");
        }
        this.isCurrent = isCurrent;
    }

    private void setText(String key) {
        Text text = Text.translatable("screen.researcher." + key);
        int newWidth = MinecraftClient.getInstance().textRenderer.getWidth(text) + 8;
        setWidth(newWidth);
        setX(originalX - newWidth);
        setMessage(text);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        super.appendDefaultNarrations(builder);
    }
}
