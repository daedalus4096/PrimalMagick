package com.verdantartifice.primalmagick.common.books;

import javax.annotation.Nullable;

import net.minecraft.util.StringRepresentable;

/**
 * Defines what mode of operation a Scribe's Table is currently operating in, and thus what menu
 * it's currently showing, or was last showing.
 * 
 * @author Daedalus4096
 */
public enum ScribeTableMode implements StringRepresentable {
    STUDY_VOCABULARY("study_vocabulary"),
    GAIN_COMPREHENSION("gain_comprehension"),
    TRANSCRIBE_WORKS("transcribe_works");
    
    private final String tag;
    
    private ScribeTableMode(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return this.tag;
    }
    
    @Override
    public String getSerializedName() {
        return this.tag;
    }
    
    @Nullable
    public static ScribeTableMode fromName(@Nullable String name) {
        for (ScribeTableMode mode : values()) {
            if (mode.getSerializedName().equals(name)) {
                return mode;
            }
        }
        return null;
    }
}
