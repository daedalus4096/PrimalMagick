package com.verdantartifice.primalmagick.common.spells.payloads;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.blocks.misc.GlowFieldBlock;
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

import java.util.List;

/**
 * Definition for a light conjuration spell.  Creates a fading glow field at the designated point.
 * Most of the work is done in the base class.  Has no effect when cast by non-players.
 * 
 * @author Daedalus4096
 */
public class ConjureLightSpellPayload extends AbstractConjureBlockSpellPayload<ConjureLightSpellPayload> {
    public static final ConjureLightSpellPayload INSTANCE = new ConjureLightSpellPayload();
    
    public static final MapCodec<ConjureLightSpellPayload> CODEC = MapCodec.unit(ConjureLightSpellPayload.INSTANCE);
    public static final StreamCodec<ByteBuf, ConjureLightSpellPayload> STREAM_CODEC = StreamCodec.unit(ConjureLightSpellPayload.INSTANCE);
    
    public static final String TYPE = "conjure_light";
    protected static final AbstractRequirement<?> REQUIREMENT = new ResearchRequirement(new ResearchEntryKey(ResearchEntries.SPELL_PAYLOAD_CONJURE_LIGHT));
    
    public ConjureLightSpellPayload() {
        super(() -> BlocksPM.get(BlocksPM.GLOW_FIELD).defaultBlockState().setValue(GlowFieldBlock.FADING, Boolean.TRUE));
    }

    public static AbstractRequirement<?> getRequirement() {
        return REQUIREMENT;
    }

    public static ConjureLightSpellPayload getInstance() {
        return INSTANCE;
    }
    
    @Override
    public SpellPayloadType<ConjureLightSpellPayload> getType() {
        return SpellPayloadsPM.CONJURE_LIGHT.get();
    }

    @Override
    protected List<SpellProperty> getPropertiesInner() {
        return ImmutableList.of();
    }

    @Override
    public Source getSource() {
        return Sources.SUN;
    }

    @Override
    public int getBaseManaCost(SpellPropertyConfiguration properties) {
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
