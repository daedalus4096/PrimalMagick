package com.verdantartifice.primalmagic.common.spells.mods;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.common.spells.SpellProperty;

import net.minecraft.nbt.CompoundNBT;

public abstract class AbstractSpellMod implements ISpellMod {
    protected final Map<String, SpellProperty> properties;

    public AbstractSpellMod() {
        this.properties = this.initProperties();
    }
    
    protected abstract String getModType();
    
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("ModType", this.getModType());
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
