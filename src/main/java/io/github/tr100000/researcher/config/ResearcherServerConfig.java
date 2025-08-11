package io.github.tr100000.researcher.config;

import io.github.tr100000.researcher.ResearchSyncMode;
import io.github.tr100000.researcher.Researcher;
import me.fzzyhmstrs.fzzy_config.annotations.Action;
import me.fzzyhmstrs.fzzy_config.annotations.RequiresAction;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedEnum;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber;

@Translation(prefix = "config.researcher.server")
public class ResearcherServerConfig extends Config {
    public ResearcherServerConfig() {
        super(Researcher.id("server"));
    }

    @RequiresAction(action = Action.RELOAD_DATA)
    public ValidatedFloat researchCostMultiplier = new ValidatedFloat(1, 100000, 0.1F, ValidatedNumber.WidgetType.TEXTBOX);
    public ValidatedEnum<ResearchSyncMode> researchSyncMode = new ValidatedEnum<>(ResearchSyncMode.NONE);

    @Override
    public int defaultPermLevel() {
        return 3;
    }
}
