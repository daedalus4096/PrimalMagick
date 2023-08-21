package com.verdantartifice.primalmagick.common.wands;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Rarity;

/**
 * Definition of a wand component data structure.  Components are either cores, caps, or gems.
 * 
 * @author Daedalus4096
 */
public interface IWandComponent {
    /**
     * Get the unique identifying tag for the component.
     * 
     * @return the unique identifying tag for the component
     */
    @Nonnull
    public String getTag();
    
    /**
     * Get the type of the component.
     * 
     * @return the type of the component
     */
    @Nonnull
    public Type getComponentType();

    /**
     * Get the rarity of the component.
     * 
     * @return the rarity of the component
     */
    @Nonnull
    public Rarity getRarity();

    /**
     * Get the translation key for the component's display name.
     * 
     * @return the translation key for the component's display name
     */
    @Nonnull
    public default String getNameTranslationKey() {
        return String.join(".", this.getComponentType().getSerializedName(), PrimalMagick.MODID, this.getTag());
    }

    /**
     * Get the enchantability conferred to a wand by this component.
     * 
     * @return the enchantability conferred to a wand by this component
     */
    public default int getEnchantability() {
        switch (this.getRarity()) {
        case COMMON:
            return 4;
        case UNCOMMON:
            return 8;
        case RARE:
            return 12;
        case EPIC:
            return 16;
        default:
            return 0;
        }
    }
    
    public static enum Type implements StringRepresentable {
        CORE("wand_core"),
        CAP("wand_cap"),
        GEM("wand_gem");
        
        private final String name;
        
        private Type(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}