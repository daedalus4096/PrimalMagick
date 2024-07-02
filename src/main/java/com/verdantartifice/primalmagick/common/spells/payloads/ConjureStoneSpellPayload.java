package com.verdantartifice.primalmagick.common.spells.payloads;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;
import com.verdantartifice.primalmagick.common.spells.SpellPropertyConfiguration;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

/**
 * Definition for a stone conjuration spell.  Creates a block of regular stone at the target point.
 * Most of the work is done in the base class.  Has no effect when cast by non-players.
 * 
 * @author Daedalus4096
 */
public class ConjureStoneSpellPayload extends AbstractConjureBlockSpellPayload<ConjureStoneSpellPayload> {
    public static final ConjureStoneSpellPayload INSTANCE = new ConjureStoneSpellPayload();
    
    public static final MapCodec<ConjureStoneSpellPayload> CODEC = MapCodec.unit(ConjureStoneSpellPayload.INSTANCE);
    public static final StreamCodec<ByteBuf, ConjureStoneSpellPayload> STREAM_CODEC = StreamCodec.unit(ConjureStoneSpellPayload.INSTANCE);
    
    public static final String TYPE = "conjure_stone";
    protected static final AbstractRequirement<?> REQUIREMENT = new ResearchRequirement(new ResearchEntryKey(ResearchEntries.SPELL_PAYLOAD_CONJURE_STONE));
    
    public ConjureStoneSpellPayload() {
        super(Blocks.STONE.defaultBlockState());
    }
    
    public static AbstractRequirement<?> getRequirement() {
        return REQUIREMENT;
    }

    @Override
    public SpellPayloadType<ConjureStoneSpellPayload> getType() {
        return SpellPayloadsPM.CONJURE_STONE.get();
    }

    @Override
    protected List<SpellProperty> getPropertiesInner() {
        return ImmutableList.of();
    }

    @Override
    public Source getSource() {
        return Sources.EARTH;
    }

    @Override
    public int getBaseManaCost(SpellPropertyConfiguration properties) {
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
