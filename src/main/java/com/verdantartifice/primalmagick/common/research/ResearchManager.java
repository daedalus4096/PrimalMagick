package com.verdantartifice.primalmagick.common.research;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.advancements.critereon.CriteriaTriggersPM;
import com.verdantartifice.primalmagick.common.affinities.AffinityManager;
import com.verdantartifice.primalmagick.common.attunements.AttunementManager;
import com.verdantartifice.primalmagick.common.attunements.AttunementType;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.crafting.recipe_book.ArcaneRecipeBookManager;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.keys.AbstractResearchKey;
import com.verdantartifice.primalmagick.common.research.keys.EntityScanKey;
import com.verdantartifice.primalmagick.common.research.keys.ItemScanKey;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Primary access point for research-related methods.
 * 
 * @author Daedalus4096
 */
public class ResearchManager {
    // Hash codes of items that must be crafted to complete one or more research stages
    private static final Set<Integer> CRAFTING_REFERENCES = new HashSet<>();
    
    // Set of unique IDs of players that need their research synced to their client
    private static final Set<UUID> SYNC_SET = ConcurrentHashMap.newKeySet();
    
    // Registry of all defined scan triggers
    private static final List<IScanTrigger> SCAN_TRIGGERS = new ArrayList<>();
    
    private static final ResearchEntryKey FIRST_STEPS = new ResearchEntryKey(ResearchEntries.FIRST_STEPS);
    
    public static Set<Integer> getAllCraftingReferences() {
        return Collections.unmodifiableSet(CRAFTING_REFERENCES);
    }
    
    public static boolean addCraftingReference(int reference) {
        return CRAFTING_REFERENCES.add(Integer.valueOf(reference));
    }
    
    static void clearCraftingReferences() {
        CRAFTING_REFERENCES.clear();
    }
    
    @Nullable
    public static Optional<ResearchEntry> getEntryForRecipe(RegistryAccess registryAccess, ResourceLocation recipeId) {
        return ResearchEntries.stream(registryAccess)
                .filter(entry -> entry.getAllRecipeIds().contains(recipeId))
                .findFirst();
    }
    
    public static boolean hasStartedProgression(Player player) {
        return FIRST_STEPS.isKnownBy(player);
    }
    
    public static boolean isRecipeVisible(ResourceLocation recipeId, Player player) {
        IPlayerKnowledge know = PrimalMagickCapabilities.getKnowledge(player).orElseThrow(() -> new IllegalStateException("No knowledge provider for player"));
        ResearchEntry entry = ResearchManager.getEntryForRecipe(player.level().registryAccess(), recipeId).orElse(null);
        if (entry == null) {
            // If the recipe has no controlling research, then assume it's not visible
            return false;
        }
        
        // First check to see if the current stage for the entry has the recipe listed
        int currentStageIndex = know.getResearchStage(entry.key());
        if (currentStageIndex == entry.stages().size()) {
            ResearchStage currentStage = entry.stages().get(currentStageIndex - 1);
            if (currentStage.recipes().contains(recipeId)) {
                return true;
            }
        } else if (currentStageIndex >= 0 && currentStageIndex < entry.stages().size()) {
            ResearchStage currentStage = entry.stages().get(currentStageIndex);
            if (currentStage.recipes().contains(recipeId)) {
                return true;
            }
        }
        
        // If that doesn't pan out, check to see if any unlocked addendum lists the recipe
        for (ResearchAddendum addendum : entry.addenda()) {
            if (addendum.completionRequirementOpt().isPresent() && addendum.recipes().contains(recipeId) && addendum.completionRequirementOpt().get().isMetBy(player)) {
                return true;
            }
        }
        
        // Otherwise, return false
        return false;
    }
    
    public static boolean isSyncScheduled(@Nullable Player player) {
        if (player == null) {
            return false;
        } else {
            return SYNC_SET.remove(player.getUUID());
        }
    }
    
    public static void scheduleSync(@Nullable Player player) {
        if (player != null) {
            SYNC_SET.add(player.getUUID());
        }
    }
    
