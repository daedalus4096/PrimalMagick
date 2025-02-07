package com.verdantartifice.primalmagick.common.spells.payloads;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
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
 * Definition for a consecration spell.  Creates a two-high field of holy energy at the target location
 * which prevents entry by non-player mobs.  It also heals and restores hunger to players.  Has no effect
 * when cast by non-players.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.common.blocks.misc.ConsecrationFieldBlock}
 */
public class ConsecrateSpellPayload extends AbstractConjureBlockSpellPayload<ConsecrateSpellPayload> {
    public static final ConsecrateSpellPayload INSTANCE = new ConsecrateSpellPayload();
    
    public static final MapCodec<ConsecrateSpellPayload> CODEC = MapCodec.unit(ConsecrateSpellPayload.INSTANCE);
    public static final StreamCodec<ByteBuf, ConsecrateSpellPayload> STREAM_CODEC = StreamCodec.unit(ConsecrateSpellPayload.INSTANCE);
    
    public static final String TYPE = "consecrate";
    protected static final AbstractRequirement<?> REQUIREMENT = new ResearchRequirement(new ResearchEntryKey(ResearchEntries.SPELL_PAYLOAD_CONSECRATE));

    public ConsecrateSpellPayload() {
        super(() -> BlocksPM.CONSECRATION_FIELD.get().defaultBlockState(), 2);
    }
    
    public static AbstractRequirement<?> getRequirement() {
        return REQUIREMENT;
    }
    
    public static ConsecrateSpellPayload getInstance() {
        return INSTANCE;
    }
    
    @Override
    public SpellPayloadType<ConsecrateSpellPayload> getType() {
        return SpellPayloadsPM.CONSECRATE.get();
    }

    @Override
    protected List<SpellProperty> getPropertiesInner() {
        return ImmutableList.of();
    }

    @Override
    public Source getSource() {
        return Sources.HALLOWED;
    }

    @Override
    public int getBaseManaCost(SpellPropertyConfiguration properties) {
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
