package com.verdantartifice.primalmagick.common.spells.vehicles;

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

public class ConfiguredSpellVehicle<T extends AbstractSpellVehicle<?>> extends AbstractConfiguredSpellComponent<T> {
    public ConfiguredSpellVehicle(T vehicle) {
        super(vehicle);
    }
    
    public ConfiguredSpellVehicle(T vehicle, Map<SpellProperty, Integer> configuredProperties) {
        super(vehicle, configuredProperties);
    }
    
    public ConfiguredSpellVehicle(T vehicle, SpellPropertyConfiguration configuredProperties) {
        super(vehicle, configuredProperties);
    }
    
    @SuppressWarnings("unchecked")
    public static Codec<ConfiguredSpellVehicle<?>> codec() {
        return RecordCodecBuilder.create(instance -> instance.group(
                AbstractSpellVehicle.dispatchCodec().fieldOf("vehicle").forGetter(ConfiguredSpellVehicle::getComponent),
                SpellPropertyConfiguration.CODEC.fieldOf("properties").forGetter(csc -> csc.configuredProperties)
            ).apply(instance, ConfiguredSpellVehicle::new));
    }
    
    @SuppressWarnings("unchecked")
    public static StreamCodec<RegistryFriendlyByteBuf, ConfiguredSpellVehicle<?>> streamCodec() {
        return StreamCodec.composite(
                AbstractSpellVehicle.dispatchStreamCodec(),
                ConfiguredSpellVehicle::getComponent,
                SpellPropertyConfiguration.STREAM_CODEC,
                csc -> csc.configuredProperties,
                ConfiguredSpellVehicle::new);
    }
    
    /**
     * Get the additive modifier to be applied to the spell vehicle's package's base cost.
     * 
     * @return the additive modifier for the spell package's cost
     */
    public int getBaseManaCostModifier() {
        return this.component.getBaseManaCostModifier(this.configuredProperties);
    }
    
    /**
     * Get the multiplicative modifier to be applied to the spell vehicle's package's total cost.
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
        protected AbstractSpellVehicle<?> vehicle = EmptySpellVehicle.INSTANCE;
        protected final Map<SpellProperty, Integer> properties = new HashMap<>();
        
        public Builder(SpellPackage.Builder parent) {
            this.parent = parent;
        }
        
        public Builder type(AbstractSpellVehicle<?> vehicle) {
            this.vehicle = vehicle;
            return this;
        }
        
        public Builder with(SpellProperty property, int value) {
            this.properties.put(property, value);
            return this;
        }
        
        private void validate() {
            if (this.vehicle == null) {
                throw new IllegalStateException("No type specified for spell vehicle");
            }
        }
        
        public ConfiguredSpellVehicle<?> build() {
            this.validate();
            return new ConfiguredSpellVehicle<>(this.vehicle, this.properties);
        }
        
        public SpellPackage.Builder end() {
            return this.parent;
        }
    }
}