    public static boolean hasPrerequisites(@Nullable Player player, @Nullable AbstractResearchKey<?> key) {
        if (player == null) {
            return false;
        }
        if (key == null) {
            return true;
        }
        if (key instanceof ResearchEntryKey entryKey) {
            Optional<Holder.Reference<ResearchEntry>> entryRefOpt = player.level().registryAccess().registryOrThrow(RegistryKeysPM.RESEARCH_ENTRIES).getHolder(entryKey.getRootKey());
            if (entryRefOpt.isEmpty() || entryRefOpt.get().get().parents().isEmpty()) {
                return true;
            } else {
                // Perform a strict completion check on the given entry's parent research
                return entryRefOpt.get().get().parents().stream().allMatch(k -> k.isKnownBy(player));
            }
        } else {
            return true;
        }
    }
    
    public static boolean isResearchStarted(@Nullable Player player, @Nonnull ResourceKey<ResearchEntry> rawKey) {
        return isResearchStarted(player, new ResearchEntryKey(rawKey));
    }
    
    public static boolean isResearchStarted(@Nullable Player player, @Nullable AbstractResearchKey<?> key) {
        if (player == null || key == null) {
            return false;
        }
        return PrimalMagickCapabilities.getKnowledge(player).map(k -> k.isResearchKnown(key)).orElse(false);
    }
    
    public static boolean isResearchComplete(@Nullable Player player, @Nonnull ResourceKey<ResearchEntry> rawKey) {
        return isResearchComplete(player, new ResearchEntryKey(rawKey));
    }
    
    public static boolean isResearchComplete(@Nullable Player player, @Nullable AbstractResearchKey<?> key) {
        if (player == null || key == null) {
            return false;
        }
        RegistryAccess registryAccess = player.level().registryAccess();
        return PrimalMagickCapabilities.getKnowledge(player).map(k -> k.isResearchComplete(registryAccess, key)).orElse(false);
    }
    
    public static boolean completeResearch(@Nullable Player player, @Nonnull ResourceKey<ResearchEntry> rawKey) {
        return completeResearch(player, new ResearchEntryKey(rawKey));
    }
    
    public static boolean completeResearch(@Nullable Player player, @Nullable AbstractResearchKey<?> key) {
        // Complete the given research and sync it to the player's client
        return completeResearch(player, key, true);
    }
    
    public static boolean completeResearch(@Nullable Player player, @Nullable AbstractResearchKey<?> key, boolean sync) {
        // Complete the given research, optionally syncing it to the player's client
        return completeResearch(player, key, sync, true, true);
    }
    
    public static boolean completeResearch(@Nullable Player player, @Nullable AbstractResearchKey<?> key, boolean sync, boolean showNewFlags, boolean showPopups) {
        // Repeatedly progress the given research until it is completed, optionally syncing it to the player's client
        boolean retVal = false;
        while (progressResearch(player, key, sync, showNewFlags, showPopups)) {
            retVal = true;
        }
        return retVal;
    }
    
    public static void forceGrantWithAllParents(@Nullable Player player, @Nonnull ResourceKey<ResearchEntry> rawKey) {
        forceGrantWithAllParents(player, new ResearchEntryKey(rawKey));
    }
    
    public static void forceGrantWithAllParents(@Nullable Player player, @Nullable ResearchEntryKey key) {
        if (player != null && key != null) {
            RegistryAccess registryAccess = player.level().registryAccess();
            PrimalMagickCapabilities.getKnowledge(player).ifPresent(knowledge -> {
                if (!knowledge.isResearchComplete(registryAccess, key)) {
                    ResearchEntry entry = ResearchEntries.getEntry(registryAccess, key);
                    if (entry != null) {
                        // Recursively force-grant all of this entry's parent entries, even if not all of them are required
                        entry.parents().forEach(parentKey -> forceGrantWithAllParents(player, parentKey));

                        for (ResearchStage stage : entry.stages()) {
                            // Force complete any requirements for any of the entry's stages
                            stage.completionRequirementOpt().ifPresent(req -> req.forceComplete(player));
                        }
                    }
                    
                    // Once all prerequisites are out of the way, complete this entry itself
                    completeResearch(player, key, true, true, false);
                    
                    // Mark as updated any research entry that has a stage which requires completion of this entry
                    registryAccess.registryOrThrow(RegistryKeysPM.RESEARCH_ENTRIES).forEach(searchEntry -> {
                        for (ResearchStage searchStage : searchEntry.stages()) {
                            if (searchStage.completionRequirementOpt().isPresent() && searchStage.completionRequirementOpt().get().contains(key)) {
                                knowledge.addResearchFlag(searchEntry.key(), IPlayerKnowledge.ResearchFlag.UPDATED);
                                break;
                            }
                        }
                    });
                }
            });
        }
    }
    
