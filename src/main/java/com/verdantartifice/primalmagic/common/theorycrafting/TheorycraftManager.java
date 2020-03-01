package com.verdantartifice.primalmagic.common.theorycrafting;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import javax.annotation.Nullable;

public class TheorycraftManager {
    protected static final Map<String, Supplier<AbstractProject>> PROJECT_SUPPLIERS = new HashMap<>();
    
    public static void registerProjectType(@Nullable String type, @Nullable Supplier<AbstractProject> supplier) {
        // Don't allow null or empty data in the project registry
        if (type != null && !type.isEmpty() && supplier != null) {
            PROJECT_SUPPLIERS.put(type, supplier);
        }
    }
    
    public static Supplier<AbstractProject> getProjectSupplier(@Nullable String type) {
        return PROJECT_SUPPLIERS.get(type);
    }
}
