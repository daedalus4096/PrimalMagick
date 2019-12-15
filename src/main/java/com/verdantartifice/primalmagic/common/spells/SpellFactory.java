package com.verdantartifice.primalmagic.common.spells;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.spells.mods.ISpellMod;
import com.verdantartifice.primalmagic.common.spells.payloads.ISpellPayload;
import com.verdantartifice.primalmagic.common.spells.vehicles.ISpellVehicle;

import net.minecraft.nbt.CompoundNBT;

public class SpellFactory {
    @Nullable
    public static ISpellVehicle getVehicleFromType(String type) {
        Supplier<ISpellVehicle> factory = SpellManager.getVehicleSupplier(type);
        return (factory == null) ? null : factory.get();
    }
    
    @Nullable
    public static ISpellVehicle getVehicleFromNBT(CompoundNBT tag) {
        ISpellVehicle retVal = getVehicleFromType(tag.getString("VehicleType"));
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
