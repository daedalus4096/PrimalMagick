package com.verdantartifice.primalmagick.common.spells.vehicles;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.spells.ISpellComponent;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.SpellPropertyConfiguration;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Primary interface for a spell vehicle.  Spell vehicles are what determine the target of a spell
 * and carry the spell package from the caster to that target.  Vehicles may have properties which
 * alter their behavior, but most do not.  Spell vehicles sometimes also modify the base cost of
 * the spell.
 * 
 * @author Daedalus4096
 */
public interface ISpellVehicle extends ISpellComponent {
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
     * Get the additive modifier to be applied to the spell vehicle's package's base cost.
     * 
     * @return the additive modifier for the spell package's cost
     */
    public int getBaseManaCostModifier(SpellPropertyConfiguration properties);
    
    /**
     * Get the multiplicative modifier to be applied to the spell mod's package's total cost.
     * 
     * @return the multiplicative modifier for the spell package's cost
     */
    public int getManaCostMultiplier(SpellPropertyConfiguration properties);
    
    /**
     * Determine whether the spell vehicle deals its effect indirectly (i.e. via a projectile).
     * 
     * @return whether the spell vehicle deals its effect indirectly
     */
    public boolean isIndirect();
}
