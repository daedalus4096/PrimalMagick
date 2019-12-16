package com.verdantartifice.primalmagic.common.spells.vehicles;

import com.verdantartifice.primalmagic.common.spells.SpellPackage;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class EmptySpellVehicle extends AbstractSpellVehicle {
    public static final String TYPE = "none";

    @Override
    public void execute(SpellPackage spell, World world, PlayerEntity caster) {
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
}
