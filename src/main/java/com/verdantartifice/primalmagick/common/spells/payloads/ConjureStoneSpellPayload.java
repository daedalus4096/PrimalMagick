package com.verdantartifice.primalmagick.common.spells.payloads;

import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

/**
 * Definition for a stone conjuration spell.  Creates a block of regular stone at the target point.
 * Most of the work is done in the base class.  Has no effect when cast by non-players.
 * 
 * @author Daedalus4096
 */
public class ConjureStoneSpellPayload extends AbstractConjureBlockSpellPayload {
    public static final String TYPE = "conjure_stone";
    protected static final AbstractRequirement<?> REQUIREMENT = new ResearchRequirement(new ResearchEntryKey(ResearchEntries.SPELL_PAYLOAD_CONJURE_STONE));
    
    public ConjureStoneSpellPayload() {
        super(Blocks.STONE.defaultBlockState());
    }
    
    public static AbstractRequirement<?> getRequirement() {
        return REQUIREMENT;
    }

    @Override
    public Source getSource() {
        return Sources.EARTH;
    }

    @Override
    public int getBaseManaCost() {
        return 25;
    }

    @Override
    public void playSounds(Level world, BlockPos origin) {
        world.playSound(null, origin, SoundsPM.ROCKSLIDE.get(), SoundSource.PLAYERS, 1.0F, 1.0F + (float)(world.random.nextGaussian() * 0.05D));
    }

    @Override
    protected String getPayloadType() {
        return TYPE;
    }
}
