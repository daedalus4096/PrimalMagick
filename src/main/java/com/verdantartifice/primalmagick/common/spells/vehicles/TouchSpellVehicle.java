package com.verdantartifice.primalmagick.common.spells.vehicles;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

/**
 * Definition of a touch spell vehicle.  This vehicle selects the closest target within the caster's
 * reach along their line of sight.
 * 
 * @author Daedalus4096
 */
public class TouchSpellVehicle extends AbstractRaycastSpellVehicle<TouchSpellVehicle> {
    public static final TouchSpellVehicle INSTANCE = new TouchSpellVehicle();
    
    public static final MapCodec<TouchSpellVehicle> CODEC = MapCodec.unit(TouchSpellVehicle.INSTANCE);
    public static final StreamCodec<ByteBuf, TouchSpellVehicle> STREAM_CODEC = StreamCodec.unit(TouchSpellVehicle.INSTANCE);
    
    public static final String TYPE = "touch";
    protected static final AbstractRequirement<?> REQUIREMENT = new ResearchRequirement(new ResearchEntryKey(ResearchEntries.BASIC_SORCERY));
    
    public static AbstractRequirement<?> getRequirement() {
        return REQUIREMENT;
    }

    @Override
    public SpellVehicleType<TouchSpellVehicle> getType() {
        return SpellVehiclesPM.TOUCH.get();
    }

    @Override
    protected List<SpellProperty> getPropertiesInner() {
        return ImmutableList.of();
    }

    @Override
    protected String getVehicleType() {
        return TYPE;
    }
    
    @Override
    protected double getReachDistance(LivingEntity caster, SpellPackage spell, ItemStack spellSource) {
        return caster.getAttribute(Attributes.BLOCK_INTERACTION_RANGE).getValue();
    }

    @Override
    public boolean isIndirect() {
        return false;
    }
}
