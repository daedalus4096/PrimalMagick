package com.verdantartifice.primalmagick.common.spells.mods;

import java.util.Map;

import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;

import net.minecraft.world.item.ItemStack;

/**
 * Definition of the Amplify spell mod.  This mod increases the effective value of power and duration
 * properties of each other component in the spell package by its own power property value.
 * 
 * @author Daedalus4096
 */
public class AmplifySpellMod extends AbstractSpellMod {
    public static final String TYPE = "amplify";
    protected static final AbstractRequirement<?> REQUIREMENT = new ResearchRequirement(new ResearchEntryKey(ResearchEntries.SPELL_MOD_AMPLIFY));

    public AmplifySpellMod() {
        super();
    }
    
    public AmplifySpellMod(int power) {
        super();
        this.getProperty("power").setValue(power);
    }
    
    public static AbstractRequirement<?> getRequirement() {
        return REQUIREMENT;
    }
    
    @Override
    protected Map<String, SpellProperty> initProperties() {
        Map<String, SpellProperty> propMap = super.initProperties();
        propMap.put("power", new SpellProperty("power", "spells.primalmagick.property.power", 1, 5));
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
    public int getModdedPropertyValue(String name, SpellPackage spell, ItemStack spellSource) {
        // Don't amplify self or take amplification from wand enchantments
        return this.getPropertyValue(name);
    }
}
