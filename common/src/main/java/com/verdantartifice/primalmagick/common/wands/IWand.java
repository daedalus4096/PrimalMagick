package com.verdantartifice.primalmagick.common.wands;

import com.verdantartifice.primalmagick.common.crafting.IWandTransform;
import com.verdantartifice.primalmagick.common.crafting.WandTransforms;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Base interface for a wand.  Wands store mana for use in crafting and, optionally, casting spells.
 * They are replenished by drawing from mana fonts or being charged in a wand charger.  The wand's mana
 * is stored internally as centimana (hundredths of mana points).
 * 
 * @author Daedalus4096
 */
public interface IWand {
    /**
     * Get the amount of centimana for the given source which is contained in the given wand stack.
     * 
     * @param stack the wand stack to be queried
     * @param source the type of mana to be queried
     * @return the amount of centimana contained
     */
    int getMana(@Nullable ItemStack stack, @Nullable Source source);
    
    /**
     * Get the text representation of centimana for the given source which is contained in the given wand stack.
     * 
     * @param stack the wand stack to be queried
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
     * @param stack the wand stack to be modified
     * @param source the type of mana to be added
     * @param amount the amount of centimana to be added
     * @return the amount of leftover centimana that could not fit in the wand
     */
    int addMana(@Nullable ItemStack stack, @Nullable Source source, int amount);
    
    /**
     * Consume the given amount of the given type of centimana from the given wand stack for the given player.  Takes
     * into account any cost modifiers.
     * 
     * @param stack the wand stack to be modified
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
     * @param stack the wand stack to be modified
     * @param player the player doing the consuming, if applicable
     * @param sources the amount of each type of centimana to be consumed
     * @return true if sufficient centimana was present in the wand and successfully consumed, false otherwise
     */
    boolean consumeMana(@Nullable ItemStack stack, @Nullable Player player, @Nullable SourceList sources, HolderLookup.Provider registries);
    
    /**
     * Remove the given amount of the given type of centimana from the given wand stack.  Ignores any cost modifiers.
     * 
     * @param stack the wand stack to be modified
     * @param source the type of mana to be removed
     * @param amount the amount of mana to be removed
     * @return true if sufficient mana was present in the wand and successfully removed, false otherwise
     */
    boolean removeManaRaw(@Nullable ItemStack stack, @Nullable Source source, int amount);
    
    /**
     * Determine if the given wand stack contains the given amount of the given type of centimana for the given player.  Takes
     * into account any cost modifiers.
     * 
     * @param stack the wand stack to be queried
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
     * @param stack the wand stack to be queried
     * @param player the player doing the check, if applicable
     * @param sources the amount of each type of centimana required
     * @return true if sufficient centimana is present, false otherwise
     */
    boolean containsMana(@Nullable ItemStack stack, @Nullable Player player, @Nullable SourceList sources, HolderLookup.Provider registries);
    
    /**
     * Determine if the given wand stack contains the given amount of the given type of centimana.  Ignores any cost
     * modifiers.
     * 
     * @param stack the wand stack to be queried
     * @param source the type of mana being queried
     * @param amount the amount of mana required
     * @return true if sufficient mana is present, false otherwise
     */
    boolean containsManaRaw(@Nullable ItemStack stack, @Nullable Source source, int amount);
    
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
    int getTotalCostModifier(@Nullable ItemStack stack, @Nullable Player player, @Nullable Source source, HolderLookup.Provider registries);

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
    int getModifiedCost(@Nullable ItemStack stack, @Nullable Player player, @Nullable Source source, int baseCost, HolderLookup.Provider registries);

    /**
     *
     * @param stack the wand stack to be queried
     * @param player the player consuming the mana
     * @param baseCost the base amount of mana being consumed for each source
     * @param registries a registry lookup provider
     * @return the final mana cost modified from the given base costs
     */
    SourceList getModifiedCost(@Nullable ItemStack stack, @Nullable Player player, SourceList baseCost, HolderLookup.Provider registries);
    
    /**
     * Get the amount of centimana to siphon from a mana font when channeling it.
     * 
     * @param stack the wand stack to be queried
     * @return the amount of centimana to siphon from mana fonts
     */
    int getSiphonAmount(@Nullable ItemStack stack);

    /**
     * Clear any stored position data for the last interacted-with tile.
     * 
     * @param wandStack the wand stack to be modified
     */
    void clearPositionInUse(@NotNull ItemStack wandStack);

    /**
     * Store the given position data into the given wand stack.
     * 
     * @param wandStack the wand stack to be modified
     * @param pos the position data is to be stored
     */
    void setPositionInUse(@NotNull ItemStack wandStack, @NotNull BlockPos pos);

    /**
     * Get the position currently being interacted with by the given wand stack.
     * 
     * @param wandStack the wand stack to be queried
     * @return the position currently being interacted with, or null if none was found
     */
    @Nullable
    BlockPos getPositionInUse(@NotNull ItemStack wandStack);
    
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
    
    /**
     * Determine if the given wand stack has a glamour applied (i.e. if its appearance differs from that 
     * of its construction).
     * 
     * @param stack the wand stack to be queried
     * @return true if the wand has a glamour applied, false otherwise
     */
    boolean isGlamoured(@Nullable ItemStack stack);

    default InteractionResult onWandUseFirst(ItemStack stack, UseOnContext context) {
        // Only process on server side
        Level world = context.getLevel();
        if (world.isClientSide) {
            return InteractionResult.PASS;
        }

        // Bypass wand functionality if the player is sneaking
        if (context.getPlayer().isShiftKeyDown()) {
            return InteractionResult.PASS;
        }

        context.getPlayer().startUsingItem(context.getHand());

        // If the mouseover target is a wand-sensitive block, trigger that initial interaction
        BlockState bs = context.getLevel().getBlockState(context.getClickedPos());
        if (bs.getBlock() instanceof IInteractWithWand wandable) {
            return wandable.onWandRightClick(context.getItemInHand(), context.getLevel(), context.getPlayer(), context.getClickedPos(), context.getClickedFace());
        }

        // If the mouseover target is a wand-sensitive tile entity, trigger that initial interaction
        BlockEntity tile = context.getLevel().getBlockEntity(context.getClickedPos());
        if (tile != null && tile instanceof IInteractWithWand wandable) {
            return wandable.onWandRightClick(context.getItemInHand(), context.getLevel(), context.getPlayer(), context.getClickedPos(), context.getClickedFace());
        }

        // Otherwise, see if the mouseover target is a valid target for wand transformation
        for (IWandTransform transform : WandTransforms.getAll()) {
            if (transform.isValid(context.getLevel(), context.getPlayer(), context.getClickedPos())) {
                if (!context.getPlayer().mayUseItemAt(context.getClickedPos(), context.getClickedFace(), context.getItemInHand())) {
                    return InteractionResult.FAIL;
                } else {
                    // If so, save its position for future channeling
                    this.setPositionInUse(stack, context.getClickedPos());
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }
}
