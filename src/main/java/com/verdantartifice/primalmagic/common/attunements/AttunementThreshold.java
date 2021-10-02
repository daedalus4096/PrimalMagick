package com.verdantartifice.primalmagic.common.attunements;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

/**
 * Definition of attunement thresholds, determining when certain bonuses are received.
 * 
 * @author Daedalus4096
 */
public enum AttunementThreshold {
    MINOR("minor", 30),
    LESSER("lesser", 60),
    GREATER("greater", 90);
    
    private final String tag;
    private final int value;
    
    private AttunementThreshold(String tag, int value) {
        this.tag = tag;
        this.value = value;
    }
    
    public String getTag() {
        return this.tag;
    }
    
    public int getValue() {
        return this.value;
    }
    
    public Component getThresholdText() {
        return new TranslatableComponent("primalmagic.attunement_threshold." + this.name());
    }
}
