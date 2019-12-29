package com.verdantartifice.primalmagic.common.spells.payloads;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.common.spells.SpellPackage;
import com.verdantartifice.primalmagic.common.spells.SpellProperty;
import com.verdantartifice.primalmagic.common.spells.mods.AmplifySpellMod;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

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
    public List<SpellProperty> getProperties() {
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
    
    public int getModdedPropertyValue(String name, SpellPackage spell) {
        int retVal = this.getPropertyValue(name);
        if (retVal > 0 && ("power".equals(name) || "duration".equals(name))) {
            AmplifySpellMod ampMod = spell.getMod(AmplifySpellMod.class, "power");
            if (ampMod != null) {
                retVal += ampMod.getPropertyValue("power");
            }
        }
        return retVal;
    }
    
    @Override
    public ITextComponent getTypeName() {
        return new TranslationTextComponent("primalmagic.spell.payload.type." + this.getPayloadType());
    }
    
    @Override
    public ITextComponent getDefaultNamePiece() {
        return new TranslationTextComponent("primalmagic.spell.payload.default_name." + this.getPayloadType());
    }
    
    @Override
    public boolean isActive() {
        return true;
    }
}
