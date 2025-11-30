package io.github.tr100000.researcher.impl.recipe;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.trutils.api.gui.ExtendedTooltipComponent;
import io.github.tr100000.trutils.api.utils.GameUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.IngredientPlacement;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.context.ContextParameterMap;

import java.util.Collections;
import java.util.List;

public class CraftingRecipeTooltipComponent extends ExtendedTooltipComponent {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final Identifier BACKGROUND = ModUtils.id("textures/gui/crafting.png");

    private final int recipeWidth;
    private final List<List<ItemStack>> ingredients;
    private final ItemStack result;
    private final OrderedText recipeText;
    private final OrderedText idText;
    private final OrderedText modNameText;

    public static CraftingRecipeTooltipComponent create(Identifier recipeId, int recipeWidth, IngredientPlacement ingredientPlacement, SlotDisplay result, ContextParameterMap contextParameterMap) {
        List<List<ItemStack>> ingredients = new ObjectArrayList<>();
        for (int i = 0; i < ingredientPlacement.getPlacementSlots().size(); i++) {
            int ingredientIndex = ingredientPlacement.getPlacementSlots().getInt(i);
            if (ingredientIndex < 0) {
                ingredients.add(Collections.emptyList());
            }
            else {
                ingredients.add(ingredientPlacement.getIngredients().get(ingredientIndex).toDisplay().getStacks(contextParameterMap));
            }
        }

        ItemStack resultStack = result.getFirst(contextParameterMap);

        return new CraftingRecipeTooltipComponent(recipeId, recipeWidth, ingredients, resultStack);
    }

    public CraftingRecipeTooltipComponent(Identifier recipeId, int recipeWidth, List<List<ItemStack>> ingredients, ItemStack result) {
        this.recipeWidth = recipeWidth;
        this.ingredients = ingredients;
        this.result = result;
        this.recipeText = Text.translatableWithFallback(recipeId.toTranslationKey("recipe"), result.getName().getString()).asOrderedText();
        this.idText = Text.literal(recipeId.toString()).formatted(Formatting.DARK_GRAY).asOrderedText();
        this.modNameText = Text.literal(GameUtils.getModName(recipeId.getNamespace())).formatted(Formatting.BLUE, Formatting.ITALIC).asOrderedText();
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return longestText(textRenderer, 176, recipeText, idText, modNameText);
    }

    @Override
    public int getHeight(TextRenderer textRenderer) {
        int textureHeight = 86;
        return 10 + textureHeight + 2 + (client.options.advancedItemTooltips ? 20 : 10);
    }

    @Override
    public void drawText(DrawContext draw, TextRenderer textRenderer, int x, int y) {
        text(draw, textRenderer, recipeText, x, y);
        y += 10;
        y += 86;
        y += 2;
        if (client.options.advancedItemTooltips) {
            text(draw, textRenderer, idText, x, y);
            y += 10;
        }
        text(draw, textRenderer, modNameText, x, y);
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, int width, int height, DrawContext draw) {
        draw.drawTexture(RenderPipelines.GUI_TEXTURED, BACKGROUND, x, y + 10, 0, 0, width, height, 256, 256);

        final long currentTick = System.currentTimeMillis() / 1000;

        for (int i = 0; i < ingredients.size(); i++) {
            List<ItemStack> stacks = ingredients.get(i);
            if (stacks.isEmpty()) continue;
            int row = i / recipeWidth;
            int column = i % recipeWidth;

            ItemStack stack = stacks.get((int)(currentTick % stacks.size()));
            draw.drawItemWithoutEntity(stack, x + 30 + column * 18, y + 27 + row * 18);
        }

        draw.drawItemWithoutEntity(result, x + 124, y + 45);
    }
}
