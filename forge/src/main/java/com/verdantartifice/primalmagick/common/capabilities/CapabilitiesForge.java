package com.verdantartifice.primalmagick.common.capabilities;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilitiesForge {
    public static final Capability<IPlayerKnowledge> KNOWLEDGE = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<IPlayerCooldowns> COOLDOWNS = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<IPlayerStats> STATS = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<IPlayerAttunements> ATTUNEMENTS = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<IPlayerCompanions> COMPANIONS = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<IPlayerWard> WARD = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<IPlayerLinguistics> LINGUISTICS = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<ITileResearchCache> RESEARCH_CACHE = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<IManaStorage<?>> MANA_STORAGE = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<IPlayerArcaneRecipeBook> ARCANE_RECIPE_BOOK = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<IEntitySwappers> ENTITY_SWAPPERS = CapabilityManager.get(new CapabilityToken<>(){});

    @Nonnull
    public static LazyOptional<ITileResearchCache> getResearchCache(@Nonnull BlockEntity tile) {
        return tile.getCapability(RESEARCH_CACHE);
    }

    @Nonnull
    public static LazyOptional<IManaStorage<?>> getManaStorage(@Nonnull BlockEntity tile) {
        return tile.getCapability(MANA_STORAGE);
    }
}
