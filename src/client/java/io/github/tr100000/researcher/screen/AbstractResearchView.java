package io.github.tr100000.researcher.screen;

import io.github.tr100000.trutils.api.gui.AbstractView;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.navigation.ScreenRectangle;

import java.util.function.Consumer;

public abstract class AbstractResearchView extends AbstractView implements LayoutElement {
    protected static final Minecraft client = Minecraft.getInstance();

    protected ScreenRectangle scissorRect;
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
    public void visitWidgets(Consumer<AbstractWidget> consumer) {
        children().forEach(child -> {
            if (child instanceof AbstractWidget widgetChild) {
                consumer.accept(widgetChild);
            }
        });
    }

    @Override
    public ScreenRectangle getRectangle() {
        return LayoutElement.super.getRectangle();
    }

    @Override
    public void render(GuiGraphics draw, int mouseX, int mouseY, float delta) {
        draw.pose().pushMatrix();
        draw.enableScissor(scissorRect.left(), scissorRect.top(), scissorRect.right(), scissorRect.bottom());
        renderView(draw, mouseX, mouseY, delta);
        draw.disableScissor();
        draw.pose().popMatrix();
    }

    public void renderView(GuiGraphics draw, int mouseX, int mouseY, float delta) {
        super.render(draw, mouseX, mouseY, delta);
    }
}
