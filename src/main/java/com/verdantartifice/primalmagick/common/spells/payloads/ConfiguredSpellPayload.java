package com.verdantartifice.primalmagick.common.spells.payloads;

import java.util.HashMap;
import java.util.Map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.spells.AbstractConfiguredSpellComponent;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;
import com.verdantartifice.primalmagick.common.spells.SpellPropertyConfiguration;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class ConfiguredSpellPayload<T extends AbstractSpellPayload<?>> extends AbstractConfiguredSpellComponent<T> {
    public ConfiguredSpellPayload(T payload) {
        super(payload);
    }
    
    public ConfiguredSpellPayload(T payload, Map<SpellProperty, Integer> configuredProperties) {
        super(payload, configuredProperties);
    }
    
    public ConfiguredSpellPayload(T payload, SpellPropertyConfiguration configuredProperties) {
        super(payload, configuredProperties);
    }
    
    @SuppressWarnings("unchecked")
    public static Codec<ConfiguredSpellPayload<?>> codec() {
        return RecordCodecBuilder.create(instance -> instance.group(
                AbstractSpellPayload.dispatchCodec().fieldOf("payload").forGetter(ConfiguredSpellPayload::getComponent),
                SpellPropertyConfiguration.CODEC.fieldOf("properties").forGetter(csc -> csc.configuredProperties)
            ).apply(instance, ConfiguredSpellPayload::new));
    }
    
    @SuppressWarnings("unchecked")
    public static StreamCodec<RegistryFriendlyByteBuf, ConfiguredSpellPayload<?>> streamCodec() {
        return StreamCodec.composite(
                AbstractSpellPayload.dispatchStreamCodec(),
                ConfiguredSpellPayload::getComponent,
                SpellPropertyConfiguration.STREAM_CODEC,
                csc -> csc.configuredProperties,
                ConfiguredSpellPayload::new);
    }

    /**
     * Get the base cost of the spell payload.
     * 
     * @return the base cost of the spell payload
     */
    public int getBaseManaCost() {
        return this.component.getBaseManaCost(this.configuredProperties);
    }
    
    public static Builder builder(SpellPackage.Builder parent) {
        return new Builder(parent);
    }
    
    public static class Builder {
        protected final SpellPackage.Builder parent;
        protected AbstractSpellPayload<?> payload = EmptySpellPayload.INSTANCE;
        protected final Map<SpellProperty, Integer> properties = new HashMap<>();
        
        public Builder(SpellPackage.Builder parent) {
            this.parent = parent;
        }
        
        public Builder type(AbstractSpellPayload<?> payload) {
            this.payload = payload;
            return this;
        }
        
        public Builder with(SpellProperty property, int value) {
            this.properties.put(property, value);
            return this;
        }
        
        private void validate() {
            if (this.payload == null) {
                throw new IllegalStateException("No type specified for spell payload");
            }
        }
        
        public ConfiguredSpellPayload<?> build() {
            this.validate();
            return new ConfiguredSpellPayload<>(this.payload, this.properties);
        }
        
        public SpellPackage.Builder end() {
            return this.parent;
        }
    }
}
