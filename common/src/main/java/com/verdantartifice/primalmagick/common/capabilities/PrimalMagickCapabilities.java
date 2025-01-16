package com.verdantartifice.primalmagick.common.capabilities;

import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Access point for all capabilities defined by the mod.  Capabilities are injected with the output 
 * of their registered factories post-registration.
 * 
 * @author Daedalus4096
 */
public class PrimalMagickCapabilities {
    public static final Capability<IWorldEntitySwappers> ENTITY_SWAPPERS = CapabilityManager.get(new CapabilityToken<>(){});

    @Nullable
    public static IWorldEntitySwappers getEntitySwappers(@Nonnull Level world) {
        return world.getCapability(ENTITY_SWAPPERS, null).orElse(null);
    }
}
