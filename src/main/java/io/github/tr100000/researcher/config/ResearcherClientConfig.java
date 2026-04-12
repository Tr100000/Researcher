package io.github.tr100000.researcher.config;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.Researcher;
import me.fzzyhmstrs.fzzy_config.annotations.RootConfig;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
import me.fzzyhmstrs.fzzy_config.util.EnumTranslatable;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedColor;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedEnum;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber;

import java.awt.Color;

@RootConfig
@Translation(prefix = "config.researcher.client")
public class ResearcherClientConfig extends Config {
    public ResearcherClientConfig() {
        super(ModUtils.id("client"));
    }

    public ConfigGroup hudGroup = new ConfigGroup("hud");
    public ValidatedBoolean researchHud = new ValidatedBoolean(true);
    @ConfigGroup.Pop
    public ValidatedBoolean pinAvailableResearches = new ValidatedBoolean(false);

    public ConfigGroup researchScreenGroup = new ConfigGroup("research_screen");
    public ValidatedEnum<ResearchTreeMode> researchTreeMode = new ValidatedEnum<>(ResearchTreeMode.ALL_RELATED);
    public ValidatedBoolean discoveryResearchMode = new ValidatedBoolean(false);
    public ValidatedColor highlightColor = new ValidatedColor(new Color(0xFFEBC000), true);
    public ValidatedFloat researchTreeScrollSensitivity = new ValidatedFloat(2.5F, 30.0F, 0.5F, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);
    public ValidatedInt researchScreenSidebarWidth = new ValidatedInt(230, Integer.MAX_VALUE, 10);
    @ConfigGroup.Pop
    public ValidatedInt researchScreenInfoViewHeight = new ValidatedInt(140, Integer.MAX_VALUE, 10);

    @Override
    public String translationKey() {
        return getId().toLanguageKey("config");
    }

    public enum ResearchTreeMode implements EnumTranslatable {
        DIRECTLY_RELATED,
        ALL_RELATED,
        ALL
        ;

        @Override
        public String prefix() {
            return "config." + Researcher.MODID + ".tree_mode";
        }
    }
}
