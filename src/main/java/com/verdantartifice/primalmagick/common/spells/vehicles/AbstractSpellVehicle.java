package com.verdantartifice.primalmagick.common.spells.vehicles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.common.spells.SpellProperty;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

/**
 * Base class for a spell vehicle.  Handles property management and serialization.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractSpellVehicle implements ISpellVehicle {
    protected final Map<String, SpellProperty> properties;
    
    public AbstractSpellVehicle() {
        this.properties = this.initProperties();
    }

    /**
     * Get the type name for this spell vehicle.
     * 
     * @return the type name for this spell vehicle
     */
    protected abstract String getVehicleType();

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("VehicleType", this.getVehicleType());
        for (Map.Entry<String, SpellProperty> entry : this.properties.entrySet()) {
            nbt.putInt(entry.getKey(), entry.getValue().getValue());
        }
        return nbt;
    }
    
    @Override
    public void deserializeNBT(CompoundTag nbt) {
        for (Map.Entry<String, SpellProperty> entry : this.properties.entrySet()) {
            entry.getValue().setValue(nbt.getInt(entry.getKey()));
        }
    }
    
    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public Component getTypeName() {
        return Component.translatable("spells.primalmagick.vehicle." + this.getVehicleType() + ".type");
    }
    
    @Override
    public Component getDefaultNamePiece() {
        return Component.translatable("spells.primalmagick.vehicle." + this.getVehicleType() + ".default_name");
    }
    
    @Override
    public int getBaseManaCostModifier() {
        // No change by default
        return 0;
    }

    @Override
    public int getManaCostMultiplier() {
        // No multiplication by default
        return 1;
    }

    /**
     * Initialize the property map for this spell vehicle.  Should create a maximum of two properties.
     * 
     * @return a map of property names to spell properties
     */
    @Nonnull
    protected Map<String, SpellProperty> initProperties() {
        return new HashMap<>();
    }
    
    @Override
    public List<SpellProperty> getProperties() {
        // Sort properties by their display names
        return this.properties.values().stream().sorted((p1, p2) -> p1.getName().compareTo(p2.getName())).collect(Collectors.toList());
    }

    @Override
    public SpellProperty getProperty(String name) {
        return this.properties.get(name);
    }

    @Override
    public int getPropertyValue(String name) {
        return this.properties.containsKey(name) ? this.properties.get(name).getValue() : 0;
    }
}
