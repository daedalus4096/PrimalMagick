package com.verdantartifice.primalmagic.common.wands;

import javax.annotation.Nonnull;

import net.minecraft.item.Rarity;

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
    public String getNameTranslationKey();

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
}