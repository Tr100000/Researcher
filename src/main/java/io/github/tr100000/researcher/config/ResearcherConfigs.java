package io.github.tr100000.researcher.config;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;

public final class ResearcherConfigs {
    private ResearcherConfigs() {}

    public static final ResearcherClientConfig client = ConfigApiJava.registerAndLoadConfig(ResearcherClientConfig::new, RegisterType.CLIENT);
    public static final ResearcherServerConfig server = ConfigApiJava.registerAndLoadConfig(ResearcherServerConfig::new, RegisterType.BOTH);

    public static void init() { /* loads the class */ }
}
