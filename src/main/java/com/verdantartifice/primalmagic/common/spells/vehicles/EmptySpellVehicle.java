package com.verdantartifice.primalmagic.common.spells.vehicles;

import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

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
    public void execute(SpellPackage spell, World world, LivingEntity caster, ItemStack spellSource) {
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
    
    public static CompoundResearchKey getResearch() {
        return null;
    }
}
