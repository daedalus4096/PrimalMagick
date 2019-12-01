package com.verdantartifice.primalmagic.common.spells;

import net.minecraft.nbt.CompoundNBT;

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
}
