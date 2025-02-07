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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.VanillaInventoryCodeHooks;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    public Optional<IItemHandlerPM> itemHandler(@NotNull Level level, @NotNull BlockPos pos, @Nullable Direction face) {
        Optional<Pair<IItemHandler, Object>> optional = VanillaInventoryCodeHooks.getItemHandler(level, pos.getX(), pos.getY(), pos.getZ(), face);
        Pair<IItemHandler, Object> pair = optional.orElse(null);
        if (pair != null && pair.getLeft() != null) {
            // If the tile entity directly provides an item handler capability, return that
            if (pair.getLeft() instanceof IItemHandlerPM castHandler) {
                return Optional.of(castHandler);
            } else {
                return Optional.of(new ItemStackHandlerPMForge(pair.getLeft(), null));
            }
        } else if (level.getBlockEntity(pos) instanceof Container container) {
            // If the tile entity does not provide an item handler but does have an inventory, return a wrapper around that
            return Optional.ofNullable(Services.ITEM_HANDLERS.wrap(container, face));
        } else {
            // If the tile entity does not have an inventory at all, return null
            return Optional.empty();
        }
    }

    @Override
    public Optional<IItemHandlerPM> itemHandler(AbstractTilePM tile, Direction face) {
        if (tile == null || tile.getLevel() == null) {
            return Optional.empty();
        } else {
            return this.itemHandler(tile.getLevel(), tile.getBlockPos(), face);
        }
    }
}
