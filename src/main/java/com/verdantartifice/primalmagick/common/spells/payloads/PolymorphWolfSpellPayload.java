package com.verdantartifice.primalmagick.common.spells.payloads;

import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.research.ResearchNames;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;

/**
 * Definition of a polymorph spell.  Temporarily replaces the target living, non-player, non-boss
 * entity with a neutral wolf.  The length of the replacement scales with the payload's duration
 * property.  NBT data of the original entity is preserved for the swap back.  Has no effect on
 * blocks.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.common.misc.EntitySwapper}
 */
public class PolymorphWolfSpellPayload extends AbstractPolymorphSpellPayload {
    public static final String TYPE = "polymorph";
    protected static final CompoundResearchKey RESEARCH = ResearchNames.SPELL_PAYLOAD_POLYMORPH.get().compoundKey();

    public PolymorphWolfSpellPayload() {
        super();
    }
    
    public PolymorphWolfSpellPayload(int duration) {
        super(duration);
    }
    
    public static CompoundResearchKey getResearch() {
        return RESEARCH;
    }
    
    @Override
    protected String getPayloadType() {
        return TYPE;
    }

    @Override
    protected EntityType<?> getNewEntityType() {
        return EntityType.WOLF;
    }

    @Override
    protected SoundEvent getCastSoundEvent() {
        return SoundEvents.WOLF_AMBIENT;
    }
}
