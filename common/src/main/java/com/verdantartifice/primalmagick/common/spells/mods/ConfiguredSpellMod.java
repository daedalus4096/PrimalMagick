package com.verdantartifice.primalmagick.common.spells.mods;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.spells.AbstractConfiguredSpellComponent;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;
import com.verdantartifice.primalmagick.common.spells.SpellPropertyConfiguration;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.HashMap;
import java.util.Map;

public class ConfiguredSpellMod<T extends AbstractSpellMod<?>> extends AbstractConfiguredSpellComponent<T> {
    public ConfiguredSpellMod(T mod) {
        super(mod);
    }
    
    public ConfiguredSpellMod(T mod, Map<SpellProperty, Integer> configuredProperties) {
        super(mod, configuredProperties);
    }
    
    public ConfiguredSpellMod(T mod, SpellPropertyConfiguration configuredProperties) {
        super(mod, configuredProperties);
    }
    
    @SuppressWarnings("unchecked")
    public static Codec<ConfiguredSpellMod<?>> codec() {
        return RecordCodecBuilder.create(instance -> instance.group(
                AbstractSpellMod.dispatchCodec().fieldOf("mod").forGetter(ConfiguredSpellMod::getComponent),
                SpellPropertyConfiguration.CODEC.fieldOf("properties").forGetter(csc -> csc.configuredProperties)
            ).apply(instance, ConfiguredSpellMod::new));
    }
    
    @SuppressWarnings("unchecked")
    public static StreamCodec<RegistryFriendlyByteBuf, ConfiguredSpellMod<?>> streamCodec() {
        return StreamCodec.composite(
                AbstractSpellMod.dispatchStreamCodec(),
                ConfiguredSpellMod::getComponent,
                SpellPropertyConfiguration.STREAM_CODEC,
                csc -> csc.configuredProperties,
                ConfiguredSpellMod::new);
    }
    
    /**
     * Get the additive modifier to be applied to the spell mod's package's base cost.
     * 
     * @return the additive modifier for the spell package's cost
     */
    public int getBaseManaCostModifier() {
        return this.component.getBaseManaCostModifier(this.configuredProperties);
    }
    
    /**
     * Get the multiplicative modifier to be applied to the spell mod's package's total cost.
     * 
     * @return the multiplicative modifier for the spell package's cost
     */
    public int getManaCostMultiplier() {
        return this.component.getManaCostMultiplier(this.configuredProperties);
    }
    
    public static Builder builder(SpellPackage.Builder parent) {
        return new Builder(parent);
    }
    
    public static class Builder {
        protected final SpellPackage.Builder parent;
        protected AbstractSpellMod<?> mod = EmptySpellMod.INSTANCE;
        protected final Map<SpellProperty, Integer> properties = new HashMap<>();
        
        public Builder(SpellPackage.Builder parent) {
            this.parent = parent;
        }
        
        public Builder type(AbstractSpellMod<?> mod) {
            this.mod = mod;
            return this;
        }
        
        public Builder with(SpellProperty property, int value) {
            this.properties.put(property, value);
            return this;
        }
        
        private void validate() {
            if (this.mod == null) {
                throw new IllegalStateException("No type specified for spell mod");
            }
        }
        
        public ConfiguredSpellMod<?> build() {
            this.validate();
            return new ConfiguredSpellMod<>(this.mod, this.properties);
        }
        
        public SpellPackage.Builder end() {
            return this.parent;
        }
    }
}
