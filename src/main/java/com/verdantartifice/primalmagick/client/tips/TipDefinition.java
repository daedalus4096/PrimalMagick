package com.verdantartifice.primalmagick.client.tips;

import java.util.Optional;
import java.util.function.BiConsumer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

/**
 * Contains the definition of a game hint that can be displayed to the user in the Grimoire if
 * they've advanced sufficiently far in mod progression.
 * 
 * @author Daedalus4096
 */
public record TipDefinition(String translationKey, Optional<CompoundResearchKey> requiredResearch) {
    public static final Codec<TipDefinition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("translationKey").forGetter(TipDefinition::translationKey), 
            CompoundResearchKey.CODEC.optionalFieldOf("requiredResearch").forGetter(TipDefinition::requiredResearch)
        ).apply(instance, TipDefinition::new));
    
    public Component getText() {
        return Component.translatable(this.translationKey);
    }
    
    public boolean shouldShow(Player player) {
        if (this.requiredResearch().isPresent()) {
            return this.requiredResearch().get().isKnownByStrict(player);
        } else {
            return true;
        }
    }
    
    public static Builder builder(ResourceLocation id) {
        return new Builder(id);
    }
    
    public static Builder builder(String id) {
        return new Builder(PrimalMagick.resource(id));
    }
    
    public static class Builder {
        protected final ResourceLocation id;
        protected String translationKey;
        protected Optional<CompoundResearchKey> requiredResearch;
        
        protected Builder(ResourceLocation id) {
            this.id = id;
            this.translationKey = String.join(".", "tip", id.getNamespace(), id.getPath());
            this.requiredResearch = Optional.empty();
        }
        
        public Builder translationKey(String key) {
            this.translationKey = key;
            return this;
        }
        
        public Builder requiredResearch(CompoundResearchKey researchKey) {
            this.requiredResearch = Optional.ofNullable(researchKey);
            return this;
        }
        
        public TipDefinition build() {
            return new TipDefinition(this.translationKey, this.requiredResearch);
        }
        
        public void save(BiConsumer<ResourceLocation, TipDefinition> consumer) {
            consumer.accept(this.id, this.build());
        }
    }
}
