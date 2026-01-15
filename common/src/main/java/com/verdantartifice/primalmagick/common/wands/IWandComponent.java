package com.verdantartifice.primalmagick.common.wands;

import com.verdantartifice.primalmagick.Constants;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;

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
    @NotNull
    String getTag();
    
    /**
     * Get the type of the component.
     * 
     * @return the type of the component
     */
    @NotNull
    Type getComponentType();

    /**
     * Get the rarity of the component.
     * 
     * @return the rarity of the component
     */
    @NotNull
    Rarity getRarity();

    /**
     * Get the translation key for the component's display name.
     * 
     * @return the translation key for the component's display name
     */
    @NotNull
    default String getNameTranslationKey() {
        return String.join(".", this.getComponentType().getSerializedName(), Constants.MOD_ID, this.getTag());
    }

    enum Type implements StringRepresentable {
        CORE("wand_core"),
        CAP("wand_cap"),
        GEM("wand_gem");
        
        private final String name;
        
        Type(String name) {
            this.name = name;
        }

        @Override
        @NotNull
        public String getSerializedName() {
            return this.name;
        }
    }
}