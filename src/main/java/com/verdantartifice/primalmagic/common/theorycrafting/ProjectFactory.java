package com.verdantartifice.primalmagic.common.theorycrafting;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundNBT;

/**
 * Collection of factory methods for creating research project related data structures.
 * 
 * @author Daedalus4096
 */
public class ProjectFactory {
    @Nullable
    public AbstractProjectMaterial getMaterialFromNBT(CompoundNBT tag) {
        AbstractProjectMaterial retVal = null;
        String materialType = tag.getString("MaterialType");
        if (materialType == ItemProjectMaterial.TYPE) {
            retVal = new ItemProjectMaterial();
        } else if (materialType == ObservationProjectMaterial.TYPE) {
            retVal = new ObservationProjectMaterial();
        }
        if (retVal != null) {
            retVal.deserializeNBT(tag);
        }
        return retVal;
    }
}
