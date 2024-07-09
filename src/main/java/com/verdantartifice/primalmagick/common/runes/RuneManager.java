package com.verdantartifice.primalmagick.common.runes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.research.keys.RuneEnchantmentKey;
import com.verdantartifice.primalmagick.common.research.keys.RuneEnchantmentPartialKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

/**
 * Primary access point for rune-related methods.
 * 
 * @author Daedalus4096
 */
public class RuneManager {
    private static final String RUNE_TAG_NAME = PrimalMagick.MODID + ":runes";
    
    /**
     * Gets the set of enchantments that can be replicated with runes.
     * 
     * @return the set of enchantments that can be replicated with runes
     */
    public static Set<Holder<Enchantment>> getRuneEnchantments(RegistryAccess registryAccess) {
        return registryAccess.registryOrThrow(RegistryKeysPM.RUNE_ENCHANTMENT_DEFINITIONS).stream().map(RuneEnchantmentDefinition::result).collect(Collectors.toUnmodifiableSet());
    }
    
    /**
     * Gets the list of enchantments that can be replicated with runes, sorted by display name.
     * 
     * @return the list of enchantments that can be replicated with runes, sorted by display name
     */
    public static List<Holder<Enchantment>> getRuneEnchantmentsSorted(RegistryAccess registryAccess) {
        return getRuneEnchantments(registryAccess).stream().sorted(Comparator.comparing(e -> Enchantment.getFullname(e, 1).getString())).collect(Collectors.toList());
    }
    
    /**
     * Calculate the map of enchantments and corresponding levels which are created by applying the given
     * combination of runes to the given item stack.
     * 
     * @param runes the runes for which to determine enchantments
     * @param stack the item stack to which the given runes are to be applied
     * @param player the player attempting to generate the rune enchantments
     * @param filterIncompatible whether detection should exclude incompatible enchantments
     * @return the map of rune enchantments and the levels at which they should be applied
     */
    @Nonnull
    public static Map<Holder<Enchantment>, Integer> getRuneEnchantments(RegistryAccess registryAccess, @Nullable List<Rune> runes, @Nullable ItemStack stack, @Nullable Player player, boolean filterIncompatible) {
        if (runes == null || runes.isEmpty() || stack == null || stack.isEmpty() || player == null || !checkLimits(runes)) {
            return Collections.emptyMap();
        }
        
        // Separate out the given runes by type
        List<VerbRune> verbRunes = runes.stream().filter(r -> r != null && r.getType() == RuneType.VERB).map(r -> (VerbRune)r).toList();
        List<NounRune> nounRunes = runes.stream().filter(r -> r != null && r.getType() == RuneType.NOUN).map(r -> (NounRune)r).toList();
        List<SourceRune> sourceRunes = runes.stream().filter(r -> r != null && r.getType() == RuneType.SOURCE).map(r -> (SourceRune)r).toList();
        int powerLevel = 1 + (int)runes.stream().filter(r -> r != null && r.getType() == RuneType.POWER).count();
        
        // Iterate through each combination of verb, noun, and source to find enchantments
        List<EnchantmentInstance> intermediate = new ArrayList<>();
        for (VerbRune verb : verbRunes) {
            for (NounRune noun : nounRunes) {
                for (SourceRune source : sourceRunes) {
                    // Intersect the sets of enchantments for each verb, noun, and source combination
                    Set<Holder<Enchantment>> possibleEnchantments = new HashSet<>();
                    possibleEnchantments.addAll(getVerbEnchantments(registryAccess, verb));
                    possibleEnchantments.retainAll(getNounEnchantments(registryAccess, noun));
                    possibleEnchantments.retainAll(getSourceEnchantments(registryAccess, source));
                    
                    for (Holder<Enchantment> possible : possibleEnchantments) {
                        // If the rune enchantment can be applied to the given item stack, is compatible with 
                        // those already found, it meets the minimum power level, and the player has any needed
                        // research, add the enchantment to the result set
                        Optional<AbstractRequirement<?>> reqOpt = getEnchantmentRequirement(registryAccess, possible);
                        if ( possible.value().canEnchant(stack) && 
                             (reqOpt.isEmpty() || reqOpt.get().isMetBy(player)) &&
                             powerLevel >= possible.value().getMinLevel() ) {
                            intermediate.add(new EnchantmentInstance(possible, Math.min(powerLevel, possible.value().getMaxLevel())));
                        }
                    }
                }
            }
        }
        
        // Sort enchantments first by their minimum XP cost (descending) and then by hash code (ascending) to ensure consistent results
        intermediate.sort(Comparator.<EnchantmentInstance>comparingInt(i -> i.enchantment.value().getMinCost(i.level)).reversed().thenComparingInt(i -> i.hashCode()));
        
        // Add intermediate enchantments to the result map, filtering out incompatible enchantments if appropriate
        Map<Holder<Enchantment>, Integer> retVal = new HashMap<>();
        intermediate.forEach(instance -> {
            if (!filterIncompatible || EnchantmentHelper.isEnchantmentCompatible(retVal.keySet(), instance.enchantment)) {
                retVal.put(instance.enchantment, instance.level);
            }
        });
        
        return retVal;
    }
    
