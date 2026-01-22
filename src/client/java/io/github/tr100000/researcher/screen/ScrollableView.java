package io.github.tr100000.researcher.screen;

public interface ScrollableView {
    default int getOffsetX() {
        return 0;
    }

    default int getOffsetY() {
        return 0;
    }

    default double offsetToScreenX(double offsetX) {
        return getOffsetX() + offsetX;
    }

    default double offsetToScreenY(double offsetY) {
        return getOffsetY() + offsetY;
    }
}
