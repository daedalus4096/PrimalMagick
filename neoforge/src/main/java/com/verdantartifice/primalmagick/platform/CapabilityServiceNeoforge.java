package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.capabilities.CapabilitiesNeoforge;
import com.verdantartifice.primalmagick.common.capabilities.IEntitySwappers;
import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerArcaneRecipeBook;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerAttunements;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerCompanions;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerCooldowns;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerLinguistics;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerStats;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerWard;
import com.verdantartifice.primalmagick.common.capabilities.ItemStackHandlerPMNeoforge;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTilePM;
import com.verdantartifice.primalmagick.platform.services.ICapabilityService;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.capabilities.Capabilities;

import java.util.Optional;

public class CapabilityServiceNeoforge implements ICapabilityService {
    @Override
    public Optional<IPlayerKnowledge> knowledge(Player player) {
        return player == null ? Optional.empty() : Optional.of(player.getData(CapabilitiesNeoforge.KNOWLEDGE));
    }

    @Override
    public Optional<IPlayerCooldowns> cooldowns(Player player) {
        return player == null ? Optional.empty() : Optional.of(player.getData(CapabilitiesNeoforge.COOLDOWNS));
    }

    @Override
    public Optional<IPlayerStats> stats(Player player) {
        return player == null ? Optional.empty() : Optional.of(player.getData(CapabilitiesNeoforge.STATS));
    }

    @Override
    public Optional<IPlayerAttunements> attunements(Player player) {
        return player == null ? Optional.empty() : Optional.of(player.getData(CapabilitiesNeoforge.ATTUNEMENTS));
    }

    @Override
    public Optional<IPlayerCompanions> companions(Player player) {
        return player == null ? Optional.empty() : Optional.of(player.getData(CapabilitiesNeoforge.COMPANIONS));
    }

    @Override
    public Optional<IPlayerWard> ward(Player player) {
        return player == null ? Optional.empty() : Optional.of(player.getData(CapabilitiesNeoforge.WARD));
    }

    @Override
    public Optional<IPlayerLinguistics> linguistics(Player player) {
        return player == null ? Optional.empty() : Optional.of(player.getData(CapabilitiesNeoforge.LINGUISTICS));
    }

    @Override
    public Optional<IPlayerArcaneRecipeBook> arcaneRecipeBook(Player player) {
        return player == null ? Optional.empty() : Optional.of(player.getData(CapabilitiesNeoforge.ARCANE_RECIPE_BOOK));
    }

    @Override
    public Optional<IEntitySwappers> swappers(Entity entity) {
        return entity == null ? Optional.empty() : Optional.of(entity.getData(CapabilitiesNeoforge.ENTITY_SWAPPERS));
    }

    @Override
    public Optional<IItemHandlerPM> itemHandler(AbstractTilePM tile, Direction face) {
        if (tile == null || tile.getLevel() == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(tile.getLevel().getCapability(Capabilities.ItemHandler.BLOCK, tile.getBlockPos(), face)).map(
                neoforgeHandler -> new ItemStackHandlerPMNeoforge(neoforgeHandler, tile));
    }
}
