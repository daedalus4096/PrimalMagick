package com.verdantartifice.primalmagick.common.spells.payloads;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.research.ResearchNames;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

/**
 * Definition for a consecration spell.  Creates a two-high field of holy energy at the target location
 * which prevents entry by non-player mobs.  It also heals and restores hunger to players.  Has no effect
 * when cast by non-players.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.common.blocks.misc.ConsecrationFieldBlock}
 */
public class ConsecrateSpellPayload extends AbstractConjureBlockSpellPayload {
    public static final String TYPE = "consecrate";
    protected static final CompoundResearchKey RESEARCH = ResearchNames.SPELL_PAYLOAD_CONSECRATE.get().compoundKey();

    public ConsecrateSpellPayload() {
        super(BlocksPM.CONSECRATION_FIELD.get().defaultBlockState(), 2);
    }
    
    public static CompoundResearchKey getResearch() {
        return RESEARCH;
    }
    
    @Override
    public Source getSource() {
        return Source.HALLOWED;
    }

    @Override
    public int getBaseManaCost() {
        return 25;
    }

    @Override
    public void playSounds(Level world, BlockPos origin) {
        world.playSound(null, origin, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.0F, 1.0F + (float)(world.random.nextGaussian() * 0.05D));
    }

    @Override
    protected String getPayloadType() {
        return TYPE;
    }
}
