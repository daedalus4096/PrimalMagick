package com.verdantartifice.primalmagick.common.wands;

import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Base interface for an item that can hold, but not necessarily cast, spells.
 *
 * @author Daedalus4096
 */
public interface ISpellContainer {
    int NO_SPELL_SELECTED = -1;
    int OTHER_HAND_SELECTED = -2;

    /**
     * Get the list of spell packages currently inscribed on the given wand stack.
     *
     * @param stack the wand stack to be queried
     * @return the list of spell packages currently inscribed
     */
    @NotNull
    List<SpellPackage> getSpells(@Nullable ItemStack stack);

    /**
     * Get the number of spell packages currently inscribed on the given wand stack.
     *
     * @param stack the wand stack to be queried
     * @return the number of spell packages currently inscribed
     */
    int getSpellCount(@Nullable ItemStack stack);

    /**
     * Get the text for the spell capacity of the given wand stack.
     *
     * @param stack the wand stack to be queried
     * @return the text for the spell capacity
     */
    Component getSpellCapacityText(@Nullable ItemStack stack);

    /**
     * Get the index of the currently selected inscribed spell package on the given wand stack.
     *
     * @param stack the wand stack to be queried
     * @return the zero-based index of the currently selected spell, or -1 if no spell is selected
     */
    int getActiveSpellIndex(@Nullable ItemStack stack);

    /**
     * Get the currently selected inscribed spell package on the given wand stack.
     *
     * @param stack the wand stack to be queried
     * @return the currently selected spell, or null if no spell is selected
     */
    @Nullable
    SpellPackage getActiveSpell(@Nullable ItemStack stack);

    /**
     * Get the index of the currently selected inscribed spell package on the given wand stack.
     *
     * @param stack the wand stack to be modified
     * @param index the zero-based index of the newly selected spell, or -1 if no spell is to be selected
     * @return true if the given index was valid for the given wand, false otherwise
     */
    boolean setActiveSpellIndex(@Nullable ItemStack stack, int index);

    /**
     * Determine if the given spell package can be inscribed onto the given wand stack.
     *
     * @param stack the wand stack to be queried
     * @param spell the spell package to be inscribed
     * @return true if the spell will fit on the wand, false otherwise
     */
    boolean canAddSpell(@Nullable ItemStack stack, @Nullable SpellPackage spell);

    /**
     * Add the given spell package to the given wand stack's list of inscribed spells.
     *
     * @param stack the wand stack to be modified
     * @param spell the spell package to be inscribed
     * @return true if the spell was successfully added, false otherwise
     */
    boolean addSpell(@Nullable ItemStack stack, @Nullable SpellPackage spell);

    /**
     * Remove all spell packages from the given wand stack's list of inscribed spells.
     *
     * @param stack the wand stack to be modified
     */
    void clearSpells(@Nullable ItemStack stack);
}
