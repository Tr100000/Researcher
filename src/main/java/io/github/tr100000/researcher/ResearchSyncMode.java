package io.github.tr100000.researcher;

import me.fzzyhmstrs.fzzy_config.util.EnumTranslatable;
import org.jetbrains.annotations.NotNull;

public enum ResearchSyncMode implements EnumTranslatable {
    NONE,
    TEAM,
    GLOBAL,
    ;

    @Override
    public @NotNull String prefix() {
        return Researcher.MODID + ".research_sync_mode";
    }
}
