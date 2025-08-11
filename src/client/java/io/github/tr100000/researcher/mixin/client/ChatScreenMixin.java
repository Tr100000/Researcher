package io.github.tr100000.researcher.mixin.client;

import io.github.tr100000.researcher.ClientResearchTracker;
import io.github.tr100000.researcher.networking.StartResearchC2SPacket;
import io.github.tr100000.trutils.api.gui.GuiHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin extends Screen {
    protected ChatScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "mouseClicked", at = @At("TAIL"))
    private void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        ClientResearchTracker researchTracker = client.getNetworkHandler().researcher$getClientTracker();

        int y = 0;
        if (researchTracker.getCurrentResearching() != null) {
            unsetResearchIfTouching(y + 1, researchTracker.getCurrentResearchingId(), false, mouseX, mouseY);
            y += 27;
        }
        for (Identifier id : researchTracker.getPinnedResearches()) {
            unsetResearchIfTouching(y + 1, id, true, mouseX, mouseY);
            y += 27;
        }
    }

    @Unique
    private void unsetResearchIfTouching(int y, Identifier id, boolean pin, double mouseX, double mouseY) {
        if (GuiHelper.isMouseTouching(0, y, 150, 26, mouseX, mouseY)) {
            if (pin) {
                ClientPlayNetworking.send(new StartResearchC2SPacket(StartResearchC2SPacket.Mode.UNPIN, Optional.of(id)));
            }
            else {
                ClientPlayNetworking.send(new StartResearchC2SPacket(StartResearchC2SPacket.Mode.SET_CURRENT, Optional.empty()));
            }
        }
    }
}
