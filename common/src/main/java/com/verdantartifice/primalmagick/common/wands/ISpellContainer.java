package com.verdantartifice.primalmagick.common.wands;

import com.google.common.collect.ImmutableList;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
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
     * Get the list of spell packages currently inscribed on the given spell container stack.
     *
     * @param stack the spell container stack to be queried
     * @return the list of spell packages currently inscribed
     */
    @NotNull
    default List<SpellPackage> getSpells(@Nullable ItemStack stack) {
        // Deserialize the list of inscribed spells from the given container stack's data
        return stack == null ? ImmutableList.of() : ImmutableList.copyOf(stack.getOrDefault(DataComponentsPM.SPELL_PACKAGE_LIST.get(), ImmutableList.of()));
    }

    /**
     * Get the number of spell packages currently inscribed on the given spell container stack.
     *
     * @param stack the spell container stack to be queried
     * @return the number of spell packages currently inscribed
     */
    default int getSpellCount(@Nullable ItemStack stack) {
        if (stack != null) {
            return stack.getOrDefault(DataComponentsPM.SPELL_PACKAGE_LIST.get(), ImmutableList.of()).size();
        } else {
            return 0;
        }
    }

    /**
     * Get the text for the spell capacity of the given spell container stack.
     *
     * @param stack the spell container stack to be queried
     * @return the text for the spell capacity
     */
    Component getSpellCapacityText(@Nullable ItemStack stack);

    /**
     * Get the index of the currently selected inscribed spell package on the given spell container stack.
     *
     * @param stack the spell container stack to be queried
     * @return the zero-based index of the currently selected spell, or -1 if no spell is selected
     */
    default int getActiveSpellIndex(@Nullable ItemStack stack) {
        return stack == null ? ISpellContainer.NO_SPELL_SELECTED : stack.getOrDefault(DataComponentsPM.ACTIVE_SPELL_INDEX.get(), ISpellContainer.NO_SPELL_SELECTED);
    }

    /**
     * Get the currently selected inscribed spell package on the given spell container stack.
     *
     * @param stack the spell container stack to be queried
     * @return the currently selected spell, or null if no spell is selected
     */
    @Nullable
    default SpellPackage getActiveSpell(@Nullable ItemStack stack) {
        // Deserialize the active inscribed spell from the given container stack's NBT data
        SpellPackage retVal = null;
        if (stack != null) {
            List<SpellPackage> spellList = stack.get(DataComponentsPM.SPELL_PACKAGE_LIST.get());
            int index = this.getActiveSpellIndex(stack);
            if (spellList != null && index >= 0 && index < spellList.size()) {
                retVal = spellList.get(index);
            }
        }
        return retVal;
    }

    /**
     * Get the index of the currently selected inscribed spell package on the given spell container stack.
     *
     * @param stack the spell container stack to be modified
     * @param index the zero-based index of the newly selected spell, or -1 if no spell is to be selected
     * @return true if the given index was valid for the given spell container, false otherwise
     */
    default boolean setActiveSpellIndex(@Nullable ItemStack stack, int index) {
        if (stack == null) {
            return false;
        } else if (index == ISpellContainer.NO_SPELL_SELECTED || index == ISpellContainer.OTHER_HAND_SELECTED || (index >= 0 && index < this.getSpells(stack).size())) {
            stack.set(DataComponentsPM.ACTIVE_SPELL_INDEX.get(), index);
            return true;
        }
        return false;
    }

    /**
     * Determine if the given spell package can be inscribed onto the given spell container stack.
     *
     * @param stack the spell container stack to be queried
     * @param spell the spell package to be inscribed
     * @return true if the spell will fit on the spell container, false otherwise
     */
    boolean canAddSpell(@Nullable ItemStack stack, @Nullable SpellPackage spell);

    /**
     * Add the given spell package to the given spell container stack's list of inscribed spells.
     *
     * @param stack the spell container stack to be modified
     * @param spell the spell package to be inscribed
     * @return true if the spell was successfully added, false otherwise
     */
    default boolean addSpell(@Nullable ItemStack stack, @Nullable SpellPackage spell) {
        if (stack != null && spell != null && this.canAddSpell(stack, spell)) {
            // Save the given spell into the container stack's data
            stack.set(DataComponentsPM.SPELL_PACKAGE_LIST.get(), ImmutableList.<SpellPackage>builder().addAll(this.getSpells(stack)).add(spell).build());
            return true;
        } else {
            return false;
        }
    }

    /**
     * Remove all spell packages from the given spell container stack's list of inscribed spells.
     *
     * @param stack the spell container stack to be modified
     */
    default void clearSpells(@Nullable ItemStack stack) {
        if (stack != null) {
            stack.remove(DataComponentsPM.SPELL_PACKAGE_LIST.get());
            stack.remove(DataComponentsPM.ACTIVE_SPELL_INDEX.get());
        }
    }
}
