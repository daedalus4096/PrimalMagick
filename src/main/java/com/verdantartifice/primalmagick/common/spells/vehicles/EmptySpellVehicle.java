package com.verdantartifice.primalmagick.common.spells.vehicles;

import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Definition of an empty spell vehicle.  This vehicle has no effect and is not valid in spells.  Its 
 * only purpose is to provide a selection entry in the spellcrafting altar GUI for when the player has
 * not selected a vehicle for the spell.
 * 
 * @author Daedalus4096
 */
public class EmptySpellVehicle extends AbstractSpellVehicle {
    public static final String TYPE = "none";

    @Override
    public void execute(SpellPackage spell, Level world, LivingEntity caster, ItemStack spellSource) {
        // Do nothing
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

    @Override
    public boolean isIndirect() {
        return false;
    }
}
