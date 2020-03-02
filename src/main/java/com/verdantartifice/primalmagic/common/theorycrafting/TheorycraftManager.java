package com.verdantartifice.primalmagic.common.theorycrafting;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.util.WeightedRandomBag;

import net.minecraft.entity.player.PlayerEntity;

/**
 * Primary access point for theorycraft-related methods.  Also stores defined research projects in a
 * static registry.
 * 
 * @author Daedalus4096
 */
public class TheorycraftManager {
    protected static final Map<String, Supplier<AbstractProject>> PROJECT_SUPPLIERS = new HashMap<>();
    
    public static void registerProjectType(@Nullable String type, @Nullable Supplier<AbstractProject> supplier) {
        // Don't allow null or empty data in the project registry
        if (type != null && !type.isEmpty() && supplier != null) {
            PROJECT_SUPPLIERS.put(type, supplier);
        }
    }
    
    @Nullable
    public static Supplier<AbstractProject> getProjectSupplier(@Nullable String type) {
        return PROJECT_SUPPLIERS.get(type);
    }
    
    @Nonnull
    public static AbstractProject createRandomProject(@Nonnull PlayerEntity player) {
        WeightedRandomBag<String> typeBag = new WeightedRandomBag<>();
        for (String typeStr : PROJECT_SUPPLIERS.keySet()) {
            typeBag.add(typeStr, 1);
        }
        
        AbstractProject retVal = null;
        int attempts = 0;   // Don't allow an infinite loop
        while (retVal == null && attempts < 1000) {
            attempts++;
            String selectedType = typeBag.getRandom(player.getRNG());
            AbstractProject tempProject = ProjectFactory.getProjectFromType(selectedType);
            if (tempProject != null && tempProject.initialize(player)) {
                retVal = tempProject;
            }
        }
        return retVal;
    }
}
