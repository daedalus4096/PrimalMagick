package com.verdantartifice.primalmagick.common.attunements;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;

/**
 * Definition of attunement thresholds, determining when certain bonuses are received.
 * 
 * @author Daedalus4096
 */
public enum AttunementThreshold implements StringRepresentable {
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
        return Component.translatable(this.getNameTranslationKey());
    }

    @Nonnull
    public String getNameTranslationKey() {
        return String.join(".", "attunement_threshold", PrimalMagick.MODID, this.getSerializedName());
    }

    @Override
    public String getSerializedName() {
        return this.tag;
    }
}
