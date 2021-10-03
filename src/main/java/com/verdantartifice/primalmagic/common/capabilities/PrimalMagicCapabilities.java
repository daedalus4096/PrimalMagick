package com.verdantartifice.primalmagic.common.capabilities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;

/**
 * Access point for all capabilities defined by the mod.  Capabilities are injected with the output 
 * of their registered factories post-registration.
 * 
 * @author Daedalus4096
 */
public class PrimalMagicCapabilities {
    @CapabilityInject(IPlayerKnowledge.class)
    public static final Capability<IPlayerKnowledge> KNOWLEDGE = null;
    
    @CapabilityInject(IPlayerCooldowns.class)
    public static final Capability<IPlayerCooldowns> COOLDOWNS = null;
    
    @CapabilityInject(IPlayerStats.class)
    public static final Capability<IPlayerStats> STATS = null;
    
    @CapabilityInject(IPlayerAttunements.class)
    public static final Capability<IPlayerAttunements> ATTUNEMENTS = null;
    
    @CapabilityInject(IPlayerCompanions.class)
    public static final Capability<IPlayerCompanions> COMPANIONS = null;
    
    @CapabilityInject(IPlayerArcaneRecipeBook.class)
    public static final Capability<IPlayerArcaneRecipeBook> ARCANE_RECIPE_BOOK = null;
    
    @CapabilityInject(IWorldEntitySwappers.class)
    public static final Capability<IWorldEntitySwappers> ENTITY_SWAPPERS = null;
    
    @CapabilityInject(IManaStorage.class)
    public static final Capability<IManaStorage> MANA_STORAGE = null;
    
    @CapabilityInject(ITileResearchCache.class)
    public static final Capability<ITileResearchCache> RESEARCH_CACHE = null;
    
    @Nonnull
    public static LazyOptional<IPlayerKnowledge> getKnowledge(@Nullable Player player) {
        return player == null ? LazyOptional.empty() : player.getCapability(KNOWLEDGE, null);
    }
    
    @Nullable
    public static IPlayerCooldowns getCooldowns(@Nullable Player player) {
        return player == null ? null : player.getCapability(COOLDOWNS, null).orElse(null);
    }
    
    @Nullable
    public static IPlayerStats getStats(@Nullable Player player) {
        return player == null ? null : player.getCapability(STATS, null).orElse(null);
    }
    
    @Nullable
    public static IPlayerAttunements getAttunements(@Nullable Player player) {
        return player == null ? null : player.getCapability(ATTUNEMENTS, null).orElse(null);
    }
    
    @Nullable
    public static IPlayerCompanions getCompanions(@Nullable Player player) {
        return player == null ? null : player.getCapability(COMPANIONS, null).orElse(null);
    }
    
    @Nonnull
    public static LazyOptional<IPlayerArcaneRecipeBook> getArcaneRecipeBook(@Nullable Player player) {
        return player == null ? LazyOptional.empty() : player.getCapability(ARCANE_RECIPE_BOOK);
    }
    
    @Nullable
    public static IWorldEntitySwappers getEntitySwappers(@Nonnull Level world) {
        return world.getCapability(ENTITY_SWAPPERS, null).orElse(null);
    }
    
    @Nullable
    public static IManaStorage getManaStorage(@Nonnull BlockEntity tile) {
        return tile.getCapability(MANA_STORAGE, null).orElse(null);
    }
    
    @Nonnull
    public static LazyOptional<ITileResearchCache> getResearchCache(@Nonnull BlockEntity tile) {
        return tile.getCapability(RESEARCH_CACHE);
    }
}
