package com.verdantartifice.primalmagick.common.spells.mods;

import java.util.Arrays;
import java.util.List;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.SpellPropertiesPM;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;
import com.verdantartifice.primalmagick.common.spells.SpellPropertyConfiguration;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

/**
 * Definition of the Amplify spell mod.  This mod increases the effective value of power and duration
 * properties of each other component in the spell package by its own power property value.
 * 
 * @author Daedalus4096
 */
public class AmplifySpellMod extends AbstractSpellMod<AmplifySpellMod> {
    public static final AmplifySpellMod INSTANCE = new AmplifySpellMod();
    
    public static final MapCodec<AmplifySpellMod> CODEC = MapCodec.unit(AmplifySpellMod.INSTANCE);
    public static final StreamCodec<ByteBuf, AmplifySpellMod> STREAM_CODEC = StreamCodec.unit(AmplifySpellMod.INSTANCE);
    
    public static final String TYPE = "amplify";
    protected static final AbstractRequirement<?> REQUIREMENT = new ResearchRequirement(new ResearchEntryKey(ResearchEntries.SPELL_MOD_AMPLIFY));
    protected static final List<SpellProperty> PROPERTIES = Arrays.asList(SpellPropertiesPM.AMPLIFY_POWER.get());

    public static AbstractRequirement<?> getRequirement() {
        return REQUIREMENT;
    }
    
    public static AmplifySpellMod getInstance() {
        return INSTANCE;
    }
    
    @Override
    protected List<SpellProperty> getPropertiesInner() {
        return PROPERTIES;
    }

    @Override
    public int getBaseManaCostModifier(SpellPropertyConfiguration properties) {
        return 0;
    }
    
    @Override
    public int getManaCostMultiplier(SpellPropertyConfiguration properties) {
        return 1 + properties.getOrDefault(SpellPropertiesPM.AMPLIFY_POWER.get(), 0);
    }

    @Override
    protected String getModType() {
        return TYPE;
    }
    
    @Override
    public int getModdedPropertyValue(SpellProperty property, SpellPackage spell, ItemStack spellSource) {
        // Don't amplify self or take amplification from wand enchantments
        return spell.getMod(this.getType()).orElseThrow().getPropertyValue(property);
    }

    @Override
    public SpellModType<AmplifySpellMod> getType() {
        return SpellModsPM.AMPLIFY.get();
    }
}
