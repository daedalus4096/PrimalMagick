package com.verdantartifice.primalmagick.common.wands;

import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.sources.Sources;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;

/**
 * Base interface for an item that can provide mana for spellcasting. The mana is
 * stored internally as centimana (hundredths of mana points).
 *
 * @author Daedalus4096
 */
public interface IManaContainer {
    int INFINITE_MANA = -1;
    DecimalFormat MANA_FORMATTER = new DecimalFormat("#######.##");

    ManaStorage getManaStorage(ItemStack stack);

    /**
     * Get the amount of centimana for the given source which is contained in the given wand stack.
     *
     * @param stack  the wand stack to be queried
     * @param source the type of mana to be queried
     * @return the amount of centimana contained
     */
    default int getMana(@Nullable ItemStack stack, @Nullable Source source) {
        if (this.getMaxMana(stack, source) == IManaContainer.INFINITE_MANA) {
            // If the given stack has infinite mana, return that
            return IManaContainer.INFINITE_MANA;
        } else {
            // Otherwise get the current centimana for that source from the stack's data
            int retVal = 0;
            if (stack != null && source != null) {
                retVal = this.getManaStorage(stack).getManaStored(source);
            }
            return retVal;
        }
    }

    /**
     * Get the text representation of centimana for the given source which is contained in the given wand stack.
     *
     * @param stack  the wand stack to be queried
     * @param source the type of mana to be queried
     * @return the text representation of the amount of centimana contained
     */
    default MutableComponent getManaText(@Nullable ItemStack stack, @Nullable Source source) {
        int mana = this.getMana(stack, source);
        if (mana == IManaContainer.INFINITE_MANA) {
            // If the given stack has infinite mana, show the infinity symbol
            return Component.literal(Character.toString('\u221E'));
        } else {
            // Otherwise show the current whole mana value for that source from the stack's data
            return Component.literal(MANA_FORMATTER.format(mana / 100.0D));
        }
    }

    /**
     * Get the centimana amounts of all types of mana contained in the given wand stack.
     *
     * @param stack the wand stack to be queried
     * @return the amount of each type of mana contained
     */
    @NotNull
    default SourceList getAllMana(@Nullable ItemStack stack) {
        SourceList retVal = SourceList.EMPTY;
        SourceList stored = this.getManaStorage(stack).getAllManaStored();
        for (Source source : Sources.getAllSorted()) {
            if (this.getMaxMana(stack, source) == IManaContainer.INFINITE_MANA) {
                // If the stack has infinite mana, set that into the returned source list (not merge; it would keep the default zero)
                retVal = retVal.set(source, IManaContainer.INFINITE_MANA);
            } else {
                // Otherwise, merge the current centimana into the returned source list
                retVal = retVal.merge(source, stored.getAmount(source));
            }
        }
        return retVal;
    }

    /**
     * Get the maximum amount of centimana that can be held by the given wand stack.
     *
     * @param stack  the wand stack whose maximum mana to return
     * @param source
     * @return the maximum amount of centimana that can be held by the given wand stack
     */
    int getMaxMana(@Nullable ItemStack stack, @Nullable Source source);

    /**
     * Get the text representation of the maximum amount of centimana that can be held by the given wand stack.
     *
     * @param stack the wand stack whose maximum mana to return
     * @return the text representation of the maximum amount of centimana that can be held by the given wand stack
     */
    default MutableComponent getMaxManaText(@Nullable ItemStack stack, @Nullable Source source) {
        int mana = stack == null || source == null ? 0 : this.getMaxMana(stack, source);
        if (mana == IManaContainer.INFINITE_MANA) {
            // If the given wand stack has infinite mana, show the infinity symbol
            return Component.literal(Character.toString('\u221E'));
        } else {
            // Otherwise show the max centimana for that source from the stack's data
            return Component.literal(MANA_FORMATTER.format(mana / 100.0D));
        }
    }

