package io.github.tr100000.researcher.screen;

import io.github.tr100000.researcher.Research;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Environment(EnvType.CLIENT)
public class ResearchTooltipWrapper {
    private final boolean showStatus;
    @Nullable
    private ResearchTooltipComponent tooltipComponent;

    public ResearchTooltipWrapper(boolean showStatus) {
        this.showStatus = showStatus;
    }

    public List<TooltipComponent> getOrCreate(Research research) {
        if (tooltipComponent == null) {
            tooltipComponent = new ResearchTooltipComponent(research, showStatus);
        }
        return List.of(tooltipComponent);
    }
}
