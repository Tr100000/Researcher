package io.github.tr100000.researcher;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.tr100000.researcher.api.PlayerResearchHolder;
import io.github.tr100000.researcher.api.ResearchHolder;
import io.github.tr100000.trutils.api.gui.Icon;
import io.github.tr100000.trutils.api.gui.TextureIcon;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public record Research(Optional<Component> titleText, Optional<Component> descriptionText, ResearchCriterion<?> trigger, List<Identifier> prerequisiteIds, List<Identifier> recipeUnlocks, Icon display) {
    public static final Icon DEFAULT_ICON = new TextureIcon(ModUtils.id("textures/research/default.png"));

    public static final Codec<Research> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
                ComponentSerialization.CODEC.optionalFieldOf("title").forGetter(Research::titleText),
                ComponentSerialization.CODEC.optionalFieldOf("description").forGetter(Research::descriptionText),
                ResearchCriterion.CODEC.optionalFieldOf("toUnlock", ResearchCriterion.IMPOSSIBLE).forGetter(Research::trigger),
                Identifier.CODEC.listOf().optionalFieldOf("prerequisites", Collections.emptyList()).forGetter(Research::prerequisiteIds),
                Identifier.CODEC.listOf().optionalFieldOf("recipeUnlocks", Collections.emptyList()).forGetter(Research::recipeUnlocks),
                Icon.CODEC.optionalFieldOf("display", DEFAULT_ICON).forGetter(Research::display)
        ).apply(instance, Research::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, Research> PACKET_CODEC = StreamCodec.composite(
            ComponentSerialization.OPTIONAL_STREAM_CODEC, Research::titleText,
            ComponentSerialization.OPTIONAL_STREAM_CODEC, Research::descriptionText,
            ResearchCriterion.PACKET_CODEC, Research::trigger,
            Identifier.STREAM_CODEC.apply(ByteBufCodecs.list()), Research::prerequisiteIds,
            Identifier.STREAM_CODEC.apply(ByteBufCodecs.list()), Research::recipeUnlocks,
            Icon.PACKET_CODEC, Research::display,
            Research::new
    );

    public String getTranslationKey(ResearchHolder researchManager) {
        return researchManager.getIdOrEmpty(this).toLanguageKey(ResearchManager.PATH);
    }

    public String getDescriptionTranslationKey(ResearchHolder researchManager) {
        return researchManager.getIdOrEmpty(this).toLanguageKey(ResearchManager.PATH, "desc");
    }

    public Component getTitle(ResearchHolder researchManager) {
        return titleText.orElseGet(() -> Component.translatable(getTranslationKey(researchManager)));
    }

    public Component getDescription(ResearchHolder researchManager) {
        return descriptionText.orElseGet(() -> Component.translatableWithFallback(getDescriptionTranslationKey(researchManager), ""));
    }

    public List<Research> prerequisites(ResearchHolder researchManager) {
        return prerequisiteIds().stream().map(researchManager::get).toList();
    }

    public MutableComponent getChatAnnouncementText(ResearchHolder researchManager, ServerPlayer player) {
        Component titleText = getTitle(researchManager);
        Component descriptionText = getDescription(researchManager);
        MutableComponent hoverText = titleText.copy();
        if (!descriptionText.getString().isBlank()) {
            hoverText = hoverText.append("\n").append(getDescription(researchManager));
        }
        HoverEvent hoverEvent = new HoverEvent.ShowText(hoverText.withStyle(ChatFormatting.GREEN));
        ClickEvent clickEvent = new ClickEvent.RunCommand("/researcher_client show " + researchManager.getId(this));
        Component researchText = ComponentUtils.wrapInSquareBrackets(titleText).withStyle(style -> style.withHoverEvent(hoverEvent).withClickEvent(clickEvent)).withStyle(ChatFormatting.GREEN);
        return Component.translatable("chat.researcher.research", player.getDisplayName(), researchText);
    }

    public CriterionTrigger<?> getTrigger() {
        return trigger().criterion().trigger();
    }

    public CriterionTriggerInstance getConditions() {
        return trigger().criterion().triggerInstance();
    }

    public static Comparator<Research> idComparator(ResearchHolder researchManager) {
        return Comparator.comparing(researchManager::getIdOrEmpty);
    }

    public static Comparator<Research> statusComparator(PlayerResearchHolder researchManager) {
        return Comparator.comparing(researchManager::getStatus);
    }
}