    public static void forceGrantParentsOnly(@Nullable Player player, @Nullable ResearchEntryKey key) {
        if (player != null && key != null) {
            RegistryAccess registryAccess = player.level().registryAccess();
            PrimalMagickCapabilities.getKnowledge(player).ifPresent(knowledge -> {
                if (!knowledge.isResearchComplete(registryAccess, key)) {
                    ResearchEntry entry = ResearchEntries.getEntry(registryAccess, key);
                    if (entry != null) {
                        // Recursively force-grant all of this entry's parent entries, even if not all of them are required
                        entry.parents().forEach(parentKey -> forceGrantWithAllParents(player, parentKey));

                        for (ResearchStage stage : entry.stages()) {
                            // Force complete any requirements for any of the entry's stages
                            stage.completionRequirementOpt().ifPresent(req -> req.forceComplete(player));
                        }
                    }
                }
            });
        }
    }
    
    public static void forceGrantAll(@Nullable Player player) {
        if (player != null) {
            player.level().registryAccess().registryOrThrow(RegistryKeysPM.RESEARCH_ENTRIES).forEach(entry -> forceGrantWithAllParents(player, entry.key()));
        }
    }
    
    public static void forceRevokeWithAllChildren(@Nullable Player player, @Nullable ResearchEntryKey key) {
        if (player != null && key != null) {
            RegistryAccess registryAccess = player.level().registryAccess();
            PrimalMagickCapabilities.getKnowledge(player).ifPresent(knowledge -> {
                if (knowledge.isResearchComplete(registryAccess, key)) {
                    // Revoke all child research of this entry
                    registryAccess.registryOrThrow(RegistryKeysPM.RESEARCH_ENTRIES).forEach(entry -> {
                        if (entry.parents().contains(key)) {
                            forceRevokeWithAllChildren(player, entry.key());
                        }
                    });
                    
                    // Once all children are revoked, revoke this entry itself
                    revokeResearch(player, key);
                }
            });
        }
    }
    
    public static boolean revokeResearch(@Nullable Player player, @Nullable ResearchEntryKey key) {
        // Revoke the given research and sync it to the player's client
        return revokeResearch(player, key, true);
    }
    
    public static boolean revokeResearch(@Nullable Player player, @Nullable ResearchEntryKey key, boolean sync) {
        // Remove the given research from the player's known list and optionally sync to the player's client
        if (player == null || key == null) {
            return false;
        }
        
        IPlayerKnowledge knowledge = PrimalMagickCapabilities.getKnowledge(player).orElse(null);
        if (knowledge == null) {
            return false;
        }
        
        // Remove all recipes that are unlocked by the given research from the player's arcane recipe book
        if (player instanceof ServerPlayer serverPlayer) {
            ResearchEntry entry = ResearchEntries.getEntry(player.level().registryAccess(), key);
            if (entry != null) {
                RecipeManager recipeManager = serverPlayer.level().getRecipeManager();
                Set<RecipeHolder<?>> recipesToRemove = entry.getAllRecipeIds().stream().map(r -> recipeManager.byKey(r).orElse(null)).filter(Objects::nonNull).collect(Collectors.toSet());
                ArcaneRecipeBookManager.removeRecipes(recipesToRemove, serverPlayer);
                serverPlayer.resetRecipes(recipesToRemove);
            }
        }

        knowledge.removeResearch(key);
        if (sync) {
            scheduleSync(player);
        }
        return true;
    }
    
    public static boolean progressResearch(@Nullable Player player, @Nonnull ResourceKey<ResearchEntry> rawKey) {
        return progressResearch(player, new ResearchEntryKey(rawKey));
    }
    
    public static boolean progressResearch(@Nullable Player player, @Nullable ResearchEntryKey key) {
        // Progress the given research to its next stage and sync to the player's client
        return progressResearch(player, key, true);
    }
    
