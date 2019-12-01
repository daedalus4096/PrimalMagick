package com.verdantartifice.primalmagic.common.spells;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundNBT;

public class SpellFactory {
    @Nullable
    public static ISpellPackage fromNBT(CompoundNBT tag) {
        String type = tag.getString("SpellType");
        ISpellPackage retVal = null;
        if (TouchSpellPackage.TYPE.equals(type)) {
            retVal = new TouchSpellPackage();
        } else {
            return null;
        }
        retVal.deserializeNBT(tag);
        return retVal;
    }
}
