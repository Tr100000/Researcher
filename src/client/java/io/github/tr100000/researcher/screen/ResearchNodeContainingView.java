package io.github.tr100000.researcher.screen;

public abstract class ResearchNodeContainingView extends AbstractResearchView {
    protected ResearchNodeContainingView(ResearchScreen parent, int x, int y, int width, int height) {
        super(parent, x, y, width, height);
    }

    public void onNodeShiftClicked(ResearchNodeWidget widget) {}
}