    public static boolean progressResearch(@Nullable Player player, @Nullable ResearchEntryKey key, boolean sync) {
        // Progress the given research to its next stage and sync to the player's client
        return progressResearch(player, key, sync, true, true);
    }
    
    public static boolean progressResearch(@Nullable Player player, @Nullable AbstractResearchKey<?> key, boolean sync, boolean showNewFlags, boolean showPopups) {
        // Progress the given research to its next stage and optionally sync to the player's client
        if (player == null || key == null) {
            return false;
        }
        RegistryAccess registryAccess = player.level().registryAccess();

        IPlayerKnowledge knowledge = PrimalMagickCapabilities.getKnowledge(player).orElse(null);
        if (knowledge == null) {
            return false;
        }
        if (knowledge.isResearchComplete(registryAccess, key)) {
            // If the research is already complete, trigger advancement criteria if on server side, then abort
            if (player instanceof ServerPlayer serverPlayer) {
                CriteriaTriggersPM.RESEARCH_COMPLETED.trigger(serverPlayer, key);
            }
            return false;
        } else if (!hasPrerequisites(player, key)) {
            // If the player doesn't have the prerequisites, just abort
            return false;
        }
        
        // If the research is not started yet, start it
        boolean added = false;
        if (!knowledge.isResearchKnown(key)) {
            knowledge.addResearch(key);
            added = true;
        }
        
        ResearchEntry entry = key instanceof ResearchEntryKey entryKey ? ResearchEntries.getEntry(registryAccess, entryKey) : null;
        boolean entryComplete = true;   // Default to true for non-entry research (e.g. scan triggers)
        if (entry != null && !entry.stages().isEmpty()) {
            // Get the current stage number of the research entry
            ResearchStage currentStage = null;
            int currentStageNum = knowledge.getResearchStage(key);
            
            // Increment the current stage, unless the research was just added then skip this step (because that already 
            // incremented the stage from -1 to 0)
            if (!added) {
                currentStageNum++;
            }
            if (currentStageNum == (entry.stages().size() - 1) && !entry.stages().get(currentStageNum).hasPrerequisites()) {
                // If we've advanced to the final stage of the entry and it has no further prereqs (which it shouldn't), then
                // advance one more to be considered complete
                currentStageNum++;
            }
            currentStageNum = Math.min(currentStageNum, entry.stages().size());
            if (currentStageNum >= 0) {
                currentStage = entry.stages().get(Math.min(currentStageNum, entry.stages().size() - 1));
            }
            knowledge.setResearchStage(key, currentStageNum);
            
            // Determine whether the entry has been completed
            entryComplete = (currentStageNum >= entry.stages().size());
            
            if (currentStage != null) {
                // Process any attunement grants in the newly-reached stage
                SourceList attunements = currentStage.attunements();
                for (Source source : attunements.getSources()) {
                    int amount = attunements.getAmount(source);
                    if (amount > 0) {
                        AttunementManager.incrementAttunement(player, source, AttunementType.PERMANENT, amount);
                    }
                }
                
                // Add any unlocked recipes from the current stage to the player's arcane recipe book
                if (player instanceof ServerPlayer serverPlayer) {
                    RecipeManager recipeManager = serverPlayer.level().getRecipeManager();
                    Set<RecipeHolder<?>> recipesToUnlock = currentStage.recipes().stream().map(r -> recipeManager.byKey(r).orElse(null)).filter(Objects::nonNull).collect(Collectors.toSet());
                    ArcaneRecipeBookManager.addRecipes(recipesToUnlock, serverPlayer);
                    serverPlayer.awardRecipes(recipesToUnlock);
                }
                
                // Grant any sibling research from the current stage
                for (AbstractResearchKey<?> sibling : currentStage.siblings()) {
                    completeResearch(player, sibling, sync);
                }
                
                // Open any research to be revealed by the current stage
                for (ResearchEntryKey revelation : currentStage.revelations()) {
                    if (!knowledge.isResearchKnown(revelation)) {
                        knowledge.addResearch(revelation);
                        if (showPopups) {
                            knowledge.addResearchFlag(revelation, IPlayerKnowledge.ResearchFlag.POPUP);
                        }
                        knowledge.addResearchFlag(revelation, IPlayerKnowledge.ResearchFlag.NEW);
                    }
                }
            }
            
            if (entryComplete && !entry.addenda().isEmpty() && player instanceof ServerPlayer serverPlayer) {
                RecipeManager recipeManager = serverPlayer.level().getRecipeManager();
                for (ResearchAddendum addendum : entry.addenda()) {
                    if (addendum.completionRequirementOpt().isPresent() || addendum.completionRequirementOpt().get().isMetBy(player)) {
                        // Add any unlocked recipes from this entry's addenda to the player's arcane recipe book
                        Set<RecipeHolder<?>> recipesToUnlock = addendum.recipes().stream().map(r -> recipeManager.byKey(r).orElse(null)).filter(Objects::nonNull).collect(Collectors.toSet());
                        ArcaneRecipeBookManager.addRecipes(recipesToUnlock, serverPlayer);
                        serverPlayer.awardRecipes(recipesToUnlock);
                        
                        // Grant any sibling research from this entry's addenda
                        for (AbstractResearchKey<?> sibling : addendum.siblings()) {
                            completeResearch(player, sibling, sync);
                        }
                    }
                }
            }

            // Give the player experience for advancing their research
            if (!added) {
                player.giveExperiencePoints(5);
            }
        }
        
        if (entryComplete) {
            if (sync) {
                // If the entry has been completed and we're syncing, add the appropriate flags
                if (showPopups) {
                    knowledge.addResearchFlag(key, IPlayerKnowledge.ResearchFlag.POPUP);
                }
                if (showNewFlags) {
                    knowledge.addResearchFlag(key, IPlayerKnowledge.ResearchFlag.NEW);
                }
            }
            
            // Reveal any addenda that depended on this research
            registryAccess.registryOrThrow(RegistryKeysPM.RESEARCH_ENTRIES).forEach(searchEntry -> {
                if (!searchEntry.addenda().isEmpty() && knowledge.isResearchComplete(registryAccess, searchEntry.key())) {
                    for (ResearchAddendum addendum : searchEntry.addenda()) {
                        addendum.completionRequirementOpt().filter(req -> req.contains(key) && req.isMetBy(player)).ifPresent(req -> {
                            // Announce completion of the addendum
                            Component nameComp = Component.translatable(searchEntry.getNameTranslationKey());
                            player.sendSystemMessage(Component.translatable("event.primalmagick.add_addendum", nameComp));
                            knowledge.addResearchFlag(searchEntry.key(), IPlayerKnowledge.ResearchFlag.UPDATED);
                            
                            // Process attunement grants
                            SourceList attunements = addendum.attunements();
                            for (Source source : attunements.getSources()) {
                                int amount = attunements.getAmount(source);
                                if (amount > 0) {
                                    AttunementManager.incrementAttunement(player, source, AttunementType.PERMANENT, amount);
                                }
                            }
                            
                            // Add any unlocked recipes to the player's arcane recipe book
                            if (player instanceof ServerPlayer serverPlayer) {
                                RecipeManager recipeManager = serverPlayer.level().getRecipeManager();
                                Set<RecipeHolder<?>> recipesToUnlock = addendum.recipes().stream().map(r -> recipeManager.byKey(r).orElse(null)).filter(Objects::nonNull).collect(Collectors.toSet());
                                ArcaneRecipeBookManager.addRecipes(recipesToUnlock, serverPlayer);
                                serverPlayer.awardRecipes(recipesToUnlock);
                            }
                            
                            // Grant any unlocked sibling research
                            for (AbstractResearchKey<?> sibling : addendum.siblings()) {
                                completeResearch(player, sibling, sync);
                            }
                        });
                    }
                }
            });
            
            // If completing this entry finished its discipline, reveal any appropriate finale research
            if (entry != null) {
                entry.disciplineKeyOpt().ifPresent(disciplineKey -> {
                    ResearchDiscipline discipline = registryAccess.registryOrThrow(RegistryKeysPM.RESEARCH_DISCIPLINES).get(disciplineKey.getRootKey());
                    if (discipline != null) {
                        for (ResearchEntry finaleEntry : discipline.getFinaleEntries(registryAccess)) {
                            ResearchEntryKey finaleKey = finaleEntry.key();
                            if (!knowledge.isResearchKnown(finaleKey)) {
                                boolean shouldUnlock = finaleEntry.finales().stream().map(k -> registryAccess.registryOrThrow(RegistryKeysPM.RESEARCH_DISCIPLINES).get(k.getRootKey()))
                                        .filter(Objects::nonNull).flatMap(d -> d.getEntryStream(registryAccess)).filter(e -> e.finales().isEmpty() && !e.flags().finaleExempt()).allMatch(e -> e.isComplete(player));
                                if (shouldUnlock) {
                                    knowledge.addResearch(finaleKey);
                                    if (showPopups) {
                                        knowledge.addResearchFlag(finaleKey, IPlayerKnowledge.ResearchFlag.POPUP);
                                    }
                                    knowledge.addResearchFlag(finaleKey, IPlayerKnowledge.ResearchFlag.NEW);
                                }
                            }
                        }
                    }
                });
            }
            
            // Trigger any relevant advancements
            if (player instanceof ServerPlayer serverPlayer) {
                CriteriaTriggersPM.RESEARCH_COMPLETED.trigger(serverPlayer, key);
            }
        }
        
        // If syncing, queue it up for next tick
        if (sync) {
            scheduleSync(player);
        }

        return true;
    }
    
