package io.github.tr100000.researcher.screen;

import io.github.tr100000.trutils.api.gui.AbstractView;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.Widget;

import java.util.function.Consumer;

public abstract class AbstractResearchView extends AbstractView implements Widget {
    protected static final MinecraftClient client = MinecraftClient.getInstance();

    protected ScreenRect scissorRect;
    protected final ResearchScreen parent;
    protected int x;
    protected int y;
    protected int width;
    protected int height;

    protected AbstractResearchView(ResearchScreen parent, int x, int y, int width, int height) {
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX >= getX() && mouseY >= getY() && mouseX < getX() + getWidth() && mouseY < getY() + getHeight();
    }

    @Override
    public void forEachChild(Consumer<ClickableWidget> consumer) {
        children().forEach(child -> {
            if (child instanceof ClickableWidget widgetChild) {
                consumer.accept(widgetChild);
            }
        });
    }

    @Override
    public ScreenRect getNavigationFocus() {
        return Widget.super.getNavigationFocus();
    }

    @Override
    public void render(DrawContext draw, int mouseX, int mouseY, float delta) {
        draw.getMatrices().pushMatrix();
        draw.enableScissor(scissorRect.getLeft(), scissorRect.getTop(), scissorRect.getRight(), scissorRect.getBottom());
        renderView(draw, mouseX, mouseY, delta);
        draw.disableScissor();
        draw.getMatrices().popMatrix();
    }

    public void renderView(DrawContext draw, int mouseX, int mouseY, float delta) {
        super.render(draw, mouseX, mouseY, delta);
    }
}
