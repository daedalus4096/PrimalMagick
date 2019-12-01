package com.verdantartifice.primalmagic.common.spells;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.spells.payloads.EarthDamageSpellPayload;
import com.verdantartifice.primalmagic.common.spells.payloads.ISpellPayload;

import net.minecraft.nbt.CompoundNBT;

public class SpellFactory {
    @Nullable
    public static ISpellPackage getPackageFromNBT(CompoundNBT tag) {
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
    
    @Nullable
    public static ISpellPayload getPayloadFromNBT(CompoundNBT tag) {
        String type = tag.getString("PayloadType");
        ISpellPayload retVal = null;
        if (EarthDamageSpellPayload.TYPE.equals(type)) {
            retVal = new EarthDamageSpellPayload();
        } else {
            return null;
        }
        retVal.deserializeNBT(tag);
        return retVal;
    }
}
