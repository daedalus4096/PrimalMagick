package com.verdantartifice.primalmagick.common.theorycrafting;

import javax.annotation.Nullable;

import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;

/**
 * Collection of factory methods for creating research project related data structures.
 * 
 * @author Daedalus4096
 */
public class ProjectFactory {
    protected static final Logger LOGGER = LogManager.getLogger();
    
    @Nullable
    public static Project getProjectFromNBT(@Nullable CompoundTag tag) {
        // Deserialize a research project instance from the given NBT data
        MutableObject<Project> retVal = new MutableObject<>();
        Project.CODEC.parse(NbtOps.INSTANCE, tag)
            .resultOrPartial(LOGGER::error)
            .ifPresent(project -> retVal.setValue(project));
        return retVal.getValue();
    }
}
