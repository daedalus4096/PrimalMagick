package com.verdantartifice.primalmagic.common.spells;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.spells.mods.ISpellMod;
import com.verdantartifice.primalmagic.common.spells.payloads.ISpellPayload;
import com.verdantartifice.primalmagic.common.spells.vehicles.ISpellVehicle;

import net.minecraft.nbt.CompoundNBT;

/**
 * Collection of factory methods for creating spell component data structures.
 * 
 * @author Daedalus4096
 */
public class SpellFactory {
    @Nullable
    public static ISpellVehicle getVehicleFromType(@Nullable String type) {
        // Create a default spell vehicle component using the given type's registered supplier
        Supplier<ISpellVehicle> factory = SpellManager.getVehicleSupplier(type);
        return (factory == null) ? null : factory.get();
    }
    
    @Nullable
    public static ISpellVehicle getVehicleFromNBT(@Nullable CompoundNBT tag) {
        // Deserialize a spell vehicle component from the given NBT data
        ISpellVehicle retVal = tag == null ? null : getVehicleFromType(tag.getString("VehicleType"));
        if (retVal != null) {
            retVal.deserializeNBT(tag);
        }
        return retVal;
    }

    @Nullable
    public static ISpellPayload getPayloadFromType(@Nullable String type) {
        // Create a default spell payload component using the given type's registered supplier
        Supplier<ISpellPayload> factory = SpellManager.getPayloadSupplier(type);
        return (factory == null) ? null : factory.get();
    }
    
    @Nullable
    public static ISpellPayload getPayloadFromNBT(@Nullable CompoundNBT tag) {
        // Deserialize a spell payload component from the given NBT data
        ISpellPayload retVal = tag == null ? null : getPayloadFromType(tag.getString("PayloadType"));
        if (retVal != null) {
            retVal.deserializeNBT(tag);
        }
        return retVal;
    }
    
    @Nullable
    public static ISpellMod getModFromType(@Nullable String type) {
        // Create a default spell mod component using the given type's registered supplier
        Supplier<ISpellMod> factory = SpellManager.getModSupplier(type);
        return (factory == null) ? null : factory.get();
    }
    
    @Nullable
    public static ISpellMod getModFromNBT(@Nullable CompoundNBT tag) {
        // Deserialize a spell mod component from the given NBT data
        ISpellMod retVal = tag == null ? null : getModFromType(tag.getString("ModType"));
        if (retVal != null) {
            retVal.deserializeNBT(tag);
        }
        return retVal;
    }
}
