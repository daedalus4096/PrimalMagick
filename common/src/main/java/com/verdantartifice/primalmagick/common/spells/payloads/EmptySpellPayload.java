package com.verdantartifice.primalmagick.common.spells.payloads;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;
import com.verdantartifice.primalmagick.common.spells.SpellPropertyConfiguration;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Definition of an empty spell payload.  This payload has no effect and is not valid in spells.  Its 
 * only purpose is to provide a selection entry in the spellcrafting altar GUI for when the player has
 * not selected a payload for the spell.
 * 
 * @author Daedalus4096
 */
public class EmptySpellPayload extends AbstractSpellPayload<EmptySpellPayload> {
    public static final EmptySpellPayload INSTANCE = new EmptySpellPayload();
    
    public static final MapCodec<EmptySpellPayload> CODEC = MapCodec.unit(EmptySpellPayload.INSTANCE);
    public static final StreamCodec<ByteBuf, EmptySpellPayload> STREAM_CODEC = StreamCodec.unit(EmptySpellPayload.INSTANCE);
    
    public static final String TYPE = "none";

    @Override
    public void execute(HitResult target, Vec3 burstPoint, SpellPackage spell, Level world, LivingEntity caster, ItemStack spellSource, Entity projectileEntity) {
        // Do nothing
    }

    public static EmptySpellPayload getInstance() {
        return INSTANCE;
    }
    
    @Override
    public SpellPayloadType<EmptySpellPayload> getType() {
        return SpellPayloadsPM.EMPTY.get();
    }

    @Override
    protected List<SpellProperty> getPropertiesInner() {
        return ImmutableList.of();
    }

    @Override
    public boolean isActive() {
        return false;
    }
    
    @Override
    public Source getSource() {
        return Sources.EARTH;
    }

    @Override
    public int getBaseManaCost(SpellPropertyConfiguration properties) {
        return 0;
    }

    @Override
    public void playSounds(Level world, BlockPos origin) {
        // Do nothing
    }

    @Override
    protected String getPayloadType() {
        return TYPE;
    }
    
    public static AbstractRequirement<?> getRequirement() {
        return null;
    }
}
