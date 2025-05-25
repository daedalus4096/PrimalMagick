package com.verdantartifice.primalmagick.common.wands;

import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;

/**
 * Primary access point for mana related methods for a player. Takes into account all equipped gear.
 *
 * @author Daedalus4096
 */
public class ManaManager {
    private static final DecimalFormat MANA_FORMATTER = new DecimalFormat("#######.##");

    /**
     * Get the centimana for the given source which is contained in the given player's equipment.
     *
     * @param player the player whose equipment is to be queried
     * @param source the type of mana to be queried
     * @return the amount of centimana contained
     */
    public static int getMana(@Nullable Player player, @Nullable Source source) {
        int retVal = 0;
        if (player != null && source != null) {
            if (player.getMainHandItem().getItem() instanceof IManaContainer mainHandContainer) {
                int mainHandAmount = mainHandContainer.getMana(player.getMainHandItem(), source);
                if (mainHandAmount == IManaContainer.INFINITE_MANA) {
                    return IManaContainer.INFINITE_MANA;
                }
                retVal += mainHandAmount;
            }
            if (player.getOffhandItem().getItem() instanceof IManaContainer offHandContainer) {
                int offHandAmount = offHandContainer.getMana(player.getOffhandItem(), source);
                if (offHandAmount == IManaContainer.INFINITE_MANA) {
                    return IManaContainer.INFINITE_MANA;
                }
                retVal += offHandAmount;
            }
        }
        return retVal;
    }

    /**
     * Get the text representation of centimana for the given source which is contained in the given player's equipment.
     *
     * @param player the player whose equipment is to be queried
     * @param source the type of mana to be queried
     * @return the text representation of the amount of centimana contained
     */
    public static @NotNull MutableComponent getManaText(@Nullable Player player, @Nullable Source source) {
        int mana = getMana(player, source);
        if (mana == IManaContainer.INFINITE_MANA) {
            // If the given player has infinite mana, show the infinity symbol
            return Component.literal(Character.toString('\u221E'));
        } else {
            // Otherwise show the current whole mana value for that source from the stack's data
            return Component.literal(MANA_FORMATTER.format(mana / 100.0D));
        }
    }

    /**
     * Get the centimana amounts of all types of mana contained in the given player's equipment.
     *
     * @param player the player whose equipment is to be queried
     * @param wandStack the current player's wand stack
     * @return the amount of each type of mana contained
     */
    public static @NotNull SourceList getAllMana(@Nullable Player player, @Nullable ItemStack wandStack) {
        // TODO Stub
        return SourceList.EMPTY;
    }

    /**
     * Get the maximum amount of centimana that can be held by the given player's equipment.
     *
     * @param player the player whose equipment is to be queried
     * @param wandStack the current player's wand stack
     * @param source the source of mana to be queried
     * @return the maximum amount of centimana that can be held by the given player's equipment
     */
    public static int getMaxMana(@Nullable Player player, @Nullable ItemStack wandStack, @Nullable Source source) {
        // TODO Stub
        return 0;
    }

    /**
     * Get the text representation of the maximum amount of centimana that can be held by the given player's equipment.
     *
     * @param player the player whose eqiupment is to be queried
     * @param wandStack the current player's wand stack
     * @param source the source of mana to be queried
     * @return the text representation of the maximum amount of centimana that can be held by the given player's equipment
     */
    public static @NotNull MutableComponent getMaxManaText(@Nullable Player player, @Nullable ItemStack wandStack, @Nullable Source source) {
        // TODO Stub
        return Component.empty();
    }

    /**
     * Add the given amount of the given type of centimana to the given player's equipment, up to their maximum.
     *
     * @param player the player doing the adding
     * @param wandStack the wand stack to be modified
     * @param source the type of mana to be added
     * @param amount the amount of centimana to be added
     * @return the amount of leftover centimana that could not fit in the wand
     */
    public static int addMana(@Nullable Player player, @Nullable ItemStack wandStack, @Nullable Source source, int amount) {
        // TODO Stub
        return 0;
    }

    /**
     * Consume the given amount of centimana from the given player's equipment.  Takes into account any
     * cost modifiers.
     *
     * @param player the player doing the consuming
     * @param wandStack the wand stack to be modified
     * @param source the type of mana to be consumed
     * @param amount the amount of mana to be consumed
     * @param registries active registry lookup provider
     * @return true if sufficient centimana was present in the wand and successfully consumed, false otherwise
     */
    public static boolean consumeMana(@Nullable Player player, @Nullable ItemStack wandStack, @Nullable Source source, int amount, HolderLookup.Provider registries) {
        // TODO Stub
        return false;
    }

    /**
     * Consume the given amounts of centimana from the given player's equipment.  Takes into account any
     * cost modifiers.
     *
     * @param player the player doing the consuming
     * @param wandStack the wand stack to be modified
     * @param sources the amount of each type of centimana to be consumed
     * @param registries active registry lookup provider
     * @return true if sufficient centimana was present in the wand and successfully consumed, false otherwise
     */
    public static boolean consumeMana(@Nullable Player player, @Nullable ItemStack wandStack, @Nullable SourceList sources, HolderLookup.Provider registries) {
        // TODO Stub
        return false;
    }

    /**
     * Remove the given amount of the given type of centimana from the given player's equipment.  Ignores any cost
     * modifiers.
     *
     * @param player the player whose equipment is to be modified
     * @param wandStack the wand stack to be modified
     * @param source the type of mana to be removed
     * @param amount the amount of mana to be removed
     * @return true if sufficient mana was present in the wand and successfully removed, false otherwise
     */
    public static boolean removeManaRaw(@Nullable Player player, @Nullable ItemStack wandStack, @Nullable Source source, int amount) {
        // TODO Stub
        return false;
    }

    /**
     * Determine if the given player has the given amount of centimana in their equipment.  Takes into account
     * any cost modifiers.
     *
     * @param player the player to be queried
     * @param wandStack the wand stack to be queried
     * @param source the source of centimana required
     * @param amount the amount of centimana required
     * @param registries active registry lookup provider
     * @return true if sufficient mana is present, false otherwise
     */
    public static boolean containsMana(@Nullable Player player, @Nullable ItemStack wandStack, @Nullable Source source, int amount, HolderLookup.Provider registries) {
        // TODO Stub
        return false;
    }

    /**
     * Determine if the given player has the given amounts of centimana in their equipment.  Takes into account
     * any cost modifiers.
     *
     * @param player the player to be queried
     * @param wandStack the wand stack to be queried
     * @param sources the amount of each type of centimana required
     * @param registries active registry lookup provider
     * @return true if sufficient mana is present, false otherwise
     */
    public static boolean containsMana(@Nullable Player player, @Nullable ItemStack wandStack, @Nullable SourceList sources, HolderLookup.Provider registries) {
        // TODO Stub
        return false;
    }

    /**
     * Determine if the given player has the given amount of the given type of centimana in their equipment. Ignores any
     * cost modifiers.
     *
     * @param player the player to be queried
     * @param wandStack the wand stack to be queried
     * @param source the type of mana being queried
     * @param amount the amount of mana required
     * @return true if sufficient mana is present, false otherwise
     */
    public static boolean containsManaRaw(@Nullable Player player, @Nullable ItemStack wandStack, @Nullable Source source, int amount) {
        // TODO Stub
        return false;
    }

    private static @NotNull ItemStack getOffhandStack(@NotNull Player player, @NotNull ItemStack wandStack) {
        if (wandStack == player.getOffhandItem()) { // Reference comparison intended
            return player.getMainHandItem();
        } else {
            return player.getOffhandItem();
        }
    }
}
