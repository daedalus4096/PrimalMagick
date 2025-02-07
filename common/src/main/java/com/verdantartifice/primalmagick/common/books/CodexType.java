package com.verdantartifice.primalmagick.common.books;

import net.minecraft.util.StringRepresentable;

/**
 * Definition of a codex type.  Determines the amount of comprehension granted by the codex.
 * 
 * @author Daedalus4096
 */
public enum CodexType implements StringRepresentable {
    DEFAULT("default", 1),
    CREATIVE("creative", Short.MAX_VALUE);
    
    private final String tag;
    private final int amount;
    
    private CodexType(String tag, int amount) {
        this.tag = tag;
        this.amount = amount;
    }
    
    public String getTag() {
        return this.tag;
    }
    
    public int getAmount() {
        return this.amount;
    }

    @Override
    public String getSerializedName() {
        return this.tag;
    }
}
