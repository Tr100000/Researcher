package io.github.tr100000.researcher.mixin.client.compat.jei;

import io.github.tr100000.researcher.ClientResearchTracker;
import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.Research;
import io.github.tr100000.trutils.api.gui.GuiHelper;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.library.plugins.vanilla.crafting.CraftingRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingRecipeCategory.class)
public class CraftingRecipeCategoryMixin {
    @Shadow @Final
    public static int height;

    @Unique
    private static final Identifier ERROR_TEXTURE = ModUtils.id("textures/gui/error_overlay.png");

    @Inject(method = "draw(Lnet/minecraft/world/item/crafting/RecipeHolder;Lmezz/jei/api/gui/ingredient/IRecipeSlotsView;Lnet/minecraft/client/gui/GuiGraphicsExtractor;DD)V", at = @At("TAIL"))
    private void draw(RecipeHolder<CraftingRecipe> recipeHolder, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY, CallbackInfo ci) {
        ClientResearchTracker tracker = getTracker();
        if (tracker == null) return;
        if (!tracker.canCraftRecipe(recipeHolder.id().identifier())) {
            final int texWidth = 22;
            final int texHeight = 16;
            final int x = 61;
            final int y = (height - texHeight) / 2;
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, ERROR_TEXTURE, x, y, 0, 0, texWidth, texHeight, texWidth, texHeight);
        }
    }

    @Inject(method = "getTooltip(Lmezz/jei/api/gui/builder/ITooltipBuilder;Lnet/minecraft/world/item/crafting/RecipeHolder;Lmezz/jei/api/gui/ingredient/IRecipeSlotsView;DD)V", at = @At("TAIL"))
    private void getTooltip(ITooltipBuilder tooltip, RecipeHolder<CraftingRecipe> recipeHolder, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY, CallbackInfo ci) {
        ClientResearchTracker tracker = getTracker();
        if (tracker == null) return;
        if (!tracker.canCraftRecipe(recipeHolder.id().identifier()) && GuiHelper.isMouseTouching(61, (height - 16) / 2, 22, 16, mouseX, mouseY)) {
            tooltip.add(ModUtils.getScreenTranslated("unlocked_by_header"));
            tracker.recipeUnlockedBy(recipeHolder.id().identifier()).forEach(id -> {
                Research research = tracker.get(id);
                assert research != null;
                tooltip.add(Component.literal("  ").append(research.getTitle(tracker)));
            });
        }
    }

    @Unique
    private @Nullable ClientResearchTracker getTracker() {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        if (connection == null) return null;
        return connection.researcher$getClientTracker();
    }
}
