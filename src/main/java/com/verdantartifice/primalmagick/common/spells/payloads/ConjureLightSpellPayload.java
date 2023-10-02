package com.verdantartifice.primalmagick.common.spells.payloads;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.blocks.misc.GlowFieldBlock;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.research.ResearchNames;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

/**
 * Definition for a light conjuration spell.  Creates a fading glow field at the designated point.
 * Most of the work is done in the base class.  Has no effect when cast by non-players.
 * 
 * @author Daedalus4096
 */
public class ConjureLightSpellPayload extends AbstractConjureBlockSpellPayload {
    public static final String TYPE = "conjure_light";
    protected static final CompoundResearchKey RESEARCH = ResearchNames.SPELL_PAYLOAD_CONJURE_LIGHT.get().compoundKey();
    
    public ConjureLightSpellPayload() {
        super(BlocksPM.GLOW_FIELD.get().defaultBlockState().setValue(GlowFieldBlock.FADING, Boolean.TRUE));
    }

    public static CompoundResearchKey getResearch() {
        return RESEARCH;
    }

    @Override
    public Source getSource() {
        return Source.SUN;
    }

    @Override
    public int getBaseManaCost() {
        return 10;
    }

    @Override
    public void playSounds(Level world, BlockPos origin) {
        world.playSound(null, origin, SoundEvents.FLINTANDSTEEL_USE, SoundSource.PLAYERS, 1.0F, 1.0F + (float)(world.random.nextGaussian() * 0.05D));
    }

    @Override
    protected String getPayloadType() {
        return TYPE;
    }
}
