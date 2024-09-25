package com.verdantartifice.primalmagick.common.rituals;

import java.util.function.Consumer;

/**
 * Class describing a ritual mishap, including the method to call to execute it, whether it's severe,
 * and a minimum instability.
 * 
 * @author Daedalus4096
 */
public class Mishap {
    protected final Consumer<Boolean> action;
    protected final boolean severe;
    protected final float minInstability;
    
    public Mishap(Consumer<Boolean> action, boolean severe, float minInstability) {
        this.action = action;
        this.severe = severe;
        this.minInstability = Math.abs(minInstability);
    }
    
    public boolean execute(float currentStability) {
        if (currentStability <= 0.0F && Math.abs(currentStability) >= this.minInstability) {
            this.action.accept(this.severe);
            return true;
        } else {
            return false;
        }
    }
}
