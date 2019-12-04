package com.verdantartifice.primalmagic.common.spells.payloads;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.common.spells.SpellProperty;

import net.minecraft.nbt.CompoundNBT;

public abstract class AbstractSpellPayload implements ISpellPayload {
    protected final Map<String, SpellProperty> properties;
    
    public AbstractSpellPayload() {
        this.properties = this.initProperties();
    }
    
    protected abstract String getPayloadType();
    
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("PayloadType", this.getPayloadType());
        for (Map.Entry<String, SpellProperty> entry : this.properties.entrySet()) {
            nbt.putInt(entry.getKey(), entry.getValue().getValue());
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        for (Map.Entry<String, SpellProperty> entry : this.properties.entrySet()) {
            entry.getValue().setValue(nbt.getInt(entry.getKey()));
        }
    }
    
    @Nonnull
    protected Map<String, SpellProperty> initProperties() {
        return new HashMap<>();
    }
    
    @Override
    public Collection<SpellProperty> getProperties() {
        return Collections.unmodifiableCollection(this.properties.values());
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
