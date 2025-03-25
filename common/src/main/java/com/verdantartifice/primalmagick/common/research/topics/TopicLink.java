package com.verdantartifice.primalmagick.common.research.topics;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

/**
 * Record that describes a link to a particular topic for use in Grimoire navigation.
 *
 * @author Daedalus4096
 */
public record TopicLink(AbstractResearchTopic<?> target, Optional<String> textTranslationKey) {
    public static Codec<TopicLink> codec() {
        return RecordCodecBuilder.create(instance -> instance.group(
            AbstractResearchTopic.dispatchCodec().fieldOf("target").forGetter(TopicLink::target),
            Codec.STRING.optionalFieldOf("textTranslationKey").forGetter(TopicLink::textTranslationKey)
        ).apply(instance, TopicLink::new));
    }

    public static StreamCodec<RegistryFriendlyByteBuf, TopicLink> streamCodec() {
        return StreamCodec.composite(
            AbstractResearchTopic.dispatchStreamCodec(), TopicLink::target,
            ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8), TopicLink::textTranslationKey,
            TopicLink::new
        );
    }

    public static Builder builder(@NotNull AbstractResearchTopic<?> target) {
        return new Builder(target);
    }

    public Component getDisplayText() {
        return Component.translatable(this.textTranslationKey().orElse("label.primalmagick.topic_link.text.default"));
    }

    public static class Builder {
        protected final AbstractResearchTopic<?> target;
        protected Optional<String> textTranslationKey = Optional.empty();

        public Builder(@NotNull AbstractResearchTopic<?> target) {
            this.target = Objects.requireNonNull(target);
        }

        public Builder textTranslationKey(@NotNull String translationKey) {
            this.textTranslationKey = Optional.of(translationKey);
            return this;
        }

        private void validate() {
            if (this.textTranslationKey.isPresent() && this.textTranslationKey.get().isBlank()) {
                throw new IllegalStateException("textTranslationKey cannot be blank");
            }
        }

        public TopicLink build() {
            this.validate();
            return new TopicLink(this.target, this.textTranslationKey);
        }
    }
}
