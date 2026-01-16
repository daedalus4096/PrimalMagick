package com.verdantartifice.primalmagick.common.attunements;

import com.verdantartifice.primalmagick.Constants;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a type of magickal attunement.  Permanent attunement is gained through research and
 * cannot be removed.  Induced attunement is gained or lost through rituals, but does not decay
 * over time.  Temporary attunement is gained by crafting items and casting spells, but decays
 * slowly over time.
 * 
 * @author Daedalus4096
 */
public enum AttunementType implements StringRepresentable {
    PERMANENT("permanent", -1),
    INDUCED("induced", 50),
    TEMPORARY("temporary", 50);
    
    private final String name;
    private final int maximum;    // The maximum attunement amount of this type that the player can have at once
    
    AttunementType(String name, int max) {
        this.name = name;
        this.maximum = max;
    }
    
    public boolean isCapped() {
        return (this.maximum > 0);
    }
    
    public int getMaximum() {
        return this.maximum;
    }
    
    @NotNull
    public String getNameTranslationKey() {
        return String.join(".", "attunement_type", Constants.MOD_ID, this.getSerializedName());
    }

    @Override
    @NotNull
    public String getSerializedName() {
        return this.name;
    }

    @Nullable
    public static AttunementType fromName(@Nullable String name) {
        for (AttunementType type : AttunementType.values()) {
            if (type.getSerializedName().equals(name)) {
                return type;
            }
        }
        return null;
    }
}
