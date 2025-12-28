package io.github.tr100000.researcher;

import me.fzzyhmstrs.fzzy_config.util.EnumTranslatable;

public enum ResearchSyncMode implements EnumTranslatable {
    NONE,
    TEAM,
    GLOBAL,
    ;

    @Override
    public String prefix() {
        return "config." + Researcher.MODID + ".sync_mode";
    }
}
