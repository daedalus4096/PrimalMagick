package com.verdantartifice.primalmagick.common.spells.mods;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;
import com.verdantartifice.primalmagick.common.spells.SpellPropertyConfiguration;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

/**
 * Definition of an empty spell mod.  This mod has no effect and is not valid in spells.  Its only
 * purpose is to provide a selection entry in the spellcrafting altar GUI for when the player has
 * not selected either a primary or secondary mod for the spell.
 * 
 * @author Daedalus4096
 */
public class EmptySpellMod extends AbstractSpellMod<EmptySpellMod> {
    public static final EmptySpellMod INSTANCE = new EmptySpellMod();
    
    public static final MapCodec<EmptySpellMod> CODEC = MapCodec.unit(EmptySpellMod.INSTANCE);
    public static final StreamCodec<ByteBuf, EmptySpellMod> STREAM_CODEC = StreamCodec.unit(EmptySpellMod.INSTANCE);
    
    public static final String TYPE = "none";

    public EmptySpellMod() {
    }

    @Override
    public SpellModType<EmptySpellMod> getType() {
        return SpellModsPM.EMPTY.get();
    }

    @Override
    protected List<SpellProperty> getPropertiesInner() {
        return ImmutableList.of();
    }
    
    @Override
    public boolean isActive() {
        return false;
    }
    
    @Override
    protected String getModType() {
        return TYPE;
    }
    
    public static AbstractRequirement<?> getRequirement() {
        return null;
    }
    
    @Override
    public int getBaseManaCostModifier(SpellPropertyConfiguration properties) {
        return 0;
    }

    @Override
    public int getManaCostMultiplier(SpellPropertyConfiguration properties) {
        return 1;
    }
}
