package com.verdantartifice.primalmagick.common.spells;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public interface ISpellComponent {
    /**
     * Get a name-ordered list of properties used by this spell component.
     * 
     * @return a name-ordered list of properties used by this spell component
     */
    @Nonnull
    List<SpellProperty> getProperties();
    
    /**
     * Get a specific property of the spell component.
     * 
     * @param id the identifier of the property to retrieve
     * @return the named property, or null if no such property is attached to this spell component
     */
    @Nullable
    SpellProperty getProperty(ResourceLocation id);
    
    /**
     * Determine whether the spell component has an effect that should be executed.  Should be true for all but
     * placeholder spell components.
     * 
     * @return true if the spell component has an effect that should be executed, false otherwise
     */
    boolean isActive();
    
    /**
     * Get a display text component containing the human-friendly name of this spell component type.
     * 
     * @return the spell component type name
     */
    @Nonnull
    Component getTypeName();
    
    /**
     * Get a display text component to show in the details tooltip of the spell.
     * 
     * @return the spell component details
     */
    default Component getDetailTooltip(SpellPackage spell, ItemStack spellSource) {
        return this.getTypeName();
    }
    
    /**
     * Get a display text component containing the human-friendly text to be used to identify the
     * spell component in the default of a spell package.
     * 
     * @return the spell component's default name
     */
    @Nonnull
    Component getDefaultNamePiece();
}