    private static Set<Holder<Enchantment>> getVerbEnchantments(RegistryAccess registryAccess, VerbRune verb) {
        return registryAccess.registryOrThrow(RegistryKeysPM.RUNE_ENCHANTMENT_DEFINITIONS).stream().filter(def -> def.verb().equals(verb)).map(def -> def.result()).collect(Collectors.toUnmodifiableSet());
    }
    
    private static Set<Holder<Enchantment>> getNounEnchantments(RegistryAccess registryAccess, NounRune noun) {
        return registryAccess.registryOrThrow(RegistryKeysPM.RUNE_ENCHANTMENT_DEFINITIONS).stream().filter(def -> def.noun().equals(noun)).map(def -> def.result()).collect(Collectors.toUnmodifiableSet());
    }
    
    private static Set<Holder<Enchantment>> getSourceEnchantments(RegistryAccess registryAccess, SourceRune source) {
        return registryAccess.registryOrThrow(RegistryKeysPM.RUNE_ENCHANTMENT_DEFINITIONS).stream().filter(def -> def.source().equals(source)).map(def -> def.result()).collect(Collectors.toUnmodifiableSet());
    }
    
    private static Optional<AbstractRequirement<?>> getEnchantmentRequirement(RegistryAccess registryAccess, Holder<Enchantment> enchant) {
        return getRuneDefinition(registryAccess, enchant).flatMap(def -> def.requirementOpt());
    }
    