    public static boolean addKnowledge(Player player, KnowledgeType type, int points) {
        return addKnowledge(player, type, points, true);
    }
    
    public static boolean addKnowledge(Player player, KnowledgeType type, int points, boolean scheduleSync) {
        // Add the given number of knowledge points to the player and sync to their client
        IPlayerKnowledge knowledge = PrimalMagickCapabilities.getKnowledge(player).orElse(null);
        if (knowledge == null) {
            return false;
        }
        int levelsBefore = knowledge.getKnowledge(type);
        boolean success = knowledge.addKnowledge(type, points);
        if (!success) {
            return false;
        }
        if (points > 0) {
            int levelsAfter = knowledge.getKnowledge(type);
            int delta = levelsAfter - levelsBefore;
            if (type == KnowledgeType.OBSERVATION) {
                StatsManager.incrementValue(player, StatsPM.OBSERVATIONS_MADE, delta);
            } else if (type == KnowledgeType.THEORY) {
                StatsManager.incrementValue(player, StatsPM.THEORIES_FORMED, delta);
            }
            for (int index = 0; index < delta; index++) {
                // TODO send knowledge gain packet to player to show client effects for each level gained
            }
        }
        if (scheduleSync) {
            scheduleSync(player);
        }
        return true;
    }
    
    public static boolean registerScanTrigger(@Nullable IScanTrigger trigger) {
        if (trigger == null) {
            return false;
        }
        return SCAN_TRIGGERS.add(trigger);
    }
    
