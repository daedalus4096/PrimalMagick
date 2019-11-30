package com.verdantartifice.primalmagic.common.capabilities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class PrimalMagicCapabilities {
    @CapabilityInject(IPlayerKnowledge.class)
    public static final Capability<IPlayerKnowledge> KNOWLEDGE = null;
    
    @CapabilityInject(IPlayerCooldowns.class)
    public static final Capability<IPlayerCooldowns> COOLDOWNS = null;
    
    @Nullable
    public static IPlayerKnowledge getKnowledge(@Nonnull PlayerEntity player) {
        return player.getCapability(KNOWLEDGE, null).orElse(null);
    }
    
    @Nullable
    public static IPlayerCooldowns getCooldowns(@Nonnull PlayerEntity player) {
        return player.getCapability(COOLDOWNS, null).orElse(null);
    }
}
