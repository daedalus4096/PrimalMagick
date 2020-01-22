package com.verdantartifice.primalmagic.common.spells.mods;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.common.spells.SpellPackage;
import com.verdantartifice.primalmagic.common.spells.SpellProperty;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Base class for a spell mod.  Handles property management and serialization.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractSpellMod implements ISpellMod {
    protected final Map<String, SpellProperty> properties;

    public AbstractSpellMod() {
        this.properties = this.initProperties();
    }
    
    /**
     * Get the type name for this spell mod.
     * 
     * @return the type name for this spell mod
     */
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

    /**
     * Initialize the property map for this spell mod.  Should create a maximum of two properties.
     * 
     * @return a map of property names to spell properties
     */
    @Nonnull
    protected Map<String, SpellProperty> initProperties() {
        return new HashMap<>();
    }
    
    @Override
    public boolean isActive() {
        return true;
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
    
    public int getModdedPropertyValue(String name, SpellPackage spell) {
        int retVal = this.getPropertyValue(name);
        if (retVal > 0 && ("power".equals(name) || "duration".equals(name))) {
            AmplifySpellMod ampMod = spell.getMod(AmplifySpellMod.class, "power");
            if (ampMod != null) {
                // For power or duration properties greater than zero, increase the total result by
                // the power of any attached Amplify spell mod
                retVal += ampMod.getPropertyValue("power");
            }
        }
        return retVal;
    }
    
    @Override
    public ITextComponent getTypeName() {
        return new TranslationTextComponent("primalmagic.spell.mod.type." + this.getModType());
    }
    
    @Override
    public ITextComponent getDefaultNamePiece() {
        return new TranslationTextComponent("primalmagic.spell.mod.default_name." + this.getModType());
    }
}
