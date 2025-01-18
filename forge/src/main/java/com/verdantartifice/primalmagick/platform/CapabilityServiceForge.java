package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.capabilities.CapabilitiesForge;
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
import com.verdantartifice.primalmagick.common.capabilities.ItemStackHandlerPMForge;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTilePM;
import com.verdantartifice.primalmagick.platform.services.ICapabilityService;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import java.util.Optional;

public class CapabilityServiceForge implements ICapabilityService {
    @Override
    public Optional<IPlayerKnowledge> knowledge(Player player) {
        return player == null ? Optional.empty() : player.getCapability(CapabilitiesForge.KNOWLEDGE).resolve();
    }

    @Override
    public Optional<IPlayerCooldowns> cooldowns(Player player) {
        return player == null ? Optional.empty() : player.getCapability(CapabilitiesForge.COOLDOWNS).resolve();
    }

    @Override
    public Optional<IPlayerStats> stats(Player player) {
        return player == null ? Optional.empty() : player.getCapability(CapabilitiesForge.STATS).resolve();
    }

    @Override
    public Optional<IPlayerAttunements> attunements(Player player) {
        return player == null ? Optional.empty() : player.getCapability(CapabilitiesForge.ATTUNEMENTS).resolve();
    }

    @Override
    public Optional<IPlayerCompanions> companions(Player player) {
        return player == null ? Optional.empty() : player.getCapability(CapabilitiesForge.COMPANIONS).resolve();
    }

    @Override
    public Optional<IPlayerWard> ward(Player player) {
        return player == null ? Optional.empty() : player.getCapability(CapabilitiesForge.WARD).resolve();
    }

    @Override
    public Optional<IPlayerLinguistics> linguistics(Player player) {
        return player == null ? Optional.empty() : player.getCapability(CapabilitiesForge.LINGUISTICS).resolve();
    }

    @Override
    public Optional<IPlayerArcaneRecipeBook> arcaneRecipeBook(Player player) {
        return player == null ? Optional.empty() : player.getCapability(CapabilitiesForge.ARCANE_RECIPE_BOOK).resolve();
    }

    @Override
    public Optional<IEntitySwappers> swappers(Entity entity) {
        return entity == null ? Optional.empty() : entity.getCapability(CapabilitiesForge.ENTITY_SWAPPERS).resolve();
    }

    @Override
    public Optional<IItemHandlerPM> itemHandler(AbstractTilePM tile, Direction face) {
        if (tile == null) {
            return Optional.empty();
        }
        return tile.getCapability(ForgeCapabilities.ITEM_HANDLER, face).map(forgeHandler -> new ItemStackHandlerPMForge(forgeHandler, tile));
    }
}
