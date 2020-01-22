package com.verdantartifice.primalmagic.common.spells.mods;

import java.util.Map;

import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;
import com.verdantartifice.primalmagic.common.spells.SpellProperty;

/**
 * Definition of the Amplify spell mod.  This mod increases the effective value of power and duration
 * properties of each other component in the spell package by its own power property value.
 * 
 * @author Daedalus4096
 */
public class AmplifySpellMod extends AbstractSpellMod {
    public static final String TYPE = "amplify";
    protected static final CompoundResearchKey RESEARCH = CompoundResearchKey.from(SimpleResearchKey.parse("SPELL_MOD_AMPLIFY"));

    public AmplifySpellMod() {
        super();
    }
    
    public AmplifySpellMod(int power) {
        super();
        this.getProperty("power").setValue(power);
    }
    
    public static CompoundResearchKey getResearch() {
        return RESEARCH;
    }
    
    @Override
    protected Map<String, SpellProperty> initProperties() {
        Map<String, SpellProperty> propMap = super.initProperties();
        propMap.put("power", new SpellProperty("power", "primalmagic.spell.property.power", 1, 5));
        return propMap;
    }
    
    @Override
    public int getBaseManaCostModifier() {
        return 0;
    }
    
    @Override
    public int getManaCostMultiplier() {
        return 1 + this.getPropertyValue("power");
    }

    @Override
    protected String getModType() {
        return TYPE;
    }
    
    @Override
    public int getModdedPropertyValue(String name, SpellPackage spell) {
        // Don't amplify self
        return this.getPropertyValue(name);
    }
}
