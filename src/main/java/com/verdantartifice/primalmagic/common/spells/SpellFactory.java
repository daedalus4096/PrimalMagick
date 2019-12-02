package com.verdantartifice.primalmagic.common.spells;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.spells.packages.ISpellPackage;
import com.verdantartifice.primalmagic.common.spells.packages.SelfSpellPackage;
import com.verdantartifice.primalmagic.common.spells.packages.TouchSpellPackage;
import com.verdantartifice.primalmagic.common.spells.payloads.EarthDamageSpellPayload;
import com.verdantartifice.primalmagic.common.spells.payloads.FrostDamageSpellPayload;
import com.verdantartifice.primalmagic.common.spells.payloads.ISpellPayload;

import net.minecraft.nbt.CompoundNBT;

public class SpellFactory {
    @Nullable
    public static ISpellPackage getPackageFromNBT(CompoundNBT tag) {
        String type = tag.getString("SpellType");
        ISpellPackage retVal = null;
        if (TouchSpellPackage.TYPE.equals(type)) {
            retVal = new TouchSpellPackage();
        } else if (SelfSpellPackage.TYPE.equals(type)) {
            retVal = new SelfSpellPackage();
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
        } else if (FrostDamageSpellPayload.TYPE.equals(type)) {
            retVal = new FrostDamageSpellPayload();
        } else {
            return null;
        }
        retVal.deserializeNBT(tag);
        return retVal;
    }
}
