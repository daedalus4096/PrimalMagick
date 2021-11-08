package com.verdantartifice.primalmagick.common.theorycrafting;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;

/**
 * Collection of factory methods for creating research project related data structures.
 * 
 * @author Daedalus4096
 */
public class ProjectFactory {
    @Nullable
    public static Project getProjectFromNBT(@Nullable CompoundTag tag) {
        // Deserialize a research project instance from the given NBT data
        Project retVal = new Project();
        retVal.deserializeNBT(tag);
        return retVal;
    }
    
    @Nullable
    public static AbstractProjectMaterial getMaterialFromNBT(@Nullable CompoundTag tag) {
        AbstractProjectMaterial retVal = null;
        String materialType = (tag == null) ? null : tag.getString("MaterialType");
        if (ItemProjectMaterial.TYPE.equals(materialType)) {
            retVal = new ItemProjectMaterial();
        } else if (ItemTagProjectMaterial.TYPE.equals(materialType)) {
            retVal = new ItemTagProjectMaterial();
        } else if (ObservationProjectMaterial.TYPE.equals(materialType)) {
            retVal = new ObservationProjectMaterial();
        } else if (ExperienceProjectMaterial.TYPE.equals(materialType)) {
            retVal = new ExperienceProjectMaterial();
        }
        if (retVal != null) {
            retVal.deserializeNBT(tag);
        }
        return retVal;
    }
}
