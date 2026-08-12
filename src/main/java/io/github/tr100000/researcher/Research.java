package io.github.tr100000.researcher;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.tr100000.researcher.api.PlayerResearchHolder;
import io.github.tr100000.researcher.api.ResearchHolder;
import io.github.tr100000.researcher.api.ResearchReward;
import io.github.tr100000.trutils.api.gui.Icon;
import io.github.tr100000.trutils.api.gui.TextureIcon;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.triggers.CriterionTrigger;
import net.minecraft.network.FriendlyByteBuf;
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
import net.minecraft.util.ExtraCodecs;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public record Research(
        Optional<Component> titleText,
        Optional<Component> descriptionText,
        ResearchCriterion<?> trigger,
        List<Identifier> prerequisiteIds,
        List<Identifier> recipeUnlocks,
        List<ResearchReward> rewards,
        Icon display,
        SizeSettings sizeSettings) {
    public static final Icon DEFAULT_ICON = new TextureIcon(ModUtils.id("textures/research/default.png"));

    public static final Codec<Research> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
                ComponentSerialization.CODEC.optionalFieldOf("title").forGetter(Research::titleText),
                ComponentSerialization.CODEC.optionalFieldOf("description").forGetter(Research::descriptionText),
                ResearchCriterion.CODEC.optionalFieldOf("to_unlock", ResearchCriterion.IMPOSSIBLE).forGetter(Research::trigger),
                Identifier.CODEC.listOf().optionalFieldOf("prerequisites", Collections.emptyList()).forGetter(Research::prerequisiteIds),
                Identifier.CODEC.listOf().optionalFieldOf("recipe_unlocks", Collections.emptyList()).forGetter(Research::recipeUnlocks),
                ResearchReward.CODEC.listOf().optionalFieldOf("rewards", Collections.emptyList()).forGetter(Research::rewards),
                Icon.CODEC.optionalFieldOf("display", DEFAULT_ICON).forGetter(Research::display),
                SizeSettings.CODEC.optionalFieldOf("size", SizeSettings.DEFAULT).forGetter(Research::sizeSettings)
        ).apply(instance, Research::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, Research> STREAM_CODEC = StreamCodec.composite(
            ComponentSerialization.OPTIONAL_STREAM_CODEC, Research::titleText,
            ComponentSerialization.OPTIONAL_STREAM_CODEC, Research::descriptionText,
            ResearchCriterion.STREAM_CODEC, Research::trigger,
            Identifier.STREAM_CODEC.apply(ByteBufCodecs.list()), Research::prerequisiteIds,
            Identifier.STREAM_CODEC.apply(ByteBufCodecs.list()), Research::recipeUnlocks,
            ResearchReward.STREAM_CODEC.apply(ByteBufCodecs.list()), Research::rewards,
            Icon.PACKET_CODEC, Research::display,
            SizeSettings.STREAM_CODEC, Research::sizeSettings,
            Research::new
    );

    @Deprecated(forRemoval = true, since = "0.4.1")
    public Research(
            Optional<Component> titleText,
            Optional<Component> descriptionText,
            ResearchCriterion<?> trigger,
            List<Identifier> prerequisiteIds,
            List<Identifier> recipeUnlocks,
            List<ResearchReward> rewards,
            Icon display
    ) {
        this(titleText, descriptionText, trigger, prerequisiteIds, recipeUnlocks, rewards, display, SizeSettings.DEFAULT);
    }

    @Contract(pure = true)
    public String getTranslationKey(ResearchHolder researchManager) {
        return researchManager.getIdOrEmpty(this).toLanguageKey(ResearchManager.PATH);
    }

    @Contract(pure = true)
    public String getDescriptionTranslationKey(ResearchHolder researchManager) {
        return researchManager.getIdOrEmpty(this).toLanguageKey(ResearchManager.PATH, "desc");
    }

    @Contract(pure = true)
    public Component getTitle(ResearchHolder researchManager) {
        return titleText.orElseGet(() -> Component.translatable(getTranslationKey(researchManager)));
    }

    @Contract(pure = true)
    public Component getDescription(ResearchHolder researchManager) {
        return descriptionText.orElseGet(() -> Component.translatableWithFallback(getDescriptionTranslationKey(researchManager), ""));
    }

    @Contract(pure = true)
    @UnmodifiableView
    public List<Research> prerequisites(ResearchHolder researchManager) {
        return prerequisiteIds().stream().map(researchManager::get).toList();
    }

    @Contract(pure = true)
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

    @Contract(pure = true)
    public CriterionTrigger<?> getTrigger() {
        return trigger().criterion().trigger();
    }

    @Contract(pure = true)
    public CriterionTriggerInstance getConditions() {
        return trigger().criterion().triggerInstance();
    }

    @Contract(pure = true)
    public static Comparator<Research> idComparator(ResearchHolder researchManager) {
        return Comparator.comparing(researchManager::getIdOrEmpty);
    }

    @Contract(pure = true)
    public static Comparator<Research> statusComparator(PlayerResearchHolder researchManager) {
        return Comparator.comparing(researchManager::getStatus);
    }

    public record SizeSettings(Size normalSize, Size selectedSize) {
        public static final SizeSettings DEFAULT = new SizeSettings(new Size(48), new Size(64));

        public static final Codec<SizeSettings> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Size.CODEC.fieldOf("normal").forGetter(SizeSettings::normalSize),
                Size.CODEC.fieldOf("selected").forGetter(SizeSettings::selectedSize)
        ).apply(instance, SizeSettings::new));

        public static final StreamCodec<FriendlyByteBuf, SizeSettings> STREAM_CODEC = StreamCodec.composite(
                Size.STREAM_CODEC, SizeSettings::normalSize,
                Size.STREAM_CODEC, SizeSettings::selectedSize,
                SizeSettings::new
        );
    }

    public record Size(int width, int height) {
        public static final Codec<Size> CODEC = Codec.either(
                RecordCodecBuilder.<Size>create(instance -> instance.group(
                        ExtraCodecs.POSITIVE_INT.fieldOf("width").forGetter(Size::width),
                        ExtraCodecs.POSITIVE_INT.fieldOf("height").forGetter(Size::height)
                ).apply(instance, Size::new)),
                ExtraCodecs.POSITIVE_INT
        ).xmap(
                either -> either.map(Function.identity(), Size::new),
                size -> size.width == size.height ? Either.right(size.width) : Either.left(size)
        );

        public static final StreamCodec<FriendlyByteBuf, Size> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT, Size::width,
                ByteBufCodecs.INT, Size::height,
                Size::new
        );

        public Size(int size) {
            this(size, size);
        }
    }
}
