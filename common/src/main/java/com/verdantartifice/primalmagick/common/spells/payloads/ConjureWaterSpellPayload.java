package com.verdantartifice.primalmagick.common.spells.payloads;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;
import com.verdantartifice.primalmagick.common.spells.SpellPropertyConfiguration;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;

import java.util.List;

/**
 * Definition for a water conjuration spell.  Creates a source block of water at the designated point.
 * Works similarly to placing water from a bucket.  Most of the work is done in the base class.  Has 
 * no effect when cast by non-players.
 * 
 * @author Daedalus4096
 */
public class ConjureWaterSpellPayload extends AbstractConjureFluidSpellPayload<ConjureWaterSpellPayload> {
    public static final ConjureWaterSpellPayload INSTANCE = new ConjureWaterSpellPayload();
    
    public static final MapCodec<ConjureWaterSpellPayload> CODEC = MapCodec.unit(ConjureWaterSpellPayload.INSTANCE);
    public static final StreamCodec<ByteBuf, ConjureWaterSpellPayload> STREAM_CODEC = StreamCodec.unit(ConjureWaterSpellPayload.INSTANCE);
    
    public static final String TYPE = "conjure_water";
    protected static final AbstractRequirement<?> REQUIREMENT = new ResearchRequirement(new ResearchEntryKey(ResearchEntries.SPELL_PAYLOAD_CONJURE_WATER));

    public ConjureWaterSpellPayload() {
        super(Fluids.WATER);
    }
    
    public static AbstractRequirement<?> getRequirement() {
        return REQUIREMENT;
    }

    public static ConjureWaterSpellPayload getInstance() {
        return INSTANCE;
    }
    
    @Override
    public SpellPayloadType<ConjureWaterSpellPayload> getType() {
        return SpellPayloadsPM.CONJURE_WATER.get();
    }

    @Override
    protected List<SpellProperty> getPropertiesInner() {
        return ImmutableList.of();
    }

    @Override
    public Source getSource() {
        return Sources.SEA;
    }

    @Override
    public int getBaseManaCost(SpellPropertyConfiguration properties) {
        return 25;
    }

    @Override
    public void playSounds(Level world, BlockPos origin) {
        world.playSound(null, origin, SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS, 1.0F, 1.0F + (float)(world.random.nextGaussian() * 0.05D));
    }

    @Override
    protected String getPayloadType() {
        return TYPE;
    }
}
