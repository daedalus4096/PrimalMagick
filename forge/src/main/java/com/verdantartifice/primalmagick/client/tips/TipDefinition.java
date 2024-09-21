package com.verdantartifice.primalmagick.client.tips;

import java.util.Optional;
import java.util.function.BiConsumer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.keys.AbstractResearchKey;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

/**
 * Contains the definition of a game hint that can be displayed to the user in the Grimoire if
 * they've advanced sufficiently far in mod progression.
 * 
 * @author Daedalus4096
 */
public record TipDefinition(String translationKey, Optional<AbstractRequirement<?>> requirement) {
    public static Codec<TipDefinition> codec() {
        return RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("translationKey").forGetter(TipDefinition::translationKey), 
                AbstractRequirement.dispatchCodec().optionalFieldOf("requirement").forGetter(TipDefinition::requirement)
            ).apply(instance, TipDefinition::new));
    }
    
    public Component getText() {
        return Component.translatable(this.translationKey);
    }
    
    public boolean shouldShow(Player player) {
        return this.requirement.map(req -> req.isMetBy(player)).orElse(true);
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
        protected Optional<AbstractRequirement<?>> requirement;
        
        protected Builder(ResourceLocation id) {
            this.id = id;
            this.translationKey = String.join(".", "tip", id.getNamespace(), id.getPath());
            this.requirement = Optional.empty();
        }
        
        public Builder translationKey(String key) {
            this.translationKey = key;
            return this;
        }
        
        public Builder requirement(AbstractRequirement<?> requirement) {
            this.requirement = Optional.ofNullable(requirement);
            return this;
        }
        
        public Builder requiredResearch(AbstractResearchKey<?> researchKey) {
            return this.requirement(new ResearchRequirement(researchKey));
        }
        
        public Builder requiredResearch(ResourceKey<ResearchEntry> rawResearchKey) {
            return this.requiredResearch(new ResearchEntryKey(rawResearchKey));
        }
        
        public TipDefinition build() {
            return new TipDefinition(this.translationKey, this.requirement);
        }
        
        public void save(BiConsumer<ResourceLocation, TipDefinition> consumer) {
            consumer.accept(this.id, this.build());
        }
    }
}
