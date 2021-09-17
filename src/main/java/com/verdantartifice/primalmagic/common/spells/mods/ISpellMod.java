package com.verdantartifice.primalmagic.common.spells.mods;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.spells.SpellProperty;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Primary interface for a spell mod component.  Spell mods typically alter the functionality of spells in
 * ways more complex than can be captured in a simple property, or are independent of vehicle/payload type.
 * An example is the Burst spell mod, which causes the spell to explode from its impact point and affect
 * multiple targets.  Spell mods themselves may have properties which determine the extent of their effect.
 * In addition, spell mods raise the cost of spells, typically multiplicatively.
 * 
 * @author Daedalus4096
 */
public interface ISpellMod extends INBTSerializable<CompoundTag> {
    /**
     * Determine whether the spell mod has an effect that should be executed.  Should be true for all but
     * placeholder spell mods.
     * 
     * @return true if the spell mod has an effect that should be executed, false otherwise
     */
    public boolean isActive();
    
    /**
     * Get a name-ordered list of properties used by this spell mod.
     * 
     * @return a name-ordered list of properties used by this spell mod
     */
    @Nonnull
    public List<SpellProperty> getProperties();
    
    /**
     * Get a specific property of the spell mod.
     * 
     * @param name the name of the property to retrieve
     * @return the named property, or null if no such property is attached to this spell mod
     */
    @Nullable
    public SpellProperty getProperty(String name);
    
    /**
     * Get the value of a specific property of the spell mod.
     * 
     * @param name the name of the property value to retrieve
     * @return the named property's value, or zero if no such property is attached to this spell mod
     */
    public int getPropertyValue(String name);
    
    /**
     * Get a display text component containing the human-friendly name of this spell mod type.
     * 
     * @return the spell mod type name
     */
    @Nonnull
    public Component getTypeName();
    
    /**
     * Get a display text component to show in the details tooltip of the spell.
     * 
     * @return the spell mod details
     */
    public default Component getDetailTooltip() {
        return this.getTypeName();
    }
    
    /**
     * Get a display text component containing the human-friendly text to be used to identify the
     * spell mod in the default of a spell package.
     * 
     * @return the spell mod's default name
     */
    @Nonnull
    public Component getDefaultNamePiece();
    
    /**
     * Get the additive modifier to be applied to the spell mod's package's base cost.
     * 
     * @return the additive modifier for the spell package's cost
     */
    public int getBaseManaCostModifier();
    
    /**
     * Get the multiplicative modifier to be applied to the spell mod's package's total cost.
     * 
     * @return the multiplicative modifier for the spell package's cost
     */
    public int getManaCostMultiplier();
}