    /**
     * Check that none of the given runes exceed their per-type limits.
     * 
     * @param runes the runes to be checked
     * @return true if the list of runes is within allowed limits, false otherwise
     */
    public static boolean checkLimits(@Nonnull List<Rune> runes) {
        Map<ResourceLocation, Integer> counts = new HashMap<>();
        for (Rune rune : runes) {
            if (rune.hasLimit()) {
                counts.put(rune.getId(), counts.getOrDefault(rune.getId(), 0) + 1);
                if (counts.getOrDefault(rune.getId(), 0) > rune.getLimit()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Merge the two enchantment maps, taking the stronger one in case of a collision.  Enchantments in
     * the addition map will not be added if they are incompatible with those in the original map.
     * 
     * @param original the first enchantment map
     * @param addition the second enchantment map
     * @return the merged enchantment map
     */
    public static Map<Holder<Enchantment>, Integer> mergeEnchantments(@Nonnull Map<Holder<Enchantment>, Integer> original, @Nonnull Map<Holder<Enchantment>, Integer> addition) {
        // Start with the original map as a base
        Map<Holder<Enchantment>, Integer> retVal = new HashMap<>(original);
        
        for (Map.Entry<Holder<Enchantment>, Integer> entry : addition.entrySet()) {
            if (retVal.containsKey(entry.getKey())) {
                // If the original already contains the enchantment to be added, set its value to the higher of the two levels
                retVal.put(entry.getKey(), Math.max(original.getOrDefault(entry.getKey(), 0), entry.getValue()));
            } else if (EnchantmentHelper.isEnchantmentCompatible(original.keySet(), entry.getKey())) {
                // Only add the addition enchantment if it's compatible with all those in the current output set
                retVal.put(entry.getKey(), entry.getValue());
                
            }
        }
        
        return retVal;
    }
    
    /**
     * Determine whether the given item stack has had runes applied to it.
     * 
     * @param stack the item stack to query
     * @return true if the item stack has one or more runes applied, false otherwise
     */
    public static boolean hasRunes(@Nullable ItemStack stack) {
        if (stack == null || stack.isEmpty() || !stack.hasTag()) {
            return false;
        } else {
            return !stack.getTag().getList(RUNE_TAG_NAME, Tag.TAG_STRING).isEmpty();
        }
    }
    
    /**
     * Get the list of runes that have been applied to this item stack.
     * 
     * @param stack the item stack to query
     * @return the list of runes applied to the item; empty if none
     */
    @Nonnull
    public static List<Rune> getRunes(@Nullable ItemStack stack) {
        if (stack == null || stack.isEmpty() || !stack.hasTag()) {
            return Collections.emptyList();
        }
        
        List<Rune> retVal = new ArrayList<>();
        ListTag tagList = stack.getTag().getList(RUNE_TAG_NAME, Tag.TAG_STRING);
        for (int index = 0; index < tagList.size(); index++) {
            String tagStr = tagList.getString(index);
            Rune rune = Rune.getRune(ResourceLocation.parse(tagStr));
            if (rune != null) {
                retVal.add(rune);
            }
        }
        
        return retVal;
    }
    
    /**
     * Sets the list of runes applied to this item stack.
     * 
     * @param stack the item stack to modify
     * @param runes the list of runes to apply to the item
     */
    public static void setRunes(@Nullable ItemStack stack, @Nullable List<Rune> runes) {
        if (stack != null && !stack.isEmpty() && runes != null && !runes.isEmpty()) {
            ListTag tagList = new ListTag();
            for (Rune rune : runes) {
                if (rune != null) {
                    tagList.add(StringTag.valueOf(rune.getId().toString()));
                }
            }
            stack.addTagElement(RUNE_TAG_NAME, tagList);
        }
    }
    
    /**
     * Removes all runes from the given item stack.
     * 
     * @param stack the item stack to modify
     */
    public static void clearRunes(@Nullable ItemStack stack) {
        if (stack != null) {
            stack.removeTagKey(RUNE_TAG_NAME);
        }
    }
    
    /**
     * Determine whether the given enchantment has a rune combination defined for it.
     * 
     * @param enchant the enchantment to query
     * @return true if the enchantment has a rune combination defined for it, false otherwise
     */
    public static boolean hasRuneDefinition(RegistryAccess registryAccess, Enchantment enchant) {
        return getRuneEnchantments(registryAccess).contains(enchant);
    }
    
    /**
     * Get the rune combination definition for the given enchant, if any.
     * 
     * @param enchant the enchantment to query
     * @return the rune combination definition for the given enchant, or null if one was not registered
     */
    public static Optional<RuneEnchantmentDefinition> getRuneDefinition(RegistryAccess registryAccess, Holder<Enchantment> enchant) {
        return registryAccess.registryOrThrow(RegistryKeysPM.RUNE_ENCHANTMENT_DEFINITIONS).stream().filter(def -> def.result().value().equals(enchant.value())).findFirst();
    }
    
    public static boolean isRuneKnown(Player player, Holder<Enchantment> enchant, RuneType runeType) {
        return ResearchManager.isResearchComplete(player, new RuneEnchantmentKey(enchant)) || 
                ResearchManager.isResearchComplete(player, new RuneEnchantmentPartialKey(enchant, runeType));
    }
}
