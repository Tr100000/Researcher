package io.github.tr100000.researcher.config;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.Researcher;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.util.EnumTranslatable;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedEnum;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber;
import org.jetbrains.annotations.NotNull;

@Translation(prefix = "config.researcher.client")
public class ResearcherClientConfig extends Config {
    public ResearcherClientConfig() {
        super(ModUtils.id("client"));
    }

    public ValidatedBoolean researchHud = new ValidatedBoolean(true);
    public ValidatedBoolean pinAvailableResearches = new ValidatedBoolean(false);
    public ValidatedBoolean discoveryResearchMode = new ValidatedBoolean(false);
    public ValidatedEnum<ResearchTreeMode> researchTreeMode = new ValidatedEnum<>(ResearchTreeMode.DIRECTLY_RELATED);
    public ValidatedFloat researchTreeScrollSensitivity = new ValidatedFloat(2.5F, 30.0F, 0.5F, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);
    public ValidatedInt researchScreenSidebarWidth = new ValidatedInt(230);
    public ValidatedInt researchScreenInfoViewHeight = new ValidatedInt(140);

    @Override
    public @NotNull String translationKey() {
        return getId().toTranslationKey("config");
    }

    public enum ResearchTreeMode implements EnumTranslatable {
        DIRECTLY_RELATED,
        ALL_RELATED,
        ;

        @Override
        public @NotNull String prefix() {
            return "config." + Researcher.MODID + ".tree_mode";
        }
    }
}
