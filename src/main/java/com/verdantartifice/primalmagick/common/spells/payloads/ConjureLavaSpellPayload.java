package com.verdantartifice.primalmagick.common.spells.payloads;

import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;

/**
 * Definition for a lava conjuration spell.  Creates a source block of lava at the designated point.
 * Works similarly to placing lava from a bucket.  Most of the work is done in the base class.  Has
 * no effect when cast by non-players.
 * 
 * @author Daedalus4096
 */
public class ConjureLavaSpellPayload extends AbstractConjureFluidSpellPayload {
    public static final String TYPE = "conjure_lava";
    protected static final AbstractRequirement<?> REQUIREMENT = new ResearchRequirement(new ResearchEntryKey(ResearchEntries.SPELL_PAYLOAD_CONJURE_LAVA));

    public ConjureLavaSpellPayload() {
        super(Fluids.LAVA);
    }

    public static AbstractRequirement<?> getRequirement() {
        return REQUIREMENT;
    }

    @Override
    public Source getSource() {
        return Sources.INFERNAL;
    }

    @Override
    public int getBaseManaCost() {
        return 200;
    }

    @Override
    public void playSounds(Level world, BlockPos origin) {
        world.playSound(null, origin, SoundEvents.BUCKET_EMPTY_LAVA, SoundSource.PLAYERS, 1.0F, 1.0F + (float)(world.random.nextGaussian() * 0.05D));
    }

    @Override
    protected String getPayloadType() {
        return TYPE;
    }
}