    public static void checkScanTriggers(ServerPlayer player, ItemLike itemProvider) {
        checkScanTriggersInner(player, itemProvider);
    }
    
    public static void checkScanTriggers(ServerPlayer player, EntityType<?> entityType) {
        checkScanTriggersInner(player, entityType);
    }
    
    private static void checkScanTriggersInner(ServerPlayer player, Object obj) {
        for (IScanTrigger trigger : SCAN_TRIGGERS) {
            if (trigger.matches(player, obj)) {
                trigger.onMatch(player, obj);
            }
        }
    }
    
    public static boolean hasScanTriggers(ServerPlayer player, ItemLike itemProvider) {
        return hasScanTriggersInner(player, itemProvider);
    }
    
    public static boolean hasScanTriggers(ServerPlayer player, EntityType<?> entityType) {
        return hasScanTriggersInner(player, entityType);
    }
    
    private static boolean hasScanTriggersInner(ServerPlayer player, Object obj) {
        for (IScanTrigger trigger : SCAN_TRIGGERS) {
            if (trigger.matches(player, obj)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isScanned(@Nullable ItemStack stack, @Nullable Player player) {
        if (stack == null || stack.isEmpty() || player == null) {
            return false;
        }
        Optional<SourceList> affinitiesOpt = AffinityManager.getInstance().getAffinityValues(stack, player.level());
        if (affinitiesOpt.isPresent()) {
            SourceList affinities = affinitiesOpt.get();
            if ((affinities == null || affinities.isEmpty()) && (!(player instanceof ServerPlayer) || !hasScanTriggers((ServerPlayer)player, stack.getItem()))) {
                // If the given itemstack has no affinities, consider it already scanned
                return true;
            }
            return new ItemScanKey(stack).isKnownBy(player);
        } else {
            // If the affinities for the item are not ready yet, temporarily consider the item scanned
            return true;
        }
    }
    
    public static CompletableFuture<Boolean> isScannedAsync(@Nullable ItemStack stack, @Nullable Player player) {
        if (stack == null || stack.isEmpty() || player == null) {
            return CompletableFuture.completedFuture(Boolean.FALSE);
        }
        
        return AffinityManager.getInstance().getAffinityValuesAsync(stack, player.level()).thenApply(affinities -> {
            if ((affinities == null || affinities.isEmpty()) && (!(player instanceof ServerPlayer) || !hasScanTriggers((ServerPlayer)player, stack.getItem()))) {
                // If the given itemstack has no affinities, consider it already scanned
                return true;
            }
            return new ItemScanKey(stack).isKnownBy(player);
        });
    }
    
    public static boolean isScanned(@Nullable EntityType<?> type, @Nullable Player player) {
        if (type == null || player == null) {
            return false;
        }
        Optional<SourceList> affinitiesOpt = AffinityManager.getInstance().getAffinityValues(type, player.level().registryAccess());
        if (affinitiesOpt.isPresent()) {
            SourceList affinities = affinitiesOpt.get();
            if ((affinities == null || affinities.isEmpty()) && (!(player instanceof ServerPlayer) || !hasScanTriggers((ServerPlayer)player, type))) {
                // If the given entity has no affinities, consider it already scanned
                return true;
            }
            return new EntityScanKey(type).isKnownBy(player);
        } else {
            // If the affinities for the entity are not ready yet, temporarily consider the entity scanned
            return true;
        }
    }
    
    public static CompletableFuture<Boolean> isScannedAsync(@Nullable EntityType<?> type, @Nullable Player player) {
        if (type == null || player == null) {
            return CompletableFuture.completedFuture(Boolean.FALSE);
        }
        
        return AffinityManager.getInstance().getAffinityValuesAsync(type, player.level().registryAccess()).thenApply(affinities -> {
            if ((affinities == null || affinities.isEmpty()) && (!(player instanceof ServerPlayer) || !hasScanTriggers((ServerPlayer)player, type))) {
                // If the given entity has no affinities, consider it already scanned
                return true;
            }
            return new EntityScanKey(type).isKnownBy(player);
        });
    }

    public static boolean setScanned(@Nullable ItemStack stack, @Nullable ServerPlayer player) {
        // Scan the given itemstack and sync the data to the player's client
        return setScanned(stack, player, true);
    }

    public static boolean setScanned(@Nullable ItemStack stack, @Nullable ServerPlayer player, boolean sync) {
        if (stack == null || stack.isEmpty() || player == null) {
            return false;
        }
        IPlayerKnowledge knowledge = PrimalMagickCapabilities.getKnowledge(player).orElse(null);
        if (knowledge == null) {
            return false;
        }
        
        // Check to see if any scan triggers need to be run for the item
        checkScanTriggers(player, stack.getItem());
        
        // Generate a research key for the itemstack and add that research to the player
        ItemScanKey key = new ItemScanKey(stack);
        if (key != null && knowledge.addResearch(key)) {
            // Determine how many observation points the itemstack is worth and add those to the player's knowledge
            getObservationPointsAsync(stack, player.getCommandSenderWorld()).thenAccept(obsPoints -> {
                if (obsPoints > 0) {
                    addKnowledge(player, KnowledgeType.OBSERVATION, obsPoints, false);
                }
            });
            
            // Increment the items analyzed stat
            StatsManager.incrementValue(player, StatsPM.ITEMS_ANALYZED);
            
            // Sync the research/knowledge changes to the player's client if requested
            if (sync) {
                knowledge.sync(player); // Sync immediately, rather than scheduling, for snappy arcanometer response
            }
            return true;
        } else {
            return false;
        }
    }
    
    public static boolean setScanned(@Nullable EntityType<?> type, @Nullable ServerPlayer player) {
        return setScanned(type, player, true);
    }
    
    public static boolean setScanned(@Nullable EntityType<?> type, @Nullable ServerPlayer player, boolean sync) {
        if (type == null || player == null) {
            return false;
        }
        IPlayerKnowledge knowledge = PrimalMagickCapabilities.getKnowledge(player).orElse(null);
        if (knowledge == null) {
            return false;
        }
        
        // Generate a research key for the entity type and add that research to the player
        EntityScanKey key = new EntityScanKey(type);
        if (key != null && knowledge.addResearch(key)) {
            // Determine how many observation points the entity is worth and add those to the player's knowledge
            getObservationPointsAsync(type, player.getCommandSenderWorld()).thenAccept(obsPoints -> {
                if (obsPoints > 0) {
                    addKnowledge(player, KnowledgeType.OBSERVATION, obsPoints, false);
                }
            });
            
            // Increment the entities analyzed stat
            StatsManager.incrementValue(player, StatsPM.ENTITIES_ANALYZED);
            
            // Check to see if any scan triggers need to be run for the entity
            checkScanTriggers(player, type);
            
            // Sync the research/knowledge changes to the player's client if requested
            if (sync) {
                knowledge.sync(player); // Sync immediately, rather than scheduling, for snappy arcanometer response
            }
            return true;
        } else {
            return false;
        }
    }

    public static int setAllScanned(@Nullable ServerPlayer player) {
        if (player == null) {
            return 0;
        }
        IPlayerKnowledge knowledge = PrimalMagickCapabilities.getKnowledge(player).orElse(null);
        if (knowledge == null) {
            return 0;
        }
        int count = 0;
        ItemScanKey key;
        ItemStack stack;
        
        // Iterate over all registered items in the game
        List<CompletableFuture<Integer>> obsPointsFutures = new ArrayList<>();
        for (Item item : ForgeRegistries.ITEMS) {
            // Generate a research key for the itemstack and add that research to the player
            stack = new ItemStack(item);
            if (!stack.isEmpty()) { // Skip over air
                key = new ItemScanKey(stack);
                if (key != null && knowledge.addResearch(key)) {
                    count++;
        
                    // Determine how many observation points the itemstack is worth and queue them up to add to the player's knowledge
                    obsPointsFutures.add(getObservationPointsAsync(stack, player.getCommandSenderWorld()));
                    
                    // Check to see if any scan triggers need to be run for the item
                    checkScanTriggers(player, item);
                }
            }
        }
        
        // Once all items are processed, then add any accrued observation points to the player's knowledge
        Util.sequence(obsPointsFutures).thenAccept(obsPointsList -> {
            int obsPoints = obsPointsList.stream().mapToInt(i -> i).sum();
            if (obsPoints > 0) {
                addKnowledge(player, KnowledgeType.OBSERVATION, obsPoints, false);
            }
        });
        
        // If any items were successfully scanned, sync the research/knowledge changes to the player's client
        if (count > 0) {
            knowledge.sync(player); // Sync immediately, rather than scheduling, for snappy arcanometer response
        }
        
        // Return the number of items successfully scanned
        return count;
    }

    private static CompletableFuture<Integer> getObservationPointsAsync(@Nonnull ItemStack stack, @Nonnull Level world) {
        // Calculate observation points for the itemstack based on its affinities
        return AffinityManager.getInstance().getAffinityValuesAsync(stack, world).thenApply(ResearchManager::getObservationPoints);
    }
    
    private static CompletableFuture<Integer> getObservationPointsAsync(@Nonnull EntityType<?> type, @Nonnull Level level) {
        // Get affinities from affinity manager for entity type
        return AffinityManager.getInstance().getAffinityValuesAsync(type, level.registryAccess()).thenApply(ResearchManager::getObservationPoints);
    }
    
    private static int getObservationPoints(@Nullable SourceList affinities) {
        if (affinities == null || affinities.isEmpty()) {
            return 0;
        }
        double total = 0.0D;
        for (Source source : affinities.getSources()) {
            // Not all sources are worth the same amount of observation points
            total += (affinities.getAmount(source) * source.getObservationMultiplier());
        }
        if (total > 0.0D) {
            total = Math.sqrt(total);
        }
        
        // Round up to ensure that any item with affinities generates at least one observation point
        return Mth.ceil(total);
    }
}
