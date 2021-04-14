package com.verdantartifice.primalmagic.common.capabilities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

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
    
    @CapabilityInject(IWorldEntitySwappers.class)
    public static final Capability<IWorldEntitySwappers> ENTITY_SWAPPERS = null;
    
    @CapabilityInject(IManaStorage.class)
    public static final Capability<IManaStorage> MANA_STORAGE = null;
    
    @Nullable
    public static IPlayerKnowledge getKnowledge(@Nonnull PlayerEntity player) {
        return player.getCapability(KNOWLEDGE, null).orElse(null);
    }
    
    @Nullable
    public static IPlayerCooldowns getCooldowns(@Nonnull PlayerEntity player) {
        return player.getCapability(COOLDOWNS, null).orElse(null);
    }
    
    @Nullable
    public static IPlayerStats getStats(@Nonnull PlayerEntity player) {
        return player.getCapability(STATS, null).orElse(null);
    }
    
    @Nullable
    public static IPlayerAttunements getAttunements(@Nonnull PlayerEntity player) {
        return player.getCapability(ATTUNEMENTS, null).orElse(null);
    }
    
    @Nullable
    public static IWorldEntitySwappers getEntitySwappers(@Nonnull World world) {
        return world.getCapability(ENTITY_SWAPPERS, null).orElse(null);
    }
    
    @Nullable
    public static IManaStorage getManaStorage(@Nonnull TileEntity tile) {
        return tile.getCapability(MANA_STORAGE, null).orElse(null);
    }
}
