package com.verdantartifice.primalmagick.common.wands;

import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Base interface for an item that can provide mana for spellcasting.
 *
 * @author Daedalus4096
 */
public interface IManaContainer {
    /**
     * Get the amount of centimana for the given source which is contained in the given wand stack.
     *
     * @param stack  the wand stack to be queried
     * @param source the type of mana to be queried
     * @return the amount of centimana contained
     */
    int getMana(@Nullable ItemStack stack, @Nullable Source source);

    /**
     * Get the text representation of centimana for the given source which is contained in the given wand stack.
     *
     * @param stack  the wand stack to be queried
     * @param source the type of mana to be queried
     * @return the text representation of the amount of centimana contained
     */
    MutableComponent getManaText(@Nullable ItemStack stack, @Nullable Source source);

    /**
     * Get the centimana amounts of all types of mana contained in the given wand stack.
     *
     * @param stack the wand stack to be queried
     * @return the amount of each type of mana contained
     */
    @NotNull
    SourceList getAllMana(@Nullable ItemStack stack);

    /**
     * Get the maximum amount of centimana that can be held by the given wand stack.
     *
     * @param stack the wand stack whose maximum mana to return
     * @return the maximum amount of centimana that can be held by the given wand stack
     */
    int getMaxMana(@Nullable ItemStack stack);

    /**
     * Get the text representation of the maximum amount of centimana that can be held by the given wand stack.
     *
     * @param stack the wand stack whose maximum mana to return
     * @return the text representation of the maximum amount of centimana that can be held by the given wand stack
     */
    MutableComponent getMaxManaText(@Nullable ItemStack stack);

    /**
     * Add the given amount of the given type of centimana to teh given wand stack, up to its maximum.
     *
     * @param stack  the wand stack to be modified
     * @param source the type of mana to be added
     * @param amount the amount of centimana to be added
     * @return the amount of leftover centimana that could not fit in the wand
     */
    int addMana(@Nullable ItemStack stack, @Nullable Source source, int amount);

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
    boolean containsMana(@Nullable ItemStack stack, @Nullable Player player, @Nullable SourceList sources, HolderLookup.Provider registries);

    /**
     * Determine if the given wand stack contains the given amount of the given type of centimana.  Ignores any cost
     * modifiers.
     *
     * @param stack  the wand stack to be queried
     * @param source the type of mana being queried
     * @param amount the amount of mana required
     * @return true if sufficient mana is present, false otherwise
     */
    boolean containsManaRaw(@Nullable ItemStack stack, @Nullable Source source, int amount);
}
