package com.verdantartifice.primalmagick.common.wands;

import com.verdantartifice.primalmagick.common.attunements.AttunementManager;
import com.verdantartifice.primalmagick.common.attunements.AttunementThreshold;
import com.verdantartifice.primalmagick.common.attunements.AttunementType;
import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.effects.EffectsPM;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentHelperPM;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.items.armor.IManaDiscountGear;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
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
    DecimalFormat FRACTIONAL_MANA_FORMATTER = new DecimalFormat("######0.00");
    DecimalFormat WHOLE_MANA_FORMATTER = new DecimalFormat("#######");

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
     * @param stack    the wand stack to be queried
     * @param source   the type of mana to be queried
     * @param truncate whether to truncate any fractional part of the mana value
     * @return the text representation of the amount of centimana contained
     */
    default MutableComponent getManaText(@Nullable ItemStack stack, @Nullable Source source, boolean truncate) {
        int mana = this.getMana(stack, source);
        if (mana == IManaContainer.INFINITE_MANA) {
            // If the given stack has infinite mana, show the infinity symbol
            return Component.literal(Character.toString('\u221E'));
        } else {
            // Otherwise show the current whole mana value for that source from the stack's data
            double wholeMana = mana / 100.0D;
            double toDisplay = truncate ? (int)wholeMana : wholeMana;
            DecimalFormat formatter = (toDisplay == (int)toDisplay) ? WHOLE_MANA_FORMATTER : FRACTIONAL_MANA_FORMATTER;
            return Component.literal(formatter.format(toDisplay));
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
            return Component.literal(WHOLE_MANA_FORMATTER.format(mana / 100.0D));
        }
    }

    void setMana(@NotNull ItemStack stack, @NotNull Source source, int amount);

    /**
     * Add the given amount of the given type of centimana to the given wand stack, up to its maximum.
     *
     * @param stack  the wand stack to be modified
     * @param source the type of mana to be added
     * @param amount the amount of centimana to be added
     * @return the amount of leftover centimana that could not fit in the wand
     */
    default int addMana(ItemStack stack, Source source, int amount) {
        return this.addMana(stack, source, amount, this.getMaxMana(stack, source));
    }

    /**
     * Add the given amount of the given type of centimana to the given wand stack, up to the given maximum.
     *
     * @param stack  the wand stack to be modified
     * @param source the type of mana to be added
     * @param amount the amount of centimana to be added
     * @param max the maximum amount of mana the stack should have post-add
     * @return the amount of leftover centimana that could not fit in the wand
     */
    default int addMana(@Nullable ItemStack stack, @Nullable Source source, int amount, int max) {
        // If the parameters are invalid or the given wand stack has infinite mana, do nothing
        if (stack == null || source == null || this.getMaxMana(stack, source) == IManaContainer.INFINITE_MANA) {
            return 0;
        }

        // Otherwise, increment and set the new centimana total for the source into the wand's data, up to
        // the given centimana threshold, returning any leftover centimana that wouldn't fit. Ensure that
        // stored mana is only ever increased via this operation.
        int current = this.getMana(stack, source);
        int toStore = current + amount;
        int leftover = Math.max(toStore - max, 0);
        this.setMana(stack, source, Math.max(current, Math.min(toStore, max)));
        return leftover;
    }

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
    default int deductMana(@Nullable ItemStack stack, @Nullable Source source, int amount) {
        if (stack == null || source == null) {
            // If the parameters are invalid, do nothing
            return amount;
        } else if (this.getMaxMana(stack, source) == IManaContainer.INFINITE_MANA) {
            // If the given stack has infinite mana, no deduction need take place
            return 0;
        }

        // Otherwise, decrement and set the new centimana total for the source into the wand's data, up to
        // its maximum, returning any leftover centimana that couldn't be covered
        int toStore = this.getMana(stack, source) - amount;
        int leftover = Math.max(-toStore, 0);
        this.setMana(stack, source, Math.max(toStore, 0));
        return leftover;
    }

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
    default boolean consumeMana(ItemStack stack, Player player, Source source, int amount, HolderLookup.Provider registries) {
        if (stack == null || source == null) {
            return false;
        }
        if (this.containsMana(stack, player, source, amount, registries)) {
            // If the wand stack contains enough mana, process the consumption and return success
            if (this.getMaxMana(stack, source) != IManaContainer.INFINITE_MANA) {
                // Only actually consume something if the wand doesn't have infinite mana
                this.setMana(stack, source, this.getMana(stack, source) - (amount == 0 ? 0 : Math.max(1, this.getModifiedCost(stack, player, source, amount, registries))));
            }

            if (player != null) {
                ManaManager.recordManaConsumptionSideEffects(player, source, amount);
            }

            return true;
        } else {
            // Otherwise return failure
            return false;
        }
    }

    /**
     * Consume the given amounts of centimana from the given wand stack for the given player.  Takes into account any
     * cost modifiers.
     *
     * @param stack   the wand stack to be modified
     * @param player  the player doing the consuming, if applicable
     * @param sources the amount of each type of centimana to be consumed
     * @return true if sufficient centimana was present in the wand and successfully consumed, false otherwise
     */
    default boolean consumeMana(ItemStack stack, Player player, SourceList sources, HolderLookup.Provider registries) {
        if (stack == null || sources == null) {
            return false;
        }
        if (this.containsMana(stack, player, sources, registries)) {
            // If the wand stack contains enough mana, process the consumption and return success
            SourceList.Builder deltaBuilder = SourceList.builder();
            for (Source source : sources.getSources()) {
                int amount = sources.getAmount(source);
                int realAmount = amount / 100;
                if (this.getMaxMana(stack, source) != IManaContainer.INFINITE_MANA) {
                    // Only actually consume something if the wand doesn't have infinite mana
                    this.setMana(stack, source, this.getMana(stack, source) - this.getModifiedCost(stack, player, source, amount, registries));
                }

                if (player != null) {
                    // Record the spent mana statistic change with pre-discount mana
                    StatsManager.incrementValue(player, StatsPM.MANA_SPENT_TOTAL, realAmount);
                    StatsManager.incrementValue(player, source.getManaSpentStat(), realAmount);
                }

                // Compute the amount of temporary attunement to be added to the player
                deltaBuilder.with(source, Mth.floor(Math.sqrt(realAmount)));
            }
            SourceList attunementDeltas = deltaBuilder.build();
            if (player != null && !attunementDeltas.isEmpty()) {
                // Update attunements in a batch
                AttunementManager.incrementAttunement(player, AttunementType.TEMPORARY, attunementDeltas);
            }
            return true;
        } else {
            // Otherwise return failure
            return false;
        }
    }

    /**
     * Remove the given amount of the given type of centimana from the given wand stack.  Ignores any cost modifiers.
     *
     * @param stack  the wand stack to be modified
     * @param source the type of mana to be removed
     * @param amount the amount of mana to be removed
     * @return true if sufficient mana was present in the wand and successfully removed, false otherwise
     */
    default boolean removeManaRaw(ItemStack stack, Source source, int amount) {
        if (stack == null || source == null) {
            return false;
        }
        if (this.containsManaRaw(stack, source, amount)) {
            // If the wand stack contains enough mana, process the consumption and return success
            if (this.getMaxMana(stack, source) != IManaContainer.INFINITE_MANA) {
                // Only actually consume something if the wand doesn't have infinite mana
                this.setMana(stack, source, this.getMana(stack, source) - (amount == 0 ? 0 : Math.max(1, amount)));
            }

            return true;
        } else {
            // Otherwise return failure
            return false;
        }
    }

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
    default boolean containsMana(ItemStack stack, Player player, Source source, int amount, HolderLookup.Provider registries) {
        // A wand stack with infinite mana always contains the requested amount of mana
        return this.getMaxMana(stack, source) == IManaContainer.INFINITE_MANA || this.getMana(stack, source) >= this.getModifiedCost(stack, player, source, amount, registries);
    }

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

    /**
     * Get the base mana cost modifier to be applied to mana consumption, in whole percentage points, as determined by
     * the cap of the wand, if any.
     *
     * @param stack the wand stack to be queried
     * @return the base mana cost modifier to be applied to mana consumption
     */
    int getBaseCostModifier(@Nullable ItemStack stack);

    /**
     * Get the total mana cost modifier to be applied to mana consumption, in whole percentage points, from all factors
     * (e.g. wand cap, player gear, attunement).
     *
     * @param stack      the wand stack to be queried
     * @param player     the player consuming the mana
     * @param source     the type of mana being consumed
     * @param registries a registry lookup provider
     * @return the total mana cost modifier to be applied to mana consumption
     */
    default int getTotalCostModifier(ItemStack stack, @Nullable Player player, Source source, HolderLookup.Provider registries) {
        // Start with the base modifier, as determined by wand cap
        int modifier = this.getBaseCostModifier(stack);

        if (player != null) {
            // Add discounts from equipped player gear and enchantments
            int gearDiscount = 0;
            for (ItemStack gearStack : player.getAllSlots()) {
                if (gearStack.getItem() instanceof IManaDiscountGear discountItem) {
                    gearDiscount += discountItem.getManaDiscount(gearStack, player, source);
                }
                gearDiscount += (2 * EnchantmentHelperPM.getEnchantmentLevel(gearStack, EnchantmentsPM.MANA_EFFICIENCY, registries));
            }
            if (gearDiscount > 0) {
                modifier += gearDiscount;
            }

            // Add discounts from attuned sources
            if (AttunementManager.meetsThreshold(player, source, AttunementThreshold.MINOR)) {
                modifier += 5;
            }

            // Add discounts from temporary conditions
            if (player.hasEffect(EffectsPM.MANAFRUIT.getHolder())) {
                // 1% at amp 0, 3% at amp 1, 5% at amp 2, etc
                modifier += ((2 * player.getEffect(EffectsPM.MANAFRUIT.getHolder()).getAmplifier()) + 1);
            }

            // Subtract penalties from temporary conditions
            if (player.hasEffect(EffectsPM.MANA_IMPEDANCE.getHolder())) {
                // 5% at amp 0, 10% at amp 1, 15% at amp 2, etc
                modifier -= (5 * (player.getEffect(EffectsPM.MANA_IMPEDANCE.getHolder()).getAmplifier() + 1));
            }
        }

        return modifier;
    }

    /**
     * Compute the final, fully-modified mana cost for the given base cost.
     *
     * @param stack the wand stack to be queried
     * @param player the player consuming the mana
     * @param source the type of mana being consumed
     * @param baseCost the base amount of mana being consumed
     * @param registries a registry lookup provider
     * @return the final mana cost modified from the given base cost
     */
    default int getModifiedCost(@Nullable ItemStack stack, @Nullable Player player, @Nullable Source source, int baseCost, HolderLookup.Provider registries) {
        return (int)Math.floor(baseCost / (1 + (this.getTotalCostModifier(stack, player, source, registries) / 100.0D)));
    }

    /**
     * Compute the final, fully-modified mana cost for the given base cost.
     *
     * @param stack the wand stack to be queried
     * @param player the player consuming the mana
     * @param baseCost the base amount of mana being consumed for each source
     * @param registries a registry lookup provider
     * @return the final mana cost modified from the given base costs
     */
    default SourceList getModifiedCost(@Nullable ItemStack stack, @Nullable Player player, SourceList baseCost, HolderLookup.Provider registries) {
        SourceList retVal = SourceList.EMPTY;
        for (Source s : baseCost.getSources()) {
            retVal = retVal.set(s, this.getModifiedCost(stack, player, s, baseCost.getAmount(s), registries));
        }
        return retVal;
    }
}
