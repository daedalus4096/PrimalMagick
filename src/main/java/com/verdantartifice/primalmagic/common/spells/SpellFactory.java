package com.verdantartifice.primalmagic.common.spells;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.spells.mods.EmptySpellMod;
import com.verdantartifice.primalmagic.common.spells.mods.ISpellMod;
import com.verdantartifice.primalmagic.common.spells.packages.ISpellPackage;
import com.verdantartifice.primalmagic.common.spells.packages.ProjectileSpellPackage;
import com.verdantartifice.primalmagic.common.spells.packages.SelfSpellPackage;
import com.verdantartifice.primalmagic.common.spells.packages.TouchSpellPackage;
import com.verdantartifice.primalmagic.common.spells.payloads.EarthDamageSpellPayload;
import com.verdantartifice.primalmagic.common.spells.payloads.FrostDamageSpellPayload;
import com.verdantartifice.primalmagic.common.spells.payloads.ISpellPayload;

import net.minecraft.nbt.CompoundNBT;

public class SpellFactory {
    @Nullable
    public static ISpellPackage getPackageFromType(String type) {
        if (TouchSpellPackage.TYPE.equals(type)) {
            return new TouchSpellPackage();
        } else if (ProjectileSpellPackage.TYPE.equals(type)) {
            return new ProjectileSpellPackage();
        } else if (SelfSpellPackage.TYPE.equals(type)) {
            return new SelfSpellPackage();
        } else {
            return null;
        }
    }
    
    @Nullable
    public static ISpellPackage getPackageFromNBT(CompoundNBT tag) {
        ISpellPackage retVal = getPackageFromType(tag.getString("SpellType"));
        if (retVal != null) {
            retVal.deserializeNBT(tag);
        }
        return retVal;
    }

    @Nullable
    public static ISpellPayload getPayloadFromType(String type) {
        if (EarthDamageSpellPayload.TYPE.equals(type)) {
            return new EarthDamageSpellPayload();
        } else if (FrostDamageSpellPayload.TYPE.equals(type)) {
            return new FrostDamageSpellPayload();
        } else {
            return null;
        }
    }
    
    @Nullable
    public static ISpellPayload getPayloadFromNBT(CompoundNBT tag) {
        ISpellPayload retVal = getPayloadFromType(tag.getString("PayloadType"));
        if (retVal != null) {
            retVal.deserializeNBT(tag);
        }
        return retVal;
    }
    
    @Nullable
    public static ISpellMod getModFromType(String type) {
        if (EmptySpellMod.TYPE.equals(type)) {
            return new EmptySpellMod();
        } else {
            return null;
        }
    }
    
    @Nullable
    public static ISpellMod getModFromNBT(CompoundNBT tag) {
        ISpellMod retVal = getModFromType(tag.getString("ModType"));
        if (retVal != null) {
            retVal.deserializeNBT(tag);
        }
        return retVal;
    }
}
