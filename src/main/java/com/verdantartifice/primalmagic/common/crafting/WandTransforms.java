package com.verdantartifice.primalmagic.common.crafting;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class WandTransforms {
    protected static final Set<IWandTransform> REGISTRY = new HashSet<>();
    
    public static Set<IWandTransform> getAll() {
        return Collections.unmodifiableSet(REGISTRY);
    }
    
    public static boolean register(IWandTransform transform) {
        return REGISTRY.add(transform);
    }
}
