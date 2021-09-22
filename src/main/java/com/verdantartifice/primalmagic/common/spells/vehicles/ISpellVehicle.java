package com.verdantartifice.primalmagic.common.spells.vehicles;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.spells.SpellPackage;
import com.verdantartifice.primalmagic.common.spells.SpellProperty;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Primary interface for a spell vehicle.  Spell vehicles are what determine the target of a spell
 * and carry the spell package from the caster to that target.  Vehicles may have properties which
 * alter their behavior, but most do not.  Spell vehicles sometimes also modify the base cost of
 * the spell.
 * 
 * @author Daedalus4096
 */
public interface ISpellVehicle extends INBTSerializable<CompoundTag> {
    /**
     * Execute this spell vehicle to determine the target of the spell, then execute the spell package's
     * payload if one is found.
     *  
     * @param spell the full spell package containing this vehicle
     * @param world the world in which the vehicle should be executed
     * @param caster the entity that originally casted the spell
     * @param spellSource the wand or scroll that originally contained the spell
     */
    public void execute(@Nonnull SpellPackage spell, @Nonnull Level world, @Nonnull LivingEntity caster, @Nullable ItemStack spellSource);

    /**
     * Determine whether this vehicle has an effect that should be executed.  Should be true for all but
     * placeholder vehicles.
     * 
     * @return true if this vehicle has an effect that should be executed, false otherwise
     */
    public boolean isActive();
    
    /**
     * Get a display text component containing the human-friendly name of this spell vehicle type.
     * 
     * @return the spell vehicle type name
     */
    @Nonnull
    public Component getTypeName();
    
    /**
     * Get a display text component to show in the details tooltip of the spell.
     * 
     * @return the spell vehicle details
     */
    public default Component getDetailTooltip() {
        return this.getTypeName();
    }
    
    /**
     * Get a display text component containing the human-friendly text to be used to identify the
     * spell vehicle in the default of a spell package.
     * 
     * @return the spell vehicle's default name
     */
    @Nonnull
    public Component getDefaultNamePiece();
    
    /**
     * Get the additive modifier to be applied to the spell vehicle's package's base cost.
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
    
    /**
     * Get a name-ordered list of properties used by this spell vehicle.
     * 
     * @return a name-ordered list of properties used by this spell vehicle
     */
    public List<SpellProperty> getProperties();
    
    /**
     * Get a specific property of the spell vehicle.
     * 
     * @param name the name of the property to retrieve
     * @return the named property, or null if no such property is attached to this spell vehicle
     */
    @Nullable
    public SpellProperty getProperty(String name);
    
    /**
     * Get the value of a specific property of the spell vehicle.
     * 
     * @param name the name of the property value to retrieve
     * @return the named property's value, or zero if no such property is attached to this spell vehicle
     */
    public int getPropertyValue(String name);
    
    /**
     * Determine whether the spell vehicle deals its effect indirectly (i.e. via a projectile).
     * 
     * @return whether the spell vehicle deals its effect indirectly
     */
    public boolean isIndirect();
}
