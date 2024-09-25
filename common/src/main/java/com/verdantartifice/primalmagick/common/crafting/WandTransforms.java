package com.verdantartifice.primalmagick.common.crafting;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Registry for defined wand transformations.
 * 
 * @author Daedalus4096
 */
public class WandTransforms {
    public static final int CHANNEL_DURATION = 40;
    protected static final Set<IWandTransform> REGISTRY = new HashSet<>();
    
    public static Set<IWandTransform> getAll() {
        return Collections.unmodifiableSet(REGISTRY);
    }
    
    public static boolean register(IWandTransform transform) {
        return REGISTRY.add(transform);
    }
}
