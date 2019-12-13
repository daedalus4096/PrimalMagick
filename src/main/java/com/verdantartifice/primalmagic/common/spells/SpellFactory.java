package com.verdantartifice.primalmagic.common.spells;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.spells.mods.ISpellMod;
import com.verdantartifice.primalmagic.common.spells.packages.ISpellPackage;
import com.verdantartifice.primalmagic.common.spells.payloads.ISpellPayload;

import net.minecraft.nbt.CompoundNBT;

public class SpellFactory {
    @Nullable
    public static ISpellPackage getPackageFromType(String type) {
        Supplier<ISpellPackage> factory = SpellManager.getPackageSupplier(type);
        return (factory == null) ? null : factory.get();
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
        Supplier<ISpellPayload> factory = SpellManager.getPayloadSupplier(type);
        return (factory == null) ? null : factory.get();
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
        Supplier<ISpellMod> factory = SpellManager.getModSupplier(type);
        return (factory == null) ? null : factory.get();
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
