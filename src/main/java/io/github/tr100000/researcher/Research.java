package io.github.tr100000.researcher;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.tr100000.researcher.api.PlayerResearchHolder;
import io.github.tr100000.researcher.api.ResearchHolder;
import io.github.tr100000.trutils.api.gui.IconRenderer;
import io.github.tr100000.trutils.api.gui.TextureIconRenderer;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public record Research(Optional<Text> titleText, Optional<Text> descriptionText, ResearchCriterion<?> trigger, List<Identifier> prerequisiteIds, List<Identifier> recipeUnlocks, IconRenderer display) {
    public static final IconRenderer DEFAULT_ICON = new TextureIconRenderer(Researcher.id("textures/research/default.png"));

    public static final Codec<Research> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
                TextCodecs.CODEC.optionalFieldOf("title").forGetter(Research::titleText),
                TextCodecs.CODEC.optionalFieldOf("description").forGetter(Research::descriptionText),
                ResearchCriterion.CODEC.optionalFieldOf("toUnlock", ResearchCriterion.IMPOSSIBLE).forGetter(Research::trigger),
                Identifier.CODEC.listOf().optionalFieldOf("prerequisites", Collections.emptyList()).forGetter(Research::prerequisiteIds),
                Identifier.CODEC.listOf().optionalFieldOf("recipeUnlocks", Collections.emptyList()).forGetter(Research::recipeUnlocks),
                IconRenderer.CODEC.optionalFieldOf("display", DEFAULT_ICON).forGetter(Research::display)
        ).apply(instance, Research::new)
    );

    public static final PacketCodec<RegistryByteBuf, Research> PACKET_CODEC = PacketCodec.tuple(
            TextCodecs.OPTIONAL_PACKET_CODEC, Research::titleText,
            TextCodecs.OPTIONAL_PACKET_CODEC, Research::descriptionText,
            ResearchCriterion.PACKET_CODEC, Research::trigger,
            Identifier.PACKET_CODEC.collect(PacketCodecs.toList()), Research::prerequisiteIds,
            Identifier.PACKET_CODEC.collect(PacketCodecs.toList()), Research::recipeUnlocks,
            IconRenderer.PACKET_CODEC, Research::display,
            Research::new
    );

    public String getTranslationKey(ResearchHolder researchManager) {
        return researchManager.getId(this).toTranslationKey(ResearchManager.PATH);
    }

    public String getDescriptionTranslationKey(ResearchHolder researchManager) {
        return researchManager.getId(this).toTranslationKey(ResearchManager.PATH, "desc");
    }

    public Text getTitle(ResearchHolder researchManager) {
        return titleText.orElseGet(() -> Text.translatable(getTranslationKey(researchManager)));
    }

    public Text getDescription(ResearchHolder researchManager) {
        return descriptionText.orElseGet(() -> Text.translatableWithFallback(getDescriptionTranslationKey(researchManager), ""));
    }

    public List<Research> prerequisites(ResearchHolder researchManager) {
        return prerequisiteIds().stream().map(researchManager::get).toList();
    }

    public MutableText getChatAnnouncementText(ResearchHolder researchManager, ServerPlayerEntity player) {
        Text titleText = getTitle(researchManager);
        Text descriptionText = getDescription(researchManager);
        MutableText hoverText = titleText.copy();
        if (!descriptionText.getString().isBlank()) {
            hoverText = hoverText.append("\n").append(getDescription(researchManager));
        }
        HoverEvent hoverEvent = new HoverEvent.ShowText(hoverText.formatted(Formatting.GREEN));
        ClickEvent clickEvent = new ClickEvent.RunCommand("/researcher_client show " + researchManager.getId(this));
        Text researchText = Texts.bracketed(titleText).styled(style -> style.withHoverEvent(hoverEvent).withClickEvent(clickEvent)).formatted(Formatting.GREEN);
        return Text.translatable("chat.researcher.research", player.getDisplayName(), researchText);
    }

    public Criterion<?> getTrigger() {
        return trigger().criterion().trigger();
    }

    public CriterionConditions getConditions() {
        return trigger().criterion().conditions();
    }

    public static Comparator<Research> idComparator(ResearchHolder researchManager) {
        return Comparator.comparing(researchManager::getIdOrEmpty);
    }

    public static Comparator<Research> statusComparator(PlayerResearchHolder researchManager) {
        return Comparator.comparing(researchManager::getStatus);
    }
}
