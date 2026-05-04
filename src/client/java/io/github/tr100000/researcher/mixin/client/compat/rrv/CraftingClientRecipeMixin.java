package io.github.tr100000.researcher.mixin.client.compat.rrv;

import cc.cassian.rrv.api.recipe.ReliableClientRecipe;
import cc.cassian.rrv.common.builtin.crafting.CraftingClientRecipe;
import cc.cassian.rrv.common.recipe.inventory.RecipeViewScreen;
import io.github.tr100000.researcher.ClientResearchTracker;
import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.Research;
import io.github.tr100000.trutils.api.gui.GuiHelper;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(CraftingClientRecipe.class)
public abstract class CraftingClientRecipeMixin implements ReliableClientRecipe {
    @Unique
    private static final Identifier ERROR_TEXTURE = ModUtils.id("textures/gui/error_overlay.png");

    @Unique
    private @Nullable List<ClientTooltipComponent> cachedTooltip;

    @Shadow
    public abstract @Nullable Identifier getId();

    @Inject(method = "renderRecipe", at = @At("TAIL"))
    private void render(RecipeViewScreen screen, RecipePosition recipePosition, GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        ClientResearchTracker tracker = getTracker();
        if (tracker == null) return;
        if (!tracker.canCraftRecipe(getId())) {
            final int texWidth = 22;
            final int texHeight = 16;
            final int x = 61;
            final int y = (getType().getDisplayHeight() - texHeight) / 2 - 1;
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, ERROR_TEXTURE, x, y, 0, 0, texWidth, texHeight, texWidth, texHeight);

            if (GuiHelper.isMouseTouching(x, y, texWidth, texHeight, mouseX, mouseY))
                GuiHelper.tooltip(guiGraphics, Minecraft.getInstance().font, computeTooltipIfAbsent(tracker), recipePosition.left() + mouseX, recipePosition.top() + mouseY, DefaultTooltipPositioner.INSTANCE);
        }
    }

    @Unique
    private @Nullable ClientResearchTracker getTracker() {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        if (connection == null) return null;
        return connection.researcher$getClientTracker();
    }

    @Unique
    private List<ClientTooltipComponent> computeTooltipIfAbsent(ClientResearchTracker tracker) {
        if (cachedTooltip != null)
            return cachedTooltip;

        List<Component> components = new ObjectArrayList<>();
        components.add(ModUtils.getScreenTranslated("unlocked_by_header"));
        tracker.recipeUnlockedBy(getId()).forEach(id -> {
            Research research = tracker.get(id);
            assert research != null;
            components.add(Component.literal("  ").append(research.getTitle(tracker)));
        });

        cachedTooltip = components.stream()
                .map(Component::getVisualOrderText)
                .map(ClientTooltipComponent::create)
                .toList();
        return cachedTooltip;
    }
}
