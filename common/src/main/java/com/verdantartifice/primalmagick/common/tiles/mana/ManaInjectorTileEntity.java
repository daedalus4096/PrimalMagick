package com.verdantartifice.primalmagick.common.tiles.mana;

import com.verdantartifice.primalmagick.common.mana.network.IManaConsumer;
import com.verdantartifice.primalmagick.common.mana.network.RouteTable;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.tiles.BlockEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTilePM;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public abstract class ManaInjectorTileEntity extends AbstractTilePM implements IManaConsumer {
    public ManaInjectorTileEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypesPM.MANA_INJECTOR.get(), pos, state);
    }

    @Override
    public boolean canConsume(@NotNull Source source) {
        return false;
    }

    @Override
    public int receiveMana(@NotNull Source source, int maxReceive, boolean simulate) {
        return 0;
    }

    @Override
    public int getNetworkRange() {
        return 0;
    }

    @Override
    public int getManaThroughput() {
        return 0;
    }

    @Override
    public @NotNull RouteTable getRouteTable() {
        return null;
    }
}
