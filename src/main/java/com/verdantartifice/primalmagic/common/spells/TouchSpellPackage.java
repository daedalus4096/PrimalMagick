package com.verdantartifice.primalmagic.common.spells;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public class TouchSpellPackage extends AbstractSpellPackage {
    public static final String TYPE = "touch";
    
    public TouchSpellPackage() {
        super();
    }

    public TouchSpellPackage(String name) {
        super(name);
    }
    
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = super.serializeNBT();
        tag.putString("SpellType", TYPE);
        return tag;
    }

    @Override
    public void cast(World world, PlayerEntity caster) {
        PrimalMagic.LOGGER.info("Casting spell {}", this.getName());
    }
}
