package com.verdantartifice.primalmagick.common.spells.mods;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;
import com.verdantartifice.primalmagick.common.spells.SpellPropertiesPM;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;
import com.verdantartifice.primalmagick.common.spells.SpellPropertyConfiguration;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * Definition of the Quicken spell mod.  This mod causes spells to incur a shorter cooldown upon being
 * cast.  The mod's haste property determines how much the cooldown is reduced.
 * 
 * @author Daedalus4096
 */
public class QuickenSpellMod extends AbstractSpellMod<QuickenSpellMod> {
    public static final QuickenSpellMod INSTANCE = new QuickenSpellMod();
    
    public static final MapCodec<QuickenSpellMod> CODEC = MapCodec.unit(QuickenSpellMod.INSTANCE);
    public static final StreamCodec<ByteBuf, QuickenSpellMod> STREAM_CODEC = StreamCodec.unit(QuickenSpellMod.INSTANCE);
    
    public static final String TYPE = "quicken";
    protected static final AbstractRequirement<?> REQUIREMENT = new ResearchRequirement(new ResearchEntryKey(ResearchEntries.SPELL_MOD_QUICKEN));
    protected static final Supplier<List<SpellProperty>> PROPERTIES = () -> Arrays.asList(SpellPropertiesPM.HASTE.get());

    public static AbstractRequirement<?> getRequirement() {
        return REQUIREMENT;
    }
    
    public static QuickenSpellMod getInstance() {
        return INSTANCE;
    }
    
    @Override
    public SpellModType<QuickenSpellMod> getType() {
        return SpellModsPM.QUICKEN.get();
    }

    @Override
    protected List<SpellProperty> getPropertiesInner() {
        return PROPERTIES.get();
    }

    @Override
    public int getBaseManaCostModifier(SpellPropertyConfiguration properties) {
        return 0;
    }
    
    @Override
    public int getManaCostMultiplier(SpellPropertyConfiguration properties) {
        return 1 + properties.get(SpellPropertiesPM.HASTE.get());
    }

    @Override
    protected String getModType() {
        return TYPE;
    }
}
