package com.verdantartifice.primalmagic.common.theorycrafting;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundNBT;

/**
 * Collection of factory methods for creating research project related data structures.
 * 
 * @author Daedalus4096
 */
public class ProjectFactory {
    @Nullable
    public static AbstractProject getProjectFromType(@Nullable String type) {
        // Create a default research project instance using the given type's registered supplier
        Supplier<AbstractProject> factory = TheorycraftManager.getProjectSupplier(type);
        return (factory == null) ? null : factory.get();
    }
    
    @Nullable
    public static AbstractProject getProjectFromNBT(@Nullable CompoundNBT tag) {
        // Deserialize a research project instance from the given NBT data
        AbstractProject retVal = (tag == null) ? null : getProjectFromType(tag.getString("ProjectType"));
        if (retVal != null) {
            retVal.deserializeNBT(tag);
        }
        return retVal;
    }
    
    @Nullable
    public static AbstractProjectMaterial getMaterialFromNBT(@Nullable CompoundNBT tag) {
        AbstractProjectMaterial retVal = null;
        String materialType = (tag == null) ? null : tag.getString("MaterialType");
        if (ItemProjectMaterial.TYPE.equals(materialType)) {
            retVal = new ItemProjectMaterial();
        } else if (ObservationProjectMaterial.TYPE.equals(materialType)) {
            retVal = new ObservationProjectMaterial();
        }
        if (retVal != null) {
            retVal.deserializeNBT(tag);
        }
        return retVal;
    }
}
