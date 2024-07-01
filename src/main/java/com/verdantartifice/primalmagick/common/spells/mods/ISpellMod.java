package com.verdantartifice.primalmagick.common.spells.mods;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.spells.ISpellComponent;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
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
public interface ISpellMod extends ISpellComponent, INBTSerializable<CompoundTag> {
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
