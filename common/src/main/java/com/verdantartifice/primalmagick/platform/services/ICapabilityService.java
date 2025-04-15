package com.verdantartifice.primalmagick.platform.services;

import com.verdantartifice.primalmagick.common.capabilities.IEntitySwappers;
import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.capabilities.IManaStorage;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerArcaneRecipeBook;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerAttunements;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerCompanions;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerCooldowns;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerLinguistics;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerStats;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerWard;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTilePM;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface ICapabilityService {
    Optional<IPlayerKnowledge> knowledge(@Nullable Player player);
    Optional<IPlayerCooldowns> cooldowns(@Nullable Player player);
    Optional<IPlayerStats> stats(@Nullable Player player);
    Optional<IPlayerAttunements> attunements(@Nullable Player player);
    Optional<IPlayerCompanions> companions(@Nullable Player player);
    Optional<IPlayerWard> ward(@Nullable Player player);
    Optional<IPlayerLinguistics> linguistics(@Nullable Player player);
    Optional<IPlayerArcaneRecipeBook> arcaneRecipeBook(@Nullable Player player);

    Optional<IEntitySwappers> swappers(@Nullable Entity entity);

    /**
     * Attempts to get an item handler capability for the given side of the given position in the given world.
     * First searches for tiles that directly implement the capability, then attempts to wrap instances of the
     * vanilla inventory interface.
     *
     * @param level the level containing the desired tile entity
     * @param pos the position of the desired tile entity
     * @param face the side of the tile entity to be queried
     * @return an optional containing the item handler of the tile entity, or empty if no such capability could be found
     */
    Optional<IItemHandlerPM> itemHandler(@NotNull Level level, @NotNull BlockPos pos, @Nullable Direction face);
    Optional<IItemHandlerPM> itemHandler(@Nullable AbstractTilePM tile, @Nullable Direction face);

    /**
     * Attempts to get a mana storage capability for the given side of the given position in the given world.
     *
     * @param level the level containing the desired tile entity
     * @param pos the position of the desired tile entity
     * @param face the side of the tile entity to be queried
     * @return an optional containing the mana storage of the tile entity, or empty if no such capability could be found
     */
    Optional<IManaStorage<?>> manaStorage(@NotNull Level level, @NotNull BlockPos pos, @Nullable Direction face);
    Optional<IManaStorage<?>> manaStorage(@Nullable AbstractTilePM tile, @Nullable Direction face);
}