    /**
     * Add the given amount of the given type of centimana to the given wand stack, up to its maximum.
     *
     * @param stack  the wand stack to be modified
     * @param source the type of mana to be added
     * @param amount the amount of centimana to be added
     * @return the amount of leftover centimana that could not fit in the wand
     */
    int addMana(@Nullable ItemStack stack, @Nullable Source source, int amount);

    int addMana(@Nullable ItemStack stack, @Nullable Source source, int amount, int max);

    /**
     * Deduct the given amount of the given type of centimana from the given wand stack, to a minimum of zero.  Intended
     * to be used when deducting mana from the whole of the player's equipment, where another piece of gear can cover
     * whatever's missing from this stack.
     *
     * @param stack  the wand stack to be modified
     * @param source the type of mana to be deducted
     * @param amount the amount of centimana to be deducted
     * @return the amount of leftover centimana that could not be deducted
     */
    int deductMana(@Nullable ItemStack stack, @Nullable Source source, int amount);

    /**
     * Consume the given amount of the given type of centimana from the given wand stack for the given player.  Takes
     * into account any cost modifiers.
     *
     * @param stack  the wand stack to be modified
     * @param player the player doing the consuming, if applicable
     * @param source the type of mana to be consumed
     * @param amount the amount of centimana to be consumed
     * @return true if sufficient centimana was present in the wand and successfully consumed, false otherwise
     */
    boolean consumeMana(@Nullable ItemStack stack, @Nullable Player player, @Nullable Source source, int amount, HolderLookup.Provider registries);

    /**
     * Consume the given amounts of centimana from the given wand stack for the given player.  Takes into account any
     * cost modifiers.
     *
     * @param stack   the wand stack to be modified
     * @param player  the player doing the consuming, if applicable
     * @param sources the amount of each type of centimana to be consumed
     * @return true if sufficient centimana was present in the wand and successfully consumed, false otherwise
     */
    boolean consumeMana(@Nullable ItemStack stack, @Nullable Player player, @Nullable SourceList sources, HolderLookup.Provider registries);

    /**
     * Remove the given amount of the given type of centimana from the given wand stack.  Ignores any cost modifiers.
     *
     * @param stack  the wand stack to be modified
     * @param source the type of mana to be removed
     * @param amount the amount of mana to be removed
     * @return true if sufficient mana was present in the wand and successfully removed, false otherwise
     */
    boolean removeManaRaw(@Nullable ItemStack stack, @Nullable Source source, int amount);

    /**
     * Determine if the given wand stack contains the given amount of the given type of centimana for the given player.  Takes
     * into account any cost modifiers.
     *
     * @param stack  the wand stack to be queried
     * @param player the player doing the check, if applicable
     * @param source the type of mana being queried
     * @param amount the amount of centimana required
     * @return true if sufficient centimana is present, false otherwise
     */
    boolean containsMana(@Nullable ItemStack stack, @Nullable Player player, @Nullable Source source, int amount, HolderLookup.Provider registries);

    /**
     * Determine if the given wand stack contains the given amounts of centimana for the given player.  Takes into account
     * any cost modifiers.
     *
     * @param stack   the wand stack to be queried
     * @param player  the player doing the check, if applicable
     * @param sources the amount of each type of centimana required
     * @return true if sufficient centimana is present, false otherwise
     */
    default boolean containsMana(@Nullable ItemStack stack, @Nullable Player player, @Nullable SourceList sources, HolderLookup.Provider registries) {
        for (Source source : sources.getSources()) {
            if (!this.containsMana(stack, player, source, sources.getAmount(source), registries)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determine if the given wand stack contains the given amount of the given type of centimana.  Ignores any cost
     * modifiers.
     *
     * @param stack  the wand stack to be queried
     * @param source the type of mana being queried
     * @param amount the amount of mana required
     * @return true if sufficient mana is present, false otherwise
     */
    default boolean containsManaRaw(@Nullable ItemStack stack, @Nullable Source source, int amount) {
        // A wand stack with infinite mana always contains the requested amount of mana
        return this.getMaxMana(stack, source) == IManaContainer.INFINITE_MANA || this.getMana(stack, source) >= amount;
    }
}
