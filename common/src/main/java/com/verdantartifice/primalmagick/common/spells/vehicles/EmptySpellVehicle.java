package com.verdantartifice.primalmagick.common.spells.vehicles;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * Definition of an empty spell vehicle.  This vehicle has no effect and is not valid in spells.  Its 
 * only purpose is to provide a selection entry in the spellcrafting altar GUI for when the player has
 * not selected a vehicle for the spell.
 * 
 * @author Daedalus4096
 */
public class EmptySpellVehicle extends AbstractSpellVehicle<EmptySpellVehicle> {
    public static final EmptySpellVehicle INSTANCE = new EmptySpellVehicle();
    
    public static final MapCodec<EmptySpellVehicle> CODEC = MapCodec.unit(EmptySpellVehicle.INSTANCE);
    public static final StreamCodec<ByteBuf, EmptySpellVehicle> STREAM_CODEC = StreamCodec.unit(EmptySpellVehicle.INSTANCE);
    
    public static final String TYPE = "none";

    @Override
    public void execute(SpellPackage spell, Level world, LivingEntity caster, ItemStack spellSource) {
        // Do nothing
    }
    
    @Override
    public SpellVehicleType<EmptySpellVehicle> getType() {
        return SpellVehiclesPM.EMPTY.get();
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
    protected String getVehicleType() {
        return TYPE;
    }
    
    public static AbstractRequirement<?> getRequirement() {
        return null;
    }
    
    public static EmptySpellVehicle getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean isIndirect() {
        return false;
    }
}
