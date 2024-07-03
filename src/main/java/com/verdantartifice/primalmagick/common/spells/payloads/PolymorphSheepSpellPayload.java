package com.verdantartifice.primalmagick.common.spells.payloads;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;

/**
 * Definition of a polymorph spell.  Temporarily replaces the target living, non-player, non-boss
 * entity with a sheep.  The length of the replacement scales with the payload's duration
 * property.  NBT data of the original entity is preserved for the swap back.  Has no effect on
 * blocks.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.common.misc.EntitySwapper}
 */
public class PolymorphSheepSpellPayload extends AbstractPolymorphSpellPayload<PolymorphSheepSpellPayload> {
    public static final PolymorphSheepSpellPayload INSTANCE = new PolymorphSheepSpellPayload();
    
    public static final MapCodec<PolymorphSheepSpellPayload> CODEC = MapCodec.unit(PolymorphSheepSpellPayload.INSTANCE);
    public static final StreamCodec<ByteBuf, PolymorphSheepSpellPayload> STREAM_CODEC = StreamCodec.unit(PolymorphSheepSpellPayload.INSTANCE);
    
    public static final String TYPE = "polymorph_sheep";
    protected static final AbstractRequirement<?> REQUIREMENT = new ResearchRequirement(new ResearchEntryKey(ResearchEntries.SPELL_PAYLOAD_POLYMORPH_SHEEP));

    public static AbstractRequirement<?> getRequirement() {
        return REQUIREMENT;
    }
    
    public static PolymorphSheepSpellPayload getInstance() {
        return INSTANCE;
    }
    
    @Override
    public SpellPayloadType<PolymorphSheepSpellPayload> getType() {
        return SpellPayloadsPM.POLYMORPH_SHEEP.get();
    }

    @Override
    protected String getPayloadType() {
        return TYPE;
    }

    @Override
    protected EntityType<?> getNewEntityType() {
        return EntityType.SHEEP;
    }

    @Override
    protected SoundEvent getCastSoundEvent() {
        return SoundEvents.SHEEP_AMBIENT;
    }
}
